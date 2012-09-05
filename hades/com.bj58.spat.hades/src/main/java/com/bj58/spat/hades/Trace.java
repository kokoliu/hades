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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.bj58.spat.hades.thread.BeatContextBean;
import com.bj58.spat.hades.trace.CustomTraceInfo;
import com.bj58.spat.hades.trace.RequestTraceInfo;
import com.bj58.spat.hades.trace.ResponseTraceInfo;
import com.bj58.spat.hades.trace.TraceInfo;

/**
 *  用于跟踪执行时的信息
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class Trace {

	
	private static final String customTraceTitle = "<table width='100%'><tr colspan='2'  bgcolor='#333366'>CUSTOM TRACE INFO : </tr><tr><td>CONTENT</td><td>WASTETIME</td></tr>";
	private static final String sysTraceTitle = "<table width='100%'><tr colspan='2'  bgcolor='#333366'>SYS TRACE INFO : </tr><tr><td>TITLE</td><td>CONTENT</td></tr>";
	private static final String tableEnd = "</table>";
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");   
	
	public static void trace(String content, BeatContext beat){
		
		TraceInfo traceInfo = beat.getTraceInfo();
		
		if(traceInfo == null)
			return;
		
		CustomTraceInfo cti = new CustomTraceInfo();
		cti.setContent(content);
		long curTime = System.currentTimeMillis();
		cti.setWasteTime(curTime - traceInfo.getLastTraceTime());
		traceInfo.setLastTraceTime(curTime);
		traceInfo.getCustomTraceInfos().add(cti);
	}
	
	public static void init(BeatContextBean beat){
		
		String trace = beat.getRequest().getParameter("trace"+sdf.format(new Date()));
		
		if(trace == null || !trace.equals("true"))
			return;
		
		TraceInfo traceInfo = new TraceInfo();
    	traceInfo.setArriveTime(System.currentTimeMillis());
    	traceInfo.setLastTraceTime(System.currentTimeMillis());
    	traceInfo.setCustomTraceInfos(new ArrayList<CustomTraceInfo>());
    	traceInfo.setReqInfo(new RequestTraceInfo());
    	traceInfo.setResInfo(new ResponseTraceInfo());
    	beat.setTraceInfo(traceInfo);
    	
	}
	public static void wrapper(BeatContext beat) throws IOException{

		if(beat.getTraceInfo() == null)
			return ;
		
		StringBuilder sb = new StringBuilder();
		getCustomInfo(sb, beat);		
		getSysInfo(sb, beat);
		beat.getResponse().getWriter().write(sb.toString());
	}
	
	private static void getCustomInfo(StringBuilder sb, BeatContext beat){
		
		sb.append(customTraceTitle);
		
		for(CustomTraceInfo cti : beat.getTraceInfo().getCustomTraceInfos()){
			
			sb.append("<tr>");
			sb.append("<td>" + cti.getContent() + "</td>");
			sb.append("<td>" + cti.getWasteTime() + "</td>");
			sb.append("</tr>");
			
		}
		sb.append(tableEnd);
		
	}
	
	private static void getSysInfo(StringBuilder sb, BeatContext beat){
		sb.append(sysTraceTitle);
		sb.append(tableEnd);
	}
}
