package com.bsoft.hospital.pub.suzhoumh.activity.app.news;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.pulltorefresh.PullToRefreshBase;
import com.app.tanklib.pulltorefresh.PullToRefreshBase.Mode;
import com.app.tanklib.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.app.tanklib.pulltorefresh.PullToRefreshListView;
import com.app.tanklib.util.AsyncTaskUtil;
import com.app.tanklib.view.BsoftActionBar.Action;
import com.bsoft.hospital.pub.suzhoumh.AppApplication;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;

/**
 * 健康资讯
 * @author Administrator
 *
 */
public class NewsListActivity extends BaseActivity{

	private LinearLayout ll_titles;
	private ArrayList<NewsTitle> titlelist = new ArrayList<NewsTitle>();
	private PullToRefreshListView mPullRefreshListView;
	private GetTitleTask getTitleTask;
	private ListView listView;
	private GetDataTask getDataTask;
	
	private ArrayList<NewsItem> currentlist = new ArrayList<NewsItem>();//当前资讯列表
	private HashMap<String,ArrayList<NewsItem>> map = new HashMap<String,ArrayList<NewsItem>>();
	private NewItemAdapter adapter;
	private String currentId = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_list);
		findView();
		initTitle();
		initData();
	}

	@Override
	public void findView() {
		// TODO Auto-generated method stub
		findActionBar();
		actionBar.setTitle("健康资讯");
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
		
		ll_titles = (LinearLayout)findViewById(R.id.ll_titles);
		
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
						getDataTask.execute(currentId);
					}
				});
		getTitleTask = new GetTitleTask();
		getTitleTask.execute();
		
		adapter = new NewItemAdapter(baseContext,currentlist);
		listView = mPullRefreshListView.getRefreshableView();
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent= new Intent(NewsListActivity.this,NewsDetailActivity.class);
				intent.putExtra("newsitem", currentlist.get(position-1));
				startActivity(intent);
			}
			
		});
	}

	/**
	 * 获取标题
	 * @author Administrator
	 *
	 */
	private class GetTitleTask extends AsyncTask<Void, Void, ResultModel<ArrayList<NewsTitle>>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			actionBar.startTextRefresh();
		}

		@Override
		protected ResultModel<ArrayList<NewsTitle>> doInBackground(Void... params) {
			return HttpApi.getInstance().parserArray(NewsTitle.class,
					"auth/satisfaction/getConsultativeType",
					new BsoftNameValuePair("id", loginUser.id),
					new BsoftNameValuePair("sn", loginUser.sn)
					);
		}

		@Override
		protected void onPostExecute(ResultModel<ArrayList<NewsTitle>> result) {
			super.onPostExecute(result);
			if (null != result) {
				if (result.statue == Statue.SUCCESS) {
					titlelist = result.list;
					initTitle();
				} else {
					result.showToast(baseContext);
				}
			} else {
				Toast.makeText(baseContext, "加载失败", Toast.LENGTH_SHORT).show();
			}
			actionBar.endTextRefresh();
		}
	} 
	
	/**
	 * 获取详情
	 * @author Administrator
	 *
	 */
	private class GetDataTask extends AsyncTask<String, Void, ResultModel<ArrayList<NewsItem>>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			actionBar.startTextRefresh();
			mPullRefreshListView.setRefreshing();
		}

		@Override
		protected ResultModel<ArrayList<NewsItem>> doInBackground(String... params) {
			return HttpApi.getInstance().parserArray(NewsItem.class,
					"auth/dynamic/list",
					new BsoftNameValuePair("id", loginUser.id),
					new BsoftNameValuePair("sn", loginUser.sn),
					new BsoftNameValuePair("dtype",params[0])
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
						map.put(currentId, currentlist);
						/*if(!map.containsKey(currentId))
						{
							map.put(currentId, currentlist);
						}*/
					}
					else
					{	currentlist = new ArrayList<NewsItem>();
						Toast.makeText(baseContext, result.message, Toast.LENGTH_SHORT).show();
					}
					adapter.refresh(currentlist);
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
	
	/**
	 * 初始化标题
	 */
	private void initTitle()
	{
		if(titlelist == null || titlelist.size() == 0)return;
		boolean b = titlelist.size() > 3;
		int width = AppApplication.getWidthPixels() / titlelist.size();
		for (NewsTitle vo : titlelist) {
			final Button view = new Button(this);
			view.setPadding(10, 5, 10, 5);
			view.setText(vo.title);
			view.setBackgroundResource(R.drawable.btn_survey_main_unpress);
			view.setTag(vo.iid);
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					for (NewsTitle vos : titlelist) {
						if (vos.iid.equals(view.getTag().toString())) {
							ll_titles.findViewWithTag(vos.iid)
									.setBackgroundResource(
											R.drawable.btn_survey_main_press);
							TextView tv=(TextView)ll_titles.findViewWithTag(vos.iid);
							tv.setTextColor(getResources().getColor(R.color.color_news_title));
						} else {
							ll_titles.findViewWithTag(vos.iid)
									.setBackgroundResource(
											R.drawable.btn_survey_main_unpress);
							TextView tv=(TextView)ll_titles.findViewWithTag(vos.iid);
							tv.setTextColor(getResources().getColor(R.color.black));
						}
					}
					currentId = view.getTag().toString();
					currentlist = map.get(view
							.getTag().toString());
					if (null != currentlist && currentlist.size() > 0) {
						adapter.refresh(currentlist);
					} else {
						getDataTask = new GetDataTask();
						getDataTask.execute(view.getTag().toString());
					}
				}
			});
			ll_titles.addView(view, b ? LayoutParams.WRAP_CONTENT : width,
					LayoutParams.WRAP_CONTENT);
			//初始化第一个点击
			TextView tv =(TextView)ll_titles.findViewWithTag(titlelist.get(0).iid);
			tv.setTextColor(getResources().getColor(R.color.color_news_title));
			tv.setBackgroundResource(R.drawable.btn_survey_main_press);
			getDataTask = new GetDataTask();
			currentId = titlelist.get(0).iid;
			getDataTask.execute(currentId);
		}
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		AsyncTaskUtil.cancelTask(getDataTask);
		AsyncTaskUtil.cancelTask(getTitleTask);
	}
}
