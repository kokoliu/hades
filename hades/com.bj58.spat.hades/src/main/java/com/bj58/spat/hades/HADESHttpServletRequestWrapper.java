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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.bj58.spat.hades.utils.SqlHtmlConverter;

/**
 * HADES的请求封装可以过滤参数
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class HADESHttpServletRequestWrapper extends HttpServletRequestWrapper{

	private final BeatContext beat;
	
	public HADESHttpServletRequestWrapper(HttpServletRequest request, BeatContext beat) {
		
		super(request);
		this.beat = beat;
	}
	
	public String getOriginalParameter(String name){
		
		return super.getParameter(name);
	}
    public String[] getOriginalParameterValues(String name){
	 
		 return  super.getParameterValues(name);
	 }
    @Override
    public String getParameter(String name) {
    	
    	String source = super.getParameter(name);
    	
    	String[] pwv = beat.getParamWithoutValidate();
    	if(source == null)
    		return null;
    	
    	if(pwv == null)
    		return SqlHtmlConverter.convert(source);
    	
    	for(String s : pwv){
			if(name.equals(s))
				return source;
    	}
    	
    	return SqlHtmlConverter.convert(source);
    }

    @Override
    public String[] getParameterValues(String name){
		
    	String [] ss = super.getParameterValues(name);
    	
    	if(ss == null)
    		return null;
    	
    	String[] pwvs = beat.getParamWithoutValidate();
    	
    	for(int i = 0 ; i < ss.length ; i++){
    		String s = ss[i];
	    	if( s == null){
	    		continue;
	    	}
	    	
	    	if(pwvs == null){
	    		ss[i] = SqlHtmlConverter.convert(s);
	    		continue;
	    	}
	    	
	    	boolean needConvert = true;
	    	
	    	for(String pwv : pwvs){	    		
				if(name.equals(pwv)){
					needConvert = false;
					break;
				}
	    	}
	    	if(needConvert)
	    		ss[i] = SqlHtmlConverter.convert(s);
    	}
    	return ss;
    	
    }
}
