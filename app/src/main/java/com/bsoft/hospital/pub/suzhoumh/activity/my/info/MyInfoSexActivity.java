package com.bsoft.hospital.pub.suzhoumh.activity.my.info;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.app.tanklib.view.BsoftActionBar.Action;

/**
 * @author Tank E-mail:zkljxq@126.com
 * 
 * @类说明
 */
public class MyInfoSexActivity extends BaseActivity {

	ImageView sex1, sex2;
	RelativeLayout layout1, layout2;
	int sex = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myinfo_sex);
		sex = getIntent().getIntExtra("sex", 0);
		findView();
		setClick();
	}

	void setClick() {
		actionBar.setTitle("性别");
		if (sex == 1) {
			sex1.setVisibility(View.VISIBLE);
		} else if (sex == 2) {
			sex2.setVisibility(View.VISIBLE);

		}
		layout1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.putExtra("sexcode", "1");
				setResult(RESULT_OK, intent);
				back();
			}
		});
		layout2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.putExtra("sexcode", "2");
				setResult(RESULT_OK,intent);
				back();
			}
		});
	}

	@Override
	public void findView() {
		findActionBar();
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
		sex1 = (ImageView) findViewById(R.id.sex1);
		sex2 = (ImageView) findViewById(R.id.sex2);
		layout1 = (RelativeLayout) findViewById(R.id.layout1);
		layout2 = (RelativeLayout) findViewById(R.id.layout2);
	}

}
