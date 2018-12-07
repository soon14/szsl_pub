package com.bsoft.hospital.pub.suzhoumh.activity.app.insurance;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.app.tanklib.view.BsoftActionBar;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.util.Html5WebView;

/**
 * @author :lizhengcao
 * @date :2018/3/21
 * E-mail:lizc@bsoft.com.cn
 * @类说明 商业保险
 */

public class InsuranceActivity extends BaseActivity {

    private Html5WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance);
        findView();
        initView();

    }

    private void initView() {
        mWebView = (Html5WebView) findViewById(R.id.webview);
        String birthday = loginUser.idcard.substring(6, 14);
        mWebView.loadUrl("http://publich5.taivexmhealth.com/api/visit/taivex?" +
                "username=" + loginUser.realname +
                "&cardCode=" + "01" +
                "&cardNo=" + loginUser.idcard +
                "&tenantId=" + "suzhould" + "" +
                "&phone=" + loginUser.mobile +
                "&sex=" + loginUser.sexcode +
                "&country=" + "中国" +
                "&birthday=" + birthday);
//        mWebView.setWebViewClient(mWebClientListener);
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

//    /**
//     * web网页加载时的监听
//     */
//    public WebViewClient mWebClientListener = new WebViewClient() {
//        @Override
//        public void onPageStarted(WebView view, String url, Bitmap favicon) {
//            actionBar.startTextRefresh();
//        }
//
//        @Override
//        public void onPageFinished(WebView view, String url) {
//            actionBar.endTextRefresh();
//        }
//    };

    @Override
    public void findView() {
        findActionBar();
//        actionBar.setTitle("商业保险");
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
    }
}
