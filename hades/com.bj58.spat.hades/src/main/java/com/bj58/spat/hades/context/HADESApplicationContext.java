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
package com.bj58.spat.hades.context;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;

import com.bj58.spat.hades.utils.ClassUtil;

/**
 * 
 * 自实现的 WebAppContext初始化web信息，进行包扫描 等工具的安装。
 * 
 *
 * @author Service Platform Architecture Team (spat@58.com)
 *
 */
public class HADESApplicationContext {

	
	private ServletContext servletContext;
	
	private String configLocation;
	
	private String id;
	
	public static Set<Class<?>> clazzes ;
	
	
	
	
	public ServletContext getServletContext() {
		return servletContext;
	}

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public String getConfigLocation() {
		return configLocation;
	}

	public void setConfigLocation(String configLocation) {
		this.configLocation = configLocation;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void init(){
		ClassUtil util = new ClassUtil();
		String filter = "*Controller";
		List<String> filters = new ArrayList<String>();
		filters.add(filter);
		util.setClassFilters(filters);
		clazzes = util.scanPackageFile("com", true);
		
		
		
//		servletContext.
	}
	
	public HADESApplicationContext(ServletContext servletContext){
		
	}
	public HADESApplicationContext(){
		
	}
	public Class<?> getType(String beanName){
		
		Iterator<Class<?>> it = clazzes.iterator();
		
		
		
		while(it.hasNext()){
			Class<?> c = it.next();
			if(beanName.equals(c.getName()))
				return c;
			
		}
		return null;
	}
	public Object getBean(String beanName) {
		
		Iterator<Class<?>> it = clazzes.iterator();
		
		
		
		while(it.hasNext()){
			Class<?> c = it.next();
			if(beanName.equals(c.getName()))
				try {
					return c.newInstance();
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		}
		
		return null;
	
	}
	
	public String[] getBeanNamesForType(Class<?> type){
		
		Iterator<Class<?>> it = clazzes.iterator();
		
		List<String> strs = new ArrayList<String>();
		
		while(it.hasNext()){
			Class<?> c = it.next();
			if(type.isAssignableFrom(c))
			strs.add(c.getName());
		}
		
		return (String[])strs.toArray(new String[strs.size()]);
	}
	
	
}
