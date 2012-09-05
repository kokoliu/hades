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
package com.bj58.spat.hades.route;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Set;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

import org.apache.commons.lang.StringUtils;

import com.bj58.spat.hades.ActionAttribute;
import com.bj58.spat.hades.BeatContext;
import com.bj58.spat.hades.annotation.AnnotationUtils;
import com.bj58.spat.hades.annotation.GET;
import com.bj58.spat.hades.annotation.POST;
import com.bj58.spat.hades.annotation.Path;
import com.bj58.spat.hades.route.interceptor.InterceptorHandler;
import com.bj58.spat.hades.utils.AntPathMatcher;
import com.bj58.spat.hades.utils.PathMatcher;

/**
 * 对url的一个执行器
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class ActionInfo implements ActionAttribute {
	
	/**
	 * 父层容器
	 */
	private ControllerInfo controllerInfo;

	/**
	 * 处理方法
	 */
	private Method actionMethod;
	
	/**
	 * 利用Ant匹配模型处理url
	 */
	private PathMatcher pathMatcher = new AntPathMatcher();
	
	/**
	 * 是否是http GET方法
	 */
	private boolean isGet = false;
	
	/**
	 * 是否是http POST方法
	 */
	private boolean isPost = false;

	/**
	 * 所能处理的url模式数组
	 */
	private String[] mappedPatterns = new String[0];
	
	/**
	 * 方法中参数名称数组
	 */
	private String[] paramNames = new String[0];
	
	/**
	 * 得到所有的Annotion
	 * 2010-12-7
	 */
	private Set<Annotation> annotations = new LinkedHashSet<Annotation>();
	
	private InterceptorHandler interceptorHandler = new InterceptorHandler();
	
	/**
	 * 得到所有的Annotion
	 * 2010-12-7
	 */
	public Set<Annotation> getAnnotations() {
		return annotations;
	}
	
	/**
	 * 获得拦截器
	 * @return
	 */
	public InterceptorHandler getInterceptorHandler(){
		return interceptorHandler;
	}

	/**
	 * 方法中参数类型数组
	 */
	Class<?>[] paramTypes = new Class<?>[0];
	
	/**
	 * 得到方法中参数类型数组
	 * @return 方法中参数类型数组
	 */
	public Class<?>[] getParamTypes() {
		return paramTypes;
	}

	/**
	 * 得到方法中参数名称数组
	 * @return the paramNames
	 */
	public String[] getParamNames() {
		return paramNames;
	}

	/**
	 * 得到父层controller对象，
	 * 不是类，因为每个Controller类是个单体类
	 * @return the controller
	 */
	public Object getController() {
		return controllerInfo.controller;
	}

	/**
	 * @param controller the controller to set
	 */
	void setController(ControllerInfo controller) {
		this.controllerInfo = controller;
		
		//this.actionMethod .getGenericParameterTypes()
	}

	/**
	 * 对应的处理方法
	 * @return the actionMethod
	 */
	public Method getActionMethod() {
		return actionMethod;
	}

	/**
	 * 是否是http GET
	 * @return the isGet
	 */
	public boolean isGet() {
		return isGet;
	}

	/**
	 * 是否是http Post
	 * @return the isPost
	 */
	public boolean isPost() {
		return isPost;
	}

	/**
	 * @return the mappedPatterns
	 */
	public String[] getMappedPatterns() {
		return mappedPatterns;
	}
	
	/**
	 * 测试本处理器是否能处理对应的请求的http方法
	 * @param beat 请求的上下文
	 * @return 如果本处理器能可以处理，true
	 */
	public boolean matchRequestMethod(BeatContext beat){
		String requestMethod = beat.getRequest().getMethod();	
		if(StringUtils.equalsIgnoreCase(requestMethod, "GET") && (!isGet)) return false;
		if(StringUtils.equalsIgnoreCase(requestMethod, "POST") && (!isPost)) return false;
		return true;
		
	}
	
	/**
	 * 构造函数
	 * @param controller 父层容器
	 * @param method 对应方法
	 */
	private ActionInfo(ControllerInfo controller, Method method){
		
		this.controllerInfo = controller;
		this.actionMethod = method;
		
		Path path = AnnotationUtils.findAnnotation(method, Path.class);
		String[] pathPatterns = path != null? path.value() : new String[] {method.getName()};
		
		Set<String> pathUrls = new LinkedHashSet<String>();
		
		for (String mappedPattern : pathPatterns) {
			if (!controller.hasTypePath() && (mappedPattern.length() == 0 || mappedPattern.charAt(0) != '/')) {
				mappedPattern = "/" + mappedPattern;
			}
			pathUrls.add(mappedPattern);
		}
		
		buildUrlsByController(pathUrls);
        
		isGet = AnnotationUtils.findAnnotation(method, GET.class) == null ?
				false : true;
		isPost = AnnotationUtils.findAnnotation(method, POST.class) == null ?
				false : true;
		
		if(!isGet && !isPost) {
			isGet = controller.isGet;
			isPost = controller.isPost;
		}
		
		//Class<?> clazz = controller.controller.getClass();
		paramTypes = method.getParameterTypes();		
		paramNames = getMethodParamNames();

		
		
		// 2010-12-07
		for(Annotation ann : controller.annotations)
			this.annotations.add(ann);

		for(Annotation ann : method.getAnnotations())
			annotations.add(ann);
		
		// 2010-12-7 初始化拦截器
		interceptorHandler.build(this);
	}
	
	/**
	 * 构建url模式
	 * @param pathUrls
	 */
	private void buildUrlsByController(Set<String> pathUrls){
		
		String[] controllerPatterns = controllerInfo.getUrlTypeLevelPatterns();
		
		Set<String> urlPatterns = new LinkedHashSet<String>();
		
		for (String controllerPattern : controllerPatterns) {
			if (controllerPattern.length() == 0 || controllerPattern.charAt(0) != '/') {
				controllerPattern = "/" + controllerPattern;
			}

			for (String methodLevelPattern : pathUrls) {
				String combinedPattern = pathMatcher.combine(controllerPattern, methodLevelPattern);
				addUrlsForPath(urlPatterns, combinedPattern);
			}
		}
		
		if (controllerPatterns.length == 0){
			for (String methodLevelPattern : pathUrls) {
				if (methodLevelPattern.length() == 0 || methodLevelPattern.charAt(0) != '/') 
					methodLevelPattern = "/" + methodLevelPattern;
				addUrlsForPath(urlPatterns, methodLevelPattern);
			}
		}
		
		this.mappedPatterns = urlPatterns.toArray(new String[urlPatterns.size()]);
		
		System.out.println(">>mappedPatterns : " + this.actionMethod);
		for(String p : mappedPatterns)
			System.out.println("...  " + p);
	}
	
	/**
	 * Add URLs and/or URL patterns for the given path.
	 * @param urls the Set of URLs for the current bean
	 * @param path the currently introspected path
	 */
	private void addUrlsForPath(Set<String> urls, String path) {
		urls.add(path);
		if (path.indexOf('.') == -1 && !path.endsWith("/")) {
			urls.add(path + ".*");
			urls.add(path + "/");
		}
	}
	
	/**
	 * 得到方法参数名称数组
	 * 由于java没有提供获得参数名称的api，利用了javassist来实现
	 * @return
	 */
	private String[] getMethodParamNames() {
		Class<?> clazz = this.controllerInfo.controller.getClass();
		Method method = this.actionMethod;
		try {
			ClassPool pool = ClassPool.getDefault();
			
			pool.insertClassPath(new ClassClassPath(clazz));

			CtClass cc = pool.get(clazz.getName());
			
			//DEBUG, 函数名相同的方法重载的信息读不到 2011-03-21
//			CtMethod cm = cc.getDeclaredMethod(method.getName());
			
			//2011-03-21
			String[] paramTypeNames = new String[method.getParameterTypes().length]; 
			for (int i = 0; i < paramTypes.length; i++)  
	            paramTypeNames[i] = paramTypes[i].getName();  
			CtMethod cm = cc.getDeclaredMethod(method.getName(), pool.get(paramTypeNames));
			
			// 使用javaassist的反射方法获取方法的参数名
			MethodInfo methodInfo = cm.getMethodInfo();
			
			CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
			LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute
					.getAttribute(LocalVariableAttribute.tag);
			if (attr == null) {
				throw new RuntimeException("class:"+clazz.getName()
						+", have no LocalVariableTable, please use javac -g:{vars} to compile the source file");
			}
			
//			for(int i  = 0 ; i< attr.length() ; i++){
//				System.out.println(i);
//				try {
//					System.out.println("===="+attr.nameIndex(i));
//					System.out.println("===="+attr.index(i));
////					System.out.println("===="+attr.nameIndex(i));
//					System.out.println(clazz.getName()+"================"+i+attr.variableName(i));
//					
//					
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
			//add by lzw 用于兼容jdk 编译时 LocalVariableTable顺序问题
			int startIndex = getStartIndex(attr);
			String[] paramNames = new String[cm.getParameterTypes().length];
			int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
			
			for (int i = 0; i < paramNames.length; i++)
				paramNames[i] = attr.variableName(startIndex + i + pos);
			// paramNames即参数名
			for (int i = 0; i < paramNames.length; i++) {
				System.out.println(paramNames[i]);
			}
			
			return paramNames;

		} catch (NotFoundException e) {
			e.printStackTrace();
			return new String[0];
		}
	}
	
	/**
	 * 类工厂
	 * @param controller
	 * @param method
	 * @return
	 */
	public static ActionInfo Factory(ControllerInfo controller, Method method){

		return new ActionInfo(controller, method);
		
	}

//	@Override
//	public <T extends Annotation> T getAnnotation(
//			Class<T> annotationClass) {
//		
//		return AnnotationUtils.getAnnotation(this.getActionMethod(), annotationClass);
//	}
//	

	private int getStartIndex(LocalVariableAttribute attr){
		
		int startIndex = 0;
		for(int i  = 0 ; i< attr.length() ; i++){
			if("this".equals(attr.variableName(i))){
				startIndex = i;
				break;
			}
		}
		return startIndex;
	}

}
