package com.ldgx.eshop.lucene.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ldgx.eshop.lucene.entity.Goods;


@Mapper
public interface IGoodsDao {

	public List<Goods> queryGoods(String name);
}
