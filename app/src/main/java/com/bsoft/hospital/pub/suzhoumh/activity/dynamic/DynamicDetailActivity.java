package com.bsoft.hospital.pub.suzhoumh.activity.dynamic;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tanklib.view.BsoftActionBar.Action;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.AppApplication;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.activity.adapter.CommentAdapter;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.Comment;
import com.bsoft.hospital.pub.suzhoumh.model.DynamicShow;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.util.DateUtil;
import com.bsoft.hospital.pub.suzhoumh.view.FooterView;
import com.app.tanklib.bitmap.CacheManage;
import com.app.tanklib.bitmap.IndexUrlCache;
import com.app.tanklib.bitmap.view.ProgressImageView;
import com.app.tanklib.bitmap.view.RoundImageView;
import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.pulltorefresh.PullToRefreshBase;
import com.app.tanklib.pulltorefresh.PullToRefreshBase.Mode;
import com.app.tanklib.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.app.tanklib.pulltorefresh.PullToRefreshListView;
import com.app.tanklib.util.AsyncTaskUtil;
import com.app.tanklib.util.StringUtil;

/**
 * @author Tank E-mail:zkljxq@126.com
 * 
 * @类说明
 */
public class DynamicDetailActivity extends BaseActivity {

	ProgressBar emptyProgress;
	GetDataTask getDataTask;
	PullToRefreshListView mPullRefreshListView;
	ListView listView;
	CommentAdapter adapter;
	View header;
	LayoutInflater mLayoutInflater;

	ProgressImageView progressImageView;
	RoundImageView headerImage;

	MoreTask moreTask;
	FooterView footerView;
	int pageNo = 1;
	int pageSize = 50;

	DynamicShow vo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dynamic_detail);
		this.mLayoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		vo = (DynamicShow) getIntent().getSerializableExtra("vo");
		urlMap = new IndexUrlCache();
		findView();
		setClick();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.Dynamic_comment_ACTION);
		this.registerReceiver(this.broadcastReceiver, filter);
		getDataTask = new GetDataTask();
		getDataTask.execute();
	}

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (Constants.Dynamic_comment_ACTION.equals(intent.getAction())) {
				Comment comment = (Comment) intent.getSerializableExtra("vo");
				if (null != comment) {
					adapter.addData(comment);
					vo.replycount = vo.replycount + 1;
					((TextView) header.findViewById(R.id.commentCount))
							.setText("评论 (" + vo.replycount + ")");
				}
			}
		}
	};

	void setClick() {
	}

	@Override
	public void findView() {
		findActionBar();
		actionBar.setTitle("动态详情");
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
		actionBar.setRefreshTextView("评论", new Action() {

			@Override
			public void performAction(View view) {
				Intent intent = new Intent(baseContext,
						DynamicCommentActivity.class);
				intent.putExtra("vo", vo);
				startActivity(intent);
			}

			@Override
			public int getDrawable() {
				return 0;
			}
		});
		emptyProgress = (ProgressBar) findViewById(R.id.emptyProgress);
		initHeader();
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
		listView = mPullRefreshListView.getRefreshableView();
		footerView = new FooterView(this);
		footerView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				pageNo++;
				moreTask = new MoreTask();
				moreTask.execute();
			}
		});
		adapter = new CommentAdapter(this);
		listView.addHeaderView(header);
		listView.setAdapter(adapter);
	}

	void initHeader() {
		if (null != vo) {
			header = mLayoutInflater.inflate(R.layout.dynamic_detail_header,
					null);
			((TextView) header.findViewById(R.id.time)).setText(DateUtil
					.getDateTime(vo.createdate));
			if (vo.uid == 1) {
				((TextView) header.findViewById(R.id.name)).setText("健康助手");
			} else {
				((TextView) header.findViewById(R.id.name))
						.setText(vo.realname);
			}
			((TextView) header.findViewById(R.id.content)).setText(vo.content);
			((TextView) header.findViewById(R.id.commentCount)).setText("评论 ("
					+ vo.replycount + ")");
			if (!StringUtil.isEmpty(vo.imgurl)) {
				progressImageView = (ProgressImageView) header
						.findViewById(R.id.progressImageView);
				progressImageView.setVisibility(View.VISIBLE);

				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				params.width = AppApplication.getWidthPixels();
				params.height = AppApplication.getWidthPixels() * 2 / 3;
				if (null != progressImageView.getImageView()) {
					progressImageView.getImageView().getDrawable()
							.setCallback(null);
				}
				progressImageView.setLatout(params);
				progressImageView.mIgImageView.resertStates();
				progressImageView.setImageUrl(HttpApi.getImageUrl(vo.imgurl,
						CacheManage.IMAGE_TYPE_PROGRESS));

				urlMap.add(-1, HttpApi.getImageUrl(vo.imgurl,
						CacheManage.IMAGE_TYPE_PROGRESS));
			}

			headerImage = (RoundImageView) header.findViewById(R.id.header);
			headerImage.resertStates();
			if (!StringUtil.isEmpty(vo.header)) {
				if (vo.uid == 1) {
					headerImage.setImageUrl(HttpApi.getImageUrl(vo.header,
							CacheManage.IMAGE_TYPE_HEADER),
							CacheManage.IMAGE_TYPE_HEADER,
							R.drawable.admin_header);
				} else {
					headerImage.setImageUrl(HttpApi.getImageUrl(vo.header,
							CacheManage.IMAGE_TYPE_HEADER),
							CacheManage.IMAGE_TYPE_HEADER,
							R.drawable.doc_header);
				}

				// // 加入图片管理
				urlMap.add(-1, HttpApi.getImageUrl(vo.header,
						CacheManage.IMAGE_TYPE_HEADER));
			} else {
				if (vo.uid == 1) {
					headerImage.setImageResource(R.drawable.admin_header);
				} else {
					headerImage.setImageResource(R.drawable.doc_header);
				}
			}
		}
	}

	private class GetDataTask extends
			AsyncTask<Void, Void, ResultModel<ArrayList<Comment>>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			actionBar.startTextRefresh();;
		}

		@Override
		protected ResultModel<ArrayList<Comment>> doInBackground(Void... params) {
			return HttpApi.getInstance().parserArray(Comment.class,
					"auth/dynamic/reply/list",
					new BsoftNameValuePair("drid", vo.drid),
					new BsoftNameValuePair("start", String.valueOf(pageNo)),
					new BsoftNameValuePair("length", String.valueOf(pageSize)),
					new BsoftNameValuePair("sn", loginUser.sn),
					new BsoftNameValuePair("id", loginUser.id));
		}

		@Override
		protected void onPostExecute(ResultModel<ArrayList<Comment>> result) {
			super.onPostExecute(result);
			if (null != result) {
				if (result.statue == Statue.SUCCESS) {
					if (null != result.list && result.list.size() > 0) {
						adapter.addData(result.list);
						if (result.list.size() == pageSize) {
							listView.addFooterView(footerView);
						}
					} 
//					else {
//						 Toast.makeText(baseContext, "评论数据为空",
//						 Toast.LENGTH_SHORT).show();
//					}
				} else {
					result.showToast(baseContext);
				}
			} else {
				Toast.makeText(baseContext, "评论加载失败", Toast.LENGTH_SHORT)
						.show();
			}
			actionBar.endTextRefresh();;
			mPullRefreshListView.onRefreshComplete();
		}
	}

	private class MoreTask extends
			AsyncTask<Void, Void, ResultModel<ArrayList<Comment>>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			actionBar.startTextRefresh();;
		}

		@Override
		protected ResultModel<ArrayList<Comment>> doInBackground(Void... params) {
			return HttpApi.getInstance().parserArray(Comment.class,
					"auth/dynamic/reply/list",
					new BsoftNameValuePair("id", loginUser.id),
					new BsoftNameValuePair("drid", vo.drid),
					new BsoftNameValuePair("start", String.valueOf(pageNo)),
					new BsoftNameValuePair("length", String.valueOf(pageSize)),
					new BsoftNameValuePair("sn", loginUser.sn));
		}

		@Override
		protected void onPostExecute(ResultModel<ArrayList<Comment>> result) {
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
		if (null != broadcastReceiver) {
			unregisterReceiver(broadcastReceiver);
			broadcastReceiver = null;
		}
	}

}
