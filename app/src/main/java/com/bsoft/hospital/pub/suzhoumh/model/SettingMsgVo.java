package com.bsoft.hospital.pub.suzhoumh.model;


import org.json.JSONException;
import org.json.JSONObject;

import com.app.tanklib.model.AbsBaseVoSerializ;

/**
 * 
 * 
 * @author zkl
 * 
 */

public class SettingMsgVo extends AbsBaseVoSerializ {
	private static final long serialVersionUID = 1L;
	// 新消息通知
	public int msg = 1;
	
	// 预约挂号
	//public int pubappoint = 1;
	// 计划通知
	//public int plan = 1;
	//声音
	public int sound=1;
	//震动
	public int shock=1;

	@Override
	public void buideJson(JSONObject job) {
//		try {
//			if (!job.isNull("ownnotice")) {
//				this.ownnotice = job.getInt("ownnotice");
//			}
//			if (!job.isNull("fmyregister")) {
//				this.fmyregister = job.getInt("fmyregister");
//			}
//			if (!job.isNull("fmytreat")) {
//				this.fmytreat = job.getInt("fmytreat");
//			}
//			if (!job.isNull("fmyplan")) {
//				this.fmyplan = job.getInt("fmyplan");
//			}
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
	}

	@Override
	public JSONObject toJson() {
		JSONObject ob = new JSONObject();
		try {
			ob.put("msg", msg);
			//ob.put("pubappoint", pubappoint);
			//ob.put("plan", plan);
			ob.put("sound", sound);
			ob.put("shock", shock);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return ob;
	}

	// @Override
	// public boolean equals(Object obj) {
	// return this.showId == ((DynamicShow) obj).showId;
	// }

}
