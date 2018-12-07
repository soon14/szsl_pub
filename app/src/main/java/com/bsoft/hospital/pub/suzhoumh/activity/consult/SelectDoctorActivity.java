package com.bsoft.hospital.pub.suzhoumh.activity.consult;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tanklib.bitmap.view.RoundImageView;
import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.util.AsyncTaskUtil;
import com.app.tanklib.view.BsoftActionBar.Action;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.model.consult.DoctorInfo;

public class SelectDoctorActivity extends BaseActivity{

	private ListView lv_doctor;
	private List<DoctorInfo> list;
	private DoctorAdapter adapter;
	private String id;
	private GetDataTask task;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_doctor);
		
		findView();
		initView();
		initData();
	}

	@Override
	public void findView() {
		// TODO Auto-generated method stub
		findActionBar();
		actionBar.setTitle("选择医生");
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
	
	private void initView()
	{
		lv_doctor = (ListView) findViewById(R.id.lv_doctor);
	}
	
	private void initData()
	{
		list = new ArrayList<DoctorInfo>();
		adapter = new DoctorAdapter();
		id = getIntent().getStringExtra("id");
		lv_doctor.setAdapter(adapter);
		task = new GetDataTask();
		task.execute();
	}
	
	class GetDataTask extends AsyncTask<Void, Void, ResultModel<ArrayList<DoctorInfo>>>
	{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			actionBar.startTextRefresh();
		}
		@Override
		protected void onPostExecute(ResultModel<ArrayList<DoctorInfo>> result) {
			// TODO Auto-generated method stub
			actionBar.endTextRefresh();
			if (null != result) {
				if (result.statue == Statue.SUCCESS) {
					if (null != result.list && result.list.size() > 0) {
						list = result.list;
						adapter.notifyDataSetChanged();
					}
					else
					{
						Toast.makeText(baseContext, "当前科室无医生", Toast.LENGTH_SHORT)
						.show();
					}
				} else {
					result.showToast(baseContext);
				}
			} 
		}

		@Override
		protected ResultModel<ArrayList<DoctorInfo>> doInBackground(
				Void... params) {
			// TODO Auto-generated method stub
			return HttpApi.getInstance().parserArray(DoctorInfo.class,
					"auth/oameeting/getdoctors",
					new BsoftNameValuePair("rid",id),
					new BsoftNameValuePair("sn", loginUser.sn),
					new BsoftNameValuePair("id", loginUser.id));
		}
	}
	
	class DoctorAdapter extends BaseAdapter
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
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			final ViewHolder holder;
			if(convertView == null)
			{
				holder = new ViewHolder();
				convertView = LayoutInflater.from(baseContext).inflate(R.layout.select_doctor_item, null);
				holder.iv_head = (RoundImageView)convertView.findViewById(R.id.iv_head);
				holder.tv_name = (TextView)convertView.findViewById(R.id.tv_name);
				holder.tv_intro = (TextView)convertView.findViewById(R.id.tv_intro);
				holder.tv_dept = (TextView)convertView.findViewById(R.id.tv_dept);
				holder.tv_profession = (TextView)convertView.findViewById(R.id.tv_profession);
				holder.btn_consult = (Button)convertView.findViewById(R.id.btn_consult);
				holder.tv_see_all = (TextView)convertView.findViewById(R.id.tv_see_all);
				convertView.setTag(holder);
			}
			else
			{
				holder = (ViewHolder)convertView.getTag();
			}
			if(list.get(position).sexcode.equals("1"))
			{
				holder.iv_head.setBackgroundResource(R.drawable.doc_male);
			}
			else if(list.get(position).sexcode.equals("2"))
			{
				holder.iv_head.setBackgroundResource(R.drawable.doc_female);
			}
			holder.tv_name.setText(list.get(position).name);
			holder.tv_dept.setText(list.get(position).deptname);
			holder.tv_profession.setText(list.get(position).professionaltitle);
			holder.tv_intro.setText(list.get(position).introduce);
			if(!list.get(position).introduce.equals(""))
			{
				holder.tv_see_all.setVisibility(View.VISIBLE);
				holder.tv_see_all.setOnClickListener(new OnClickListener() {
					Boolean flag = true;

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						// Log.i("tv.getLineCount()",tv.getHeight()+"");
						if (flag) {

							flag = false;
							holder.tv_intro.setEllipsize(null); // 展开
							holder.tv_intro.setSingleLine(flag);
							Drawable drawable = getResources().getDrawable(
									R.drawable.info_up);
							// / 这一步必须要做,否则不会显示.
							drawable.setBounds(0, 0, drawable.getMinimumWidth(),
									drawable.getMinimumHeight());
							holder.tv_see_all.setCompoundDrawables(null, null,
									drawable, null);
						} else {
							flag = true;
							holder.tv_intro.setEllipsize(TextUtils.TruncateAt.END); // 收缩
							holder.tv_intro.setMaxLines(2);
							Drawable drawable = getResources().getDrawable(
									R.drawable.info_down);
							// / 这一步必须要做,否则不会显示.
							drawable.setBounds(0, 0, drawable.getMinimumWidth(),
									drawable.getMinimumHeight());
							holder.tv_see_all.setCompoundDrawables(null, null,
									drawable, null);
							// tv.setSingleLine(flag);
						}
					}
				});
			}
			else
			{
				holder.tv_see_all.setVisibility(View.GONE);
			}
			holder.btn_consult.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(SelectDoctorActivity.this,ConsultAddActivity.class);
					intent.putExtra("doctorinfo", list.get(position));
					startActivity(intent);
				}
				
			});
			return convertView;
		}
		
		class ViewHolder
		{
			RoundImageView iv_head;
			TextView tv_name;
			TextView tv_intro;
			TextView tv_profession;
			TextView tv_dept;
			TextView tv_see_all;
			Button btn_consult;
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		AsyncTaskUtil.cancelTask(task);
	}
	
}
