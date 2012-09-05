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
package com.bj58.spat.hades;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bj58.spat.hades.context.MvcWebAppContext;
import com.bj58.spat.hades.invoke.ActionInvoker;
import com.bj58.spat.hades.route.BeatRouter;
import com.bj58.spat.hades.route.RouteResult;
import com.bj58.spat.hades.route.RouterBuilder;
import com.bj58.spat.hades.thread.BeatContextBean;
import com.bj58.spat.hades.thread.BeatContextUtils;
import com.bj58.spat.hades.view.VelocityTemplateFactory;

/**
 *  用于处理Rest请求调度的核心类
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
class MvcDispatcher {
	
	/**
	 * 管理url模版与执行器间的对应关系
	 */
	private BeatRouter router = null;
	
	private ServletContext sc;
	
	private ActionInvoker handler = new ActionInvoker();

	/**
	 * 根据ServletContext建立调度
	 * 主要过程：建立url模版与执行器间的对应关系
	 * @param sc
	 * @throws ServletException
	 */
	public MvcDispatcher(ServletContext sc) throws ServletException {
		this.sc = sc;
		
		/**
		 * 初始化配置文件
		 */
		System.out.println("Starting Mvc Webapplication ...");
		System.out.println("namespace:" + HADES.getNamespace());
		
		VelocityTemplateFactory.init(sc);
		
		MvcWebAppContext context = initWebApplicationtext();
		
		//TODO 
		context.init();

//		BindAndValidate.setApplicationContext(context);
		//actionMapping.setApplicationContext(context);
		
		router = RouterBuilder.build(context);
		
		BeatContextBean.servletContext = sc;
	}
	
	/**
	 * 根据http请求处理
	 * 
	 * 过程：
	 * 查找对应的处理器，执行处理
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public boolean service(HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		
//		 TODO: 这个需要挪个位置
//	     set default character encoding to "utf-8" if encoding is not set:
	    if (request.getCharacterEncoding()==null)
	    	request.setCharacterEncoding("UTF-8");
//	    request.getParameterMap();
		
	    // 包装整个请求的上下文
		BeatContext beat = BeatContextUtils.BeatContextWapper(request, response);
		
		// 查找匹配的结果
		RouteResult match = router.route(beat);
		
		if (match == null) return removeCurrentThread(false);
		
		// 设置Action
		// TODO:硬编码
		((BeatContextBean) beat).setAction(match.action);
		
		// 执行Action
		Object result =  handler.invoke(match); //match.execute();
		
		ActionResult actionResult = (ActionResult) result;
		
		// TODO: 是否需要返回
		if (actionResult == null) return removeCurrentThread(false);
		
		actionResult.render(beat);
		
		
			
		//TODO: Cache 2011-05-16
		beat.getCache().setCacheResult();

		Trace.wrapper(beat);
		
		return removeCurrentThread(true);
		
	}
	
    public void destroy() {
    	
    	System.out.println("end Mvc WebApplication");
//        log.info("Destroy Dispatcher...");
    }
    
    private boolean removeCurrentThread(boolean result){
    	BeatContextUtils.remove();
    	return result;
    }
	
    /**
     * 初始化WebApplicationcontext
     * @return
     */
	private MvcWebAppContext initWebApplicationtext() {
		
//        ApplicationContext oldRootContext = (ApplicationContext) sc.getAttribute(
//                MvcConstants.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
//
//        
//        // 如果web.xml配置使用了spring装载root应用context ...... 不可以
//        // mvcFilter可能因为启动失败，在请求的时候容器还会尝试重新启动，此时rootContext可能已经存在，不要简单地抛出异常
//        // 同时这样留出了使用Listener作为init context的扩展机会
//        if (oldRootContext != null) {
//            if (oldRootContext.getClass() != MvcWebAppContext.class) {
//                throw new IllegalStateException(
//                        "Cannot initialize context because there is already a root application context present - "
//                                + "check whether you have multiple ContextLoader* definitions in your web.xml!");
//            }
//            return (MvcWebAppContext) oldRootContext;
//        }
//        
        MvcWebAppContext context = new MvcWebAppContext(sc, false);
        
//        String contextConfigLocation =MvcConstants.DEFAULT_CONFIG_LOCATION;
//        System.out.println("-------------contextConfigLocation------------"+contextConfigLocation);
//        context.setConfigLocation(contextConfigLocation);
        context.setId("com.bj58.mvcapplicationcontext");
        System.out.println("----MvcWebAppContext----" + context);
		sc.setAttribute(MvcConstants.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, context);
		return context;
	}
}
