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
package com.bj58.spat.hades.trace;

import java.util.List;


/**
 * Trace信息。
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class TraceInfo {
	
	private long arriveTime;
	
	private long lastTraceTime;
	
	private long leaveTime;
	
	private RequestTraceInfo reqInfo;
	
	private ResponseTraceInfo resInfo;
	
	private List<CustomTraceInfo> customTraceInfos;
	
	public RequestTraceInfo getReqInfo() {
		return reqInfo;
	}

	public void setReqInfo(RequestTraceInfo reqInfo) {
		this.reqInfo = reqInfo;
	}

	public ResponseTraceInfo getResInfo() {
		return resInfo;
	}

	public void setResInfo(ResponseTraceInfo resInfo) {
		this.resInfo = resInfo;
	}

	public long getArriveTime() {
		return arriveTime;
	}

	public void setArriveTime(long arriveTime) {
		this.arriveTime = arriveTime;
	}

	public long getLastTraceTime() {
		return lastTraceTime;
	}

	public void setLastTraceTime(long lastTraceTime) {
		this.lastTraceTime = lastTraceTime;
	}
	
	public long getLeaveTime() {
		return leaveTime;
	}

	public void setLeaveTime(long leaveTime) {
		this.leaveTime = leaveTime;
	}

	public List<CustomTraceInfo> getCustomTraceInfos() {
		return customTraceInfos;
	}

	public void setCustomTraceInfos(List<CustomTraceInfo> customTraceInfos) {
		this.customTraceInfos = customTraceInfos;
	}
}
