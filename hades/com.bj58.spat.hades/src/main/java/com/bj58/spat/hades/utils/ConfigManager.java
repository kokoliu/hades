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

import java.io.File;

import com.bj58.spat.hades.HADES;


/**
 * 管理配置文件
 * 临时性的
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class ConfigManager {
	
	private static boolean hasCopy = false;
	private final static String srcFolder = "config";
	
	public static void copyConfig(){
		
		if (hasCopy) return;
		
		hasCopy = true;

		File destFolder = new File(HADES.getConfigFolder() + HADES.getNamespace() + "/");
		
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		File root;
		try {
//			System.out.print("copy config");
			root = new File(cl.getResource("").toURI());
//			System.out.println(root.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		String path = root.getAbsolutePath() + "/config";
		File configFolder = new File(path);
		if (!configFolder.exists() || configFolder.isFile()) return;
		
		Copy.copy(configFolder, destFolder);
		
	}
}
