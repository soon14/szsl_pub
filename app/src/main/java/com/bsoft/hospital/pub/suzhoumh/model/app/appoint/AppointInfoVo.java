package com.bsoft.hospital.pub.suzhoumh.model.app.appoint;

import org.json.JSONException;
import org.json.JSONObject;

import com.app.tanklib.model.AbsBaseVoSerializ;

/**
 * 预约信息
 * @author Administrator
 *
 */
public class AppointInfoVo extends AbsBaseVoSerializ{

	/**
	 * 科室ID
	 */
	public String ksid;
	/**
	 * 科室名称
	 */
	public String ksmc;
	/**
	 * 医生Id
	 */
	public String ysid;
	/**
	 * 医生姓名
	 */
	public String ysxm;
	/**
	 * 预约日期
	 */
	public String yyrq;
	/**
	 * 时间段
	 */
	public String sjd;
	/**
	 * 就诊序号
	 */
	public String jzxh;
	/**
	 * 预约状态
	 */
	public String yyzt;
	/**
	 * 登记时间
	 */
	public String djsj;
	/**
	 * 预约类型
	 */
	public String yylx;
	/**
	 * 当前日期
	 */
	public String dqrq;
	
	//坐班类别
	public String zblb;//1上午2下午

	public String id;

	public String jzwz;//就诊位置
	public String brxz; //0 自费 1 医保
	@Override
	public void buideJson(JSONObject job) throws JSONException {

	}

	@Override
	public JSONObject toJson() {
		return null;
	}

}
