package com.bsoft.hospital.pub.suzhoumh.model;

import org.json.JSONException;
import org.json.JSONObject;

import com.app.tanklib.model.AbsBaseVoSerializ;

/**
 * 
 * 
 * @author zkl
 * 
 * 
 *         {"data":{"imageHeight":"200","imageUrl":
 *         "http://115.236.19.146:58080/upload/favicon/91/20150121134059319header.jpg"
 *         ,"imageWidth":"200"},"code":"0","msg":"头像上传成功"}
 */

public class UploadHeader extends AbsBaseVoSerializ {
	private static final long serialVersionUID = 1L;

	public String imageUrl;

	@Override
	public void buideJson(JSONObject job) {
		try {
			if (!job.isNull("imageUrl")) {
				this.imageUrl = job.getString("imageUrl");
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
