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
package com.bj58.spat.hades.server;

import java.util.Enumeration;

import javax.servlet.http.HttpSession;

import com.bj58.spat.hades.BeatContext;

/**
 * 基本的session处理工具
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class BaseSessionHandler extends SessionHandler {
	
	BeatContext beat;
	
	HttpSession session;

	public BaseSessionHandler(BeatContext beat) {
		super();
		this.beat = beat;
		session = this.beat.getRequest().getSession();
	}
	
	/* (non-Javadoc)
	 * @see com.bj58.spat.hades.mvc.server.Session#get(java.lang.String)
	 */
	@Override
	public Object get(String name){
		return session.getAttribute(name);
		
	}
	
	/* (non-Javadoc)
	 * @see com.bj58.spat.hades.mvc.server.Session#getCreationTime()
	 */
	@Override
	public Object getCreationTime(){
		return session.getCreationTime();
	}
	
	/* (non-Javadoc)
	 * @see com.bj58.spat.hades.mvc.server.Session#getNames()
	 */
	@Override
	public Enumeration<String> getNames(){
		return session.getAttributeNames();
	}
	
	/* (non-Javadoc)
	 * @see com.bj58.spat.hades.mvc.server.Session#getId()
	 */
	@Override
	public String getId(){
		return session.getId();
	}
	
	/* (non-Javadoc)
	 * @see com.bj58.spat.hades.mvc.server.Session#getLastAccessedTime()
	 */
	@Override
	public long getLastAccessedTime(){
		return session.getLastAccessedTime();
	}
	
	/* (non-Javadoc)
	 * @see com.bj58.spat.hades.mvc.server.Session#getMaxInactiveInterval()
	 */
	@Override
	public int getMaxInactiveInterval(){
		return session.getMaxInactiveInterval();
	}
	
	/* (non-Javadoc)
	 * @see com.bj58.spat.hades.mvc.server.Session#invalidate()
	 */
	@Override
	public void invalidate(){
		session.invalidate();
	}
	
	/* (non-Javadoc)
	 * @see com.bj58.spat.hades.mvc.server.Session#isNew()
	 */
	@Override
	public boolean isNew(){
		return session.isNew();
	}
	
	
	/* (non-Javadoc)
	 * @see com.bj58.spat.hades.mvc.server.Session#remove(java.lang.String)
	 */
	@Override
	public void remove(String name){
		session.removeAttribute(name);
	}
	
	/* (non-Javadoc)
	 * @see com.bj58.spat.hades.mvc.server.Session#set(java.lang.String, java.lang.Object)
	 */
	@Override
	public void set(String name, Object value){
		session.setAttribute(name, value);
	}
	
	/* (non-Javadoc)
	 * @see com.bj58.spat.hades.mvc.server.Session#setMaxInactiveInterval(int)
	 */
	@Override
	public void setMaxInactiveInterval(int value){
		session.setMaxInactiveInterval(value);
	}

	@Override
	public void flush() {
	}
	
	
	
	
	
}
