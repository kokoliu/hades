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

import java.io.IOException;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bj58.spat.hades.log.Log;
import com.bj58.spat.hades.log.LogFactory;

/**
 *  利用Filter来实行调度
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
@WebFilter(urlPatterns = {"/*"},  
         dispatcherTypes = {DispatcherType.REQUEST},  
         initParams = {@WebInitParam(name = "encoding", value = "UTF-8")}   
)
public class MvcFilter implements Filter {
	
	protected final static Log log = LogFactory.getLog(MvcFilter.class);

	/**
	 * 调度器
	 */
    private static MvcDispatcher dispatcher ;
	private static volatile boolean hasInitial = false;
    
    /**
     * 静态文件处理器
     */
    private static StaticFileHandler staticFileHandler = new StaticFileHandler();
    
    private static ServletContext sc;
    
    /**
     * 对请求的url进行处理
     * 先用调度器处理，如果调度器处理不成功，则使用静态文件处理器来处理
     */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
	
        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpServletResponse httpResp = (HttpServletResponse) response;
        String method = httpReq.getMethod();
        if ("GET".equals(method) || "POST".equals(method)) {
            try {
				if (!dispatcher.service(httpReq, httpResp))
				    staticFileHandler.handle(httpReq, httpResp);
			} catch (Exception e) {
				log.error(e.getMessage(),e);
				e.printStackTrace();
				httpResp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			}
            return;
        }
        
        httpResp.sendError(HttpServletResponse.SC_NOT_FOUND);		
	}
	
	/**
	 * 初始化
	 */
	@Override
	public void init(FilterConfig config) throws ServletException {
		
		System.out.println("Filter:" + this);
		synchronized(MvcFilter.class) {
			if (hasInitial) return;
			hasInitial = true;
		}

		log.info("Mvc WebApplication initialing");
		System.out.println(HADES.getNamespace() + ": initialing ...");
		sc = config.getServletContext();
		
		dispatcher = new MvcDispatcher(this.getServletContext());
		log.info("Startted Mvc WebApplication");
		System.out.println(HADES.getNamespace() + ": Startted Mvc WebApplication");
	}
	
	@Override
	public void destroy() {
		System.out.println("destroy filter:" + this);
	}
	
	private ServletContext getServletContext() {
		return sc;
	}

}
