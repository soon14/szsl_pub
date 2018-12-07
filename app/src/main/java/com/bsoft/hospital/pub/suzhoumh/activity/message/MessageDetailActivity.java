package com.bsoft.hospital.pub.suzhoumh.activity.message;

import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.adapter.MessageDetailAdapter;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.Message;
import com.bsoft.hospital.pub.suzhoumh.model.MessageDetail;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.view.FooterView;
import com.app.tanklib.view.BsoftActionBar.Action;
import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.pulltorefresh.PullToRefreshBase;
import com.app.tanklib.pulltorefresh.PullToRefreshListView;
import com.app.tanklib.pulltorefresh.PullToRefreshBase.Mode;
import com.app.tanklib.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.app.tanklib.util.AsyncTaskUtil;

/**
 * @author Tank E-mail:zkljxq@126.com
 * 
 * @类说明
 */
public class MessageDetailActivity extends BaseActivity {
	PullToRefreshListView mPullRefreshListView;
	ListView listView;
	MessageDetailAdapter adapter;
	ProgressBar emptyProgress;
	GetDataTask getDataTask;
	FooterView footerView;
	int pageNo = 1;
	int pageSize = 20;
	MoreTask moreTask;
	Message messageVo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_detail);
		messageVo = (Message) getIntent().getSerializableExtra("vo");
		findView();
		getDataTask = new GetDataTask();
		getDataTask.execute();
	}

	@Override
	public void findView() {
		findActionBar();
		actionBar.setTitle(messageVo.getName());
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
		adapter = new MessageDetailAdapter(this, messageVo.kinds);
		listView.setAdapter(adapter);
	}

	private class GetDataTask extends
			AsyncTask<Void, Void, ResultModel<ArrayList<MessageDetail>>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// emptyProgress.setVisibility(View.VISIBLE);
			actionBar.startTextRefresh();;
			mPullRefreshListView.setRefreshing();
		}

		@Override
		protected ResultModel<ArrayList<MessageDetail>> doInBackground(
				Void... params) {
			return HttpApi.getInstance().parserArray(
					MessageDetail.class,
					"auth/msg/detail",
					new BsoftNameValuePair("id", loginUser.id),
					new BsoftNameValuePair("kinds", String
							.valueOf(messageVo.kinds)),
					new BsoftNameValuePair("start", String.valueOf(pageNo)),
					new BsoftNameValuePair("length", String.valueOf(pageSize)),
					new BsoftNameValuePair("sn", loginUser.sn));
		}

		@Override
		protected void onPostExecute(
				ResultModel<ArrayList<MessageDetail>> result) {
			super.onPostExecute(result);
			if (null != result) {
				if (result.statue == Statue.SUCCESS) {
					if (null != result.list && result.list.size() > 0) {
						adapter.addData(result.list);
						if (result.list.size() == pageSize) {
							listView.addFooterView(footerView);
						}
						/*Intent intent = new Intent(
								Constants.MessageCount_ACTION);
						intent.putExtra("kinds", messageVo.kinds);
						sendBroadcast(intent);
						AppApplication.messageCount = AppApplication.messageCount
								- messageVo.count;
						sendBroadcast(new Intent(
								Constants.HomeMessageCount_ACTION));*/
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

	private class MoreTask extends
			AsyncTask<Void, Void, ResultModel<ArrayList<MessageDetail>>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			actionBar.startTextRefresh();;
		}

		@Override
		protected ResultModel<ArrayList<MessageDetail>> doInBackground(
				Void... params) {
			return HttpApi.getInstance().parserArray(
					MessageDetail.class,
					"auth/msg/detail",
					new BsoftNameValuePair("id", loginUser.id),
					new BsoftNameValuePair("kinds", String
							.valueOf(messageVo.kinds)),
					new BsoftNameValuePair("start", String.valueOf(pageNo)),
					new BsoftNameValuePair("length", String.valueOf(pageSize)),
					new BsoftNameValuePair("sn", loginUser.sn));
		}

		@Override
		protected void onPostExecute(
				ResultModel<ArrayList<MessageDetail>> result) {
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
	}
}
