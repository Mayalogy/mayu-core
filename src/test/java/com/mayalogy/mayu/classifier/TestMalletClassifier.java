package com.mayalogy.mayu.classifier;

import java.util.HashMap;
import java.util.Map;


import junit.framework.TestCase;

public class TestMalletClassifier extends TestCase {

    public void testOnObviousCats() throws Exception {
        String[] trainingSets = { "src/test/resources/data/classifier/testObviousCats"};
        assertMaxErrorValues(trainingSets, .07f, .07f, .07f); //Note: this is a statistical test, it might fail on occassion. If so, bump the thresholds.
    }

    private void assertMaxErrorValues(String[] trainingSets, float maxTenPercentTestRandomSubSampleValidationError, 
            float maxTwentyPercentTestRandomSubSampleValidationError, float maxTrainingSetError) throws Exception {
        Map<String, Evaluation> evalMap = new HashMap<String, Evaluation>();
        for(String ts:trainingSets) {
            Evaluation eval = MalletClassifier.evaluate(ts, 5);
            evalMap.put(ts, eval);
        }
        for(String c:evalMap.keySet()) {
            System.out.println("\nEfficacy for: " + c);
            Evaluation eval=evalMap.get(c);
            System.out.println("TenPercentTestRandomSubSampleValidationError: " + eval.getTenPercentTestRandomSubSampleValidationError());
            System.out.println("TwentyPercentTestRandomSubSampleValidationError: " + eval.getTwentyPercentTestRandomSubSampleValidationError());
            System.out.println("Training Set Error: " + eval.getTrainingSetError());

            assertTrue(eval.getTenPercentTestRandomSubSampleValidationError()<maxTenPercentTestRandomSubSampleValidationError);
            assertTrue(eval.getTwentyPercentTestRandomSubSampleValidationError()<maxTwentyPercentTestRandomSubSampleValidationError);
            assertTrue(eval.getTrainingSetError()<maxTrainingSetError);
        }
    }

    public void testOnNewsFiles() throws Exception {        
        String[] trainingSets = {"src/test/resources/data/classifier/n-test"};
        assertMaxErrorValues(trainingSets, .6f, .6f, .6f);
    }

}
