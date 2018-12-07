package com.app.tanklib.util;

/**
 * @author Tank E-mail:zkljxq@126.com
 * 
 * @类说明
 */
public class SystemUtils {


	/**
	 * 
	 * @Description: 检测当前的版本信息
	 * @param
	 * @return int
	 * @throws
	 */
	public static int getSystemVersion() {
		return android.os.Build.VERSION.SDK_INT;
	}
	
	
	/**
	 * 获取设备名称
	 * 
	 * @return
	 */
	public static String getDeviceName(){
		StringBuffer sb=new StringBuffer();
		if(!StringUtil.isEmpty(android.os.Build.BRAND)){
			sb.append(android.os.Build.BRAND);
		}
		if(!StringUtil.isEmpty(android.os.Build.MODEL)){
			sb.append(android.os.Build.MODEL);
		}
		return sb.toString();
	}
	
	
	/**
	 * 获取设备名称
	 * @return
	 */
	public static String getPhoneModel(){
		return android.os.Build.MODEL;
	}

}
