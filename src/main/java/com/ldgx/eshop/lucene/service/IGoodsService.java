package com.ldgx.eshop.lucene.service;

import java.util.List;

import com.ldgx.eshop.lucene.entity.Goods;


public interface IGoodsService {
	
	/**
	 * lucene的全文检索查询
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public List<Goods> querylucene(String name) throws Exception;
	
	/**
	 * 常规的数据库查询
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public List<Goods> query(String name) throws Exception;
}
