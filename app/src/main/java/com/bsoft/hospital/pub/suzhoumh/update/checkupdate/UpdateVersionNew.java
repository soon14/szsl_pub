package com.bsoft.hospital.pub.suzhoumh.update.checkupdate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;

import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.util.AppInfoUtil;
import com.bsoft.hospital.pub.suzhoumh.AppApplication;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.update.UpdateInfo;
import com.bsoft.hospital.pub.suzhoumh.util.ObjectUtil;
import com.bsoft.hospital.pub.suzhoumh.util.ToastUtils;
import com.bsoft.hospital.pub.suzhoumh.view.AppProgressDialog;
import com.qiangxi.checkupdatelibrary.CheckUpdateOption;
import com.qiangxi.checkupdatelibrary.CheckUpdate;

/**
 * @author :lizhengcao
 * @date :2018/10/11
 * E-mail:lizc@bsoft.com.cn
 * @类说明 版本更新类
 */

public class UpdateVersionNew {
    private Context mContext;
    private boolean checkNewUpdate;
    private AppProgressDialog progressDialog;
    private String appName;
    private AppApplication application;

    public UpdateVersionNew(Context context, AppApplication application, boolean checkNewUpdate) {
        appName = context.getResources().getString(R.string.app_name);
        this.mContext = context;
        this.checkNewUpdate = checkNewUpdate;//true 表示检测是否有版本，false不检测
        this.application = application;
        new GetUpdateTask().execute();
    }

    /**
     * 版本更新的实现类
     */
    @SuppressLint("StaticFieldLeak")
    @SuppressWarnings("unchecked")
    class GetUpdateTask extends AsyncTask<Void, Void, ResultModel<UpdateInfo>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (checkNewUpdate) {
                //检测新本版时调用
                if (progressDialog == null) {
                    progressDialog = new AppProgressDialog(mContext, "版本检测中...");
                }
                progressDialog.start();
            }

        }

        @Override
        protected ResultModel<UpdateInfo> doInBackground(Void... voids) {
            return HttpApi.getInstance().parserData(
                    UpdateInfo.class,
                    "version",
                    new BsoftNameValuePair("appcode", "android_pub"),
                    new BsoftNameValuePair("appversion", AppInfoUtil
                            .getVersionCode(mContext) + ""));
        }

        @Override
        protected void onPostExecute(ResultModel<UpdateInfo> result) {
            super.onPostExecute(result);
            if (checkNewUpdate) {
                if (progressDialog != null) {
                    progressDialog.stop();
                    progressDialog = null;
                }
            }
            UpdateInfo data = result.data;
            //对于版本号>=服务器版本号时，此时不返回任何字段
            if (ObjectUtil.isEmpty(data)) {
                if (checkNewUpdate)
                    ToastUtils.showToastShort("已是最新版,无需更新!");
            } else {
                if (!ObjectUtil.isEmpty(data.appversion))
                    if (data.appversion > AppInfoUtil.getVersionCode(mContext))
                        //服务器的版本大于apk自身版本，更新
                        CheckUpdate.show((FragmentActivity) mContext, generateOption(data));
            }
        }
    }


    /**
     * @param type 1表示非强制更新  2表示强制更新
     * @return
     */
    private static final String unForceUpdateType = "1";
    private static final String forceUpdateType = "2";

    private boolean isForceUpdate(String type) {
        if (forceUpdateType.equals(type))
            return true;
        else if (unForceUpdateType.equals(type))
            return false;
        else
            return false;
    }


    public CheckUpdateOption generateOption(UpdateInfo data) {

        String url = data.appurl;
        String fileName = url.substring(url.lastIndexOf("/") + 1);
        return new CheckUpdateOption.Builder()
                .setAppName(appName)
                .setFileName(fileName)
                .setFilePath(application.getStoreDir())
                .setIsForceUpdate(isForceUpdate(data.type))
                .setNewAppUpdateDesc(data.des)
                .setNewAppUrl(Constants.getHttpUrl() + url)
                .setNotificationSuccessContent("下载成功，点击安装")
                .setNotificationFailureContent("下载失败，点击重新下载")
                .setNotificationIconResId(R.drawable.ic_sllogo_update)
                .setNotificationTitle(appName)
                .build();
    }
}
