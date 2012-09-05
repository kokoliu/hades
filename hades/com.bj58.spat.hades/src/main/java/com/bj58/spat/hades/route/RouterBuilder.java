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
package com.bj58.spat.hades.route;

import java.util.LinkedHashSet;
import java.util.Set;

import com.bj58.spat.hades.MvcController;
import com.bj58.spat.hades.context.HADESApplicationContext;

/**
 * 路由构造器，根据 {@code}HADESApplicationContext 初始化所有的路由信息
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class RouterBuilder {
	
	// private BeatRouter router = new BeatRouter();
	// private ApplicationContext context = null;

//	/**
//	 * 根据当前请求获得匹配结果
//	 * @param beat
//	 * @return
//	 */
//	public RouteResult match(BeatContext beat){
//		return router.get(beat);
//	}
//	
//	/**
//	 * @param context the context to set
//	 */
//	public void setApplicationContext(ApplicationContext context) {
//		this.context = context;
//		detectActionExecutions();
//	}
	
	public static BeatRouter build(HADESApplicationContext context){
		return detectActionExecutions(context);
		
	}

	
	private static BeatRouter detectActionExecutions(HADESApplicationContext context) {

		Set<ActionInfo> actions = new LinkedHashSet<ActionInfo>();
		
		String[] beanNames = context.getBeanNamesForType(MvcController.class);
		// Take any bean name that we can determine URLs for.
		
		
		for (String beanName : beanNames) {
			
			System.out.println("beanName : " + beanName);

			ControllerInfo ce = ControllerInfo.Factory(beanName, context);
			if (ce == null) continue;
			
			addActionExecution(actions, ce);
		}		
		return BuildMappingGroup(actions);
	}

	private static void addActionExecution(Set<ActionInfo> actions, ControllerInfo ce){
		actions.addAll(ce.actions);		
	}
	
	private static BeatRouter BuildMappingGroup(Set<ActionInfo> actions){
		BeatRouter router = new BeatRouter();
		
		for(ActionInfo action : actions)
			router.addMapping(action);

		return router;	
	}


}
