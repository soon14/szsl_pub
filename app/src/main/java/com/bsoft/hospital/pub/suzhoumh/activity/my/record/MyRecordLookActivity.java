package com.bsoft.hospital.pub.suzhoumh.activity.my.record;

import java.util.ArrayList;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.view.BsoftActionBar.Action;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.adapter.MyRecordLookAdapter;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.model.record.RecordVo;
import com.bsoft.hospital.pub.suzhoumh.view.FooterView;
import com.app.tanklib.pulltorefresh.PullToRefreshBase;
import com.app.tanklib.pulltorefresh.PullToRefreshListView;
import com.app.tanklib.pulltorefresh.PullToRefreshBase.Mode;
import com.app.tanklib.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.app.tanklib.util.AsyncTaskUtil;
import com.app.tanklib.util.StringUtil;

/**
 * @author Tank E-mail:zkljxq@126.com
 * 
 * @类说明
 */
public class MyRecordLookActivity extends BaseActivity {

	ProgressBar emptyProgress;
	GetDataTask getDataTask;
	MoreTask moreTask;
	FooterView footerView;
	PullToRefreshListView mPullRefreshListView;
	ListView listView;
	MyRecordLookAdapter adapter;
	boolean firstLoad = false;
	int type = 0;
	int pageNo = 1;
	int pageSize = 50;
	String title;
	String hosCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.base_refreshlist);
		type = getIntent().getIntExtra("type", 0);
		title = getIntent().getStringExtra("title");
		hosCode = getIntent().getStringExtra("hosCode");
		findView();
		setClick();
		getDataTask = new GetDataTask();
		getDataTask.execute();
	}

	void setClick() {
	}

	@Override
	public void findView() {
		findActionBar();
		actionBar.setTitle(StringUtil.getTextLimit(title, 8));
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

						pageNo = 1;
						emptyProgress.setVisibility(View.GONE);
						getDataTask = new GetDataTask();
						getDataTask.execute();
					}
				});
		footerView = new FooterView(this);
		footerView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				pageNo++;
				moreTask = new MoreTask();
				moreTask.execute();
			}
		});
		listView = mPullRefreshListView.getRefreshableView();
		adapter = new MyRecordLookAdapter(this, type);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(baseContext,
						MyRecordInfoActivity.class);
				intent.putExtra("vo", adapter.getItem(arg2 - 1));
				startActivity(intent);
			}
		});
	}

	private class GetDataTask extends
			AsyncTask<Void, Void, ResultModel<ArrayList<RecordVo>>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (!firstLoad) {
				emptyProgress.setVisibility(View.VISIBLE);
			}
			mPullRefreshListView.setRefreshing();
		}

		@Override
		protected ResultModel<ArrayList<RecordVo>> doInBackground(
				Void... params) {
			switch (type) {
			case 1:
				return HttpApi
						.getInstance()
						.parserArray(
								RecordVo.class,
								"auth/opt/list/date/more",
								new BsoftNameValuePair("id", loginUser.id),
								new BsoftNameValuePair("phrid", loginUser.phrid),
								new BsoftNameValuePair("start", String
										.valueOf(pageNo)),
								new BsoftNameValuePair("length", String
										.valueOf(pageSize)),
								new BsoftNameValuePair("year", title),
								new BsoftNameValuePair("sn", loginUser.sn));
			case 2:
				return HttpApi
						.getInstance()
						.parserArray(
								RecordVo.class,
								"auth/opt/list/hospital/more",
								new BsoftNameValuePair("id", loginUser.id),
								new BsoftNameValuePair("phrid", loginUser.phrid),
								new BsoftNameValuePair("start", String
										.valueOf(pageNo)),
								new BsoftNameValuePair("length", String
										.valueOf(pageSize)),
								new BsoftNameValuePair("hospcode", hosCode),
								new BsoftNameValuePair("sn", loginUser.sn));
			case 3:
				return HttpApi
						.getInstance()
						.parserArray(
								RecordVo.class,
								"auth/opt/list/disease/more",
								new BsoftNameValuePair("id", loginUser.id),
								new BsoftNameValuePair("phrid", loginUser.phrid),
								new BsoftNameValuePair("start", String
										.valueOf(pageNo)),
								new BsoftNameValuePair("length", String
										.valueOf(pageSize)),
								new BsoftNameValuePair("disease", title),
								new BsoftNameValuePair("sn", loginUser.sn));
			default:
				return null;
			}
		}

		@Override
		protected void onPostExecute(ResultModel<ArrayList<RecordVo>> result) {
			super.onPostExecute(result);
			if (null != result) {
				if (result.statue == Statue.SUCCESS) {
					if (null != result.list && result.list.size() > 0) {
						adapter.addData(result.list);
						if (result.list.size() == pageSize) {
							listView.addFooterView(footerView);
						}
						firstLoad = true;
					} 
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

	private class MoreTask extends
			AsyncTask<Void, Void, ResultModel<ArrayList<RecordVo>>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			actionBar.startTextRefresh();;
		}

		@Override
		protected ResultModel<ArrayList<RecordVo>> doInBackground(
				Void... params) {
			switch (type) {
			case 1:
				return HttpApi
						.getInstance()
						.parserArray(
								RecordVo.class,
								"auth/opt/list/date/more",
								new BsoftNameValuePair("uid", loginUser.id),
								new BsoftNameValuePair("phrid", loginUser.phrid),
								new BsoftNameValuePair("start", String
										.valueOf(pageNo)),
								new BsoftNameValuePair("length", String
										.valueOf(pageSize)),
								new BsoftNameValuePair("year", title),
								new BsoftNameValuePair("sn", loginUser.sn));
			case 2:
				return HttpApi
						.getInstance()
						.parserArray(
								RecordVo.class,
								"auth/opt/list/hospital/more",
								new BsoftNameValuePair("uid", loginUser.id),
								new BsoftNameValuePair("phrid", loginUser.phrid),
								new BsoftNameValuePair("start", String
										.valueOf(pageNo)),
								new BsoftNameValuePair("length", String
										.valueOf(pageSize)),
								new BsoftNameValuePair("hospcode", hosCode),
								new BsoftNameValuePair("sn", loginUser.sn));
			case 3:
				return HttpApi
						.getInstance()
						.parserArray(
								RecordVo.class,
								"auth/opt/list/disease/more",
								new BsoftNameValuePair("uid", loginUser.id),
								new BsoftNameValuePair("phrid", loginUser.phrid),
								new BsoftNameValuePair("start", String
										.valueOf(pageNo)),
								new BsoftNameValuePair("length", String
										.valueOf(pageSize)),
								new BsoftNameValuePair("disease", title),
								new BsoftNameValuePair("sn", loginUser.sn));
			default:
				return null;
			}
		}

		@Override
		protected void onPostExecute(ResultModel<ArrayList<RecordVo>> result) {
			super.onPostExecute(result);
			if (null != result) {
				if (result.statue == Statue.SUCCESS) {
					if (null != result.list && result.list.size() > 0) {
						adapter.addMore(result.list);
						if (result.list.size() < pageSize) {
							listView.removeFooterView(footerView);
						}
					} else {
						listView.removeFooterView(footerView);
					}
				} else {
					pageNo--;
					result.showToast(baseContext);
				}
			} else {
				pageNo--;
				Toast.makeText(baseContext, "加载失败", Toast.LENGTH_SHORT).show();
			}
			actionBar.endTextRefresh();;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		AsyncTaskUtil.cancelTask(getDataTask);
		AsyncTaskUtil.cancelTask(moreTask);
	}

}
