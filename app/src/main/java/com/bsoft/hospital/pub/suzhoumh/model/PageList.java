package com.bsoft.hospital.pub.suzhoumh.model;

import java.util.ArrayList;

import org.json.JSONArray;

import com.app.tanklib.model.AbsBaseVoSerializ;

import android.content.Context;
import android.widget.Toast;

/**
 * 类说明
 * 
 * @author zkl
 * 
 */

public class PageList<T extends AbsBaseVoSerializ> {
	public ArrayList<T> list;

	public T data;
	
	public String dataStr;
	// 状态值
	public int statue = Statue.ERROR;

	public boolean hasnextpage = false;

	public String message;
	
	public int count;
	// 第几条
	public int pagestart = 0;

	public void  setData(T t) {
		this.data = t;
	}

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
	
	public String toJsonArrayString(){
		if(null!=list&&list.size()>0){
			JSONArray arr=new JSONArray();
			for(T t:list){
				arr.put(t.toJson());
			}
			return arr.toString();
		}
		return null;
	}
}
