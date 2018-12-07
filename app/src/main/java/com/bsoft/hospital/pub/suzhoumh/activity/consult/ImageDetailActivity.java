package com.bsoft.hospital.pub.suzhoumh.activity.consult;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.app.tanklib.view.BsoftActionBar.Action;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.util.BitmapUtility;

/**
 * 图片详情
 * @author Administrator
 *
 */
public class ImageDetailActivity extends BaseActivity{

	private ImageView iv;
	
	private String image_path;
	
	private Bitmap bitmap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_detail);
		findView();
		initView();
		initData();
	}

	@Override
	public void findView() {
		// TODO Auto-generated method stub
		findActionBar();
		actionBar.setTitle("图片详情");
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
	}
	
	private void initView()
	{
		iv = (ImageView) findViewById(R.id.iv);
	}
	
	private void initData()
	{
		image_path = getIntent().getStringExtra("image_path");
		
		bitmap = BitmapUtility.getSmallBitmap(image_path,480,800);
		BitmapDrawable drawable = new BitmapDrawable(bitmap);
		iv.setBackgroundDrawable(drawable);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (null != bitmap) {
			bitmap.recycle();
			bitmap = null;
		}
	}
	
	

}
