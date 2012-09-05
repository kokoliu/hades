package com.bj58.wf.mvc.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.bj58.spat.hades.utils.ClassUtil;

public class TestClassUtil {

	@Test
	public void get(){
//		
//		String filePath = System.getProperty( "user.dir");
//        // 自定义过滤规则  
//        List<String> classFilters = new ArrayList<String>();  
//        classFilters.add("File*");  
//  
//        // 创建一个扫描处理器，排除内部类 扫描符合条件的类  
//        ClassUtil handler = new ClassUtil(true, true, classFilters);  
//  
//        System.out.println("开始递归扫描jar文件的包：org.apache.commons.io 下符合自定义过滤规则的类...");  
//        Set<Class<?>> calssList = handler.getPackageAllClasses("org.apache.commons.io", true);  
//        for (Class<?> cla : calssList) {  
//            System.out.println(cla.getName());  
//        }  
//        System.out.println("开始递归扫描file文件的包：michael.hessian 下符合自定义过滤规则的类...");  
//        classFilters.clear();  
//        classFilters.add("Hessian*");  
//        calssList = handler.getPackageAllClasses("michael.hessian", true);  
//        for (Class<?> cla : calssList) {  
//            System.out.println(cla.getName());  
//        }  
	}
	
	@Test
	public void testGetPackageAllClasses() {
		System.out.println("======================================================");
		List<String> classFilters = new ArrayList<String>();
		classFilters.add("*dog");
    	try {
			ClassUtil util = new ClassUtil();
			util.setClassFilters(classFilters);
			Set<Class<?>> clazzes = util.scanPackageFile("com.bj58", true);
			Iterator<Class<?>> it = clazzes.iterator();
			while(it.hasNext()){
				Class<?> c = it.next();
				System.out.println("c.getName()" + c.getName());
				System.out.println("c.getModifiers()" + c.getModifiers());
				System.out.println("c.getClass().getName()" + c.getClass().getName());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
