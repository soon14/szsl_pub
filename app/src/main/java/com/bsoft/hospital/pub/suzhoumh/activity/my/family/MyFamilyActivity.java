package com.bsoft.hospital.pub.suzhoumh.activity.my.family;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tanklib.bitmap.IndexUrlCache;
import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.pulltorefresh.PullToRefreshBase;
import com.app.tanklib.pulltorefresh.PullToRefreshBase.Mode;
import com.app.tanklib.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.app.tanklib.pulltorefresh.PullToRefreshListView;
import com.app.tanklib.util.AsyncTaskUtil;
import com.app.tanklib.view.BsoftActionBar.Action;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.model.my.MyFamilyVo;
import com.bsoft.hospital.pub.suzhoumh.util.IDCard;

/**
 * 家庭成员列表
 * @author Administrator
 *
 */
public class MyFamilyActivity extends BaseActivity {

	PullToRefreshListView mPullRefreshListView;
	ListView listView;
	MyFamilyAdapter adapter;
	ProgressBar emptyProgress;
	GetDataTask getDataTask;

	boolean firstLoad = false;
	private TextView tv_name;
	private TextView tv_age;
	private TextView tv_idcard;
	private TextView tv_phone;
	private TextView tv_sex;
	
	public  int STATE_ADD = 0;
	public  int STATE_EDIT = 1;
	
	private ArrayList<MyFamilyVo> list = new ArrayList<MyFamilyVo>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myfamily);
		this.urlMap = new IndexUrlCache();
		findView();
		initView();
		getDataTask = new GetDataTask();
		getDataTask.execute();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.MyFamily_ACTION);
		filter.addAction(Constants.MyFamilyActivate_ACTION);
		this.registerReceiver(this.broadcastReceiver, filter);
	}

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (Constants.MyFamily_ACTION.equals(intent.getAction())) {

				getDataTask = new GetDataTask();
				getDataTask.execute();
			}
			
		}
	};

	private void initView()
	{
		tv_name = (TextView)findViewById(R.id.tv_name);
		tv_age = (TextView)findViewById(R.id.tv_age);
		tv_idcard = (TextView)findViewById(R.id.tv_idcard);
		tv_phone = (TextView)findViewById(R.id.tv_phone);
		tv_sex = (TextView)findViewById(R.id.tv_sex);
		
		tv_name.setText(loginUser.realname);
		tv_idcard.setText(IDCard.getHideCardStr(loginUser.idcard));
		tv_phone.setText(IDCard.getHideMobileStr(loginUser.mobile));
		if(loginUser.idcard!=null&&!loginUser.idcard.equals(""))
		{
			tv_age.setText(IDCard.getAge(loginUser.idcard)+"岁");
			tv_sex.setText(IDCard.getSex(loginUser.idcard));
		}
	}
	
	@Override
	public void findView() {
		findActionBar();
		actionBar.setTitle("家庭管理");
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
		actionBar.setRefreshTextView("添加",new Action() {

			@Override
			public void performAction(View view) {
				Intent intent = new Intent(baseContext,
						MyFamilyAddActivity.class);
				intent.putExtra("currentstate", STATE_ADD);
				startActivity(intent);
			}

			@Override
			public int getDrawable() {
				return R.drawable.btn_add;
			}
		});
		emptyProgress = (ProgressBar) findViewById(R.id.emptyProgress);
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		mPullRefreshListView.setMode(Mode.PULL_FROM_START);
		mPullRefreshListView
				.setOnRefreshListener(new OnRefreshListener<ListView>() {
					@Override
					public void onRefresh(
							PullToRefreshBase<ListView> refreshView) {
						String label = DateUtils.formatDateTime(
								getApplicationContext(),
								System.currentTimeMillis(),
								DateUtils.FORMAT_SHOW_TIME
										| DateUtils.FORMAT_SHOW_DATE
										| DateUtils.FORMAT_ABBREV_ALL);
						refreshView.getLoadingLayoutProxy()
								.setLastUpdatedLabel(label);

						emptyProgress.setVisibility(View.GONE);
						getDataTask = new GetDataTask();
						getDataTask.execute();
					}
				});
		listView = mPullRefreshListView.getRefreshableView();
		adapter = new MyFamilyAdapter(this,list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(MyFamilyActivity.this,MyFamilyEditActivity.class);
				intent.putExtra("myfamilyvo", adapter.getItem(position-1));
				startActivity(intent);
			}
		});
	}

	private class GetDataTask extends
			AsyncTask<Void, Void, ResultModel<ArrayList<MyFamilyVo>>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (!firstLoad) {
				emptyProgress.setVisibility(View.VISIBLE);
			}
			mPullRefreshListView.setRefreshing();
		}

		@Override
		protected ResultModel<ArrayList<MyFamilyVo>> doInBackground(
				Void... params) {
			return HttpApi.getInstance().parserArray(MyFamilyVo.class,
					"auth/family/list",
					new BsoftNameValuePair("id", loginUser.id),
					new BsoftNameValuePair("sn", loginUser.sn));
		}

		@Override
		protected void onPostExecute(ResultModel<ArrayList<MyFamilyVo>> result) {
			super.onPostExecute(result);
			if (null != result) {
				if (result.statue == Statue.SUCCESS) {
					if (null != result.list && result.list.size() > 0) {
						firstLoad = true;
					}
					adapter.refresh(result.list);
				} else {
					result.showToast(baseContext);
				}
			} else {
				Toast.makeText(baseContext, "加载失败", Toast.LENGTH_SHORT).show();
			}
			emptyProgress.setVisibility(View.GONE);
			mPullRefreshListView.onRefreshComplete();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		AsyncTaskUtil.cancelTask(getDataTask);
		if (null != broadcastReceiver) {
			unregisterReceiver(broadcastReceiver);
			broadcastReceiver = null;
		}
	}
}
