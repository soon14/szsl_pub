package com.bsoft.hospital.pub.suzhoumh.activity.app.news;

import org.json.JSONException;
import org.json.JSONObject;

import com.app.tanklib.model.AbsBaseVoSerializ;

/**
 * 资讯内容详情
 * @author Administrator
 *
 */
public class NewsItem extends AbsBaseVoSerializ{

	public String drid;
	public String uid;
	public String title;
	public String content;
	public String createdate;
	public String imgurl;
	
	@Override
	public void buideJson(JSONObject job) throws JSONException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public JSONObject toJson() {
		// TODO Auto-generated method stub
		return null;
	}
}
