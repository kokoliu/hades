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
package com.bj58.spat.hades;

import com.bj58.spat.hades.context.MvcWebAppContext;

/**
 * 常量
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public abstract class MvcConstants {
	
	/**
	 * Application Context的记录名次
	 * @author renjun
	 */
    public static final String ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE = "WebApplicationContext.ROOT";

    /** Default config location for the root context */
//    public static final String DEFAULT_CONFIG_LOCATION = "classpath*:MvcApplicationContext.xml"; // "/WEB-INF/MvcApplicationContext.xml";
    
    public static final String APPLICATION_CONTEXT_ID = MvcWebAppContext.class.getName() + "_CONTEXT_ID";
;
	
	public static final String VIEW_PREFIX = "views";
	
	public static final String RESOURCES_PREFIX = "/resources";
	
	public static final String VIEW_ENGINE = "vm"; // view引擎 jsp, vm
}
