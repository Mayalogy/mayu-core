package com.mayalogy.mayu.classifier;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/*
 * Loads ClassData from a file directory structure where dir names are class names and files are text docs.
 */
public class FileLoader {

	private static Logger logger = LoggerFactory.getLogger(FileLoader.class);

	public static List<ClassData> load(String filePath) throws IOException {
		logger.info("Loading class data from: " + filePath);
		File f = new File(filePath);
		if(!f.exists())
			throw new IllegalArgumentException("Path: " + filePath + " does not exist.");
		List<ClassData> cdl = new ArrayList<ClassData>();
		for(File dir:f.listFiles()) {
			if(f.isDirectory()) {
				ClassData cd = new ClassData(dir.getName());
				for(File df:dir.listFiles()) {
					if(df.isFile() && !df.getAbsolutePath().contains(".svn")) {
						Document d = new Document(df.getCanonicalPath(), FileUtils.readFileToString(df));
						cd.add(d);
					}
				}
				cdl.add(cd);
			}
		}
		return cdl;
	}
}
