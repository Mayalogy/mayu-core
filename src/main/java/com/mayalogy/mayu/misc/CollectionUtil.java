package com.mayalogy.mayu.misc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CollectionUtil {

	public static String[] convert(Collection<String> c) {
		if(c==null)
			return null;

		String[] r = new String[c.size()];
		int i=0;
		for(String s:c) {
			r[i]=s;
			i++;
		}
		return r;
	}

	public static List<String> convert(String[] c) {
		List<String> l = new ArrayList<String>();
		if(c!=null) {
			for(String s:c) {
				if(s!=null) {
					l.add(s);
				}
			}
		}
		return l;
	}

	public static void sortByLength( List<String> terms ) {
		StringLengthComparator comparator =
				StringLengthComparator.getInstance();
		Collections.sort(terms, comparator);
	}

} 

class StringLengthComparator implements Comparator<String> {

	StringLengthComparator(){
		super();
	}

	public static StringLengthComparator getInstance(){
		return new StringLengthComparator();
	}

	public int compare( String r1, String r2 ) {
		return r2.length()-r1.length();
	}
}
