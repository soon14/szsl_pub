package com.bsoft.hospital.pub.suzhoumh.model;

import org.json.JSONObject;

import com.app.tanklib.model.AbsBaseVoSerializ;

/**
 * 
 * 
 * @author zkl
 * 
 */

public class Message extends AbsBaseVoSerializ {
	private static final long serialVersionUID = 1L;

	//未读数
	public int count;
	public String content;
	public long date;
	public int kinds;
	//根据kinds得到
	public String name;


	@Override
	public void buideJson(JSONObject job) {
	}
	
	public String getName(){
		switch (this.kinds) {
		case 1:
			return "系统管理";
		case 2:
			return "预约挂号";
		case 3:
			return "陪诊服务";
		case 4:
			return "计免";
		case 5:
			return "儿童";
		case 6:
			return "孕妇";
		case 7:
			return "慢病";
		case 11:
			return "留言板";
		default:
			return "";
		}
	}

	@Override
	public JSONObject toJson() {
		return null;
	}

}
