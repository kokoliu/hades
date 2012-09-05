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

import org.apache.commons.lang.StringUtils;

import com.bj58.spat.hades.BeatContext;
import com.bj58.spat.hades.utils.AntPathMatcher;
import com.bj58.spat.hades.utils.PathMatcher;

/**
 * 提供用于定义路由及获取路由相关信息。
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class BeatRouter {
	
	private Set<ActionInfo> actions = new LinkedHashSet<ActionInfo>();
	
	private PathMatcher pathMatcher = new AntPathMatcher();
	
	public void addMapping(ActionInfo action) {
		actions.add(action);
	}
	
	public RouteResult route(BeatContext beat) {
		String url = beat.getClient().getRelativeUrl(); //beat.getRelativeUrl();

	    String bestPathMatch = null;
	    ActionInfo bestAction = null;
	    
	    // 精确匹配
	    for(ActionInfo action : actions){
	    	for (String registeredPath : action.getMappedPatterns()) { 
	    		if (StringUtils.equals(url, registeredPath))
	    			if (action.matchRequestMethod(beat)){
		    	    	bestPathMatch = registeredPath;
		    	    	bestAction = action;
		    	    }
	    	}
	    }
	    
	    // 正则匹配
	    if (bestPathMatch == null)
	    	for(ActionInfo action : actions){
			    for (String registeredPath : action.getMappedPatterns()) {  
			        if (pathMatcher.match(registeredPath, url) &&  
			                (bestPathMatch == null || bestPathMatch.length() < registeredPath.length())) {
			    	    if (action.matchRequestMethod(beat)){
			    	    	bestPathMatch = registeredPath;
			    	    	bestAction = action;
			    	    }
			        } 
			    }
		    }
		
	    if (bestPathMatch == null) return null;
	    
		RouteResult match = new RouteResult();
		//match.isExactMatch = false;
		match.action = bestAction;
		match.beat = beat;
		
		//TODO: 预判断下，可以提高效率
		if (match.action.getParamNames().length > 0)
			match.urlParams = pathMatcher.extractUriTemplateVariables(bestPathMatch, url);
		
		
		return match;
	    
	}


}
