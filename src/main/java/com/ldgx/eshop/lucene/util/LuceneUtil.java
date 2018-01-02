package com.ldgx.eshop.lucene.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.ldgx.eshop.lucene.entity.Goods;


public class LuceneUtil {
	public static final String INDEX_PATH = "E:\\lucene-db";
	
	/**
	 * 判断是否存在数据
	 * @return true:该文件夹下面有文件
	 * false:该文件夹下面没有文件
	 */
	public static boolean ifExists() throws Exception{
		
		File pfile = new File(INDEX_PATH);
		if(pfile.exists() && pfile.isDirectory() ) {
			File[] files = pfile.listFiles();
			if(files != null && files.length > 0) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 创建索引
	 * @param list:商品列表
	 */
	public static void createIndex(List<Goods> list) {
		IndexWriter writer = null;
		
		try {
			/*
			 * 1.创建Directory
			 * Directory directory = new RAMDirectory();//这个方法是建立在内存中的索引
			 * 在Lucene工具当中有两个子类分别是RAMDirectory 和 FSDirectory
			 * 这两个目录度可以作为索引的存储路径
			 * RAMDirectory是存放到内存当中的一个区域，FSDirectory是存放到文件系统中的磁盘里
			 * 虽然向其添加Document的过程与使用FSDDirectory一样，但是由于它是内存中的一块区域
			 * 因此，如果不将RAMDirectory中的内存写入磁盘，当虚拟机退出后，里面的内容也会随之消失。
			 * 一次需要将RAMDirectory中的内容转到FSDirectory中
			 */
			
			Directory directory = FSDirectory.open(Paths.get(INDEX_PATH));
			
			
			/*
			 * 2.创建IndexWriter,用完后要关闭
			 * 创建IndexWriter实例时，通过IndexWriterConfig来设置其相关配置：
			 * public IndexWriterConfig(Analyzer analyzer)
			 * analyzer：分词器对象
			 *  StandardAnalyzer是lucene中内置的“标准分析器”，可以做如下功能:
			 *  对原有句子按照空格进行了分词
			 *  所有的大写字母都可以能转换为小写的字母
			 *  可以去掉一些没有用处的单词，例如"is","the","are"等单词，也删除了所有的标点		            
			 */
			  
			IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
			 /*
             * IndexWriter用于更新或创建索引。它不是用来读取索引。
             * 创建索引写入对象，该对象既可以把索引写入到磁盘中也可以写入到内存中。 参数说明：
             * public IndexWriter(Directory directory, IndexWriterConfig conf)
             * directory:目录对象,也可以是FSDirectory 磁盘目录对象
             * conf:写入对象的控制
             */
			writer = new IndexWriter(directory,config);
			
			writer.deleteAll();// 清除以前的index
			
			/*
			 * 3.创建Document对象
			 * 创建Document 文档对象，在lucene中创建的索引可以看成数据库中的一张表，
        	 * 表中也可以有字段,往里面添加内容之后可以根据字段去匹配查询
             * 下面创建的doc对象中添加了三个字段，分别为name,sex,dosomething,
			 */
			Document doc = null;
			
			//3.保存信息
			for(Goods goods : list) {
				//Document代表是一条数据，Field代表数据中的一个属性，一个Document中有多个Field
				doc = new Document();

				/*			 * 	
				 * 用户String类型的字段的存储，StringField是只索引不分词
				 * 对String类型的字段进行存储，TextField和StringField的不同是TextField既索引又分词
				 * 
				 * Store.YES 保存，可以查询，可以打印内容
				 * Store.NO 不保存，可以查询，不可打印内容，由于不保存内容可以节省空间
				 * Store.COMPRESS 压缩保存 可以查询 可以打印内容 可以节省生成索引文件的空间
				 */	
				//4.为Document添加field
				doc.add(new Field("id", goods.getId() + "", TextField.TYPE_STORED));
				doc.add(new Field("name", goods.getName(), TextField.TYPE_STORED));
				String remark = goods.getRemark();
				if(remark == null) {
					remark = "";
				}
				doc.add(new Field("remark", remark, TextField.TYPE_NOT_STORED));
						
				
				
				
				//5.通过IndexWriter添加文档到索引中
				writer.addDocument(doc);//添加文档
			}
			
			//writer.close();//indexer创建完索引后如果没有关闭（提交）导致索引没有完整创建，就会导致搜索报错
			//directory.close();
			
		}catch(IOException e) {
			e.printStackTrace();
		}finally {
			//6.关闭writer
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
	 * 搜索
	 * @param keyWord:查询的关键字
	 * @param limit:最多显示几条数据
	 * @return
	 */
	public static List<Goods> search(String keyWord){
		List<Goods> list = new ArrayList<Goods>();
		
		try {
			//1.创建Directory
			Directory directory = FSDirectory.open(Paths.get(INDEX_PATH));
			
			//2.创建IndexSearcher检索索引的对象，里面要传递上面写入的内存目录对象directory
			DirectoryReader ireader = DirectoryReader.open(directory);
			 //3.指向索引目录的搜索器
			IndexSearcher indexSearcher = new IndexSearcher(ireader);

			// 4、创建搜索的Query
			Analyzer analyzer = new StandardAnalyzer();
			//Analyzer analyzer = new IKAnalyzer(true); // 使用IK分词
			
			// 简单的查询，创建Query表示搜索域为content包含keyWord的文档
			//Query query = new QueryParser("content", analyzer).parse(keyWord);
			String[] fields = {"id", "name", "remark"};
			// MUST 表示and，MUST_NOT 表示not ，SHOULD表示or
			BooleanClause.Occur[] clauses = {BooleanClause.Occur.SHOULD, BooleanClause.Occur.SHOULD, BooleanClause.Occur.SHOULD};
			// MultiFieldQueryParser表示多个域解析， 同时可以解析含空格的字符串，如果我们搜索"上海 中国" 
			Query multiFieldQuery = MultiFieldQueryParser.parse(keyWord, fields, clauses, analyzer);
			
			// 5、根据searcher搜索并且返回TopDocs
			TopDocs topDocs = indexSearcher.search(multiFieldQuery, 100); // 搜索前100条结果
			System.out.println("共找到匹配处：" + topDocs.totalHits);
			// 6、根据TopDocs获取ScoreDoc对象
			ScoreDoc[] scoreDocs = topDocs.scoreDocs;
			System.out.println("共找到匹配文档数：" + scoreDocs.length);
			
			QueryScorer scorer = new QueryScorer(multiFieldQuery, "content");
			SimpleHTMLFormatter htmlFormatter = new SimpleHTMLFormatter("<span style=\"backgroud:red\">", "</span>");
			Highlighter highlighter = new Highlighter(htmlFormatter, scorer);
			highlighter.setTextFragmenter(new SimpleSpanFragmenter(scorer));
			for (ScoreDoc scoreDoc : scoreDocs)
			{
				// 7、根据searcher和ScoreDoc对象获取具体的Document对象
				Document document = indexSearcher.doc(scoreDoc.doc);
				String remark = document.get("remark");
				//TokenStream tokenStream = new SimpleAnalyzer().tokenStream("content", new StringReader(content));
				//TokenSources.getTokenStream("content", tvFields, content, analyzer, 100);
				//TokenStream tokenStream = TokenSources.getAnyTokenStream(indexSearcher.getIndexReader(), scoreDoc.doc, "content", document, analyzer);
				//System.out.println(highlighter.getBestFragment(tokenStream, content));
				/*System.out.println("-----------------------------------------");
				System.out.println("文章标题："+document.get("title"));
				System.out.println("文章地址：" + document.get("url"));
				System.out.println("文章内容：");
				System.out.println(highlighter.getBestFragment(analyzer, "content", content));
				System.out.println("");*/
				// 8、根据Document对象获取需要的值
				Goods goods = new Goods();
				goods.setName(document.get("name"));
				if(remark == null) {
					remark = "";
				}
				goods.setRemark(highlighter.getBestFragment(analyzer, "remark", remark));
				list.add(goods);
			}
			
			ireader.close();
			directory.close();
			return list;
		}catch(Exception e) {
			e.printStackTrace();
			return list;
		}
		
	}
	
}
