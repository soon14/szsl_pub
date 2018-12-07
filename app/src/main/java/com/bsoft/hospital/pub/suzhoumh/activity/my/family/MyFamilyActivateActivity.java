package com.bsoft.hospital.pub.suzhoumh.activity.my.family;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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
import com.bsoft.hospital.pub.suzhoumh.model.ChoiceItem;
import com.bsoft.hospital.pub.suzhoumh.model.NullModel;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.model.my.HosVo;
import com.bsoft.hospital.pub.suzhoumh.model.my.MyFamilyVo;
import com.bsoft.hospital.pub.suzhoumh.view.AppProgressDialog;

//暂时不用
/**
 * @author Tank E-mail:zkljxq@126.com
 * 
 * @类说明
 */
public class MyFamilyActivateActivity extends BaseActivity {
	AppProgressDialog progressDialog;
	View mainView;
	EditText cardnum;
	RelativeLayout cardLayout, addressLayout;
	TextView name, card, address;

	ChoiceItem cardResult, cityResult;
	HosVo hosVo;
	GetTask task;

	int index = -1;
	MyFamilyVo vo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myfamily_activate);
		vo = (MyFamilyVo) getIntent().getSerializableExtra("vo");
		index = getIntent().getIntExtra("index", -1);
		cardResult = new ChoiceItem("2", "就诊卡");
		findView();
		setClick();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.MyFamilyCardType_ACTION);
		filter.addAction(Constants.MyFamilyAddressCity_ACTION);
		filter.addAction(Constants.MyFamilyAddressHos_ACTION);
		this.registerReceiver(this.broadcastReceiver, filter);
	}

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (Constants.MyFamilyCardType_ACTION.equals(intent.getAction())) {
				cardResult = (ChoiceItem) intent.getSerializableExtra("result");
				card.setText(cardResult.itemName);
				if ("1".equals(cardResult.index)) {
					address.setText("选择城市");
				} else if ("2".equals(cardResult.index)) {
					address.setText("选择医院");
				}
			} else if (Constants.MyFamilyAddressCity_ACTION.equals(intent
					.getAction())) {
				cityResult = (ChoiceItem) intent.getSerializableExtra("result");
				address.setText(cityResult.itemName);
			} else if (Constants.MyFamilyAddressHos_ACTION.equals(intent
					.getAction())) {
				hosVo = (HosVo) intent.getSerializableExtra("result");
				address.setText(hosVo.title);
			}
		}
	};

	void setClick() {
		if (null != vo) {
			name.setText(vo.realname);
		}
		card.setText(cardResult.itemName);
		if ("1".equals(cardResult.index)) {
			address.setText("选择城市");
		} else if ("2".equals(cardResult.index)) {
			address.setText("选择医院");
		}
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
			public void onClick(View v) {
				if (StringUtil.isEmpty(cardnum.getText().toString())) {
					Toast.makeText(baseContext, "卡号为空，请输入", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				if ("1".equals(cardResult.index)) {
					if (cityResult == null) {
						Toast.makeText(baseContext, "归属地为空，请选择",
								Toast.LENGTH_SHORT).show();
						return;
					}
				} else if ("2".equals(cardResult.index)) {
					if (hosVo == null) {
						Toast.makeText(baseContext, "归属地为空，请选择",
								Toast.LENGTH_SHORT).show();
						return;
					}
				}
				task = new GetTask();
				task.execute();

			}
		});
		cardLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/*Intent intent = new Intent(baseContext, DicActivity.class);
				intent.putExtra("data", ModelCache.getInstance()
						.getCardTypeList());
				intent.putExtra("result", cardResult);
				intent.putExtra("title", "卡类型");
				intent.putExtra("action", Constants.MyFamilyCardType_ACTION);
				startActivity(intent);*/
			}
		});
		addressLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null == cardResult) {
					Toast.makeText(baseContext, "请先选择卡类型", Toast.LENGTH_SHORT)
							.show();
				} else {
					/*if ("1".equals(cardResult.index)) {
						Intent intent = new Intent(baseContext,
								CityActivity.class);
						intent.putExtra("title", "选择城市");
						intent.putExtra("action",
								Constants.MyFamilyAddressCity_ACTION);
						startActivity(intent);
					} else if ("2".equals(cardResult.index)) {
						Intent intent = new Intent(baseContext,
								HosActivity.class);
						intent.putExtra("action",
								Constants.MyFamilyAddressHos_ACTION);
						startActivity(intent);
					}*/
				}
			}
		});

	}

	@Override
	public void findView() {
		findActionBar();
		actionBar.setTitle("成员验证");
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
		mainView = findViewById(R.id.mainView);
		name = (TextView) findViewById(R.id.name);
		cardnum = (EditText) findViewById(R.id.cardnum);
		cardLayout = (RelativeLayout) findViewById(R.id.cardLayout);
		addressLayout = (RelativeLayout) findViewById(R.id.addressLayout);
		card = (TextView) findViewById(R.id.card);
		address = (TextView) findViewById(R.id.address);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		AsyncTaskUtil.cancelTask(task);
		if (null != broadcastReceiver) {
			unregisterReceiver(broadcastReceiver);
			broadcastReceiver = null;
		}
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
					"auth/family/active",
					new BsoftNameValuePair("fid", vo.id),
					new BsoftNameValuePair("cardtype", cardResult.index),
					new BsoftNameValuePair("cardnum", cardnum.getText()
							.toString()),
					new BsoftNameValuePair("belong", "1"
							.equals(cardResult.index) ? cityResult.index
							: hosVo.id),
					new BsoftNameValuePair("belongname", "1"
							.equals(cardResult.index) ? cityResult.itemName
							: hosVo.title),
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
					Toast.makeText(baseContext, "验证成功", Toast.LENGTH_SHORT)
							.show();
					Intent intent = new Intent(
							Constants.MyFamilyActivate_ACTION);
					intent.putExtra("index", index);
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
