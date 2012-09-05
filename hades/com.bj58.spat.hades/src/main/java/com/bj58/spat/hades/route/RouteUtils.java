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
package com.bj58.spat.hades.route;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import com.bj58.spat.hades.ActionResult;
import com.bj58.spat.hades.annotation.AnnotationUtils;
import com.bj58.spat.hades.annotation.Ignored;
import com.bj58.spat.hades.utils.ReflectionUtils.MethodFilter;

/**
 * 路由工具类，主要用于生成MethodFilter，从而可以实现对Route的匹配。
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class RouteUtils {
	
	
	/**
	 * Pre-built MethodFilter that matches all non-bridge methods
	 * which are not declared on <code>java.lang.Object</code>.
	 */
	public static MethodFilter HANDLER_METHODS = new MethodFilter() {

		public boolean matches(Method method) {
	        if (AnnotationUtils.findAnnotation(method, Ignored.class) != null) return false;
	        
	        Class<?> returnType = method.getReturnType();
	        if (returnType == null) return false;
	        if (! ActionResult.class.isAssignableFrom(returnType)) return false;
	        
			return (!method.isBridge() && method.getDeclaringClass() != Object.class
					&& Modifier.isPublic(method.getModifiers()));
		}
	};
	




}
