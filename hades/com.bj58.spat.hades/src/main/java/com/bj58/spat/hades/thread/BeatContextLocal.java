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

import com.bj58.spat.hades.ActionAttribute;
import com.bj58.spat.hades.BeatContext;
import com.bj58.spat.hades.bind.BeatBindResults;
import com.bj58.spat.hades.cache.CacheContext;
import com.bj58.spat.hades.client.ClientContext;
import com.bj58.spat.hades.server.ServerContext;
import com.bj58.spat.hades.trace.TraceInfo;
/**
 * 用于封装每个请求的上文信息
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class BeatContextLocal implements BeatContext {

	private BeatContext getCurrent() {

		return BeatContextUtils.getCurrent();
	}


	@Override
	public HttpServletRequest getRequest() {
		return getCurrent().getRequest();
	}

	@Override
	public HttpServletResponse getResponse() {
		return getCurrent().getResponse();
	}


	@Override
	public Model getModel() {
		return getCurrent().getModel();
	}


	@Override
	public String getRelativeUrl() {
		return getCurrent().getRelativeUrl();
	}

//
//	@Override
//	public BeatBindResults getBindResults() {
//		return getCurrent().getBindResults();
//	}


	@Override
	public ClientContext getClient() {
		return getCurrent().getClient();
	}


	@Override
	public ServletContext getServletContext() {
		return getCurrent().getServletContext();
	}


	@Override
	public ServerContext getServer() {
		return getCurrent().getServer();
	}


	@Override
	public ActionAttribute getAction() {
		return getCurrent().getAction();
	}


	@Override
	public CacheContext getCache() {
		return getCurrent().getCache();
	}


	@Override
	public TraceInfo getTraceInfo() {
		return getCurrent().getTraceInfo();
	}


	@Override
	public void setParamWithoutValidate(String[] params) {
		getCurrent().setParamWithoutValidate(params);
	}


	@Override
	public String[] getParamWithoutValidate() {
		
		return getCurrent().getParamWithoutValidate();
	}


	@Override
	public BeatBindResults getBindResults() {
		// TODO Auto-generated method stub
		return null;
	}


}
