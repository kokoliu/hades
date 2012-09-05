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
package com.bj58.spat.hades.cache;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bj58.spat.hades.ActionResult;
import com.bj58.spat.hades.BeatContext;
import com.bj58.spat.hades.log.Log;
import com.bj58.spat.hades.log.LogFactory;
import com.bj58.spat.hades.thread.BeatContextBean;

/**
 * Cache信息用于封装Cache上下文
 * @author Service Platform Architecture Team (spat@58.com)
 *
 */
public class CacheContext {
	
	protected static final Log log = LogFactory.getLog(CacheContext.class);
	
    private static final int time = 60 * 60; // time before cache should be refreshed - default one hour (in seconds)
    private static final long lastModified = HttpCache.LAST_MODIFIED_INITIAL; // defines if the last-modified-header will be sent - default is intial setting
    private static final long expires = HttpCache.EXPIRES_ON; // defines if the expires-header will be sent - default is on
//    private static final long cacheControlMaxAge = -60; // defines which max-age in Cache-Control to be set - default is 60 seconds for max-age

	
	private String cacheKey;
	private int expiredTime = time;
	private BeatContext beat;
	private boolean hit = false; // cache hit
	
	private CacheHttpServletResponseWrapper responseWrapper = null;
	
	
	public CacheContext(BeatContext beat) {
		this.beat = beat;
		cacheKey = getKey(beat);
	}
	
	
	public String getCacheKey() {
		return cacheKey;
	}

	public void setCacheKey(String cacheKey) {
		this.cacheKey = cacheKey;
	}

	/**
	 * second
	 * @return
	 */
	public int getExpiredTime() {
		return expiredTime;
	}

	/**
	 * second
	 * @param expiredTime
	 */
	public void setExpiredTime(int expiredTime) {
		this.expiredTime = expiredTime;
	}
	
	/**
	 * 需要缓存
	 */
	public void needCache() {
		
		if (hit) return;
		
		if(PageCacheMemCacheTool.getCache() == null) {
			System.err.println("page cache error: no config file, needCache");
			return;
		}
		
		if (responseWrapper != null) return;
		
		String key = getCacheKey();
		wrapResponse(beat, key);
	}
	
	/**
	 * 获得缓存对象
	 * @return
	 */
	public ActionResult getCacheResult() {
		String key = getCacheKey();
		
		if(PageCacheMemCacheTool.getCache() == null) {
			System.err.println("page cache error: no config file, getCacheResult");
			return null;
		}
		
		Object cacheObject = PageCacheMemCacheTool.getCache().get(key);
		
		if (cacheObject == null)  return null;

        hit = true;
        
		final ResponseContent respContent = (ResponseContent) cacheObject;
		
		log.debug("Page Cache: Using cached entry for " + key);
        
        long clientLastModified = beat.getRequest().getDateHeader(HttpCache.HEADER_IF_MODIFIED_SINCE); // will return -1 if no header...

        
        // only reply with SC_NOT_MODIFIED
        // if the client has already the newest page and the response isn't a fragment in a page 
        if ((clientLastModified != -1) && (clientLastModified >= respContent.getLastModified())) {
            return NOT_MODIFIED;
        }
        
        return new ActionResult(){
			@Override
			public void render(BeatContext beat) throws Exception {
				respContent.writeTo(beat.getResponse(), false, false);
			}
        };
	}
	
	public void setCacheResult() {
		if (hit) return;
		
		if (responseWrapper == null) return;
		 
		if (! isCacheable(responseWrapper)) return;
		 
		if (PageCacheMemCacheTool.getCache() == null) return;
		 
		try {
			responseWrapper.flushBuffer();
			
			Date expired =new Date((new Date().getTime() +expiredTime * 1000L));

			 PageCacheMemCacheTool.getCache().set(this.getCacheKey(),  responseWrapper.getContent(), expired);
			 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String getKey(BeatContext beat) {
		
		return getFullUrl(beat);
	}
	
	private String getFullUrl(BeatContext beat){
		HttpServletRequest request = beat.getRequest();
		
        StringBuffer url = new StringBuffer();
        String scheme = request.getScheme();
        int port = request.getServerPort();
        if (port < 0)
            port = 80; // Work around java.net.URL bug

        url.append(scheme);
        url.append("://");
        url.append(request.getServerName());
        if ((scheme.equals("http") && (port != 80))
            || (scheme.equals("https") && (port != 443))) {
            url.append(':');
            url.append(port);
        }
        url.append(request.getRequestURI());
        
        String queryString = request.getQueryString();
        
        if(queryString != null)
        	url.append('?')
        	.append(queryString);
        
        return url.toString();
	}
	
	private static final ActionResult NOT_MODIFIED = new ActionResult() {
		@Override
		public void render(BeatContext beat) throws Exception {
			
			beat.getResponse().setStatus(HttpServletResponse.SC_NOT_MODIFIED);
		}
		
	};
	
	private void wrapResponse(BeatContext beat, String key){
		BeatContextBean bcb = (BeatContextBean) beat;
		
		if (bcb == null) {
			System.err.println("beat is not a BeatContextBean.");
			return;
		}
		
		long cacheControl = 0 - expiredTime;
        CacheHttpServletResponseWrapper cacheResponse = new CacheHttpServletResponseWrapper(beat.getResponse(), false, expiredTime * 1000L, lastModified, expires, cacheControl);
//        CacheHttpServletResponseWrapper cacheResponse = new CacheHttpServletResponseWrapper(beat.getResponse(), false, expiredTime * 1000L, lastModified, expires, cacheControlMaxAge);
        cacheResponse.cacheKey = key;
        bcb.setResponse(cacheResponse);
        
        responseWrapper = cacheResponse;
	}
	
    private boolean isCacheable(CacheHttpServletResponseWrapper cacheResponse) {
        // Only cache if the response was 200
        return cacheResponse.getStatus() == HttpServletResponse.SC_OK;
    }
	
}
