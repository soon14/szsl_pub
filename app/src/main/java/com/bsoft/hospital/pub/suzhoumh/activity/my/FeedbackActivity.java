package com.bsoft.hospital.pub.suzhoumh.activity.my;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.app.tanklib.http.BsoftNameValuePair;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.NullModel;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.app.tanklib.view.BsoftActionBar.Action;
import com.app.tanklib.util.AsyncTaskUtil;
import com.app.tanklib.util.StringUtil;

/**
 * @author Tank E-mail:zkljxq@126.com
 * 
 * @类说明
 */
public class FeedbackActivity extends BaseActivity {

	View mainView;
	EditText edit;
	GetTask task;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback);
		findView();
		setClick();
	}

	void setClick() {
		mainView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);
				return false;
			}
		});
	}

	@Override
	public void findView() {
		findActionBar();
		actionBar.setTitle("意见反馈");
		actionBar.setBackAction(new Action() {
			@Override
			public void performAction(View view) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);
				back();
			}

			@Override
			public int getDrawable() {
				return R.drawable.btn_back;
			}
		});
		actionBar.setRefreshTextView("提交", new Action() {

			@Override
			public void performAction(View view) {
				if (StringUtil.isEmpty(edit.getText().toString())) {
					edit.requestFocus();
					Toast.makeText(baseContext, " 反馈信息为空，请输入",
							Toast.LENGTH_SHORT).show();
					return;
				}
				task = new GetTask();
				task.execute();
			}

			@Override
			public int getDrawable() {
				return 0;
			}
		});
		mainView = findViewById(R.id.mainView);
		edit = (EditText) findViewById(R.id.edit);
	}

	class GetTask extends AsyncTask<Void, Object, ResultModel<NullModel>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			actionBar.startTextRefresh();
		}

		@Override
		protected ResultModel<NullModel> doInBackground(Void... params) {
			return HttpApi.getInstance()
					.parserData(
							NullModel.class,
							"auth/advise/add",
							new BsoftNameValuePair("id", loginUser.id),
							new BsoftNameValuePair("kinds", "1"),
							new BsoftNameValuePair("content", edit.getText()
									.toString()),
							new BsoftNameValuePair("sn", loginUser.sn));
		}

		@Override
		protected void onPostExecute(ResultModel<NullModel> result) {
			actionBar.endTextRefresh();
			if (null != result) {
				if (result.statue == Statue.SUCCESS) {
					Toast.makeText(baseContext, "反馈信息成功", Toast.LENGTH_SHORT)
							.show();
					edit.setText("");
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);
					back();
				} else {
					result.showToast(baseContext);
				}
			}
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		AsyncTaskUtil.cancelTask(task);
	}

}
