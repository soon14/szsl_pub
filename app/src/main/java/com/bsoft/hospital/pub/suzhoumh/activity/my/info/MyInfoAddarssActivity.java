package com.bsoft.hospital.pub.suzhoumh.activity.my.info;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tanklib.http.BsoftNameValuePair;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.cache.CityCache;
import com.bsoft.hospital.pub.suzhoumh.model.NullModel;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.model.my.CityCode;
import com.bsoft.hospital.pub.suzhoumh.model.my.StreetVo;
import com.bsoft.hospital.pub.suzhoumh.model.my.UserDetailVo;
import com.app.tanklib.view.BsoftActionBar.Action;
import com.app.tanklib.util.AsyncTaskUtil;
import com.app.tanklib.util.StringUtil;

/**
 * @author Tank E-mail:zkljxq@126.com
 * 
 * @类说明
 */
public class MyInfoAddarssActivity extends BaseActivity {

	RelativeLayout layout1, layout2;
	TextView province, street;
	EditText address;
	UserDetailVo userDetailVo;

	CityCode city1, city2, city3;
	StreetVo city4;

	GetTask task;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myinfo_address);
		userDetailVo = (UserDetailVo) getIntent().getSerializableExtra("vo");
		findView();
		setClick();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.MyAddress_ACTION);
		this.registerReceiver(this.broadcastReceiver, filter);
	}

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (Constants.MyAddress_ACTION.equals(intent.getAction())) {
				int type = intent.getIntExtra("type", 0);
				if (type == 1) {
					city1 = (CityCode) intent.getSerializableExtra("city1");
					city2 = (CityCode) intent.getSerializableExtra("city2");
					city3 = (CityCode) intent.getSerializableExtra("city3");
					province.setText(city1.Title + "" + city2.Title + ""
							+ city3.Title);
					city4 = null;
					street.setText("街道");
				} else if (type == 2) {
					city4 = (StreetVo) intent.getSerializableExtra("city4");
					street.setText(city4.title);
				}
			}
		}
	};

	void setClick() {
		if (null != userDetailVo) {
			if (!StringUtil.isEmpty(userDetailVo.provinceid)
					|| !StringUtil.isEmpty(userDetailVo.districtid)
					|| !StringUtil.isEmpty(userDetailVo.cityid)) {
				city1 = CityCache.getInstance().allMap
						.get(userDetailVo.provinceid);
				city2 = CityCache.getInstance().allMap
						.get(userDetailVo.districtid);
				city3 = CityCache.getInstance().allMap.get(userDetailVo.cityid);
				province.setText(CityCache.getInstance().getCityName(
						userDetailVo.provinceid, userDetailVo.districtid,
						userDetailVo.cityid));
			}
			if (!StringUtil.isEmpty(userDetailVo.streetid)
					&& !StringUtil.isEmpty(userDetailVo.streetstr)) {
				city4 = new StreetVo();
				city4.id = userDetailVo.streetid;
				city4.title = userDetailVo.streetstr;
				street.setText(userDetailVo.streetstr);
			}
			if (!StringUtil.isEmpty(userDetailVo.detailadr)) {
				address.setText(userDetailVo.detailadr);
			}
		}
		layout1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null != getCurrentFocus()
						&& null != getCurrentFocus().getWindowToken()) {
					((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
							.hideSoftInputFromWindow(getCurrentFocus()
									.getWindowToken(), 0);
				}
				Intent intent = new Intent(baseContext,
						MyInfoAddressChooseActivity.class);
				startActivity(

				intent);
			}
		});
		layout2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null != city1 && null != city2 && null != city3) {
					if (null != getCurrentFocus()
							&& null != getCurrentFocus().getWindowToken()) {
						((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
								.hideSoftInputFromWindow(getCurrentFocus()
										.getWindowToken(), 0);
					}
					Intent intent = new Intent(baseContext,
							MyInfoAddressStreetActivity.class);
					intent.putExtra("id", city3.ID);
					startActivity(

					intent);
				} else {
					Toast.makeText(baseContext, "请选择省、市、区（县）",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	@Override
	public void findView() {
		findActionBar();
		actionBar.setTitle("居住地址");
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
				if (null == city1 || null == city2 || null == city3) {
					Toast.makeText(baseContext, "请选择省、市、区（县）",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (null == city4) {
					Toast.makeText(baseContext, "请选择街道", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				if (StringUtil.isEmpty(address.getText().toString())) {
					Toast.makeText(baseContext, "请输入详细地址", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				task = new GetTask();
				task.execute();
			}

			@Override
			public int getDrawable() {
				return 0;
			}
		});
		layout1 = (RelativeLayout) findViewById(R.id.layout1);
		layout2 = (RelativeLayout) findViewById(R.id.layout2);
		province = (TextView) findViewById(R.id.province);
		street = (TextView) findViewById(R.id.street);
		address = (EditText) findViewById(R.id.address);
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

	public String getAddress() {
		StringBuffer sb = new StringBuffer();
		sb.append(city1.Title).append(city2.Title).append(city3.Title)
				.append(city4.title);
		sb.append(address.getText().toString());
		return sb.toString();
	}

	class GetTask extends AsyncTask<Void, Object, ResultModel<NullModel>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			actionBar.startTextRefresh();
		}

		@Override
		protected ResultModel<NullModel> doInBackground(Void... params) {
			return HttpApi.getInstance().parserData(
					NullModel.class,
					"auth/ainfo/modify/address",
					new BsoftNameValuePair("id", loginUser.id),
					new BsoftNameValuePair("provinceid", city1.ID),
					new BsoftNameValuePair("districtid", city2.ID),
					new BsoftNameValuePair("cityid", city3.ID),
					new BsoftNameValuePair("streetid", city4.id),
					new BsoftNameValuePair("detailadr", address.getText()
							.toString()),
					new BsoftNameValuePair("addressstr", getAddress()),
					new BsoftNameValuePair("sn", loginUser.sn));
		}

		@Override
		protected void onPostExecute(ResultModel<NullModel> result) {
			actionBar.endTextRefresh();
			if (null != result) {
				if (result.statue == Statue.SUCCESS) {
					Toast.makeText(baseContext, "保存居住地址成功", Toast.LENGTH_SHORT)
							.show();
					Intent intent = new Intent(Constants.MyInfo_ACTION);
					intent.putExtra("index", 6);
					intent.putExtra("detailadr", address.getText().toString());
					intent.putExtra("provinceid", city1.ID);
					intent.putExtra("districtid", city2.ID);
					intent.putExtra("cityid", city3.ID);
					intent.putExtra("streetid", city4.id);
					intent.putExtra("streetstr", city4.title);
					intent.putExtra("value", getAddress());
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
