package com.bsoft.hospital.pub.suzhoumh.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.app.tanklib.view.BsoftActionBar.Action;

/**
 * @author Tank E-mail:zkljxq@126.com
 * 
 * @类说明
 */
public class SettingAccountActivity extends BaseActivity {

	RelativeLayout layout1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settingaccount);
		findView();
		setClick();
	}

	void setClick() {
		layout1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(baseContext,
						SettingPwdActivity.class);
				startActivity(intent);
			}
		});
	
	}

	@Override
	public void findView() {
		findActionBar();
		actionBar.setTitle("账号设置");
		actionBar.setBackAction(new Action() {
			@Override
			public void performAction(View view) {
				back();
			}

			@Override
			public int getDrawable() {
				return R.drawable.btn_back;
			}
		});
		layout1 = (RelativeLayout) findViewById(R.id.layout1);
	}

}
