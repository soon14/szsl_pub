package com.bsoft.hospital.pub.suzhoumh.activity;

import com.app.tanklib.util.StringUtil;
import com.app.tanklib.view.BsoftActionBar.Action;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

/**
 * @author Tank E-mail:zkljxq@126.com
 * 
 * @类说明
 */
public class WebActivity extends BaseActivity {
	WebView web;
	ProgressBar emptyProgress;
	String weib_url;
	String title;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);
		weib_url = getIntent().getStringExtra("newUrl");
		title = getIntent().getStringExtra("title");
		findView();
		if (!StringUtil.isEmpty(weib_url)) {
			web.loadUrl(weib_url);
		}
	}

	public void findView() {
		findActionBar();
		actionBar.setTitle(title);
		actionBar.setBackAction(new Action() {

			@Override
			public int getDrawable() {
				return R.drawable.btn_back;
			}

			@Override
			public void performAction(View view) {
				finish();
			}
		});
		emptyProgress = (ProgressBar) findViewById(R.id.emptyProgress);
		web = (WebView) findViewById(R.id.webview);
		web.getSettings().setJavaScriptEnabled(true);
		// web.addJavascriptInterface(new InJavaScript(), "health");
		web.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		web.requestFocus();
		web.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if (newProgress == 100) {
					emptyProgress.setVisibility(View.GONE);
				}
			}
		});

		web.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) { // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
				view.loadUrl(url);
				return true;
			}

			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
			}

			public void onPageFinished(WebView view, String url) {
				view.loadUrl("javascript:window.local_obj.showSource('<head>'+"
						+ "document.getElementsByTagName('html')[0].innerHTML+'</head>');");
				super.onPageFinished(view, url);
			}
		});
	}

	 public boolean onKeyDown(int keyCode, KeyEvent event) {
	 if ((keyCode == KeyEvent.KEYCODE_BACK) && web.canGoBack()) {
	  web.goBack();
	 return true;
	 }
	 return super.onKeyDown(keyCode, event);
	 }

	@Override
	public void onDestroy() {
		super.onDestroy();
		CookieSyncManager.createInstance(this);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeAllCookie();
	}

}
