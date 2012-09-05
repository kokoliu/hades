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
package com.bj58.spat.hades.route.interceptor;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bj58.spat.hades.ActionResult;
import com.bj58.spat.hades.BeatContext;
import com.bj58.spat.hades.annotation.AnnotationUtils;
import com.bj58.spat.hades.annotation.Interceptor;
import com.bj58.spat.hades.annotation.Interceptor.InterceptorType;
import com.bj58.spat.hades.interceptor.ActionInterceptor;
import com.bj58.spat.hades.route.ActionInfo;
import com.bj58.spat.hades.utils.BeanUtils;

/**
 * 拦截器的处理器。
 * 按照顺序依次执行各类型的拦截器。
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class InterceptorHandler {
	
	/**
	 * 前置拦截器容器
	 */
	List<OrderActionInterceptor> actionInterceptors =  new ArrayList<OrderActionInterceptor>();
	
	/**
	 * 后置拦截器容器
	 */
	List<OrderActionInterceptor> resultInterceptors =  new ArrayList<OrderActionInterceptor>();
	
	/**
	 * 异常拦截器
	 */
	List<OrderActionInterceptor> exceptionInterceptors =  new ArrayList<OrderActionInterceptor>();
	
	/**
	 * 按顺序执行前置拦截器
	 * @param beat
	 * @return
	 */
	public ActionResult preExecute(BeatContext beat){
		for(OrderActionInterceptor oai : actionInterceptors){
			
			ActionResult result = oai.interceptor.preExecute(beat);
			if (result != null) return result;			
		}
		
		return null;
	}
	
	/**
	 * 按顺序执行后置拦截器
	 * @param beat
	 * @return
	 */
	public ActionResult afterExecute(BeatContext beat){
		for(OrderActionInterceptor oai : resultInterceptors){
			ActionResult result = oai.interceptor.preExecute(beat);
			if (result != null) return result;
		}
		
		return null;
	}
	
	/**
	 * 按顺序执行异常拦截器
	 * @param beat
	 * @return
	 */
	public ActionResult exceptionExecute(BeatContext beat){
		for(OrderActionInterceptor oai : exceptionInterceptors){
			ActionResult result = oai.interceptor.preExecute(beat);
			if (result != null) return result;
		}
		
		return null;
	}
	/**
	 * 
	 * 增加一个拦截器
	 * @param ai
	 * @param order
	 */
	public void add(ActionInterceptor ai, int order, InterceptorType type){
		
		if(type == InterceptorType.ACTION){
			int index = 0;
			for(; index < actionInterceptors.size(); index++)
				if (order < actionInterceptors.get(index).order)
					break;
			
			actionInterceptors.add(index, new OrderActionInterceptor(order, ai));	
		}
		
		if(type == InterceptorType.RESULT){
			int index = 0;
			for(; index < resultInterceptors.size(); index++)
				if (order < resultInterceptors.get(index).order)
					break;
			
			resultInterceptors.add(index, new OrderActionInterceptor(order, ai));	
		}
		
		if(type == InterceptorType.EXECEPTION){
			int index = 0;
			for(; index < exceptionInterceptors.size(); index++)
				if (order < exceptionInterceptors.get(index).order)
					break;
			
			exceptionInterceptors.add(index, new OrderActionInterceptor(order, ai));	
		}
	}
	
	/**
	 * 根据ActionInfo获得方法的所有拦截器
	 * @param actionInfo
	 */
	public void build(ActionInfo actionInfo){

		for(Annotation ann : actionInfo.getAnnotations()){
			// 判断ann是否是拦截器
			Class<?> clazz = ann.getClass();
			Interceptor icp = AnnotationUtils.findAnnotation(clazz, Interceptor.class);
			if (icp == null) continue;
			
			
			InterceptorType type = icp.type();
			Class<? extends ActionInterceptor> aiClazz = icp.value();
			ActionInterceptor interceptor = ActionInterceptorFactory(aiClazz);
			Object orderObject = AnnotationUtils.getValue(ann, "order");
			int order = orderObject == null ? 1
					: (Integer)orderObject;   // TODO: maybe throw exception.

				add(interceptor, order, type);

		}
	}
	
	/**
	 * 有序的前置拦截器
	 * @author liuzw
	 *
	 */
	class OrderActionInterceptor{
		int order;
		ActionInterceptor interceptor;
		public OrderActionInterceptor(int order, ActionInterceptor interceptor) {
			super();
			this.order = order;
			this.interceptor = interceptor;
		}		
	}
	
	private static Map<Class<?>, ActionInterceptor> actionInterceptorMap = new HashMap<Class<?>, ActionInterceptor>();
	
	private static <T extends ActionInterceptor>  ActionInterceptor ActionInterceptorFactory(Class<T> clazz){
		ActionInterceptor actionInterceptor = actionInterceptorMap.get(clazz);
		if (actionInterceptor == null){
			actionInterceptor = BeanUtils.instantiate(clazz);
			actionInterceptorMap.put(clazz, actionInterceptor);
		}
		return actionInterceptor;
	}

}
