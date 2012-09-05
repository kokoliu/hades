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

import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import org.apache.velocity.context.Context;
import org.apache.velocity.io.VelocityWriter;
import org.apache.velocity.runtime.RuntimeInstance;

import com.bj58.spat.hades.ActionResult;
import com.bj58.spat.hades.BeatContext;
import com.bj58.spat.hades.MvcConstants;

/**
 * Velocity类型的{@code ActionResult} 
 * 
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class VelocityViewResult extends ActionResult {
	
	private String prefix = MvcConstants.VIEW_PREFIX;
	private String suffix = ".html";
	private String viewName;
   
    private static RuntimeInstance rtInstance = new RuntimeInstance();
    
    public VelocityViewResult(String viewName){
    	this.viewName = viewName;
    }
    
	/**
	 * @return the viewName
	 */
	public String getViewName() {
		return viewName;
	}

	@Override
	public void render(BeatContext beat) throws Exception {
		
		String path = prefix  +"\\"+ viewName + suffix;

		Template template =  Velocity.getTemplate(path);
		

        HttpServletResponse response = beat.getResponse();
        response.setContentType("text/html;charset=\"UTF-8\"");
        response.setCharacterEncoding("UTF-8");
        // init context:
        Context context = new VelocityContext(beat.getModel().getModel());
        // render:
        VelocityWriter vw = new VelocityWriter(response.getWriter());
        try {
            template.merge(context, vw);
            vw.flush();
        }
        finally {
            vw.recycle(null);
        }		
	}

}
