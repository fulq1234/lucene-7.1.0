package com.ldgx.eshop.lucene.util.searcher;

import java.io.IOException;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.QueryParser.Operator;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

public class SearcherUtil2 {
	
	private String[] ids = {"1","2","3","4","5","6"};
	private String[] emails = {"aa@itat.org","bb@itat.org","cc@itat.org","dd@itat.org","ee@itat.org","gg@itat.org"};
	private String[] content = {"hello world aa@itat.org","come on bb@itat.org","jiazhang cc@itat.org","fale dd@itat.org","qita ee@itat.org","xuexiao gg@itat.org"};
	private Date[] dates = null;
	
	private int[] attachs = {2,3,1,4,5,5};
	private String[] names = {"aa bb","bb","cc","dd","ee","gg"};

	private Directory directory = null;
	
	private Map<String,Float> scores = new HashMap<String,Float>();
	

	private IndexReader reader;
	
	public SearcherUtil2() {
		setDates();
		scores.put("itat.org", 2.0f);
		scores.put("zttc.edu", 1.5f);
		//directory = new RAMDirectory();//内存Directory
		try {
			directory = FSDirectory.open(Paths.get("E:\\lucene-db\\i3"));
			index();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void setDates() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		dates = new Date[ids.length];
		try {
			dates[0] = sdf.parse("2010-02-19");
			dates[1] = sdf.parse("2010-01-11");
			dates[2] = sdf.parse("2010-09-19");
			dates[3] = sdf.parse("2010-12-22");
			dates[4] = sdf.parse("2010-12-10");
			dates[5] = sdf.parse("2010-05-19");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 建立索引
	 */
	public void index() {
		
		//2.创建IndexWriter
		IndexWriter writer = null;
		try {
			writer = new IndexWriter(directory, new IndexWriterConfig(new StandardAnalyzer()));
			
			//删除以前的
			writer.deleteAll();
			
			//2.Document
			Document doc = null;			
			for(int i =0;i<ids.length;i++) {
				doc = new Document();
				doc.add(new Field("id",ids[i],TextField.TYPE_STORED));
				doc.add(new Field("email",emails[i],TextField.TYPE_STORED));
				doc.add(new Field("content",content[i],TextField.TYPE_NOT_STORED));
				doc.add(new NumericDocValuesField("attach",attachs[i]));//�洢����
				doc.add(new NumericDocValuesField("date",dates[i].getTime()));//�洢ʱ��date
				doc.add(new Field("name",names[i],TextField.TYPE_STORED));
				
				//��Ȩ
				String et = emails[i].substring(emails[i].lastIndexOf("@") +1);
				//System.out.println(et);
				
				if(scores.containsKey(et)) {
					
				}
				
				writer.addDocument(doc);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	
	
	/**
	 * 得到IndexSearcher
	 * @return
	 */
	public IndexSearcher getSearcher() {
		
		try {
			if(reader == null) {
				reader = DirectoryReader.open(directory);
			}else {
				IndexReader tr = DirectoryReader.openIfChanged(DirectoryReader.open(directory));
				if(tr !=null) {
					reader.close();
					reader = tr;
				}
			}
			return new IndexSearcher(reader);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
		
	}
	
	/**
	 * 精确查询
	 * @param field
	 * @param name
	 * @param num
	 */
	public void searchByTerm(String field,String name,int num) {
		IndexSearcher searcher = getSearcher();
		Query query = new TermQuery(new Term(field,name));
		try {
			TopDocs tds = searcher.search(query, num);
			System.out.println("一共查询了:" + tds.totalHits);
			
			for(ScoreDoc sd : tds.scoreDocs) {
				Document doc =  searcher.doc(sd.doc);
				System.out.println(sd.doc +","+"id:"+doc.get("id")+",name:"+doc.get("name")+",email:"+doc.get("email")+",date:"+doc.get("date"));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 根据一个范围查询
	 * @param field:字段名称
	 * @param start:搜索的起始值
	 * @param end:搜索的结束值
	 * @param num:几个
	 */
	public void searcherByTermRange(String field,String start,String end,int num) {
		
		IndexSearcher searcher = getSearcher();
		byte[] bstart = start.getBytes();
		byte[] bend = end.getBytes();
		BytesRef lowerTerm = new BytesRef(bstart,0,bstart.length);
		BytesRef upperTerm = new BytesRef(bend,0,bend.length);
		TermRangeQuery query = new TermRangeQuery(field,lowerTerm,upperTerm,true,true);
		try {
			TopDocs tds = searcher.search(query, num);
			System.out.println("一共查询了:" + tds.totalHits);
			
			for(ScoreDoc sd : tds.scoreDocs) {
				Document doc =  searcher.doc(sd.doc);
				System.out.println(sd.doc +","+"id:"+doc.get("id")+",name:"+doc.get("name")+",email:"+doc.get("email")+",date:"+doc.get("date"));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 以前缀查询
	 * @param field
	 * @param value
	 * @param num
	 */
	public void searchByPrefix(String field,String value,int num) {

		IndexSearcher searcher = getSearcher();
		
		Query query = new PrefixQuery(new Term(field,value));
		try {
			TopDocs tds = searcher.search(query, num);
			System.out.println("一共查询了:" + tds.totalHits);
			
			for(ScoreDoc sd : tds.scoreDocs) {
				Document doc =  searcher.doc(sd.doc);
				System.out.println(sd.doc +","+"id:"+doc.get("id")+",name:"+doc.get("name")+",email:"+doc.get("email")+",date:"+doc.get("date"));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据通配符查询
	 * @param field
	 * @param value
	 * @param num
	 */
	public void searchWildcardQuery(String field,String value,int num) {

		IndexSearcher searcher = getSearcher();
		
		//在插入的value中可以使用通配符,?表示一个字符, *表示多个字符。
		Query query = new WildcardQuery(new Term(field,value));
		try {
			TopDocs tds = searcher.search(query, num);
			System.out.println("一共查询了:" + tds.totalHits);
			
			for(ScoreDoc sd : tds.scoreDocs) {
				Document doc =  searcher.doc(sd.doc);
				System.out.println(sd.doc +","+"id:"+doc.get("id")+",name:"+doc.get("name")+",email:"+doc.get("email")+",date:"+doc.get("date"));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 联合查询
	 * @param num
	 */
	/*public void searchBooleanQuery (int num) {

		IndexSearcher searcher = getSearcher();
		
		//在插入的value中可以使用通配符,?表示一个字符, *表示多个字符。
		
		BooleanQuery query = null;

		try {
			TopDocs tds = searcher.search(query, num);
			System.out.println("一共查询了:" + tds.totalHits);
			
			for(ScoreDoc sd : tds.scoreDocs) {
				Document doc =  searcher.doc(sd.doc);
				System.out.println(sd.doc +","+"id:"+doc.get("id")+",name:"+doc.get("name")+",email:"+doc.get("email")+",date:"+doc.get("date"));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	
	/**
	 * 短语查询
	 * @param num
	 */
	public void searchPhraseQuery(int num) {

		IndexSearcher searcher = getSearcher();
		
		//slop是指两个项的位置之间允许的最大间隔距离
		PhraseQuery query = new PhraseQuery(2,"name","aa","bb");
		

		try {
			TopDocs tds = searcher.search(query, num);
			System.out.println("一共查询了:" + tds.totalHits);
			
			for(ScoreDoc sd : tds.scoreDocs) {
				Document doc =  searcher.doc(sd.doc);
				System.out.println(sd.doc +","+"id:"+doc.get("id")+",name:"+doc.get("name")+",email:"+doc.get("email")+",date:"+doc.get("date"));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 模糊查询
	 * @param num
	 */
	public void searchFuzzyQuery(int num) {

		IndexSearcher searcher = getSearcher();
		
		
		Query query = new FuzzyQuery(new Term("name","bb"));

		try {
			TopDocs tds = searcher.search(query, num);
			System.out.println("一共查询了:" + tds.totalHits);
			
			for(ScoreDoc sd : tds.scoreDocs) {
				Document doc =  searcher.doc(sd.doc);
				System.out.println(sd.doc +","+"id:"+doc.get("id")+",name:"+doc.get("name")+",email:"+doc.get("email")+",date:"+doc.get("date"));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * QueryParse,最常见的的查询
	 * @param num
	 * @throws org.apache.lucene.queryparser.classic.ParseException 
	 */
	public void searchQueryParse(int num) throws org.apache.lucene.queryparser.classic.ParseException {

		IndexSearcher searcher = getSearcher();
		
		QueryParser parse = new QueryParser("email", new StandardAnalyzer());
		
		//parse.setDefaultOperator(Operator.AND);
		//搜索content包含有aa或者itat.org，空格默认是OR的
		Query query = parse.parse("aa itat.org");

		//搜索域改为name为bb
		query = parse.parse("name:bb");
		
		//同样适用*和?来进行通配符匹配
		query = parse.parse("name:g*");
		
		//通配符默认不能放在首位
		//parse.setAllowLeadingWildcard(true);
		//query = parse.parse("name:*g");
		
		//匹配name中不能有cc，并且email中有aa的
		query = parse.parse("-name:cc +aa");
		
		//匹配一个开区间
		query = parse.parse("id:[1 TO 2]");
		
		//匹配闭区间
		query = parse.parse("id:(1 TO 3)");
		try {
			TopDocs tds = searcher.search(query, num);
			System.out.println("一共查询了:" + tds.totalHits);
			
			for(ScoreDoc sd : tds.scoreDocs) {
				Document doc =  searcher.doc(sd.doc);
				System.out.println(sd.doc +","+"id:"+doc.get("id")+",name:"+doc.get("name")+",email:"+doc.get("email")+",date:"+doc.get("date"));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
