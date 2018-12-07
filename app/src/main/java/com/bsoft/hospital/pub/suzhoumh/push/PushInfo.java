package com.bsoft.hospital.pub.suzhoumh.push;

import org.json.JSONException;
import org.json.JSONObject;

import com.app.tanklib.model.AbsBaseVoSerializ;


/**
 * @author Tank E-mail:zkljxq@126.com
 * 
 * @类说明
 */
public class PushInfo extends AbsBaseVoSerializ {

	public String title;
	public String description;
	public String type;
	public int kinds;
	//是否强制退出
	public int login=0;

	@Override
	public void buideJson(JSONObject job) throws JSONException {
		try {
			if (!job.isNull("title")) {
				this.title = job.getString("title");
			}
			if (!job.isNull("description")) {
				this.description = job.getString("description");
			}
			if (!job.isNull("type")) {
				this.type = job.getString("type");
			}
			if (!job.isNull("kinds")) {
				this.kinds = job.getInt("kinds");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * case 1: return "系统管理"; case 2: return "预约挂号"; case 3: return "陪诊服务"; case
	 * 4: return "计免"; case 5: return "儿童"; case 6: return "孕妇"; case 7: return
	 * "慢病"; default: return "";
	 */
	public int getKinds() {
		if (type.startsWith("01")) {
			return 1;
		} else if (type.startsWith("02")) {
			return 2;
		} else if (type.startsWith("03")) {
			return 3;
		} else if (type.startsWith("04")) {
			return 4;
		} else if (type.startsWith("05")) {
			return 5;
		} else if (type.startsWith("06")) {
			return 6;
		} else if (type.startsWith("07")) {
			return 7;
		}
		return 0;
	}

	@Override
	public JSONObject toJson() {
		return null;
	}

}
