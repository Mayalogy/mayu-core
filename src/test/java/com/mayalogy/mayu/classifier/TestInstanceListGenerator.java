package com.mayalogy.mayu.classifier;

import java.io.IOException;
import java.util.List;


import junit.framework.TestCase;
import cc.mallet.types.Instance;

public class TestInstanceListGenerator extends TestCase {

	public void testGetInstanceList() throws IOException {
		
		for(Instance i:InstanceListGenerator.getInstance().getInstanceList("src/test/resources/data/classifier/simple")) {
			System.out.println("*** complete dictionary ***");
			InstanceReader ir = new InstanceReader(i);
			assertTrue(contains("feed", ir.getCompleteDictionary()));
			assertTrue(!contains("i", ir.getCompleteDictionary()));

			System.out.println("*** sample words ***");
			for(String w:ir.getSampleWords()) {
				System.out.println(w);
			}
			
			
			System.out.println("*** target labels ***");
			for(String w:ir.getTargetLabels()) {
				System.out.println(w);
			}
		}
		
		InstanceListGenerator.getInstance().addStopword("feed");
		InstanceListGenerator.getInstance().removeStopword("i");
		for(Instance i:InstanceListGenerator.getInstance().getInstanceList("src/test/resources/data/classifier/simple")) {
			System.out.println("*** complete dictionary ***");
			InstanceReader ir = new InstanceReader(i);
			assertTrue(!contains("feed", ir.getCompleteDictionary()));
			assertTrue(contains("i", ir.getCompleteDictionary()));
		}
		
	}
	
	private boolean contains(String a, List<String> bucket) {
		System.out.println("---");
		for(String w:bucket) {
			System.out.println(w);
		}
		return bucket.contains(a);
	}
}
