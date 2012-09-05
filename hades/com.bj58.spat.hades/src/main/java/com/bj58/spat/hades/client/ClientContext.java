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
package com.bj58.spat.hades.client;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.bj58.spat.hades.BeatContext;
import com.bj58.spat.hades.server.SessionHandler;


/**
 * 获得客户端信息
 * @author Service Platform Architecture Team (spat@58.com)
 *
 */
public class ClientContext {
	
	final BeatContext beat;
	
	CookieHandler cookie;
	SessionHandler session;
	
	public ClientContext(BeatContext beat) {
		super();
		this.beat = beat;
	}


	public CookieHandler getCookies(){
		if (cookie == null)
			cookie = new CookieHandler(beat);
		return cookie;
	}
	
	public UploadRequest getUploads(){
		HttpServletRequest request = beat.getRequest();
		return  (request instanceof UploadRequest) 
			? (UploadRequest) beat.getRequest() 
			: null;
	}
	
	public boolean isUpload(){
		return getUploads() == null ? false : true;
	}
	
    /**
     * 得到当前请求的相对url,不含ServerContext路径和参数
     * @return
     */
    public String getRelativeUrl(){
    	HttpServletRequest request = beat.getRequest();
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String relativeUrl = uri.substring(contextPath.length());
        
        // TODO: 为什么是默认的/index.jsp
        if (StringUtils.equals("/index.jsp", relativeUrl)){
        	relativeUrl = "/";
        }
        
        return relativeUrl;
    }
}
