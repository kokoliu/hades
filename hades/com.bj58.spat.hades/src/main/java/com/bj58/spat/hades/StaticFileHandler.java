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





import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *  处理静态文件请求
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class StaticFileHandler {
    
    public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


    	// TODO: 是否是有路径注入..危险
        String url = request.getRequestURI();
        String contextPath = request.getContextPath();
        url = url.substring(contextPath.length());
        if (url.toUpperCase().startsWith("/WEB-INF/")) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        int n = url.indexOf('?');
        if (n!=(-1))
            url = url.substring(0, n);
        n = url.indexOf('#');
        if (n!=(-1))
            url = url.substring(0, n);
             
        String path = MvcConstants.RESOURCES_PREFIX + url;
        request.getRequestDispatcher(path).forward(request, response);
       
    }

}
