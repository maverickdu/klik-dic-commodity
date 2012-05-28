package com.klik.dic.commodity.test;



import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class BrandSpider4Auto {
	
	
	public void getBrand() throws Exception{
		Document doc=Jsoup.connect("http://data.auto.sina.com.cn/").get();
		Elements eles= doc.select("div.car_pt h6 a");
		for(Element ele:eles){
			System.out.println(ele.text());
		}
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		BrandSpider4Auto spider=new BrandSpider4Auto();
		spider.getBrand();
	}

}
