package com.ldgx.eshop.lucene.util;

import org.junit.Before;
import org.junit.Test;

public class IndexUtilTest {
	IndexUtil util = null;
	@Before
	public void before() {
		util = new IndexUtil();
	}
	@Test
	public void testIndex() {
		util.index();
	}
	
	@Test
	public void testDelete() {
		
		util.delete();
	}

	@Test
	public void testQuery() {
		util.query();
	}
}
