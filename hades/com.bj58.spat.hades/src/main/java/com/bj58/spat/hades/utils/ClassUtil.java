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


import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

import org.hibernate.validator.jtype.TypeUtils;

import com.bj58.spat.hades.log.Log;
import com.bj58.spat.hades.log.LogFactory;
  

/**
 * 
 * ClassUtil
 * 
 * 扫描指定包（包括jar）下的class文件 <br> 
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class ClassUtil {
  
    /** 
     * 过滤规则列表 如果是null或者空，即全部符合不过滤 
     */  
	
	private final static Log logger = LogFactory.getLog(ClassUtil.class);
	
    private List<String> classFilters;  
    
    private static final char PACKAGE_SEPARATOR = '.';
    
    public static final String CGLIB_CLASS_SEPARATOR = "$$";
    
    private static final char INNER_CLASS_SEPARATOR = '$';
  
    public static final String ARRAY_SUFFIX = "[]";
	
    
    /** 
     * 扫描文件类型的class
     * @param basePackage 基础包 
     * @param recursive 是否递归搜索子包 
     * @return Set 
     */  
    public Set<Class<?>> scanPackageFile(String packageName, boolean recursive) {  
        
    	Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
  
    	String package2Path = packageName.replace('.', '/');  
    	logger.debug("================= START FILE SCAN =================");
        try {
        	
        	Enumeration<URL> dirs = Thread.currentThread().getContextClassLoader().getResources(package2Path);
        	
            while (dirs.hasMoreElements()) {
            	
                URL url = dirs.nextElement();
                
                String protocol = url.getProtocol();  
                if ("file".equals(protocol)) {
                	
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");  
                    logger.debug("start filePath : " + filePath);
                    doScanPackageClassesByFile(classes, packageName, filePath, recursive);  
                }
            }  
        } catch (IOException e) {  
        	logger.error("SCAN FILE ERROR : ", e);
        }  
        logger.debug("================= END FILE SCAN =================");
        return classes;  
    }  
    /** 
     * 扫描jar包内的class
     * @param basePackage 基础包 
     * @param recursive 是否递归搜索子包 
     * @return Set 
     */  
    public Set<Class<?>> scanPackageJar(String packageName, boolean recursive) {  
        
    	Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
  
    	String package2Path = packageName.replace('.', '/');  
    	logger.debug("================= START JAR SCAN =================");
        try {  
        	Enumeration<URL> dirs = Thread.currentThread().getContextClassLoader().getResources(package2Path);              
            
            while (dirs.hasMoreElements()) {  
            	
                URL url = dirs.nextElement();  
                if ("jar".equals(url.getProtocol())) {  
                	
                	logger.debug("SCAN JAR ERROR : " + URLDecoder.decode(url.getFile(), "UTF-8"));
                	
                	doScanPackageClassesByJar(packageName, url, recursive,  classes);  
                }
            }  
        } catch (IOException e) {  
        	logger.error("SCAN JAR ERROR : ", e);
        }  
        logger.debug("================= END JAR SCAN =================");
        return classes;  
    }  
      
    /** 
     * 以jar的方式扫描包下的所有Class文件<br> 
     * @param basePackage eg：michael.utils. 
     * @param url 
     * @param recursive 
     * @param classes 
     */  
    private void doScanPackageClassesByJar(String basePackage, URL url, boolean recursive, Set<Class<?>> classes) {  
        
        String package2Path = basePackage.replace('.', '/');  
        
        try {  
        	JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();  
            Enumeration<JarEntry> entries = jar.entries();  
            while (entries.hasMoreElements()) {  
                JarEntry entry = entries.nextElement();  
                String name = entry.getName();  
                if (!name.startsWith(package2Path) || entry.isDirectory()) {  
                    continue;  
                }  
  
                // 判断是否递归搜索子包  
                if (!recursive && name.lastIndexOf('/') != package2Path.length()) {  
                    continue;  
                }  
                // 判断是否过滤 inner class  
                if (name.indexOf('$') != -1) {  
//                    System.out.println("exclude inner class with name:" + name);  
                    continue;  
                }  
                String classSimpleName = name.substring(name.lastIndexOf('/') + 1);  
                // 判定是否符合过滤条件  
                if (this.filterClassName(classSimpleName)) {  
                    String className = name.replace('/', '.');  
                    className = className.substring(0, className.length() - 6);  
                    try {  
                        classes.add(Thread.currentThread().getContextClassLoader().loadClass(className));  
                    } catch (ClassNotFoundException e) {  
                    	logger.error("Class.forName error:", e);  
                    }  
                }  
            }  
        } catch (IOException e) {  
        	
        	logger.error("IOException error:", e);
        }  
    }  
  
    /** 
     * 以文件的方式扫描包下的所有Class文件 
     *  
     * @param packageName 
     * @param packagePath 
     * @param recursive 
     * @param classes 
     */  
    private void doScanPackageClassesByFile(Set<Class<?>> classes, String packageName, String packagePath, boolean recursive) {  
    	
        File dir = new File(packagePath);  
        if (!dir.exists() || !dir.isDirectory()) {  
            return;  
        }
        
        final boolean fileRecursive = recursive;
        // 过滤当前文件夹下的所有文件
        File[] dirfiles = dir.listFiles(new FileFilter() {  
             
            public boolean accept(File file) {  

                return fiterlFile(file, fileRecursive);
            }  
        });  
        for (File file : dirfiles) {
        	
        	logger.debug("filePath : " + file.getAbsolutePath()+ " , fileName : "+file.getName());
            
        	if (file.isDirectory()) {  
            	
                doScanPackageClassesByFile(classes, packageName + "." + file.getName(), file.getAbsolutePath(), recursive);  
            } else {  
                String className = file.getName().substring(0, file.getName().length() - 6);  
                try {  
                    classes.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className));  
  
                } catch (ClassNotFoundException e) {  
                	logger.error("无法找到类 : " + className, e);
                }  
            }  
        }  
    }  
  
    /** 
     * 根据过滤规则判断类名 
     * @param className 
     * @return 
     */  
    private boolean fiterlFile(File file, boolean fileRecursive) {
        
    	if (file.isDirectory()) {  
            return fileRecursive;  
        }  
        String filename = file.getName();  
        if (filename.indexOf('$') != -1) {  
//            System.out.println("exclude inner class with name:" + filename);  
            return false;  
        }  
    	if(!"controllers".equals(file.getParentFile().getName()))
    		return false;
    	
    	return filterClassName(file.getName());
    }  
    /** 
     * 根据过滤规则判断类名 
     * @param className 
     * @return 
     */  
    private boolean filterClassName(String className) {
    	

        if (!className.endsWith(".class")) {  
            return false;  
        }

        if (null == this.classFilters || this.classFilters.isEmpty()) {  
            return true;  
        }  
        String tmpName = className.substring(0, className.length() - 6);  
        boolean flag = false;  
        for (String str : classFilters) {  
            String tmpreg = "^" + str.replace("*", ".*") + "$";  
            Pattern p = Pattern.compile(tmpreg);  
            if (p.matcher(tmpName).find()) {  
                flag = true;  
                break;  
            }  
        }  
        return flag;  
    }  
 
    /** 
     * @return the classFilters 
     */  
    public List<String> getClassFilters() {  
        return classFilters;  
    }  
  
    /** 
     * @param pClassFilters the classFilters to set 
     */  
    public void setClassFilters(List<String> pClassFilters) {  
        classFilters = pClassFilters;  
    }  	
    
	public static String getShortNameAsProperty(Class<?> clazz) {
		String shortName = ClassUtil.getShortName(clazz);
		int dotIndex = shortName.lastIndexOf('.');
		shortName = (dotIndex != -1 ? shortName.substring(dotIndex + 1) : shortName);
		return Introspector.decapitalize(shortName);
	}
	public static String getShortName(Class<?> clazz) {
		return getShortName(getQualifiedName(clazz));
	}
	public static String getShortName(String className) {
		Assert.hasLength(className, "Class name must not be empty");
		int lastDotIndex = className.lastIndexOf(PACKAGE_SEPARATOR);
		int nameEndIndex = className.indexOf(CGLIB_CLASS_SEPARATOR);
		if (nameEndIndex == -1) {
			nameEndIndex = className.length();
		}
		String shortName = className.substring(lastDotIndex + 1, nameEndIndex);
		shortName = shortName.replace(INNER_CLASS_SEPARATOR, PACKAGE_SEPARATOR);
		return shortName;
	}
	public static String getQualifiedName(Class<?> clazz) {
		Assert.notNull(clazz, "Class must not be null");
		if (clazz.isArray()) {
			return getQualifiedNameForArray(clazz);
		}
		else {
			return clazz.getName();
		}
	}
	private static String getQualifiedNameForArray(Class<?> clazz) {
		StringBuilder result = new StringBuilder();
		while (clazz.isArray()) {
			clazz = clazz.getComponentType();
			result.append(ClassUtil.ARRAY_SUFFIX);
		}
		result.insert(0, clazz.getName());
		return result.toString();
	}
	/**
	 * Return all interfaces that the given class implements as array,
	 * including ones implemented by superclasses.
	 * <p>If the class itself is an interface, it gets returned as sole interface.
	 * @param clazz the class to analyze for interfaces
	 * @return all interfaces that the given object implements as array
	 */
	public static Class<?>[] getAllInterfacesForClass(Class<?> clazz) {
		return getAllInterfacesForClass(clazz, null);
	}
	/**
	 * Return all interfaces that the given class implements as array,
	 * including ones implemented by superclasses.
	 * <p>If the class itself is an interface, it gets returned as sole interface.
	 * @param clazz the class to analyze for interfaces
	 * @param classLoader the ClassLoader that the interfaces need to be visible in
	 * (may be <code>null</code> when accepting all declared interfaces)
	 * @return all interfaces that the given object implements as array
	 */
	public static Class<?>[] getAllInterfacesForClass(Class<?> clazz, ClassLoader classLoader) {
		Set<Class> ifcs = getAllInterfacesForClassAsSet(clazz, classLoader);
		return ifcs.toArray(new Class[ifcs.size()]);
	}
	/**
	 * Return all interfaces that the given class implements as Set,
	 * including ones implemented by superclasses.
	 * <p>If the class itself is an interface, it gets returned as sole interface.
	 * @param clazz the class to analyze for interfaces
	 * @param classLoader the ClassLoader that the interfaces need to be visible in
	 * (may be <code>null</code> when accepting all declared interfaces)
	 * @return all interfaces that the given object implements as Set
	 */
	public static Set<Class> getAllInterfacesForClassAsSet(Class clazz, ClassLoader classLoader) {
		Assert.notNull(clazz, "Class must not be null");
		if (clazz.isInterface() && isVisible(clazz, classLoader)) {
			return Collections.singleton(clazz);
		}
		Set<Class> interfaces = new LinkedHashSet<Class>();
		while (clazz != null) {
			Class<?>[] ifcs = clazz.getInterfaces();
			for (Class<?> ifc : ifcs) {
				interfaces.addAll(getAllInterfacesForClassAsSet(ifc, classLoader));
			}
			clazz = clazz.getSuperclass();
		}
		return interfaces;
	}
	
	/**
	 * Check whether the given class is visible in the given ClassLoader.
	 * @param clazz the class to check (typically an interface)
	 * @param classLoader the ClassLoader to check against (may be <code>null</code>,
	 * in which case this method will always return <code>true</code>)
	 */
	public static boolean isVisible(Class<?> clazz, ClassLoader classLoader) {
		if (classLoader == null) {
			return true;
		}
		try {
			Class<?> actualClass = classLoader.loadClass(clazz.getName());
			return (clazz == actualClass);
			// Else: different interface class found...
		}
		catch (ClassNotFoundException ex) {
			// No interface class found...
			return false;
		}
	}
	
	/**
	 * Check if the right-hand side type may be assigned to the left-hand side
	 * type, assuming setting by reflection. Considers primitive wrapper
	 * classes as assignable to the corresponding primitive types.
	 * @param lhsType the target type
	 * @param rhsType the value type that should be assigned to the target type
	 * @return if the target type is assignable from the value type
	 * @see TypeUtils#isAssignable
	 */
	public static boolean isAssignable(Class<?> lhsType, Class<?> rhsType) {
		Assert.notNull(lhsType, "Left-hand side type must not be null");
		Assert.notNull(rhsType, "Right-hand side type must not be null");
		if (lhsType.isAssignableFrom(rhsType)) {
			return true;
		}
		return false;
	}
	/**
	 * Determine the name of the package of the given class:
	 * e.g. "java.lang" for the <code>java.lang.String</code> class.
	 * @param clazz the class
	 * @return the package name, or the empty String if the class
	 * is defined in the default package
	 */
	public static String getPackageName(Class<?> clazz) {
		Assert.notNull(clazz, "Class must not be null");
		String className = clazz.getName();
		int lastDotIndex = className.lastIndexOf(PACKAGE_SEPARATOR);
		return (lastDotIndex != -1 ? className.substring(0, lastDotIndex) : "");
	}
	
}
