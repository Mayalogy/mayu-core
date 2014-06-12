package com.mayalogy.mayu.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Jama.Matrix;

public class DistanceDeterminer {

    public static float calcCosine(List<String> tFVector1, List<String> tFVector2) {
        final Set<String> totalWords = new HashSet<String>();
        totalWords.addAll(tFVector1);
        final int termsInFirstString = totalWords.size();
        final Set<String> tFVectorSet = new HashSet<String>();
        tFVectorSet.addAll(tFVector2);
        final int termsInSecondString = tFVectorSet.size();

        totalWords.addAll(tFVectorSet);
        final int commonWords = (termsInFirstString + termsInSecondString) - totalWords.size();
        float distance = 1-(float) (commonWords) / (float) (Math.pow((float) termsInFirstString, 0.5f) * Math.pow((float) termsInSecondString, 0.5f));
        return distance;
    }

    public static float calcCosineDistanceFromTerms(List<Term> tFVector1, List<Term> tFVector2) {
        Map<String, Integer> terms = new HashMap<String, Integer>(tFVector1.size()+tFVector2.size());
        int idx = 0;
        for(Term term: tFVector1) {
            if(!terms.containsKey(term.getValue()))
                terms.put(term.getValue(), idx++);
        }
        for(Term term: tFVector2) {
            if(!terms.containsKey(term.getValue()))
                terms.put(term.getValue(), idx++);
        }

        Matrix matrix1 = new Matrix(terms.size(), 1, 0.0D);
        for(Term term:tFVector1) {
            matrix1.set(terms.get(term.getValue()), 0, (double)term.getCount());
        }

        Matrix matrix2 = new Matrix(terms.size(), 1, 0.0D);
        for(Term term:tFVector2) {
            matrix2.set(terms.get(term.getValue()), 0, (double)term.getCount());
        }

        double dotProduct = matrix1.arrayTimes(matrix2).norm1();
        double eucledianDististance = matrix1.normF()*matrix2.normF();
        return 1-(float)(dotProduct / eucledianDististance);
    }
    
    public static float calcJaccard(List<String> tFVector1, List<String> tFVector2) {
        Set<String> completeTermSet = new HashSet<String>();
        completeTermSet.addAll(tFVector1);
        Set<String> tFVectorSet = new HashSet<String>();
        tFVectorSet.addAll(tFVector2);
        completeTermSet.addAll(tFVectorSet);
        final int commonTerms = (completeTermSet.size() + tFVectorSet.size()) - completeTermSet.size();
        return 1-(float) (commonTerms) / (float) (completeTermSet.size());
    }
}