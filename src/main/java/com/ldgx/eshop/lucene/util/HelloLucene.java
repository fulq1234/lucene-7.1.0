package com.ldgx.eshop.lucene.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class HelloLucene {

	/**
	 * ��������
	 * @throws IOException 
	 */
	public void index(){
		
		
		IndexWriter writer = null;
		try {
			
			//1.����Directory
			//Directory directory = new RAMDirectory();//�ڴ���
			Directory directory = FSDirectory.open(Paths.get("E:/lucene-db"));//������Ӳ����
			//2.����IndexWriter
			Analyzer analyzer = new StandardAnalyzer();
			IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
			
			writer = new IndexWriter(directory, iwc);
			
			//3.����Document����
			Document doc = null;	
			
			//4.ΪDocument���field
			File f = new File("E:/BaiduNetdiskDownload/jwtc/story/��Խ");
			for(File file : f.listFiles()) {
				System.out.println(file.getName() + ":" + file.getAbsolutePath());
				doc = new Document();
				
				String content = FileUtils.readFileToString(file);
				System.out.println(content);
				
				//TextField.TYPE_NOT_STORED:�������ִʲ�����(Indexed, tokenized, not stored)
				doc.add(new Field("content",new FileReader(file),TextField.TYPE_NOT_STORED));//�ļ�����
				doc.add(new Field("filename",file.getName(),TextField.TYPE_STORED));//�ļ�����
				doc.add(new Field("path",file.getAbsolutePath(),TextField.TYPE_STORED));//�ļ�·��
				//5.ͨ��IndexWriter����ĵ���������
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
	 * ����
	 */
	public void searcher() {
		//1.����Directory
		try {
			Directory directory = FSDirectory.open(Paths.get("E:/lucene-db/i1"));//������Ӳ����
			
			//2.����IndexReader
			Analyzer analyzer = new StandardAnalyzer();
			IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
			IndexWriter writer =   new IndexWriter(directory, iwc);
			IndexReader reader = DirectoryReader.open(writer);			
			
			//3.����IndexReader����IndexSearcher
			IndexSearcher searcher = new IndexSearcher(reader);
			
			//4.����������Query
			//����parser��ȷ�������ļ�������,param1:��������
			QueryParser parser = new QueryParser("content",new StandardAnalyzer());
			//����Query,������Ϊcontent�а�������Խ�����ĵ�
			Query query = parser.parse("��Խ");
			
			//5.����searcher�������ҷ���TopDocs
			//search(param1,param2) param2:��ʾ����
			TopDocs tds = searcher.search(query, 10);
			
			//6.����TopDocs��ȡScoreDoc����
			ScoreDoc[] sds = tds.scoreDocs;
			//����
			for(ScoreDoc sd : sds) {

				//7.����searcher��scoreDoc�����ȡ�����Document����
				Document d = searcher.doc(sd.doc);
				
				//8.����Document�����ȡ��Ҫ��ֵ
				System.out.println(d.get("filename") + "[" +d.get("path")+ "]");
			}
			
			
			//9.�ر�reader
			reader.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
}
