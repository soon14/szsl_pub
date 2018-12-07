package com.bsoft.hospital.pub.suzhoumh.activity.consult;

import java.util.ArrayList;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.util.AsyncTaskUtil;
import com.app.tanklib.view.BsoftActionBar.Action;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.model.consult.DeptInfo;

public class SelectDeptActivity extends BaseActivity{

	private ListView lv_dept;
	private ArrayList<DeptInfo> list;
	private DeptAdapter adapter;
	private GetDataTask task;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_dept);
		findView();
		initView();
		initData();
	}

	@Override
	public void findView() {
		findActionBar();
		actionBar.setTitle("选择科室");
		actionBar.setBackAction(new Action(){

			@Override
			public int getDrawable() {
				return R.drawable.btn_back;
			}

			@Override
			public void performAction(View view) {
				finish();
			}
			
		});
	}
	
	private void initView()
	{
		lv_dept = (ListView) findViewById(R.id.lv_dept);
	}
	
	private void initData()
	{
		list = new ArrayList<DeptInfo>();
		adapter = new DeptAdapter();
		lv_dept.setAdapter(adapter);
		lv_dept.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(SelectDeptActivity.this,SelectDoctorActivity.class);
				intent.putExtra("id", list.get(position).id);
				startActivity(intent);
			}
			
		});
		task = new GetDataTask();
		task.execute();
	}
	
	class GetDataTask extends AsyncTask<Void, Void, ResultModel<ArrayList<DeptInfo>> >
	{

		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			actionBar.startTextRefresh();
		}

		@Override
		protected ResultModel<ArrayList<DeptInfo>> doInBackground(Void... params) {
			return HttpApi.getInstance().parserArray(DeptInfo.class,
					"auth/oameeting/getdepts",
					new BsoftNameValuePair("sn", loginUser.sn),
					new BsoftNameValuePair("id", loginUser.id));
		}

		@Override
		protected void onPostExecute(ResultModel<ArrayList<DeptInfo>> result) {
			super.onPostExecute(result);
			actionBar.endTextRefresh();
			if (null != result) {
				if (result.statue == Statue.SUCCESS) {
					if (null != result.list && result.list.size() > 0) {
						list = result.list;
						removeItem(list);
						adapter.notifyDataSetChanged();
					} 
				} else {
					result.showToast(baseContext);
				}
			} 
		}
	}
	
	/**
	 *删除第一项的全部 
	 */
	private void removeItem(ArrayList<DeptInfo> list)
	{
		for(int i=0;i<list.size();i++)
		{
			if(list.get(i).gname.equals("全部"))
			{
				list.remove(i);
				break;
			}
		}
	}
	
	class DeptAdapter extends BaseAdapter
	{

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if(convertView == null)
			{
				holder = new ViewHolder();
				convertView = LayoutInflater.from(baseContext).inflate(R.layout.select_dept_item, null);
				holder.tv_dept = (TextView) convertView.findViewById(R.id.tv_dept);
				convertView.setTag(holder);
			}
			else
			{
				holder = (ViewHolder)convertView.getTag();
			}
			holder.tv_dept.setText(list.get(position).gname);
			return convertView;
		}
		
		class ViewHolder
		{
			TextView tv_dept;
		}
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		AsyncTaskUtil.cancelTask(task);
	}
	
	

}
