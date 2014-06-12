package com.mayalogy.mayu.core;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class TestDistanceDeterminer extends TestCase {

	public void testCalcCentroidDistance() {
		List<Term> tFVM = Centroid.generate("i eat potatoes on tuesday weekday i fries on tuesday weekday");
		List<Term> tFVM1 = Centroid.generate("i eat weekday");
		List<Term> tFVM2 = Centroid.generate("i am a martian");
		List<Term> tFVM3 = Centroid.generate("i eat potatoes and fries on tuesday");
		List<Term> tFVM4 = Centroid.generate("I eat Potatoes and Fries on Tuesday");


		float f1 = DistanceDeterminer.calcCosineDistanceFromTerms(tFVM1, tFVM);
		float f2 = DistanceDeterminer.calcCosineDistanceFromTerms(tFVM2, tFVM);
		float f3 = DistanceDeterminer.calcCosineDistanceFromTerms(tFVM3, tFVM);
		float f4 = DistanceDeterminer.calcCosineDistanceFromTerms(tFVM4, tFVM);

		System.out.println("1:"+f1);
		System.out.println("2:"+f2);
		System.out.println("3:"+f3);
		System.out.println("4:"+f4);

		assertTrue(f1<f2);
		assertTrue(f3<f2);
		assertTrue(f3<f1);
	}
	
    public void testCalcCosine() {
        List<String> d1 = new ArrayList<String>(); d1.add("a"); d1.add("c"); d1.add("c");
        List<String> d2 = new ArrayList<String>(); d2.add("a"); d1.add("c"); d1.add("c");
        assertEquals(DistanceDeterminer.calcCosine(d1, d2), DistanceDeterminer.calcCosineDistanceFromTerms(getAsTerms(d1), getAsTerms(d2)));
    }

    private List<Term> getAsTerms(List<String> z) {
        List<Term> es = new ArrayList<Term>(z.size());
        for(String s:z) {
        	Term e = new Term(s);
            e.setCount(e.getCount()+1);
            es.add(e);
        }
        return es;
    }
}
