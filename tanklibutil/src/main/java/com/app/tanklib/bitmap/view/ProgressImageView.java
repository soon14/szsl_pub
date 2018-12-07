package com.app.tanklib.bitmap.view;



import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.app.tanklib.R;
import com.app.tanklib.bitmap.CacheManage;
import com.app.tanklib.util.MD5;

public class ProgressImageView extends LinearLayout implements
		NetImageView.Listener {

	public NetImageView mIgImageView;
	public ProgressBar mProgressBar;
	private Context paramContext;
	public String imageUrl;

	public ProgressImageView(Context paramContext) {
		super(paramContext);
		this.paramContext = paramContext;
		init();
	}

	public ProgressImageView(Context paramContext,
			AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
		this.paramContext = paramContext;
		init();
	}

	public void init() {

	}

	@Override
	public void onImageLoadStarted() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(MD5.getMD5(imageUrl));
	/*	BaseLBSApplication.registerProcessReceiver(mReceiver, filter);*/
		mProgressBar.setVisibility(View.VISIBLE);
	}

	@Override
	public void onImageLoadCancelled() {

	}

	@Override
	public void onImageLoadComplete() {
		mProgressBar.setVisibility(View.GONE);
		mIgImageView.setVisibility(View.VISIBLE);
		/*BaseLBSApplication.unregisterProcessReceiver(mReceiver);*/
	}

	@Override
	public void onImageLoadError() {
	}

	@Override
	public void onImageLoading(int result) {
		mProgressBar.setProgress(result);
	}

//	public void setImageUrl(String url) {
//		this.imageUrl = url;
//		mIgImageView.setListener(this);
//		mIgImageView.setImageUrl(url, null, R.color.red,
//				R.color.image_backgroud, CacheManage.IMAGE_TYPE_BIG);
//	}

	public void setImageUrl(String url) {
		this.imageUrl = url;
		mIgImageView.setListener(this);
		mIgImageView.setImageUrl(url, CacheManage.IMAGE_TYPE_PROGRESS);
	}

	public void setImageUrl(String url, BitmapFactory.Options options) {
		this.imageUrl = url;
		mIgImageView.setListener(this);
		mIgImageView.setImageUrl(url, options, CacheManage.IMAGE_TYPE_PROGRESS);
	}

	public void setLatout(LayoutParams params) {
		removeAllViews();
		LayoutInflater la = (LayoutInflater) paramContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = la.inflate(R.layout.progressimage, null);
		mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
		mIgImageView = (NetImageView) view.findViewById(R.id.imgPic);
		mIgImageView.setScaleType(ScaleType.CENTER_CROP);
		// mIgImageView.setMaxWidth(ShowApplication.getWidthPixels());
		// mIgImageView.setAdjustViewBounds(true);
		// mIgImageView.setScaleType(ScaleType.FIT_XY);
		ViewGroup.LayoutParams lp = mIgImageView.getLayoutParams();
		lp.width = params.width;
		lp.height = params.height;
		mIgImageView.setLayoutParams(lp);
		addView(view, params);
	}

	BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(MD5.getMD5(imageUrl))) {
				onImageLoading(intent.getIntExtra("result", 0));
			}
		}
	};

	public NetImageView getImageView() {
		return mIgImageView;
	}

}
