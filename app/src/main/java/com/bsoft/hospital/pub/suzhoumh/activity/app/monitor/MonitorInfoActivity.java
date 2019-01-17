package com.bsoft.hospital.pub.suzhoumh.activity.app.monitor;

import java.util.ArrayList;
import java.util.Collections;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.util.AsyncTaskUtil;
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
import com.github.mikephil.charting.charts.BarLineChartBase.BorderPosition;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.XLabels;
import com.github.mikephil.charting.utils.XLabels.XLabelPosition;

/**
 * 健康检测详情
 * @author Administrator
 * 
 */
public class MonitorInfoActivity extends BaseActivity implements OnClickListener{

	
	private TextView unit, time, monitorValue,name;

	private ImageView icon;
	
	private LineChart mChart;

	private MonitorSetting model;
	
	private ArrayList<MonitorModel> list = new ArrayList<MonitorModel>();
	
	private boolean firstLoad = true;
	
	private LineData mLineData;
	
	//修改历史
	//private RelativeLayout rl_history;
	//左边箭头
	private RelativeLayout rl_left;
	//右边箭头
	private RelativeLayout rl_right;
	
	public int currentIndex = 0;//当前的index
	
	public GetDataTask task;
	
	//血压 有两个值要做特殊处理
	private LinearLayout ll_mmhg;
	private TextView tv_sz_value;
	private TextView tv_ss_value;
	
	private ListView listView;
	public MyAdapter adapter;
	public Builder builder;
	
	public int deletePosition = 0;
	private DelDataTask deltask;
	private int colors[] = new int[]{R.color.monitor_2,R.color.monitor_3,R.color.monitor_4,R.color.monitor_5,
			R.color.monitor_6,R.color.monitor_7,R.color.monitor_8,R.color.monitor_9};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.monitor_info);
		model = (MonitorSetting) getIntent().getSerializableExtra("model");
		findView();
		initView();
		initData();
	}
	/**
	 * 监听添加数据的广播
	 */
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (Constants.MonitorChange_ACTION.equals(intent.getAction())) {
					task = new GetDataTask();
					task.execute();
			}
		}
	};
	/**
	 * 查询数据任务
	 * @author Administrator
	 *
	 */
	private class GetDataTask extends
			AsyncTask<Void, Void, ResultModel<ArrayList<MonitorModel>>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			actionBar.startTextRefresh();;
		}

		@Override
		protected ResultModel<ArrayList<MonitorModel>> doInBackground(
				Void... params) {
			ArrayList<BsoftNameValuePair> list = new ArrayList<BsoftNameValuePair>();
			list.add(new BsoftNameValuePair("mids", String.valueOf(model.id)));
			list.add(new BsoftNameValuePair("sn", loginUser.sn));
			list.add(new BsoftNameValuePair("id", loginUser.id));
			BsoftNameValuePair[] arr = new BsoftNameValuePair[list.size()];
			list.toArray(arr);
			return HttpApi.getInstance().parserArray(MonitorModel.class,
					"auth/health/monitor/list", arr);
		}

		@Override
		protected void onPostExecute(ResultModel<ArrayList<MonitorModel>> result) {
			super.onPostExecute(result);
			if (null != result) {
				if (result.statue == Statue.SUCCESS) {
					if (null != result.list && result.list.size() > 0) {
							mChart.setData(getDate(model.id, result.list,
									model.name));
							mChart.animateX(2000);
							list = result.list;
							adapter.notifyDataSetChanged();
							//setMonitorValue();
							
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
		actionBar.setRefreshTextView("添加", new Action() {

			@Override
			public void performAction(View view) {
				Intent intent = new Intent(baseContext,
						MonitorAddActivity.class);
				intent.putExtra("monitortype", model.id);
				intent.putExtra("monitorname", model.name);
				intent.putExtra("currentType", 0);//添加
				startActivityForResult(intent, 110);
			}

			@Override
			public int getDrawable() {
				return 0;
			}
		});

	}

	private void initView()
	{
		unit = (TextView) findViewById(R.id.unit);
		time = (TextView) findViewById(R.id.time);
		ll_mmhg = (LinearLayout) findViewById(R.id.ll_mmhg);
		monitorValue = (TextView) findViewById(R.id.monitorValue);
		tv_sz_value = (TextView) findViewById(R.id.tv_sz_value);
		tv_ss_value = (TextView) findViewById(R.id.tv_ss_value);
		icon = (ImageView) findViewById(R.id.icon);
		name = (TextView) findViewById(R.id.name);
		rl_left = (RelativeLayout) findViewById(R.id.rl_left);
		rl_right = (RelativeLayout) findViewById(R.id.rl_right);
		listView = (ListView) findViewById(R.id.listView);
	}
	
	private void initData()
	{
		icon.setImageResource(MonitorUtils.getImageId(model.id));
		name.setText(model.name);
		unit.setText(model.unit);
		rl_left.setOnClickListener(this);
		rl_right.setOnClickListener(this);
		
		Bundle bundle = getIntent().getBundleExtra("bundle");
		if(bundle!=null&&bundle.getSerializable("list")!=null)
		{
			list = (ArrayList<MonitorModel>) getIntent().getBundleExtra("bundle").getSerializable("list");
		}
		/*if (list != null && list.size() > 0) {
			setMonitorValue();
		}*/
		createChart();
		
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.MonitorChange_ACTION);
		registerReceiver(broadcastReceiver, filter);
		adapter = new MyAdapter();
		listView.setAdapter(adapter);
	}
	
	/**
	 * 创建表格图
	 */
	private void createChart() {
		mChart = (LineChart) findViewById(R.id.chart);
		// 设置单位
		//mChart.setUnit(model.u);
		mChart.setDrawUnitsInChart(true);
		mChart.setStartAtZero(false);
		// 设置是否显示y轴的值的数据
		mChart.setDrawYValues(false);
		mChart.setDrawBorder(true);
		mChart.setBorderPositions(new BorderPosition[] { BorderPosition.LEFT,
				BorderPosition.BOTTOM });

		XLabels xl = mChart.getXLabels();
		//xl.setCenterXLabelText(true);
		xl.setPosition(XLabelPosition.BOTTOM);

		// 设置表格的描述
		mChart.setDescription("");
		// 如果没有数据的时候，会显示这个，类似listview的emtpyview
		mChart.setNoDataTextDescription("You need to provide data for the chart.");
		mChart.setHighlightEnabled(true);
		mChart.setTouchEnabled(true);
		mChart.setDragEnabled(true);
		mChart.setScaleEnabled(true);
		mChart.setPinchZoom(true);
		// MyMarkerView mv = new MyMarkerView(this,
		// R.layout.custom_marker_view);
		// mChart.setMarkerView(mv);
		mChart.setHighlightIndicatorEnabled(false);
		mChart.setNoDataTextDescription("没有数据");
		//mChart.setData(getDate(15, 100, model.name));
		if(list!=null&&list.size()>0)
		{
			mChart.setData(getDate(model.id, list, model.name));
		}
		mChart.animateX(2000);

	}

	// 得到X轴数据
	public ArrayList<String> getXVals(ArrayList<MonitorModel> list) {
		ArrayList<String> xVals = new ArrayList<String>();
		for (int i = 0; i < list.size(); i++) {
			// xVals.add(DateUtil.getDateTime("MM-dd HH:mm",
			// list.get(i).createdate));
			xVals.add(DateUtil.getDateTime("MM-dd", list.get(i).createdate));
		}
		return xVals;
	}
	// 得到图标数据
	public LineData getDate(int type, ArrayList<MonitorModel> list, String name) {
		Collections.sort(list);
		return new LineData(getXVals(list), getYVals(type, list));
	}
	// 得到Y轴数据
	public ArrayList<LineDataSet> getYVals(int type,
			ArrayList<MonitorModel> list) {
		ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
		if (type == 1) {
			// 血压- 2跳线
			ArrayList<Entry> yVals1 = new ArrayList<Entry>();
			ArrayList<Entry> yVals2 = new ArrayList<Entry>();
			for (int i = 0; i < list.size(); i++) {
				MonitorBP bp = JSON.parseObject(list.get(i).monitorinfo,
						MonitorBP.class);
				yVals1.add(new Entry(bp.sbp, i));
				yVals2.add(new Entry(bp.dbp, i));
			}
			LineDataSet set1 = new LineDataSet(yVals1, "收缩压");

			set1.enableDashedLine(10f, 5f, 0f);
			set1.setColor(getResources().getColor(R.color.monitor_1_1));
			set1.setCircleColor(getResources().getColor(R.color.monitor_1_1));
			set1.setLineWidth(1f);
			set1.setCircleSize(4f);
			set1.setFillAlpha(65);
			set1.setFillColor(Color.BLACK);

			LineDataSet set2 = new LineDataSet(yVals2, "舒张压");
			set2.enableDashedLine(10f, 5f, 0f);
			set2.setColor(getResources().getColor(R.color.monitor_1_2));
			set2.setCircleColor(getResources().getColor(R.color.monitor_1_2));
			set2.setLineWidth(1f);
			set2.setCircleSize(4f);
			set2.setFillAlpha(65);
			set2.setFillColor(Color.BLACK);

			dataSets.add(set1);
			dataSets.add(set2);
		} else {
			ArrayList<Entry> yVals = new ArrayList<Entry>();
			for (int i = 0; i < list.size(); i++) {
				yVals.add(new Entry(getValue(type, list.get(i)), i));
			}
			LineDataSet set1 = new LineDataSet(yVals, model.name);

			set1.enableDashedLine(10f, 5f, 0f);
			set1.setColor(getResources().getColor(colors[type-2]));
			set1.setCircleColor(getResources().getColor(colors[type-2]));
			set1.setLineWidth(1f);
			set1.setCircleSize(4f);
			set1.setFillAlpha(65);
			set1.setFillColor(Color.BLACK);

			dataSets.add(set1);
		}

		return dataSets;
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
		AsyncTaskUtil.cancelTask(task);
		if (null != this.broadcastReceiver) {
			this.unregisterReceiver(this.broadcastReceiver);
			broadcastReceiver = null;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == 110) {

			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			/*case R.id.rl_left:
				if (currentIndex > 0) {
					currentIndex--;
					setMonitorValue();
				} else {
					Toast.makeText(baseContext, "没有更多数据", Toast.LENGTH_SHORT)
							.show();
				}
				break;
			case R.id.rl_right:
				if (list!=null&&currentIndex < list.size() - 1) {
					currentIndex++;
					setMonitorValue();
				} else {
					Toast.makeText(baseContext, "没有更多数据", Toast.LENGTH_SHORT)
							.show();
				}
				break;
			case R.id.rl_history:
				Intent intent = new Intent(MonitorInfoActivity.this,MonitorListActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("list", list);
				intent.putExtra("bundle", bundle);
				intent.putExtra("model", model);
				startActivity(intent);
				break;*/
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
			builder = new Builder(MonitorInfoActivity.this);
			builder.setItems(new String[]{"修改","删除"}, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					if(which == 0)//修改
					{
						Intent intent = new Intent(MonitorInfoActivity.this,MonitorAddActivity.class);
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
						new Builder(MonitorInfoActivity.this)
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
	
	/**
	 * 设置数据
	 *//*
	private void setMonitorValue()
	{
		if(model.id == 1)//血压
		{
			ll_mmhg.setVisibility(View.VISIBLE);
			monitorValue.setVisibility(View.GONE);
			MonitorBP bp = JSON.parseObject(list.get(currentIndex).monitorinfo,
					MonitorBP.class);
			tv_sz_value.setText(String.valueOf(bp.sbp));
			tv_ss_value.setText(String.valueOf(bp.dbp));
		}
		else
		{
			monitorValue.setText(String.valueOf(getValue(model.id,
					list.get(currentIndex))));
		}
		time.setText(DateUtil.getDateTime("yyyy-MM-dd HH:mm",
				list.get(currentIndex).createdate));
		
	}*/
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
}
