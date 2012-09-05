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
package com.bj58.spat.hades.utils;

import java.io.*;
import java.util.Scanner;
/**
 * 文件复制的工具类
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
class Copy {

	// 传过来的参数为文件名和目标地址
	public Copy(String from, String to) {
		copy(from, to);
	}

	public Copy(File from, File to) {
		copy(from, to);
	}

	// 参数 from文件复制到to文件中
	public static void copy(File from, File to) {
		if (from.isDirectory()) {
			if (!to.exists())
				to.mkdir();
			String[] s = from.list();
			for (int i = 0; i < s.length; i++) {
				File f = new File(from.getAbsoluteFile() + "/" + s[i]);
				File t = new File(to.getAbsoluteFile() + "/" + s[i]);
				copy(f, t);
			}
		} else if (from.isFile()) {
			copyFile(from, to);
		}
	}

	//
	public static void copy(String from, String to) {
		File file = new File(from);
		// System.out.println("原文件名："+file.getName());
		File newFile = new File(to + "/" + file.getName());
		if (file.isDirectory()) {
			newFile.mkdir();
			String[] s = file.list();
			for (int i = 0; i < s.length; i++) {
				// System.out.println("新文件名："+newFile.getName());
				copy(file.getAbsolutePath() + "/" + s[i],
						newFile.getAbsolutePath());
			}

		} else if (file.isFile()) {
			copyFile(file, newFile);
		}

	}

	// 复制文件
	public static void copyFile(File from, File to) {
		
		if (to.isFile()) return;
		FileInputStream fis = null;
		FileOutputStream fos = null;
		byte[] t = new byte[256];
		try {
			fis = new FileInputStream(from);
			fos = new FileOutputStream(to);
			int c;
			int i = 0;
			while ((c = fis.read(t)) != -1) {
				fos.write(c);
				// System.out.println("复制文件中:"+i++);
			}
		} catch (Exception e) {
			System.err.println("FileStreamsTest: " + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				fis.close();
				fos.close();
			} catch (IOException e) {
				System.err.println("FileStreamsTest: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

}