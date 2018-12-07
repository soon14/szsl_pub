package com.bsoft.hospital.pub.suzhoumh.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tanklib.Preferences;
import com.app.tanklib.bitmap.CacheManage;
import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.nineoldandroids.view.ViewHelper;
import com.app.tanklib.util.ExitUtil;
import com.app.tanklib.view.LRViewPager;
import com.app.tanklib.view.LRViewPager.ChangeViewCallback;
import com.app.tanklib.view.TipsView;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.AppApplication;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.activity.account.LoginActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.app.news.HospitalNewsActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.fragment.index.ConsultFragment;
import com.bsoft.hospital.pub.suzhoumh.fragment.index.HomeFragment;
import com.bsoft.hospital.pub.suzhoumh.fragment.index.MessageFragment;
import com.bsoft.hospital.pub.suzhoumh.fragment.index.MyFragment;
import com.bsoft.hospital.pub.suzhoumh.model.LoginUser;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.push.PushInfo;
import com.bsoft.hospital.pub.suzhoumh.push.Utils;
import com.bsoft.hospital.pub.suzhoumh.update.checkupdate.UpdateVersionNew;
import com.bsoft.hospital.pub.suzhoumh.util.AntiHijackingUtil;
import com.bsoft.hospital.pub.suzhoumh.util.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author Tank E-mail:zkljxq@126.com
 * @类说明
 */
public class MainTabActivity extends AppCompatActivity {

    @BindView(R.id.viewPager)
    LRViewPager mViewPager;
    @BindView(R.id.lay1)
    RelativeLayout lay1;
    @BindView(R.id.lay3)
    RelativeLayout lay3;
    @BindView(R.id.lay4)
    RelativeLayout lay4;
    @BindView(R.id.iv_message_count)
    ImageView iv_message_count;//消息红点
    @BindView(R.id.f1_n)
    ImageView f1N;
    @BindView(R.id.f3_n)
    ImageView f3N;
    @BindView(R.id.f4_n)
    ImageView f4N;
    @BindView(R.id.f1_p)
    ImageView f1P;
    @BindView(R.id.f3_p)
    ImageView f3P;
    @BindView(R.id.f4_p)
    ImageView f4P;
    @BindView(R.id.tv1_n)
    TextView tv1N;
    @BindView(R.id.tv3_n)
    TextView tv3N;
    @BindView(R.id.tv4_n)
    TextView tv4N;
    @BindView(R.id.tv1_p)
    TextView tv1P;
    @BindView(R.id.tv3_p)
    TextView tv3P;
    @BindView(R.id.tv4_p)
    TextView tv4P;

    MyPagerAdapter myPagerAdapter;
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    ArrayList<ImageView> normalFoots = new ArrayList<ImageView>();
    ArrayList<ImageView> selectedFoots = new ArrayList<ImageView>();

    ArrayList<TextView> normalTextViews = new ArrayList<TextView>();
    ArrayList<TextView> selectTextViews = new ArrayList<TextView>();
    TipsView tipsView;

    AppApplication application;
    LoginUser loginUser;

    Handler handler = new Handler();
    Dialog builder;
    View viewDialog;
    LayoutInflater mInflater;
    String storeHeader;
    UploadHeaderTask uploadHeaderTask;
    private String type;
    private Unbinder unbinder;
    private Context mContext;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        System.out.println("从通知消息进来");
        type = intent.getStringExtra("type");
        if (type != null) {
            if (type.equals(Constants.MESSAGE_TYPE_1))//咨询
            {
                mViewPager.setCurrentItem(1, false);
                changeIndex(1);
                ConsultFragment fragment = (ConsultFragment) mFragmentList.get(1);
                fragment.refreshData();
            } else if (type.equals(Constants.MESSAGE_TYPE_2))//院内通知
            {
                intent = new Intent(this, HospitalNewsActivity.class);
                startActivity(intent);
            } else if (type.equals(Constants.MESSAGE_TYPE_3))//消息
            {
                mViewPager.setCurrentItem(1, false);
                changeIndex(1);
                MessageFragment fragment = (MessageFragment) mFragmentList.get(2);
                fragment.refreshData();
            } else if (type.equals(Constants.MESSAGE_TYPE_4))//单点登录
            {
                // 单点登录
                Intent logoutIntent = new Intent(Constants.Logout_ACTION);
                logoutIntent.putExtra("message", intent.getStringExtra("message"));
                sendBroadcast(logoutIntent);
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_tab_pager);
        unbinder = ButterKnife.bind(this);
        this.mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = this;
        application = (AppApplication) getApplication();
        loginUser = application.getLoginUser();


        // 主tab界面切换
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.BACK_ACTION);
        filter.addAction(Constants.CLOSE_ACTION);
        filter.addAction(Constants.HomeMessageCount_ACTION);
        filter.addAction(Constants.MessageCountClear_ACTION);
        filter.addAction(Constants.Logout_ACTION);
        this.registerReceiver(this.broadcastReceiver, filter);


        findViews();
        //程序在后台从消息通知栏点进来
        type = getIntent().getStringExtra("type");
        if (type != null) {
            Intent intent = new Intent();
            if (type.equals(Constants.MESSAGE_TYPE_1)) {//咨询
                mViewPager.setCurrentItem(1, false);
                changeIndex(1);
                ConsultFragment fragment = (ConsultFragment) mFragmentList.get(1);
                fragment.refreshData();
            } else if (type.equals(Constants.MESSAGE_TYPE_2)) {//院内通知
                intent = new Intent(this, HospitalNewsActivity.class);
                startActivity(intent);
            } else if (type.equals(Constants.MESSAGE_TYPE_3)) {//消息
                mViewPager.setCurrentItem(2, false);
                changeIndex(2);
                MessageFragment fragment = (MessageFragment) mFragmentList.get(2);
                fragment.refreshData();
            } else if (type.equals(Constants.MESSAGE_TYPE_4)) {//单点登录
                Intent logoutIntent = new Intent(Constants.Logout_ACTION);
                logoutIntent.putExtra("message", getIntent().getStringExtra("message"));
                sendBroadcast(logoutIntent);
            }
        }

//        // 自动更新  因为存在着更新问题  故屏蔽不使用
//        new UpdateVersionTask(MainTabActivity.this, application.getStoreDir())
//                .execute();
        new UpdateVersionNew(mContext, application, false);
        initBaiduPush();
    }


    void findViews() {
        mViewPager.setOffscreenPageLimit(4);

        TopTabClickListener clickListener = new TopTabClickListener();
        lay1.setOnClickListener(clickListener);
        lay3.setOnClickListener(clickListener);
        lay4.setOnClickListener(clickListener);
        normalFoots.add(f1N);
        normalFoots.add(f3N);
        normalFoots.add(f4N);
        selectedFoots.add(f1P);
        selectedFoots.add(f3P);
        selectedFoots.add(f4P);

        normalTextViews.add(tv1N);
        normalTextViews.add(tv3N);
        normalTextViews.add(tv4N);
        selectTextViews.add(tv1P);
        selectTextViews.add(tv3P);
        selectTextViews.add(tv4P);

        mFragmentList.add(new HomeFragment());
        MessageFragment messgaeFragment = new MessageFragment();
        messgaeFragment.setTipsView(tipsView);
        mFragmentList.add(messgaeFragment);
        mFragmentList.add(new MyFragment());


        changeIndex(0);

        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(myPagerAdapter);
        mViewPager.setChangeViewCallback(new ChangeViewCallback() {

            @Override
            public void getCurrentPageIndex(int index) {
                changeIndex(index);
            }

            @Override
            public void changeView(boolean left, boolean right, int position,
                                   float positionOffset) {
                if (positionOffset > 0) {
                    ViewHelper.setAlpha(normalFoots.get(position),
                            positionOffset);
                    ViewHelper.setAlpha(normalTextViews.get(position),
                            positionOffset);
                    ViewHelper.setAlpha(selectedFoots.get(position),
                            1 - positionOffset);
                    ViewHelper.setAlpha(selectTextViews.get(position),
                            1 - positionOffset);
                    ViewHelper.setAlpha(normalFoots.get(position + 1),
                            1 - positionOffset);
                    ViewHelper.setAlpha(normalTextViews.get(position + 1),
                            1 - positionOffset);
                    ViewHelper.setAlpha(selectedFoots.get(position + 1),
                            positionOffset);
                    ViewHelper.setAlpha(selectTextViews.get(position + 1),
                            positionOffset);
                }
            }
        });
    }

    private class TopTabClickListener implements OnClickListener {

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.lay1) {
                mViewPager.setCurrentItem(0, false);
            } else if (view.getId() == R.id.lay3) {
                mViewPager.setCurrentItem(1, false);
            } else if (view.getId() == R.id.lay4) {
                mViewPager.setCurrentItem(2, false);
            }
            changeIndex(mViewPager.getCurrentItem());
        }
    }

    private void changeIndex(int index) {
        for (int i = 0; i < 3; i++) {
            if (i == index) {
                ViewHelper.setAlpha(normalFoots.get(index), 0);
                ViewHelper.setAlpha(selectedFoots.get(index), 1);
                ViewHelper.setAlpha(normalTextViews.get(index), 0);
                ViewHelper.setAlpha(selectTextViews.get(index), 1);
            } else {
                ViewHelper.setAlpha(normalFoots.get(i), 1);
                ViewHelper.setAlpha(normalTextViews.get(i), 1);
                ViewHelper.setAlpha(selectedFoots.get(i), 0);
                ViewHelper.setAlpha(selectTextViews.get(i), 0);
            }
        }
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * 得到每个页面
         */
        @Override
        public Fragment getItem(int arg0) {
            return (mFragmentList == null || mFragmentList.size() == 0) ? null
                    : mFragmentList.get(arg0);
        }

        @Override
        public int getCount() {
            return mFragmentList == null ? 0 : mFragmentList.size();
        }
    }

    // 启动百度推送服务
    private void initBaiduPush() {

        Log.i("baidupush", "connected->" + PushManager.isConnected(application) + " pushenabled->" + PushManager.isPushEnabled(application));
        System.out.println("============deviceId==" + Preferences
                .getInstance().getStringData("deviceId"));
        if (Utility.isNetworkAvailable(application)) {
            if (!Utils.hasBind(application)) {
                Log.i("baidupush", "unbind->startWrok");
                PushManager.startWork(getApplicationContext(),
                        PushConstants.LOGIN_TYPE_API_KEY,
                        Utils.getMetaValue(MainTabActivity.this, "api_key"));//启动推送服务
            } else {
                if (!PushManager.isConnected(application) || !PushManager.isPushEnabled(application)) {
                    Log.i("baidupush", "unconnected->startWork");
                    PushManager.startWork(getApplicationContext(),
                            PushConstants.LOGIN_TYPE_API_KEY,
                            Utils.getMetaValue(MainTabActivity.this, "api_key"));//启动推送服务
                }
            }
        } else {
            Toast.makeText(application, "当前网络没有连接", Toast.LENGTH_SHORT).show();
        }
    }

    // 写一个广播的内部类，当收到动作时，结束activity
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constants.HomeMessageCount_ACTION.equals(intent.getAction())) {
                if (AppApplication.messageCount <= 0) {
                    iv_message_count.setVisibility(View.GONE);
                } else {
                    iv_message_count.setVisibility(View.VISIBLE);

                }
            } else if (Constants.MessageCountClear_ACTION.equals(intent
                    .getAction())) {
                AppApplication.messageCount = 0;
                iv_message_count.setVisibility(View.GONE);
            } else if (Constants.CLOSE_ACTION.equals(intent.getAction())) {
                release();
                finish();
            } else if (Constants.Logout_ACTION.equals(intent.getAction())) {
                // 消息推送过来，改数字及内容
                PushInfo pushInfo = (PushInfo) intent
                        .getSerializableExtra("pushInfo");
                if (null != pushInfo) {
                    if (null != builder && builder.isShowing()) {
                        builder.dismiss();
                    }
                    if (pushInfo.login == 1) {
                        logout1(pushInfo.description);
                    } else {
                        logout(pushInfo.description);
                    }
                }
            }
        }
    };

    void logout(String description) {
        builder = new Dialog(MainTabActivity.this, R.style.alertDialogTheme);
        builder.setCancelable(false);
        builder.show();
        viewDialog = mInflater.inflate(R.layout.logout_push_alert, null);

        // 设置对话框的宽高
        LayoutParams layoutParams = new LayoutParams(
                AppApplication.getWidthPixels() * 85 / 100,
                LayoutParams.WRAP_CONTENT);
        builder.setContentView(viewDialog, layoutParams);
        ((TextView) viewDialog.findViewById(R.id.message)).setText(description);
        viewDialog.findViewById(R.id.delect).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                builder.dismiss();
                new Thread() {
                    public void run() {
                        HttpApi.getInstance().get("ecard.ifc.login.again", new BsoftNameValuePair("sn", loginUser.sn), new BsoftNameValuePair("type", "2"), new BsoftNameValuePair("deviceToken", Preferences.getInstance().getStringData("userId")), new BsoftNameValuePair("channelId", Preferences.getInstance().getStringData("channelId")), new BsoftNameValuePair("locale", Preferences.getInstance().getStringData("deviceId")));
                    }

                    ;
                }.start();
            }
        });
        viewDialog.findViewById(R.id.cancel).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        builder.dismiss();
                        application.clearInfo();
                        Preferences.getInstance().setStringData("index", null);
                        Preferences.getInstance()
                                .setStringData("weather", null);
                        Preferences.getInstance()
                                .setStringData("dynamic", null);
                        Intent intent = new Intent(MainTabActivity.this,
                                LoginActivity.class);
                        startActivity(intent);
                        sendBroadcast(new Intent(Constants.CLOSE_ACTION));
                    }
                });
    }

    void logout1(String description) {
        builder = new Dialog(MainTabActivity.this, R.style.alertDialogTheme);
        builder.setCancelable(false);
        builder.show();
        viewDialog = mInflater.inflate(R.layout.logout_push_alert1, null);

        // 设置对话框的宽高
        LayoutParams layoutParams = new LayoutParams(
                AppApplication.getWidthPixels() * 85 / 100,
                LayoutParams.WRAP_CONTENT);
        builder.setContentView(viewDialog, layoutParams);
        ((TextView) viewDialog.findViewById(R.id.message)).setText(description);
        viewDialog.findViewById(R.id.delect).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                builder.dismiss();
                application.clearInfo();
                Intent intent = new Intent(MainTabActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // land do nothing is ok
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // port do nothing is ok
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mViewPager.getCurrentItem() != 0) {
                mViewPager.setCurrentItem(0, false);
            } else {
                dialog();
            }
            return true;
        }
        return false;
    }

    protected void dialog() {
        ExitUtil.ExitApp(this);
    }

    // 释放所有的资源
    void release() {
        // 线程释放
//		ImageLoader.shutdown();
        // 释放缓存数据
        // ModelCache.getInstance().clear();
    }

    @Override
    public void onDestroy() {
        if (unbinder != null)
            unbinder.unbind();
        super.onDestroy();
        if (null != this.broadcastReceiver) {
            this.unregisterReceiver(this.broadcastReceiver);
            broadcastReceiver = null;
        }
        release();
        // 释放图片
        CacheManage.getInstance().clearCache();
        System.gc();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 相册
        if (requestCode == 40110) {
            if (resultCode == RESULT_OK) {
                uploadHeaderTask = new UploadHeaderTask();
                uploadHeaderTask.execute();
            }
        }
        // 相机
        else if (requestCode == 40120) {
            if (resultCode == RESULT_OK) {
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(Uri.fromFile(new File(storeHeader)),
                        "image/*");
                intent.putExtra("crop", "true");
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("outputX", 250);
                intent.putExtra("outputY", 250);
                intent.putExtra("output", Uri.fromFile(new File(storeHeader)));
                intent.putExtra("outputFormat", "JPEG");// 返回格式
                startActivityForResult(intent, 40110);
            }
        }
    }

    @SuppressLint("InflateParams")
    public void showCamera() {
        builder = new Dialog(MainTabActivity.this, R.style.alertDialogTheme);
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
                    public void onClick(View v) {
                        if (checkSDCard()) {
                            storeHeader = getSDHeaderImageUrl();
                            Intent getImageByCamera = new Intent(
                                    "android.media.action.IMAGE_CAPTURE");
                            Uri localUri = Uri.fromFile(new File(storeHeader));
                            getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT,
                                    localUri);
                            getImageByCamera.putExtra(
                                    "android.intent.extra.videoQuality", 0);
                            startActivityForResult(getImageByCamera, 40120);
                            builder.dismiss();
                        } else {
                            Toast.makeText(MainTabActivity.this, "SD卡不可用!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        viewDialog.findViewById(R.id.sd).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (checkSDCard()) {
                            storeHeader = getSDHeaderImageUrl();
                            Intent intent = null;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                                intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                            } else {
                                intent = new Intent(
                                        Intent.ACTION_GET_CONTENT);
                                intent.setType("image/*");
                            }
                            intent.putExtra("crop", "true");// crop=true
                            // 有这句才能出来最后的裁剪页面.
                            intent.putExtra("aspectX", 1);// 这两项为裁剪框的比例.
                            intent.putExtra("aspectY", 1);// x:y=1:2
                            intent.putExtra("outputX", 250);
                            intent.putExtra("outputY", 250);
                            intent.putExtra("output",
                                    Uri.fromFile(new File(storeHeader)));
                            intent.putExtra("outputFormat", "JPEG");// 返回格式
                            startActivityForResult(
                                    Intent.createChooser(intent, "选择图片"), 40110);
                            builder.dismiss();
                        } else {
                            Toast.makeText(MainTabActivity.this, "SD卡不可用!",
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

    public String getSDHeaderImageUrl() {
        return new StringBuffer(application.getStoreDir()).append("header")
                .append(".jpg").toString();
    }

    /**
     * 检查SD卡是否可用
     */
    private boolean checkSDCard() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    @SuppressLint("StaticFieldLeak")
    class UploadHeaderTask extends AsyncTask<Void, Object, ResultModel<String>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ResultModel<String> doInBackground(Void... params) {
            return HttpApi.getInstance().postHeader(storeHeader,
                    new BsoftNameValuePair("uid", loginUser.id),
                    new BsoftNameValuePair("sn", loginUser.sn));
        }

        @Override
        protected void onPostExecute(ResultModel<String> result) {
            if (null != result) {
                if (result.statue == Statue.SUCCESS && null != result.data) {
                    sendBroadcast(new Intent(Constants.Header_ACTION));
                    loginUser.header = result.data;
                    application.setLoginUser(loginUser);
                } else {
                    result.showToast(MainTabActivity.this);
                }
            }
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        AntiHijackingUtil.checkActivity(this);
    }


}
