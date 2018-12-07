package com.bsoft.hospital.pub.suzhoumh.activity.my.record;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

import com.app.tanklib.bitmap.CacheManage;
import com.app.tanklib.bitmap.IndexUrlCache;
import com.app.tanklib.bitmap.view.NetImageView;
import com.app.tanklib.http.BsoftNameValuePair;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.AppApplication;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.model.record.LabVo;
import com.bsoft.hospital.pub.suzhoumh.model.record.MyRecipeVo;
import com.bsoft.hospital.pub.suzhoumh.model.record.MyRecordDetailVo;
import com.bsoft.hospital.pub.suzhoumh.model.record.MyReportVo;
import com.bsoft.hospital.pub.suzhoumh.model.record.RecipeVo;
import com.bsoft.hospital.pub.suzhoumh.model.record.RecordDetailVo;
import com.bsoft.hospital.pub.suzhoumh.model.record.RecordVo;
import com.bsoft.hospital.pub.suzhoumh.util.DateUtil;
import com.app.tanklib.view.BsoftActionBar.Action;
import com.app.tanklib.util.DensityUtil;
import com.app.tanklib.util.StringUtil;

/**
 * @author Tank E-mail:zkljxq@126.com
 * 
 * @类说明
 */
public class MyRecordInfoActivity extends BaseActivity {

	LinearLayout mainView;
	RecordVo recordVo;

	LayoutInflater mLayoutInflater;
	//提示
	TextView textView1,textView2,myTextView1,myTextView2;

	TextView chiefcomplaint, deptname, hospname, visitdatetime;

	LinearLayout layout1, layout2, mylayout1, mylayout2;

	MyRecordDetailVo myRecordDetailVo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myrecord_info);
		urlMap = new IndexUrlCache(8);
		recordVo = (RecordVo) getIntent().getSerializableExtra("vo");
		this.mLayoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		findView();
		setClick();
		if (null != recordVo) {
			// 0 自己输入的 1：档案关联的
			if (recordVo.source == 0) {
				View view = mLayoutInflater.inflate(R.layout.lab_item, null);
				myTextView1=((TextView) view.findViewById(R.id.kinds));
				myTextView1.setText("加载中...");
				mylayout1.addView(view);
				View view1 = mLayoutInflater.inflate(R.layout.lab_item, null);
				myTextView2=((TextView) view1.findViewById(R.id.kinds));
				myTextView2.setText("加载中...");
				mylayout2.addView(view1);
				MyDetailTask task = new MyDetailTask();
				task.execute();
			} else if (recordVo.source == 1) {
				View view = mLayoutInflater.inflate(R.layout.lab_item, null);
				textView1=((TextView) view.findViewById(R.id.kinds));
				textView1.setText("加载中...");
				layout1.addView(view);
				View view1 = mLayoutInflater.inflate(R.layout.lab_item, null);
				textView2=((TextView) view1.findViewById(R.id.kinds));
				textView2.setText("加载中...");
				layout2.addView(view1);
				DetailTask task = new DetailTask();
				task.execute();
			}
		}
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.MyRecord_ACTION);
		this.registerReceiver(this.broadcastReceiver, filter);
	}

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (Constants.MyRecord_ACTION.equals(intent.getAction())) {
				MyDetailTask task = new MyDetailTask();
				task.execute();
			}
		}
	};

	void setClick() {
		chiefcomplaint.setText(recordVo.chiefcomplaint);
		deptname.setText(recordVo.deptname);
		hospname.setText(recordVo.hospname);
		visitdatetime
				.setText(DateUtil.getBirthDateTime(recordVo.visitdatetime));
	}

	@Override
	public void findView() {
		findActionBar();
		actionBar.setTitle("就诊详情");
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
		if (recordVo.source == 0) {
			actionBar.setRefreshTextView("编辑", new Action() {
				@Override
				public void performAction(View view) {
					/*if (null != myRecordDetailVo) {
						Intent intent = new Intent(baseContext,
								MyRecordEditActivity.class);
						intent.putExtra("vo", myRecordDetailVo);
						startActivity(intent);
					} else {
						Toast.makeText(MyRecordInfoActivity.this,
								"数据加载失败，无法编辑", Toast.LENGTH_SHORT).show();
					}*/
				}

				@Override
				public int getDrawable() {
					return 0;
				}
			});
		}
		mainView = (LinearLayout) findViewById(R.id.mainView);
		chiefcomplaint = (TextView) findViewById(R.id.chiefcomplaint);
		deptname = (TextView) findViewById(R.id.deptname);
		hospname = (TextView) findViewById(R.id.hospname);
		visitdatetime = (TextView) findViewById(R.id.visitdatetime);
		layout1 = (LinearLayout) findViewById(R.id.layout1);
		layout2 = (LinearLayout) findViewById(R.id.layout2);
		mylayout1 = (LinearLayout) findViewById(R.id.mylayout1);
		mylayout2 = (LinearLayout) findViewById(R.id.mylayout2);
	}

	public String getSmallImage(String url) {
		StringBuffer sb = new StringBuffer();
		sb.append(url.substring(0, url.lastIndexOf("."))).append("_250x250")
				.append(url.substring(url.lastIndexOf("."), url.length()));
		return sb.toString();
	}

	void setView(RecordDetailVo vo) {
		if (null != vo.recipe && vo.recipe.size() > 0) {
			layout1.removeAllViews();
			for (RecipeVo rv : vo.recipe) {
				View view = mLayoutInflater.inflate(R.layout.recipe_item, null);
				((TextView) view.findViewById(R.id.drugname))
						.setText(StringUtil.getTextLimit(rv.drugname, 10));
				((TextView) view.findViewById(R.id.drugusingrateTitle))
						.setText(rv.drugusingrateTitle);
				((TextView) view.findViewById(R.id.drugUsePathwaysTitle))
						.setText(rv.drugUsePathwaysTitle);
				((TextView) view.findViewById(R.id.drugusedays)).setText("共"
						+ rv.drugusedays + "天");
				layout1.addView(view);
			}
		}else{
			textView1.setText("无");
		}
		if (null != vo.lab && vo.lab.size() > 0) {
			layout2.removeAllViews();
			for (LabVo rv : vo.lab) {
				View view = mLayoutInflater.inflate(R.layout.lab_item, null);
				((TextView) view.findViewById(R.id.kinds))
						.setText(rv.kinds == 1 ? "检验检查" : "其他");
				((TextView) view.findViewById(R.id.title)).setText(rv.title);
				layout2.addView(view);
			}
		}else{
			textView2.setText("无");
		}
	}

	void setView(MyRecordDetailVo vo) {
		if (null != vo.record) {
			chiefcomplaint.setText(vo.record.chiefcomplaint);
			deptname.setText(vo.record.deptname);
			hospname.setText(vo.record.hospname);
			visitdatetime.setText(vo.record.vdate);
		}
		int lpmargin_left = DensityUtil.dip2px(baseContext, 10);
		int dd = DensityUtil.dip2px(baseContext, 40);
		int width = (AppApplication.getWidthPixels() - dd - 5 * lpmargin_left) / 4;
		if (null != vo.recipes && vo.recipes.size() > 0) {
			mylayout1.removeAllViews();
			for (final MyRecipeVo rv : vo.recipes) {
				if (!StringUtil.isEmpty(rv.pic)) {
					NetImageView view = new NetImageView(baseContext);
					view.setScaleType(ScaleType.CENTER_CROP);
					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.WRAP_CONTENT,
							LinearLayout.LayoutParams.WRAP_CONTENT);

					lp.setMargins(lpmargin_left / 2, lpmargin_left,
							lpmargin_left, 0);
					lp.width = width;
					lp.height = lp.width;
					view.setLayoutParams(lp);
					view.setImageUrl(HttpApi.getImageUrl(rv.pic, "_250x250",
							CacheManage.IMAGE_TYPE_SMALL),
							CacheManage.IMAGE_TYPE_SMALL);
					urlMap.add(-1, HttpApi.getImageUrl(rv.pic, "_250x250",
							CacheManage.IMAGE_TYPE_SMALL));
					view.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(
									MyRecordInfoActivity.this,
									PicInfoActivity.class);
							intent.putExtra("newUrl", HttpApi.getImageUrl(rv.pic, CacheManage.IMAGE_TYPE_BIG));
							startActivity(intent);
						}
					});
					mylayout1.addView(view);
				}
			}
		}else{
			myTextView1.setText("无");
		}
		if (null != vo.reports && vo.reports.size() > 0) {
			mylayout2.removeAllViews();
			for (final MyReportVo rv : vo.reports) {
				if (!StringUtil.isEmpty(rv.pic)) {
					NetImageView view = new NetImageView(baseContext);
					view.setScaleType(ScaleType.CENTER_CROP);
					// ((BaseGroup) getParent()).addPicUrl(rv.pic, -1);

					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.WRAP_CONTENT,
							LinearLayout.LayoutParams.WRAP_CONTENT);

					lp.setMargins(lpmargin_left / 2, lpmargin_left,
							lpmargin_left, 0);
					lp.width = width;
					lp.height = lp.width;
					view.setLayoutParams(lp);
					view.setImageUrl(HttpApi.getImageUrl(rv.pic, "_250x250",
							CacheManage.IMAGE_TYPE_SMALL),
							CacheManage.IMAGE_TYPE_SMALL);
					view.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(
									MyRecordInfoActivity.this,
									PicInfoActivity.class);
							intent.putExtra("newUrl", HttpApi.getImageUrl(rv.pic, CacheManage.IMAGE_TYPE_BIG));
							startActivity(intent);
						}
					});
					urlMap.add(-1, HttpApi.getImageUrl(rv.pic, "_250x250",
							CacheManage.IMAGE_TYPE_SMALL));
					mylayout2.addView(view);
				}
			}
		}else{
			myTextView2.setText("无");
		}
	}

	private class DetailTask extends
			AsyncTask<Void, Void, ResultModel<RecordDetailVo>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			actionBar.startTextRefresh();;
		}

		@Override
		protected ResultModel<RecordDetailVo> doInBackground(Void... params) {
			return HttpApi.getInstance().parserData(RecordDetailVo.class,
					"auth/opt/detail",
					new BsoftNameValuePair("id", loginUser.id),
					new BsoftNameValuePair("phrid", loginUser.phrid),
					new BsoftNameValuePair("clinicid", recordVo.clinicid),
					new BsoftNameValuePair("source", "1"),
					new BsoftNameValuePair("sn", loginUser.sn));
		}

		@Override
		protected void onPostExecute(ResultModel<RecordDetailVo> result) {
			super.onPostExecute(result);
			if (null != result) {
				if (result.statue == Statue.SUCCESS && null != result.data) {
					setView(result.data);
				} else {
					result.showToast(baseContext);
				}
			} else {
				Toast.makeText(baseContext, "加载失败", Toast.LENGTH_SHORT).show();
			}
			actionBar.endTextRefresh();;
		}
	}

	private class MyDetailTask extends
			AsyncTask<Void, Void, ResultModel<MyRecordDetailVo>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			actionBar.startTextRefresh();;
		}

		@Override
		protected ResultModel<MyRecordDetailVo> doInBackground(Void... params) {
			return HttpApi.getInstance().parserData(MyRecordDetailVo.class,
					"auth/opt/detail",
					new BsoftNameValuePair("id", loginUser.id),
					new BsoftNameValuePair("phrid", loginUser.phrid),
					new BsoftNameValuePair("clinicid", recordVo.clinicid),
					new BsoftNameValuePair("source", "0"),
					new BsoftNameValuePair("sn", loginUser.sn));
		}

		@Override
		protected void onPostExecute(ResultModel<MyRecordDetailVo> result) {
			super.onPostExecute(result);
			if (null != result) {
				if (result.statue == Statue.SUCCESS && null != result.data) {
					myRecordDetailVo = result.data;
					setView(result.data);
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
	public void onDestroy() {
		super.onDestroy();
		if (null != broadcastReceiver) {
			unregisterReceiver(broadcastReceiver);
			broadcastReceiver = null;
		}
	}
}
