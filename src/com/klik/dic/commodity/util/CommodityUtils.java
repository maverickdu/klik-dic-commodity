package com.klik.dic.commodity.util;

import java.io.File;
import java.io.RandomAccessFile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.klik.dic.commodity.dao.ICommodityDicDao;
import com.klik.dic.commodity.dao.ICommoditySynDicDao;
import com.klik.dic.commodity.mo.CommodityDicCompositeColumn;
import com.netflix.astyanax.model.Row;
import com.netflix.astyanax.model.Rows;

@Component("CommodityUtils")
public class CommodityUtils {
	
	@Autowired
	ICommodityDicDao commodityDicDao;
	@Autowired
	ICommoditySynDicDao commoditySynDicDao;
	
	public void makeCommodityDicFile(String fileName) throws Exception{
		
		String path=this.getClass().getClassLoader().getResource("").getPath().replace("classes/", "")+fileName;
		
		File file=new File(path);
		if(file.exists()){
			file.delete();
		}
		
		RandomAccessFile raf=new RandomAccessFile(path, "rw");
		int i=0;
		
		Rows<String, CommodityDicCompositeColumn> rows=commodityDicDao.getAllKeys();
		for(Row<String, CommodityDicCompositeColumn> row:rows){
			String key=row.getKey();
			raf.write((key+"\n").getBytes("UTF-8"));
			raf.seek(raf.length());
			i++;
			System.out.println(i+key);
		}
		
		Rows<String, String> synWords=commoditySynDicDao.getAllKeys();
		
		for(Row<String,String> synWord:synWords){
			String key=synWord.getKey();
			raf.write((key+"\n").getBytes("UTF-8"));
			raf.seek(raf.length());
			i++;
			System.out.println(i+key);
		}
		
		raf.close();
		
		System.out.println("Dic File make Complete: "+path);
	}
	
	public static void main(String[] args) throws Exception{
		AbstractApplicationContext ctx= new ClassPathXmlApplicationContext("classpath:spring/*.xml");
		ctx.registerShutdownHook();
		CommodityUtils utils=(CommodityUtils)ctx.getBean("CommodityUtils");
		utils.makeCommodityDicFile("commodityDic.dic");
	}
	
}
