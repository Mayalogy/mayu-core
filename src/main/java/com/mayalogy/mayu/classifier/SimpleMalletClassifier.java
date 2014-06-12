package com.mayalogy.mayu.classifier;

import java.io.File;

import cc.mallet.classify.Classifier;
import cc.mallet.classify.ClassifierTrainer;
import cc.mallet.classify.NaiveBayesTrainer;
import cc.mallet.pipe.CharSequence2TokenSequence;
import cc.mallet.pipe.CharSubsequence;
import cc.mallet.pipe.FeatureSequence2FeatureVector;
import cc.mallet.pipe.Input2CharSequence;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.PrintInputAndTarget;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.Target2Label;
import cc.mallet.pipe.TokenSequence2FeatureSequence;
import cc.mallet.pipe.TokenSequenceLowercase;
import cc.mallet.pipe.TokenSequenceRemoveStopwords;
import cc.mallet.pipe.iterator.FileIterator;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;
import cc.mallet.types.Labeling;

public class SimpleMalletClassifier {

    static public void main (String[] args) {

        InstanceList ilist  = getIntancesFromDir("src/test/resources/data/test/mlc/test2/training");

        // Make a test/train split; ilists[0] will be for training; ilists[1] will be for testing
        InstanceList[] ilists = ilist.split (new double[] {.5, .5});

        // Create a classifier trainer, and use it to create a classifier
        ClassifierTrainer<?> naiveBayesTrainer = new NaiveBayesTrainer();
        Classifier classifier = naiveBayesTrainer.train (ilists[0]);

        System.out.println ("The training accuracy is "+ classifier.getAccuracy (ilists[0]));
        System.out.println ("The testing accuracy is "+ classifier.getAccuracy (ilists[1]));

        InstanceList instancesToClassify = getIntancesFromDir("src/test/resources/data/test/mlc/test2/test2");
        for(int i=0; i<instancesToClassify.size(); i++) {
            Instance ins = instancesToClassify.get(i);
            System.out.println("Classifying: " + ins.getName());

            Labeling labeling = classifier.classify(ins).getLabeling();
            for (int rank = 0; rank < labeling.numLocations(); rank++){
                System.out.print(labeling.getLabelAtRank(rank) + ":" +
                        labeling.getValueAtRank(rank) + "\n");
            }
            System.out.println();
        }

    }
    
    static InstanceList getIntancesFromDir(String dir) {
        File fdir = new File(dir);

        File[] directories = new File[fdir.listFiles().length];
        int i=0;
        for(File f:fdir.listFiles()) {
            directories[i++]=f;
        }

        // Create the pipeline that will take as input {data = File, target = String for classname}
        // and turn them into {data = FeatureVector, target = Label}
        Pipe instancePipe = new SerialPipes (new Pipe[] {
                new Target2Label (),                                                      // Target String -> class label
                new Input2CharSequence (),                                // Data File -> String containing contents
                new CharSubsequence (CharSubsequence.SKIP_HEADER), // Remove UseNet or email header
                new CharSequence2TokenSequence (),  // Data String -> TokenSequence
                new TokenSequenceLowercase (),            // TokenSequence words lowercased
                new TokenSequenceRemoveStopwords (),// Remove stopwords from sequence
                new TokenSequence2FeatureSequence(),// Replace each Token with a feature index
                new FeatureSequence2FeatureVector(),// Collapse word order into a "feature vector"
                new PrintInputAndTarget(),
        });
        
        // Create an empty list of the training instances
        InstanceList ilist = new InstanceList (instancePipe);

        // Add all the files in the directories to the list of instances.
        // The Instance that goes into the beginning of the instancePipe
        // will have a File in the "data" slot, and a string from args[] in the "target" slot.
        ilist.addThruPipe (new FileIterator (directories, FileIterator.STARTING_DIRECTORIES));
        return ilist;
    }
}

