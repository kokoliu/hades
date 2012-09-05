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
package com.bj58.spat.hades.binder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * HADES自实现的错误对象实体
 * @author Service Platform Architecture Team (spat@58.com)
 *
 */
public class RequestBinder {

	
	private Object target;
	
	private List<ObjectError> objectErrors = new ArrayList<ObjectError> ();
	
	public RequestBinder(Object target){
		this.target = target;
	}
	
	public void bind(HttpServletRequest httpServletRequest){
		
		Field[] fields = target.getClass().getDeclaredFields();
		
		for(Field field : fields){
			String paraValue = null;
			try {
				 paraValue = httpServletRequest.getParameter(field.getName());
				if(paraValue != null){
					
					Method m = (Method) target.getClass().getMethod("set" + getMethodName(field.getName()),field.getType());
					m.invoke(target, paraValue);
					
				}
			} catch (Exception e) {
				System.out.println(e);
				ObjectError error = new ObjectError();
				error.setObjectName(target.getClass().getName()+" : "+field.getName());
				error.setErrorMessage(paraValue);
				objectErrors.add(error);
			}
		}
	}

	
	public BindingResult getBindingResult(){
		
		BindingResult br = new BindingResult();
		br.setTarget(target);
		br.setErrors(objectErrors);
		
		return br;
	}
	
    private static String getMethodName(String fildeName) throws Exception{  
        byte[] items = fildeName.getBytes();  
        items[0] = (byte) ((char) items[0] - 'a' + 'A');  
        return new String(items);  
    } 
}
