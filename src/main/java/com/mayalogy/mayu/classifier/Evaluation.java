package com.mayalogy.mayu.classifier;

public class Evaluation {

    private double tenPercentTestRandomSubSampleValidationError = -1;
    private double twentyPercentTestRandomSubSampleValidationError = -1;
    private double trainingSetError = -1;

    public Evaluation(double twentyPercentTestRandomSubSampleValidationError, double tenPercentTestRandomSubSampleValidationError ) {
        this.twentyPercentTestRandomSubSampleValidationError=twentyPercentTestRandomSubSampleValidationError;
        this.tenPercentTestRandomSubSampleValidationError=tenPercentTestRandomSubSampleValidationError;
    }
    
    public void setTrainingSetError(double tse) {
        this.trainingSetError=tse;
    }
    
    public double getTrainingSetError() {
        return this.trainingSetError;
    }
    
    public double getTwentyPercentTestRandomSubSampleValidationError() {
        return this.twentyPercentTestRandomSubSampleValidationError;
    }
    
    public double getTenPercentTestRandomSubSampleValidationError() {
        return this.tenPercentTestRandomSubSampleValidationError;
    }
    
    public String toSummaryString() {
        StringBuilder sb = new StringBuilder();
        sb.append("tenPercentTestError: " + getTenPercentTestRandomSubSampleValidationError()+"\n");
        sb.append("twentyPercentTestError: " + getTwentyPercentTestRandomSubSampleValidationError()+"\n");
        sb.append("trainingSetError: " + getTrainingSetError()+"\n");
        return sb.toString();
    }
}
