package com.mayalogy.mayu.io;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class TabularFileLoader {

	/**
	 * Reads tabular contents of a file separated by delimiter. Lines starting w/ # are skipped.
	 * 
	 * @param resourcePath Path to resource
	 * @param delimiter Comma, tab, | etc. Needs to include requisite escapes.
	 * @return A list of table rows.
	 * @throws IOException
	 */
	public static List<TableRow> load(String resourcePath, String delimiter) throws IOException {
		List<TableRow> rows = new ArrayList<TableRow>();
		InputStream is = JarReader.getResourceAsInputStream(resourcePath);
		DataInputStream in = new DataInputStream(is);
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line;
		while ((line = reader.readLine()) != null) {
			if (StringUtils.isNotBlank(line) && !line.startsWith("#")) {
				String[] values = line.split(delimiter);
				if(values.length>1) {
					TableRow r = new TableRow(line);
					for(String v:values) {
						r.addColumn(v);
					}
					rows.add(r);
				}
			}
		}
		reader.close();
		return rows;
	}

	public static List<TableRow> loadTabSeparated(String resourcePath) throws IOException {
		return load( resourcePath, "\t");
	}
}
