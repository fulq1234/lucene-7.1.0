package com.ldgx.eshop.lucene.service.impl;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ldgx.eshop.lucene.entity.Goods;
import com.ldgx.eshop.lucene.service.IGoodsService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations ="classpath:config/spring-*.xml")
public class GoodsServiceTest {

	@Autowired
	private IGoodsService goodsService;
	
		
	@Test
	public void test() {
		List<Goods> list;
		try {
			list = goodsService.query("学生 备注",100);
			

			for(Goods goods :list) {
				System.out.println("-----------------------------------------");
				System.out.println("文章标题："+goods.getTitle());
				System.out.println("文章地址：" + goods.getUrl());
				System.out.println("文章内容：");
				System.out.println(goods.getContent());
				System.out.println("");
			}
			/*
			StringBuffer sb = new StringBuffer();
			for(int i =0;i<list.size();i++) {
				Goods goods = list.get(i);
				sb.append(goods.getName() + ",");
			}
			System.out.println(sb.toString());*/
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
