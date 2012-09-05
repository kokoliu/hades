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





import javax.servlet.http.HttpServletRequest;

import com.bj58.spat.hades.ActionResult;
import com.bj58.spat.hades.BeatContext;
import com.bj58.spat.hades.MvcConstants;
import com.bj58.spat.hades.BeatContext.Model;


/**
 * JSP类型的{@code ActionResult} 
 * 
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class JspViewResult extends ActionResult {
	
	private String prefix = MvcConstants.VIEW_PREFIX;
	private String suffix = ".jsp";
	
	public JspViewResult(String viewName) {
		this.viewName = viewName;
	}

	private String viewName;

	/**
	 * @return the viewName
	 */
	public String getViewName() {
		return viewName;
	}

	@Override
	public void render(BeatContext beat) throws Exception {
		String path = prefix + viewName + suffix;
		
		HttpServletRequest request = beat.getRequest();
		Model model = beat.getModel();
		for(String key : model.getModel().keySet())
			request.setAttribute(key, model.getAttributeValue(key));
		
//		request.setAttribute("errors", beat.getBindResults().getErrors());
		
		request.getRequestDispatcher(path).forward(request, beat.getResponse());
		
	}
}