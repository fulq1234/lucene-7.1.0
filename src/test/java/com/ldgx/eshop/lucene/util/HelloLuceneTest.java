package com.ldgx.eshop.lucene.util;


import org.junit.Test;

public class HelloLuceneTest {

	@Test
	public void testIndex() {
		HelloLucene l = new HelloLucene();
		l.index();
	}
	
	@Test
	public void testSearcher() {
		HelloLucene l = new HelloLucene();
		l.searcher();
	}

}
