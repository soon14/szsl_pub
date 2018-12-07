package com.bsoft.hospital.pub.suzhoumh.model.app.appoint;

import org.json.JSONException;
import org.json.JSONObject;

import com.app.tanklib.model.AbsBaseVoSerializ;

/**
 * 预约日期
 * @author Administrator
 *
 */
public class AppointDateVo extends AbsBaseVoSerializ {

	public String zblb;
	public String gzrq;
	public String dqrq;
	public String ygdm;
	public String ksdm;
	public String zt;
	
	public String ghrq;//普通预约的时候用到
	public String dayofweek;//当前是周几，普通预约的时候用到
	
	@Override
	public void buideJson(JSONObject job) throws JSONException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public JSONObject toJson() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		return "AppointDateVo{" +
				"zblb='" + zblb + '\'' +
				", gzrq='" + gzrq + '\'' +
				", dqrq='" + dqrq + '\'' +
				", ygdm='" + ygdm + '\'' +
				", ksdm='" + ksdm + '\'' +
				", zt='" + zt + '\'' +
				", ghrq='" + ghrq + '\'' +
				", dayofweek='" + dayofweek + '\'' +
				'}';
	}
}
