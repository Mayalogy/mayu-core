package com.mayalogy.mayu.classifier;

import java.io.IOException;
import java.util.List;

import junit.framework.TestCase;

public class TestFileLoader extends TestCase {

    public void testLoad() throws IOException {
        List<ClassData> trainingData = FileLoader.load("src/test/resources/data/classifier/n-test");
        assertTrue(trainingData.size()>=4);
        for(ClassData cd:trainingData) {
            System.out.println(cd);
        }
    }
}
