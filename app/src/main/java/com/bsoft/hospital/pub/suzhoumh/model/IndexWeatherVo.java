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

public class IndexWeatherVo extends AbsBaseVoSerializ {
	private static final long serialVersionUID = 1L;

	public String advice;
	public String currentCity;
	public String currentId;
	public String date;
	public String pm25;
	public String pm25Value;
	public String temperature;
	public String weather;
	public String wind;
	// 时间戳
	public long outtime;

	@Override
	public void buideJson(JSONObject job) {
		try {
			if (!job.isNull("advice")) {
				this.advice = job.getString("advice");
			}
			if (!job.isNull("currentCity")) {
				this.currentCity = job.getString("currentCity");
			}
			if (!job.isNull("currentId")) {
				this.currentId = job.getString("currentId");
			}
			if (!job.isNull("date")) {
				this.date = job.getString("date");
			}
			if (!job.isNull("pm25")) {
				this.pm25 = job.getString("pm25");
			}
			if (!job.isNull("pm25Value")) {
				this.pm25Value = job.getString("pm25Value");
			}
			if (!job.isNull("temperature")) {
				this.temperature = job.getString("temperature");
			}
			if (!job.isNull("weather")) {
				this.weather = job.getString("weather");
			}
			if (!job.isNull("wind")) {
				this.wind = job.getString("wind");
			}
			if (!job.isNull("outtime")) {
				this.outtime = job.getLong("outtime");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public JSONObject toJson() {
		JSONObject ob = new JSONObject();
		try {
			ob.put("advice", advice);
			ob.put("currentCity", currentCity);
			ob.put("currentId", currentId);
			ob.put("date", date);
			ob.put("pm25", pm25);
			ob.put("pm25Value", pm25Value);
			ob.put("temperature", temperature);
			ob.put("weather", weather);
			ob.put("wind", wind);
			ob.put("outtime", System.currentTimeMillis());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return ob;
	}

	//10分钟失效
	public boolean isNeedUpdate() {
		return ((System.currentTimeMillis() - outtime) / (1 * 60000)) > 1;
	}
	
	//30分钟城市失效
	public boolean isCityUpdate(){
		return ((System.currentTimeMillis() - outtime) / (3 * 60000)) > 1;
	}

}
