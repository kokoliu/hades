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
import java.io.File;
import java.io.IOException;

import com.bj58.spat.ares.MemcachedClient;
import com.bj58.spat.ares.exception.ConfigException;
import com.bj58.spat.hades.HADES;

/**
 * 用于操作Memcache的客户端工具类。
 * @author Service Platform Architecture Team (spat@58.com)
 *
 */
public class PageCacheMemCacheTool {

	private static MemcachedClient mc = null;
	
	/**
	 * 获得Cache对象
	 * @return
	 */
	public static MemcachedClient getCache() {
		
		if (mc  != null) return mc;
		
		String path = HADES.getConfigFolder() + HADES.getNamespace() + "/pagecache_memcache.xml";

		File cacheFile = new File(path);
		
		if (!cacheFile.exists()) return null;
		
		synchronized (PageCacheMemCacheTool.class){
			if (mc  != null) return mc;
			try {
				mc = MemcachedClient.getInstrance(path);
			} catch (ConfigException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return mc;
		}
	}
	
}
