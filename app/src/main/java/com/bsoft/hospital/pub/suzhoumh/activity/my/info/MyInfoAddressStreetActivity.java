package com.bsoft.hospital.pub.suzhoumh.activity.my.info;

import java.util.ArrayList;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.app.tanklib.http.BsoftNameValuePair;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.activity.adapter.MyInfoAddressStreetAdapter;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.model.my.StreetVo;
import com.app.tanklib.view.BsoftActionBar.Action;
import com.app.tanklib.util.AsyncTaskUtil;

/**
 * @author Tank E-mail:zkljxq@126.com
 * 
 * @类说明
 */
public class MyInfoAddressStreetActivity extends BaseActivity {

	MyInfoAddressStreetAdapter adapter;
	ListView listView;
	ProgressBar emptyProgress;

	String codeId;

	GetDataTask task;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.base_list);
		codeId = getIntent().getStringExtra("id");
		findView();
		setClick();
		task = new GetDataTask();
		task.execute();
	}

	void setClick() {
		adapter = new MyInfoAddressStreetAdapter(this);
		listView.setAdapter(adapter);
	}

	@Override
	public void findView() {
		findActionBar();
		actionBar.setTitle("选择街道");
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

		listView = (ListView) findViewById(R.id.listView);
		emptyProgress = (ProgressBar) findViewById(R.id.emptyProgress);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(Constants.MyAddress_ACTION);
				intent.putExtra("type", 2);
				intent.putExtra("city4", adapter.getItem(arg2));
				sendBroadcast(intent);
				back();
			}
		});
	}

	private class GetDataTask extends
			AsyncTask<Void, Void, ResultModel<ArrayList<StreetVo>>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			emptyProgress.setVisibility(View.VISIBLE);
		}

		@Override
		protected ResultModel<ArrayList<StreetVo>> doInBackground(
				Void... params) {
			return HttpApi.getInstance().parserArray(StreetVo.class,
					"auth/ainfo/streets",
					new BsoftNameValuePair("districtid", codeId),
					new BsoftNameValuePair("id", loginUser.id),
					new BsoftNameValuePair("sn", loginUser.sn));
		}

		@Override
		protected void onPostExecute(ResultModel<ArrayList<StreetVo>> result) {
			super.onPostExecute(result);
			if (null != result) {
				if (result.statue == Statue.SUCCESS) {
					if (null != result.list && result.list.size() > 0) {
						adapter.addData(result.list);
					} 
				} else {
					result.showToast(baseContext);
				}
			} else {
				Toast.makeText(baseContext, "加载失败", Toast.LENGTH_SHORT).show();
			}
			emptyProgress.setVisibility(View.GONE);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		AsyncTaskUtil.cancelTask(task);
	}

}
