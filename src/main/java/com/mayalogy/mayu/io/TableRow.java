package com.mayalogy.mayu.io;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a table row.
 */
public class TableRow {

	private List<String> values= new ArrayList<String>();
	private String line = null;
	
	TableRow(String line) {
		this.line=line;
	}
	
	public String getLine() {
		return this.line;
	}
	
	public int getNumberOfColumns() {
		return values.size();
	}

	void addColumn(String v) {
		if(v==null)
			throw new IllegalArgumentException("Null vals not allowed");
		values.add(v.trim());
	}

	private boolean hasColumn(int column) {
		return column>=0 && column < getNumberOfColumns();
	}

	public String getValueForColumn(int column) {
		if(hasColumn(column)) {
			return values.get(column);
		}
		return null;
	}

	public int getValueForColumnAsInt(int colNum) {
		if(hasColumn(colNum)) {
			return Integer.parseInt(getValueForColumn(colNum));
		}
		return -1;
	}

	public boolean getValueForColumnAsBoolean(int colNum) {
		if(hasColumn(colNum)) {
			return Boolean.valueOf(getValueForColumn(colNum));
		}
		return false;
	}
	
	/**
	 * Returns values present in a column. Presumes vals are comma separated.
	 */
	public String[] getValueForColumnAsStringArray(int colNum) {
		if(hasColumn(colNum)) {
			String[] parts = getValueForColumn(colNum).trim().split(",");
			String[] cv = new String[parts.length];
			for(int i=0; i<parts.length; i++) {
				cv[i]=parts[i].trim();
			}
			return cv;
		}
		return null;
	}
}
