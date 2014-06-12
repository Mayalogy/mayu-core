package com.mayalogy.mayu.classifier;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import junit.framework.TestCase;

import org.apache.commons.io.FileUtils;

import com.mayalogy.mayu.core.WordGramExtractor;

public class TestTrainingFileBalancer extends TestCase {

	public void testScrambleFiles() throws IOException {
	    Random generator = new Random();
		File d = new File("src/test/resources/data/classifier/n-test");
		for(File sd:d.listFiles()) {
			System.out.println("Scrambling contents of: " + sd.getAbsolutePath());
			for(File f:sd.listFiles()) {
				if(f.isDirectory()) continue;
				System.out.println("Scrambling: " + f.getAbsolutePath());
				String c = FileUtils.readFileToString(f);
				List<String> ws = WordGramExtractor.get1Gram(c, false);
				Collections.shuffle(ws);
				StringBuilder sb = new StringBuilder();
				for(String w:ws) {
					if((generator.nextInt(6) + 1)==1 || w.startsWith("http")) {
						System.out.println("dropping word: " + w);
						continue;
					} else {
						System.out.println("adding word: " + w);
					}
					sb.append(w+" ");
				}
				File o = new File(f.getAbsolutePath()+".s");
				System.out.println("Writing to: " + o.getAbsolutePath());
				System.out.println(sb.toString());
				FileUtils.writeStringToFile(o, sb.toString());
				f.delete();
			}
		}
	}
	
	public void testBalance() throws IOException {
		File tfb = new File("src/main/resources/data/temp/TestTrainingFileBalancer");
		if(tfb.exists())
			FileUtils.deleteDirectory(tfb);

		tfb.mkdir();

		File label = new File(tfb.getAbsolutePath()+"/is_medical_history");
		label.mkdir();

		File f1 = new File(label.getAbsolutePath()+"/1");
		File f2 = new File(label.getAbsolutePath()+"/2");

		populate(f1,10);
		populate(f2,100);

		assertTrue(f1.list().length!=f2.list().length);
		Map<String, Integer> map = TrainingFileBalancer.computeMaxAllowable(tfb.getAbsolutePath());
		for(String l:map.keySet()) {
			System.out.println(l+": "+map.get(l));
		}
		TrainingFileBalancer.balance(tfb.getAbsolutePath());
		assertTrue(f1.list().length==f2.list().length);

		FileUtils.deleteDirectory(tfb);
	}

	private void populate(File f, int num) throws IOException {
		f.mkdir();

		for(int i=0;i<num; i++) {
			File n = new File(f.getAbsolutePath()+"/"+i+".txt");
			FileUtils.writeStringToFile(n, "hello banana");
		}
	}
}
