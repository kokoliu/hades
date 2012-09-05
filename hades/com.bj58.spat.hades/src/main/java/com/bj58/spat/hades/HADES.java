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
package com.bj58.spat.hades;

import java.io.File;
import java.io.InputStream;
import java.util.PropertyResourceBundle;

import com.bj58.spat.hades.exception.ExceptionUtils;
import com.bj58.spat.hades.exception.ResourceNotFoundException;
import com.bj58.spat.hades.ioc.context.ServiceContext;
import com.bj58.spat.hades.log.Log4jConfigurator;
import com.bj58.spat.hades.utils.ConfigManager;
import com.bj58.spat.hades.utils.FileUtil;
import com.bj58.spat.hades.utils.SqlHtmlConverter;

/**
 * HADES初始化类
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public abstract class HADES{

	
	protected static final String namespace;
	
	protected static final String scanBasedPackage; 
	
	public static final String CONFIG_FOLDER;//unix系统下没有测试
	
	private static final String LOG_CONFIG_FILE; 
	
	public static final String DATASOURCE_CONFIG_FILE;
		
	public static final String LOG_PATH;
	
	private static ServiceContext services;
	
	static {
		try {
			
			CONFIG_FOLDER          = FileUtil.getRootPath() + "/opt/hades/";
			LOG_PATH = FileUtil.getRootPath() + "/opt/hades/logs/";
			LOG_CONFIG_FILE        = CONFIG_FOLDER + "${namespace}/bj58log.properties";
			DATASOURCE_CONFIG_FILE = CONFIG_FOLDER + "${namespace}/db.properties";


			
			ClassLoader cl = Thread.currentThread().getContextClassLoader();
			InputStream inputStream = cl.getResourceAsStream("META-INF/namespace.properties");
			PropertyResourceBundle pp = new PropertyResourceBundle(inputStream);
			
			//名称空间
			namespace = pp.containsKey("namespace") ? pp.getString("namespace") : "";
			if(namespace == null || "".equals(namespace.trim()))
				throw new ResourceNotFoundException("Does not specify a value for the namespace");
			
			//Ioc描述基本包
			scanBasedPackage =  pp.containsKey("scanpackage") ? pp.getString("scanpackage") : "com.bj58";
			
			//往配置文件夹拷入各类配置
//			initConfigFile();			
//			initServices();
			initConverter();
			
		} catch (Exception e) {
			e.printStackTrace();
			throw ExceptionUtils.makeThrow("META-INF in the classpath folder to ensure that there is 'namespace.properties' configuration file, and specifies the value namespace", e);
		}
	}

	public static ServiceContext getServiceContext() {
		
		return services;
	}	
	/**
	 * 框架版本号
	 * @return WebJ version in file of META-INF/MANIFEST.MF 
	 */
	public static String getVersion() {
		Package pkg = HADES.class.getPackage();
		return (pkg != null ? pkg.getImplementationVersion() : null);
	}
	
	/**
	 * 返回项目配置文件的名称
	 * <br>
	 * 所有的配置文件将存放在 {@link CONFIG_FOLDER}
	 * @return
	 */
	public static String getNamespace(){
		return namespace;
	}
	
	/**
	 * 获取配置路径
	 * @return
	 */
	public static String getConfigFolder(){
		return CONFIG_FOLDER;
	}
	
	/**
	 * 获取数据库连接池配置文件路径
	 * @return 完整路径
	 */
	public static String getDBConfigFilePath(){
		return DATASOURCE_CONFIG_FILE.replace("${namespace}", namespace);
	}
	
	/**
	 * 获取Log4j配置文件路径
	 * @return 完整路径
	 */
	public static String getLog4jConfigFilePath(){
		return LOG_CONFIG_FILE.replace("${namespace}", namespace);
	}


	
	private static void initConfigFile(){

		//log配置文件
		Log4jConfigurator.initConfigurator(getLog4jConfigFilePath());
		
		//拷入Memcache配置文件
		
		// copy用户配置文件
		ConfigManager.copyConfig();
		
	}
	
	private static void initConverter(){
		
		String sqlInjectFilePath = HADES.getConfigFolder() + HADES.getNamespace()+"/sql-inject.properties";
		String htmlEncodeFilePath = HADES.getConfigFolder() + HADES.getNamespace()+"/html-encode.properties";
		File fSql = new File(sqlInjectFilePath);
		
		File fHtml = new File(htmlEncodeFilePath);
		
		if(fSql.exists())	
			SqlHtmlConverter.initSqlInject(sqlInjectFilePath);
		
		if(fHtml.exists())	
			SqlHtmlConverter.initHtmlEncode(htmlEncodeFilePath);
		
	}
}
