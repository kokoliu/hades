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


import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import com.bj58.spat.hades.MvcController;
import com.bj58.spat.hades.annotation.AnnotationUtils;
import com.bj58.spat.hades.annotation.GET;
import com.bj58.spat.hades.annotation.Ignored;
import com.bj58.spat.hades.annotation.POST;
import com.bj58.spat.hades.annotation.Path;
import com.bj58.spat.hades.context.HADESApplicationContext;
import com.bj58.spat.hades.utils.ClassUtil;
import com.bj58.spat.hades.utils.ReflectionUtils;

/**
 * 控制器相关的信息。
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
class ControllerInfo {
	
	public Object controller;
	
	public Path path = null;
	
	public boolean isGet = false;
	public boolean isPost = false;

	public Set<ActionInfo> actions = new LinkedHashSet<ActionInfo>();
	
	/**
	 * 得到所有的Annotion
	 * 2010-12-7
	 */
	public Set<Annotation> annotations = new LinkedHashSet<Annotation>();
	
	public int size() {
		return actions.size();
	}
	
	public boolean hasTypePath(){
		return path == null ? false : true;
	}
	
	public String[] getUrlTypeLevelPatterns(){
		return path == null ? new String[0] : path.value();
	}
	
	private ControllerInfo(String beanName, HADESApplicationContext context) {
		Class<?> handlerType = context.getType(beanName);
	
        path = AnnotationUtils.findAnnotation(handlerType, Path.class);
        
		isGet = AnnotationUtils.findAnnotation(handlerType, GET.class) == null ?
				false : true;
		isPost = AnnotationUtils.findAnnotation(handlerType, POST.class) == null ?
				false : true;
		
		if(!isGet && !isPost) {
			isGet = true;
			isPost = true;
		}
		
		controller = context.getBean(beanName);
		
		// 2010-12-07
		for(Annotation ann : handlerType.getAnnotations())
			annotations.add(ann);
		
		
		// 处理，增加Action
		ReflectionUtils.doWithMethods(handlerType, new ReflectionUtils.MethodCallback() {
			public void doWith(Method method) {
				ActionInfo action = ActionInfo.Factory(ControllerInfo.this, method);
				actions.add(action);
			}
		}, RouteUtils.HANDLER_METHODS
		);		
	}
	
	static HashMap<String, ControllerInfo> classMaps = new LinkedHashMap<String, ControllerInfo>();
	
	public static ControllerInfo Factory(String beanName, HADESApplicationContext context) {
		Class<?> handlerType = context.getType(beanName);
		ControllerInfo c = classMaps.get(beanName);
		if (c != null) return c;
		
		// 查找合适的Ctroller作为mapping
		if (! handlerType.getName().endsWith("Controller")) return null;
		if (! ClassUtil.isAssignable(MvcController.class, handlerType)) return null;
		String packageName = ClassUtil.getPackageName(handlerType);
		if (! packageName.startsWith("com.bj58.") ||!  packageName.endsWith("controllers"))
			return null;
        if (AnnotationUtils.findAnnotation(handlerType, Ignored.class) != null) return null;
        
        ControllerInfo ce = new ControllerInfo(beanName, context);
        classMaps.put(beanName, ce);
        
        return ce;
        
	}

}
