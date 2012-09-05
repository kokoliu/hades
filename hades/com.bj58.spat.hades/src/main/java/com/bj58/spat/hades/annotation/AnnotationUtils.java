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
package com.bj58.spat.hades.annotation;


import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.WeakHashMap;

import com.bj58.spat.hades.utils.Assert;
import com.bj58.spat.hades.utils.BridgeMethodResolver;

/**
 * Annotation的工具类，处理Annotation
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 * 
 * @see java.lang.reflect.Method#getAnnotations()
 * @see java.lang.reflect.Method#getAnnotation(Class)
 */
public abstract class AnnotationUtils {

	
	static final String VALUE = "value";

	
	/**annotation 的缓存  **/
	private static final Map<Class, Boolean> annotatedInterfaceCache = new WeakHashMap<Class, Boolean>();


	/**
	 * 获取 给定{@link Method}的全部{@link Annotation Annotations}。
	 *
	 * @param method 被查询的method
	 * @return 查找到的annotation数组
	 */
	public static Annotation[] getAnnotations(Method method) {
		return BridgeMethodResolver.findBridgedMethod(method).getAnnotations();
	}

	/**
	 * 获取 给定{@link Method}的单个{@link Annotation Annotations}。
	 * 当根据类型从方法中未找到符合条件的Annotation时，可以通过查找其全部声明并进行类型比较处理。
	 * @param 被查询的method
	 * @param 查找的annotation类型
	 * @return 查找到的annotation
	 */
	public static <A extends Annotation> A getAnnotation(Method method, Class<A> annotationType) {
		
		Method resolvedMethod = BridgeMethodResolver.findBridgedMethod(method);
		A ann = resolvedMethod.getAnnotation(annotationType);
		if (ann == null) {
			for (Annotation metaAnn : resolvedMethod.getAnnotations()) {
				ann = metaAnn.annotationType().getAnnotation(annotationType);
				if (ann != null) {
					break;
				}
			}
		}
		return ann;
	}

	/**
	 * 获取 给定{@link Method}的单个{@link Annotation Annotations}。
	 * 当根据类型从方法中未找到符合条件的Annotation时，查找该方法所在类所实现的接口，看接口是否存在注解。
	 * @param 被查询的method
	 * @param 查找的annotation类型
	 * @return 查找到的annotation
	 */
	public static <A extends Annotation> A findAnnotation(Method method, Class<A> annotationType) {
		A annotation = getAnnotation(method, annotationType);
		Class<?> cl = method.getDeclaringClass();
		if (annotation == null) {
			annotation = searchOnInterfaces(method, annotationType, cl.getInterfaces());
		}
		while (annotation == null) {
			cl = cl.getSuperclass();
			if (cl == null || cl == Object.class) {
				break;
			}
			try {
				Method equivalentMethod = cl.getDeclaredMethod(method.getName(), method.getParameterTypes());
				annotation = getAnnotation(equivalentMethod, annotationType);
				if (annotation == null) {
					annotation = searchOnInterfaces(method, annotationType, cl.getInterfaces());
				}
			}
			catch (NoSuchMethodException ex) {
				// We're done...
			}
		}
		return annotation;
	}

	private static <A extends Annotation> A searchOnInterfaces(Method method, Class<A> annotationType, Class[] ifcs) {
		A annotation = null;
		for (Class<?> iface : ifcs) {
			if (isInterfaceWithAnnotatedMethods(iface)) {
				try {
					Method equivalentMethod = iface.getMethod(method.getName(), method.getParameterTypes());
					annotation = getAnnotation(equivalentMethod, annotationType);
				}
				catch (NoSuchMethodException ex) {
					// Skip this interface - it doesn't have the method...
				}
				if (annotation != null) {
					break;
				}
			}
		}
		return annotation;
	}

	private static boolean isInterfaceWithAnnotatedMethods(Class<?> iface) {
		synchronized (annotatedInterfaceCache) {
			Boolean flag = annotatedInterfaceCache.get(iface);
			if (flag != null) {
				return flag;
			}
			boolean found = false;
			for (Method ifcMethod : iface.getMethods()) {
				if (ifcMethod.getAnnotations().length > 0) {
					found = true;
					break;
				}
			}
			annotatedInterfaceCache.put(iface, found);
			return found;
		}
	}
	
	/**
	 * 获取 给定{@link Class}的单个{@link Annotation Annotations}。
	 * 当根据类型从方法中未找到符合条件的Annotation时，遍历该类的父类或者接口进行查找。
	 * @param 被查询的class
	 * @param 查找的annotation类型
	 * @return 查找到的annotation，或者<code>null</code>
	 */
	public static <A extends Annotation> A findAnnotation(Class<?> clazz, Class<A> annotationType) {
		Assert.notNull(clazz, "Class must not be null");
		A annotation = clazz.getAnnotation(annotationType);
		if (annotation != null) {
			return annotation;
		}
		for (Class<?> ifc : clazz.getInterfaces()) {
			annotation = findAnnotation(ifc, annotationType);
			if (annotation != null) {
				return annotation;
			}
		}
		if (!Annotation.class.isAssignableFrom(clazz)) {
			for (Annotation ann : clazz.getAnnotations()) {
				annotation = findAnnotation(ann.annotationType(), annotationType);
				if (annotation != null) {
					return annotation;
				}
			}
		}
		Class<?> superClass = clazz.getSuperclass();
		if (superClass == null || superClass == Object.class) {
			return null;
		}
		return findAnnotation(superClass, annotationType);
	}


	/**
	 * 获取声明中的<em>value</em>值
	 * @param 被取值的声明
	 * @return value的值, 或者 <code>null</code> 
	 * @see #getValue(Annotation, String)
	 */
	public static Object getValue(Annotation annotation) {
		return getValue(annotation, VALUE);
	}

	/**
	 * 获取声明中的某属性值
	 * @param 被取值的声明
	 * @param 属性名
	 * @return value的值, 或者 <code>null</code> 
	 * @see #getValue(Annotation)
	 */
	public static Object getValue(Annotation annotation, String attributeName) {
		try {
			Method method = annotation.annotationType().getDeclaredMethod(attributeName, new Class[0]);
			return method.invoke(annotation);
		}
		catch (Exception ex) {
			return null;
		}
	}
}

