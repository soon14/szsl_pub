package com.bsoft.hospital.pub.suzhoumh.util;

import android.content.Intent;

/** 解决子Activity无法接收Activity回调的问题 
 * */
public interface IOnTabActivityResultListener {    
	public void onTabActivityResult(int requestCode, int resultCode, Intent data);
}
