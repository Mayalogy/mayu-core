package com.mayalogy.mayu.topsy;

public class QueryContext {
    
    TimeSliceParameters timeSliceParams = new TimeSliceParameters(1,1);
    
    private int numberOfPages = 1;
    private int resultsPerPage=10;
    
    public void setTimeSliceParameters(TimeSliceParameters tsp) {
        timeSliceParams=tsp;
    }
    
    public TimeSliceParameters getTimeSliceParameters() {
        return this.timeSliceParams;
    }
    
    public int getNumberOfPages() {
        return this.numberOfPages;
    }
    
    public int getResultsPerPage() {
        return this.resultsPerPage;
    }
    
    public void setNumberOfPages(int nPages) {
        this.numberOfPages=nPages;
    }
    
    public void setResultsPerPage(int rpp) {
        this.resultsPerPage=rpp;
    }
}

