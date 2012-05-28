package com.klik.dic.commodity.mo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class CommodityWord {
	
	
	private String key;

	private HashMap<CommodityDicCompositeColumn, String> relateWords;
	private HashMap<CommodityDicCompositeColumn, String> categories;
	private HashMap<CommodityDicCompositeColumn, String> properties;
	
	
	private Integer ambiguityCount;
	private boolean withAmbiguity;
	
	public CommodityWord(){
		categories = new HashMap<CommodityDicCompositeColumn, String>();
		properties = new HashMap<CommodityDicCompositeColumn, String>();
		relateWords = new HashMap<CommodityDicCompositeColumn, String>();
	}
	
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public HashMap<CommodityDicCompositeColumn, String> getCategories() {
		return categories;
	}
	public void setCategories(
			HashMap<CommodityDicCompositeColumn, String> categories) {
		this.categories = categories;
	}
	public HashMap<CommodityDicCompositeColumn, String> getProperties() {
		return properties;
	}
	public void setProperties(
			HashMap<CommodityDicCompositeColumn, String> properties) {
		this.properties = properties;
	}
	
	
	
	public HashMap<CommodityDicCompositeColumn, String> getRelateWords() {
		return relateWords;
	}


	public void setRelateWords(
			HashMap<CommodityDicCompositeColumn, String> relateWords) {
		this.relateWords = relateWords;
	}


	public void addCategory(CommodityDicCompositeColumn col,String value){
		categories.put(col, value);
	}
	public void addProperty(CommodityDicCompositeColumn col,String value){
		properties.put(col, value);
	}
	public void addRelateWord(CommodityDicCompositeColumn col,String value){
		relateWords.put(col, value);
	}
	
	public Integer getAmbiguityCount() {
		return ambiguityCount;
	}

	public void setAmbiguityCount(Integer ambiguityCount) {
		this.ambiguityCount = ambiguityCount;
	}


	public boolean isWithAmbiguity() {
		return withAmbiguity;
	}


	public void setWithAmbiguity(boolean withAmbiguity) {
		this.withAmbiguity = withAmbiguity;
	}
	
	public List<String> getCategories(Integer idx){
		List<String> categoryList=new ArrayList<String>();
		Set<CommodityDicCompositeColumn> keys=this.categories.keySet();
		
		for(CommodityDicCompositeColumn key:keys){
			if (key.getAmbiguityIdx()==idx)
				categoryList.add(this.categories.get(key));
		}
		
		return categoryList;
	}
	
	public List<String> getRelateWords(Integer idx){
		List<String> relateWordList=new ArrayList<String>();
		
		Set<CommodityDicCompositeColumn> keys=this.relateWords.keySet();
		for(CommodityDicCompositeColumn key:keys){
			if (key.getAmbiguityIdx()==idx)
				relateWordList.add(this.relateWords.get(key));
		}
		
		return relateWordList;
	}
	
	public static CommodityWord selectOneAmbiguity(CommodityWord wordWithAmbiguty,Integer idx){
		if (!wordWithAmbiguty.isWithAmbiguity()){
			return wordWithAmbiguty;
		}
		CommodityWord word=new CommodityWord();
		word.setKey(wordWithAmbiguty.getKey());
		
		Set<CommodityDicCompositeColumn> keys=wordWithAmbiguty.getRelateWords().keySet();
		for(CommodityDicCompositeColumn col:keys){
			if (col.getAmbiguityIdx()==idx){
				word.addRelateWord(col,wordWithAmbiguty.getRelateWords().get(col));
			}
		}
		
		keys=wordWithAmbiguty.getProperties().keySet();
		for(CommodityDicCompositeColumn col:keys){
			if (col.getAmbiguityIdx()==idx){
				word.addProperty(col,wordWithAmbiguty.getProperties().get(col));
			}
		}
		
		keys=wordWithAmbiguty.getCategories().keySet();
		for(CommodityDicCompositeColumn col:keys){
			if (col.getAmbiguityIdx()==idx){
				word.addCategory(col,wordWithAmbiguty.getCategories().get(col));
			}
		}
		
		word.setAmbiguityCount(wordWithAmbiguty.getAmbiguityCount());
		word.setWithAmbiguity(false);
		return word;
	}
	
	
	
}
