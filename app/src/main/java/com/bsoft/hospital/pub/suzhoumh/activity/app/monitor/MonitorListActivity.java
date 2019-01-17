package com.bsoft.hospital.pub.suzhoumh.activity.app.monitor;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.view.BsoftActionBar.Action;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.activity.app.healthtool.MedicineRemindModel;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.NullModel;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.model.moitor.MonitorBP;
import com.bsoft.hospital.pub.suzhoumh.model.moitor.MonitorBmi;
import com.bsoft.hospital.pub.suzhoumh.model.moitor.MonitorHeight;
import com.bsoft.hospital.pub.suzhoumh.model.moitor.MonitorModel;
import com.bsoft.hospital.pub.suzhoumh.model.moitor.MonitorOxygen;
import com.bsoft.hospital.pub.suzhoumh.model.moitor.MonitorRate;
import com.bsoft.hospital.pub.suzhoumh.model.moitor.MonitorSetting;
import com.bsoft.hospital.pub.suzhoumh.model.moitor.MonitorSports;
import com.bsoft.hospital.pub.suzhoumh.model.moitor.MonitorSugar;
import com.bsoft.hospital.pub.suzhoumh.model.moitor.MonitorWaist;
import com.bsoft.hospital.pub.suzhoumh.model.moitor.MonitorWeight;
import com.bsoft.hospital.pub.suzhoumh.util.DateUtil;

/**
 * 历史列表数据
 * @author Administrator 暂时不用了
 * 
 */
public class MonitorListActivity extends BaseActivity {

	private ListView listView;
	private MyAdapter adapter;
	private ProgressBar emptyProgress;
	private LayoutInflater mLayoutInflater;
	private boolean firstLoad = true;

	private ArrayList<MonitorModel> list = new ArrayList<MonitorModel>();
	public MonitorSetting model;
	public Builder builder;
	
	public int deletePosition = 0;
	
	private DelDataTask deltask;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.base_list);
		
		this.mLayoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		model = (MonitorSetting)getIntent().getSerializableExtra("model");
		findView();
		initData();
	}

	@Override
	public void findView() {
		findActionBar();
		actionBar.setTitle(model.name);
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
		emptyProgress = (ProgressBar) findViewById(R.id.emptyProgress);
		listView = (ListView) findViewById(R.id.listView);
	}

	private void initData()
	{
		list = (ArrayList<MonitorModel>) getIntent().getBundleExtra("bundle").getSerializable("list");
		adapter = new MyAdapter();
		listView.setAdapter(adapter);
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
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			if(convertView==null)
			{
				holder = new ViewHolder();
				convertView = LayoutInflater.from(baseContext).inflate(R.layout.monitor_list_item, null);
				//其他
				holder.tv_value_other = (TextView) convertView.findViewById(R.id.tv_value_other);
				holder.tv_time_other = (TextView) convertView.findViewById(R.id.tv_time_other);
				holder.ll_other = (LinearLayout) convertView.findViewById(R.id.ll_other);
				//血压
				holder.tv_value_sbp = (TextView) convertView.findViewById(R.id.tv_value_sbp);
				holder.tv_value_dbp = (TextView) convertView.findViewById(R.id.tv_value_dbp);
				holder.tv_time_bp = (TextView) convertView.findViewById(R.id.tv_time_bp);
				holder.ll_bp = (LinearLayout) convertView.findViewById(R.id.ll_bp);
				
				convertView.setTag(holder);
			}
			else
			{
				holder = (ViewHolder)convertView.getTag();
			}
			if(model.id == 1)//血压
			{
				setMonitorBpValue(holder.tv_value_sbp, holder.tv_value_dbp, holder.tv_time_bp, position);
				String value_sbp = holder.tv_value_sbp.getText().toString();
				String value_dbp = holder.tv_value_dbp.getText().toString();
				final String time_bp = holder.tv_time_bp.getText().toString();
				final String value = value_sbp+"_"+value_dbp;
				holder.ll_bp.setVisibility(View.VISIBLE);
				holder.ll_other.setVisibility(View.GONE);
				holder.ll_bp.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						showSelectDialog(position,value,time_bp);
					}
					
				});
			}
			else//其他
			{
				setMonitorValue(holder.tv_value_other, holder.tv_time_other, position);
				final String value = holder.tv_value_other.getText().toString();
				final String time = holder.tv_time_other.getText().toString();
				holder.ll_bp.setVisibility(View.GONE);
				holder.ll_other.setVisibility(View.VISIBLE);
				holder.ll_other.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						showSelectDialog(position,value,time);
					}
					
				});
			}
			return convertView;
		}
		
		//item项弹出的dialog
		private void showSelectDialog(final int position,final String value,final String time)
		{
			builder = new Builder(MonitorListActivity.this);
			builder.setItems(new String[]{"修改","删除"}, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					if(which == 0)//修改
					{
						Intent intent = new Intent(MonitorListActivity.this,MonitorAddActivity.class);
						intent.putExtra("monitortype", list.get(position).monitortype);
						intent.putExtra("monitorname", model.name);
						intent.putExtra("monitorvalue", value);
						intent.putExtra("monitortime",time);
						intent.putExtra("rid", list.get(position).id);
						intent.putExtra("currentType", 1);//修改
						baseContext.startActivity(intent);
					}
					else if(which == 1)//删除
					{
						new Builder(MonitorListActivity.this)
	    				.setMessage("确定删除该条记录？")
						.setTitle("提示")
						.setPositiveButton("确定",new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								showLoadingView();
								deletePosition = position;
								deltask = new DelDataTask();
								deltask.execute();
							}
						})
						.setNegativeButton("取消", null)
						.create().show();
					}
				}
			} );
			builder.create().show();
		}
		
		class ViewHolder
		{
			//其他数据
			TextView tv_value_other;
			TextView tv_time_other;
			LinearLayout ll_other;
			
			//血压数据
			TextView tv_value_sbp,tv_value_dbp;
			TextView tv_time_bp;
			LinearLayout ll_bp;
		}
		
	}
	
	/**
	 * 删除任务
	 * @author Administrator
	 *
	 */
	private class DelDataTask extends
			AsyncTask<Void, Void, ResultModel<ArrayList<NullModel>>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			actionBar.startTextRefresh();;
		}

		@Override
		protected ResultModel<ArrayList<NullModel>> doInBackground(
				Void... params) {
			return HttpApi.getInstance().parserArray(MedicineRemindModel.class,
					"auth/health/monitor/remove", 
					new BsoftNameValuePair("sn", loginUser.sn),
					new BsoftNameValuePair("id", loginUser.id),
					new BsoftNameValuePair("rid",String.valueOf(list.get(deletePosition).id))
					);
		}

		@Override
		protected void onPostExecute(ResultModel<ArrayList<NullModel>> result) {
			super.onPostExecute(result);
			if (null != result) {
				if (result.statue == Statue.SUCCESS) {
					if (null != result) {
						if (result.statue == Statue.SUCCESS) {
							sendBroadcast(new Intent(
									Constants.MonitorChange_ACTION));
							Toast.makeText(baseContext, "删除成功", Toast.LENGTH_SHORT)
									.show();
							list.remove(deletePosition);
							adapter.notifyDataSetChanged();
							
						} else {
							result.showToast(baseContext);
						}
					}
				} else {
					result.showToast(baseContext);
				}
			} else {
				Toast.makeText(baseContext, "删除失败", Toast.LENGTH_SHORT).show();
			}
			actionBar.endTextRefresh();;
		}
	}
	
	/**
	 * 设置其他数据
	 */
	private void setMonitorValue(TextView value,TextView time,int currentIndex)
	{
		value.setText(String.valueOf(getValue(model.id, list.get(currentIndex))));
		time.setText(DateUtil.getDateTime("yyyy-MM-dd HH:mm",
				list.get(currentIndex).createdate));
		
	}
	
	/**
	 * 设置血压数据
	 * @param sbpvalue
	 * @param dbpvalue
	 * @param time
	 * @param currentIndex
	 */
	private void setMonitorBpValue(TextView sbpvalue,TextView dbpvalue,TextView time,int currentIndex)
	{
		MonitorBP bp = JSON.parseObject(list.get(currentIndex).monitorinfo,
				MonitorBP.class);
		sbpvalue.setText(String.valueOf(bp.sbp));
		dbpvalue.setText(String.valueOf(bp.dbp));
		time.setText(DateUtil.getDateTime("yyyy-MM-dd HH:mm",
				list.get(currentIndex).createdate));
	}
	
	public float getValue(int type,MonitorModel vo) {
		switch (type) {
		case 2:
			MonitorSugar sugar = JSON.parseObject(vo.monitorinfo,MonitorSugar.class);
			return sugar.sugar;
		case 3:
			MonitorWeight weight=JSON.parseObject(vo.monitorinfo, MonitorWeight.class);
			return weight.weight;
		case 4:
			MonitorHeight height=JSON.parseObject(vo.monitorinfo,MonitorHeight.class);
			return height.height;
		case 5:
			MonitorBmi bmi = JSON.parseObject(vo.monitorinfo,MonitorBmi.class);
			return bmi.bmi;
		case 6:
			MonitorRate rate = JSON.parseObject(vo.monitorinfo,MonitorRate.class);
			return rate.rate;
		case 7:
			MonitorWaist waist = JSON.parseObject(vo.monitorinfo,MonitorWaist.class);
			return waist.waist;
		case 8:
			MonitorSports sports = JSON.parseObject(vo.monitorinfo,MonitorSports.class);
			return sports.sports;
		case 9:
			MonitorOxygen oxygen = JSON.parseObject(vo.monitorinfo,MonitorOxygen.class);
			return oxygen.oxygen;
		default:
			return 0;
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
