package test.spring;

import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

import com.bj58.spat.hades.utils.AntPathMatcher;
import com.bj58.spat.hades.utils.PathMatcher;


public class TestAntMatch{

	@Test
	public void testPathMatch(){
		PathMatcher pathMatcher = new AntPathMatcher();
		String registeredPath = "/me/hello/{name}";
		String url = "/me/hello/renjun";
		Assert.assertTrue(pathMatcher.match(registeredPath, url));

		Map<String, String> values = pathMatcher.extractUriTemplateVariables(registeredPath, url);
		Assert.assertEquals(1, values.size());
		Assert.assertEquals("renjun", values.get("name"));
		
		System.out.println("OK testpathMatch");
		
		
	}


}
