package com.bsoft.hospital.pub.suzhoumh.fragment.index;

import java.util.ArrayList;

import org.json.JSONArray;

import com.app.tanklib.Preferences;
import com.app.tanklib.bitmap.IndexUrlCache;
import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.pulltorefresh.PullToRefreshBase;
import com.app.tanklib.pulltorefresh.PullToRefreshListView;
import com.app.tanklib.pulltorefresh.PullToRefreshBase.Mode;
import com.app.tanklib.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.app.tanklib.util.AsyncTaskUtil;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.activity.adapter.DynamicAdapter;
import com.bsoft.hospital.pub.suzhoumh.activity.dynamic.DynamicDetailActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.cache.ModelCache;
import com.bsoft.hospital.pub.suzhoumh.model.Comment;
import com.bsoft.hospital.pub.suzhoumh.model.DynamicShow;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.view.FooterView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class DynamicFragment extends BaseFragment {

	PullToRefreshListView mPullRefreshListView;
	ListView listView;
	DynamicAdapter adapter;
	ProgressBar emptyProgress;
	GetDataTask getDataTask;
	FooterView footerView;
	int pageNo = 1;
	int pageSize = 50;
	MoreTask moreTask;
	boolean firstLoad = false;
	ArrayList<DynamicShow> dataList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mainView = inflater.inflate(R.layout.dynamic, container, false);
		return mainView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		this.urlMap = new IndexUrlCache(15);
		findView();
	}

	@Override
	public void startHint() {
		if (isLoaded) {
			return;
		}
		dataList = ModelCache.getInstance().getDynamicShows();
		if (null != dataList && dataList.size() > 0) {
			adapter.addData(dataList);
			firstLoad = true;
		}
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.Dynamic_comment_ACTION);
		getActivity().registerReceiver(this.broadcastReceiver, filter);
		getDataTask = new GetDataTask();
		getDataTask.execute();
		isLoaded = true;
	}

	@Override
	public void endHint() {

	}

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (Constants.Dynamic_comment_ACTION.equals(intent.getAction())) {
				Comment comment = (Comment) intent.getSerializableExtra("vo");
				if (null != comment) {
					adapter.addCommentCount(comment.drid);
				}
			}
		}
	};

	public void findView() {
		findActionBar();
		actionBar.setTitle("动态");
		emptyProgress = (ProgressBar) mainView.findViewById(R.id.emptyProgress);
		mPullRefreshListView = (PullToRefreshListView) mainView
				.findViewById(R.id.pull_refresh_list);
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
		adapter = new DynamicAdapter(baseContext, urlMap);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(baseContext,
						DynamicDetailActivity.class);
				intent.putExtra("vo", adapter.getItem(arg2 - 1));
				startActivity(intent);
			}
		});
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

	@Override
	public void onDestroy() {
		super.onDestroy();
		AsyncTaskUtil.cancelTask(getDataTask);
		AsyncTaskUtil.cancelTask(moreTask);
		if (null != broadcastReceiver && isReceiver) {
			getActivity().unregisterReceiver(broadcastReceiver);
			broadcastReceiver = null;
		}
	}

}
