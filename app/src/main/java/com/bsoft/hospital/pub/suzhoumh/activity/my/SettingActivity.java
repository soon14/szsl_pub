package com.bsoft.hospital.pub.suzhoumh.activity.my;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.tanklib.Preferences;
import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.util.AppInfoUtil;
import com.app.tanklib.util.StringUtil;
import com.app.tanklib.view.BsoftActionBar.Action;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.AppApplication;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.activity.account.LoginActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.update.CheckVersionTask;
import com.bsoft.hospital.pub.suzhoumh.update.checkupdate.UpdateVersionNew;
import com.umeng.analytics.MobclickAgent;

/**
 * @author Tank E-mail:zkljxq@126.com
 * @类说明
 */
public class SettingActivity extends BaseActivity {

    RelativeLayout layout1, layout2, layout3, layout4, layout5;
    TextView newV, version;
    boolean isUpdate = false;

    LayoutInflater mLayoutInflater;
    Dialog builder;
    View viewDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        this.mLayoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        findView();
        setClick();
    }

    void setClick() {
        layout1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(baseContext,
                        SettingMsgActivity.class);
                startActivity(intent);
            }
        });
        layout2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(baseContext, FeedbackActivity.class);
                startActivity(intent);
            }
        });
        layout3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(baseContext, GuideActivity.class);
                intent.putExtra("flage", 2);
				startActivity(intent);*/
                Intent intent = new Intent(baseContext, SettingAboutActivity.class);
                intent.putExtra("title", "关于");
                startActivity(intent);
            }
        });
        layout4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//				CheckVersionTask task = new CheckVersionTask(baseContext,
//						((AppApplication) getApplication()).getStoreDir());
//				task.execute();
                new UpdateVersionNew(baseContext, application, true);
            }
        });
        layout5.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
				/*Intent intent = new Intent(baseContext,
						SettingAccountActivity.class);
				startActivity(intent);*/
                Intent intent = new Intent(baseContext,
                        SettingPwdActivity.class);
                startActivity(intent);
            }
        });

        //退出登录
        findViewById(R.id.logout).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                builder = new Dialog(baseContext, R.style.alertDialogTheme);
                builder.show();
                viewDialog = mLayoutInflater.inflate(R.layout.logout_alert,
                        null);

                // 设置对话框的宽高
                LayoutParams layoutParams = new LayoutParams(AppApplication
                        .getWidthPixels() * 85 / 100, LayoutParams.WRAP_CONTENT);
                builder.setContentView(viewDialog, layoutParams);
                viewDialog.findViewById(R.id.delect).setOnClickListener(
                        new OnClickListener() {
                            @Override
                            public void onClick(View arg0) {
                                builder.dismiss();
                                application.clearInfo();
                                Preferences.getInstance().setStringData(
                                        "index", null);
                                Preferences.getInstance().setStringData(
                                        "weather", null);
                                Preferences.getInstance().setStringData(
                                        "dynamic", null);
                                MobclickAgent.onProfileSignOff();
                                new Thread() {
                                    public void run() {
                                        // 退出接口
                                        HttpApi.getInstance().post(
                                                "auth/ainfo/logout",
                                                new BsoftNameValuePair("id",
                                                        loginUser.id),
                                                new BsoftNameValuePair("sn",
                                                        loginUser.sn));
                                    }
                                }.start();
                                sendBroadcast(new Intent(Constants.CLOSE_ACTION));
                                Intent intent = new Intent(
                                        SettingActivity.this,
                                        LoginActivity.class);
                                startActivity(intent);
                            }
                        });
                viewDialog.findViewById(R.id.cancel).setOnClickListener(
                        new OnClickListener() {
                            @Override
                            public void onClick(View arg0) {
                                builder.dismiss();
                            }
                        });
            }
        });

        version.setText("V" + AppInfoUtil.getVersionName(SettingActivity.this));
        String u = Preferences.getInstance().getStringData("newUp");
        if (!StringUtil.isEmpty(u)) {
            if ("1".equals(u)) {
                newV.setVisibility(View.VISIBLE);
                isUpdate = true;
            }
        }
    }

    @Override
    public void findView() {
        findActionBar();
        actionBar.setTitle("设置");
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
        layout1 = (RelativeLayout) findViewById(R.id.layout1);
        layout2 = (RelativeLayout) findViewById(R.id.layout2);
        layout3 = (RelativeLayout) findViewById(R.id.layout3);
        layout4 = (RelativeLayout) findViewById(R.id.layout4);
        layout5 = (RelativeLayout) findViewById(R.id.layout5);
        version = (TextView) findViewById(R.id.version);
        newV = (TextView) findViewById(R.id.newV);
    }

}
