package com.bsoft.hospital.pub.suzhoumh.model;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import com.app.tanklib.model.AbsBaseVoSerializ;

public class FeeVo extends AbsBaseVoSerializ{

	public String id;// 编号,
	public String fyrq;// 费用日期,
	public String xmlx;// 项目类型，
	public String ysxm;//医生姓名，
	public String ksmc;// 科室名称，
	public String hjje;// 合计金额,
	public String sbxh;// 识别序号，
	public String cfyj;// 处方医技（1，处方；2检查单）
	public String mzhm;// 门诊号码,
	public String brxm;// 病人姓名,
	public String brxz;// 病人性质  （6000 园区医保）
	public String brid;// 病人编号,
	public String sfzh;// 身份证号,
	public String sfgh;// 是否挂号（0，否；1，是。是挂号时需要默认勾选并且不能取消勾选）
	public boolean isSelected = false;
//有可能多条记录中的brid 不一致时不能结算）


@Override
	public void buideJson(JSONObject job) throws JSONException {

	}

	@Override
	public JSONObject toJson() {
		return null;
	}

	public String xzmc(){
		String xzmc = "";
		if(TextUtils.equals("1000", brxz)){
			xzmc = "自费";
		}else if(TextUtils.equals("2000", brxz)){
			xzmc = "公费";
		}else if(TextUtils.equals("3000", brxz)){
			xzmc = "参保";
		}else if(TextUtils.equals("5000", brxz)){
			xzmc = "记账";
		}else if(TextUtils.equals("5001", brxz)){
			xzmc = "本院职工";
		}else if(TextUtils.equals("6000", brxz)){
			xzmc = "园区医保";
		}else if(TextUtils.equals("7000", brxz)){
			xzmc = "医疗保险";
		}else if(TextUtils.equals("7001", brxz)){
			xzmc = "苏州离休";
		}else if(TextUtils.equals("8000", brxz)){
			xzmc = "吴中医保";
		}else if(TextUtils.equals("8001", brxz)){
			xzmc = "相城医保";
		}else if(TextUtils.equals("8003", brxz)){
			xzmc = "吴江医保";
		}

		return xzmc;
	}

}
