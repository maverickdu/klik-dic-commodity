package com.klik.dic.commodity.dao;

import java.util.Map.Entry;

import com.netflix.astyanax.model.Rows;

public interface ICommoditySynDicDao {
	public Entry<String, Integer> hasSynWord(String key) throws Exception;
	public Rows<String, String> getAllKeys() throws Exception;
}
