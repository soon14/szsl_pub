package com.bsoft.hospital.pub.suzhoumh.activity.app.healthtool;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.tanklib.view.BsoftActionBar.Action;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.app.monitor.MonitorActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;

/**
 * 健康工具
 * @author Administrator
 *
 */
public class HealthToolActivity extends BaseActivity implements OnClickListener{

	private RelativeLayout rl_yytx;
	private RelativeLayout rl_yycx;
	private RelativeLayout rl_jkjc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.healthtool);
		
		findView();
		initData();
	}

	@Override
	public void findView() {
		// TODO Auto-generated method stub
		findActionBar();
		actionBar.setTitle("健康工具");
		actionBar.setBackAction(new Action() {

			@Override
			public int getDrawable() {
				// TODO Auto-generated method stub
				return R.drawable.btn_back;
			}

			@Override
			public void performAction(View view) {
				// TODO Auto-generated method stub
				finish();
			}

		});

		rl_yytx = (RelativeLayout) findViewById(R.id.rl_yytx);
		rl_yycx = (RelativeLayout) findViewById(R.id.rl_ypcx);
		rl_jkjc = (RelativeLayout) findViewById(R.id.rl_jkjc);
	}
	
	private void initData()
	{
		rl_yytx.setOnClickListener(this);
		rl_yycx.setOnClickListener(this);
		rl_jkjc.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent;
		switch(v.getId())
		{
			case R.id.rl_yytx://用药提醒
				intent = new Intent(HealthToolActivity.this,MedicineRemindActivity.class);
				startActivity(intent);
				break;
			case R.id.rl_ypcx://药品查询
				intent = new Intent(HealthToolActivity.this,MedicineSearchActivity.class);
				startActivity(intent);
				break;
			case R.id.rl_jkjc:// 健康监测
				if (loginUser != null && loginUser.idcard != null) {
					intent = new Intent(baseContext, MonitorActivity.class);
					startActivity(intent);
				} else {
					Toast.makeText(baseContext, "请先完善个人信息", Toast.LENGTH_SHORT)
							.show();
				}
				break;
		}
	}
	
	

}
