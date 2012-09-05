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
package com.bj58.spat.hades.thread;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.bj58.spat.hades.ActionAttribute;
import com.bj58.spat.hades.BeatContext;
import com.bj58.spat.hades.Trace;
import com.bj58.spat.hades.HADESHttpServletRequestWrapper;
import com.bj58.spat.hades.bind.BeatBindResults;
import com.bj58.spat.hades.cache.CacheContext;
import com.bj58.spat.hades.client.ClientContext;
import com.bj58.spat.hades.client.UploadRequest;
import com.bj58.spat.hades.server.ServerContext;
import com.bj58.spat.hades.trace.TraceInfo;
import com.bj58.spat.hades.utils.UrlPathHelper;

/**
 * 用于封装每个请求的上文信息
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class BeatContextBean implements BeatContext {
	
	public static final String BEAT_MODEL_ATTRIBUTE = BeatContextBean.class.getName() + ".MODEL";
	
	public static ServletContext servletContext = null;
	
	private final BeatContext.Model model = new BeatContext.Model();
	private HttpServletRequest request;
	private HttpServletResponse response;
	
	private String relativeUrl = null;
	
	private String[] paramWithoutValidate = null;
	
	private static UrlPathHelper urlPathHelper= new UrlPathHelper(); // TODO: 是否有线程问题
	
	ClientContext client;
	ServerContext server;
	CacheContext cache;
	
	private TraceInfo traceInfo;
	
	ActionAttribute action;
	
	private BeatBindResults bindResults = new BeatBindResults();

	public BeatContextBean(HttpServletRequest request, HttpServletResponse response) {
        
		
		
		HttpServletRequest convertRequest = new HADESHttpServletRequestWrapper(request, this);
		
		request = UploadRequest.wrapper(convertRequest);
		
		setRequest(request);
		
        setResponse(response);
        
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        relativeUrl = uri.substring(contextPath.length());        
        
        // TODO: 为什么是默认的/index.jsp
        if (StringUtils.equals("/index.jsp", relativeUrl)){
        	relativeUrl = "/";
        }

        client = new ClientContext(this);
        server = new ServerContext(this);
        cache = new CacheContext(this);
	    
        Trace.init(this);

    }

	private void setRequest(HttpServletRequest request) {
		this.request = request;		
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	@Override
	public HttpServletRequest getRequest() {
		return request;
	}
//	/**
//	 * 封装一个请求，暂时对其中的请求进行过滤。
//	 */
//	@Override
//	public HttpServletRequest getRequest() {
//		
//		return new ConvertHttpServletRequestWrapper(request);
//	}

	@Override
	public HttpServletResponse getResponse() {
		return response;
	}


	/* (non-Javadoc)
	 * @see com.bj58.spat.hades.mvc.BeatContext#getModel()
	 */
	@Override
	public Model getModel() {
		return model;
	}


	@Override
	public String getRelativeUrl() {
		return this.relativeUrl;
	}


	@Override
	public BeatBindResults getBindResults() {
		return this.bindResults;
	}

	@Override
	public ClientContext getClient() {
		return client;
	}


	@Override
	public ServletContext getServletContext() {
//		if(servletContext == null)
//			servletContext = this.getRequest().getServletContext();
		return servletContext;
	}


	@Override
	public ServerContext getServer() {
		return server;
	}


	@Override
	public ActionAttribute getAction() {
		return action;
	}
	
	public void setAction(ActionAttribute action){
		this.action = action;
	}


	@Override
	public CacheContext getCache() {
		return cache;
	}

	@Override
	public TraceInfo getTraceInfo() {
		
		return traceInfo;
	}
	public void setTraceInfo(TraceInfo traceInfo) {
		
		this.traceInfo = traceInfo;
	}

	@Override
	public void setParamWithoutValidate(String[] params) {
		
		this.paramWithoutValidate = params;
	}

	@Override
	public String[] getParamWithoutValidate() {
		
		return this.paramWithoutValidate;
	}

}
