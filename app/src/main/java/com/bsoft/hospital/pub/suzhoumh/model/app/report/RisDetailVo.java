package com.bsoft.hospital.pub.suzhoumh.model.app.report;

import com.app.tanklib.model.AbsBaseVoSerializ;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 检查报告详情
 * @author Administrator
 *
 */
public class RisDetailVo extends AbsBaseVoSerializ{

	public String sqdh;
	public String xmmc;
	public String jczd;
	public String jcms;
	/**
	 * tpdz : 201702/201700716_001.jpg
	 */

	public List<DataTpdzBean> data_tpdz;


	public static class DataTpdzBean {
		public String tpdz;

	}

	@Override
	public void buideJson(JSONObject job) throws JSONException {

	}

	@Override
	public JSONObject toJson() {
		return null;
	}
}
