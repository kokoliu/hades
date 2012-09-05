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

import java.util.HashMap;
import java.util.Map;

import com.bj58.spat.hades.BeatContext;

/**
 * cache session处理工具(未实现)
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class MemcacheSessionHandler extends SessionHandler {
	
	BeatContext beat;
	
	Map<String, Object> map = new HashMap<String, Object>();
	
	public MemcacheSessionHandler(BeatContext beat) {
		this.beat = beat;
	}

	@Override
	public Object get(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getCreationTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Enumeration<String> getNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getLastAccessedTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMaxInactiveInterval() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void invalidate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isNew() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void remove(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void set(String name, Object value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMaxInactiveInterval(int value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void flush() {
		
	}
	
	private String getCacheKey() {
		return null;
	}
	
	private Map<String, Object> getSession(){
		return null;
	}

}
