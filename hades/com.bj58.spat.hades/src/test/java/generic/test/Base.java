package generic.test;



import java.util.LinkedHashSet;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;

public class Base<T> {
	
	@Test
	public void testSet(){
		Set<Long> set = new LinkedHashSet<Long>();
		set.add(100L);
		set.add(1L);
		set.add(100L);
		set.add(100L);
		set.add(100L);
		
		Assert.assertEquals(2, set.size());
		
		
	}

}
