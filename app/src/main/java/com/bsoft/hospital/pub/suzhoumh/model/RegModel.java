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

public class RegModel extends AbsBaseVoSerializ {
	private static final long serialVersionUID = 1L;

	public LoginUser loginUser;
	public int code = 0;
	public String msg="注册失败";

	@Override
	public void buideJson(JSONObject job) {
		try {
			if (!job.isNull("code")) {
				this.code = job.getInt("code");
			}
			if (code != 200) {
				if (!job.isNull("msg")) {
					this.msg=job.getString("msg");
				}
				return;
			}else{
				loginUser=new LoginUser();
				loginUser.buideJson(job);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public JSONObject toJson() {
		return null;
	}

	// @Override
	// public boolean equals(Object obj) {
	// return this.showId == ((DynamicShow) obj).showId;
	// }

}
