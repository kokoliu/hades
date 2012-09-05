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


/**
 * 用于管理在一个beat过程中所有绑定和校验信息
 * @author Service Platform Architecture Team (spat@58.com)
 *
 */
public class BeatBindResults {
	
	/**
	 * 绑定结果列表
	 */
	private List<ObjectBindResult> results = new ArrayList<ObjectBindResult>();
	
	/**
	 * 根据目标对象获得绑定信息
	 * @param target
	 * @return
	 */
	public ObjectBindResult get(Object target){
		for(ObjectBindResult result : results)
			if (result.getTarget().equals(target)) return result;
		return null;
	}
	
	/**
	 * 增加绑定信息
	 * 如果目标对象已存在，则合并，否则增加
	 * @param other
	 */
	public void add(ObjectBindResult other){
		for(ObjectBindResult result : results)
			if (result.getTarget().equals(other.getTarget())){
				result.merge(other);
				return;
			}
		
		results.add(other);
	}
	
	/**
	 * 得到所有有错误的绑定信息
	 * @return
	 */
	public List<ObjectBindResult> getErrorBindResults(){
		List<ObjectBindResult> errorResults = new ArrayList<ObjectBindResult>();
		
		for(ObjectBindResult result : results)
			if (result.getErrorCount() > 0)
				errorResults.add(result);
			
		return errorResults;
	}
	
	/**
	 * 是否有错误
	 * @return
	 */
	public boolean hasError(){
		return (getErrorBindResults().size() > 0) ? true : false;
	}
	
	/**
	 * 所有校验和数据绑定错误
	 * @return
	 */
	public CheckedError[] getErrors(){
		List<CheckedError> errors = new ArrayList<CheckedError>();
		for(ObjectBindResult br : getErrorBindResults()){
			for(CheckedError e : br.getErrors())
				errors.add(e);
		}
		return errors.toArray(new CheckedError[errors.size()]);
	}

}
