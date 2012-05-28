package com.klik.dic.commodity.dao;

import java.util.HashMap;
import java.util.Map.Entry;

import com.klik.dic.commodity.mo.CommodityDicCompositeColumn;
import com.klik.dic.commodity.mo.CommodityWord;
import com.netflix.astyanax.model.Rows;

public interface ICommodityDicDao {
	
	public void insertWord(String key,HashMap<CommodityDicCompositeColumn, String> categories,
			HashMap<CommodityDicCompositeColumn, String> properties,
			HashMap<CommodityDicCompositeColumn, String> relateWords) throws Exception;
	
	public void insertWord(CommodityWord word) throws Exception;
	
	public CommodityWord findWord(String key,Integer ambigutyIdx) throws Exception;
	public CommodityWord findWord(String key) throws Exception;
	
	public Integer getAmbiguityCount(String key) throws Exception;
	
	public Entry<String, Integer> hasSynWord(String key) throws Exception;
	
	public Rows<String, CommodityDicCompositeColumn> getAllKeys() throws Exception;
}
