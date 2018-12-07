package com.bsoft.hospital.pub.suzhoumh.activity.my.record;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.AppApplication;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.app.tanklib.view.BsoftActionBar;
import com.app.tanklib.view.BsoftActionBar.Action;
import com.app.tanklib.photoview.PhotoViewAttacher;
import com.app.tanklib.photoview.PhotoViewAttacher.OnMatrixChangedListener;
import com.app.tanklib.photoview.PhotoViewAttacher.OnPhotoTapListener;
import com.app.tanklib.util.BitMapUtil;
import com.app.tanklib.util.StringUtil;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * @author Tank E-mail:zkljxq@126.com
 * 
 * @类说明
 */
public class PicInfoActivity extends BaseActivity {

	private ProgressDialog myDialog = null;
	public LayoutInflater mLayoutInflater;
	private ImageView mImageView;
	private PhotoViewAttacher mAttacher;
	Dialog builder;
	View viewDialog;
	Bitmap bmp;
	String url;
	int index = -1;
	int type = -1;
	BsoftActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
		mLayoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		setContentView(R.layout.pic_info);

		url = getIntent().getStringExtra("newUrl");
		index = getIntent().getIntExtra("index", -1);
		type = getIntent().getIntExtra("type", -1);
		if (StringUtil.isEmpty(url)) {
			Toast.makeText(this, "加载失败", Toast.LENGTH_SHORT).show();
			onBackPressed();
		} else {
			findView();
			initData();
		}
	}

	@Override
	public void findView() {
		myDialog = ProgressDialog.show(PicInfoActivity.this, "图片载入中", "请等待……");
		actionBar = (BsoftActionBar) findViewById(R.id.actionbar);
		actionBar.setTitle("图片详情");
		actionBar.setBackAction(new Action() {
			@Override
			public void performAction(View view) {
				finish();
			}

			@Override
			public int getDrawable() {
				return R.drawable.btn_back;
			}
		});
		if (index != -1 || type != -1) {
			actionBar.addAction(new Action() {
				@Override
				public void performAction(View view) {
					showDelView();
				}

				@Override
				public int getDrawable() {
					return R.drawable.btn_delete;
				}
			});
		}
		mImageView = (ImageView) findViewById(R.id.iv_photo);
	}
	

	void showDelView() {
		builder = new Dialog(PicInfoActivity.this, R.style.alertDialogTheme);
		builder.show();
		viewDialog = mLayoutInflater
				.inflate(R.layout.camera_delect_alert, null);

		// 设置对话框的宽高
		LayoutParams layoutParams = new LayoutParams(
				AppApplication.getWidthPixels() * 85 / 100,
				LayoutParams.WRAP_CONTENT);
		builder.setContentView(viewDialog, layoutParams);
		viewDialog.findViewById(R.id.delect).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						builder.dismiss();
						getIntent().putExtra("index", index);
						getIntent().putExtra("type", type);
						setResult(RESULT_OK, getIntent());
						finish();
					}
				});
		viewDialog.findViewById(R.id.cancel).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						builder.dismiss();
					}
				});
	}

	private void initData() {
		final String ErrMsg = "加载失败";
		final Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				myDialog.dismiss();
				if (msg.what == 1 && msg.obj != null) {
					Bitmap bitmap = (Bitmap) msg.obj;

					mImageView.setImageBitmap(bitmap);

					// The MAGIC happens here!
					mAttacher = new PhotoViewAttacher(mImageView);

					// Lets attach some listeners, not required though!
					mAttacher
							.setOnMatrixChangeListener(new MatrixChangeListener());
					mAttacher.setOnPhotoTapListener(new PhotoTapListener());

				} else {
					Toast.makeText(PicInfoActivity.this, ErrMsg,
							Toast.LENGTH_SHORT).show();
					finish();
				}
			}
		};
		new Thread() {
			public void run() {
				Message msg = new Message();
				try {
					if (url.startsWith("http://")) {
						// bmp = CacheManage.getInstance().getBitmap(
						// new ImageRequest(null, newUrl, null, null,
						// CacheManage.IMAGE_TYPE_BIG));
						bmp = getURLimage(url);
					} else {
						bmp = BitmapFactory.decodeFile(url);
					}
					msg.what = 1;
					msg.obj = bmp;
				} catch (Exception e) {
					e.printStackTrace();
					msg.what = -1;
					msg.obj = e;
				}
				handler.sendMessage(msg);
			}
		}.start();
	}

	// 加载图片
	public Bitmap getURLimage(String url) {
		Bitmap bmp = null;
		try {
			URL myurl = new URL(url);
			// 获得连接
			HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
			conn.setConnectTimeout(6000);// 设置超时
			conn.setDoInput(true);
			conn.setUseCaches(false);// 不缓存
			conn.connect();
			InputStream is = conn.getInputStream();// 获得图片的数据流
			bmp = BitmapFactory.decodeStream(is);
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bmp;
	}

	private class PhotoTapListener implements OnPhotoTapListener {

		@Override
		public void onPhotoTap(View view, float x, float y) {

		}
	}

	private class MatrixChangeListener implements OnMatrixChangedListener {

		@Override
		public void onMatrixChanged(RectF rect) {

		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mAttacher.cleanup();
		BitMapUtil.recycle(bmp);
	}


}
