package com.ldgx.eshop.lucene.service;

import java.util.List;

import com.ldgx.eshop.lucene.entity.Goods;


public interface IGoodsService {
	
	public List<Goods> query(String name,int limit) throws Exception;
}
