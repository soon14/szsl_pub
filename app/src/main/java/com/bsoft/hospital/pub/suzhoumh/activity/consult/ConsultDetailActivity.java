package com.bsoft.hospital.pub.suzhoumh.activity.consult;

import java.io.File;
import java.io.FileInputStream;
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
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tanklib.bitmap.view.RoundImageView;
import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.util.AsyncTaskUtil;
import com.app.tanklib.view.BsoftActionBar.Action;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.AppApplication;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.NullModel;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.model.consult.ConsultInfo;
import com.bsoft.hospital.pub.suzhoumh.model.consult.DoctorInfo;
import com.bsoft.hospital.pub.suzhoumh.model.consult.ConsultInfo.InteractServants;
import com.bsoft.hospital.pub.suzhoumh.model.consult.ConsultInfo.SysFiles;
import com.bsoft.hospital.pub.suzhoumh.util.BitmapUtility;
import com.bsoft.hospital.pub.suzhoumh.util.DateUtil;
import com.bsoft.hospital.pub.suzhoumh.util.SystemUtils;
import com.bsoft.hospital.pub.suzhoumh.util.Utility;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * 咨询内容详情
 * @author Administrator
 *
 */
public class ConsultDetailActivity extends BaseActivity implements OnClickListener{

	private TextView tv_name;
	private TextView tv_dept;
	private TextView tv_title;
	private TextView tv_content;
	private TextView tv_time;
	private ImageView iv_head;
	private RoundImageView iv_my_head;
	private LinearLayout ll_content;
	private EditText et_content;
	private ImageView iv_add;
	private Button btn_send;
	
	private LinearLayout ll_images_server;
	private LinearLayout ll_images_local;
	private ScrollView m_scrollview;
	
	private String id;
	private ConsultInfo consultInfo;
	
	public ArrayList<InteractServants> reply_list = new ArrayList<InteractServants>();//回复问题集合
	private ArrayList<String> sysfiles = new ArrayList<String>();//要发送的照片集合
	
	private Dialog builder;
	private View viewDialog;
	
	private String storeConsultImage;
	
	private Bitmap bitmap;
	
	private UploadDataTask uploaddatatask;
	private GetDataTask getdatatask;
	private ChangeStateTask changestatetask;
	
	public ImageLoader imageLoader = ImageLoader.getInstance();
	
	private Handler handler = new Handler();
	
	private String sexcode = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.consult_detail);
		findView();
		initView();
		initData();
	}

	private void initView()
	{
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_dept = (TextView) findViewById(R.id.tv_dept);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_content = (TextView) findViewById(R.id.tv_content);
		tv_time = (TextView) findViewById(R.id.tv_time);
		et_content = (EditText) findViewById(R.id.et_content);
		ll_content = (LinearLayout) findViewById(R.id.ll_content);
		m_scrollview = (ScrollView) findViewById(R.id.m_scrollview);
		iv_head = (ImageView) findViewById(R.id.iv_head);
		iv_my_head = (RoundImageView) findViewById(R.id.iv_my_head);
		iv_add = (ImageView) findViewById(R.id.iv_add);
		btn_send = (Button) findViewById(R.id.btn_send);
		
		ll_images_server = (LinearLayout) findViewById(R.id.ll_images_server);
		ll_images_local = (LinearLayout) findViewById(R.id.ll_images_local);
	}
	
	private void initData()
	{
		imageLoader.init(ImageLoaderConfiguration.createDefault(ConsultDetailActivity.this));
		id = getIntent().getStringExtra("id");
		tv_name.setText(getIntent().getStringExtra("docname"));
		tv_dept.setText(getIntent().getStringExtra("deptname"));
		tv_title.setText(getIntent().getStringExtra("title"));
		tv_content.setText(getIntent().getStringExtra("content"));
		tv_time.setText(getIntent().getStringExtra("time"));
		try
		{
			if(getSDHeaderImageUrl()!=null&&!getSDHeaderImageUrl().equals(""))
			{
				bitmap = BitmapFactory.decodeStream(new FileInputStream(
						new File(getSDHeaderImageUrl())));
				iv_my_head.setImageBitmap(bitmap);
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		sexcode = getIntent().getStringExtra("sexcode");
		
		if(sexcode!=null)
		{
			if(sexcode.equals("1"))
			{
				iv_head.setBackgroundResource(R.drawable.doc_male);
			}
			else
			{
				iv_head.setBackgroundResource(R.drawable.doc_female);
			}
		}
		iv_add.setOnClickListener(this);
		btn_send.setOnClickListener(this);
		
		getdatatask = new GetDataTask();
		getdatatask.execute();
	}
	
	class GetDataTask extends AsyncTask<Void, Void, String>
	{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			actionBar.startTextRefresh();
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			actionBar.endTextRefresh();
			try
			{
				//consultInfo 有二级列表
				changestatetask = new ChangeStateTask();
				changestatetask.execute();
				consultInfo = new ConsultInfo();
				JSONObject object = new JSONObject(result);
				String data = object.getString("data");
				consultInfo.buideJson(data);
				setData();
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			/*return HttpApi.getInstance().parserData(ConsultInfo.class, 
					"auth/interaction/detail", 
					new BsoftNameValuePair("rid",id),
					new BsoftNameValuePair("sn", loginUser.sn),
					new BsoftNameValuePair("id", loginUser.id)
					);*/
			return HttpApi.getInstance().post("auth/interaction/detail", new BsoftNameValuePair("rid",id),
					new BsoftNameValuePair("sn", loginUser.sn),
					new BsoftNameValuePair("id", loginUser.id));
		}
		
	}
	
	/**
	 * 设置用户和医生对话内容
	 */
	private void setData()
	{
		if(consultInfo.sysFiles!=null&&consultInfo.sysFiles.size()>0)//先设置我的回复内容
		{
			ll_images_server.setVisibility(View.VISIBLE);
			ArrayList<SysFiles> list = consultInfo.sysFiles;
			for(int i=0;i<list.size();i++)
			{
				final View consult_view = LayoutInflater.from(baseContext).inflate(R.layout.consult_image, null);
				final ImageView iv = (ImageView)consult_view.findViewById(R.id.iv);
				iv.setBackgroundResource(R.drawable.bg_default_img);
		        final String storePath = application.getStoreDir()+list.get(i).filename;
				//显示图片的配置  
		        DisplayImageOptions options = new DisplayImageOptions.Builder()  
		                .cacheInMemory(true)  
		                .cacheOnDisk(true)  
		                .bitmapConfig(Bitmap.Config.RGB_565)  
		                .build();  
//		        ImageLoader.getInstance().loadImage(Constants.HttpUrl+list.get(i).filefullpath, null, options, new SimpleImageLoadingListener(){
		        ImageLoader.getInstance().loadImage(Constants.getHttpUrl()+list.get(i).filefullpath, null, options, new SimpleImageLoadingListener(){

		            @Override  
		            public void onLoadingComplete(String imageUri, View view,  
		                    Bitmap loadedImage) {  
		                super.onLoadingComplete(imageUri, view, loadedImage);  
		                //iv.setImageBitmap(loadedImage); 
		                BitmapDrawable drawable = new BitmapDrawable(loadedImage);
		                iv.setBackgroundDrawable(drawable);
		                //存储到sd卡中
		                BitmapUtility.saveFile(loadedImage,storePath);
		            }  
		              
		        });
		        iv.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(ConsultDetailActivity.this,ImageDetailActivity.class);
						intent.putExtra("image_path", storePath);
						startActivity(intent);
					}
		        	
		        });
		        ll_images_server.addView(consult_view);
			}
		}
		if(consultInfo.interactServants!=null)
		{
			reply_list = consultInfo.interactServants;
		}
		for(int i=0;i<reply_list.size();i++)//在设置追加问题的内容和医生回复的内容
		{
			int type = Integer.valueOf(reply_list.get(i).type);
			ArrayList<SysFiles> list = reply_list.get(i).sysFiles;
			View view = new View(baseContext);;
			
			if(type == 1)
			{
				view =LayoutInflater.from(baseContext).inflate(R.layout.consult_detail_list_doctor, null);
				ImageView head = (ImageView)view.findViewById(R.id.iv_head);
				if(sexcode!=null)
				{
					if(sexcode.equals("1"))
					{
						head.setBackgroundResource(R.drawable.doc_male);
					}
					else
					{
						head.setBackgroundResource(R.drawable.doc_female);
					}
				}
			}
			else if(type == 2)
			{
				view = LayoutInflater.from(baseContext).inflate(R.layout.consult_detail_list_my, null);
				try//设置我的头像
				{
					RoundImageView my_head = (RoundImageView)view.findViewById(R.id.iv_head);
					if(getSDHeaderImageUrl()!=null&&!getSDHeaderImageUrl().equals(""))
					{
						bitmap = BitmapFactory.decodeStream(new FileInputStream(
								new File(getSDHeaderImageUrl())));
						my_head.setImageBitmap(bitmap);
					}
				}catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			
			TextView tv_content = (TextView)view.findViewById(R.id.tv_content);
			TextView tv_time = (TextView)view.findViewById(R.id.tv_time);
			final LinearLayout ll_images = (LinearLayout)view.findViewById(R.id.ll_images);
			if(list!=null&&list.size()>0)
			{
				ll_images.setVisibility(View.VISIBLE);
				for(int j=0;j<list.size();j++)
				{
					final View consult_view = LayoutInflater.from(baseContext).inflate(R.layout.consult_image, null);
					final ImageView iv = (ImageView)consult_view.findViewById(R.id.iv);
					iv.setBackgroundResource(R.drawable.bg_default_img);
					//获取要存储的文件名
			        final String storePath = application.getStoreDir()+list.get(j).filename;
					//显示图片的配置  
			        DisplayImageOptions options = new DisplayImageOptions.Builder()  
			                .cacheInMemory(true)  
			                .cacheOnDisk(true)  
			                .bitmapConfig(Bitmap.Config.RGB_565)  
			                .build();
//			        ImageLoader.getInstance().loadImage(Constants.HttpUrl+list.get(j).filefullpath, null, options, new SimpleImageLoadingListener(){
			        ImageLoader.getInstance().loadImage(Constants.getHttpUrl()+list.get(j).filefullpath, null, options, new SimpleImageLoadingListener(){

						@Override  
			            public void onLoadingComplete(String imageUri, View view,  
			                    Bitmap loadedImage) {  
			                super.onLoadingComplete(imageUri, view, loadedImage);  
			                //iv.setImageBitmap(loadedImage); 
			                BitmapDrawable drawable = new BitmapDrawable(loadedImage);
			                iv.setBackgroundDrawable(drawable);
			                //存储到sd卡中
			                BitmapUtility.saveFile(loadedImage,storePath);
			            }  
			              
			        });
			        iv.setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent intent = new Intent(ConsultDetailActivity.this,ImageDetailActivity.class);
							intent.putExtra("image_path", storePath);
							startActivity(intent);
						}
			        	
			        });
			        ll_images.addView(consult_view);
				}
			}
			if(reply_list.get(i).acontent!=null&&!reply_list.get(i).acontent.equals(""))
			{
				tv_content.setText(reply_list.get(i).acontent);
			}
			else
			{
				tv_content.setVisibility(View.GONE);
			}
			tv_time.setText(DateUtil.getDateTime("yyyy-MM-dd HH:mm:ss",Long.parseLong(reply_list.get(i).creattime)));
			ll_content.addView(view);
		}
		
	}
	
	public String getSDHeaderImageUrl() {
		return new StringBuffer(application.getStoreDir()).append("header")
				.append(".jpg").toString();
	}
	
	//设置当前发送的内容
	private void showCurrentSendData()
	{
		View view = LayoutInflater.from(baseContext).inflate(R.layout.consult_detail_list_my, null);
		TextView tv_content = (TextView)view.findViewById(R.id.tv_content);
		TextView tv_time = (TextView)view.findViewById(R.id.tv_time);
		if(!et_content.getText().toString().equals(""))
		{
			tv_content.setText(et_content.getText().toString());
		}
		else
		{
			tv_content.setVisibility(View.GONE);
		}
		if(sysfiles!=null&&sysfiles.size()>0)
		{
			for(int i=0;i<sysfiles.size();i++)
			{
				final String filePath = sysfiles.get(i);
				bitmap = BitmapUtility.getSmallBitmap(filePath,480,800);
				/*final String new_filePath = getConsultImageUrl();
				BitmapUtility.saveFile(bitmap, new_filePath);*/
				BitmapDrawable drawable = new BitmapDrawable(bitmap);
				final LinearLayout ll_images = (LinearLayout)view.findViewById(R.id.ll_images);
				final View consult_view = LayoutInflater.from(baseContext).inflate(R.layout.consult_image, null);
				final ImageView iv = (ImageView)consult_view.findViewById(R.id.iv);
				iv.setBackgroundDrawable(drawable);
				iv.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(ConsultDetailActivity.this,ImageDetailActivity.class);
						intent.putExtra("image_path", filePath);
						startActivity(intent);
					}
				});
				ll_images.addView(consult_view);
				ll_images.setVisibility(View.VISIBLE);
			}
		}
		tv_time.setText(DateUtil.dateFormate(new Date(),"yyyy-MM-dd HH:mm:ss"));
		ll_content.addView(view);
		et_content.setText("");
		ll_images_local.removeAllViews();
		ll_images_local.setVisibility(View.GONE);
		handler.post(new Runnable() {
		    @Override
		    public void run() {
		        m_scrollview.fullScroll(ScrollView.FOCUS_DOWN);
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
				File file = new File(filePath);
				// 检测图片是否存在
				if (file.exists()) {
					file.delete(); // 删除
				}
				break;
			}
		}
	}
	
	/**
	 * 删除当前使用的图片
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
	
	@Override
	public void findView() {
		// TODO Auto-generated method stub
		findActionBar();
		actionBar.setTitle("咨询详情");
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
		actionBar.setRefreshTextView("继续咨询", new Action(){

			@Override
			public int getDrawable() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public void performAction(View view) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ConsultDetailActivity.this,ConsultAddActivity.class);
				DoctorInfo doctorInfo = new DoctorInfo();
				doctorInfo.deptid = consultInfo.deptid;
				doctorInfo.deptname = tv_dept.getText().toString();
				doctorInfo.uid = consultInfo.docid;
				doctorInfo.name = consultInfo.docname;
				doctorInfo.sexcode = sexcode;
				intent.putExtra("doctorinfo", doctorInfo);
				startActivity(intent);
			}
			
		});
		actionBar.setBackGround(Color.TRANSPARENT);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
			case R.id.iv_add:
				if(sysfiles!=null&&sysfiles.size()<=3)
				{
					showCamera();
				}
				else
				{
					Toast.makeText(baseContext, "最多输入四张", Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.btn_send:
				if(et_content.getText().toString().equals(""))
				{
					if(sysfiles!=null&&sysfiles.size()>0)
					{
						btn_send.setClickable(false);
						uploaddatatask = new UploadDataTask();
						uploaddatatask.execute();
					}
					else
					{
						Toast.makeText(baseContext, "请输入内容或图片", Toast.LENGTH_SHORT).show();
					}
				}
				else
				{
					btn_send.setClickable(false);
					uploaddatatask = new UploadDataTask();
					uploaddatatask.execute();
				}
				break;
		}
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
					"upload/saveanswer", 
					strs,
					new BsoftNameValuePair("qid",consultInfo.id),
					new BsoftNameValuePair("acontent",et_content.getText().toString()),
					new BsoftNameValuePair("sn", loginUser.sn),
					new BsoftNameValuePair("uid", loginUser.id),//传用户uid
					new BsoftNameValuePair("type","2")//1医生回答，2用户提问
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
						showCurrentSendData();//显示当前发送的内容
						sysfiles = new ArrayList<String>();
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
			btn_send.setClickable(true);
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
        	if(SystemUtils.getSystemVersion() == SystemUtils.V4_4)
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
		iv_del.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ll_images_local.removeView(view);
				delSingleImage(storeConsultImage);
			}
			
		});
		ll_images_local.addView(view);
		ll_images_local.setVisibility(View.VISIBLE);
		iv.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ConsultDetailActivity.this,ImageDetailActivity.class);
				intent.putExtra("image_path", storeConsultImage);
				startActivity(intent);
			}
			
		});
	}
	
	/**
	 * 显示对话框
	 */
	public void showCamera() {
		builder = new Dialog(ConsultDetailActivity.this, R.style.alertDialogTheme);
		builder.show();
		viewDialog = LayoutInflater.from(baseContext).inflate(R.layout.camera_chose_alert, null);

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
							Toast.makeText(baseContext, "SD卡不可用!",
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
							Toast.makeText(baseContext, "SD卡不可用!",
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
		if(bitmap !=null)
		{
			bitmap.recycle();
			bitmap = null;
		}
		AsyncTaskUtil.cancelTask(getdatatask);
		AsyncTaskUtil.cancelTask(uploaddatatask);
		if(imageLoader!=null)
		{
			imageLoader.destroy();
		}
		delAllImage();
	}
	
	/**
	 * 修改消息状态
	 * @author Administrator
	 *
	 */
	class ChangeStateTask extends AsyncTask<Void, Void, ResultModel<NullModel>>
	{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			actionBar.startTextRefresh();
		}

		@Override
		protected ResultModel<NullModel> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			return HttpApi.getInstance().parserData(NullModel.class, "auth/interaction/updatereadstatus", 
					new BsoftNameValuePair("rid",id),
					new BsoftNameValuePair("sn", loginUser.sn),
					new BsoftNameValuePair("id", loginUser.id)
					);
		}

		@Override
		protected void onPostExecute(ResultModel<NullModel> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			actionBar.endTextRefresh();
			if (null != result) {
				try
				{
					if(result.statue == Statue.SUCCESS)
					{
						if(AppApplication.messageCount>0)//有未读消息，去前一个界面刷新
						{
							Intent intent = new Intent();
							intent.setAction(Constants.CONSULT_ACTION);
							sendBroadcast(intent);
						}
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
