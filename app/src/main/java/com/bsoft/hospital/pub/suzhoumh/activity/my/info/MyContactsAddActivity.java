package com.bsoft.hospital.pub.suzhoumh.activity.my.info;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.util.AsyncTaskUtil;
import com.app.tanklib.util.StringUtil;
import com.app.tanklib.view.BsoftActionBar.Action;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.cache.ModelCache;
import com.bsoft.hospital.pub.suzhoumh.model.ChoiceItem;
import com.bsoft.hospital.pub.suzhoumh.model.CodeModel;
import com.bsoft.hospital.pub.suzhoumh.model.NullModel;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.model.my.MyContactVo;

/**
 * @author Tank E-mail:zkljxq@126.com
 * 
 * @类说明
 */
public class MyContactsAddActivity extends BaseActivity {

	RelativeLayout layout1;
	EditText name, mobile;
	ImageView nameclear, mobileclear;
	TextView relation;

	GetTask task;
	EditTask editTask;

	ChoiceItem relationResult;

	MyContactVo vo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mycontacts_add);
		vo = (MyContactVo) getIntent().getSerializableExtra("vo");
		findView();
		setClick();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.MyContactsAdd_ACTION);
		this.registerReceiver(this.broadcastReceiver, filter);
		if (null != vo) {
			name.setText(vo.name);
			mobile.setText(vo.mobile);
			relationResult = new ChoiceItem(vo.relation, ModelCache
					.getInstance().getRelationName(vo.relation));
			relation.setText(relationResult.itemName);
		}
	}

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (Constants.MyContactsAdd_ACTION.equals(intent.getAction())) {
				relationResult = (ChoiceItem) intent
						.getSerializableExtra("result");
				relation.setText(relationResult.itemName);
			}
		}
	};

	void setClick() {
		layout1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/*Intent intent = new Intent(baseContext, DicActivity.class);
				intent.putExtra("data", ModelCache.getInstance()
						.getRelationList());
				intent.putExtra("result", relationResult);
				intent.putExtra("title", "与本人关系");
				intent.putExtra("action", Constants.MyContactsAdd_ACTION);
				startActivity(intent);*/
			}
		});
		name.addTextChangedListener(new TextWatcher() {
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
				if (name.getText().toString().length() == 0) {
					nameclear.setVisibility(View.INVISIBLE);
				} else {
					nameclear.setVisibility(View.VISIBLE);
				}
			}
		});
		nameclear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				name.setText("");
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
	}

	@Override
	public void findView() {
		findActionBar();
		if (null == vo) {
			actionBar.setTitle("新增联系人");
		} else {
			actionBar.setTitle("编辑联系人");
		}
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
		actionBar.setRefreshTextView("提交", new Action() {

			@Override
			public void performAction(View view) {
				if (StringUtil.isEmpty(name.getText().toString())) {
					name.requestFocus();
					Toast.makeText(MyContactsAddActivity.this, "姓名不能为空，请输入",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (StringUtil.isEmpty(mobile.getText().toString())) {
					mobile.requestFocus();
					Toast.makeText(MyContactsAddActivity.this, "电话号码不能为空，请输入",
							Toast.LENGTH_SHORT).show();
					return;
				} else if (!StringUtil.isMobilPhoneNumber(mobile.getText()
						.toString())) {
					mobile.requestFocus();
					Toast.makeText(MyContactsAddActivity.this, "电话号码不符合，请重新输入",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (null == relationResult) {
					Toast.makeText(MyContactsAddActivity.this, "与本人关系未选择，请选择",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (null == vo) {
					task = new GetTask();
					task.execute();
				} else {
					editTask = new EditTask();
					editTask.execute();
				}
			}

			@Override
			public int getDrawable() {
				return 0;
			}
		});
		layout1 = (RelativeLayout) findViewById(R.id.layout1);
		name = (EditText) findViewById(R.id.name);
		mobile = (EditText) findViewById(R.id.mobile);
		nameclear = (ImageView) findViewById(R.id.nameclear);
		mobileclear = (ImageView) findViewById(R.id.mobileclear);
		relation = (TextView) findViewById(R.id.relation);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		AsyncTaskUtil.cancelTask(task);
		AsyncTaskUtil.cancelTask(editTask);
		if (null != broadcastReceiver) {
			unregisterReceiver(broadcastReceiver);
			broadcastReceiver = null;
		}
	}

	class GetTask extends AsyncTask<Void, Object, ResultModel<CodeModel>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			actionBar.startTextRefresh();
		}

		@Override
		protected ResultModel<CodeModel> doInBackground(Void... params) {
			return HttpApi.getInstance().parserData(
					CodeModel.class,
					"auth/ainfo/contact/add",
					new BsoftNameValuePair("id", loginUser.id),
					new BsoftNameValuePair("name", name.getText().toString()),
					new BsoftNameValuePair("mobile", mobile.getText()
							.toString()),
					new BsoftNameValuePair("relation", relationResult.index),
					new BsoftNameValuePair("sn", loginUser.sn));
		}

		@Override
		protected void onPostExecute(ResultModel<CodeModel> result) {
			actionBar.endTextRefresh();
			if (null != result) {
				if (result.statue == Statue.SUCCESS && null != result.data
						&& !StringUtil.isEmpty(result.data.id)) {
					Toast.makeText(baseContext, "保存联系人成功", Toast.LENGTH_SHORT)
							.show();
					Intent intent = new Intent(Constants.MyContacts_ACTION);
					MyContactVo vo = new MyContactVo();
					vo.mobile = mobile.getText().toString();
					vo.name = name.getText().toString();
					vo.relation = relationResult.index;
					vo.id = result.data.id;
					intent.putExtra("vo", vo);
					sendBroadcast(intent);
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

	class EditTask extends AsyncTask<Void, Object, ResultModel<NullModel>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			actionBar.startTextRefresh();
		}

		@Override
		protected ResultModel<NullModel> doInBackground(Void... params) {
			return HttpApi.getInstance().parserData(
					NullModel.class,
					"auth/ainfo/contact/update",
					new BsoftNameValuePair("cid", vo.id),
					new BsoftNameValuePair("id", loginUser.id),
					new BsoftNameValuePair("name", name.getText().toString()),
					new BsoftNameValuePair("mobile", mobile.getText()
							.toString()),
					new BsoftNameValuePair("relation", relationResult.index),
					new BsoftNameValuePair("sn", loginUser.sn));
		}

		@Override
		protected void onPostExecute(ResultModel<NullModel> result) {
			actionBar.endTextRefresh();
			if (null != result) {
				if (result.statue == Statue.SUCCESS) {
					Toast.makeText(baseContext, "保存联系人成功", Toast.LENGTH_SHORT)
							.show();
					Intent intent = new Intent(Constants.MyContactsEdit_ACTION);
					vo.mobile = mobile.getText().toString();
					vo.name = name.getText().toString();
					vo.relation = relationResult.index;
					intent.putExtra("vo", vo);
					sendBroadcast(intent);
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
