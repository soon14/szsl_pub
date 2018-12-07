package com.bsoft.hospital.pub.suzhoumh.model;

import org.json.JSONException;
import org.json.JSONObject;

import com.app.tanklib.model.AbsBaseVoSerializ;

/**
 * 
 * 活动
 * 
 * @author zkl
 * 
 */

public class ActivityVo extends AbsBaseVoSerializ {
	private static final long serialVersionUID = 1L;

	 public Long id;
	 public Long uid;
	 public String title;
	 public String des;
	 public String content;
	 public String pay;
	 public String imgurl;
	 public String address;
	 public double longitude;
	 public double latitude;
	 public String linkman;
	 public String linkphone;
	 public long stime;
	 public long etime;
	 public long createdate;
	    
	@Override
	public void buideJson(JSONObject job) {
		try {
			if (!job.isNull("id")) {
				this.id = job.getLong("id");
			}
			if (!job.isNull("uid")) {
				this.uid = job.getLong("uid");
			}
			if (!job.isNull("title")) {
				this.title = job.getString("title");
			}
			if (!job.isNull("des")) {
				this.des = job.getString("des");
			}
			if (!job.isNull("content")) {
				this.content = job.getString("content");
			}
			if (!job.isNull("pay")) {
				this.pay = job.getString("pay");
			}
			if (!job.isNull("imgurl")) {
				this.imgurl = job.getString("imgurl");
			}
			if (!job.isNull("address")) {
				this.address = job.getString("address");
			}
			if (!job.isNull("longitude")) {
				this.longitude = job.getDouble("longitude");
			}
			if (!job.isNull("latitude")) {
				this.latitude = job.getDouble("latitude");
			}
			if (!job.isNull("linkman")) {
				this.linkman = job.getString("linkman");
			}
			if (!job.isNull("linkphone")) {
				this.linkphone = job.getString("linkphone");
			}
			if (!job.isNull("stime")) {
				this.stime = job.getLong("stime");
			}
			if (!job.isNull("etime")) {
				this.etime = job.getLong("etime");
			}
			if (!job.isNull("createdate")) {
				this.createdate = job.getLong("createdate");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public JSONObject toJson() {
		return null;
	}

}
