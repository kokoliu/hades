package com.bj58.wf.mvc.bind;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;

import com.bj58.spat.hades.bind.BindAndValidate;
import com.bj58.spat.hades.bind.CheckedError;
import com.bj58.spat.hades.bind.ObjectBindResult;

public class TestBindUtil {

	private ExecutorService es = Executors.newFixedThreadPool(10);
	
	@Test
	public void testvalidate() throws InterruptedException{
		
	
				
					HelloEntity he = new HelloEntity();
					he.setId(0);
					he.setName("qqq");
					ObjectBindResult br = BindAndValidate.validate(he);
					System.out.println(br.getErrorCount());
					List<CheckedError> list = br.getErrors();
					for (CheckedError error : list){
						System.out.println(error.getMessage());
					}
					
					HelloEntity he1 = new HelloEntity();
					he1.setId(1);
//					he1.setName("ddddddddddddddddddddddddddd");
					ObjectBindResult br1 = BindAndValidate.validate(he1);
					
					List<CheckedError> hellolist1 = br1.getErrors();
					for (CheckedError error : hellolist1){
						System.out.println(error.getMessage());
					}

					GoodByeEntity gbe = new GoodByeEntity();
					gbe.setId(1);
					gbe.setName("qqq");
					gbe.setQqq("babbbabbaaaa");
					ObjectBindResult obr2 = BindAndValidate.validate(gbe);
					System.out.println(Thread.currentThread().getId()+obr2.getErrorCount());
					List<CheckedError> list2 = obr2.getErrors();
					for (CheckedError error : list2){
						System.out.println(error.getMessage());
					}

	}
}
