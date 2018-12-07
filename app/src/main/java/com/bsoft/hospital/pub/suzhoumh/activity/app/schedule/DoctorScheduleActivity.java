package com.bsoft.hospital.pub.suzhoumh.activity.app.schedule;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.app.tanklib.view.BsoftActionBar.Action;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;


public class DoctorScheduleActivity extends BaseActivity{

	private WebView webViewSchedule;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acitivity_doctorschedule);
		findView();
		initData();
	}

	@Override
	public void findView() {
		// TODO Auto-generated method stub
		findActionBar();
		actionBar.setTitle("专家排班");
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
		webViewSchedule = (WebView)findViewById(R.id.webview_schedule);
	}
	
	private void initData()
	{
//		webViewSchedule.loadUrl(Constants.HttpUrl+"/hiss/order");
		webViewSchedule.loadUrl(Constants.getHttpUrl()+"/hiss/order");
		//获取WebView的设置对象
		WebSettings webSettings=webViewSchedule.getSettings();     
		//设置开启js调用  
		webSettings.setJavaScriptEnabled(true);  
		webViewSchedule.setWebChromeClient(new WebChromeClient() {
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
