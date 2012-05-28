package com.klik.dic.commodity.test;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;


public class AutoTools {
	
	
	public static void makeAutoInsertSql(){
		
		String synColKey="0:SYN:TO";
		
		InputStream in = AutoTools.class.getResourceAsStream("/asserts/auto_brand.txt");
		String content=stream2String(in,"utf-8").toUpperCase();
		String[] lines=content.split("\n");
		System.out.println("------------------BEGIN----------------------");
		for(String line:lines){
			String[] items=line.split(",");
			System.out.println("set COMMODITY_DICTIONARY['"+items[0]+"']['0:CATEGORY:TYPE']=utf8('汽车');");
			
			for(int i=1;i<items.length;i++){
				System.out.println("set COMMODITY_DIC_SYN['"+items[i]+"']['SYN']=utf8('"+items[0]+"');");
				System.out.println("set COMMODITY_DIC_SYN['"+items[i]+"']['IDX']=utf8('0');");
			}
				
			
			
		}
		System.out.println("------------------DONE----------------------");
	}
	
	 /** 
     * 文件转换为字符串 
     * 
     * @param in            字节流 
     * @param charset 文件的字符集 
     * @return 文件内容 
     */ 
    public  static String stream2String(InputStream in, String charset) { 
            StringBuffer sb = new StringBuffer(); 
            try { 
                    Reader r = new InputStreamReader(in, charset); 
                    int length = 0; 
                    for (char[] c = new char[1024]; (length = r.read(c)) != -1;) { 
                            sb.append(c, 0, length); 
                    } 
                    r.close(); 
            } catch (UnsupportedEncodingException e) { 
                    e.printStackTrace(); 
            } catch (FileNotFoundException e) { 
                    e.printStackTrace(); 
            } catch (IOException e) { 
                    e.printStackTrace(); 
            } 
            return sb.toString(); 
    } 
	
    public static void main(String[] args) throws Exception{
    	makeAutoInsertSql();
    }
    
}
