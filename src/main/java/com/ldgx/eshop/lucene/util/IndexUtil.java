package com.ldgx.eshop.lucene.util;

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
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class IndexUtil {

	private String[] ids = {"1","2","3","4","5","6"};
	private String[] emails = {"aa@itat.org","bb@itat.org","cc@itat.org","dd@itat.org","ee@itat.org","gg@itat.org"};
	private String[] content = {
			"������æ��һҹ�����ڴ��ϴ��ȴ���á��ص�һ�����죬С�������ֱ�����ű����������������۾�֧��������һ������Ȼ�����Լ������Ѽ����������Ƚ�����˽���һ������ɫְҵװ��ѩ�׵�����ֱͦͦ����һ�ų��ż��˿ɰ�ƻ�������Դ����������������˳���֮�֣�������̧����ȥ��������ԭ��ҡҡ��׹�����ϰ�ľ���Ϻ�Ȼ����һ�������׵�Ь����",
			"��Ƚ�������ȥ�μӱ�װ��ᱻ�˼ҵ���δ�����˸��ϳ���Ҳ�ò�������ô������,����Ƚһ���Ȱ���һֻЬ�ߵ������Լ�������һ����������ᣬ�Ҷ�Ҫ���ˣ��㲻֪����Щ���ͻ���һ������ò��Ȼ��������Ƥ������Լ��������ˡ������ҵ��ȣ��ҵ�������������ô���ҵ����������ڵ��������˵ģ�Ҳ��������������С������ʲô�ˣ�",
			"��˵�ţ��ֺݺ�������ͷ�Ե���ż�������Ĵ������̷ɳ�����Զ������һ��ŭ����׼ȷ����������Ͱ������ûһ���ö������������淭����������һ��שͷ��������ͳ�һ���ƶ�Ӳ��������������Ƚ�Ĵ��ϣ������㣬���˾�֪��ʲô��û�����ֻ�и����ˡ�",
			"��Щ������������ֲ�Ƭֻ�Ǵ����������������ֱ����꣬�����������硣",
			"����Ƚ���ߣ����������ϵ��ĵ���ȥ�ˡ�������̾�˿�����Ҳû����˯�⣬�о��������Ѿ����϶������Լ��������Ӻ���ǰһ����ÿ������һ�ߵĽ���ʳ�����ᣬ������Ƚ�Ǿ�����Ҫ��ȥ����ҵ���������˸���ƹ�˾ʵϰ��ȴ��ÿ��������죬ƽ��ÿ����ٵ�һ˫�߸�Ь�������������������𣬱����˼������´���������Ƚ��ߣ�����һ�𿴡�����˭��ûע�⵽�����Եĵ�Դ��ͷ�ϲ�ʱð���ĵ��𻨡����ݵ�÷����Ϊ��һֻĸ������ɧ������Ƚ�̲�ס�������������������÷��Ҳ߯���ˣ��������ˡ�",
			"��������������ס���ۿ�����һȭ����ʾ���ԱߵĻ��䣬�����ü������죬ͻȻһ�������������������ͬʱʧȥ����ʶ��"
			
	};
	private Date[] dates = null;
	
	private int[] attachs = {2,3,1,4,5,5};
	private String[] names = {"����","����","����","����","����","����"};

	private Directory directory = null;
	
	private Map<String,Float> scores = new HashMap<String,Float>();
	
	/**
	 * ���캯��,����Directory
	 */
	public IndexUtil() {
		try {
			setDates();
			scores.put("itat.org", 2.0f);
			scores.put("zttc.edu", 1.5f);
			directory = FSDirectory.open(Paths.get("E:/lucene-db/i2"));
		} catch (IOException e) {
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
	 * ��������
	 */
	public void index() {
		
		//2.����IndexWriter
		IndexWriter writer = null;
		try {
			writer = new IndexWriter(directory, new IndexWriterConfig(new StandardAnalyzer()));
			
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
				System.out.println(et);
				
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
	 * ��ѯ����
	 */
	public void query() {
		//2.����IndexReader
		try {
			IndexReader reader = DirectoryReader.open(directory);
			int maxDoc = reader.maxDoc();//��������
			System.out.println("maxDoc:" + maxDoc);
			System.out.println("numDoc:" + reader.numDocs());
			System.out.println("deleteDocs:" + reader.numDeletedDocs());
			
			IndexSearcher searcher = new IndexSearcher(reader);
			
			TermQuery query = new TermQuery(new Term("content", "��"));
			
			TopDocs tds = searcher.search(query, 10);
			for(ScoreDoc sd : tds.scoreDocs) {
				Document doc = searcher.doc(sd.doc);
				System.out.println(sd.doc +","+"id:"+doc.get("id")+",name:"+doc.get("name")+",email:"+doc.get("email")+",date:"+doc.get("date"));
			}
			
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * �ָ�ɾ��������
	 */
	public void undelete() {
		//ʹ��IndexReader���лָ�
		try {
			IndexReader reader =  DirectoryReader.open(directory);
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * ɾ������
	 */
	public void delete() {
		IndexWriter writer = null;
		try {
			writer = new IndexWriter(directory, new IndexWriterConfig(new StandardAnalyzer()));
			
			//������һ��ѡ�������һ��Query,Ҳ������һ�� term,term��һ����ȷ���ҵ�ֵ
			//��ʱ���ĵ���������ȫɾ�������Դ洢��һ������վ�еģ����Իָ�
			writer.deleteDocuments(new Term("id","1"));
			
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
	 * ǿ��ɾ��
	 */
	public void forceDelete() {
		IndexWriter writer = null;
		try {
			writer = new IndexWriter(directory, new IndexWriterConfig(new StandardAnalyzer()));
			
			writer.forceMergeDeletes();
			
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
	 * ǿ��merge
	 */
	public void merge() {
		IndexWriter writer = null;
		try {
			writer = new IndexWriter(directory, new IndexWriterConfig(new StandardAnalyzer()));
			//�Ὣ�����ϲ�Ϊ2�Σ��������еı�ɾ�������ݻᱻ��� 
			//�ر�ע�⣬�˴�Lucene��3.5֮�󲻽���ʹ�ã���Ϊ��������ġ�
			writer.forceMerge(2);
			
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
	
	public void update() {
		IndexWriter writer = null;
		try {
			writer = new IndexWriter(directory, new IndexWriterConfig(new StandardAnalyzer()));
			/**
			 * lucene��û���ṩ���£�����ĸ��²�����ʵ�����µ����������ĺϼ�
			 * ��ɾ���������
			 */
			Document doc = new Document();
			doc.add(new Field("id","11",TextField.TYPE_STORED));
			doc.add(new Field("email",emails[0],TextField.TYPE_STORED));
			doc.add(new Field("content",content[0],TextField.TYPE_NOT_STORED));
			//doc.add(new Field("attach",attachs[i],TextField.TYPE_STORED));
			doc.add(new Field("name",names[0],TextField.TYPE_STORED));
			
			writer.updateDocument(new Term("id", "2"), doc);
			writer.commit();//���û����������������޸ģ�
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
	
}
