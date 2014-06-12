package com.mayalogy.mayu.classifier;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.mallet.classify.Classification;
import cc.mallet.classify.Classifier;
import cc.mallet.classify.ClassifierTrainer;
import cc.mallet.classify.MaxEntTrainer;
import cc.mallet.classify.Trial;
import cc.mallet.types.InstanceList;
import cc.mallet.util.Randoms;


public class MalletClassifier implements Serializable {

    private static final long serialVersionUID = 1L;
    private static Logger logger = LoggerFactory
			.getLogger(MalletClassifier.class);
    private Classifier classifier = null;
    
    public Classifier getClassifier() {
    	return classifier;
    }

    /**
     * Returns error rate based on random sub sample validation.
     * @param trainingDir File handle to dir containing documents.
     * @param numSplits Number of times to split the document directory and re-run the random sub sample
     */
    public static Evaluation evaluate(String trainingDir, int numSplits) throws IOException {
    	logger.info("Evaluating class data in: " + trainingDir);
        Evaluation e = new Evaluation( performRandomSubSampleValidation(numSplits, .2d, InstanceListGenerator.getInstance().getInstanceList(trainingDir)),
                performRandomSubSampleValidation(numSplits, .1d, InstanceListGenerator.getInstance().getInstanceList(trainingDir)));
        e.setTrainingSetError(computeTrainingSetErrorIndependently(trainingDir));
        return e;
    }
    
    public static double evaluate(String trainingDir, int numSplits, double percentTest) throws IOException {
        if(percentTest<0||percentTest>1)
            throw new IllegalArgumentException("percentTest must be > 0 and < 1");
        
        return performRandomSubSampleValidation(numSplits, percentTest, InstanceListGenerator.getInstance().getInstanceList(trainingDir));
    }

    public static double computeTrainingSetErrorIndependently(String trainingDir) throws IOException {
        MalletClassifier mc = new MalletClassifier();
        logger.info("Training classifier using: " + trainingDir);
        mc.train(trainingDir);
        double errors = 0; double total = 0;
        for(ClassData cd:FileLoader.load(trainingDir)) {
            for(Document d:cd.getDocuments()) {
            	//logger.debug("Independently trained classifier - known label: " + cd.getName()+ " - classifying: " + d.getContent() + " and got: " + mc.classify(d.getContent()));
            	//logger.debug("Independently trained classifier - known label: " + cd.getName()+ " - classifying: " + d.getContent() + " and got: " + mc.classify(d.getContent()) + " from source: " + ConsistencyTracker.getInstance().getSentenceFromHiw(d.getContent()));
            	if(!cd.getName().equals(mc.classify(d.getContent())))
                    errors++;

                total++;
            }
        }
       return errors/total;
    }
    
    /**
     * Performs repeated sub sample validation.
     * @param numSplits The number of times to split the input data.
     * @param percentTested The percentage of data to test verse train. I.e. .1 corresponds to 90% training data, 10% test.
     * @return The average error across the multiple splits.
     */
    private static double performRandomSubSampleValidation(int numSplits, double percentTested, InstanceList instances) {
        double sum = 0;
        for(int i=0; i<numSplits; i++) {
            Trial t = testTrainSplit(instances, percentTested);
            sum=sum+(1-t.getAccuracy());
        }
        return sum/numSplits;
    }

    private static Trial testTrainSplit(InstanceList instances, double percentTested) {

        int TRAINING = 0; int TESTING = 1;

        /**
         * Split the input list into training (90%) and testing (10%) lists. The division takes place by creating a 
         * copy of the list, randomly shuffling the copy, and then allocating instances to each sub-list based on 
         * the provided proportions.   
         */                               
        InstanceList[] instanceLists = instances.split(new Randoms(), new double[] {(1-percentTested), percentTested, 0.0});

        /**
         * The third position is for the "validation" set, which is a set of instances not used directly for training,
         * but available for determining when to stop training and for estimating optimal settings of nuisance parameters. 
         * Most Mallet ClassifierTrainers can not currently take advantage of validation sets.                                                                            
         */
        ClassifierTrainer<?> trainer = new MaxEntTrainer(); //TODO max classifier trainer easily configurable
        Classifier c =  trainer.train(instanceLists[TRAINING]);
        return new Trial(c, instanceLists[TESTING]);
    }

    public void train(String trainingDir) throws IOException {
        ClassifierTrainer<?> trainer = new MaxEntTrainer ();
        classifier = trainer.train(InstanceListGenerator.getInstance().getInstanceList(trainingDir));
    }

    public void train(String rootPath, List<ClassData> trainingData) throws IOException {
        File mlc = new File(rootPath+"/mallet/training");
        mlc.mkdir();
        for(ClassData cd:trainingData) {
            File n = new File(mlc.getAbsolutePath()+"/"+cd.getName());
            System.out.println("Creating: " + n.getAbsolutePath());
            n.mkdir();
            int i=0;
            for(Document d:cd.getDocuments()) {
                File df = new File(n.getAbsolutePath()+"/"+(i++)+".txt");
                FileUtils.writeStringToFile(df, d.getContent());
            }
        }
        train(mlc.getAbsolutePath());
        FileUtils.deleteDirectory(mlc);
    }


    public String classify(String text) {
        Classification classification = classifier.classify(text);
        return classification.getLabeling().getBestLabel().toString();
    }
}
