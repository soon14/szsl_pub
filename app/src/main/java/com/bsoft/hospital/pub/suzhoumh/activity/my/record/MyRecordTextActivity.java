package com.bsoft.hospital.pub.suzhoumh.activity.my.record;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.app.tanklib.view.BsoftActionBar.Action;
import com.app.tanklib.util.StringUtil;

/**
 * @author Tank E-mail:zkljxq@126.com
 * 
 * @类说明
 */
public class MyRecordTextActivity extends BaseActivity {

	View mainView;
	EditText edit;

	String value;

	int type = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dynamic_comment);
		type = getIntent().getIntExtra("type", 0);
		value = getIntent().getStringExtra("value");
		if (type == 0) {
			Toast.makeText(baseContext, "加载失败", Toast.LENGTH_SHORT).show();
			return;
		}
		findView();
		setClick();
	}

	void setClick() {
		mainView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);
				return false;
			}
		});
	}

	@Override
	public void findView() {
		findActionBar();
		actionBar.setTitle(type == 1 ? "医生诊断" : "医生建议");
		actionBar.setBackAction(new Action() {
			@Override
			public void performAction(View view) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);
				back();
			}

			@Override
			public int getDrawable() {
				return R.drawable.btn_back;
			}
		});
		actionBar.setRefreshTextView("确认", new Action() {

			@Override
			public void performAction(View view) {
				if (StringUtil.isEmpty(edit.getText().toString().trim())) {
					Toast.makeText(baseContext,
							type == 1 ? "医生诊断不能为空" : "医生建议不能为空",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (type == 1) {
					if (edit.getText().toString().length() > 50) {
						Toast.makeText(baseContext, "医生诊断不多于50字",
								Toast.LENGTH_SHORT).show();
						return;
					}
				} else {
					if (edit.getText().toString().length() > 200) {
						Toast.makeText(baseContext, "医生建议不多于200字",
								Toast.LENGTH_SHORT).show();
						return;
					}
				}
				getIntent().putExtra("data", edit.getText().toString());
				setResult(RESULT_OK, getIntent());
				finish();
			}

			@Override
			public int getDrawable() {
				return 0;
			}
		});
		mainView = findViewById(R.id.mainView);
		edit = (EditText) findViewById(R.id.edit);
		if(StringUtil.isEmpty(value)){
			edit.setHint(type == 1 ? "医生诊断(最多50字)" : "医生建议(最多200字)");
		}else{
			edit.setText(value);
			edit.setSelection(value.length());
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
