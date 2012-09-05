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
package com.bj58.spat.hades.bind;

import java.util.ArrayList;
import java.util.List;

import com.bj58.spat.hades.binder.BindingResult;
import com.bj58.spat.hades.binder.ObjectError;

/**
 * 存储绑定结果
 * @author Service Platform Architecture Team (spat@58.com)
 *
 */
public class ObjectBindResult {

	private Object target;
	private List<CheckedError> errors = new ArrayList<CheckedError>();
	
	ObjectBindResult(BindingResult result){
		List<ObjectError> bindErrors = result.getErrors();
		for(ObjectError springError : bindErrors){
			// TODO: 错误值是多少？
			CheckedError error = new CheckedError(CheckedError.ErrorType.BIND,
					springError.getObjectName(),
					springError.getErrorMessage()); 
			errors.add(error);
		}
		
		this.target = result.getTarget();
	}
	
	
	public ObjectBindResult(Object target, List<CheckedError> errors) {
		super();
		this.target = target;
		this.errors = errors;
	}

	/**
	 * 绑定目标
	 * @return
	 */
	public Object getTarget(){
		return this.target;
	}
	
	/**
	 * 绑定错误
	 * @return
	 */
	public List<CheckedError> getErrors(){
		return errors;
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
	public void merge(ObjectBindResult result){
		this.errors.addAll(result.getErrors());
	}
}
