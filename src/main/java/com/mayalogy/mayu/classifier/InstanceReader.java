package com.mayalogy.mayu.classifier;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cc.mallet.types.Alphabet;
import cc.mallet.types.FeatureVector;
import cc.mallet.types.Instance;

public class InstanceReader {

	private Instance instance = null;

	InstanceReader(Instance i) {
		this.instance=i;
	}
	
	private FeatureVector getFeatures() {
		return (FeatureVector)instance.getData();
	}

	List<String> getSampleWords() {
		List<String> words = new ArrayList<String>();
		FeatureVector fv = getFeatures();
		int[] i = fv.getIndices();
		List<String> dict = getCompleteDictionary();
		for(int j:i) {
			words.add(dict.get(j));
		}
		return words;
	}

	List<String> getCompleteDictionary() {
		return getEntries(getFeatures().getAlphabet());
	}

	List<String> getTargetLabels() {
		return getEntries(instance.getTargetAlphabet());
	}

	private List<String> getEntries(Alphabet a) {
		List<String> dict = new ArrayList<String>();
		@SuppressWarnings("rawtypes")
		Iterator iter = a.iterator();
		while(iter.hasNext()) {
			Object o = iter.next();
			dict.add(o.toString());
		}
		return dict;
	}

}
