package com.bsoft.hospital.pub.suzhoumh.activity.app.healthtool;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.view.BsoftActionBar.Action;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;

public class MedicineDetailActivity extends BaseActivity{

	private TextView tv_title;
	private TextView tv_usagedosage;
	private TextView tv_healfun;
	private TextView tv_adversereactions;
	private TextView tv_taboo;
	private TextView tv_approvaldoc;
	private TextView tv_indfun;
	private TextView tv_english;
	private TextView tv_alias;
	
	private GetDataTask task;
	
	private MedicDetail medicdetail;
	
	private String medic_id;//药品id
	
	private LinearLayout ll_content;
	
	private LinearLayout ll_english;
	private LinearLayout ll_alias;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.medicinedetail);
		findView();
		initView();
		initData();
	}

	@Override
	public void findView() {
		// TODO Auto-generated method stub
		findActionBar();
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
			}});
		actionBar.setTitle("药品详情");
	}
	
	private void initView()
	{
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_usagedosage = (TextView) findViewById(R.id.tv_usagedosage);
		tv_healfun = (TextView) findViewById(R.id.tv_healfun);
		tv_adversereactions = (TextView) findViewById(R.id.tv_adversereactions);
		tv_taboo = (TextView) findViewById(R.id.tv_taboo);
		tv_approvaldoc = (TextView) findViewById(R.id.tv_approvaldoc);
		tv_indfun = (TextView)findViewById(R.id.tv_indfun);
		tv_english = (TextView)findViewById(R.id.tv_english);
		tv_alias = (TextView)findViewById(R.id.tv_alias);
		ll_content = (LinearLayout)findViewById(R.id.ll_content);
		
		ll_english = (LinearLayout)findViewById(R.id.ll_english);
		ll_alias = (LinearLayout)findViewById(R.id.ll_alias);
	}
	
	private void initData()
	{
		medic_id = getIntent().getStringExtra("medic_id");
		ll_content.setVisibility(View.GONE);
		task = new GetDataTask();
		task.execute();
	}
	
	/**
	 * 查询任务
	 * @author Administrator
	 *
	 */
	private class GetDataTask extends
			AsyncTask<Void, Void, ResultModel<MedicDetail>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			actionBar.startTextRefresh();;
		}

		@Override
		protected ResultModel<MedicDetail> doInBackground(
				Void... params) {
			return HttpApi.getInstance().parserData(MedicDetail.class,
					"auth/drug/detail", 
					new BsoftNameValuePair("rid",medic_id),
					new BsoftNameValuePair("sn", loginUser.sn),
					new BsoftNameValuePair("id", loginUser.id)
					//new BsoftNameValuePair("t","1")
					);
		}

		@Override
		protected void onPostExecute(ResultModel<MedicDetail> result) {
			super.onPostExecute(result);
			if (null != result) {
				if (result.statue == Statue.SUCCESS) {
					if (null != result.data)
					{
						medicdetail = result.data;
						setData();
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
	
	private void setData()
	{
		tv_title.setText(medicdetail.title);
		tv_adversereactions.setText(medicdetail.adversereactions);
		tv_approvaldoc.setText(medicdetail.approvaldoc);
		tv_healfun.setText(medicdetail.healfun);
		tv_indfun.setText(medicdetail.indfun);
		tv_taboo.setText(medicdetail.taboo);
		tv_usagedosage.setText(medicdetail.usagedosage);
		
		if(medicdetail.englishname!=null&&!medicdetail.englishname.equals(""))
		{
			tv_english.setText(medicdetail.englishname);
		}
		else
		{
			ll_english.setVisibility(View.GONE);
		}
		if(medicdetail.alias!=null&&!medicdetail.alias.equals(""))
		{
			tv_alias.setText(medicdetail.alias);
		}
		else
		{
			ll_alias.setVisibility(View.GONE);
		}
		ll_content.setVisibility(View.VISIBLE);
	}
}
