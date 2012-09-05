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
package com.bj58.spat.hades.view;

import javax.servlet.ServletContext;

import org.apache.velocity.app.Velocity;

/**
 * 使用Velocity模板的工具类
 * 
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class VelocityTemplateFactory {
	public static void init(ServletContext sc){
		
		String webAppPath = sc.getRealPath("/");
		
    	Velocity.setProperty("resource.loader", "file");
    	Velocity.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader");
    	Velocity.setProperty("file.resource.loader.path", webAppPath);
    	Velocity.setProperty("file.resource.loader.cache", "false");
    	Velocity.setProperty("file.resource.loader.modificationCheckInterval", "2");
    	Velocity.setProperty("input.encoding", "UTF-8");
    	Velocity.setProperty("output.encoding", "UTF-8");
    	Velocity.setProperty("default.contentType", "text/html; charset=UTF-8");
    	Velocity.setProperty("velocimarco.library.autoreload", "true");
    	
    	
    	
    	Velocity.setProperty("runtime.log.error.stacktrace", "false");
    	Velocity.setProperty("runtime.log.warn.stacktrace", "false");
    	Velocity.setProperty("runtime.log.info.stacktrace", "false");
    	Velocity.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.SimpleLog4JLogSystem");
    	Velocity.setProperty("runtime.log.logsystem.log4j.category", "velocity_log");
    	
    	//Velocity.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.Log4JLogChute" );
    	
    	try {
			Velocity.init();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
