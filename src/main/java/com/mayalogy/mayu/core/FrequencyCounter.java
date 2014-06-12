package com.mayalogy.mayu.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Counts frequency of occurence of terms.
 */
public class FrequencyCounter {

    private HashMap<String, Term> terms = new HashMap<String, Term>();

    /**
     * Increases term count by specified amount.
     * @param term The term.
     * @param amount The amount to increase.
     */
    public void update(String term, float amount) {
        Term t = null;
        if (terms.containsKey(term)) {
            t = terms.get(term);
            t.increaseCount(amount);
        } else {
            t = new Term(term);
            t.increaseCount(amount);
            terms.put(term, t);
        }
    }
    
    public void update(String term) {
        update(term, 1);
    }

    private List<Term> getMostFrequent(int maxTerms, int minCount, boolean ignoreMinCount) {
        if(minCount<1)
            minCount=1;

        if(maxTerms<0)
            throw new IllegalArgumentException("maxTerms: " + maxTerms 
                    + " must be greater than 0. ");

        float highestCount = 0;
        for(Term e:getAll()) {
            if(e.getCount()>highestCount)
                highestCount=e.getCount();
        }

        ArrayList<Term> pop = new ArrayList<Term>(maxTerms);
        float totalCounted = 0;
        
        for(String termName:terms.keySet()) {
            Term t = terms.get(termName);
            totalCounted=totalCounted+t.getCount();
            if(ignoreMinCount) {
                pop.add(t);
            } else if(t.getCount()>=minCount) {
                pop.add(t);
            }
        }
        Term.sort(pop);
        for(Term t:pop) {
            t.setTotalCounted(totalCounted); //Needed for Percent determination.
        }
        if(maxTerms>pop.size()) {
            return pop;
        } else {
            return pop.subList(0, maxTerms);
        }
    }

    public List<Term> getMostFrequent(int maxTerms, int minCount) {
        return getMostFrequent( maxTerms, minCount, false);
    }

    public void reset() {
        terms = new HashMap<String, Term>();
    }

    public List<Term> getMostFrequent(int maxTerms) {
        return getMostFrequent(maxTerms, 1, true);
    }

    public List<Term> getAllRankedByFrequency() {
        return getMostFrequent(size(), 1);
    }

    public List<Term> getAll() {
        return new ArrayList<Term>(this.terms.values());
    }

    public int size() {
        return terms.size();
    }
}
