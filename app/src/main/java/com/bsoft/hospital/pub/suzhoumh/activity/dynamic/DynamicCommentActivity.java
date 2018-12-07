package com.bsoft.hospital.pub.suzhoumh.activity.dynamic;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.Comment;
import com.bsoft.hospital.pub.suzhoumh.model.DynamicShow;
import com.bsoft.hospital.pub.suzhoumh.model.NullModel;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.app.tanklib.view.BsoftActionBar.Action;
import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.util.AsyncTaskUtil;
import com.app.tanklib.util.StringUtil;

/**
 * @author Tank E-mail:zkljxq@126.com
 * 
 * @类说明
 */
public class DynamicCommentActivity extends BaseActivity {

	ProgressBar emptyProgress;
	GetDataTask getDataTask;

	View mainView;
	EditText edit;

	DynamicShow vo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dynamic_comment);
		vo = (DynamicShow) getIntent().getSerializableExtra("vo");
		findView();
		setClick();
		// getDataTask = new GetDataTask();
		// getDataTask.execute();
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
		actionBar.setTitle("评论");
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
		actionBar.setRefreshTextView("保存", new Action() {

			@Override
			public void performAction(View view) {
				if (StringUtil.isEmpty(edit.getText().toString().trim())) {
					Toast.makeText(baseContext, "评论不能为空", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				if (edit.getText().toString().length() > 200) {
					Toast.makeText(baseContext, "评论数不多于200字",
							Toast.LENGTH_SHORT).show();
					return;
				}
				getDataTask = new GetDataTask();
				getDataTask.execute();
			}

			@Override
			public int getDrawable() {
				return 0;
			}
		});
		emptyProgress = (ProgressBar) findViewById(R.id.emptyProgress);
		mainView = findViewById(R.id.mainView);
		edit = (EditText) findViewById(R.id.edit);
	}

	private class GetDataTask extends
			AsyncTask<Void, Void, ResultModel<NullModel>> {
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
							"auth/dynamic/reply/add",
							new BsoftNameValuePair("id", loginUser.id),
							new BsoftNameValuePair("uid", loginUser.id),
							new BsoftNameValuePair("ruid", String.valueOf(vo.uid)),
							new BsoftNameValuePair("kinds", "1"),
							new BsoftNameValuePair("drid", vo.drid),
							new BsoftNameValuePair("content", edit.getText()
									.toString()),
							new BsoftNameValuePair("sn", loginUser.sn));
		}

		@Override
		protected void onPostExecute(ResultModel<NullModel> result) {
			super.onPostExecute(result);
			if (null != result) {
				if (result.statue == Statue.SUCCESS) {
					Comment comment = new Comment();
					comment.content = edit.getText().toString();
					comment.createdate = System.currentTimeMillis();
					comment.header = loginUser.header;
					comment.realname = loginUser.realname;
					comment.drid = vo.drid;
					Intent intent = new Intent(Constants.Dynamic_comment_ACTION);
					intent.putExtra("vo", comment);
					sendBroadcast(intent);
					Toast.makeText(baseContext, "评论成功", Toast.LENGTH_SHORT)
							.show();
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);
					back();
				} else {
					result.showToast(baseContext);
				}
			} else {
				Toast.makeText(baseContext, "评论失败，请重试", Toast.LENGTH_SHORT)
						.show();
			}
			actionBar.endTextRefresh();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		AsyncTaskUtil.cancelTask(getDataTask);
	}

}
