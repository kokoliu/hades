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
package com.bj58.spat.hades.invoke;

import java.util.Map;

import com.bj58.spat.hades.ActionResult;
import com.bj58.spat.hades.BeatContext;
import com.bj58.spat.hades.bind.BindAndValidate;
import com.bj58.spat.hades.bind.ObjectBindResult;
import com.bj58.spat.hades.invoke.converter.ConverterFactory;
import com.bj58.spat.hades.log.Log;
import com.bj58.spat.hades.log.LogFactory;
import com.bj58.spat.hades.route.ActionInfo;
import com.bj58.spat.hades.route.RouteResult;

/**
 * Action调用类用于完成对Action 的匹配和调用操作。
 * 
 *  @author Service Platform Architecture Team (spat@58.com)
 */
public class ActionInvoker {
	
	private static ConverterFactory converter = new ConverterFactory();
	
	final static Log log = LogFactory.getLog(ActionInvoker.class);
	
	/**
	 * 执行匹配结果
	 * @return
	 * @throws Exception
	 */
	public Object invoke(RouteResult match) throws Exception {
        try {
        	
        	ActionInfo action = match.action;
        	Map<String, String> urlParams = match.urlParams;
        	BeatContext beat = match.beat;
        	
        	// 处理拦截器 2010-12-07
        	ActionResult preResult = action.getInterceptorHandler().preExecute(beat);
        	if(preResult != null){
        		return preResult;
        	}
        	
        	// 处理参数
        	Class<?>[] paramTypes = action.getParamTypes();
    		Object[] param = new Object[paramTypes.length];
    		for(int i = 0; i < action.getParamNames().length; i++){
    			String paramName = action.getParamNames()[i];
    			Class<?> clazz = paramTypes[i];
    			
    			String v = urlParams.get(paramName);
    			
    			if (v != null) {
    				if(converter.canConvert(clazz)){
        				param[i] = converter.convert(clazz, v);
        				continue;
        			}
    			}
    			
    			if(converter.canConvert(clazz)) continue;
    			
    			// 绑定数据
    			ObjectBindResult br = BindAndValidate.bind(clazz, beat);
    			beat.getBindResults().add(br);
    			param[i] = br.getTarget();
    			
    			// 校验
//    			beat.getBindResults().add(BindAndValidate.Singleton().validate(param[i]));    			
    		}
    		
    		// 计时
    		Long t1= System.currentTimeMillis();
    		
    		Object result = null;
    		
    		//用于捕获执行具体Action时出现的错误并交给ExceptionInterceptor
    		
			try {
				result = action.getActionMethod().invoke(action.getController(), param);
			} catch (Exception e) {
				
				ActionResult exceptionResult = action.getInterceptorHandler().exceptionExecute(beat);
	    		if(exceptionResult != null){
	        		return exceptionResult;
	        	}
			}
    		Long t2=System.currentTimeMillis();
    		Long t = t2 - t1;
    		
    		// 时间大于，记日志
    		if (t >= 100){
    			log.info("time:"  + t + "ms, url:" + beat.getClient().getRelativeUrl());
    		}
    		
    		//处理后置拦截器
    		ActionResult afterResult = action.getInterceptorHandler().afterExecute(beat);
        	
    		if(afterResult != null){
        		return afterResult;
        	}
    		return result ;
        }
        catch (Exception e) {
            Throwable t = e.getCause();
            if (t!=null && t instanceof Exception)
                throw (Exception) t;
            throw e;
        }
	}
}
