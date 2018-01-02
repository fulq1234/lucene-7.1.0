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
		long oldl= System.currentTimeMillis();
		List<Goods> list;
		try {
			//list = goodsService.query("%name%",100000);
			list = goodsService.querylucene("name2");
			for(int i =0;i<list.size();i++) {
				Goods good = list.get(i);
				System.out.println(i +",name="+good.getName()+",remark="+good.getRemark());
			}
			System.out.println("size:"+list.size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			long newl= System.currentTimeMillis();
			System.out.println("共花费秒数:" + (newl - oldl));
		}
	}

}
