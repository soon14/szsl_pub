package com.bsoft.hospital.pub.suzhoumh.activity.base;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.tanklib.bitmap.IndexUrlCache;
import com.app.tanklib.view.EmptyLayout;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.AppApplication;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.activity.account.LoginActivity;
import com.bsoft.hospital.pub.suzhoumh.model.LoginUser;
import com.bsoft.hospital.pub.suzhoumh.model.my.PersonVo;
import com.bsoft.hospital.pub.suzhoumh.push.PushInfo;
import com.bsoft.hospital.pub.suzhoumh.util.AntiHijackingUtil;
import com.umeng.analytics.MobclickAgent;

import butterknife.Unbinder;

/**
 * Activity 基类
 *
 * @author zkl
 */
public abstract class BaseActivity extends
        com.app.tanklib.activity.BaseActivity {

    public AppApplication application;
    public LoginUser loginUser;
    public IndexUrlCache urlMap;
    public EmptyLayout mEmptyLayout;
    public PersonVo personVo;

    LayoutInflater baseLayoutInflater;
    View loginDialog;
    Dialog loginBuilder;
    protected Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.application = (AppApplication) getApplication();
        this.loginUser = application.getLoginUser();
        this.personVo = application.person;

        IntentFilter filter = new IntentFilter();
        filter.addAction(getSSOLoginAction());
        this.registerReceiver(this.outReceiver, filter);
    }

    // 写一个广播的内部类，当收到动作时，结束activity
    private BroadcastReceiver outReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (getCloseAction().equals(intent.getAction())) {
                finish();
            } else if (getSSOLoginAction().equals(intent.getAction())) {
                loginDiglog(intent);
            }
        }
    };

    public abstract void findView();

    public int getActionBarBg() {
        return R.color.actionbar_bg;
    }

    public String getCloseAction() {
        return Constants.CLOSE_ACTION;
    }

    public String getSSOLoginAction() {
        return Constants.Logout_ACTION;
    }

    public void back() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != urlMap) {
            this.urlMap.cleanAll();
        }
        if (null != this.outReceiver) {
            this.unregisterReceiver(this.outReceiver);
            this.outReceiver = null;
        }
        if (mUnbinder != null)
            mUnbinder.unbind();
    }

    public int getDialogWidth() {
        return AppApplication.getWidthPixels() * 75 / 100;
    }

    public void showErrorView() {
        if (null != mEmptyLayout) {
            mEmptyLayout.showError();
        }
    }

    public void showEmptyView() {
        if (null != mEmptyLayout) {
            mEmptyLayout.showEmpty();
        }
    }

    public void showLoadingView() {
        if (null != mEmptyLayout) {
            mEmptyLayout.showLoading();
        }
    }

    public void hideView() {
        if (null != mEmptyLayout) {
            mEmptyLayout.showNoror();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        AntiHijackingUtil.checkActivity(this);
    }

    public void loginDiglog(Intent intent) {
        // 单点登录推送
        PushInfo pushInfo = (PushInfo) intent.getSerializableExtra("pushInfo");
        if (null != pushInfo) {
            if (null != loginBuilder) {
                loginBuilder.dismiss();
            }
            logout(pushInfo.description);
        }
    }

    void logout(String description) {
        if (null == baseLayoutInflater) {
            baseLayoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        loginBuilder = new Dialog(baseContext, R.style.alertDialogTheme);
        loginBuilder.setCancelable(false);
        loginBuilder.show();
        loginDialog = baseLayoutInflater.inflate(R.layout.logout_push_alert1,
                null);

        // 设置对话框的宽高
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                AppApplication.getWidthPixels() * 85 / 100,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        loginBuilder.setContentView(loginDialog, layoutParams);
        ((TextView) loginDialog.findViewById(R.id.message))
                .setText(description);
//		loginDialog.findViewById(R.id.delect).setOnClickListener(
//				new OnClickListener() {
//					@Override
//					public void onClick(View arg0) {
//						loginBuilder.dismiss();
//					}
//				});
        loginDialog.findViewById(R.id.delect).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        loginBuilder.dismiss();
                        application.clearInfo();
                        sendBroadcast(new Intent(Constants.CLOSE_ACTION));
                        Intent intent = new Intent(baseContext,
                                LoginActivity.class);
                        startActivity(intent);

                        MobclickAgent.onProfileSignOff();
                    }
                });
    }
}
