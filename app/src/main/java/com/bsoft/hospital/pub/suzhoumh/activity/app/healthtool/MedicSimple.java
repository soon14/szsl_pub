package com.bsoft.hospital.pub.suzhoumh.activity.app.healthtool;

import org.json.JSONException;
import org.json.JSONObject;

import com.app.tanklib.model.AbsBaseVoSerializ;


public class MedicSimple extends AbsBaseVoSerializ{
	public String id;
	
	public String title;

	public String status;

	public String createdate;

	public String createuser;
	
	//详细内容
	
	//适应症
	public String healfun;
	
	//用法用量
	public String usagedosage;
	
	//不良反应
	public String adversereactions;
	
	//禁忌
	public String taboo;
	
	//批准文号
	public String approvaldoc;
	
	//功能主治
	public String indfun;
    
	@Override
	public void buideJson(JSONObject job) throws JSONException {
		// TODO Auto-generated method stub
		if(!job.isNull("id"))
		{
			this.id = job.getString("id");
		}
		if(!job.isNull("title"))
		{
			this.title = job.getString("title");
		}
		if(!job.isNull("status"))
		{
			this.status = job.getString("status");
		}
		if(!job.isNull("createdate"))
		{
			this.createdate = job.getString("createdate");
		}
		if(!job.isNull("createuser"))
		{
			this.createuser = job.getString("createuser");
		}
	}

	@Override
	public JSONObject toJson() {
		// TODO Auto-generated method stub
		return null;
	}

}