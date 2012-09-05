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
package com.bj58.spat.hades.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.bj58.spat.hades.cache.OutputCacheInterceptor;


/**
 * 用于处理页面缓存的拦截器，当方法被@see com.bj58.spat.hades.annotation.OutputCache 标识后
 * 该方法的请求先会被 OutputCacheInterceptor 处理
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 *
 */
@Interceptor(OutputCacheInterceptor.class)
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented

public @interface OutputCache {
	
	/**
	 * 可以定义拦截器的的优先级，0，1，2,...
	 * @return
	 */
	int order() default  1;
	
	/**
	 * 定义缓存的秒，Gets or sets the cache duration, in seconds.
	 * @return
	 */
	int duration() default 60;
}
