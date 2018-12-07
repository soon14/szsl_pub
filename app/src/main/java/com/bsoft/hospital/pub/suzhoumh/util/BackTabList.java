package com.bsoft.hospital.pub.suzhoumh.util;

import java.util.LinkedList;

/**
 * 记录底部Bar操作步骤
 * 
 * @author zkl
 * @version 2012-8-9 上午08:57:17
 * 
 */

public class BackTabList {
	int max;
	LinkedList<String> mBackTabList;

	/**
	 * 记录多少步
	 * 
	 * @param max
	 */
	public BackTabList(int max) {
		this.max = max;
		this.mBackTabList = new LinkedList<String>();
	}

	public int size() {
		return mBackTabList.size();
	}

	public String removeFirst() {
		return mBackTabList.removeFirst();
	}

	public String getFirst() {
		return mBackTabList.getFirst();
	}

	public void addFirst(String first) {
		mBackTabList.addFirst(first);
		if (mBackTabList.size() > max) {
			//mBackTabList.removeLast();
			mBackTabList.remove(max-2);
		}
	}

}
