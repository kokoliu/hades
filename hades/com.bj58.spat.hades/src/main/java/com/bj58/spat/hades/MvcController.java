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

import com.bj58.spat.hades.annotation.Ignored;
import com.bj58.spat.hades.log.Log;
import com.bj58.spat.hades.log.LogFactory;
import com.bj58.spat.hades.thread.BeatContextLocal;

/**
 * 所有的Controller基类
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public abstract class MvcController {
	
	/**
	 * scf服务cache
	 */
//	private static Map<Class<?>, Object> scfCache = new HashMap<Class<?>, Object>();
//	
//	private String scfUrl = "";
	
	/**
	 * 日志系统
	 */
	protected Log log = LogFactory.getLog(this.getClass());

	/**
	 * 
	 * TODO :原来用spring ioc 实现的
	 * 在一个请求过程中的上下文
	 */
	
	protected BeatContext beat = new BeatContextLocal();

	void setBeatContext(BeatContext beat){
		this.beat = beat;
	}
	
	@Ignored
	protected BeatContext getBeatContext() {
		return beat;
	}
	
	/**
	 * 提供scf服务
	 * TODO: 
	 * @param <T>
	 * @param clazz
	 * @return
	 */
//	@SuppressWarnings("unchecked")
//	protected <T> T SysService(Class<T> clazz, String url){
//		T object = (T) scfCache.get(clazz);
//		if (object != null) return object;
//		object = ProxyFactory.create(clazz, url);
//		scfCache.put(clazz, object);
//		return object;
//	}
}
