package com.bsoft.hospital.pub.suzhoumh.activity.my;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.AppApplication;
import com.bsoft.hospital.pub.suzhoumh.util.IOnTabActivityResultListener;
import com.app.tanklib.view.BsoftActionBar;
import com.app.tanklib.view.BsoftActionBar.Action;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * @author Tank E-mail:zkljxq@126.com
 * 
 * @类说明
 */
public class SettingHeaderActivity extends Activity implements
		IOnTabActivityResultListener {

	BsoftActionBar actionbar;
	AppApplication application;
	private Dialog dialog_photo;
	final CharSequence[] arr_photo = { "拍照", "相册", "取消" };

	String storeHeader;
	Bitmap bitmap;
	ImageView headerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settingheader);
		application = (AppApplication) getApplication();
		storeHeader = getSDHeaderImageUrl();
		headerView = (ImageView) findViewById(R.id.header);
		actionbar = (BsoftActionBar) findViewById(R.id.actionbar);
		actionbar.setBackAction(new Action() {
			@Override
			public void performAction(View view) {
				setResult(RESULT_OK);
				finish();
			}

			@Override
			public int getDrawable() {
				return R.drawable.btn_back;
			}
		});
		showHeaderDialog();
	}

	public void showHeaderDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				SettingHeaderActivity.this);
		builder.setTitle("设置头像照片");
		builder.setItems(arr_photo, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				if (checkSDCard()) {
					if (item == 0) {
						Intent getImageByCamera = new Intent(
								"android.media.action.IMAGE_CAPTURE");
						Uri localUri = Uri.fromFile(new File(storeHeader));
						getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT,
								localUri);
						getImageByCamera.putExtra(
								"android.intent.extra.videoQuality", 0);
						startActivityForResult(getImageByCamera, 120);
					} else if (item == 1) {
						Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
						intent.setType("image/*");
						intent.putExtra("crop", "true");// crop=true
						// 有这句才能出来最后的裁剪页面.
						intent.putExtra("aspectX", 1);// 这两项为裁剪框的比例.
						intent.putExtra("aspectY", 1);// x:y=1:2
						intent.putExtra("outputX", 200);
						intent.putExtra("outputY", 200);
						intent.putExtra("output",
								Uri.fromFile(new File(storeHeader)));
						intent.putExtra("outputFormat", "JPEG");// 返回格式
						startActivityForResult(
								Intent.createChooser(intent, "选择图片"), 110);
					}
				} else {
					Toast.makeText(SettingHeaderActivity.this, "SD卡不可用！",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		dialog_photo = builder.create();
		dialog_photo.show();
	}

	public String getSDHeaderImageUrl() {
		return new StringBuffer(application.getStoreDir()).append("header")
				.append(".jpg").toString();
	}

	/**
	 * 检查SD卡是否可用
	 * */
	private boolean checkSDCard() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	// @Override
	// protected void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// // 相册
	// if (requestCode == 110) {
	// if (resultCode == RESULT_OK) {
	// try {
	// bitmap = BitmapFactory.decodeStream(new FileInputStream(
	// new File(storeHeader)));
	// headerView.setImageBitmap(bitmap);
	// } catch (FileNotFoundException e) {
	// e.printStackTrace();
	// }
	// }
	// }
	// // 相机
	// else if (requestCode == 120) {
	// if (resultCode == RESULT_OK) {
	// Intent intent = new Intent("com.android.camera.action.CROP");
	// intent.setDataAndType(Uri.fromFile(new File(storeHeader)),
	// "image/*");
	// intent.putExtra("crop", "true");
	// intent.putExtra("aspectX", 1);
	// intent.putExtra("aspectY", 1);
	// intent.putExtra("outputX", 180);
	// intent.putExtra("outputY", 180);
	// intent.putExtra("output", Uri.fromFile(new File(storeHeader)));
	// intent.putExtra("outputFormat", "JPEG");// 返回格式
	// startActivityForResult(intent, 110);
	// }
	// }
	// }

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (null != bitmap) {
			bitmap.recycle();
			bitmap = null;
		}
	}

	@Override
	public void onTabActivityResult(int requestCode, int resultCode, Intent data) {
		// 相册
		if (requestCode == 110) {
			if (resultCode == RESULT_OK) {
				try {
					bitmap = BitmapFactory.decodeStream(new FileInputStream(
							new File(storeHeader)));
					headerView.setImageBitmap(bitmap);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		// 相机
		else if (requestCode == 120) {
			if (resultCode == RESULT_OK) {
				Intent intent = new Intent("com.android.camera.action.CROP");
				intent.setDataAndType(Uri.fromFile(new File(storeHeader)),
						"image/*");
				intent.putExtra("crop", "true");
				intent.putExtra("aspectX", 1);
				intent.putExtra("aspectY", 1);
				intent.putExtra("outputX", 180);
				intent.putExtra("outputY", 180);
				intent.putExtra("output", Uri.fromFile(new File(storeHeader)));
				intent.putExtra("outputFormat", "JPEG");// 返回格式
				startActivityForResult(intent, 110);
			}
		}
	}

	public void findView() {
		// TODO Auto-generated method stub

	}

}
