package com.bsoft.hospital.pub.suzhoumh.activity.my.info;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tanklib.http.BsoftNameValuePair;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.AppApplication;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.NullModel;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.view.AppProgressDialog;
import com.bsoft.hospital.pub.suzhoumh.view.CountDownButtonHelper;
import com.app.tanklib.view.BsoftActionBar.Action;
import com.app.tanklib.util.AsyncTaskUtil;
import com.app.tanklib.util.StringUtil;

/**
 * @author Tank E-mail:zkljxq@126.com
 * 
 * @类说明
 */
public class MyMobileBindActivity extends BaseActivity {
	AppProgressDialog progressDialog;
	EditText mobile, mobile1, checkcard, checkcard1;
	ImageView mobileclear, mobileclear1;
	Button but_checkcard, but_checkcard1;
	CountDownButtonHelper countHelper, countHelper1;
	CheckTask checkTask;
	StepTask stepTask;
	View mainView;
	ImageView p1, p2, pp;
	TextView t1, t2;

	LinearLayout layout1, layout2;
	int step = 0;
	float fromx = 0;
	float tox = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mymobile_bind);
		findView();
		setClick();
		changeText();
		changeStatue();
	}

	void setClick() {
		countHelper = new CountDownButtonHelper(but_checkcard, 60, 1);
		but_checkcard.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (StringUtil.isEmpty(mobile.getText().toString())) {
					mobile.requestFocus();
					Toast.makeText(MyMobileBindActivity.this, "电话号码不能为空，请输入",
							Toast.LENGTH_SHORT).show();
					return;
				} else if (!StringUtil.isMobilPhoneNumber(mobile.getText()
						.toString())) {
					mobile.requestFocus();
					Toast.makeText(MyMobileBindActivity.this, "电话号码不符合，请重新输入",
							Toast.LENGTH_SHORT).show();
					return;
				} else if (!StringUtil.isEmpty(loginUser.mobile)
						&& !loginUser.mobile
								.equals(mobile.getText().toString())) {
					mobile.requestFocus();
					Toast.makeText(baseContext, "输入的手机号不是原来手机号，请重新输入",
							Toast.LENGTH_SHORT).show();
					return;
				} else {
					checkTask = new CheckTask();
					checkTask.execute();
				}
			}
		});
		mobile.addTextChangedListener(new TextWatcher() {
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
				if (mobile.getText().toString().length() == 0) {
					mobileclear.setVisibility(View.INVISIBLE);
				} else {
					mobileclear.setVisibility(View.VISIBLE);
				}
			}
		});
		mobileclear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mobile.setText("");
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
		findViewById(R.id.submit).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				fromx = 0;
				tox = 0;
				switch (step) {
				case 0:
					if (StringUtil.isEmpty(mobile.getText().toString())) {
						mobile.requestFocus();
						Toast.makeText(baseContext, "电话号码不能为空，请输入",
								Toast.LENGTH_SHORT).show();
						return;
					} else if (!StringUtil.isMobilPhoneNumber(mobile.getText()
							.toString())) {
						mobile.requestFocus();
						Toast.makeText(baseContext, "电话号码不符合，请重新输入",
								Toast.LENGTH_SHORT).show();
						return;
					} else if (StringUtil.isEmpty(checkcard.getText()
							.toString())) {
						checkcard.requestFocus();
						Toast.makeText(baseContext, "验证码不能为空，请输入",
								Toast.LENGTH_SHORT).show();
						return;
					}
					fromx = AppApplication.getWidthPixels() / 4 - pp.getWidth()
							/ 2;
					tox = AppApplication.getWidthPixels() * 3 / 4
							- pp.getWidth() / 2;
					break;
				case 1:
					if (StringUtil.isEmpty(mobile1.getText().toString())) {
						mobile1.requestFocus();
						Toast.makeText(baseContext, "电话号码不能为空，请输入",
								Toast.LENGTH_SHORT).show();
						return;
					} else if (!StringUtil.isMobilPhoneNumber(mobile1.getText()
							.toString())) {
						mobile1.requestFocus();
						Toast.makeText(baseContext, "电话号码不符合，请重新输入",
								Toast.LENGTH_SHORT).show();
						return;
					} else if (StringUtil.isEmpty(checkcard1.getText()
							.toString())) {
						checkcard1.requestFocus();
						Toast.makeText(baseContext, "验证码不能为空，请输入",
								Toast.LENGTH_SHORT).show();
						return;
					}
					fromx = AppApplication.getWidthPixels() * 3 / 4
							- pp.getWidth() / 2;
					tox = AppApplication.getWidthPixels() * 5 / 4
							- pp.getWidth() / 2;
					break;
				default:
					break;
				}
				stepTask = new StepTask();
				stepTask.execute();
			}
		});
	}

	void setClick2() {
		countHelper1 = new CountDownButtonHelper(but_checkcard1, 60, 1);
		but_checkcard1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (StringUtil.isEmpty(mobile1.getText().toString())) {
					mobile1.requestFocus();
					Toast.makeText(MyMobileBindActivity.this, "电话号码不能为空，请输入",
							Toast.LENGTH_SHORT).show();
					return;
				} else if (!StringUtil.isMobilPhoneNumber(mobile1.getText()
						.toString())) {
					mobile1.requestFocus();
					Toast.makeText(MyMobileBindActivity.this, "电话号码不符合，请重新输入",
							Toast.LENGTH_SHORT).show();
					return;
				} else {
					checkTask = new CheckTask();
					checkTask.execute();
				}
			}
		});
		mobile1.addTextChangedListener(new TextWatcher() {
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
				if (mobile1.getText().toString().length() == 0) {
					mobileclear1.setVisibility(View.INVISIBLE);
				} else {
					mobileclear1.setVisibility(View.VISIBLE);
				}
			}
		});
		mobileclear1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mobile1.setText("");
			}
		});
	}

	@Override
	public void findView() {
		findActionBar();
		actionBar.setTitle("修改手机绑定");
		actionBar.setBackAction(new Action() {
			@Override
			public void performAction(View view) {
				fromx = 0;
				tox = 0;
				switch (step) {
				case 0:
					back();
					break;
				case 1:
					fromx = AppApplication.getWidthPixels() / 4 - pp.getWidth()
							/ 2;
					tox = AppApplication.getWidthPixels() * 3 / 4
							- pp.getWidth() / 2;
					// 开始动画
					startAnimationBack();
					break;
				default:
					break;
				}
			}

			@Override
			public int getDrawable() {
				return R.drawable.btn_back;
			}
		});
		mobile = (EditText) findViewById(R.id.mobile);
		checkcard = (EditText) findViewById(R.id.checkcard);
		mobileclear = (ImageView) findViewById(R.id.mobileclear);
		but_checkcard = (Button) findViewById(R.id.but_checkcard);
		mobile1 = (EditText) findViewById(R.id.mobile1);
		checkcard1 = (EditText) findViewById(R.id.checkcard1);
		mobileclear1 = (ImageView) findViewById(R.id.mobileclear1);
		but_checkcard1 = (Button) findViewById(R.id.but_checkcard1);
		layout1 = (LinearLayout) findViewById(R.id.layout1);
		layout2 = (LinearLayout) findViewById(R.id.layout2);
		mainView = findViewById(R.id.mainView);
		p1 = (ImageView) findViewById(R.id.p1);
		p2 = (ImageView) findViewById(R.id.p2);
		pp = (ImageView) findViewById(R.id.pp);
		t1 = (TextView) findViewById(R.id.t1);
		t2 = (TextView) findViewById(R.id.t2);
	}

	void startAnimation() {
		TranslateAnimation tranAnim = new TranslateAnimation(fromx, tox, 0, 0);
		tranAnim.setDuration(600);
		pp.startAnimation(tranAnim);
		tranAnim.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation arg0) {
				pp.setVisibility(View.VISIBLE);
				switch (step) {
				case 0:
					p1.setImageResource(R.drawable.pwd_q2);
					break;
				case 1:
					p2.setImageResource(R.drawable.pwd_q2);
					break;
				default:
					break;
				}
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {

			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				step++;
				setView();
				pp.setVisibility(View.GONE);
				changeText();
				changeStatue();

			}
		});
	}

	void startAnimationBack() {
		TranslateAnimation tranAnim = new TranslateAnimation(tox, fromx, 0, 0);
		tranAnim.setDuration(600);
		pp.startAnimation(tranAnim);
		tranAnim.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation arg0) {
				pp.setVisibility(View.VISIBLE);
				switch (step) {
				case 0:
					p1.setImageResource(R.drawable.pwd_q1);
					break;
				case 1:
					p2.setImageResource(R.drawable.pwd_q1);
					break;
				default:
					break;
				}
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {

			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				step--;
				switch (step) {
				case -1:
					pp.setVisibility(View.GONE);
					back();
					break;
				case 0:
					checkcard.setText("");
					mobile1.setText("");
					checkcard1.setText("");
					pp.setVisibility(View.GONE);
					setView();
					changeText();
					changeStatue();
					break;
				default:
					break;
				}

			}
		});
	}

	void setView() {
		switch (step) {
		case 0:
			mobile.requestFocus();
			layout1.setVisibility(View.VISIBLE);
			layout2.setVisibility(View.GONE);
			break;
		case 1:
			mobile1.requestFocus();
			layout1.setVisibility(View.GONE);
			layout2.setVisibility(View.VISIBLE);
			setClick2();
			break;
		default:
			break;
		}
	}

	void changeText() {
		switch (step) {
		case 0:
			t1.setTextColor(getResources().getColor(R.color.pwdtest1));
			t2.setTextColor(getResources().getColor(R.color.pwdtest2));
			break;
		case 1:
			t1.setTextColor(getResources().getColor(R.color.pwdtest2));
			t2.setTextColor(getResources().getColor(R.color.pwdtest1));
			break;
		default:
			break;
		}
	}

	void changeStatue() {
		switch (step) {
		case 0:
			p1.setImageResource(R.drawable.pwd_q3);
			p2.setImageResource(R.drawable.pwd_q1);
			break;
		case 1:
			p1.setImageResource(R.drawable.pwd_q2);
			p2.setImageResource(R.drawable.pwd_q3);
			break;
		default:
			break;
		}
	}

	/**
	 * 验证码获取中
	 */
	void checkStart() {
		switch (step) {
		case 0:
			but_checkcard.setBackgroundResource(R.drawable.recheckcard);
			but_checkcard.setText("获取中...");
			break;
		case 1:
			but_checkcard1.setBackgroundResource(R.drawable.recheckcard);
			but_checkcard1.setText("获取中...");
			break;
		default:
			break;
		}
	}

	/**
	 * 验证码获取失败
	 */
	void checkEnd() {
		switch (step) {
		case 0:
			but_checkcard.setText("");
			but_checkcard.setBackgroundResource(R.drawable.btn_checkcard);
			break;
		case 1:
			but_checkcard1.setText("");
			but_checkcard1.setBackgroundResource(R.drawable.btn_checkcard);
			break;
		default:
			break;
		}
	}

	class CheckTask extends AsyncTask<Void, Object, ResultModel<NullModel>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			checkStart();
		}

		@Override
		protected ResultModel<NullModel> doInBackground(Void... params) {
			return HttpApi.getInstance().parserData(
					NullModel.class,
					"util/phonecode",
					new BsoftNameValuePair("mobile", step == 0 ? mobile
							.getText().toString() : mobile1.getText()
							.toString()));
		}

		@Override
		protected void onPostExecute(ResultModel<NullModel> result) {
			if (null != result) {
				if (result.statue == Statue.SUCCESS) {
					switch (step) {
					case 0:
						countHelper.start();
						break;
					case 1:
						countHelper1.start();
						break;
					default:
						break;
					}
					Toast.makeText(baseContext, "已成功发送短信", Toast.LENGTH_SHORT)
							.show();
				} else {
					result.showToast(baseContext);
					checkEnd();
				}
			} else {
				Toast.makeText(baseContext, "请检查你的电话号码", Toast.LENGTH_SHORT)
						.show();
				checkEnd();
			}
		}
	}

	class StepTask extends AsyncTask<Void, Object, ResultModel<NullModel>> {

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
			switch (step) {
			case 0:
				return HttpApi.getInstance().parserData(
						NullModel.class,
						"auth/ainfo/verify/mobile",
						new BsoftNameValuePair("sn", loginUser.sn),
						new BsoftNameValuePair("id", loginUser.id),
						new BsoftNameValuePair("code", checkcard.getText()
								.toString()),
						new BsoftNameValuePair("mobile", mobile.getText()
								.toString()));
			case 1:
				return HttpApi.getInstance().parserData(
						NullModel.class,
						"auth/ainfo/modify/mobile",
						new BsoftNameValuePair("id", loginUser.id),
						new BsoftNameValuePair("code", checkcard1.getText()
								.toString()),
						new BsoftNameValuePair("mobile", mobile1.getText()
								.toString()),
						new BsoftNameValuePair("sn", loginUser.sn));
			default:
				return null;
			}

		}

		@Override
		protected void onPostExecute(ResultModel<NullModel> result) {
			if (progressDialog != null) {
				progressDialog.stop();
				progressDialog = null;
			}
			if (null != result) {
				if (result.statue == Statue.SUCCESS) {
					switch (step) {
					case 0:
						// 开始动画
						startAnimation();
						break;
					case 1:
						AlertDialog.Builder builder = new AlertDialog.Builder(
								baseContext);
						builder.setIcon(android.R.drawable.ic_dialog_info);
						builder.setTitle("操作成功");
						builder.setMessage("新手机绑定成功，点击返回");
						builder.setPositiveButton("确认",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										setResult(RESULT_OK);
										Intent intent = new Intent(
												Constants.MyInfo_ACTION);
										intent.putExtra("index", 5);
										intent.putExtra("value", mobile1
												.getText().toString());
										sendBroadcast(intent);
										if (null != getCurrentFocus()
												&& null != getCurrentFocus()
														.getWindowToken()) {
											((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
													.hideSoftInputFromWindow(
															getCurrentFocus()
																	.getWindowToken(),
															0);
										}
										back();
									}
								});
						builder.create().show();
						break;
					default:
						break;
					}
				} else {
					result.showToast(baseContext);
				}
			} else {
				Toast.makeText(baseContext, "操作失败", Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		AsyncTaskUtil.cancelTask(checkTask);
		AsyncTaskUtil.cancelTask(stepTask);
	}

}
