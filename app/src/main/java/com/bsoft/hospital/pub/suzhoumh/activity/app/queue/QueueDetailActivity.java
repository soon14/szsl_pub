package com.bsoft.hospital.pub.suzhoumh.activity.app.queue;

import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ListView;

import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.pulltorefresh.PullToRefreshBase;
import com.app.tanklib.pulltorefresh.PullToRefreshBase.Mode;
import com.app.tanklib.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.app.tanklib.pulltorefresh.PullToRefreshListView;
import com.app.tanklib.view.BsoftActionBar.Action;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.model.app.queue.PDQKVo;
import com.bsoft.hospital.pub.suzhoumh.model.app.queue.QueueVo;

/**
 * 排队情况详情
 * @author Administrator
 *
 */
public class QueueDetailActivity extends BaseActivity{

	private PullToRefreshListView mPullRefreshListView;
	private ListView listview;
	private QueueDetailAdapter detailAdapter;
	private ArrayList<QueueVo> parent_list = new ArrayList<QueueVo>();//屏幕列表
	private ArrayList<PDQKVo> pdqk_list = new ArrayList<PDQKVo>();//情况列表
	private GetDataTask getDataTask;
	private int position = 0;//当前要显示哪块屏幕
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.queue_detail);
		findView();
		initData();
	}

	@Override
	public void findView() {
		// TODO Auto-generated method stub
		findActionBar();
		actionBar.setTitle("排队情况");
		actionBar.setBackAction(new Action(){

			@Override
			public int getDrawable() {
				// TODO Auto-generated method stub
				return R.drawable.btn_back;
			}

			@Override
			public void performAction(View view) {
				// TODO Auto-generated method stub
				finish();
			}
			
		});
	}
	
	private void initData()
	{
		detailAdapter = new QueueDetailAdapter(baseContext, pdqk_list,0);
		position = getIntent().getIntExtra("position", 0);
		
		mPullRefreshListView = (PullToRefreshListView)findViewById(R.id.pull_refresh_list);
		mPullRefreshListView.setMode(Mode.PULL_FROM_START);
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
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
						
						getDataTask = new GetDataTask();
						getDataTask.execute();

					}
				});
		
		listview = mPullRefreshListView.getRefreshableView();
		listview.setDividerHeight(0);
		listview.setAdapter(detailAdapter);
		
		getDataTask = new GetDataTask();
		getDataTask.execute();
	}
	

	class GetDataTask extends AsyncTask<Void, Void, ResultModel<ArrayList<QueueVo>>>
	{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			actionBar.startTextRefresh();
			mPullRefreshListView.setRefreshing();
		}
		@Override
		protected void onPostExecute(ResultModel<ArrayList<QueueVo>> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			actionBar.endTextRefresh();
			mPullRefreshListView.onRefreshComplete();
			if (null != result) {
				if (result.statue == Statue.SUCCESS) {
					if (null != result.list && result.list.size() > 0) {
						parent_list = result.list;
						pdqk_list = parent_list.get(position).pdqk;
						detailAdapter.refresh(pdqk_list);
					} 
				} else {
					result.showToast(baseContext);
				}
			} 
		}
		@Override
		protected ResultModel<ArrayList<QueueVo>> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			return HttpApi.getInstance().parserArray_His(QueueVo.class,
					"his/pdjh/listSydl",null,
					new BsoftNameValuePair("id", loginUser.id),
					new BsoftNameValuePair("sn", loginUser.sn));
		}
	}
	
}
