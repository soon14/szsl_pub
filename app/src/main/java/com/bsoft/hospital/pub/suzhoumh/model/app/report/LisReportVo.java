package com.bsoft.hospital.pub.suzhoumh.model.app.report;

import org.json.JSONException;
import org.json.JSONObject;

import com.app.tanklib.model.AbsBaseVoSerializ;

/**
 * 检验报告
 * @author Administrator
 *
 */
public class LisReportVo extends AbsBaseVoSerializ{

	/*public String mzhm;
	public String sqdh;
	public String sqks;
	public String jyxm;
	public String jysj;
	public String wcbz;*/
	
	public String JYMD;//检验项目
	public String LRCHECKTIME;
	public String TITLE;
	public String OBSERVATIONOPTDEPTNAME;//科室名称
	public String OBSERVATIONDATETIME;//检验日期
	public String ASSAYRECORDGUID;//检验唯一号
	public String OBSERVATIONOPTNAME;//检验医生
	public String FPHM;//发票号码
	
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
