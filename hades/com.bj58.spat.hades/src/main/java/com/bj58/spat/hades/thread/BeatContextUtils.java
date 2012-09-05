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
package com.bj58.spat.hades.thread;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bj58.spat.hades.BeatContext;

/**
 * 通过ThreadLocal实现对每个请求的线程的变量封装，保证线程安全。
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class BeatContextUtils {
	
	/**
	 * 确保线程安全
	 */
	static ThreadLocal<BeatContext> beatThreadLocal = new ThreadLocal<BeatContext>();
	
	public static void bindBeatContextToCurrentThread(BeatContextLocal beat){
		beatThreadLocal.set(beat);
	}
	
	public static BeatContext getCurrent(){
		BeatContext beat = beatThreadLocal.get();
		if (beat == null) throw new IllegalStateException("BeatContext");
		
		return beat;
	}
	
	public static BeatContext BeatContextWapper(HttpServletRequest request, 
			HttpServletResponse response) {
		

		
		BeatContext beat = new BeatContextBean(request, response);
		
		// 设置系统性变量
		beat.getModel().add("__beat", beat);
		
		// TODO: 当前线程中如果已经存在Beat， 如何处理？
		beatThreadLocal.set(beat);
		
		return beat;
		
	}
	
	/*
	 * 移除当前线程的变量
	 */
	public static void remove(){
		beatThreadLocal.remove();
	}
	
	

}
