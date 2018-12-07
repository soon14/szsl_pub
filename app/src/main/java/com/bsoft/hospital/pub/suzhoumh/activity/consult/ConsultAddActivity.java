package com.bsoft.hospital.pub.suzhoumh.activity.consult;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.util.AsyncTaskUtil;
import com.app.tanklib.view.BsoftActionBar.Action;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.AppApplication;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.consult.DoctorInfo;
import com.bsoft.hospital.pub.suzhoumh.util.BitmapUtility;
import com.bsoft.hospital.pub.suzhoumh.util.SystemUtils;
import com.bsoft.hospital.pub.suzhoumh.util.Utility;

public class ConsultAddActivity extends BaseActivity implements OnClickListener{

	private ImageView iv_head;
	private TextView tv_name;
	private TextView tv_dept;
	private EditText et_title;
	private EditText et_content;
	private Button btn_send;
	private Button btn_add;
	
	private LinearLayout ll_images;
	private Bitmap bitmap;
	
	private DoctorInfo doctorinfo;
	
	private Dialog builder;
	private View viewDialog;
	private LayoutInflater mInflater;
	private String storeConsultImage;
	
	private ArrayList<String> sysfiles = new ArrayList<String>();
	private UploadDataTask task;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.consult_add);
		findView();
		initView();
		initData();
	}
	
	private void initView()
	{
		iv_head = (ImageView) findViewById(R.id.iv_head);
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_dept = (TextView) findViewById(R.id.tv_dept);
		et_title = (EditText) findViewById(R.id.et_title);
		et_content = (EditText) findViewById(R.id.et_content);
		btn_send = (Button) findViewById(R.id.btn_send);
		btn_add = (Button) findViewById(R.id.btn_add);
		ll_images = (LinearLayout) findViewById(R.id.ll_images);
	}
	
	private void initData()
	{
		btn_send.setOnClickListener(this);
		btn_add.setOnClickListener(this);
		mInflater = LayoutInflater.from(baseContext);
		doctorinfo = (DoctorInfo) getIntent().getSerializableExtra("doctorinfo");
		tv_name.setText(doctorinfo.name);
		tv_dept.setText(doctorinfo.deptname);
		if(doctorinfo.sexcode!=null)
		{
			if(doctorinfo.sexcode.equals("1"))
			{
				iv_head.setBackgroundResource(R.drawable.doc_male);
			}
			else
			{
				iv_head.setBackgroundResource(R.drawable.doc_female);
			}
		}
	}

	@Override
	public void findView() {
		// TODO Auto-generated method stub
		findActionBar();
		actionBar.setTitle("我要咨询");
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
		actionBar.setBackGround(Color.TRANSPARENT);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
			case R.id.btn_send:
				if(et_title.getText().toString().equals(""))
				{
					Toast.makeText(baseContext, "请输入咨询主题", Toast.LENGTH_SHORT).show();
				}
				else
				{
					if(et_title.getText().toString().equals(""))
					{
						Toast.makeText(baseContext, "请输入咨询内容", Toast.LENGTH_SHORT).show();
					}
					else
					{
						task = new UploadDataTask();
						task.execute();
					}
				}
				break;
			case R.id.btn_add:
				if(sysfiles!=null&&sysfiles.size()<=3)
				{
					showCamera();
				}
				else
				{
					Toast.makeText(baseContext, "最多输入四张", Toast.LENGTH_SHORT).show();
				}
				break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 相机拍照返回
		if (requestCode == 40110) {
				if (resultCode == RESULT_OK) {
					setIamgeView(storeConsultImage);
				}
			}
		//手机相册
		else if (requestCode == 40120)
			{
				if(resultCode == RESULT_OK)
				{
	                storeConsultImage = getConsultImageUrl();
	                setIamgeView(getImagePath(data));
				}
			}
	}

	/**
	 * 手机相册获取图片路径
	 * @param data
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.KITKAT)
	private String getImagePath(Intent data)
	{
		String filePath = "";
		ContentResolver resolver = getContentResolver();  
        //照片的原始资源地址  
        Uri originalUri = data.getData();
        //使用ContentProvider通过URI获取原始图片  
        //Bitmap photo = MediaStore.Images.Media.getBitmap(resolver, originalUri);
        try {
        	System.out.println("uri:"+originalUri.toString());
        	if(SystemUtils.getSystemVersion()==SystemUtils.V4_4)
        	{
        		if(DocumentsContract.isDocumentUri(baseContext, originalUri)){//处理anroid4.4
            	    String wholeID = DocumentsContract.getDocumentId(originalUri);
            	    String id = wholeID.split(":")[1];
            	    String[] column = { MediaStore.Images.Media.DATA };
            	    String sel = MediaStore.Images.Media._ID + "=?";
            	    Cursor cursor = baseContext.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column,
            	            sel, new String[] { id }, null);
            	    int columnIndex = cursor.getColumnIndex(column[0]);
            	    if (cursor.moveToFirst()) {
            	        filePath = cursor.getString(columnIndex);
            	    }
            	    cursor.close();
            	}else{
            	    String[] projection = { MediaStore.Images.Media.DATA };
            	    Cursor cursor = baseContext.getContentResolver().query(originalUri, projection, null, null, null);
            	    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            	    cursor.moveToFirst();
            	    filePath = cursor.getString(column_index);
            	}
        	}
        	else
        	{
        		String[] projection = { MediaStore.Images.Media.DATA };
         	    Cursor cursor = baseContext.getContentResolver().query(originalUri, projection, null, null, null);
         	    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
         	    cursor.moveToFirst();
         	    filePath = cursor.getString(column_index);
        	}
        	}
        	catch (Exception e) {  
                e.printStackTrace();  
            }
		return filePath;
	}
	
	/**
	 * 设置图片
	 * @param filePath
	 */
	private void setIamgeView(final String filePath)
	{
		bitmap = BitmapUtility.getSmallBitmap(filePath,480,800);
		BitmapUtility.saveFile(bitmap, storeConsultImage);
		sysfiles.add(storeConsultImage);
		BitmapDrawable drawable = new BitmapDrawable(bitmap);
		final View view = LayoutInflater.from(baseContext).inflate(R.layout.consult_image, null);
		ImageView iv = (ImageView)view.findViewById(R.id.iv);
		iv.setBackgroundDrawable(drawable);
		ImageView iv_del = (ImageView)view.findViewById(R.id.iv_del);
		iv_del.setVisibility(View.VISIBLE);
		iv_del.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ll_images.removeView(view);
				delSingleImage(filePath);
			}
		});
		ll_images.addView(view);
		ll_images.setVisibility(View.VISIBLE);
		iv.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ConsultAddActivity.this,ImageDetailActivity.class);
				intent.putExtra("image_path", filePath);
				startActivity(intent);
			}
			
		});
	}
	
	/**
	 * 删除当前图片
	 * @param filePath
	 */
	private void delSingleImage(String filePath)
	{
		for(int i=0;i<sysfiles.size();i++)
		{
			if(sysfiles.get(i).equals(filePath))
			{
				sysfiles.remove(i);
				break;
			}
		}
	}
	
	/**
	 * 删除所有图片
	 */
	private void delAllImage()
	{
		if(sysfiles!=null&&sysfiles.size()>0)
		{
			for(int i=0;i<sysfiles.size();i++)
			{
				File file = new File(sysfiles.get(i));
				// 检测图片是否存在
				if (file.exists()) {
					file.delete(); // 删除
				}
			}
		}
	}
	
	/**
	 * 显示对话框
	 */
	public void showCamera() {
		builder = new Dialog(ConsultAddActivity.this, R.style.alertDialogTheme);
		builder.show();
		viewDialog = mInflater.inflate(R.layout.camera_chose_alert, null);

		// 设置对话框的宽高
		LayoutParams layoutParams = new LayoutParams(
				AppApplication.getWidthPixels() * 85 / 100,
				LayoutParams.WRAP_CONTENT);
		builder.setContentView(viewDialog, layoutParams);
		viewDialog.findViewById(R.id.photo).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						if (Utility.checkSDCard()) {
							builder.dismiss();
							storeConsultImage = getConsultImageUrl();
							Intent getImageByCamera = new Intent(
									"android.media.action.IMAGE_CAPTURE");
							Uri localUri = Uri.fromFile(new File(storeConsultImage));
							getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT,
									localUri);
							/*getImageByCamera.putExtra(
									"android.intent.extra.videoQuality", 0);*/
							startActivityForResult(getImageByCamera, 40110);
						} else {
							Toast.makeText(ConsultAddActivity.this, "SD卡不可用!",
									Toast.LENGTH_SHORT).show();
						}
					}
				});
		viewDialog.findViewById(R.id.sd).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						if (Utility.checkSDCard()) {
							storeConsultImage = getConsultImageUrl();
							Intent intent = new Intent(
									Intent.ACTION_GET_CONTENT);
							intent.setType("image/*");
							startActivityForResult(intent,40120);
							builder.dismiss();
						} else {
							Toast.makeText(ConsultAddActivity.this, "SD卡不可用!",
									Toast.LENGTH_SHORT).show();
						}
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
	
	/**
	 * 初始化图片的名称
	 * @return
	 */
	public String getConsultImageUrl() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();
		String time = sdf.format(date);
		//int img_index = ll_images_local.getChildCount()+1;
		return new StringBuffer(application.getStoreDir()).append(time)
				.append(".jpg").toString();
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (null != bitmap) {
			bitmap.recycle();
			bitmap = null;
		}
		AsyncTaskUtil.cancelTask(task);
		delAllImage();
	}
	
	class UploadDataTask extends AsyncTask<Void, Void, String>
	{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			actionBar.startTextRefresh();
		}

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String strs[] = new String[sysfiles.size()];
			for(int i=0;i<sysfiles.size();i++)
			{
				strs[i] = sysfiles.get(i);
			}
			return HttpApi.getInstance().postConsultPic(
					"upload/savequestion", 
					strs,
					new BsoftNameValuePair("title",et_title.getText().toString()),
					new BsoftNameValuePair("content",et_content.getText().toString()),
					new BsoftNameValuePair("username",loginUser.realname),
					new BsoftNameValuePair("deptid",doctorinfo.deptid),
					new BsoftNameValuePair("docid",doctorinfo.uid),
					new BsoftNameValuePair("docname",doctorinfo.name),
					new BsoftNameValuePair("uid", loginUser.id)//传用户uid
					);
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			actionBar.endTextRefresh();
			if (null != result) {
				try
				{
					JSONObject ob = new JSONObject(result);
					if(!ob.isNull("msg"))
					{
						Toast.makeText(baseContext, ob.getString("msg"), Toast.LENGTH_SHORT).show();
						Intent intent = new Intent();
						intent.setAction(Constants.CONSULT_ACTION);
						sendBroadcast(intent);
						finish();
					}
				}catch(Exception e)
				{
					e.printStackTrace();
				}
//				System.out.println("result->"+result);
			} else {
				Toast.makeText(baseContext, "操作失败", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}
	
	
}
