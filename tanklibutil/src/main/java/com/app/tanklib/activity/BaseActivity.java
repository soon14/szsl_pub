package com.app.tanklib.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

import com.app.tanklib.R;
import com.app.tanklib.view.BsoftActionBar;

public abstract class BaseActivity extends AppCompatActivity {

	public BsoftActionBar actionBar;
	public Context baseContext;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.baseContext = this;
		IntentFilter filter = new IntentFilter();
		filter.addAction(getCloseAction());
		filter.addAction(getSSOLoginAction());
		this.registerReceiver(this.endbroadcastReceiver, filter);
	}

	// 写一个广播的内部类，当收到动作时，结束activity
	private BroadcastReceiver endbroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (getCloseAction().equals(intent.getAction())) {
				finish();
			}else if(getSSOLoginAction().equals(intent.getAction())){
				loginDiglog(intent);
			}
		}
	};

	public abstract int getActionBarBg();
	
	public abstract String getCloseAction();
	
	public abstract String getSSOLoginAction();
	
	public abstract void loginDiglog(Intent intent);

	public void findActionBar() {
		actionBar = (BsoftActionBar) findViewById(R.id.actionbar);
		actionBar.setBackGround(getResources().getColor(getActionBarBg()));
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (null != this.endbroadcastReceiver) {
			this.unregisterReceiver(this.endbroadcastReceiver);
			endbroadcastReceiver = null;
		}
	}

}
