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
import com.bsoft.hospital.pub.suzhoumh.model.app.report.ReportVo;
import com.bsoft.hospital.pub.suzhoumh.model.app.report.RisReportVo;

/**
 * 检查报告
 * @author Administrator
 * 
 */
public class RisReportActivity extends BaseActivity implements OnClickListener{

	private PullToRefreshListView mPullRefreshListView;
	private ListView listView;
	private RisReportAdapter adapter;
	private ProgressBar emptyProgress;
	private View headerView;
	private LayoutInflater mLayoutInflater;
	private ReportVo vo;
	private ArrayList<RisReportVo> data1 = new ArrayList<RisReportVo>();//门诊
	private ArrayList<RisReportVo> data2 = new ArrayList<RisReportVo>();//住院
	private GetDataTask task;
	
	private int currentType = 1;//1 门诊 2 住院
	private RelativeLayout rl_1;//门诊
	private RelativeLayout rl_2;//住院
	
	private TextView tv_1;
	private TextView tv_2;
	private ImageView iv_1;
	private ImageView iv_2;
	
	private int pageno = 1;
	private int pagesize = 10;

	private Dialog builder;
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
		actionBar.setTitle("检查报告");

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
						pageno++;
						task = new GetDataTask();
						task.execute();
					}
				});
		listView = mPullRefreshListView.getRefreshableView();
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
		adapter = new RisReportAdapter();
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (personVo.type)
				{
					case SELF:
						Intent intent = new Intent(RisReportActivity.this,RisReportDetailActivity.class);
						intent.putExtra("vo", adapter.getItem(position-1));
						startActivity(intent);
						break;
					case FAMILY:
						RisReportVo vo = adapter.getItem(position-1);
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
			AsyncTask<Void, Void, ResultModel<ArrayList<RisReportVo>>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			actionBar.startTextRefresh();
			mPullRefreshListView.setRefreshing();
		}

		@Override
		protected ResultModel<ArrayList<RisReportVo>> doInBackground(
				Void... params) {
			
			HashMap<String,String> map = new HashMap<String,String>();
			map.put("method","listjclb");
			map.put("ai_lx", String.valueOf(currentType));//1门诊2住院
			map.put("as_sfzh", personVo.idcard);
			return HttpApi.getInstance().parserArray_His(RisReportVo.class, "hiss/ser",map,
					new BsoftNameValuePair("id", loginUser.id),
					new BsoftNameValuePair("sn", loginUser.sn));
		}

		@Override
		protected void onPostExecute(ResultModel<ArrayList<RisReportVo>> result) {
			super.onPostExecute(result);
			if (null != result) {
				if (result.statue == Statue.SUCCESS) {
					if (null != result.list && result.list.size() > 0) {
						if(currentType == 1)
						{
							data1 = result.list;
							adapter.setData(data1);
						}
						else
						{
							data2 = result.list;
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

	class RisReportAdapter extends BaseAdapter {

		private List<RisReportVo> list = new ArrayList<RisReportVo>();
		
		public void setData(List<RisReportVo> list)
		{
			this.list = list;
			adapter.notifyDataSetChanged();
		}
		
		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public  RisReportVo getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mLayoutInflater.inflate(R.layout.report_list_item,
						parent,false);
				holder.title = (TextView)convertView.findViewById(R.id.tv_title);
				holder.date = (TextView)convertView.findViewById(R.id.tv_date);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			RisReportVo vo = getItem(position);
			holder.title.setText(vo.jcxm);
			holder.date.setText(vo.jcsj);
			return convertView;
		}

		public class ViewHolder {
			public TextView title, date;
		}
	}

	private void judgeFphm(final RisReportVo vo)
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
				if(!inputFphm.equals("")&&inputFphm.equals(vo.fphm))
				{
					Intent intent = new Intent(RisReportActivity.this,RisReportDetailActivity.class);
					intent.putExtra("vo", vo);
					startActivity(intent);
					builder.dismiss();
				}
				else
				{
					Toast.makeText(RisReportActivity.this,"请输入正确的发票号码",Toast.LENGTH_SHORT).show();
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

	@Override
	public void onDestroy() {
		super.onDestroy();
		AsyncTaskUtil.cancelTask(task);
	}

	@Override
	public void onClick(View v) {
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
