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
package com.bj58.spat.hades.exception;

import java.lang.reflect.InvocationTargetException;

import com.bj58.spat.hades.utils.Assert;


/**
 * 异常处理工具类
 * 
 *
 * @author Service Platform Architecture Team (spat@58.com)
 *
 */
public abstract class ExceptionUtils {

	/**
	 * 为基本信息和根异常绑定消息
	 * @param message
	 * @param cause
	 * @return message
	 */
	public static String buildMessage(String message, Throwable cause) {
		if (cause != null) {
			StringBuilder sb = new StringBuilder();
			if (message != null) {
				sb.append(message).append("; ");
			}
			sb.append("base exception is ").append(cause);
			return sb.toString();
		}
		else {
			return message;
		}
	}

	/**
	 * 判断是否为强制查检类型异常
	 * @param ex
	 * @return
	 */
	public static boolean isCheckedException(Throwable ex) {
		return !(ex instanceof RuntimeException || ex instanceof Error);
	}

	/**
	 * 检查是否给定的例外是Throwable兼容的异常类型
	 * @param ex the exception to checked
	 * @param declaredExceptions the exceptions declared in the throws clause
	 * @return whether the given exception is compatible
	 */
	@SuppressWarnings("unchecked")
	public static boolean isCompatibleWithThrowsClause(Throwable ex, Class[] declaredExceptions) {
		if (!isCheckedException(ex)) {
			return true;
		}
		if (declaredExceptions != null) {
			int i = 0;
			while (i < declaredExceptions.length) {
				if (declaredExceptions[i].isAssignableFrom(ex.getClass())) {
					return true;
				}
				i++;
			}
		}
		return false;
	}

	public static BaseRuntimeException makeThrow(String message){
		return new BaseRuntimeException(message) {
			private static final long serialVersionUID = 5688687621368786756L;
		};
	}
	public static BaseRuntimeException makeThrow(String message, Throwable cause){
		return new BaseRuntimeException(message,cause) {
			private static final long serialVersionUID = 5688687621368786756L;
		};
	}
	public static BaseRuntimeException makeThrow(String format, Object...message){
		return makeThrow(String.format(format, message));
	}

	public static Throwable getTargetException(Exception exception){
		Assert.notNull(exception);
		if(exception instanceof InvocationTargetException){
			Throwable target = ((InvocationTargetException)exception).getTargetException();
			if(target != null){
				return target;
			}
		}

		return exception;
	}

}
