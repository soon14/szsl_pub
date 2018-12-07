package com.bsoft.hospital.pub.suzhoumh.activity.my.info;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.tanklib.http.BsoftNameValuePair;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.NullModel;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.util.IDCard;
import com.app.tanklib.view.BsoftActionBar.Action;
import com.app.tanklib.util.StringUtil;

/**
 * @author Tank E-mail:zkljxq@126.com
 * 
 * @类说明
 */
public class MyInfoSettingActivity extends BaseActivity {

	View mainView;
	EditText edit;
	ImageView clear;
	int index = 0;
	String value;

	GetTask task;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myinfo_setting);
		index = getIntent().getIntExtra("index", 0);
		value = getIntent().getStringExtra("value");
		findView();
		setClick();
		edit.requestFocus();
	}

	void setClick() {
		switch (index) {
		case 1:
			actionBar.setTitle("真实姓名");
			edit.setHint("请输入真实姓名");
			break;
		case 2:
			actionBar.setTitle("身份证号");
			edit.setHint("请输入身份证号");
			break;
		default:
			break;
		}
		if (!StringUtil.isEmpty(value)) {
			edit.setText(value + "");
		}
		edit.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable arg0) {
				if (edit.getText().toString().length() == 0) {
					clear.setVisibility(View.INVISIBLE);
				} else {
					clear.setVisibility(View.VISIBLE);
				}
			}
		});
		clear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				edit.setText("");
			}
		});
		mainView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
						.hideSoftInputFromWindow(edit.getWindowToken(), 0);
				return false;
			}
		});
		actionBar.setRefreshTextView("保存", new Action() {

			@Override
			public void performAction(View view) {
				switch (index) {
				case 1:
					if (StringUtil.isEmpty(edit.getText().toString())) {
						edit.requestFocus();
						Toast.makeText(baseContext, "真实姓名为空，请输入",
								Toast.LENGTH_SHORT).show();
						return;
					}
					if (edit.getText().toString().length() > 6) {
						edit.requestFocus();
						Toast.makeText(baseContext, "真实姓名的长度不能超过6，请修改",
								Toast.LENGTH_SHORT).show();
						return;
					}
					break;
				case 2:
					if (StringUtil.isEmpty(edit.getText().toString())) {
						edit.requestFocus();
						Toast.makeText(baseContext, "身份证号为空，请输入",
								Toast.LENGTH_SHORT).show();
						return;
					}
					String msg = IDCard.IDCardValidate(edit.getText()
							.toString());
					if (!StringUtil.isEmpty(msg)) {
						edit.requestFocus();
						Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT)
								.show();
						return;
					}
					break;
				default:
					break;
				}
				task = new GetTask();
				task.execute();
			}

			@Override
			public int getDrawable() {
				return 0;
			}
		});
		// actionBar.addAction(new Action() {
		//
		// @Override
		// public void performAction(View view) {
		// switch (index) {
		// case 1:
		// if (StringUtil.isEmpty(edit.getText().toString())) {
		// edit.requestFocus();
		// Toast.makeText(baseContext, "真实姓名为空，请输入",
		// Toast.LENGTH_SHORT).show();
		// return;
		// }
		// break;
		// case 2:
		// if (StringUtil.isEmpty(edit.getText().toString())) {
		// edit.requestFocus();
		// Toast.makeText(baseContext, "身份证号为空，请输入",
		// Toast.LENGTH_SHORT).show();
		// return;
		// }
		// String msg = IDCard.IDCardValidate(edit.getText()
		// .toString());
		// if (!StringUtil.isEmpty(msg)) {
		// edit.requestFocus();
		// Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT)
		// .show();
		// return;
		// }
		// break;
		// default:
		// break;
		// }
		// task = new GetTask();
		// task.execute();
		// }
		//
		// @Override
		// public int getDrawable() {
		// return R.drawable.btn_send;
		// }
		// });
	}

	@Override
	public void findView() {
		findActionBar();
		actionBar.setBackAction(new Action() {
			@Override
			public void performAction(View view) {
				((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
						.hideSoftInputFromWindow(edit.getWindowToken(), 0);
				back();
			}

			@Override
			public int getDrawable() {
				return R.drawable.btn_back;
			}
		});
		mainView = findViewById(R.id.mainView);
		edit = (EditText) findViewById(R.id.edit);
		clear = (ImageView) findViewById(R.id.clear);
	}

	class GetTask extends AsyncTask<Void, Object, ResultModel<NullModel>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			actionBar.startTextRefresh();
		}

		@Override
		protected ResultModel<NullModel> doInBackground(Void... params) {
			switch (index) {
			case 1:
				return HttpApi.getInstance().parserData(
						NullModel.class,
						"auth/ainfo/modify/name",
						new BsoftNameValuePair("id", loginUser.id),
						new BsoftNameValuePair("name", edit.getText()
								.toString()),
						new BsoftNameValuePair("sn", loginUser.sn));
			case 2:
				return HttpApi.getInstance().parserData(
						NullModel.class,
						"auth/ainfo/modify/idcard",
						new BsoftNameValuePair("id", loginUser.id),
						new BsoftNameValuePair("cardnum", edit.getText()
								.toString()),
						new BsoftNameValuePair("sn", loginUser.sn));
			default:
				return null;
			}
		}

		@Override
		protected void onPostExecute(ResultModel<NullModel> result) {
			actionBar.endTextRefresh();
			if (null != result) {
				if (result.statue == Statue.SUCCESS) {
					switch (index) {
					case 1:
						Toast.makeText(baseContext, "真实姓名保存成功",
								Toast.LENGTH_SHORT).show();
						break;
					case 2:
						Toast.makeText(baseContext, "身份证号保存成功",
								Toast.LENGTH_SHORT).show();
						break;
					default:
						break;
					}

					Intent intent = new Intent(Constants.MyInfo_ACTION);
					intent.putExtra("index", index);
					intent.putExtra("value", edit.getText().toString());
					sendBroadcast(intent);
					((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
							.hideSoftInputFromWindow(edit.getWindowToken(), 0);
					back();
				} else {
					result.showToast(baseContext);
				}
			}
		}
	}

}
