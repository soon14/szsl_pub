package com.bsoft.hospital.pub.suzhoumh.activity.app.queue;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.pulltorefresh.PullToRefreshBase;
import com.app.tanklib.pulltorefresh.PullToRefreshListView;
import com.app.tanklib.pulltorefresh.PullToRefreshBase.Mode;
import com.app.tanklib.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.app.tanklib.util.AsyncTaskUtil;
import com.app.tanklib.view.BsoftActionBar.Action;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.app.queue.MyQueueActivity.GetDataTask;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.model.app.queue.PDQKVo;
import com.bsoft.hospital.pub.suzhoumh.model.app.queue.QueueVo;

/**
 * 所有排队
 * @author Administrator
 *
 */
public class AllQueueActivity extends BaseActivity{

//	{"message":"","data":[{"ksdm":"1","ksmc":"1","zssl":"3","dqxh":"1","wdxh":"1","ygxm":"1","zdxh":"1"},{"ksdm":"3","ksmc":"3","zssl":"3","dqxh":"3","wdxh":"3","ygxm":"3","zdxh":"3"}],"code":"1"}

	private ListView lvQueue;//标题列表
	private ArrayList<QueueVo> list = new ArrayList<QueueVo>();//所有的排队情况
	private ArrayList<PDQKVo> my_list = new ArrayList<PDQKVo>();//我的排队情况
	
	private QueueTitleAdapter adapter;
	private MyQueueAdapter my_adapter;
	//private QueueDetailAdapter queueadapter;
	
	private GetDataTask getDataTask;
	private GetMyQueueTask getMyQueueTask;
	
	private LinearLayout ll_my_queue;
	private LinearLayout ll_my_queue_empty;
	private LinearLayout ll_parent;
	private LinearLayout ll_queue_info;
	
	private PullToRefreshListView mPullRefreshListView;
	private ListView listview;
	private String cardnum = "";
	//private TextView tv_current_num;
	//private TextView tv_my_num;

	private TextView tv_num;
	private TextView tv_wait;
	private TextView tv_doctor;
	private TextView tv_dept;
	private TextView tv_name;
	private TextView tv_currentname;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.queue_all);
		findView();
		initData();
	}


	@Override
	public void findView() {
		// TODO Auto-generated method stub
		findActionBar();
		actionBar.setTitle("排队叫号");
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
		actionBar.setRefreshTextView("刷新", refreshAction);
		lvQueue = (ListView)findViewById(R.id.lv_queue);
		ll_my_queue = (LinearLayout)findViewById(R.id.ll_my_queue);
		ll_my_queue_empty = (LinearLayout)findViewById(R.id.ll_my_queue_empty);
		ll_parent = (LinearLayout)findViewById(R.id.ll_parent);
		ll_parent.setVisibility(View.GONE);

		tv_currentname = (TextView)findViewById(R.id.tv_current_num);
		tv_name = (TextView)findViewById(R.id.tv_name);
		tv_doctor = (TextView)findViewById(R.id.tv_doctor);
		tv_wait = (TextView)findViewById(R.id.tv_wait);
		tv_num = (TextView)findViewById(R.id.tv_num);
		tv_dept = (TextView)findViewById(R.id.tv_dept);
		ll_queue_info = (LinearLayout)findViewById(R.id.ll_queue_info);
	}
	Action refreshAction = new Action() {
		@Override
		public int getDrawable() {
			return 0;
		}

		@Override
		public void performAction(View view) {
			getMyQueueTask = new GetMyQueueTask();
			getMyQueueTask.execute();
		}
	};
	private void initData()
	{
		adapter = new QueueTitleAdapter(baseContext,list);
		lvQueue.setAdapter(adapter);
		lvQueue.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(AllQueueActivity.this,QueueDetailActivity.class);
				intent.putExtra("position", position);
				startActivity(intent);
			}
			
		});
		my_adapter = new MyQueueAdapter(baseContext,my_list);
		//queueadapter = new QueueDetailAdapter(baseContext, my_list,1);
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
						
						/*getDataTask = new GetDataTask();
						getDataTask.execute();*/
						getMyQueueTask = new GetMyQueueTask();
						getMyQueueTask.execute();

					}
				});
		
		listview = mPullRefreshListView.getRefreshableView();
		listview.setAdapter(my_adapter);
		/*if(loginUser.cards!=null&&loginUser.cards.size()>0)
		{
			cardnum = loginUser.cards.get(0).cardNum;
		}
		getDataTask = new GetDataTask();
		getDataTask.execute();*/
		getMyQueueTask = new GetMyQueueTask();
		getMyQueueTask.execute();
	}
	
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		AsyncTaskUtil.cancelTask(getDataTask);
		AsyncTaskUtil.cancelTask(getMyQueueTask);
	}


	/*class GetDataTask extends AsyncTask<Void, Void, ResultModel<ArrayList<QueueVo>>>
	{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			actionBar.startTextRefresh();
		}

		@Override
		protected void onPostExecute(ResultModel<ArrayList<QueueVo>> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			actionBar.endTextRefresh();
			if (null != result) {
				if (result.statue == Statue.SUCCESS) {
					if (null != result.list && result.list.size() > 0) {
						list = result.list;
						adapter.refresh(list);
						getMyQueueTask = new GetMyQueueTask();
						getMyQueueTask.execute();
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
					"his/pdjh/listSydl",null);
		}
		
	}*/
	
	class GetMyQueueTask extends AsyncTask<Void, Void, ResultModel<ArrayList<PDQKVo>>>
	{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			actionBar.startTextRefresh();
			mPullRefreshListView.setRefreshing();
		}
		@Override
		protected void onPostExecute(ResultModel<ArrayList<PDQKVo>> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			actionBar.endTextRefresh();
			mPullRefreshListView.onRefreshComplete();
			if (null != result) {
				if (result.statue == Statue.SUCCESS) {
					if (null != result.list && result.list.size() > 0) {
						my_list = result.list;
						ll_my_queue_empty.setVisibility(View.GONE);
						ll_queue_info.setVisibility(View.VISIBLE);
						setQueueInfo(my_list.get(0));
//						my_adapter.refresh(my_list);
					}
					else
					{
						ll_my_queue_empty.setVisibility(View.VISIBLE);
						ll_queue_info.setVisibility(View.GONE);
					}
				} else {
					ll_my_queue_empty.setVisibility(View.VISIBLE);
					ll_queue_info.setVisibility(View.GONE);
				}
				ll_parent.setVisibility(View.VISIBLE);
			} 
		}
		@Override
		protected ResultModel<ArrayList<PDQKVo>> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			HashMap<String,String> map = new HashMap<String,String>();
			map.put("method","listwddl");
			map.put("as_sfzh", loginUser.idcard);
			return HttpApi.getInstance().parserArray_His(PDQKVo.class,
					"hiss/ser",map,
					new BsoftNameValuePair("id", loginUser.id),
					new BsoftNameValuePair("sn", loginUser.sn));
		}
	}

	private void setQueueInfo(PDQKVo pdqkVo)
	{
		if (pdqkVo != null)
		{
			tv_currentname.setText(pdqkVo.dqxh+"\t号");//当前序号
			tv_num.setText(pdqkVo.wdxh);//我的序号
			tv_wait.setText(pdqkVo.ddrs);//前面等待
			tv_doctor.setText(pdqkVo.ygxm);
			tv_dept.setText(pdqkVo.ksmc);
			if(loginUser.realname!=null)
			{
				tv_name.setText(loginUser.realname);
			}
		}
	}
}
