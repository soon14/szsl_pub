package com.bsoft.hospital.pub.suzhoumh.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Toast;

import com.app.tanklib.Preferences;
import com.app.tanklib.util.StringUtil;
import com.bsoft.hospital.pub.suzhoumh.AppApplication;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.account.LoginActivity;
import com.bsoft.hospital.pub.suzhoumh.util.AntiHijackingUtil;
import com.bsoft.hospital.pub.suzhoumh.util.SignUtil;
import com.bsoft.hospital.pub.suzhoumh.util.SystemUiVisibilityUtil;
import com.bsoft.hospital.pub.suzhoumh.util.listener.AnimationListenerImp;
import com.bsoft.hospital.pub.suzhoumh.util.listener.OnAnimationListener;

/**
 * @author Tank
 * @类说明
 */
public class LoadingActivity extends AppCompatActivity implements OnAnimationListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.loading);
        // 渐变展示启动屏
        AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
        aa.setDuration(1800);
        findViewById(R.id.loadingImg).startAnimation(aa);
        aa.setAnimationListener(new AnimationListenerImp(this));
    }

    private boolean validateSign() {
        try {
            return SignUtil.LOCAL_SIGN_HASCODE == SignUtil.getSign(LoadingActivity.this);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    /**
     * 跳转到...
     */
    private void redirectTo() {
        Intent intent;
        if (((AppApplication) getApplication()).getLoginUser() != null) {
            if (!StringUtil.isEmpty(Preferences.getInstance().getStringData(
                    "first"))
                    && "1".equals(Preferences.getInstance().getStringData(
                    "first"))) {
                intent = new Intent(this, MainTabActivity.class);
            } else {
                intent = new Intent(this, BootPageActivity.class);
            }
        } else {
            if (!StringUtil.isEmpty(Preferences.getInstance().getStringData(
                    "first"))
                    && "1".equals(Preferences.getInstance().getStringData(
                    "first"))) {
                intent = new Intent(this, LoginActivity.class);
            } else {
                intent = new Intent(this, BootPageActivity.class);
            }
        }
        startActivity(intent);
        SystemUiVisibilityUtil.hideStatusBar(getWindow(), false);
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        AntiHijackingUtil.checkActivity(this);
    }

    @Override
    public void onAnimationEndListener() {
        if (validateSign()) {
            redirectTo();
        } else {
            Toast.makeText(LoadingActivity.this, "程序可能被篡改,请重新下载", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
