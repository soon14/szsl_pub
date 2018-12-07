package com.bsoft.hospital.pub.suzhoumh.activity.app.healthtool;

import org.json.JSONException;
import org.json.JSONObject;

import com.app.tanklib.model.AbsBaseVoSerializ;


public class MedicineRemindModel extends AbsBaseVoSerializ{
	
	public String id;//编号
	public String username;//提醒用户名称
	public String medname;//药品名字
	public String drugrepeat;//提醒间隔时间
	public String times;//提醒次数
	public String timesde;//提醒时间
	public String begindate;//开始提醒时间
	public String days;//提醒天数
	public String isremind;//是否提醒0:不提醒，1提醒
	public String uid;//用户uid
	public String updatetime;//更新时间
	public String createtime;//创建时间
	
	@Override
	public void buideJson(JSONObject job) throws JSONException {
		// TODO Auto-generated method stub
		if(!job.isNull("id"))
		{
			this.id = job.getString("id");
		}
		if(!job.isNull("username"))
		{
			this.username = job.getString("username");
		}
		if(!job.isNull("medname"))
		{
			this.medname = job.getString("medname");
		}
		if(!job.isNull("drugrepeat"))
		{
			this.drugrepeat = job.getString("drugrepeat");
		}
		if(!job.isNull("times"))
		{
			this.times = job.getString("times");
		}
		if(!job.isNull("timesde"))
		{
			this.timesde = job.getString("timesde");
		}
		if(!job.isNull("begindate"))
		{
			this.begindate = job.getString("begindate");
		}
		if(!job.isNull("days"))
		{
			this.days = job.getString("days");
		}
		if(!job.isNull("isremind"))
		{
			this.isremind = job.getString("isremind");
		}
		if(!job.isNull("uid"))
		{
			this.uid = job.getString("uid");
		}
		if(!job.isNull("updatetime"))
		{
			this.updatetime = job.getString("updatetime");
		}
		if(!job.isNull("createtime"))
		{
			this.createtime = job.getString("createtime");
		}
	}
	@Override
	public JSONObject toJson() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/*public static List<String> alltime;//选择的
	public static int times=1;
	public static List<String> getAlltime() {
		return alltime;
	}
	public static void setAlltime(List<String> alltime) {
		MedicineWarnModel.alltime = alltime;
	}
	public static int getTimes() {
		return times;
	}
	public static void setTimes(int times) {
		MedicineWarnModel.times = times;
	}*/
	
}
