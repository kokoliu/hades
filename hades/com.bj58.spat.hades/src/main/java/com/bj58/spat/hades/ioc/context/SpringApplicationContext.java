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
package com.bj58.spat.hades.ioc.context;


//public class SpringApplicationContext implements ServiceContext{
//	
//	private static ApplicationContext context ;
//	
//	public SpringApplicationContext(String configLocation){
//		
//		if(context != null)
//			return;
//		context = new FileSystemXmlApplicationContext("file:"+configLocation);
//	}
//
//	@Override
//	public Object getBean(String name) {
//		
//		return context.getBean(name);
//	}
//	
//	
//	public <T> T getBean(String name, Class<T> requiredType) {
//		
//		return context.getBean(name, requiredType);
//	}
//	
//	
//	public <T> T getBean(Class<T> requiredType) {
//		
//		return context.getBean(requiredType);
//	}
//	
//	public Object getBean(String name, Object... args) {
//
//		return context.getBean(name, args);
//	}
//	@Override
//	public boolean containsBean(String name) {
//
//		return context.containsBean(name);
//	}
//	@Override
//	public boolean isSingleton(String name) {
//
//		return context.isSingleton(name);
//	}
//}
