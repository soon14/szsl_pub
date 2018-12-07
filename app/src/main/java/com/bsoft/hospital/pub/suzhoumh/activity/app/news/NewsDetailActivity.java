package com.bsoft.hospital.pub.suzhoumh.activity.app.news;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.app.tanklib.view.BsoftActionBar.Action;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;

/**
 * 新闻详情界面
 * @author Administrator
 *
 */
public class NewsDetailActivity extends BaseActivity{

	private WebView webView;
	private NewsItem newsitem;
	//http://test103132.zgjkw.cn/pub/dynamic?id=10 动态详情的地址

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_detail);
		findView();
	}

	@Override
	public void findView() {
		// TODO Auto-generated method stub
		findActionBar();
		actionBar.setTitle("详情");
		actionBar.setBackAction(new Action(){

			@Override
			public int getDrawable() {
				// TODO Auto-generated method stub
				return R.drawable.btn_back;
			}

			@Override
			public void performAction(View view) {
				// TODO Auto-generated method stub
				finish();
			}
			
		});
		webView = (WebView)findViewById(R.id.webview);
		newsitem = (NewsItem)getIntent().getSerializableExtra("newsitem");
		webView.loadUrl(Constants.getHttpUrl()+"pub/dynamic?id="+newsitem.drid);
		
		webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                if (newProgress == 100) {
                    // 网页加载完成
                	actionBar.endTextRefresh();
                } else {
                    // 加载中
                	actionBar.startTextRefresh();
                }

            }
        });
	}

}
