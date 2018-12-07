package com.app.tanklib.http;

import org.apache.http.message.BasicNameValuePair;

/**
 * @author Tank E-mail:zkljxq@126.com
 * 
 * @类说明
 */
public class BsoftNameValuePair extends BasicNameValuePair implements Comparable<BsoftNameValuePair>{

	public BsoftNameValuePair(String name, String value) {
		super(name, value);
	}

	public int compareTo(BsoftNameValuePair o) {
		return this.getName().compareTo(o.getName());
	}

}
