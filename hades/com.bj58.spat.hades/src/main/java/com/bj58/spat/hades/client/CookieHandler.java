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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Enumeration;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bj58.spat.hades.BeatContext;
import com.bj58.spat.hades.utils.StringUtil;

/**
 * Cookie的操作类
 * @author Service Platform Architecture Team (spat@58.com)
 *
 */
public class CookieHandler {
	
	HttpServletResponse response;
	HttpServletRequest request;

	public CookieHandler(BeatContext beat) {
		super();
		response = beat.getResponse();
		request = beat.getRequest();
	}
	
	/**
	 * 通过name和value，增加一个cookie
	 * </br>
	 * 建议用set来代替
	 * @see #set(String, String)
	 * 
	 * @param name
	 * @param value
	 */
	public void add(String name, String value){
		Cookie cookie = new Cookie(name, value);
		// 设置路径（默认）
		cookie.setPath("/");
		// 把cookie放入响应中
		response.addCookie(cookie);
	}
	
	/**
	 * 通过name和value,时间，增加一个cookie
	 * @param name
	 * @param value
	 * @param time
	 */
	public void add(String name, String value, int time){
		Cookie cookie = new Cookie(name, value);
		// 设置有效日期
		cookie.setMaxAge(time);
		// 设置路径（默认）
		cookie.setPath("/");
		
		add(cookie);
	}
	
	/**
	 * 增加Cookie
	 * @param cookie
	 */
	public void add(Cookie cookie){
		response.addCookie(cookie);
	}
	
	/**
	 * 根据名称， 得到cookie的值
	 * @param name
	 * @return
	 */
	public String get(String name){
		Cookie cookie = getCookie(name);
		return cookie == null?  null : cookie.getValue();
	}
	
	/**
	 * 根据名称， 得到cookie对象
	 * @param name
	 * @return
	 */
	private Cookie getCookie(String name){
		Cookie[] cookies = request.getCookies();
		
		if (cookies == null) return null;
		
		for(Cookie cookie : cookies){
			if(name.equalsIgnoreCase(cookie.getName()))
				return cookie;
		}
		
		return null;
	}
	
	/**
	 * 设置cookie的值
	 * 如果该cookie已经存在，则修改，
	 * 否则新增一个cookie
	 * @param name
	 * @param value
	 */
	public void set(String name, String value){
		Cookie cookie = getCookie(name);
		
		if (cookie == null){
			add(name, value);
			return;
		}
		
		cookie.setValue(value);
	}
	
	/**
	 * 设置cookie的值
	 * 如果该cookie已经存在，则修改，
	 * @param name
	 * @param value
	 * @param time
	 */
	public void set(String name, String value, int time){
		Cookie cookie = getCookie(name);
		
		if (cookie == null){
			add(name, value, time);
			return;
		}
		
		cookie.setValue(value);
		cookie.setMaxAge(time);
	}
	
	/**
	 * 删除一个cookie
	 * @param name
	 */
	public void delete(String name){
		Cookie cookie = getCookie(name);
		
		if (cookie == null) return;
		
		// 销毁
		cookie.setMaxAge(0);
		response.addCookie(cookie);
	}
	
	/**
	 * 增加Cookie encoder : "URL","BASE64"
	 * @param cookie
	 * @param encode
	 * @throws UnsupportedEncodingException
	 */
	public void add(Cookie cookie, String encoder) throws UnsupportedEncodingException{
		
		if("URL".equals(encoder)){
			cookie.setValue(URLEncoder.encode(cookie.getValue(), "UTF-8"));
		}else{
			com.bj58.spat.hades.utils.Base64.encode(cookie.getValue().getBytes());
		}
		
		response.addCookie(cookie);
	}
	
	/**
	 * 获取特殊cookie的值 decoder: "UTF-8","BASE64"
	 * @param cookieName
	 * @param decoder
	 * @return
	 */
	public String getCookie(String cookieName, String decoder){
		
		String cookieValue = "";
		String cookieHeader = "";
		Enumeration cookieHeaderE = request.getHeaders("cookie"); 
		while(cookieHeaderE.hasMoreElements()){
			cookieHeader = (String) cookieHeaderE.nextElement();
			if(!StringUtil.isEmpty(cookieHeader) && cookieHeader.contains(cookieName))
				break;
		}
		if(!StringUtil.isEmpty(cookieHeader)){
			for(String cookieStr : cookieHeader.split(";")){
				cookieStr = cookieStr.trim();
				if(cookieStr.startsWith(cookieName+"=")){
					cookieValue = cookieStr.replaceFirst(cookieName+"=", "");
					try {
						cookieValue = cookieValue.replaceAll("\"", "");
						
						if("URL".equals(decoder)){
							cookieValue = URLDecoder.decode(cookieValue, "UTF-8");	
						}else{
							cookieValue = new String(com.bj58.spat.hades.utils.Base64.decode(cookieValue));
						}
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					return cookieValue.replaceAll("\"", "");
				}
			}
		}
		return "";
	}
}
