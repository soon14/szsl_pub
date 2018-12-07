package com.bsoft.hospital.pub.suzhoumh.activity.app.news;

import java.util.ArrayList;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.pulltorefresh.PullToRefreshBase;
import com.app.tanklib.pulltorefresh.PullToRefreshBase.Mode;
import com.app.tanklib.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.app.tanklib.pulltorefresh.PullToRefreshListView;
import com.app.tanklib.util.AsyncTaskUtil;
import com.app.tanklib.view.BsoftActionBar.Action;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;

/**
 * 院内通知
 * @author Administrator
 *
 */
public class HospitalNewsActivity extends BaseActivity{

	private ListView listView;
	private GetDataTask getDataTask;
	
	private ArrayList<NewsItem> currentlist = new ArrayList<NewsItem>();//资讯内容
	private PullToRefreshListView mPullRefreshListView;
	private NewItemAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.hospital_news);
		findView();
		initData();
	}

	@Override
	public void findView() {
		// TODO Auto-generated method stub
		findActionBar();
		actionBar.setTitle("通知公告");
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
						getDataTask = new GetDataTask();
						getDataTask.execute();

					}
				});
		adapter = new NewItemAdapter(baseContext,currentlist);
		listView = mPullRefreshListView.getRefreshableView();
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent= new Intent(HospitalNewsActivity.this,NewsDetailActivity.class);
				intent.putExtra("newsitem", currentlist.get(position-1));
				startActivity(intent);
			}
			
		});
		getDataTask = new GetDataTask();
		getDataTask.execute();
	}
	
	/**
	 * 获取数据
	 * @author Administrator
	 *
	 */
	private class GetDataTask extends AsyncTask<Void, Void, ResultModel<ArrayList<NewsItem>>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			actionBar.startTextRefresh();
			mPullRefreshListView.setRefreshing();
		}

		@Override
		protected ResultModel<ArrayList<NewsItem>> doInBackground(Void... params) {
			return HttpApi.getInstance().parserArray(NewsItem.class,
					"auth/dynamic/list",
					new BsoftNameValuePair("id", loginUser.id),
					new BsoftNameValuePair("sn", loginUser.sn),
					new BsoftNameValuePair("dtype","2")
					);
		}

		@Override
		protected void onPostExecute(ResultModel<ArrayList<NewsItem>> result) {
			super.onPostExecute(result);
			if (null != result) {
				if (result.statue == Statue.SUCCESS) {
					if(result.list!=null)
					{
						currentlist = result.list;
						adapter.refresh(currentlist);
					}
					else
					{
						Toast.makeText(baseContext, result.message, Toast.LENGTH_SHORT).show();
					}
				} else {
					result.showToast(baseContext);
				}
			} else {
				Toast.makeText(baseContext, "加载失败", Toast.LENGTH_SHORT).show();
			}
			actionBar.endTextRefresh();
			mPullRefreshListView.onRefreshComplete();
		}

	} 
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		AsyncTaskUtil.cancelTask(getDataTask);
		//清楚imageloader的缓存
		adapter.imageLoader.clearMemoryCache();
		adapter.imageLoader.clearDiskCache();
	}

}
