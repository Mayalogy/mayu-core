package com.mayalogy.mayu.classifier;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrainingFileBalancer {

	private static Logger logger = LoggerFactory
			.getLogger(TrainingFileBalancer.class);

	/**
	 * Ensures the same number of pos and neg training examples given a directory containing directories of the form [label]/[polarity]-[sublabel] 
	 * as in: experienced_symptom/NEGATIVE-experienced_symptom or experienced_symptom/POSITIVE-experienced_symptom.
	 * 
	 * @param dir i.e. /blah/labels where inside of labels are things like experienced_symptom, is_medical_history, etc.
	 */
	public static void balance(String dir) {
		File d = new File(dir);
		for(File label:d.listFiles()) { //label=experienced_symptom
			int maxFiles=computeMax(label);
			for(File trainingDir:label.listFiles()) {
				prune(trainingDir, maxFiles);
			}
		}
	}

	public static int computeMax(File label) {
		int maxFiles=Integer.MAX_VALUE;
		for(File trainingDir:label.listFiles()) { //trainingDir=NEGATIVE-experienced_symptom
			int nf=trainingDir.list().length;
			if(nf<maxFiles)
				maxFiles=nf;
		}
		return maxFiles;
	}

	/**
	 * Computes max allowable for balance, i.e. 5 if is_medical_history/POS* has 5 and is_medical_history/NEG* has 354.
	 * 
	 * Key=signal like: is_medical_history, value = max.
	 */
	public static Map<String, Integer> computeMaxAllowable(String dir) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		File d = new File(dir);
		for(File label:d.listFiles()) { //label=experienced_symptom
			int maxFiles=Integer.MAX_VALUE;
			for(File trainingDir:label.listFiles()) { //trainingDir=NEGATIVE-experienced_symptom
				int nf=trainingDir.list().length;
				if(nf<maxFiles)
					maxFiles=nf;
			}
			map.put(label.getName(), maxFiles);
		}
		return map;
	}

	/**
	 * Deletes files until num files = max.
	 */
	private static void prune(File d, int max) {
		if(d.list().length>max) {
			int nToDel = d.list().length-max;
			int n=1;
			for(File f:d.listFiles()) {
				if(n++>nToDel)
					break;

				f.delete();
			}
		}
		return;
	}
}
