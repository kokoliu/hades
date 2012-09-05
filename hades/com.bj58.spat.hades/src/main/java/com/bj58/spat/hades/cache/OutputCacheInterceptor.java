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
package com.bj58.spat.hades.cache;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.bj58.spat.hades.ActionResult;
import com.bj58.spat.hades.BeatContext;
import com.bj58.spat.hades.annotation.AnnotationUtils;
import com.bj58.spat.hades.annotation.OutputCache;
import com.bj58.spat.hades.interceptor.ActionInterceptor;

/**
 * 实现的一个方法拦截器。
 * @author Service Platform Architecture Team (spat@58.com)
 *
 */
public class OutputCacheInterceptor implements ActionInterceptor {
	
	private Map<Method, Integer> methodMap =new ConcurrentHashMap<Method, Integer>();
	

	@Override
	public ActionResult preExecute(BeatContext beat) {
		int duration = -1;
		Method method = beat.getAction().getActionMethod();
		
		Integer intDuration = methodMap.get(method);
		
		duration = intDuration == null ? -1 : intDuration;
		if (duration == -1) {

			OutputCache oc = AnnotationUtils.findAnnotation(method, OutputCache.class);
			duration = oc.duration();
			if (duration < 0)  duration = 60;
			methodMap.put(method, Integer.valueOf(duration));
		}
		
		CacheContext cache = beat.getCache();
		
		ActionResult result = cache.getCacheResult();
		if (result == null) {
			cache.setExpiredTime(duration);
			cache.needCache();
		}
		
		return result;
	}


}