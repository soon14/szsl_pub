package com.bsoft.hospital.pub.suzhoumh.activity.dynamic;

import java.util.ArrayList;

import org.json.JSONArray;

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

import com.app.tanklib.Preferences;
import com.app.tanklib.bitmap.IndexUrlCache;
import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.pulltorefresh.PullToRefreshBase;
import com.app.tanklib.pulltorefresh.PullToRefreshBase.Mode;
import com.app.tanklib.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.app.tanklib.pulltorefresh.PullToRefreshListView;
import com.app.tanklib.view.BsoftActionBar.Action;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.adapter.DynamicAdapter;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.DynamicShow;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.view.FooterView;

public class NoticeActivity extends BaseActivity{

	PullToRefreshListView mPullRefreshListView;
	ListView listView;
	DynamicAdapter adapter;
	//NewsAdapter adapter;
	ProgressBar emptyProgress;
	GetDataTask getDataTask;
	FooterView footerView;
	int pageNo = 1;
	int pageSize = 50;
	MoreTask moreTask;
	boolean firstLoad = false;
	ArrayList<DynamicShow> dataList;
	private String dttype = "1";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dynamic);
		findView();
		initData();
	}

	@Override
	public void findView() {
		// TODO Auto-generated method stub
		findActionBar();
		actionBar.setBackAction(new Action() {
			
			@Override
			public void performAction(View view) {
				// TODO Auto-generated method stub
				finish();
			}
			
			@Override
			public int getDrawable() {
				// TODO Auto-generated method stub
				return R.drawable.btn_back;
			}
		});
		emptyProgress = (ProgressBar)findViewById(R.id.emptyProgress);
		mPullRefreshListView = (PullToRefreshListView)findViewById(R.id.pull_refresh_list);
		mPullRefreshListView.setMode(Mode.PULL_FROM_START);
		mPullRefreshListView
				.setOnRefreshListener(new OnRefreshListener<ListView>() {
					@Override
					public void onRefresh(
							PullToRefreshBase<ListView> refreshView) {
						String label = DateUtils.formatDateTime(baseContext,
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
		footerView = new FooterView(baseContext);
		footerView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				pageNo++;
				moreTask = new MoreTask();
				moreTask.execute();
			}
		});
		listView = mPullRefreshListView.getRefreshableView();
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(baseContext,
						NoticeDetailActivity.class);
				intent.putExtra("vo", adapter.getItem(arg2 - 1));
				startActivity(intent);
			}
		});
	}
	
	private void initData()
	{
		this.urlMap = new IndexUrlCache(15);
		adapter = new DynamicAdapter(baseContext, urlMap);
		listView.setAdapter(adapter);
		dttype = getIntent().getStringExtra("dttype");
		if(dttype.equals("1"))
		{
			actionBar.setTitle("健康资讯");
		}
		else if(dttype.equals("2"))
		{
			actionBar.setTitle("通知公告");
		}
		getDataTask = new GetDataTask();
		getDataTask.execute();
	}

	private class GetDataTask extends
			AsyncTask<Void, Void, ResultModel<ArrayList<DynamicShow>>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			actionBar.startTextRefresh();;
			if (!firstLoad) {
				mPullRefreshListView.setRefreshing();
			}
		}

		@Override
		protected ResultModel<ArrayList<DynamicShow>> doInBackground(
				Void... params) {
			return HttpApi.getInstance().parserArray(DynamicShow.class,
					"auth/dynamic/list",
					new BsoftNameValuePair("start", String.valueOf(pageNo)),
					new BsoftNameValuePair("length", String.valueOf(pageSize)),
					new BsoftNameValuePair("dtype",dttype),//dtype 1新闻资讯,2院内通知
					new BsoftNameValuePair("sn", loginUser.sn),
					new BsoftNameValuePair("id", loginUser.id));
		}

		@Override
		protected void onPostExecute(ResultModel<ArrayList<DynamicShow>> result) {
			super.onPostExecute(result);
			if (null != result) {
				if (result.statue == Statue.SUCCESS) {
					if (null != result.list && result.list.size() > 0) {
						adapter.addData(result.list);
						if (result.list.size() == pageSize) {
							listView.addFooterView(footerView);
						}
						// 添加进缓存
						JSONArray arr = new JSONArray();
						for (DynamicShow t : result.list) {
							arr.put(t.toJson());
						}
						Preferences.getInstance().setStringData("dynamic",
								arr.toString());
						firstLoad = true;
					} 
				} else {
					result.showToast(baseContext);
				}
			} else {
				Toast.makeText(baseContext, "加载失败", Toast.LENGTH_SHORT).show();
			}
			actionBar.endTextRefresh();;
			mPullRefreshListView.onRefreshComplete();
		}
	}
	
	private class MoreTask extends
			AsyncTask<Void, Void, ResultModel<ArrayList<DynamicShow>>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			actionBar.startTextRefresh();;
		}

		@Override
		protected ResultModel<ArrayList<DynamicShow>> doInBackground(
				Void... params) {
			return HttpApi.getInstance().parserArray(DynamicShow.class,
					"auth/dynamic/list",
					new BsoftNameValuePair("start", String.valueOf(pageNo)),
					new BsoftNameValuePair("length", String.valueOf(pageSize)),
					new BsoftNameValuePair("dtype","1"),//dtype 1新闻资讯,2院内通知
					new BsoftNameValuePair("sn", loginUser.sn),
					new BsoftNameValuePair("id", loginUser.id));
		}

		@Override
		protected void onPostExecute(ResultModel<ArrayList<DynamicShow>> result) {
			super.onPostExecute(result);
			if (null != result) {
				if (result.statue == Statue.SUCCESS) {
					if (null != result.list && result.list.size() > 0) {
						adapter.addMoreData(result.list);
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
	
}
