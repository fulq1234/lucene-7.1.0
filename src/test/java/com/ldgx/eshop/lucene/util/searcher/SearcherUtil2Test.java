package com.ldgx.eshop.lucene.util.searcher;

import org.apache.lucene.queryparser.classic.ParseException;
import org.junit.Before;
import org.junit.Test;

public class SearcherUtil2Test {

	SearcherUtil2 su2 = null;
	
	@Before
	public void init() {
		su2 = new SearcherUtil2();
	}
	
	@Test
	public void testSearchByTerm() {
		su2.searchByTerm("name", "aa", 10);
	}
	
	@Test
	public void testSearcherByTermRange() {
		su2.searcherByTermRange("name", "b", "s", 10);
	}
	
	@Test
	public void testSearchByPrefix() {
		su2.searchByPrefix("content", "n", 10);
	}
	
	/**
	 * 根据通配符搜索，
	 * ?，一个字符.  * 多个字符
	 */
	@Test
	public void testSearchWildcardQuery() {
		su2.searchWildcardQuery("name", "d?", 10);
	}

	/**
	 * 短语查询
	 */
	@Test
	public void testSearchPhraseQuery() {
		su2.searchPhraseQuery(10);
	}
	
	@Test
	public void testSearchFuzzyQuery() {
		su2.searchFuzzyQuery(10);
	}
	
	@Test
	public void testSearchQueryParse() {
		try {
			su2.searchQueryParse(10);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
