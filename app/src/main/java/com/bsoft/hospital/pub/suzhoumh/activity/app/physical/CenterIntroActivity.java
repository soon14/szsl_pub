package com.bsoft.hospital.pub.suzhoumh.activity.app.physical;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.KeyEventCompat;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.app.tanklib.view.BsoftActionBar;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.util.Html5WebView;

import java.net.URLEncoder;

public class CenterIntroActivity extends BaseActivity {

    private Html5WebView mWebView;
    private int type;
    //体检中心
    public static final int PHYSICAL_TYPE = 1;
    //特检中心
    public static final int VIP_TYPE = 2;
    //肿瘤筛查
    public static final int TUMOR_TYPE = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physical_center);

        findView();
    }

    @Override
    public void findView() {
        findActionBar();
        type = getIntent().getIntExtra("type", -1);

        actionBar.setBackAction(new BsoftActionBar.Action() {
            @Override
            public void performAction(View view) {
                back();
            }

            @Override
            public int getDrawable() {
                return R.drawable.btn_back;
            }
        });


        mWebView = (Html5WebView) findViewById(R.id.webview);
        switch (type) {
            case PHYSICAL_TYPE:
                setMECData("hiss/physical");
                break;
            case VIP_TYPE:
                setMECData("hiss/vip");
                break;
            case TUMOR_TYPE:
                mWebView.loadUrl("http://218.4.142.107:9000/ceaafp/h5/index.html?name=" + loginUser.realname + "&sex=" + loginUser.sexcode + "&idcard=" + loginUser.idcard + "&phone=" + loginUser.mobile);
                break;
        }
        mWebView.setWebsiteChangeListener(new Html5WebView.WebsiteChangeListener() {
            @Override
            public void onWebsiteChange(String title) {
                actionBar.setTitle(title);
            }

            @Override
            public void onUrlChange(String url) {

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * @param data web网页的部分网址
     */
    public void setMECData(String data) {
        mWebView.loadUrl(Constants.getHttpUrl() + data);
    }
}
