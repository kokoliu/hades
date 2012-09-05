 /*
 *  Copyright Beijing 58 Information Technology Co.,Ltd.
 *
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package com.bj58.spat.hades.utils;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


/**
 * 放Sql和HTML注入的转换类
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class SqlHtmlConverter {

	private static Map<String, String> sqlMatches = new HashMap<String, String>();
	
	private static Map<Character, String> htmlEncodeMatches = new HashMap<Character, String>();
	
	private static Map<String, Character> htmlDecodeMatches = new HashMap<String, Character>();
	
	private static final Properties sqlInjectProperties = new Properties();
	
	private static final Properties htmlEncodeProperties = new Properties();
	
	
	
	public static String convert(String source){
		
		for(String key : sqlMatches.keySet()){
			source = source.replaceAll("["+sqlMatches.get(key)+"]",key);
		}
		
		source = SqlHtmlConverter.encodeHtml(source);
		
		return source;
	}
	
	public static void initSqlInject(String configFile){

		File file = new File(configFile);
		Reader reader = null;
		
		try {
			reader = new FileReader(file);
			sqlInjectProperties.load(reader);
			
			for (Enumeration e = sqlInjectProperties.propertyNames(); e.hasMoreElements();){
				
				String key = (String)e.nextElement();
				String content = sqlMatches.get(sqlInjectProperties.getProperty(key,""));		
				if(content == null){
					content = key;
				}else{
					content = content+"|"+key;
				}
				sqlMatches.put(sqlInjectProperties.getProperty(key,""), content);	
		    }
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(reader != null){
				try {
					reader.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		
	}
	
	public static void initHtmlEncode(String configFile){

		File file = new File(configFile);
		Reader reader = null;
		
		try {
			reader = new FileReader(file);
			htmlEncodeProperties.load(reader);
			
			for (Enumeration e = htmlEncodeProperties.propertyNames(); e.hasMoreElements();){
				
				String key = ((String)e.nextElement()).trim();
				char c = key.charAt(0);

				htmlEncodeMatches.put(c, htmlEncodeProperties.getProperty(key,""));
				htmlDecodeMatches.put(htmlEncodeProperties.getProperty(key,""), c);	
		    }
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(reader != null){
				try {
					reader.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}	
	}
	public static String encodeHtml(String str){
		
        if (str == null) {
            return null;
        }
        StringWriter writer = new StringWriter ((int)(str.length() * 1.5));
        
        for (int i = 0; i < str.length(); i++) {
        	char c = str.charAt(i);
        	if(htmlEncodeMatches.get(c) == null)
        		writer.write(c);
        	else
        		writer.write(htmlEncodeMatches.get(c));
        }
        return writer.toString();

	}
	
	public static String decodeHtml(String str) throws Exception{
	
		throw new Exception("no impl");	
	}
}
