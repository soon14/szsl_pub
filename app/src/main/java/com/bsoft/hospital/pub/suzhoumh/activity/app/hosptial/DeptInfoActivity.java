package com.bsoft.hospital.pub.suzhoumh.activity.app.hosptial;

import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.util.AsyncTaskUtil;
import com.app.tanklib.view.BsoftActionBar.Action;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.app.appoint.AppointDeptInfoActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.app.appoint.AppointDoctorInfoActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.DoctorVo;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.util.Utility;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class DeptInfoActivity extends BaseActivity{

	private TextView tv_intro;
	private ListView lv_doc;
	private GetDataTask task;
	private DoctorAdapter adapter;
	private String deptid = "";
	private String code = "";
	public ImageLoader imageLoader = ImageLoader.getInstance();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hos_dept_info);
		findView();
	}

	@Override
	public void findView() {
		findActionBar();
		Intent intent = this.getIntent();
		actionBar.setTitle(intent.getStringExtra("title"));
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

		code = intent.getStringExtra("code");
		actionBar.setRefreshTextView("科室简介", new Action() {
			@Override
			public int getDrawable() {
				return 0;
			}
			@Override
			public void performAction(View view) {
				Intent intent = new Intent(baseContext,AppointDeptInfoActivity.class);
				intent.putExtra("ksdm",code);
				intent.putExtra("type",1);
				startActivity(intent);
			}
		});
		
		tv_intro = (TextView) findViewById(R.id.tv_intro);
		if(!intent.getSerializableExtra("intro").equals(""))
		{
			tv_intro.setText(intent.getStringExtra("intro"));
			tv_intro.setVisibility(View.GONE);
		}
		lv_doc = (ListView) findViewById(R.id.lv_doc);
		
		deptid = intent.getStringExtra("deptid");
		imageLoader.init(ImageLoaderConfiguration.createDefault(this));
		task = new GetDataTask();
		task.execute();
	}

	private void setData(List<DoctorVo> list)
	{
		adapter = new DoctorAdapter(list);
		lv_doc.setAdapter(adapter);
	}
	class GetDataTask extends AsyncTask<Void, Void, ResultModel<List<DoctorVo>>>
	{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			actionBar.startTextRefresh();;
		}

		@Override
		protected void onPostExecute(ResultModel<List<DoctorVo>> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (null != result) {
				if (result.statue == Statue.SUCCESS) {
					if(result.list!=null&&result.list.size()>0)
					{
						setData(result.list);
					}
				} else {
					result.showToast(baseContext);
				}
			} else {
				Toast.makeText(baseContext, "加载失败", Toast.LENGTH_SHORT).show();
			}
			actionBar.endTextRefresh();;
		}

		@Override
		protected ResultModel<List<DoctorVo>> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			return HttpApi.getInstance().parserArray(DoctorVo.class, 
					"auth/doctor/getDoctorByDeptId", 
					new BsoftNameValuePair("id", loginUser.id),
					new BsoftNameValuePair("sn", loginUser.sn),
					new BsoftNameValuePair("deptId", deptid)
					);
		}
		
	}

	class DoctorAdapter extends BaseAdapter
	{

		private List<DoctorVo> list;
		public DoctorAdapter(List<DoctorVo> list)
		{
			this.list = list;
		}
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
				convertView = LayoutInflater.from(baseContext).inflate(R.layout.select_doctor_item_appoint, null);
				holder.iv_head = (ImageView)convertView.findViewById(R.id.iv_head);
				holder.tv_name = (TextView)convertView.findViewById(R.id.tv_name);
				holder.tv_intro = (TextView)convertView.findViewById(R.id.tv_intro);
				holder.tv_dept = (TextView)convertView.findViewById(R.id.tv_dept);
				holder.tv_profession = (TextView)convertView.findViewById(R.id.tv_profession);
				holder.btn_consult = (Button)convertView.findViewById(R.id.btn_consult);
				holder.tv_see_all = (TextView)convertView.findViewById(R.id.tv_see_all);
				holder.btn_appoint = (Button)convertView.findViewById(R.id.btn_appoint);
				holder.btn_see_docinfo = (Button)convertView.findViewById(R.id.btn_see_doctinfo);
				convertView.setTag(holder);
			}
			else
			{
				holder = (ViewHolder)convertView.getTag();
			}
			
			holder.tv_name.setText(list.get(position).name);
			holder.tv_dept.setText(list.get(position).deptname);
			holder.tv_profession.setText(list.get(position).professionaltitle);
			holder.btn_appoint.setVisibility(View.GONE);
			holder.tv_intro.setText(list.get(position).introduce);
			final String url = Constants.getHeadUrl()+Constants.getHospitalID()+"/"+list.get(position).code+"_150x150"+".png";
			holder.iv_head.setTag(url);//这行代码要写在setImageResource上面
			holder.iv_head.setImageResource(R.drawable.doc_header);
			/*if(list.get(position).sexcode!=null)
			{
				if(list.get(position).sexcode.equals("1"))//男
				{
					holder.iv_head.setImageResource(R.drawable.doc_male);
				}
				else if(list.get(position).sexcode.equals("2"))//女
				{
					holder.iv_head.setImageResource(R.drawable.doc_female);
				}
			}
			else
			{
				holder.iv_head.setImageResource(R.drawable.doc_header);
			}*/
			// 显示图片的配置
			try
			{
				new Thread() {
					public void run() {
						//这儿是耗时操作，完成之后更新UI；
						if(Utility.checkURL(url))
						{
							runOnUiThread(new Runnable(){

								@Override
								public void run() {
									//更新UI
									DisplayImageOptions options = new DisplayImageOptions.Builder()
											.cacheInMemory(true).cacheOnDisk(true)
													// .displayer(new RoundedBitmapDisplayer(20))
											.bitmapConfig(Bitmap.Config.RGB_565).build();
									imageLoader.loadImage(url, options,
											new SimpleImageLoadingListener() {

												@Override
												public void onLoadingComplete(String imageUri,
																			  View view, Bitmap loadedImage) {
													// TODO Auto-generated method stub
													super.onLoadingComplete(imageUri, view,
															loadedImage);
													if (holder.iv_head.getTag() != null
															&& holder.iv_head.getTag().equals(url)) {
														Drawable drawable = new BitmapDrawable(
																loadedImage);
														//holder.iv_head.setBackground(drawable);
														holder.iv_head.setImageDrawable(drawable);
													}
												}

											});
								}

							});
						}
					}
				}.start();
			}catch(Exception e)
			{
				e.printStackTrace();
			}
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
			holder.btn_consult.setVisibility(View.GONE);
			holder.btn_see_docinfo.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(DeptInfoActivity.this, AppointDoctorInfoActivity.class);
					intent.putExtra("ygdm",list.get(position).code);
					startActivity(intent);
				}
			});
			return convertView;
		}
		
		class ViewHolder
		{
			ImageView iv_head;
			TextView tv_name;
			TextView tv_intro;
			TextView tv_profession;
			TextView tv_dept;
			TextView tv_see_all;
			Button btn_consult;
			Button btn_appoint;
			Button btn_see_docinfo;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		AsyncTaskUtil.cancelTask(task);
		if(imageLoader!=null)
		{
			imageLoader.clearMemoryCache();
		}
	}
}
