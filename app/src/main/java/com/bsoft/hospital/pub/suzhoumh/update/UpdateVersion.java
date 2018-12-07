package com.bsoft.hospital.pub.suzhoumh.update;

import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.AppApplication;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.app.tanklib.Preferences;
import com.app.tanklib.dialog.AlertDialog;
import com.app.tanklib.dialog.AlertDialogWithButton;
import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.util.AppInfoUtil;
import com.app.tanklib.util.StringUtil;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * @author Tank E-mail:zkljxq@126.com
 * @类说明
 */
public class UpdateVersion {

    public Context context;
    // private int newVerCode = 0;
    // public String des;
    // private String newVerName = "";
    UpdateInfo updateInfoVo;
    String downloadDir;

    AlertDialog dialog;
    AlertDialogWithButton dialogWithButton;

    // public UpdateVersionNew(Context context) {
    // this.context = context;
    // }

    public UpdateVersion(Context context, String downloadDir) {
        this.context = context;
        this.downloadDir = downloadDir;
    }

    public void version() {
        if (getServerVerCode()) {
            if (!StringUtil.isEmpty(updateInfoVo.des)
                    && !StringUtil.isEmpty(updateInfoVo.appurl)) {
                Preferences.getInstance().setStringData("newUp", "1");
                doNewVersionUpdate();
            } else {
                Preferences.getInstance().setStringData("newUp", "0");
                notNewVersionShow();
            }
        }
    }

    public boolean isUpdate() {
        if (getServerVerCode()) {
            if (!StringUtil.isEmpty(updateInfoVo.des)
                    && !StringUtil.isEmpty(updateInfoVo.appurl)) {
                Preferences.getInstance().setStringData("newUp", "1");
                return true;
            }
        }
        Preferences.getInstance().setStringData("newUp", "0");
        return false;
    }


    public boolean getServerVerCode() {
        ResultModel<UpdateInfo> model = HttpApi.getInstance().parserData(
                UpdateInfo.class,
                "version",
                new BsoftNameValuePair("appcode", "android_pub"),
                new BsoftNameValuePair("appversion", AppInfoUtil
                        .getVersionCode(context) + ""));
        if (null != model && model.statue == Statue.SUCCESS
                && null != model.data) {
            this.updateInfoVo = model.data;
            return true;
        }
        return false;
    }

    public void notNewVersionShow() {
        String verName = AppInfoUtil.getVersionName(context);
        StringBuffer sb = new StringBuffer();
        sb.append("当前版本:");
        sb.append(verName);
        sb.append(",\n已是最新版,无需更新!");
        // Dialog dialog = new AlertDialog.Builder(context).setTitle("软件更新")
        // .setMessage(sb.toString())// 设置内容
        // .setPositiveButton("确定",// 设置确定按钮
        // new DialogInterface.OnClickListener() {
        // @Override
        // public void onClick(DialogInterface dialog,
        // int which) {
        // // finish();
        // }
        // }).create();// 创建
        // // 显示对话框
        // dialog.show();

        dialog = new AlertDialog(context)
                .build(false, AppApplication.getWidthPixels() * 85 / 100)
                .message(sb.toString()).color(R.color.actionbar_bg)
                .setButton("确定");
        dialog.show();

    }

    public void doNewVersionUpdate() {
        String verName = AppInfoUtil.getVersionName(context);
        StringBuffer sb = new StringBuffer();
        sb.append("当前版本:");
        sb.append(verName);
        sb.append("\n");
        sb.append("最新版本:");
        if (updateInfoVo.appversion > AppInfoUtil.getVersionCode(context)) {
            sb.append(verName.split("\\.")[0]);
            sb.append(".");
            sb.append(updateInfoVo.appversion);
        }
//         sb.append(updateInfoVo.Value);
        sb.append("\n");
        sb.append("更新内容:");
        sb.append("\n");
        if (null != updateInfoVo.des) {
            sb.append(Html.fromHtml(updateInfoVo.des));
//            sb.append(StringUtil.replaceAll(updateInfoVo.des, "\\n", "\n"));
        }
        if (null != updateInfoVo && updateInfoVo.type.equals("2")) {
            dialog = new AlertDialog(context)
                    .build(false, AppApplication.getWidthPixels() * 85 / 100)
                    .title("软件更新").message(sb.toString())
                    .color(R.color.actionbar_bg)
                    .setButton("更新", new OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            downFile();
                            dialog.dismiss();
                        }
                    });
            dialog.show();
        } else {
            dialogWithButton = new AlertDialogWithButton(context)
                    .build(false, AppApplication.getWidthPixels() * 85 / 100)
                    .title("软件更新").message(sb.toString()).color(R.color.actionbar_bg)
                    .setPositiveButton("更新", new OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            downFile();
                            dialogWithButton.dismiss();
                        }
                    }).setNegativeButton("暂不更新");
            dialogWithButton.show();
        }

    }

    void downFile() {
        UpdateThread updateTh = new UpdateThread(context, downloadDir,
                updateInfoVo);
        updateTh.start();
    }

    // void downFile() {
    // // download.show();
    // // downThread = new DownThread();
    // // downThread.start();
    // Intent intent = new Intent();
    // intent.setAction("android.intent.action.VIEW");
    // Uri content_url = Uri.parse(new StringBuffer(ShowApi.newUrl).append(
    // Config.UPDATE_APKNAME).toString());
    // intent.setData(content_url);
    // context.startActivity(intent);
    // }

    // void install() {
    // Intent intent = new Intent(Intent.ACTION_VIEW);
    // intent.setDataAndType(Uri.fromFile(new File(downloadDir,
    // Config.UPDATE_SAVENAME)),
    // "application/vnd.android.package-archive");
    // context.startActivity(intent);
    // }

}
