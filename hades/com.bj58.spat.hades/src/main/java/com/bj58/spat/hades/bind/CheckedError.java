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


/**
 * 校验和数据绑定时产生错误的存储实体
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 *
 */
public class CheckedError {
	
	/**
	 * 错误来源：绑定，校验
	 */
	private ErrorType errorType;
	
	/**
	 * 绑定对象的名称
	 */
	private String targetName;
	
	/**
	 * 错误消息
	 */
	private String message;
	
	/**
	 * 错误值
	 */
	//private Object invalidValue;
	
	public CheckedError(ErrorType errorType, String targetName, String message) {
		super();
		this.errorType = errorType;
		this.targetName = targetName;
		this.message = message;
	}
	
	/**
	 * @return the targetName
	 */
	public String getTargetName() {
		return targetName;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * @return the errorType
	 */
	public ErrorType getErrorType() {
		return errorType;
	}
	/**
	 * @return the invalidValue
	 */
//	public Object getInvalidValue() {
//		return invalidValue;
//	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ValidateError [targetName=" + targetName + ", message="
				+ message + "]";
	}
	
	public enum ErrorType{
		BIND,
		VALIDATE
	}
}
