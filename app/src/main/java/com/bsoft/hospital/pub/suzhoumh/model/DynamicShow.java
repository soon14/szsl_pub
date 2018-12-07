package com.bsoft.hospital.pub.suzhoumh.model;

import org.json.JSONException;
import org.json.JSONObject;

import com.app.tanklib.model.AbsBaseVoSerializ;

/**
 * 动态
 * 
 * 
 * @author zkl
 * 
 */

public class DynamicShow extends AbsBaseVoSerializ {
	private static final long serialVersionUID = 1L;

	public String content;
	public String title;
	public long createdate;
	public int replycount;
	public long uid;
	public String realname;
	public String imgurl;
	public String drid;
	public String header;


	@Override
	public void buideJson(JSONObject job) {
//		try {
//			if (!job.isNull("content")) {
//				this.content = job.getString("content");
//			}
//			if (!job.isNull("title")) {
//				this.title = job.getString("title");
//			}
//			if (!job.isNull("replycount")) {
//				this.replycount = job.getInt("replycount");
//			}
//			if (!job.isNull("createdate")) {
//				this.createdate = job.getLong("createdate");
//			}
//			if (!job.isNull("drid")) {
//				this.drid = job.getString("drid");
//			}
//			if (!job.isNull("uid")) {
//				this.uid = job.getString("uid");
//			}
//			if (!job.isNull("realname")) {
//				this.realname = job.getString("realname");
//			}
//			if (!job.isNull("imgurl")) {
//				this.imgurl = job.getString("imgurl");
//			}
//			if (!job.isNull("header")) {
//				this.header = job.getString("header");
//			}
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
	}

	@Override
	public JSONObject toJson() {
		JSONObject ob = new JSONObject();
		try {
			ob.put("content", content);
			ob.put("createdate", createdate);
			ob.put("replycount", replycount);
			ob.put("uid", uid);
			ob.put("realname", realname);
			ob.put("imgurl", imgurl);
			ob.put("drid", drid);
			ob.put("header", header);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return ob;
	}

	@Override
	public boolean equals(Object obj) {
		return this.drid.equals(((DynamicShow) obj).drid);
	}

}
