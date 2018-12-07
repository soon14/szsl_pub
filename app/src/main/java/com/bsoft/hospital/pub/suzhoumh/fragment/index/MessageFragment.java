package com.bsoft.hospital.pub.suzhoumh.fragment.index;

import java.util.ArrayList;

import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.pulltorefresh.PullToRefreshBase;
import com.app.tanklib.pulltorefresh.PullToRefreshListView;
import com.app.tanklib.pulltorefresh.PullToRefreshBase.Mode;
import com.app.tanklib.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.app.tanklib.util.AsyncTaskUtil;
import com.app.tanklib.view.TipsView;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.activity.adapter.MessageAdapter;
import com.bsoft.hospital.pub.suzhoumh.activity.message.MessageDetailActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.Message;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.push.PushInfo;

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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MessageFragment extends BaseFragment {

	PullToRefreshListView mPullRefreshListView;
	ListView listView;
	MessageAdapter adapter;
	TipsView tipsView;
	ProgressBar emptyProgress;
	GetDataTask getDataTask;

	boolean firstLoad = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mainView = inflater.inflate(R.layout.message, container, false);
		return mainView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		findView();
	}

	public void setTipsView(TipsView tipsView) {
		this.tipsView = tipsView;
		if (null != adapter) {
			adapter.notifyDataSetChanged();
		}
	}

	public TipsView getTipsView() {
		return tipsView;
	}

	public ListView getListView() {
		return listView;
	}

	@Override
	public void startHint() {
		if (isLoaded) {
			return;
		}
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.MessageCount_ACTION);
		filter.addAction(Constants.MessageCountClear_ACTION);
		filter.addAction(Constants.PushMessage_ACTION);
		filter.addAction(Constants.MessageHome_ACTION);
		baseContext.registerReceiver(this.broadcastReceiver, filter);
		getDataTask = new GetDataTask();
		getDataTask.execute();
		isLoaded = true;
	}

	@Override
	public void endHint() {
		// TODO Auto-generated method stub

	}

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (Constants.MessageCount_ACTION.equals(intent.getAction())) {
				int kinds = intent.getIntExtra("kinds", -1);
				if (kinds > -1) {
					adapter.changeCount(kinds);
				}
			} else if (Constants.MessageCountClear_ACTION.equals(intent
					.getAction())) {
				adapter.clearCount();

			} else if (Constants.PushMessage_ACTION.equals(intent.getAction())) {
				// 消息推送过来，改数字及内容
				PushInfo pushInfo = (PushInfo) intent
						.getSerializableExtra("pushInfo");
				if (null != pushInfo) {
					adapter.doPush(pushInfo);
				}
			} else if (Constants.MessageHome_ACTION.equals(intent.getAction())) {
				getDataTask = new GetDataTask();
				getDataTask.execute();
			}
		}
	};

	public void findView() {
		findActionBar();
		actionBar.setTitle("消息");
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

						emptyProgress.setVisibility(View.GONE);
						getDataTask = new GetDataTask();
						getDataTask.execute();
					}
				});
		listView = mPullRefreshListView.getRefreshableView();
		adapter = new MessageAdapter(baseContext, this);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(baseContext,
						MessageDetailActivity.class);
				intent.putExtra("vo", adapter.getItem(arg2 - 1));
				startActivity(intent);
				adapter.clearCount();
			}
		});
	}

	private class GetDataTask extends
			AsyncTask<Void, Void, ResultModel<ArrayList<Message>>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			actionBar.startTextRefresh();;
			// if (!firstLoad) {
			// emptyProgress.setVisibility(View.VISIBLE);
			// }
			mPullRefreshListView.setRefreshing();
		}

		@Override
		protected ResultModel<ArrayList<Message>> doInBackground(Void... params) {
			return HttpApi.getInstance().parserArray(Message.class,
					"auth/msg/list",
					new BsoftNameValuePair("id", loginUser.id),
					new BsoftNameValuePair("sn", loginUser.sn));
		}

		@Override
		protected void onPostExecute(ResultModel<ArrayList<Message>> result) {
			super.onPostExecute(result);
			if (null != result) {
				if (result.statue == Statue.SUCCESS) {
					if (null != result.list && result.list.size() > 0) {
						adapter.addData(result.list);
						firstLoad = true;
					} 
				} else {
					result.showToast(baseContext);
				}
			} else {
				Toast.makeText(baseContext, "加载失败", Toast.LENGTH_SHORT).show();
			}
			// emptyProgress.setVisibility(View.GONE);
			actionBar.endTextRefresh();;
			mPullRefreshListView.onRefreshComplete();
		}
	}

	public void refreshData()
	{
		getDataTask = new GetDataTask();
		getDataTask.execute();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		AsyncTaskUtil.cancelTask(getDataTask);
		if (null != this.broadcastReceiver && isReceiver) {
			baseContext.unregisterReceiver(this.broadcastReceiver);
			broadcastReceiver = null;
		}
	}
}
