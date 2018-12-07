package com.bsoft.hospital.pub.suzhoumh.activity.dynamic;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.tanklib.bitmap.CacheManage;
import com.app.tanklib.bitmap.IndexUrlCache;
import com.app.tanklib.bitmap.view.ProgressImageView;
import com.app.tanklib.bitmap.view.RoundImageView;
import com.app.tanklib.util.StringUtil;
import com.app.tanklib.view.BsoftActionBar.Action;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.AppApplication;
import com.bsoft.hospital.pub.suzhoumh.activity.adapter.CommentAdapter;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.DynamicShow;
import com.bsoft.hospital.pub.suzhoumh.util.DateUtil;

public class NoticeDetailActivity extends BaseActivity{

	ProgressBar emptyProgress;
	CommentAdapter adapter;
	View header;

	ProgressImageView progressImageView;
	RoundImageView headerImage;

	int pageNo = 1;
	int pageSize = 50;

	DynamicShow vo;

	@Override
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dynamic_detail);
		vo = (DynamicShow) getIntent().getSerializableExtra("vo");
		urlMap = new IndexUrlCache();
		findView();
		initData();
	}



	@Override
	public void findView() {
		findActionBar();
		actionBar.setTitle("详情");
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
	}

	private void initData() {
		if (null != vo) {
			((TextView)findViewById(R.id.time)).setText(DateUtil
					.getDateTime("yyyy-MM-dd HH:mm:ss",vo.createdate));
			((TextView)findViewById(R.id.content)).setText(Html
					.fromHtml(vo.content));
			((TextView)findViewById(R.id.title)).setText(vo.title);
			if (!StringUtil.isEmpty(vo.imgurl)) {
				progressImageView = (ProgressImageView)findViewById(R.id.progressImageView);
				progressImageView.setVisibility(View.VISIBLE);

				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				params.width = AppApplication.getWidthPixels();
				params.height = AppApplication.getWidthPixels() * 2 / 3;
				if (null != progressImageView.getImageView()) {
					progressImageView.getImageView().getDrawable()
							.setCallback(null);
				}
				progressImageView.setLatout(params);
				progressImageView.mIgImageView.resertStates();
				progressImageView.setImageUrl(HttpApi.getImageUrl(vo.imgurl,
						CacheManage.IMAGE_TYPE_PROGRESS));

				urlMap.add(-1, HttpApi.getImageUrl(vo.imgurl,
						CacheManage.IMAGE_TYPE_PROGRESS));
			}

		}
	}


	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
