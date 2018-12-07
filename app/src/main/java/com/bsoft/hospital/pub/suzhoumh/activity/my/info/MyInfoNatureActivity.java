package com.bsoft.hospital.pub.suzhoumh.activity.my.info;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.tanklib.view.BsoftActionBar.Action;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;

/**
 * 病人性质
 * 0自费 1苏州医保 2园区医保 3吴江医保
 */
public class MyInfoNatureActivity extends BaseActivity{

	ImageView iv1, iv2, iv3,iv4;
	RelativeLayout layout1, layout2, layout3,layout4;
	public final static String ZF = "0";
	public final static String SZYB = "1";
	public final static String YQYB = "2";
	public final static String WJYB = "3";
	String nature;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myinfo_nature);
		nature = getIntent().getStringExtra("nature");
		findView();
		setClick();
	}

	void setClick() {
		actionBar.setTitle("病人性质");
		Log.d("test", nature + " nature");
		if(nature!=null)
		{
			if (nature.equals(ZF)) {
				iv1.setVisibility(View.VISIBLE);
				iv2.setVisibility(View.GONE);
				iv3.setVisibility(View.GONE);
				iv4.setVisibility(View.GONE);
			} else if (nature.equals(YQYB)) {
				iv1.setVisibility(View.GONE);
				iv2.setVisibility(View.VISIBLE);
				iv3.setVisibility(View.GONE);
				iv4.setVisibility(View.GONE);
			} else if (nature.equals(SZYB)) {
				iv1.setVisibility(View.GONE);
				iv2.setVisibility(View.GONE);
				iv3.setVisibility(View.VISIBLE);
				iv4.setVisibility(View.GONE);
			}else if (nature.equals(WJYB)){
				iv1.setVisibility(View.GONE);
				iv2.setVisibility(View.GONE);
				iv3.setVisibility(View.GONE);
				iv4.setVisibility(View.VISIBLE);
			}
		}
		layout1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.putExtra("nature", ZF);
				setResult(RESULT_OK, intent);
				back();
			}
		});
		layout2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.putExtra("nature", YQYB);
				setResult(RESULT_OK,intent);
				back();
			}
		});
		layout3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.putExtra("nature", SZYB);
				setResult(RESULT_OK,intent);
				back();
			}
		});
		layout4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.putExtra("nature", WJYB);
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
		iv1 = (ImageView) findViewById(R.id.iv1);
		iv2 = (ImageView) findViewById(R.id.iv2);
		iv3 = (ImageView) findViewById(R.id.iv3);
		iv4 = (ImageView) findViewById(R.id.iv4);
		layout1 = (RelativeLayout) findViewById(R.id.layout1);
		layout2 = (RelativeLayout) findViewById(R.id.layout2);
		layout3 = (RelativeLayout) findViewById(R.id.layout3);
		layout4 = (RelativeLayout) findViewById(R.id.layout4);
	}


}
