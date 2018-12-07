package com.bsoft.hospital.pub.suzhoumh.activity.app.satisfaction;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.util.AsyncTaskUtil;
import com.app.tanklib.view.BsoftActionBar.Action;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.model.satisfaction.SatisfactionListVo;

/**
 * 服务评价列表
 * @author Administrator
 *
 */
public class SatisfactionListActivity extends BaseActivity{

	private ListView listview;
	private MyAdapter adapter;
	private ArrayList<SatisfactionListVo> list = new ArrayList<SatisfactionListVo>();
	private ArrayList<String> ids = new ArrayList<String>();
	private GetDataTask task;
	private GetIdsTask getIdsTask;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.satisfactionlist);
		findView();
		initData();
	}


	@Override
	public void findView() {
		// TODO Auto-generated method stub
		findActionBar();
		actionBar.setTitle("服务评价");
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
		
		listview = (ListView)findViewById(R.id.listview);
	}
	
	private void initData()
	{
		adapter = new MyAdapter();
		listview.setAdapter(adapter);
		
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SatisfactionListActivity.this,SatisfactionInfoActivity.class);
				intent.putExtra("vo", list.get(position));
				startActivityForResult(intent, 1);
			}
		});
		
		listview.setAdapter(adapter);
		listview.setVisibility(View.INVISIBLE);
		task = new GetDataTask();
		task.execute();
	}
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		if(arg1 == RESULT_OK)
		{
			switch(arg0)
			{
			case 1:
				task = new GetDataTask();
				task.execute();
				break;
			}
		}
	}
	
	/**
	 * 获取数据
	 * @author Administrator
	 *
	 */
	private class GetDataTask extends
			AsyncTask<Void, Void, ResultModel<ArrayList<SatisfactionListVo>>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			actionBar.startTextRefresh();;
		}

		@Override
		protected ResultModel<ArrayList<SatisfactionListVo>> doInBackground(
				Void... params) {
			
			HashMap<String,String> map = new HashMap<String,String>();
			map.put("jzkh", "123458");
			return HttpApi.getInstance().parserArray_His(SatisfactionListVo.class, "his/fwpj/listFwpj",map,
					new BsoftNameValuePair("id", loginUser.id),
					new BsoftNameValuePair("sn", loginUser.sn));
		}

		@Override
		protected void onPostExecute(ResultModel<ArrayList<SatisfactionListVo>> result) {
			super.onPostExecute(result);
			if (null != result) {
				if (result.statue == Statue.SUCCESS) {
					if (null != result.list && result.list.size() > 0) {
						list = result.list;
						adapter.notifyDataSetChanged();
						getIdsTask = new GetIdsTask();
						getIdsTask.execute();
					} else {
						Toast.makeText(baseContext, "数据为空", Toast.LENGTH_SHORT)
								.show();
					}
				} else {
					result.showToast(baseContext);
				}
			} else {
				Toast.makeText(baseContext, "加载失败", Toast.LENGTH_SHORT).show();
			}
			actionBar.endTextRefresh();;
		}
	}
	
	/**
	 * 获取已评价就诊记录序号
	 * @author Administrator
	 *
	 */
	private class GetIdsTask extends
			AsyncTask<Void, Void, ResultModel<ArrayList<String>>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			actionBar.startTextRefresh();
			;
		}

		@Override
		protected ResultModel<ArrayList<String>> doInBackground(
				Void... params) {

			return HttpApi.getInstance().parserArray(
					String.class, "auth/satisfaction/getIdsByUid",
					new BsoftNameValuePair("id", loginUser.id),
					new BsoftNameValuePair("sn", loginUser.sn)
					);
		}

		@Override
		protected void onPostExecute(
				ResultModel<ArrayList<String>> result) {
			super.onPostExecute(result);
			if (null != result) {
				if (result.statue == Statue.SUCCESS) {
					if (null != result.list && result.list.size() > 0) {
						for(int i=0;i<result.list.size();i++)
						{
							ids.add(result.list.get(i));
						}
						adapter.notifyDataSetChanged();
					}
					listview.setVisibility(View.VISIBLE);
				} else {
					result.showToast(baseContext);
				}
			} else {
				Toast.makeText(baseContext, "加载失败", Toast.LENGTH_SHORT).show();
			}
			actionBar.endTextRefresh();
			;
		}
	}
	
	class MyAdapter extends BaseAdapter
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
				convertView = LayoutInflater.from(baseContext).inflate(R.layout.satisfaction_list_item, null);
				holder.tv_time = (TextView)convertView.findViewById(R.id.tv_time);
				holder.tv_ks = (TextView)convertView.findViewById(R.id.tv_ks);
				holder.tv_content = (TextView)convertView.findViewById(R.id.tv_content);
				holder.rl_item = (RelativeLayout)convertView.findViewById(R.id.rl_item);
				holder.btn_satisfaction = (Button)convertView.findViewById(R.id.btn_satisfaction);
				convertView.setTag(holder);
			}
			else
			{
				holder = (ViewHolder)convertView.getTag();
			}
			if(ids!=null&&ids.size()>0)
			{
				for(int i=0;i<ids.size();i++)
				{
					if(list.get(position).jzxh.equals(ids.get(i)))
					{
						list.get(position).flag = 1;//设置为已评价
						holder.btn_satisfaction.setText("已评价");
						holder.btn_satisfaction.setTextColor(Color.parseColor("#999999"));
						holder.btn_satisfaction.setBackgroundResource(R.drawable.lightgray_corners_n);
						break;
					}
				}
			}
			holder.tv_time.setText(list.get(position).fwsj);
			holder.tv_ks.setText(list.get(position).ksmc);
			holder.tv_content.setText(list.get(position).zdjg);
				
			return convertView;
		}
		
		class ViewHolder
		{
			TextView tv_time,tv_ks,tv_content;
			Button btn_satisfaction;
			ImageView iv_satis;
			RelativeLayout rl_item;
		}
	}


	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		AsyncTaskUtil.cancelTask(task);
		AsyncTaskUtil.cancelTask(getIdsTask);
	}


}
