package com.klik.dic.commodity.mo;

import com.netflix.astyanax.annotations.Component;

public class CommodityDicCompositeColumn {
	
	
	public static final String _CATEGORY_DIMENSION_DEFAULT="DEFAULT";
	
	
	public static final String _COL_TYPE_CATEGORIES="CATEGORY";
	public static final String _COL_TYPE_PROPERTY="PROPERTY";
	
	private @Component(ordinal=0) Integer ambiguityIdx;
	private @Component(ordinal=1) String  colType;
	private @Component(ordinal=2) String  dimension;
	
	
	public CommodityDicCompositeColumn(){
		
	}
	
	public CommodityDicCompositeColumn(String colType,String dimension){
		this.ambiguityIdx=-1;
		this.colType=colType;
		this.dimension=dimension;
	}
	
	public CommodityDicCompositeColumn(Integer ambiguityIdx,String colType,String dimension){
		this.ambiguityIdx=ambiguityIdx;
		this.colType=colType;
		this.dimension=dimension;
	}
	
	public String getColType() {
		return colType;
	}
	public void setColType(String colType) {
		this.colType = colType;
	}
	public String getDimension() {
		return dimension;
	}
	public void setDimension(String dimension) {
		this.dimension = dimension;
	}

	public Integer getAmbiguityIdx() {
		return ambiguityIdx;
	}

	public void setAmbiguityIdx(Integer ambiguityIdx) {
		this.ambiguityIdx = ambiguityIdx;
	}
	
	
	
}
