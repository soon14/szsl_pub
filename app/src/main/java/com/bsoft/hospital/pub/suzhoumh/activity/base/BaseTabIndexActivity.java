package com.bsoft.hospital.pub.suzhoumh.activity.base;

import com.bsoft.hospital.pub.suzhoumh.Constants;

import android.content.Intent;
import android.view.KeyEvent;

/**
 * Activity 基类
 * 
 * @author zkl
 * 
 */
public abstract class BaseTabIndexActivity extends BaseActivity {


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			sendBroadcast(new Intent(Constants.BACK_ACTION));
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}


}
