package com.mayalogy.mayu.classifier;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import cc.mallet.pipe.CharSequence2TokenSequence;
import cc.mallet.pipe.FeatureSequence2FeatureVector;
import cc.mallet.pipe.Input2CharSequence;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.PrintInputAndTarget;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.Target2Label;
import cc.mallet.pipe.TokenSequence2FeatureSequence;
import cc.mallet.pipe.TokenSequenceLowercase;
import cc.mallet.pipe.TokenSequenceRemoveStopwords;
import cc.mallet.pipe.iterator.ArrayIterator;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;

import com.mayalogy.mayu.misc.CollectionUtil;

public class InstanceListGenerator {

	private static InstanceListGenerator instance = null;
	
	private Map<String, InstanceList> cache = new HashMap<String, InstanceList>();
	
	public static InstanceListGenerator getInstance() {
		if(instance==null) {
			instance = new InstanceListGenerator();
		}
		return instance;
	}
	
	private InstanceListGenerator() {}
	
	private Set<String> allowedStopwords = new HashSet<String>();
	private Set<String> additionalStopwords = new HashSet<String>();

	public void removeStopword(String word) {
		if(word!=null && word.length()>0 && !allowedStopwords.contains(word))
			allowedStopwords.add(word);
		
		clearCache();
	}
	
	public void addStopword(String word) {
		if(word!=null && word.length()>0 && !additionalStopwords.contains(word))
			additionalStopwords.add(word);
		
		clearCache();
	}
	
	@SuppressWarnings("unused")
	private InstanceList makePipe(Input2CharSequence charSequence,
			CharSequence2TokenSequence tokenSequence, boolean lowercaseTokenSequence,
			TokenSequenceRemoveStopwords tokenSequenceRemoveStopwords,
			TokenSequence2FeatureSequence ts2FeatureSequence,
			Target2Label target2Label,
			FeatureSequence2FeatureVector featureSequence2FeatureVector,
			boolean printLabel) throws IOException {

		if (charSequence == null || tokenSequence == null)
			throw new IllegalArgumentException(
					"charSequence or CharSequence2TokenSequence cannot be null");

		ArrayList<Pipe> pipelist = new ArrayList<Pipe>();

		boolean res = pipelist.add(charSequence);

		pipelist.add(tokenSequence);

		if (lowercaseTokenSequence == true) {
			TokenSequenceLowercase lowercase = new TokenSequenceLowercase();
			pipelist.add(lowercase);
		}

		if (tokenSequenceRemoveStopwords != null)
			pipelist.add(tokenSequenceRemoveStopwords);

		if (ts2FeatureSequence != null)
			pipelist.add(ts2FeatureSequence);

		if (target2Label != null)
			pipelist.add(target2Label);

		if ((featureSequence2FeatureVector == null && ts2FeatureSequence != null)
				|| (featureSequence2FeatureVector != null && ts2FeatureSequence == null))
			throw new IllegalArgumentException(
					"Invalid FeatureSequence2FeatureVector or TokenSequence2FeatureSequence parameter");

		if (featureSequence2FeatureVector != null)
			pipelist.add(featureSequence2FeatureVector);

		if (printLabel == true)
			pipelist.add(new PrintInputAndTarget("abc"));

		Pipe pipe = new SerialPipes(pipelist);
		return new InstanceList(pipe);
	}

	private void addThruPipe(InstanceList builtList, Iterator<Instance> iterator) {
		builtList.addThruPipe(iterator);
	}
	
	private void clearCache() {
		this.cache=new HashMap<String, InstanceList>();
	}
	
	/**
	 * Returns Mallet InstanceList based on specified configuration.
	 */
	public InstanceList getInstanceList(String directory) throws IOException {
		if(cache.containsKey(directory)) {
			return cache.get(directory);
		}
		List<ClassData> classes = FileLoader.load(directory);
		String[][][] trainingData = new String[classes.size()][][];

		int x =0;
		for(ClassData cd:classes) {
			trainingData[x]=new String[2][];
			int y=0;
			trainingData[x][0]=new String[cd.getDocuments().size()];
			for(Document d:cd.getDocuments()) {
				trainingData[x][0][y]=d.getContent();
				y++;
			}
			trainingData[x][1]=new String[1];
			trainingData[x][1][0]=cd.getName();
			x++;
		}
		InstanceList rlist = getInstanceList(trainingData);
		cache.put(directory, rlist);
		return rlist;
	}

	private TokenSequenceRemoveStopwords generateTokenSequenceRemoveStopwords() {
		TokenSequenceRemoveStopwords ts = new TokenSequenceRemoveStopwords(false, true);
		ts.removeStopWords(CollectionUtil.convert(allowedStopwords));
		ts.addStopWords(CollectionUtil.convert(additionalStopwords));
		return ts;
	}
	
	private InstanceList getInstanceList (String [][][] trainingdata) throws IOException {      
		Pattern tokenPattern = Pattern.compile("[\\p{L}\\p{N}_]+");
		InstanceList builtList = makePipe( new Input2CharSequence("UTF-8"),
				new CharSequence2TokenSequence(tokenPattern), 
				true, 
				generateTokenSequenceRemoveStopwords(), 
				new TokenSequence2FeatureSequence(), 
				new Target2Label(), 
				new FeatureSequence2FeatureVector(), 
				false);

		for (int i = 0; i < 3; i++) {
			try {
				addThruPipe (builtList, new ArrayIterator (trainingdata[i][0],trainingdata[i][1][0]));
			} catch (Exception e) { } //TODO figure out why out of bounds are normal here, for now suppress.
		}
		return builtList;
	}
}
