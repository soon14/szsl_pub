package com.bsoft.hospital.pub.suzhoumh.activity.my;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.util.AsyncTaskUtil;
import com.app.tanklib.view.BsoftActionBar.Action;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.NullModel;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.SettingMsgVo;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;

/**
 * @author Tank E-mail:zkljxq@126.com
 * 
 * @类说明
 */
public class SettingMsgActivity extends BaseActivity {

	CheckBox checkbox1, checkbox5, checkbox6;
	TextView text1, text5, text6;

	public SettingMsgVo settingMsgVo;

	GetDataTask getDataTask;
	
	private LinearLayout ll_msg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settingmsg);
		findView();
		getDataTask = new GetDataTask();
		getDataTask.execute();
	}

	void setClick() {
		
		if(settingMsgVo.msg == 0)
		{
			ll_msg.setVisibility(View.GONE);
		}
		else
		{
			ll_msg.setVisibility(View.VISIBLE);
		}
			
		checkbox1.setChecked(settingMsgVo.msg == 1);
		checkbox5.setChecked(settingMsgVo.sound == 1);
		checkbox6.setChecked(settingMsgVo.shock == 1);
		
		checkbox1.setOnCheckedChangeListener(listener);
		checkbox5.setOnCheckedChangeListener(listener);
		checkbox6.setOnCheckedChangeListener(listener);
	}

	OnCheckedChangeListener listener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton v, boolean checked) {
			switch (v.getId()) {
			case R.id.checkbox1:
				settingMsgVo.msg = checked ? 1 : 0;
				if(settingMsgVo.msg == 0)
				{
					ll_msg.setVisibility(View.GONE);
				}
				else
				{
					ll_msg.setVisibility(View.VISIBLE);
				}
				
				break;
			case R.id.checkbox5:
				settingMsgVo.sound = checked ? 1 : 0;
				break;
			case R.id.checkbox6:
				settingMsgVo.shock = checked ? 1 : 0;
				break;
			default:
				break;
			}
		}

	};

	@Override
	public void findView() {
		findActionBar();
		actionBar.setTitle("消息通知");
		actionBar.setBackAction(new Action() {
			@Override
			public void performAction(View view) {
				if(settingMsgVo!=null)
				{
					new SaveTask().execute();
				}
				back();
			}

			@Override
			public int getDrawable() {
				return R.drawable.btn_back;
			}
		});
		checkbox1 = (CheckBox) findViewById(R.id.checkbox1);
		checkbox5 = (CheckBox) findViewById(R.id.checkbox5);
		checkbox6 = (CheckBox) findViewById(R.id.checkbox6);
		text1 = (TextView) findViewById(R.id.text1);
		text5 = (TextView) findViewById(R.id.text5);
		text6 = (TextView) findViewById(R.id.text6);
		ll_msg = (LinearLayout) findViewById(R.id.ll_msg);
	}

	private class GetDataTask extends
			AsyncTask<Void, Void, ResultModel<SettingMsgVo>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			actionBar.startTextRefresh();;

		}

		// fuck
		@Override
		protected ResultModel<SettingMsgVo> doInBackground(Void... params) {
			return HttpApi.getInstance().parserData(SettingMsgVo.class,
					"auth/appnotice/get",
					new BsoftNameValuePair("id", loginUser.id),
					new BsoftNameValuePair("sn", loginUser.sn));
		}

		@Override
		protected void onPostExecute(ResultModel<SettingMsgVo> result) {
			super.onPostExecute(result);
			if (null != result) {
				if (result.statue == Statue.SUCCESS) {
					checkbox1.setVisibility(View.VISIBLE);
					checkbox5.setVisibility(View.VISIBLE);
					checkbox6.setVisibility(View.VISIBLE);
					text1.setVisibility(View.GONE);
					text5.setVisibility(View.GONE);
					text6.setVisibility(View.GONE);
					if (null == result.data) {
						settingMsgVo = new SettingMsgVo();
					} else {
						settingMsgVo = result.data;
					}
					setClick();
				} else {
					text1.setText("加载失败");
					text5.setText("加载失败");
					text6.setText("加载失败");
					result.showToast(baseContext);
				}
			} else {
				text1.setText("加载失败");
				text5.setText("加载失败");
				text6.setText("加载失败");
				Toast.makeText(baseContext, "加载失败", Toast.LENGTH_SHORT).show();
			}
			actionBar.endTextRefresh();;
		}
	}

	class SaveTask extends AsyncTask<String, Object, ResultModel<NullModel>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		// fuck
		@Override
		protected ResultModel<NullModel> doInBackground(String... params) {
			return HttpApi.getInstance().parserData(
					NullModel.class,
					"auth/appnotice/set",
					new BsoftNameValuePair("uid", loginUser.id),
					new BsoftNameValuePair("text",settingMsgVo.toJsonString()),
					new BsoftNameValuePair("id", loginUser.id),
					new BsoftNameValuePair("sn", loginUser.sn));
		}

		@Override
		protected void onPostExecute(ResultModel<NullModel> result) {
			if (null != result) {
				if (result.statue == Statue.SUCCESS) {
					application.setSettingMsg(settingMsgVo);
				} else {
					result.showToast(baseContext);
				}
			}
		}
	}
	
	
	public String getText(){
		JSONObject ob=new JSONObject();
		
		
		return ob.toString();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		AsyncTaskUtil.cancelTask(getDataTask);
	}

}
