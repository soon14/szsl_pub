package com.bsoft.hospital.pub.suzhoumh.view.sort;

import java.util.Comparator;

/**
 * 
 *
 */
public class PinyinComparator implements Comparator<String> {

	public int compare(String o1, String o2) {
		if (o1.equals("@")
				|| o2.equals("#")) {
			return -1;
		} else if (o1.equals("#")
				|| o2.equals("@")) {
			return 1;
		} else {
			return o1.compareTo(o2);
		}
	}
	
	
//	public int compare(SortModel o1, SortModel o2) {
//		if (o1.sortLetters.equals("@")
//				|| o2.sortLetters.equals("#")) {
//			return -1;
//		} else if (o1.sortLetters.equals("#")
//				|| o2.sortLetters.equals("@")) {
//			return 1;
//		} else {
//			return o1.sortLetters.compareTo(o2.sortLetters);
//		}
//	}

}
