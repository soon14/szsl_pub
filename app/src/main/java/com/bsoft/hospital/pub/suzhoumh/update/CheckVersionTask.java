package com.bsoft.hospital.pub.suzhoumh.update;

import com.bsoft.hospital.pub.suzhoumh.view.AppProgressDialog;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

/**
 * @author Tank E-mail:zkljxq@126.com
 * 
 * @类说明 更新任务
 */
public class CheckVersionTask extends AsyncTask<Void, Object, Boolean> {
	UpdateVersion uv;
	Context context;
	AppProgressDialog progressDialog;

	public CheckVersionTask(Context context, String dir) {
		this.uv = new UpdateVersion(context, dir);
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (progressDialog == null) {
			progressDialog = new AppProgressDialog(context, "版本检测中...");
		}
		progressDialog.start();
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		if (null != uv) {
			return uv.isUpdate();
		}
		return false;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		if (progressDialog != null) {
			progressDialog.stop();
			progressDialog = null;
		}
		if (result) {
			uv.doNewVersionUpdate();
		} else {
			// uv.notNewVersionShow();
			Toast.makeText(context, "已是最新版,无需更新!", Toast.LENGTH_SHORT).show();
		}
	}

}
