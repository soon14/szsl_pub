package com.bsoft.hospital.pub.suzhoumh.fragment.index;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.pulltorefresh.PullToRefreshBase;
import com.app.tanklib.pulltorefresh.PullToRefreshBase.Mode;
import com.app.tanklib.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.app.tanklib.pulltorefresh.PullToRefreshListView;
import com.app.tanklib.util.AsyncTaskUtil;
import com.app.tanklib.view.BsoftActionBar.Action;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.AppApplication;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.activity.consult.ConsultDetailActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.consult.SelectDeptActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.model.consult.ConsultInfo;
import com.bsoft.hospital.pub.suzhoumh.util.DateUtil;

/**
 * 咨询
 * @author Administrator
 *
 */
public class ConsultFragment extends BaseFragment{

	private PullToRefreshListView mPullRefreshListView;
	private ListView listview;
	private List<ConsultInfo> list;
	private ConsultAdapter adapter;
	private int pageNo = 1;
	private int pageSize = 100;
	private GetDataTask task;
	
 	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		findView();
		initData();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mainView = inflater.inflate(R.layout.consult_list, null);
		return mainView;
	}

	public void findView()
	{
		findActionBar();
		actionBar.setTitle("咨询");
		actionBar.setRefreshTextView("我要咨询", new Action(){

			@Override
			public int getDrawable() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public void performAction(View view) {
				// TODO Auto-generated method stub
				if(loginUser!=null&&loginUser.realname!=null)
				{
					Intent intent = new Intent(baseContext,
							SelectDeptActivity.class);
					startActivity(intent);
				}
				else
				{
					Toast.makeText(baseContext, "请先完善个人信息", Toast.LENGTH_SHORT).show();
				}
			}
			
		});
	}
	
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (Constants.CONSULT_ACTION.equals(intent.getAction())) {
				System.out.println("接收到广播");
				pageNo=1;
				task = new GetDataTask();
				task.execute();
			}
		}
	};
	
	public void refreshData()
	{
		pageNo=1;
		task = new GetDataTask();
		task.execute();
	}
	
	private void initData()
	{
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.CONSULT_ACTION);
		baseContext.registerReceiver(broadcastReceiver, filter);
		
		adapter = new ConsultAdapter();
		list = new ArrayList<ConsultInfo>();
		
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
						pageNo=1;
						task = new GetDataTask();
						task.execute();
					}
				});
		listview = mPullRefreshListView.getRefreshableView();
		//是listview的最后一项有分割线显示
		View footview = new View(baseContext);
		listview.addFooterView(footview,null,true);
		
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(baseContext,ConsultDetailActivity.class);
				intent.putExtra("id", list.get(position-1).id);
				intent.putExtra("docname", list.get(position-1).docname);
				intent.putExtra("deptname", list.get(position-1).deptname);
				intent.putExtra("title", list.get(position-1).title);
				intent.putExtra("content", list.get(position-1).content);
				intent.putExtra("sexcode", list.get(position-1).sexcode);
				intent.putExtra("time", DateUtil.getDateTime("yyyy-MM-dd HH:mm:ss", Long.parseLong(list.get(position-1).creattime)));
				startActivity(intent);
			}
			
		});
		
		task = new GetDataTask();
		task.execute();
		
	}
	class GetDataTask extends AsyncTask<Void, Void, ResultModel<ArrayList<ConsultInfo>>>
	{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			actionBar.startTextRefresh();
			mPullRefreshListView.setRefreshing();
		}
		@Override
		protected void onPostExecute(ResultModel<ArrayList<ConsultInfo>> result) {
			// TODO Auto-generated method stub
			actionBar.endTextRefresh();
			mPullRefreshListView.onRefreshComplete();
			if (null != result) {
				if (result.statue == Statue.SUCCESS) {
					if (null != result.list && result.list.size() > 0) {
						list = result.list;
						setMessageCount();
						adapter.notifyDataSetChanged();
					}
				} else {
					result.showToast(baseContext);
				}
			} else {
				Toast.makeText(baseContext, "没有咨询记录", Toast.LENGTH_SHORT)
						.show();
			}
		}
		@Override
		protected ResultModel<ArrayList<ConsultInfo>> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			return HttpApi.getInstance().parserArray(
					ConsultInfo.class, 
					"auth/interaction/getmasterexsbyuid",
					new BsoftNameValuePair("pageNo",String.valueOf(pageNo)),
					new BsoftNameValuePair("pageSize",String.valueOf(pageSize)),
					new BsoftNameValuePair("sn", loginUser.sn),
					new BsoftNameValuePair("id", loginUser.id));
		}
	}

	/**
	 * 设置未读消息数
	 */
	private void setMessageCount()
	{
		AppApplication.messageCount = 0;
		for(int i=0;i<list.size();i++)
		{
			if(list.get(i).isRead.equals("0"))
			{
				AppApplication.messageCount++;
				break;
			}
			
		}
		Intent intent = new Intent();
		intent.setAction(Constants.HomeMessageCount_ACTION);
		baseContext.sendBroadcast(intent);
	}
	class ConsultAdapter extends BaseAdapter
	{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			if(convertView == null)
			{
				holder = new ViewHolder();
				convertView = LayoutInflater.from(baseContext).inflate(R.layout.consult_list_item, null);
				holder.iv_head = (ImageView)convertView.findViewById(R.id.iv_head);
				holder.tv_name = (TextView)convertView.findViewById(R.id.tv_name);
				holder.tv_ks = (TextView)convertView.findViewById(R.id.tv_ks);
				holder.tv_time = (TextView)convertView.findViewById(R.id.tv_time);
				holder.tv_content = (TextView)convertView.findViewById(R.id.tv_content);
				holder.iv_count = (ImageView)convertView.findViewById(R.id.iv_count);
				convertView.setTag(holder);
			}
			else
			{
				holder = (ViewHolder)convertView.getTag();
			}
			if(list.get(position).isRead.equals("0"))//未读
			{
				holder.iv_count.setVisibility(View.VISIBLE);
			}
			else
			{
				holder.iv_count.setVisibility(View.GONE);
			}
			
			if(list.get(position).sexcode!=null)
			{
				if(list.get(position).sexcode.equals("1"))
				{
					holder.iv_head.setBackgroundResource(R.drawable.doc_male);
				}
				else
				{
					holder.iv_head.setBackgroundResource(R.drawable.doc_female);
				}
			}
			
			holder.tv_name.setText(list.get(position).docname);
			holder.tv_content.setText(list.get(position).title);
			//holder.tv_time.setText(list.get(position).endAnswerTime.replace(".0", ""));
			holder.tv_time.setText(DateUtil.getDayAndTime(Long.parseLong(list.get(position).endAnswerTime)));
			return convertView;
		}
		class ViewHolder
		{
			ImageView iv_head;
			ImageView iv_count;
			TextView tv_name;
			TextView tv_ks;
			TextView tv_time;
			TextView tv_content;
		}
	}
	
	@Override
	public void startHint() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endHint() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		AsyncTaskUtil.cancelTask(task);
		if(broadcastReceiver!=null)
		{
			baseContext.unregisterReceiver(broadcastReceiver);
		}
		
	}
	
	
}
