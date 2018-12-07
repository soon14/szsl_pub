package com.bsoft.hospital.pub.suzhoumh.model.moitor;

import org.json.JSONException;
import org.json.JSONObject;

import com.app.tanklib.model.AbsBaseVoSerializ;

public class MonitorSetting extends AbsBaseVoSerializ {

	public int id;
	public String name;
	public int flag;// 1为选择 默认为0
	public String unit;//单位
	
	public MonitorSetting()
	{
		
	}
	
	public MonitorSetting(String name, int flag, int id,String unit) {
		this.name = name;
		this.flag = flag;
		this.id = id;
		this.unit = unit;
	}

	@Override
	public void buideJson(JSONObject job) throws JSONException {
		
	}

	@Override
	public JSONObject toJson() {
		JSONObject ob = new JSONObject();
		try {
			ob.put("id", id);
			ob.put("name", name);
			ob.put("flag", flag);
			ob.put("unit", unit);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return ob;
	}

}
