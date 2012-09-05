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
package com.bj58.spat.hades.utils;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.bj58.spat.hades.BeatContext;


/**
 * Browser工具类
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class Browser {
	
	private BeatContext beat;

	public Browser(BeatContext beat) {
		super();
		this.beat = beat;
	}
	
	/**
	 * TODO: 可能有漏洞，比如通过" x-forwarded-for " 来欺骗
	 * 
	 * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串Ｉｐ值，究竟哪个才是真正的用户端的真实IP呢？
　　	答案是取X-Forwarded-For中第一个非unknown的有效IP字符串。
　	如：
　　	X-Forwarded-For：192.168.1.110, 192.168.1.120, 192.168.1.130, 192.168.1.100
　　	用户真实IP为： 192.168.1.110

	说明：按这种方法不一定100%准
	 * @return
	 */
	
	public String getIp(){
		HttpServletRequest request = beat.getRequest();
		
		
		String ip = getHeader(request, "x-forwarded-for");
		if ( StringUtils.isNotBlank(ip)) return ip;
		
		ip = getHeader(request, "Proxy-Client-IP");
		if ( StringUtils.isNotBlank(ip)) return ip;
		
		ip = getHeader(request, "WL-Proxy-Client-IP");
		if ( StringUtils.isNotBlank(ip)) return ip;
		
		return request.getRemoteAddr();	

	}
	
	private String getHeader(HttpServletRequest request, String headName){
		String value = request.getHeader(headName);
		
		return StringUtils.isBlank(value) || "unknown" .equalsIgnoreCase(value) ?
				"" : value;
	}

}
