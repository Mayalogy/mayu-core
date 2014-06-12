package com.mayalogy.mayu.classifier;

public class LabelledEvaluation {

    private String name;
    private Evaluation evaluation;

    LabelledEvaluation(String name, Evaluation e) {
        this.name=name;
        this.evaluation=e;
    }

    public String getLabel() {
    	return name;
    }
    
    public Evaluation getEvaluation() {
    	return evaluation;
    }
    
    static String getHeader() {
        return "name\ttenPecentTestError\ttwentyPercentTestError\ttrainingSetError";
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name+"\t");
        sb.append(evaluation.getTenPercentTestRandomSubSampleValidationError()+"\t");
        sb.append(evaluation.getTwentyPercentTestRandomSubSampleValidationError()+"\t");
        sb.append(evaluation.getTrainingSetError()+"\t");
        return sb.toString();
    }
}