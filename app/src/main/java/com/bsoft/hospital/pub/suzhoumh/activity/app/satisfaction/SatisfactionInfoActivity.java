package com.bsoft.hospital.pub.suzhoumh.activity.app.satisfaction;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.util.AsyncTaskUtil;
import com.app.tanklib.view.BsoftActionBar.Action;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.NullModel;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.model.satisfaction.Bussatiffactions;
import com.bsoft.hospital.pub.suzhoumh.model.satisfaction.SatisfactionListVo;
import com.bsoft.hospital.pub.suzhoumh.model.satisfaction.SatisfactionModel;
import com.bsoft.hospital.pub.suzhoumh.model.satisfaction.SectionVo;
import com.bsoft.hospital.pub.suzhoumh.util.Utility;

public class SatisfactionInfoActivity extends BaseActivity {

	private ListView listView;
	private MyAdapter adapter;
	private ArrayList<SectionVo> sectionlist = new ArrayList<SectionVo>();
	
	private GetSectionTask getSectionTask;
	private GetDetailTask getDetailTask;
	private SaveDataTask saveDataTask;
	
	private LinearLayout ll_unreply;
	private LinearLayout ll_my_reply;
	
	private EditText et_feedback;
	
	public SatisfactionListVo vo;
	private HashMap<String,Bussatiffactions> map = new HashMap<String,Bussatiffactions>();
	private TextView tv_name,tv_detail,tv_time,tv_my_reply;
	private ImageView iv_head;
	public  SatisfactionModel model;
	public Button btn_send;
	
	public HashMap<Integer,Boolean> state = new HashMap<Integer,Boolean>();
	
	public String[] contents = new String[]{"失望","不满","一般","满意","惊喜"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.satisfaction_info);
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
		listView = (ListView)findViewById(R.id.listview);
		et_feedback = (EditText)findViewById(R.id.et_feedback);
		iv_head = (ImageView)findViewById(R.id.iv_head);
		
		ll_unreply = (LinearLayout)findViewById(R.id.ll_unreply);
		ll_my_reply = (LinearLayout)findViewById(R.id.ll_my_reply);
		tv_my_reply = (TextView)findViewById(R.id.tv_my_repley);
		
		tv_name = (TextView)findViewById(R.id.tv_name);
		tv_detail = (TextView)findViewById(R.id.tv_detail);
		tv_time = (TextView)findViewById(R.id.tv_time);
		btn_send = (Button)findViewById(R.id.btn_send);
		
		adapter = new MyAdapter();
		listView.setAdapter(adapter);
		Utility.setListViewHeightBasedOnChildren(listView);
	}
	
	private void saveData()
	{
		if(!map.isEmpty())
		{
			if(!et_feedback.getText().toString().equals(""))
			{
				saveDataTask = new SaveDataTask();
				saveDataTask.execute();
			}
			else
			{
				Toast.makeText(baseContext, "请输入评价内容", Toast.LENGTH_SHORT).show();
			}
		}
		else
		{
			Toast.makeText(baseContext, "请选择满意度", Toast.LENGTH_SHORT).show();
		}
	}
	
	private void initData()
	{
		vo = (SatisfactionListVo) getIntent().getSerializableExtra("vo");
		tv_name.setText(vo.ygxm+"("+vo.ksmc+")");
		tv_detail.setText(vo.zdjg);
		tv_time.setText(vo.fwsj);
		
		if(vo.flag == 0)//未评价
		{
			getSectionTask = new GetSectionTask();
			getSectionTask.execute();
		}
		else
		{
			getDetailTask = new GetDetailTask();
			getDetailTask.execute();
		}
		
		btn_send.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				saveData();
			}
			
		});
	}
	
	/**
	 * 获取纬度
	 * @author Administrator
	 *
	 */
	private class GetSectionTask extends
			AsyncTask<Void, Void, ResultModel<ArrayList<SectionVo>>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			actionBar.startTextRefresh();
		}

		@Override
		protected ResultModel<ArrayList<SectionVo>> doInBackground(
				Void... params) {

			return HttpApi.getInstance().parserArray(SectionVo.class, "auth/satisfaction/getQuestion", 
					new BsoftNameValuePair("id", loginUser.id),
					new BsoftNameValuePair("sn",loginUser.sn)
					);
		}

		@Override
		protected void onPostExecute(ResultModel<ArrayList<SectionVo>> result) {
			super.onPostExecute(result);
			if (null != result) {
				if (result.statue == Statue.SUCCESS) {
					if (null != result.list && result.list.size() > 0) {
							sectionlist = result.list;
							adapter.notifyDataSetChanged();
							ll_unreply.setVisibility(View.VISIBLE);
							ll_my_reply.setVisibility(View.GONE);
							
					} 
				} else {
					result.showToast(baseContext);
				}
			} else {
				Toast.makeText(baseContext, "加载失败", Toast.LENGTH_SHORT).show();
			}
			actionBar.endTextRefresh();
		}
	}
	
	/**
	 * 保存数据
	 * @author Administrator
	 *
	 */
	private class SaveDataTask extends
			AsyncTask<Void, Void, ResultModel<NullModel>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			actionBar.startTextRefresh();
		}

		@Override
		protected ResultModel<NullModel> doInBackground(
				Void... params) {

			JSONObject ob = new JSONObject();
			JSONArray array = new JSONArray();
				for(int i=0;i<sectionlist.size();i++)
				{
					Bussatiffactions vo = map.get(sectionlist.get(i).iid);
					array.add(vo);
				}
			ob.put("busSatisfactions", array);
			String bus = ob.toString();
			return HttpApi.getInstance().parserArray(SectionVo.class,
					"auth/satisfaction/add",
					new BsoftNameValuePair("id", loginUser.id),
					new BsoftNameValuePair("sn", loginUser.sn),
					new BsoftNameValuePair("busdate",vo.fwsj),
					new BsoftNameValuePair("deptid",vo.ksdm),
					new BsoftNameValuePair("deptidname",vo.ksmc),
					new BsoftNameValuePair("docid",vo.yggh),
					new BsoftNameValuePair("docname",vo.ygxm),
					new BsoftNameValuePair("sug",et_feedback.getText().toString()),
					new BsoftNameValuePair("diagnosis",vo.zdjg),
					new BsoftNameValuePair("jzxh",vo.jzxh),
					new BsoftNameValuePair("busSatisfactions",bus)
					);
		}

		@Override
		protected void onPostExecute(ResultModel<NullModel> result) {
			super.onPostExecute(result);
			if (null != result) {
				if (result.statue == Statue.SUCCESS) {
					Toast.makeText(baseContext, "提交成功", Toast.LENGTH_SHORT).show();
					setResult(RESULT_OK);
					finish();
				} else {
					result.showToast(baseContext);
				}
			} else {
				Toast.makeText(baseContext, "提交失败", Toast.LENGTH_SHORT).show();
			}
			actionBar.endTextRefresh();
		}
}
	
	class GetDetailTask extends AsyncTask<Void, Void, ResultModel<SatisfactionModel>>{

		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			actionBar.startTextRefresh();
		}

		@Override
		protected void onPostExecute(ResultModel<SatisfactionModel> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (null != result) {
				if (result.statue == Statue.SUCCESS) {
					if (null != result.data ) {
						model = result.data;
						sectionlist = model.busSatisfactionExs;
						if(sectionlist!=null)
						{
							adapter.notifyDataSetChanged();
						}
						ll_my_reply.setVisibility(View.VISIBLE);
						tv_my_reply.setText(model.sug);
						ll_unreply.setVisibility(View.GONE);
					} 
				} else {
					result.showToast(baseContext);
				}
			} else {
				Toast.makeText(baseContext, "加载失败", Toast.LENGTH_SHORT).show();
			}
			actionBar.endTextRefresh();
		}

		@Override
		protected ResultModel<SatisfactionModel> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			return HttpApi.getInstance().parserData(SatisfactionModel.class, "auth/satisfaction/getDetailByJzxh", 
					new BsoftNameValuePair("id", loginUser.id),
					new BsoftNameValuePair("sn",loginUser.sn),
					new BsoftNameValuePair("jzxh",vo.jzxh)
					);
		}
		
	}
	
	class MyAdapter extends BaseAdapter
	{
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return sectionlist.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return sectionlist.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			//这里不复用，listview重新加载会checkbox会出错。
			final ViewHolder holder = new ViewHolder();
			if(convertView == null)
			{
				convertView = LayoutInflater.from(baseContext).inflate(R.layout.satisfaction_section, null);
			}
			holder.tv_title = (TextView)convertView.findViewById(R.id.tv_title);
			holder.tv_content = (TextView)convertView.findViewById(R.id.tv_content);
			holder.tv_title.setText(sectionlist.get(position).title);
			
			holder.btn_1 = (Button)convertView.findViewById(R.id.btn_star_1);
			holder.btn_2 = (Button)convertView.findViewById(R.id.btn_star_2);
			holder.btn_3 = (Button)convertView.findViewById(R.id.btn_star_3);
			holder.btn_4 = (Button)convertView.findViewById(R.id.btn_star_4);
			holder.btn_5 = (Button)convertView.findViewById(R.id.btn_star_5);
			
			if(vo.flag == 0)
			{
				holder.btn_1.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Bussatiffactions item = new Bussatiffactions();
						item.typecode = sectionlist.get(position).iid;
						item.degree = "1";
						map.put(sectionlist.get(position).iid, item);
						holder.tv_content.setText(contents[0]);
						setBtnGround(holder,1);
					}
				});
				holder.btn_2.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Bussatiffactions item = new Bussatiffactions();
						item.typecode = sectionlist.get(position).iid;
						item.degree = "2";
						map.put(sectionlist.get(position).iid, item);
						holder.tv_content.setText(contents[1]);
						setBtnGround(holder,2);
					}
				});
				holder.btn_3.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Bussatiffactions item = new Bussatiffactions();
						item.typecode = sectionlist.get(position).iid;
						item.degree = "3";
						map.put(sectionlist.get(position).iid, item);
						holder.tv_content.setText(contents[2]);
						setBtnGround(holder,3);
					}
				});
				holder.btn_4.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Bussatiffactions item = new Bussatiffactions();
						item.typecode = sectionlist.get(position).iid;
						item.degree = "4";
						map.put(sectionlist.get(position).iid, item);
						holder.tv_content.setText(contents[3]);
						setBtnGround(holder,4);
					}
				});
				holder.btn_5.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Bussatiffactions item = new Bussatiffactions();
						item.typecode = sectionlist.get(position).iid;
						item.degree = "5";
						map.put(sectionlist.get(position).iid, item);
						holder.tv_content.setText(contents[4]);
						setBtnGround(holder,5);
					}
				});
			}
			else
			{
				setBtnGround(holder,sectionlist.get(position).degree);
				holder.btn_1.setClickable(false);
				holder.btn_2.setClickable(false);
				holder.btn_3.setClickable(false);
				holder.btn_4.setClickable(false);
				holder.btn_5.setClickable(false);
			}
			/*if (sectionlist != null && sectionlist.size() > 0) {
				for (int i = 0; i < sectionlist.size(); i++) {
					final View view = LayoutInflater.from(baseContext).inflate(
							R.layout.satisfaction_section, null);
					TextView tv = (TextView) view.findViewById(R.id.tv_title);
					tv.setText(sectionlist.get(i).title);
					RadioGroup radioGroup = (RadioGroup) view
							.findViewById(R.id.radioGroup);
					final int j = i;
					if(vo.flag == 1)//已评价
					{
						String degree = getDegree(projectlist.get(position).hjid, sectionlist.get(j).iid);
						RadioButton radio1 = (RadioButton) view.findViewById(R.id.radio1);
						RadioButton radio2 = (RadioButton) view.findViewById(R.id.radio2);
						RadioButton radio3 = (RadioButton) view.findViewById(R.id.radio3);
						radio1.setEnabled(false);
						radio2.setEnabled(false);
						radio3.setEnabled(false);
						if(degree.equals("1"))
						{
							radio1.setChecked(true);
						}
						else if(degree.equals("2"))
						{
							radio2.setChecked(true);
						}
						else if(degree.equals("3"))
						{
							radio3.setChecked(true);
						}
					}
					else//未评价
					{
						//界面刷新后，设置选中状态
						Bussatiffactions satiffactions = map.get(projectlist.get(position).hjid
								+ "_" + sectionlist.get(j).iid);
						if(satiffactions!=null)
						{
							String degree = map.get(projectlist.get(position).hjid
									+ "_" + sectionlist.get(j).iid).degree;
							RadioButton radio1 = (RadioButton) view.findViewById(R.id.radio1);
							RadioButton radio2 = (RadioButton) view.findViewById(R.id.radio2);
							RadioButton radio3 = (RadioButton) view.findViewById(R.id.radio3);
							if(degree.equals("1"))
							{
								radio1.setChecked(true);
							}
							else if(degree.equals("2"))
							{
								radio2.setChecked(true);
							}
							else if(degree.equals("3"))
							{
								radio3.setChecked(true);
							}
						}
						radioGroup
						.setOnCheckedChangeListener(new OnCheckedChangeListener() {

							@Override
							public void onCheckedChanged(RadioGroup group,
									int checkedId) {
								// TODO Auto-generated method stub
								RadioButton button = (RadioButton) view
										.findViewById(checkedId);

								Bussatiffactions item = new Bussatiffactions();
								item.bustime = projectlist.get(position).fwsj;
								item.deptid = projectlist.get(position).ksdm;
								item.deptname = projectlist.get(position).ksmc;
								item.docid = projectlist.get(position).ygdm;
								item.docname = projectlist.get(position).ygxm;
								item.xxid = projectlist.get(position).hjid;
								item.tachename = projectlist.get(j).hjmc;// 环节名称
								item.typecode = sectionlist.get(j).iid;
								if (button.getText().toString()
										.equals("满意")) {
									item.degree = "1";
								} else if (button.getText().toString()
										.equals("一般")) {
									item.degree = "2";
								} else if (button.getText().toString()
										.equals("不满意")) {
									item.degree = "3";
								}
								map.put(projectlist.get(position).hjid
										+ "_" + sectionlist.get(j).iid,
										item);
							}
						});
					}
					holder.ll_section.addView(view);
				}
			}*/
			return convertView;
		}
		
		class ViewHolder
		{
			TextView tv_title;
			TextView tv_content;
			Button btn_1;
			Button btn_2;
			Button btn_3;
			Button btn_4;
			Button btn_5;
		}
		
		private void setBtnGround(ViewHolder holder,int count)
		{
			holder.btn_1.setBackgroundResource(R.drawable.ratingbar_bg_n);
			holder.btn_2.setBackgroundResource(R.drawable.ratingbar_bg_n);
			holder.btn_3.setBackgroundResource(R.drawable.ratingbar_bg_n);
			holder.btn_4.setBackgroundResource(R.drawable.ratingbar_bg_n);
			holder.btn_5.setBackgroundResource(R.drawable.ratingbar_bg_n);
			switch(count)
			{
				case 1:
					holder.btn_1.setBackgroundResource(R.drawable.ratingbar_bg_p_1);
					break;
				case 2:
					holder.btn_1.setBackgroundResource(R.drawable.ratingbar_bg_p_1);
					holder.btn_2.setBackgroundResource(R.drawable.ratingbar_bg_p_2);
					break;
				case 3:
					holder.btn_1.setBackgroundResource(R.drawable.ratingbar_bg_p_1);
					holder.btn_2.setBackgroundResource(R.drawable.ratingbar_bg_p_2);
					holder.btn_3.setBackgroundResource(R.drawable.ratingbar_bg_p_3);
					break;
				case 4:
					holder.btn_1.setBackgroundResource(R.drawable.ratingbar_bg_p_1);
					holder.btn_2.setBackgroundResource(R.drawable.ratingbar_bg_p_2);
					holder.btn_3.setBackgroundResource(R.drawable.ratingbar_bg_p_3);
					holder.btn_4.setBackgroundResource(R.drawable.ratingbar_bg_p_4);
					break;
				case 5:
					holder.btn_1.setBackgroundResource(R.drawable.ratingbar_bg_p_1);
					holder.btn_2.setBackgroundResource(R.drawable.ratingbar_bg_p_2);
					holder.btn_3.setBackgroundResource(R.drawable.ratingbar_bg_p_3);
					holder.btn_4.setBackgroundResource(R.drawable.ratingbar_bg_p_4);
					holder.btn_5.setBackgroundResource(R.drawable.ratingbar_bg_p_5);
					break;
			}	
		}
		/*@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId())
			{
			case R.id.btn_star_1:
				v.setBackgroundResource(R.drawable.ratingbar_bg_);
				break;
			case R.id.btn_star_2:
				v.setBackgroundResource(R.drawable.ratingbar_bg_p);
				break;
			case R.id.btn_star_3:
				v.setBackgroundResource(R.drawable.ratingbar_bg_p);
				break;
			case R.id.btn_star_4:
				v.setBackgroundResource(R.drawable.ratingbar_bg_p);
				break;
			case R.id.btn_star_5:
				v.setBackgroundResource(R.drawable.ratingbar_bg_p);
				break;
			}
		}*/
	}
	
	/*public String getDegree(String  xxid,String typecode)
	{
		String degree = "";
		List<Bussatiffactions> list = model.busSatisfactionExs;
		for(int i=0;i<list.size();i++)
		{
			Bussatiffactions vo = list.get(i);
			if(xxid.equals(vo.xxid)&&typecode.equals(vo.typecode))
			{
				degree = vo.degree;
				break;
			}
		}
		
		return degree;
	}*/

	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//AsyncTaskUtil.cancelTask(getProjectTask);
		AsyncTaskUtil.cancelTask(getSectionTask);
		AsyncTaskUtil.cancelTask(getDetailTask);
		AsyncTaskUtil.cancelTask(saveDataTask);
	}

	
}
