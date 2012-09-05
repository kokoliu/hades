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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.bj58.spat.hades.exception.ExceptionUtils;


/**
 * 文件对象操作工具类
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public abstract class FileUtil {




	/**
	 * 从 CLASSPATH 下寻找一个文件
	 * 
	 * @param path
	 *            文件路径
	 * @param klassLoader
	 *            参考 ClassLoader
	 * @param enc
	 *            文件路径编码
	 * 
	 * @return 文件对象，如果不存在，则为 null
	 */
	public static File findFile(String path, ClassLoader klassLoader, String enc) {
		path = DiskUtil.absolute(path, klassLoader, enc);
		if (null == path)
			return null;
		return new File(path);
	}

	/**
	 * 从 CLASSPATH 下寻找一个文件
	 * 
	 * @param path
	 *            文件路径
	 * @param enc
	 *            文件路径编码
	 * @return 文件对象，如果不存在，则为 null
	 */
	public static File findFile(String path, String enc) {
		return findFile(path, FileUtil.class.getClassLoader(), enc);
	}

	/**
	 * 从 CLASSPATH 下寻找一个文件
	 * 
	 * @param path
	 *            文件路径
	 * @param klassLoader
	 *            使用该 ClassLoader进行查找
	 * 
	 * @return 文件对象，如果不存在，则为 null
	 */
	public static File findFile(String path, ClassLoader klassLoader) {
		return findFile(path, klassLoader, CodingUtil.getDefaultEncoding());
	}

	/**
	 * 从 CLASSPATH 下寻找一个文件
	 * 
	 * @param path
	 *            文件路径
	 * 
	 * @return 文件对象，如果不存在，则为 null
	 */
	public static File findFile(String path) {
		return findFile(path, FileUtil.class.getClassLoader(), CodingUtil.getDefaultEncoding());
	}

	/**
	 * 获取输出流
	 * 
	 * @param path
	 *            文件路径
	 * @param klass
	 *            参考的类， -- 会用这个类的 ClassLoader
	 * @param enc
	 *            文件路径编码
	 * 
	 * @return 输出流
	 */
	public static InputStream findFileAsStream(String path, Class<?> klass, String enc) {
		File f = new File(path);
		if (f.exists())
			try {
				return new FileInputStream(f);
			}
			catch (FileNotFoundException e1) {
				return null;
			}
		if (null != klass) {
			InputStream ins = klass.getClassLoader().getResourceAsStream(path);
			if (null == ins)
				ins = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
			if (null != ins)
				return ins;
		}
		return ClassLoader.getSystemResourceAsStream(path);
	}

	/**
	 * 获取输出流
	 * 
	 * @param path
	 *            文件路径
	 * @param enc
	 *            文件路径编码
	 * 
	 * @return 输出流
	 */
	public static InputStream findFileAsStream(String path, String enc) {
		return findFileAsStream(path, FileUtil.class, enc);
	}

	/**
	 * 获取输出流
	 * 
	 * @param path
	 *            文件路径
	 * @param klass
	 *            参考的类， -- 会用这个类的 ClassLoader
	 * 
	 * @return 输出流
	 */
	public static InputStream findFileAsStream(String path, Class<?> klass) {
		return findFileAsStream(path, klass, CodingUtil.getDefaultEncoding());
	}

	/**
	 * 获取输出流
	 * 
	 * @param path
	 *            文件路径
	 * 
	 * @return 输出流
	 */
	public static InputStream findFileAsStream(String path) {
		return findFileAsStream(path, FileUtil.class, CodingUtil.getDefaultEncoding());
	}

	/**
	 * 文件对象是否是目录，可接受 null
	 */
	public static boolean isDirectory(File f) {
		if (null == f)
			return false;
		if (!f.exists())
			return false;
		if (!f.isDirectory())
			return false;
		return true;
	}

	/**
	 * 文件对象是否是文件，可接受 null
	 */
	public static boolean isFile(File f) {
		return null != f && f.exists() && f.isFile();
	}

	/**
	 * 创建新文件，如果父目录不存在，也一并创建。可接受 null 参数
	 * 
	 * @param f
	 *            文件对象
	 * @return false，如果文件已存在。 true 创建成功
	 * @throws IOException
	 */
	public static boolean createNewFile(File f) throws IOException {
		if (null == f || f.exists())
			return false;
		makeDir(f.getParentFile());
		return f.createNewFile();
	}

	/**
	 * 创建新目录，如果父目录不存在，也一并创建。可接受 null 参数
	 * 
	 * @param dir
	 *            目录对象
	 * @return false，如果目录已存在。 true 创建成功
	 * @throws IOException
	 */
	public static boolean makeDir(File dir) {
		if (null == dir || dir.exists())
			return false;
		return dir.mkdirs();
	}

	/**
	 * 强行删除一个目录，包括这个目录下所有的子目录和文件
	 * 
	 * @param dir
	 *            目录
	 * @return 是否删除成功
	 * @throws IOException
	 */
	public static boolean deleteDir(File dir) throws IOException {
		if (null == dir || !dir.exists())
			return false;
		if (!dir.isDirectory())
			throw new IOException("\"" + dir.getAbsolutePath() + "\" should be a directory!");
		File[] files = dir.listFiles();
		boolean re = false;
		if (null != files) {
			if (files.length == 0)
				return dir.delete();
			for (File f : files) {
				if (f.isDirectory())
					re |= deleteDir(f);
				else
					re |= deleteFile(f);
			}
			re |= dir.delete();
		}
		return re;
	}

	/**
	 * 删除一个文件
	 * 
	 * @param f
	 *            文件
	 * @return 是否删除成功
	 * @throws IOException
	 */
	public static boolean deleteFile(File f) {
		if (null == f)
			return false;
		return f.delete();
	}

	/**
	 * 清除一个目录里所有的内容
	 * 
	 * @param dir
	 *            目录
	 * @return 是否清除成功
	 * @throws IOException
	 */
	public static boolean clearDir(File dir) throws IOException {
		if (null == dir)
			return false;
		if (!dir.exists())
			return false;
		File[] fs = dir.listFiles();
		for (File f : fs) {
			if (f.isFile())
				FileUtil.deleteFile(f);
			else if (f.isDirectory())
				FileUtil.deleteDir(f);
		}
		return false;
	}

	/**
	 * 拷贝一个文件
	 * 
	 * @param src
	 *            原始文件
	 * @param target
	 *            新文件
	 * @return 是否拷贝成功
	 * @throws IOException
	 */
	public static boolean copyFile(File src, File target) throws IOException {
		if (src == null || target == null || !src.exists())
			return false;
		if (!target.exists())
			if (!createNewFile(target))
				return false;
		InputStream ins = new BufferedInputStream(new FileInputStream(src));
		OutputStream ops = new BufferedOutputStream(new FileOutputStream(target));
		int b;
		while (-1 != (b = ins.read()))
			ops.write(b);

		StreamUtil.safeClose(ins);
		StreamUtil.safeFlush(ops);
		StreamUtil.safeClose(ops);
		return target.setLastModified(src.lastModified());
	}

	/**
	 * 拷贝一个目录
	 * 
	 * @param src
	 *            原始目录
	 * @param target
	 *            新目录
	 * @return 是否拷贝成功
	 * @throws IOException
	 */
	public static boolean copyDir(File src, File target) throws IOException {
		if (src == null || target == null || !src.exists())
			return false;
		if (!src.isDirectory())
			throw new IOException(src.getAbsolutePath() + " should be a directory!");
		if (!target.exists())
			if (!makeDir(target))
				return false;
		boolean re = true;
		File[] files = src.listFiles();
		if (null != files) {
			for (File f : files) {
				if (f.isFile())
					re &= copyFile(f, new File(target.getAbsolutePath() + "/" + f.getName()));
				else
					re &= copyDir(f, new File(target.getAbsolutePath() + "/" + f.getName()));
			}
		}
		return re;
	}

	/**
	 * 将文件移动到新的位置
	 * 
	 * @param src
	 *            原始文件
	 * @param target
	 *            新文件
	 * @return 移动是否成功
	 * @throws IOException
	 */
	public static boolean move(File src, File target) throws IOException {
		if (src == null || target == null)
			return false;
		makeDir(target.getParentFile());
		return src.renameTo(target);
	}

	/**
	 * 将文件改名
	 * 
	 * @param src
	 *            文件
	 * @param newName
	 *            新名称
	 * @return 改名是否成功
	 */
	public static boolean rename(File src, String newName) {
		if (src == null || newName == null)
			return false;
		if (src.exists()) {
			File newFile = new File(src.getParent() + "/" + newName);
			if (newFile.exists())
				return false;
			FileUtil.makeDir(newFile.getParentFile());
			return src.renameTo(newFile);
		}
		return false;
	}

	/**
	 * 修改路径
	 * 
	 * @param path
	 *            路径
	 * @param newName
	 *            新名称
	 * @return 新路径
	 */
	public static String renamePath(String path, String newName) {
		if (!StringUtil.isBlank(path)) {
			int pos = path.replace('\\', '/').lastIndexOf('/');
			if (pos > 0)
				return path.substring(0, pos) + "/" + newName;
		}
		return newName;
	}

	/**
	 * @param path
	 *            路径
	 * @return 父路径
	 */
	public static String getParent(String path) {
		if (StringUtil.isBlank(path))
			return path;
		int pos = path.replace('\\', '/').lastIndexOf('/');
		if (pos > 0)
			return path.substring(0, pos);
		return "/";
	}

	/**
	 * @param path
	 *            全路径
	 * @return 文件或者目录名
	 */
	public static String getName(String path) {
		if (!StringUtil.isBlank(path)) {
			int pos = path.replace('\\', '/').lastIndexOf('/');
			if (pos > 0)
				return path.substring(pos);
		}
		return path;
	}

	/**
	 * 将一个目录下的特殊名称的目录彻底删除，比如 '.svn' 或者 '.cvs'
	 * 
	 * @param dir
	 *            目录
	 * @param name
	 *            要清除的目录名
	 * @throws IOException
	 */
	public static void cleanAllFolderInSubFolderes(File dir, String name) throws IOException {
		File[] files = dir.listFiles();
		for (File d : files) {
			if (d.isDirectory())
				if (d.getName().equalsIgnoreCase(name))
					deleteDir(d);
				else
					cleanAllFolderInSubFolderes(d, name);
		}
	}

	/**
	 * 精确比较两个文件是否相等
	 * 
	 * @param f1
	 *            文件1
	 * @param f2
	 *            文件2
	 * @return 是否相等
	 */
	public static boolean isEquals(File f1, File f2) {
		if (!f1.isFile() || !f2.isFile())
			return false;
		InputStream ins1 = null;
		InputStream ins2 = null;
		try {
			ins1 = new BufferedInputStream(new FileInputStream(f1));
			ins2 = new BufferedInputStream(new FileInputStream(f2));
			return StreamUtil.equals(ins1, ins2);
		}
		catch (IOException e) {
			return false;
		}
		finally {
			StreamUtil.safeClose(ins1);
			StreamUtil.safeClose(ins2);
		}
	}

	/**
	 * 在一个目录下，获取一个文件对象
	 * 
	 * @param dir
	 *            目录
	 * @param path
	 *            文件相对路径
	 * @return 文件
	 */
	public static File getFile(File dir, String path) {
		if (dir.exists()) {
			if (dir.isDirectory())
				return new File(dir.getAbsolutePath() + "/" + path);
			return new File(dir.getParent() + "/" + path);
		}
		return new File(path);
	}

	/**
	 * 获取一个目录下所有子目录。子目录如果以 '.' 开头，将被忽略
	 * 
	 * @param dir
	 *            目录
	 * @return 子目录数组
	 */
	public static File[] dirs(File dir) {
		return dir.listFiles(new FileFilter() {
			public boolean accept(File f) {
				return !f.isHidden() && f.isDirectory() && !f.getName().startsWith(".");
			}
		});
	}

	/**
	 * 递归查找获取一个目录下所有子目录(及子目录的子目录)。子目录如果以 '.' 开头，将被忽略
	 * <p/>
	 * <b>包含传入的目录</b>
	 * 
	 * @param dir
	 *            目录
	 * @return 子目录数组
	 */
	public static File[] scanDirs(File dir) {
		ArrayList<File> list = new ArrayList<File>();
		list.add(dir);
		scanDirs(dir, list);
		return list.toArray(new File[list.size()]);
	}

	private static void scanDirs(File rootDir, List<File> list) {
		File[] dirs = rootDir.listFiles(new FileFilter() {
			public boolean accept(File f) {
				return !f.isHidden() && f.isDirectory() && !f.getName().startsWith(".");
			}
		});
		if (dirs != null) {
			for (File dir : dirs) {
				scanDirs(dir, list);
				list.add(dir);
			}
		}
	}

	/**
	 * 获取一个目录下所有的文件。隐藏文件会被忽略。
	 * 
	 * @param dir
	 *            目录
	 * @param suffix
	 *            文件后缀名。如果为 null，则获取全部文件
	 * @return 文件数组
	 */
	public static File[] files(File dir, final String suffix) {
		return dir.listFiles(new FileFilter() {
			public boolean accept(File f) {
				return !f.isHidden()
						&& f.isFile()
						&& (null == suffix || f.getName().endsWith(suffix));
			}
		});
	}

	/**
	 * 判断两个文件内容是否相等
	 * 
	 * @param f1
	 *            文件对象
	 * @param f2
	 *            文件对象
	 * @return <ul>
	 *         <li>true: 两个文件内容完全相等
	 *         <li>false: 任何一个文件对象为 null，不存在 或内容不相等
	 *         </ul>
	 */
	public static boolean equals(File f1, File f2) {
		if (null == f1 || null == f2)
			return false;
		InputStream ins1, ins2;
		ins1 = StreamUtil.fileIn(f1);
		ins2 = StreamUtil.fileIn(f2);
		if (null == ins1 || null == ins2) {
			return false;
		}

		try {
			return StreamUtil.equals(ins1, ins2);
		}
		catch (IOException e) {
			throw ExceptionUtils.makeThrow("IO异常",e);
		}
		finally {
			StreamUtil.safeClose(ins1);
			StreamUtil.safeClose(ins2);
		}

	}

	public static String getRootPath(){
		File file = new File(System.getProperty("user.dir"));
		String path = file.getAbsolutePath().replace('\\', '/');
			   path = path.substring(0, path.indexOf('/'));
		return path;
	}
	
	public static void main(String[] args) {
		File file = new File(getRootPath());
		
		System.out.println(file.getPath());
		System.out.println(file.getAbsolutePath());
	}
}
