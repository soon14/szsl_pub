package com.bsoft.hospital.pub.suzhoumh.activity.app.queue;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ListView;

import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.pulltorefresh.PullToRefreshBase;
import com.app.tanklib.pulltorefresh.PullToRefreshListView;
import com.app.tanklib.pulltorefresh.PullToRefreshBase.Mode;
import com.app.tanklib.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.app.tanklib.util.AsyncTaskUtil;
import com.app.tanklib.view.BsoftActionBar.Action;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.model.app.queue.PDQKVo;

/**
 * 我的排队
 * @author Administrator
 *
 */
public class MyQueueActivity extends BaseActivity{

	private PullToRefreshListView mPullRefreshListView;
	private ListView listview;
	private QueueDetailAdapter detailAdapter;
	private ArrayList<PDQKVo> list = new ArrayList<PDQKVo>();
	private GetDataTask getDataTask;
	
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
		actionBar.setTitle("我的排队");
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
		detailAdapter = new QueueDetailAdapter(baseContext, list,1);
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
		listview.setAdapter(detailAdapter);
		
		getDataTask = new GetDataTask();
		getDataTask.execute();
	}
	
	class GetDataTask extends AsyncTask<Void, Void, ResultModel<ArrayList<PDQKVo>>>
	{

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			actionBar.startTextRefresh();
			mPullRefreshListView.setRefreshing();
		}
		@Override
		protected void onPostExecute(ResultModel<ArrayList<PDQKVo>> result) {
			super.onPostExecute(result);
			actionBar.endTextRefresh();
			mPullRefreshListView.onRefreshComplete();
			if (null != result) {
				if (result.statue == Statue.SUCCESS) {
					if (null != result.list && result.list.size() > 0) {
						list = result.list;
						detailAdapter.refresh(list);
					} 
				} else {
					result.showToast(baseContext);
				}
			} 
		}
		@Override
		protected ResultModel<ArrayList<PDQKVo>> doInBackground(Void... params) {
			HashMap<String,String> map = new HashMap<String,String>();
			map.put("jzkh", "123456");//做测试
			return HttpApi.getInstance().parserArray_His(PDQKVo.class,
					"his/pdjh/listWddl",map,
					new BsoftNameValuePair("id", loginUser.id),
					new BsoftNameValuePair("sn", loginUser.sn));
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		AsyncTaskUtil.cancelTask(getDataTask);
	}
	
}
