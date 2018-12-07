package com.bsoft.hospital.pub.suzhoumh.activity.app.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.bsoft.hospital.pub.suzhoumh.model.app.report.LisReportVo;
import com.bsoft.hospital.pub.suzhoumh.model.app.report.ReportVo;

/**
 * 检验报告
 * @author Administrator
 *
 */
public class LisReportActivity extends BaseActivity implements OnClickListener{

	private PullToRefreshListView mPullRefreshListView;
	private ListView listView;
	private LisReportAdapter adapter;
	private ProgressBar emptyProgress;
	private View headerView;
	private LayoutInflater mLayoutInflater;
	private ReportVo vo;
	private ArrayList<LisReportVo> data1 = new ArrayList<LisReportVo>();//门诊
	private ArrayList<LisReportVo> data2 = new ArrayList<LisReportVo>();//住院
	private GetDataTask task;

	private int currentType = 1;//1 门诊 2 住院
	private RelativeLayout rl_1;//门诊
	private RelativeLayout rl_2;//住院

	private TextView tv_1;
	private TextView tv_2;
	private ImageView iv_1;
	private ImageView iv_2;

	//门诊
	private int pageno_mz = 1;
	//住院
	private int pageno_zy = 1;

	public int pagesize = 10;

	public Dialog builder;
	//private FooterView footerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.report_lis);
		this.mLayoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		vo = (ReportVo) getIntent().getSerializableExtra("vo");

		findView();
		initData();
	}

	@Override
	public void findView() {
		findActionBar();
		actionBar.setTitle("检验报告");

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
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		mPullRefreshListView.setMode(Mode.PULL_FROM_END);
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
						emptyProgress.setVisibility(View.GONE);
						if(currentType == 1)
						{
							pageno_mz++;
						}
						else
						{
							pageno_zy++;
						}
						task = new GetDataTask();
						task.execute();
					}
				});
		listView = mPullRefreshListView.getRefreshableView();
		//footerView = new FooterView(this);
		/*footerView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(currentType == 1)
				{
					pageno_mz++;
				}
				else
				{
					pageno_zy++;
				}
				task = new GetDataTask();
				task.execute();
			}
		});*/
		rl_1 = (RelativeLayout)findViewById(R.id.rl_1);
		rl_2 = (RelativeLayout)findViewById(R.id.rl_2);
		tv_1 = (TextView)findViewById(R.id.tv_1);
		tv_2 =  (TextView)findViewById(R.id.tv_2);
		iv_1 = (ImageView)findViewById(R.id.iv_1);
		iv_2 = (ImageView)findViewById(R.id.iv_2);
	}

	private void initData()
	{
		rl_1.setOnClickListener(this);
		rl_2.setOnClickListener(this);
		adapter = new LisReportAdapter();
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				switch (personVo.type)
				{
					case SELF:
						Intent intent = new Intent(LisReportActivity.this,LisReportDetailActivity.class);
						intent.putExtra("vo", adapter.getItem(position-1));
						startActivity(intent);
						break;
					case FAMILY:
						LisReportVo vo = adapter.getItem(position-1);
						judgeFphm(vo);
						break;

				}
			}

		});

		task = new GetDataTask();
		task.execute();
	}
	/**
	 * 获取数据
	 * @author Administrator
	 *
	 */
	private class GetDataTask extends
			AsyncTask<Void, Void, ResultModel<ArrayList<LisReportVo>>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			actionBar.startTextRefresh();
			mPullRefreshListView.setRefreshing();
		}

		@Override
		protected ResultModel<ArrayList<LisReportVo>> doInBackground(
				Void... params) {

			HashMap<String,String> map = new HashMap<String,String>();
			//map.put("hm", "123456");
			//map.put("lx", String.valueOf(currentType));
			map.put("method","listptlabrecordbyuserid");
			map.put("ai_sjly", String.valueOf(currentType));//1门诊2住院3体检
			//map.put("as_brid", "320502196003110048");
			map.put("as_brid", personVo.idcard);
			if(currentType == 1)
			{
				map.put("ai_pageno", String.valueOf(pageno_mz));
			}
			else
			{
				map.put("ai_pageno", String.valueOf(pageno_zy));
			}
			map.put("ai_pagesize", String.valueOf(pagesize));
			map.put("ai_type", "1");//1检验2检查
			return HttpApi.getInstance().parserArray_His(LisReportVo.class, "hiss/ser",map,
					new BsoftNameValuePair("id", loginUser.id),
					new BsoftNameValuePair("sn", loginUser.sn));
		}

		@Override
		protected void onPostExecute(ResultModel<ArrayList<LisReportVo>> result) {
			super.onPostExecute(result);
			if (null != result) {
				if (result.statue == Statue.SUCCESS) {
					if (null != result.list && result.list.size() > 0) {
						if(currentType == 1)
						{
							//data1 = result.list;
							data1.addAll(result.list);
							adapter.setData(data1);
						}
						else
						{
							//data2 = result.list;
							data2.addAll(result.list);
							adapter.setData(data2);
						}
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
			actionBar.endTextRefresh();
			mPullRefreshListView.onRefreshComplete();
		}
	}

	private void judgeFphm(final LisReportVo vo)
	{
		builder = new Dialog(this, R.style.alertDialogTheme);
		View viewDialog = LayoutInflater.from(this).inflate(R.layout.dialog_input,
				null);
		// 设置对话框的宽高
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(AppApplication
				.getWidthPixels() * 85 / 100, LinearLayout.LayoutParams.WRAP_CONTENT);
		Button positiveButton = (Button)viewDialog.findViewById(R.id.positiveButton);
		Button negativeButton = (Button)viewDialog.findViewById(R.id.negativeButton);
		final EditText et_content = (EditText)viewDialog.findViewById(R.id.et_content);

		positiveButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				String inputFphm = et_content.getText().toString();
				if(!inputFphm.equals("")&&inputFphm.equals(vo.FPHM))
				{
					Intent intent = new Intent(LisReportActivity.this,LisReportDetailActivity.class);
					intent.putExtra("vo", vo);
					startActivity(intent);
					builder.dismiss();
				}
				else
				{
					Toast.makeText(LisReportActivity.this,"请输入正确的发票号码",Toast.LENGTH_SHORT).show();
				}
			}
		});
		negativeButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				builder.dismiss();
			}
		});

		builder.setContentView(viewDialog, layoutParams);
		builder.show();
	}

	class LisReportAdapter extends BaseAdapter {

		private List<LisReportVo> list = new ArrayList<LisReportVo>();

		public LisReportAdapter() {

		}

		public void setData(List<LisReportVo> list)
		{
			this.list = list;
			adapter.notifyDataSetChanged();
		}

		public void addData()
		{

		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public  LisReportVo getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView,
				ViewGroup parentViewGroup) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mLayoutInflater.inflate(R.layout.report_list_item,
						null);
				holder.title = (TextView)convertView.findViewById(R.id.tv_title);
				holder.date = (TextView)convertView.findViewById(R.id.tv_date);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			LisReportVo vo = getItem(position);
			holder.title.setText(vo.JYMD);
			holder.date.setText(vo.OBSERVATIONDATETIME);
			return convertView;
		}

		public class ViewHolder {
			public TextView title, date;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		AsyncTaskUtil.cancelTask(task);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.rl_1:
			currentType = 1;
			tv_1.setTextColor(getResources().getColor(R.color.blue));
			iv_1.setVisibility(View.VISIBLE);
			tv_2.setTextColor(getResources().getColor(R.color.black));
			iv_2.setVisibility(View.INVISIBLE);
			adapter.setData(data1);
			if(data1.size()==0)
			{
				task = new GetDataTask();
				task.execute();
			}
			break;
		case R.id.rl_2:
			currentType = 2;
			tv_2.setTextColor(getResources().getColor(R.color.blue));
			iv_2.setVisibility(View.VISIBLE);
			tv_1.setTextColor(getResources().getColor(R.color.black));
			iv_1.setVisibility(View.INVISIBLE);
			adapter.setData(data2);
			if(data2.size()==0)
			{
				task = new GetDataTask();
				task.execute();
			}
			break;
		}
	}

}
