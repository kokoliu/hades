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
package com.bj58.spat.hades.log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

import com.bj58.spat.hades.HADES;

/**
 * Log4j配置
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class Log4jConfigurator {

	/**日志记录目录**/
	public static final String LOG_RECORD_FOLDER = "/logRecords";
	
	Log4jConfigurator(){}
	
	/**
	 * 初始化Log4j配置
	 * @param configFile
	 */
	public static void initConfigurator(String configFile){
		File file = new File(configFile);
		if(!file.exists()){
			copyDefaultLog4jPropertiyFile(file);
			file = new File(configFile);
		}
		Properties log4j_properties = new Properties();
		Reader reader = null;
		try {
			reader = new FileReader(file);
			log4j_properties.load(reader);
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

		String logRecordFile = HADES.LOG_PATH + HADES.getNamespace() + ".log";
//		String logRecordFile = configFile.substring(0, configFile.lastIndexOf('/')) + LOG_RECORD_FOLDER + "/logrecord.log";
		
		log4j_properties.setProperty("log4j.appender.file.File", logRecordFile);
		log4j_properties.setProperty("log4j.appender.file.DatePattern", "'.'yyyy-MM-dd");
		PropertyConfigurator.configure(log4j_properties);
	}
	
	/**
	 * 如果配置文件夹中没有配置文件夹，将从模板中拷贝配置文件
	 * @param propertyFile
	 */
	private static void copyDefaultLog4jPropertiyFile(File propertyFile){
		InputStream in = null;
		OutputStream out = null;
		try {
			propertyFile.getParentFile().mkdirs();
			in = Thread.currentThread().getContextClassLoader().getResourceAsStream("com/bj58/spat/hades/resources/log4jConfig.template");
			out = new FileOutputStream(propertyFile);
			byte[] b = new byte[in.available()];
			in.read(b);
			out.write(b);
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(out != null){
				try {
					out.flush();
					out.close();
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
			if(in != null){
				try {
					in.close();
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
		}
	}
}
