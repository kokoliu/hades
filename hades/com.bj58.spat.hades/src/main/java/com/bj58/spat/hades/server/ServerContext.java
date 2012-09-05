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

import com.bj58.spat.hades.BeatContext;
import com.bj58.spat.hades.client.CookieHandler;

/**
 * 服务器端的相关信息
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class ServerContext {
	
	BeatContext beat;
	
	CookieHandler cookie;
	SessionHandler session;
	
	public ServerContext(BeatContext beat) {
		super();
		this.beat = beat;
	}
	
	/**
	 * session处理
	 * @return
	 */
	public SessionHandler getSessions(){
		if(session == null)
			session = new BaseSessionHandler(beat);
		return session;
	}
	
	/**
	 * 获得实际位置
	 * @return
	 */
	public String getRealPath(){
		return beat.getServletContext().getRealPath("/");
	}
	
	/**
	 * 获得部署路径
	 * @return
	 */
	public String getContextPath(){
		return beat.getServletContext().getContextPath();
	}

}
