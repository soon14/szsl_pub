package com.bsoft.hospital.pub.suzhoumh.activity.my.info;

import java.util.ArrayList;

import android.app.Dialog;
import android.widget.LinearLayout.LayoutParams;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.app.tanklib.http.BsoftNameValuePair;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.AppApplication;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.activity.adapter.MyContactsAdapter;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.NullModel;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.model.my.MyContactVo;
import com.app.tanklib.view.BsoftActionBar.Action;

/**
 * @author Tank E-mail:zkljxq@126.com
 * 
 * @类说明
 */
public class MyContactsActivity extends BaseActivity {

	MyContactsAdapter adapter;
	ListView listView;
	ProgressBar emptyProgress;

	ArrayList<MyContactVo> datas = new ArrayList<MyContactVo>();

	Dialog builder;
	View viewDialog;
	public LayoutInflater mLayoutInflater;

	DelTask task;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mycontacts);
		datas = (ArrayList<MyContactVo>) getIntent().getSerializableExtra(
				"datas");
		mLayoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		findView();
		setClick();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.MyContacts_ACTION);
		filter.addAction(Constants.MyContactsEdit_ACTION);
		this.registerReceiver(this.broadcastReceiver, filter);
	}

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (Constants.MyContacts_ACTION.equals(intent.getAction())) {
				Intent mintent = new Intent(Constants.MyInfo_ACTION);
				mintent.putExtra("index", 8);
				mintent.putExtra("value",
						"已绑定" + ((null == datas) ? 1 : (datas.size() + 1))
								+ "个");
				mintent.putExtra("vo", (MyContactVo) intent.getSerializableExtra("vo"));
				sendBroadcast(mintent);
				adapter.addData((MyContactVo) intent.getSerializableExtra("vo"));
			} else if (Constants.MyContactsEdit_ACTION.equals(intent
					.getAction())) {
				adapter.changeData((MyContactVo) intent
						.getSerializableExtra("vo"));
			}
		}
	};

	void setClick() {
		adapter = new MyContactsAdapter(this);
		adapter.addData(datas);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(baseContext,
						MyContactsAddActivity.class);
				intent.putExtra("vo", adapter.getItem(arg2));
				startActivity(intent);
			}
		});
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {
				builder = new Dialog(baseContext, R.style.alertDialogTheme);
				builder.show();
				viewDialog = mLayoutInflater.inflate(R.layout.delect_alert,
						null);

				// 设置对话框的宽高
				LayoutParams layoutParams = new LayoutParams(AppApplication
						.getWidthPixels() * 85 / 100, LayoutParams.WRAP_CONTENT);
				builder.setContentView(viewDialog, layoutParams);
				viewDialog.findViewById(R.id.delect).setOnClickListener(
						new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								task = new DelTask(arg2,
										adapter.getItem(arg2).id);
								task.execute();
								builder.dismiss();
							}
						});
				viewDialog.findViewById(R.id.cancel).setOnClickListener(
						new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								builder.dismiss();
							}
						});
				return true;
			}
		});
	}

	@Override
	public void findView() {
		findActionBar();
		actionBar.setTitle("联系人");
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
		actionBar.addAction(new Action() {

			@Override
			public void performAction(View view) {
				Intent intent = new Intent(baseContext,
						MyContactsAddActivity.class);
				startActivity(intent);
			}

			@Override
			public int getDrawable() {
				return R.drawable.btn_add;
			}
		});

		listView = (ListView) findViewById(R.id.listView);
		emptyProgress = (ProgressBar) findViewById(R.id.emptyProgress);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (null != broadcastReceiver) {
			unregisterReceiver(broadcastReceiver);
			broadcastReceiver = null;
		}
	}

	class DelTask extends AsyncTask<Void, Object, ResultModel<NullModel>> {

		int postion;
		String id;

		public DelTask(int postion, String id) {
			this.postion = postion;
			this.id = id;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			actionBar.startTextRefresh();;
			adapter.remove(postion);
		}

		@Override
		protected ResultModel<NullModel> doInBackground(Void... params) {
			return HttpApi.getInstance().parserData(NullModel.class,
					"auth/ainfo/contact/del",
					new BsoftNameValuePair("id", loginUser.id),
					new BsoftNameValuePair("cid", id),
					new BsoftNameValuePair("sn", loginUser.sn));
		}

		@Override
		protected void onPostExecute(ResultModel<NullModel> result) {
			actionBar.endTextRefresh();;
			if (null != result) {
				if (result.statue == Statue.SUCCESS) {
					Toast.makeText(baseContext, "删除联系人成功", Toast.LENGTH_SHORT)
							.show();
					Intent intent = new Intent(Constants.MyContactsDel_ACTION);
					intent.putExtra("postion", postion);
					sendBroadcast(intent);
				} else {
					result.showToast(baseContext);
				}
			}
		}
	}

}
