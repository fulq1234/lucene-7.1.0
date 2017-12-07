package com.ldgx.eshop.lucene.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ldgx.eshop.lucene.dao.IGoodsDao;
import com.ldgx.eshop.lucene.entity.Goods;
import com.ldgx.eshop.lucene.service.IGoodsService;
import com.ldgx.eshop.lucene.util.LuceneUtil;

@Service
public class GoodsService implements IGoodsService {

	@Autowired
	private IGoodsDao goodsDao;	
	
	@Override
	public List<Goods> query(String name, int limit) throws Exception {
		List<Goods> list = new ArrayList<Goods>();		
		boolean docNum = LuceneUtil.ifExists();//lucene文件夹下面有没有文件
		System.out.println("docNum="+docNum);
		if(!docNum) {//如果没有文件
			System.out.println("====dao query goods");
			list = goodsDao.queryGoods();//从数据库总查出商品的所有记录
			//保存数据到lucene中
			LuceneUtil.createIndex(list);
		}else {
			System.out.println("====lucene query goods");
			list = LuceneUtil.search(name, limit);//如果lucene有数据就从lucene中查
		}		
	
		return list;
	}

}
