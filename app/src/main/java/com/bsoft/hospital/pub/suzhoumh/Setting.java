package com.bsoft.hospital.pub.suzhoumh;

import com.app.tanklib.util.StringUtil;

/**
 * 
 * 系统配置模块
 * 
 * @author Administrator
 * 
 */
public class Setting {

	//外网api地址
	public String apiurl;
	
	// 支付宝是否开启
	public int alipay_open;

	public String alipay_partner = "2088601171342292";
	public String alipay_seller = "alipay@zgjkw.cn";
	public String alipay_rsa_private = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAK2TOsTXY9oGQ0rZHX5+ULEHybEEKXCve3DJDy8iXKs/om9L2DgE+wjws33hOPflBx5QLyXH1VtNW9MOqoSK9u1ctwhTP6F8NgqFZsmNCvz0dw4DQUPVCcx+N6C7xR+Ef8vl5E1FyTcJKDVx1AGqymsb/c58ZYt+iFP5NBfwXRDXAgMBAAECgYBOKPkSwEbXW3Cb2K6eUjT1sqMQ0eXzNyHnxLHQsLF375yub0G64AtGmFTK/0sO3ZPaA5NneNfLWEdb/8OrEYRk2ZtPHApKsZK+Zoye0qhY1KW/FXlww2r828qLBlQ1u3cnbJpbxVLSijeWgucmAMn5PQuQJ+GLiWMLe7Q0Dg7acQJBAOTCwH6MCpFTFL7aD22CJXo+FUKVc+gIgY8B0GzCMg8gpgPQQco865xI/Uj8R9i86QZ5IR6eLSz9Y3EKe7yhuLkCQQDCPkUT9EVH4nLPhOqjd5Dqzo+jlIktCRAYeyk+O4MnB/n/8EjMYfjH4KCFXs0JHIqM6QrH2OnuJIJs+ojUQi4PAkA0smxXenkguwvHX0I4jseFXnHVUcpOhE8cG2Xg9+dCNtonfLz7EoeQC/xU6NDAK9xeQl8Px45Ok9xpeOnCLVr5AkEAlBlGMeCcsiJFx1NgUyJmq6Cyg9ZMgLnYO9Irr/dQGAJM6ocZ+jr4o+zwdr/BNKE+QhGs1pLcSWYsy0p56NxMoQJAC1J8pRwK8mbnj9kEry1o0lETmu6IAWT7dpkiNDF10ayVYOLqUySv7LwPv9cMCz17Ktp7xnk1jDFu6j4bLVhmrA==";

	/**
	 * 首页
	 */
	// 预约挂号
	public int pubappoint_open;
	// 计免预约
	public int implementappoint_open;
	// 儿保预约
	public int childrenappoint_open;
	// 分诊咨询
	public int reception_open;
	// 报告查询
	public int report_open;
	// 便捷寻医
	public int seek_open;

	/**
	 * 我的
	 */
	// 我的档案
	public int myrecord_open;
	// 家庭管理
	public int family_open;
	// 服务记录
	public int serverrecord_open;
	// 签约信息
	public int sign_open;
	// 健康监测
	public int monitor_open;
	
	//所属地区
	public String sys_provinceid="";
	public String sys_province="";
	public String sys_cityid="";
	public String sys_city="";
	public String sys_countyid="";
	public String sys_county="";
	
	//分诊咨询
	public String sys_servertime;
	public String sys_serverman;
	public String sys_servercontent;
	public String sys_serverpay;
	public String sys_serverphone;
	
	//条件-完善信息条件
	
	
	
	
	
	//得到所属地区的级别，1省级 2市级 3区级(主要用于预约挂号)
	public int getRegionLevel(){
		if(!StringUtil.isEmpty(sys_countyid)){
			return 3;
		}
		if(!StringUtil.isEmpty(sys_cityid)){
			return 2;
		}
		return 1;
	}
	
	public String getRegionName(){
		StringBuffer sb=new StringBuffer();
		if(!StringUtil.isEmpty(sys_province)){
			sb.append(sys_province);
		}
		if(!StringUtil.isEmpty(sys_city)){
			sb.append(sys_city);
		}
		if(!StringUtil.isEmpty(sys_county)){
			sb.append(sys_county);
		}
		return sb.toString();
	}

	//首页模块数
	public int appCount() {
		return pubappoint_open + implementappoint_open + childrenappoint_open
				+ reception_open + report_open + seek_open;
	}

	//支付宝是否开启
	public boolean isAlipayOpen() {
		return alipay_open == 1 && !StringUtil.isEmpty(alipay_partner)
				&& !StringUtil.isEmpty(alipay_seller)
				&& !StringUtil.isEmpty(alipay_rsa_private);
	}
}
