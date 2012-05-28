package com.klik.dic.commodity.dao.imp;

import java.util.HashMap;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.klik.dic.commodity.dao.ICommodityDicDao;
import com.klik.dic.commodity.dao.ICommoditySynDicDao;
import com.klik.dic.commodity.mo.CommodityDicCompositeColumn;
import com.klik.dic.commodity.mo.CommodityWord;
import com.netflix.astyanax.ColumnListMutation;
import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.MutationBatch;
import com.netflix.astyanax.connectionpool.OperationResult;
import com.netflix.astyanax.model.Column;
import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.model.ColumnList;
import com.netflix.astyanax.model.Rows;
import com.netflix.astyanax.serializers.AnnotatedCompositeSerializer;
import com.netflix.astyanax.serializers.StringSerializer;


@Component("commodityDicDao")
public class CommodityDicDaoImp implements ICommodityDicDao, InitializingBean {
	
	@Autowired
	protected Keyspace keyspace;
	@Autowired
	protected ICommoditySynDicDao commoditySynDicDao;
	
	protected ColumnFamily<String,CommodityDicCompositeColumn> cf;
	
	Logger logger=LoggerFactory.getLogger(this.getClass());
	
	@Override
	public void afterPropertiesSet() throws Exception {
		
		AnnotatedCompositeSerializer<CommodityDicCompositeColumn> commodityDicSerializer
			=new AnnotatedCompositeSerializer<CommodityDicCompositeColumn>(CommodityDicCompositeColumn.class);
		
		cf = new ColumnFamily<String, CommodityDicCompositeColumn>("COMMODITY_DICTIONARY", 
				StringSerializer.get(), commodityDicSerializer);

	}
	
	@Override
	public void insertWord(String key,HashMap<CommodityDicCompositeColumn, String> categories,
			HashMap<CommodityDicCompositeColumn, String> properties,
			HashMap<CommodityDicCompositeColumn, String> relateWords) throws Exception{
		
		
		MutationBatch m=keyspace.prepareMutationBatch();
		ColumnListMutation<CommodityDicCompositeColumn>  clm=m.withRow(cf, key);
		
		int count=getAmbiguityCount(key);
		
		for(CommodityDicCompositeColumn col:categories.keySet()){
			col.setAmbiguityIdx(count);
			clm.putColumn(col, categories.get(col), null);
		}
		for(CommodityDicCompositeColumn col:properties.keySet()){
			col.setAmbiguityIdx(count);
			clm.putColumn(col, properties.get(col), null);
		}
		for(CommodityDicCompositeColumn col:relateWords.keySet()){
			col.setAmbiguityIdx(count);
			clm.putColumn(col, relateWords.get(col), null);
		}
		
		m.execute();
		
	}
	
	@Override
	public void insertWord(CommodityWord word) throws Exception{
		insertWord(word.getKey(),word.getCategories(),word.getProperties(),word.getRelateWords());
	}
	@Override
	public Integer getAmbiguityCount(String key) throws Exception{
		
		Integer count=0;
		
		CommodityDicCompositeColumn startColumn=new CommodityDicCompositeColumn(0,"CATEGORY","0");
		CommodityDicCompositeColumn endColumn=new CommodityDicCompositeColumn(Integer.MAX_VALUE,"CATEGORY","þ");
		OperationResult<ColumnList<CommodityDicCompositeColumn>> opResult =keyspace.prepareQuery(cf).
			getKey(key).withColumnRange(endColumn, startColumn, true,1).execute();
		
		ColumnList<CommodityDicCompositeColumn> cols=opResult.getResult();
		if (cols.size()>0){
			count = cols.getColumnByIndex(0).getName().getAmbiguityIdx()+1;
		}
		
		return count;
	}
	
	@Override
	public CommodityWord findWord(String key,Integer ambigutyIdx) throws Exception{
		Entry<String, Integer> synKey=hasSynWord(key);
		if (synKey!=null){
			key=synKey.getKey();
			ambigutyIdx = synKey.getValue();
		}
		
		CommodityWord word=new CommodityWord();
		
		CommodityDicCompositeColumn startColumn=new CommodityDicCompositeColumn(ambigutyIdx,"0","0");
		CommodityDicCompositeColumn endColumn=new CommodityDicCompositeColumn(ambigutyIdx,"þ","þ");
		
		OperationResult<ColumnList<CommodityDicCompositeColumn>> opResult=  keyspace.prepareQuery(cf).
				getKey(key).withColumnRange(startColumn, endColumn, false, Integer.MAX_VALUE).execute();
		
		ColumnList<CommodityDicCompositeColumn> cols=opResult.getResult();
		if (cols.size()==0){
			return null;
		}
		for(Column<CommodityDicCompositeColumn> col:cols){
			if (col.getName().getColType().equals(CommodityDicCompositeColumn._COL_TYPE_CATEGORIES))
				word.addCategory(col.getName(), col.getStringValue());
			if (col.getName().getColType().equals(CommodityDicCompositeColumn._COL_TYPE_PROPERTY))
				word.addProperty(col.getName(), col.getStringValue());
		}
		word.setKey(key);
		word.setAmbiguityCount(getAmbiguityCount(key));
		word.setWithAmbiguity(false);
		return word;
	}
	@Override
	public CommodityWord findWord(String key) throws Exception{
		Entry<String, Integer> synKey=hasSynWord(key);
		if (synKey!=null){
			return findWord(synKey.getKey(),synKey.getValue());
		}
		CommodityWord word=new CommodityWord();
		OperationResult<ColumnList<CommodityDicCompositeColumn>> opResult=  keyspace.prepareQuery(cf).
				getKey(key).execute();
		ColumnList<CommodityDicCompositeColumn> cols=opResult.getResult();
		if (cols.size()==0){
			return null;
		}
		for(Column<CommodityDicCompositeColumn> col:cols){
			if (col.getName().getColType().equals(CommodityDicCompositeColumn._COL_TYPE_CATEGORIES))
				word.addCategory(col.getName(), col.getStringValue());
			if (col.getName().getColType().equals(CommodityDicCompositeColumn._COL_TYPE_PROPERTY))
				word.addProperty(col.getName(), col.getStringValue());
		}
		word.setKey(key);
		word.setAmbiguityCount(getAmbiguityCount(key));
		if (word.getAmbiguityCount()>1){
			word.setWithAmbiguity(true);
		}else{
			word.setWithAmbiguity(false);
		}
		return word;
	}
	
	@Override
	public Entry<String, Integer> hasSynWord(String key) throws Exception{
		return commoditySynDicDao.hasSynWord(key);
	}
	
	@Override
	public Rows<String, CommodityDicCompositeColumn> getAllKeys() throws Exception{
		OperationResult<Rows<String, CommodityDicCompositeColumn>> opResult=keyspace.prepareQuery(cf).getAllRows().execute();
		return opResult.getResult();
	}
	
	public static void main(String[] args) throws Exception{
		AbstractApplicationContext ctx= new ClassPathXmlApplicationContext("classpath:spring/*.xml");
		ctx.registerShutdownHook();
		
		ICommodityDicDao dao=(ICommodityDicDao)ctx.getBean("commodityDicDao");
		//Rows<String, CommodityDicCompositeColumn> rows=dao.getAllKeys();
		//for(Row<String, CommodityDicCompositeColumn> row:rows){
			//System.out.println(row.getKey());
		//}
		CommodityWord word=dao.findWord("苹果公司");
		System.out.println(word.getCategories().values());
		
	}

}
