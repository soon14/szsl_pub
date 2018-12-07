package com.bsoft.hospital.pub.suzhoumh.update;

import android.content.Context;
import android.os.AsyncTask;


/**
 * @author Tank   E-mail:zkljxq@126.com
 * 
 * @类说明 更新任务
 */
public class UpdateVersionTask extends AsyncTask<Void, Object, Boolean> {
	UpdateVersion uv;

	// public UpdateVersionTask(UpdateVersionNew uv) {
	// this.uv = uv;
	// }

	public UpdateVersionTask(Context context, String dir) {
		this.uv = new UpdateVersion(context, dir);
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
		if (result) {
			uv.doNewVersionUpdate();
		}
	}

}
