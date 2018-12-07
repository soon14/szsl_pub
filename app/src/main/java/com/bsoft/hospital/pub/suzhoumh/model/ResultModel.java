package com.bsoft.hospital.pub.suzhoumh.model;

import android.content.Context;
import android.widget.Toast;

/**
 * 类说明
 * 
 * @author zkl
 * 
 */

public class ResultModel<T> {

	public T data;

	public T list;

	// 状态值
	public int statue = Statue.ERROR;

	public boolean hasnextpage = false;

	public String message = "请求失败";

	public int count;
	
	public int totalPage = 0;//总的页数，分页的时候用到

	public String warn;


	public void showToast(Context context) {
		switch (statue) {
		case Statue.NET_ERROR:
			Toast.makeText(context, "网络加载失败", Toast.LENGTH_SHORT).show();
			break;
		case Statue.ERROR:
			Toast.makeText(context,
					null != message && message.length() > 0 ? message : "请求失败",
					Toast.LENGTH_SHORT).show();
			break;
		case Statue.PARSER_ERROR:
			Toast.makeText(context, "解析失败", Toast.LENGTH_SHORT).show();
			break;
		default:
			// Toast.makeText(context, "失败", Toast.LENGTH_SHORT).show();
			break;
		}
	}

}
