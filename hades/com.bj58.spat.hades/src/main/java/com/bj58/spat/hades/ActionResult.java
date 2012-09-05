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



import com.bj58.spat.hades.view.JspViewResult;
import com.bj58.spat.hades.view.RedirectResult;
import com.bj58.spat.hades.view.VelocityViewResult;

/**
 * 所有Action的返回结果
 * 
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public abstract class ActionResult {
	
	/**
	 * 用视图显示
	 * @param viewName
	 * @return
	 */
	public static ActionResult view(String viewName) {
		// TODO: 这是一个硬编码
		if (MvcConstants.VIEW_ENGINE.equals("vm"))
			return new VelocityViewResult(viewName);
		else
			return new JspViewResult(viewName);		
	}
	
	/**
	 * 跳转到一个新页面
	 * @param redirectUrl
	 * @return
	 */
	public static ActionResult redirect(String redirectUrl){
		return new RedirectResult(redirectUrl);
	}
	
	/**
	 * 在服务器的Context，中跳转
	 * @param redirectRelativeUrl
	 * @return
	 */
	public static ActionResult redirectContext(final String redirectRelativeUrl){
		return new ActionResult() {

			@Override
			public void render(BeatContext beat) throws Exception {
				ActionResult result = redirect(beat.getServer().getContextPath() + redirectRelativeUrl);
				result.render(beat);
			}
			
		};
	}
	
	/**
	 * 返回一个文件
	 * @param fileName
	 * @return
	 */
	public static ActionResult Stream(String fileName){
		return null;
	}
	
	/**
	 * 用于生成显示页面
	 * @param beat
	 * @throws Exception
	 */
	public abstract void render(BeatContext beat)  throws Exception;

}
