package com.bsoft.hospital.pub.suzhoumh.model.my;

import java.io.Serializable;

/**
 * 预约,查询报告的时候用到
 * @author Administrator
 *
 */
public class PersonVo implements Serializable{
	
	public String realname;//姓名
	public int sexcode;//性别 1男2女
	public String idcard;//身份证号
	public String mobile;//手机号码
	public String card;//就诊卡号
	public String nature;//病人性质
	public PersonType type;

	public enum PersonType
	{
		SELF,FAMILY;
	}
}
