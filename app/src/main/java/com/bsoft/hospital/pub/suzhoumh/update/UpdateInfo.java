package com.bsoft.hospital.pub.suzhoumh.update;

import org.json.JSONException;
import org.json.JSONObject;

import com.app.tanklib.model.AbsBaseVoSerializ;


/**
 * @author Tank E-mail:zkljxq@126.com
 * @类说明
 * 
 *      {"code":0,"data":{"descr":"我的程序我做主","force":false,"newUrl":
 *      "http://ecard.zgjkw.cn/upload/app/ecard_android_5.apk"},"msg":"软件有更新"}
 */
public class UpdateInfo extends AbsBaseVoSerializ {

	public String des;
	public int appversion;
	//是否强制
	public String type;
	public String appurl;

	@Override
	public void buideJson(JSONObject object) throws JSONException {
	}

	@Override
	public JSONObject toJson() {
		return null;
	}

}
