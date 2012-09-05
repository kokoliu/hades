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
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.bj58.spat.hades.BeatContext;
import com.bj58.spat.hades.binder.BindingResult;
import com.bj58.spat.hades.binder.RequestBinder;
import com.bj58.spat.hades.utils.BeanUtils;


/**
 * 用于初始化绑定和校验工具并且完成绑定操作。
 * 目前使用的校验工具为JSR303的参考实现Hibernate Validator。
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 *
 */
public class BindAndValidate {
	
	private static Validator validator;
	
	
	static{
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		if(validator == null)
			validator = factory.getValidator();
	}
	/**
	 * 根据beat信息绑定一个目标对象
	 */
	private static ObjectBindResult bind(Object target, BeatContext beat){
		RequestBinder binder = new RequestBinder(target);
		binder.bind(beat.getRequest());
		BindingResult r = binder.getBindingResult();
		return new ObjectBindResult(r);	
	}
	
	/**
	 * 根据beat信息,和目标对象的类型，绑定一个目标对象
	 * @param targetType
	 * @param beat
	 * @return
	 * @throws Exception
	 */
	public static ObjectBindResult bind(Class<?> targetType, BeatContext beat) throws Exception{
		Object target = BeanUtils.instantiateClass(targetType);
		return bind(target, beat);
	}
	
	/**
	 * 校验一个目标对象
	 * @param <T>
	 * @param target
	 * @return
	 */
	public static <T> ObjectBindResult validate(T target){;
		//javax.validation.ValidatorFactory.
		
		Set<ConstraintViolation<T>> constraintViolations = validator.validate(target);
		List<CheckedError> errors = new ArrayList<CheckedError>();
		for(ConstraintViolation<T> constraintViolation : constraintViolations) {
			CheckedError error = new CheckedError(CheckedError.ErrorType.VALIDATE,
					constraintViolation.getPropertyPath().toString(), 
					constraintViolation.getMessage());
			errors.add(error);
		}	
		return new ObjectBindResult(target, errors);
	}
	
	
}
