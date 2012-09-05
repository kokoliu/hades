package com.bj58.wf.mvc.context;

import java.util.Iterator;

import org.junit.Test;

import com.bj58.spat.hades.MvcController;
import com.bj58.spat.hades.context.HADESApplicationContext;

public class TestWFApplicationContext {

	
	
	HADESApplicationContext context = new HADESApplicationContext();
	
	@Test
	public void testInit(){
		
		
		context.init();
		
		Iterator<Class<?>> it = HADESApplicationContext.clazzes.iterator();
		
		while(it.hasNext()){
			Class<?> cl = it.next();
			System.out.println(cl.getName());
		}
	}
	
	@Test
	public void test(){
		
		
		context.init();
		
		Iterator<Class<?>> it = HADESApplicationContext.clazzes.iterator();
		
		while(it.hasNext()){
			Class<?> cl = it.next();
			System.out.println(cl.getName());
		}
		
		
		System.out.println("==========================================");
		String[] strs = context.getBeanNamesForType(MvcController.class);
		for(String str : strs){
			System.out.println(str);
			
		}
		System.out.println("==========================================");
		
		
	}
}
