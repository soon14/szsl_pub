package com.bsoft.hospital.pub.suzhoumh.model.my;

import org.json.JSONException;
import org.json.JSONObject;

import com.app.tanklib.model.AbsBaseVoSerializ;


/**
 * 
 * 
 * @author zkl
 * 
 * 
 *         "belong": 112083, "belongName": "112083", "cardNum": "25555",
 *         "cardType": "1"
 * 
 *         {"belong":18048,"belongName":"杭州市","cardNum":"123232323245","id":12}
 */

public class MyCardVo extends AbsBaseVoSerializ {
	private static final long serialVersionUID = 1L;

	public String id="";
	public String belong="";
	public String belongName="";
	public String cardNum="";
	public int cardType=0;

	@Override
	public void buideJson(JSONObject job) {
		try {
			if (!job.isNull("id")) {
				this.id = job.getString("id");
			}
			if (!job.isNull("belong")) {
				this.belong = job.getString("belong");
			}
			if (!job.isNull("belongName")) {
				this.belongName = job.getString("belongName");
			}
			if (!job.isNull("cardNum")) {
				this.cardNum = job.getString("cardNum");
			}
			if (!job.isNull("cardType")) {
				this.cardType = job.getInt("cardType");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public JSONObject toJson() {
		JSONObject ob = new JSONObject();
		try {
			ob.put("id", id);
			ob.put("belong", belong);
			ob.put("belongName", belongName);
			ob.put("cardNum", cardNum);
			ob.put("cardType", cardType);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return ob;
	}

	@Override
	public boolean equals(Object obj) {
		return this.id.equals(((MyCardVo) obj).id);
	}
}
