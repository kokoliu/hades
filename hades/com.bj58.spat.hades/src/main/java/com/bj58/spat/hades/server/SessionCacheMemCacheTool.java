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
import java.io.File;

import com.bj58.sfft.caching.Memcache;
import com.bj58.spat.hades.HADES;


/**
 * 可以用于操作cachesession
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class SessionCacheMemCacheTool {

	private static Memcache mc = null;
	
	/**
	 * 获得Cache对象
	 * @return
	 */
	public static Memcache getCache() {
		
		if (mc  != null) return mc;
		
		String path = HADES.getConfigFolder() + HADES.getNamespace() + "/session_memcache.xml";

		File cacheFile = new File(path);
		
		if (!cacheFile.exists()) return null;
		
		synchronized (SessionCacheMemCacheTool.class){
			if (mc  != null) return mc;
			mc = Memcache.GetMemcache(path);
			return mc;
		}
	}
	
}
