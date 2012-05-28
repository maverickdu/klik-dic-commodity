package com.klik.dic.commodity.dao.imp;

import java.util.Map.Entry;
import java.util.AbstractMap.SimpleEntry;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.klik.dic.commodity.dao.ICommoditySynDicDao;
import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.connectionpool.OperationResult;
import com.netflix.astyanax.model.Column;
import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.model.ColumnList;
import com.netflix.astyanax.model.Rows;
import com.netflix.astyanax.serializers.StringSerializer;

@Component("commoditySynDicDao")
public class CommoditySynDicDaoImp implements ICommoditySynDicDao,InitializingBean {
	
	@Autowired
	protected Keyspace keyspace;
	
	protected ColumnFamily<String, String> cf;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		cf=new ColumnFamily<String, String>("COMMODITY_DIC_SYN", StringSerializer.get(), StringSerializer.get());
	}
	
	
	@Override
	public Entry<String, Integer> hasSynWord(String key) throws Exception {
		
		OperationResult<ColumnList<String>> opResult=
				keyspace.prepareQuery(cf).getKey(key).withColumnSlice("SYN","IDX").execute();
		ColumnList<String> cols=opResult.getResult();
		
		Column<String> colSyn=cols.getColumnByName("SYN");
		Column<String> colIdx=cols.getColumnByName("IDX");
		if (colSyn==null||colIdx==null){
			return null;
		}
		
		return new SimpleEntry<String, Integer>(colSyn.getStringValue(),Integer.parseInt(colIdx.getStringValue()));
	}
	
	public Rows<String, String> getAllKeys() throws Exception{
		Rows<String, String>  rows=keyspace.prepareQuery(cf).getAllRows().execute().getResult();
		return rows;
	}
	
	
}
