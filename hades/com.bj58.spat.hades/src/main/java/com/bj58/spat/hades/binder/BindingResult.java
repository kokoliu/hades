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

import java.util.List;

/**
 * HADES自实现的绑定结果
 * @author Service Platform Architecture Team (spat@58.com)
 *
 */
public class BindingResult {
	
	private Object target;
	
	private List<ObjectError> errors ;
	
	/**
	 * 绑定目标
	 * @return
	 */
	public Object getTarget(){
		return this.target;
	}
	public void setTarget(Object object){
		this.target = object;
	}
	/**
	 * 绑定错误
	 * @return
	 */
	public List<ObjectError> getErrors(){
		return errors;
	}
	/**
	 * 绑定错误
	 * @return
	 */
	public void setErrors( List<ObjectError> objectErrors){

		errors = objectErrors;
	}
	/**
	 * 得到错误的数量
	 * @return
	 */
	public int getErrorCount(){
		return errors.size();
	}
	
	/**
	 * 合并
	 * @param result
	 */
	public void merge(BindingResult result){
		this.errors.addAll(result.getErrors());
	}
}
