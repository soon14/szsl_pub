package com.bsoft.hospital.pub.suzhoumh.activity.my;

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
import com.app.tanklib.util.AsyncTaskUtil;
import com.app.tanklib.util.MD5;
import com.app.tanklib.util.StringUtil;
import com.app.tanklib.view.BsoftActionBar.Action;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.NullModel;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.view.AppProgressDialog;

/**
 * @author Tank E-mail:zkljxq@126.com
 * 
 * @类说明
 */
public class SettingPwdActivity extends BaseActivity {
	AppProgressDialog progressDialog;
	private EditText old, newpwd,second_newpwd;
	private ImageView oldclear, newclear,second_newclear;
	View mainView;
	GetTask task;


	String regex = "^(?![a-zA-z]+$)(?!\\d+$)(?![!@#$%^&*]+$)[a-zA-Z\\d!@#$%^&*]+$";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settingpwd);
		findView();
		setClick();
	}

	void setClick() {
		old.addTextChangedListener(new TextWatcher() {
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
				if (old.getText().toString().length() == 0) {
					oldclear.setVisibility(View.INVISIBLE);
				} else {
					oldclear.setVisibility(View.VISIBLE);
				}
			}
		});
		oldclear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				old.setText("");
			}
		});
		newpwd.addTextChangedListener(new TextWatcher() {
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
				if (newpwd.getText().toString().length() == 0) {
					newclear.setVisibility(View.INVISIBLE);
				} else {
					newclear.setVisibility(View.VISIBLE);
				}
			}
		});
		second_newpwd.addTextChangedListener(new TextWatcher(){

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (second_newpwd.getText().toString().length() == 0) {
					second_newclear.setVisibility(View.INVISIBLE);
				} else {
					second_newclear.setVisibility(View.VISIBLE);
				}
			}
			
		});
		newclear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				newpwd.setText("");
			}
		});
		second_newclear.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				second_newpwd.setText("");
			}
			
		});
		findViewById(R.id.submit).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (StringUtil.isEmpty(old.getText().toString())) {
					old.requestFocus();
					Toast.makeText(SettingPwdActivity.this, "原密码为空，请输入",
							Toast.LENGTH_SHORT).show();
				} else if (StringUtil.isEmpty(newpwd.getText().toString())) {
					newpwd.requestFocus();
					Toast.makeText(SettingPwdActivity.this, "新密码为空，请输入",
							Toast.LENGTH_SHORT).show();
				} else if (newpwd.getText().toString().trim().length() < 8
						|| newpwd.getText().toString().trim().length() > 20
						|| !newpwd.getText().toString().trim().matches(regex)) {
					newpwd.requestFocus();
					Toast.makeText(SettingPwdActivity.this, "密码8到20位，且必须包含数字和字母",
							Toast.LENGTH_SHORT).show();
				} else if (old.getText().toString()
						.equals(newpwd.getText().toString())) {
					old.requestFocus();
					Toast.makeText(SettingPwdActivity.this, "原密码不能与新密码相同，请输入",
							Toast.LENGTH_SHORT).show();
				} else if (!newpwd.getText().toString().equals(second_newpwd.getText().toString())) {
					second_newpwd.requestFocus();
					Toast.makeText(SettingPwdActivity.this, "两次密码输入不一样",Toast.LENGTH_SHORT).show();
				}
				else {
					task = new GetTask();
					task.execute();
				}
			}
		});
		mainView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				if (null != getCurrentFocus()
						&& null != getCurrentFocus().getWindowToken()) {
					((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
							.hideSoftInputFromWindow(getCurrentFocus()
									.getWindowToken(), 0);
				}
				return false;
			}
		});
	}

	@Override
	public void findView() {
		findActionBar();
		actionBar.setTitle("修改密码");
		actionBar.setBackAction(new Action() {
			@Override
			public void performAction(View view) {
				if (null != getCurrentFocus()
						&& null != getCurrentFocus().getWindowToken()) {
					((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
							.hideSoftInputFromWindow(getCurrentFocus()
									.getWindowToken(), 0);
				}
				back();
			}

			@Override
			public int getDrawable() {
				return R.drawable.btn_back;
			}
		});
		/*actionBar.setRefreshTextView("提交", new Action(){

			@Override
			public int getDrawable() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public void performAction(View view) {
				// TODO Auto-generated method stub
				if (StringUtil.isEmpty(old.getText().toString())) {
					old.requestFocus();
					Toast.makeText(SettingPwdActivity.this, "原密码为空，请输入",
							Toast.LENGTH_SHORT).show();
				} else if (StringUtil.isEmpty(newpwd.getText().toString())) {
					newpwd.requestFocus();
					Toast.makeText(SettingPwdActivity.this, "新密码为空，请输入",
							Toast.LENGTH_SHORT).show();
				} else if (old.getText().toString()
						.equals(newpwd.getText().toString())) {
					old.requestFocus();
					Toast.makeText(SettingPwdActivity.this, "原密码不能与新密码相同，请输入",
							Toast.LENGTH_SHORT).show();
				} else if (!newpwd.getText().toString().equals(second_newpwd.getText().toString())) {
					second_newpwd.requestFocus();
					Toast.makeText(SettingPwdActivity.this, "两次密码输入不一样",Toast.LENGTH_SHORT).show();
				}
				else {
					task = new GetTask();
					task.execute();
				}
			}
			
		});*/
		old = (EditText) findViewById(R.id.old);
		newpwd = (EditText) findViewById(R.id.newpwd);
		second_newpwd = (EditText) findViewById(R.id.second_newpwd);
		oldclear = (ImageView) findViewById(R.id.oldclear);
		newclear = (ImageView) findViewById(R.id.newclear);
		second_newclear = (ImageView) findViewById(R.id.second_newclear);
		mainView = findViewById(R.id.mainView);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		AsyncTaskUtil.cancelTask(task);
	}

	class GetTask extends AsyncTask<Void, Object, ResultModel<NullModel>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (progressDialog == null) {
				progressDialog = new AppProgressDialog(baseContext, "处理中...");
			}
			progressDialog.start();
		}

		@Override
		protected ResultModel<NullModel> doInBackground(Void... params) {
			return HttpApi.getInstance().parserData(
					NullModel.class,
					"auth/ainfo/modifypassword",
					new BsoftNameValuePair("oldpwd", MD5.getMD5(old.getText().toString())),
					new BsoftNameValuePair("newpwd", MD5.getMD5(newpwd.getText()
							.toString())),
					new BsoftNameValuePair("id", loginUser.id),
					new BsoftNameValuePair("sn", loginUser.sn));
		}

		@Override
		protected void onPostExecute(ResultModel<NullModel> result) {
			if (progressDialog != null) {
				progressDialog.stop();
				progressDialog = null;
			}
			if (null != result) {
				if (result.statue == Statue.SUCCESS) {
					Toast.makeText(baseContext, "修改密码成功", Toast.LENGTH_SHORT)
							.show();
					if (null != getCurrentFocus()
							&& null != getCurrentFocus().getWindowToken()) {
						((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
								.hideSoftInputFromWindow(getCurrentFocus()
										.getWindowToken(), 0);
					}
					back();
				} else {
					result.showToast(baseContext);
				}
			}
		}
	}

}
