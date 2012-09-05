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
package com.bj58.spat.hades.interceptor;

import java.lang.reflect.Method;

import com.bj58.spat.hades.ActionResult;
import com.bj58.spat.hades.BeatContext;
import com.bj58.spat.hades.annotation.AnnotationUtils;
import com.bj58.spat.hades.annotation.ParamWithoutValidate;

/**
 * 参数认证拦截器，用于标识哪些参数不需要进行HTML和SQL过滤
 * 。
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 *
 */
public class ParamValidateInterceptor implements ActionInterceptor {

	@Override
	public ActionResult preExecute(BeatContext beat) {
		
		Method method = beat.getAction().getActionMethod();
		
		ParamWithoutValidate nci = AnnotationUtils.findAnnotation(method, ParamWithoutValidate.class);
		
		if(nci !=null){
			String[] paramsWithoutValidate = nci.value();
			beat.setParamWithoutValidate(paramsWithoutValidate);
		}
		
		return null;
	}

}
