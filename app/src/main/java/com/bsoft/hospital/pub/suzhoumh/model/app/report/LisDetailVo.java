package com.bsoft.hospital.pub.suzhoumh.model.app.report;

import org.json.JSONException;
import org.json.JSONObject;

import com.app.tanklib.model.AbsBaseVoSerializ;

/**
 * 检验报告详情
 * @author Administrator
 *
 */
public class LisDetailVo extends AbsBaseVoSerializ {

	/*public String sqdh;
	public String xmmc;
	public String xmz;
	public String ckfw;
	public String dw;
	public String ycbz;*/

	public String OBSERVATIONSUB_NAME;
	public String OBSERVATIONVALUE;
	public String UNITS;
	public String TOPVALUE;
	public String BOTTOMVALUE;
	public int SFYC;
	public String YCBS;
	
	@Override
	public void buideJson(JSONObject job) throws JSONException {
		// TODO Auto-generated method stub

	}

	@Override
	public JSONObject toJson() {
		// TODO Auto-generated method stub
		return null;
	}

}
