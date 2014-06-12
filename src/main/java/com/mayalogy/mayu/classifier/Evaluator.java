package com.mayalogy.mayu.classifier;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Evaluator {

	private static Logger logger = LoggerFactory.getLogger(Evaluator.class);

	public static List<LabelledEvaluation> evaluateClassifiers(List<File> classes) throws Exception {
		List<LabelledEvaluation> mles = new ArrayList<LabelledEvaluation>();
		for(File f:classes) {
			try {
				logger.info("Evaluating classifier: " + f.getName() + " with data in: " + f.getAbsolutePath());
				Evaluation eval = MalletClassifier.evaluate(f.getAbsolutePath(), 10);
				logger.info("\nResults\n======\n" + eval.toSummaryString());
				LabelledEvaluation mle = new LabelledEvaluation(f.getName(), eval);

				logger.info(LabelledEvaluation.getHeader());
				logger.info(mle.toString());
				mles.add(mle);
			} catch(Exception e) {
				logger.warn("Problems processing: " + f.getAbsolutePath() + " skipping.", e);
			}
		}

		StringBuilder sb = new StringBuilder();
		logger.info("\n\n"+LabelledEvaluation.getHeader());
		sb.append("\n\n"+LabelledEvaluation.getHeader());
		for(LabelledEvaluation m:mles) {
			logger.info(m.toString());
			sb.append("\n"+m.toString());
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd-HH:mm");
		File out = new File("reports/classifier-evaluation-"+sdf.format(new Date())+".txt");
		logger.info("Writing classifier evaluation results to: " + out.getAbsolutePath());
		FileUtils.writeStringToFile(out, sb.toString());
		return mles;
	}
}

