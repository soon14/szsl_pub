package com.bsoft.hospital.pub.suzhoumh.activity.app.queue;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.app.tanklib.view.BsoftActionBar.Action;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;

/**
 * 排队叫号主界面
 * @author Administrator
 *
 */
public class QueueMainActivity extends BaseActivity implements OnClickListener{

	private RelativeLayout rl_my_queue;
	private RelativeLayout rl_all_queue;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.queue_main);
		findView();
		initData();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent;
		switch(v.getId())
		{
		case R.id.rl_my_queue:
			intent = new Intent(QueueMainActivity.this,MyQueueActivity.class);
			startActivity(intent);
			break;
		case R.id.rl_all_queue:
			intent = new Intent(QueueMainActivity.this,AllQueueActivity.class);
			startActivity(intent);
			break;
		}
	}

	@Override
	public void findView() {
		// TODO Auto-generated method stub
		findActionBar();
		actionBar.setTitle("排队叫号");
		actionBar.setBackAction(new Action(){

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
		
		rl_my_queue = (RelativeLayout)findViewById(R.id.rl_my_queue);
		rl_all_queue = (RelativeLayout)findViewById(R.id.rl_all_queue);
	}
	
	private void initData()
	{
		rl_my_queue.setOnClickListener(this);
		rl_all_queue.setOnClickListener(this);
	}
}
