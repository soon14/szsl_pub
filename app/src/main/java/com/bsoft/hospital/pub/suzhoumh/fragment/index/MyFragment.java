package com.bsoft.hospital.pub.suzhoumh.fragment.index;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tanklib.bitmap.CacheManage;
import com.app.tanklib.bitmap.IndexUrlCache;
import com.app.tanklib.bitmap.view.RoundImageView;
import com.app.tanklib.util.StringUtil;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.activity.MainTabActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.app.appoint.AppointHistoryActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.app.visit.VisitListActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.cloud.CloudExpressReceiverActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.my.SettingAboutActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.my.SettingActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.my.family.MyFamilyActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.my.info.MyInfoActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.my.insurance.InsuranceInfoActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.my.note.MyNoteActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.securitysocial.soochowjar.SocialSecurityManager;

/**
 * 我的
 *
 * @author Administrator
 */
public class MyFragment extends BaseFragment implements OnClickListener {

    RelativeLayout ll_myinfo, ll_myfamily, ll_orderhistory, ll_sweep, ll_setting, ll_visit, ll_insuranceinfo, ll_mynote, ll_goods_address;
    TextView name;
    RoundImageView header;
    Bitmap bitmap;
    LayoutInflater mInflater;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.my, container, false);
        return mainView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.urlMap = new IndexUrlCache(1);
        this.mInflater = (LayoutInflater) baseContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        findView();
        initData();
    }

    @Override
    public void startHint() {
        if (isLoaded) {
            return;
        }
        setHeaderView();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.Header_ACTION);
        filter.addAction(Constants.Name_ACTION);
        baseContext.registerReceiver(this.broadcastReceiver, filter);
        isLoaded = true;
    }

    @Override
    public void endHint() {

    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Constants.Header_ACTION.equals(intent.getAction())) {
                try {
                    bitmap = BitmapFactory.decodeStream(new FileInputStream(new File(getSDHeaderImageUrl())));
                    header.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (Constants.Name_ACTION.equals(intent.getAction())) {
                String n = intent.getStringExtra("name");
                if (!StringUtil.isEmpty(n)) {
                    name.setText(n);
                }
            }
        }
    };

    private void setHeaderView() {
        if (null != loginUser) {
            name.setText(loginUser.realname);
            if (!StringUtil.isEmpty(loginUser.header)) {
                header.setImageUrl(HttpApi.getImageUrl(loginUser.header, CacheManage.IMAGE_TYPE_HEADER), CacheManage.IMAGE_TYPE_HEADER, R.drawable.avatar_none);
            } else {
                header.setImageResource(R.drawable.avatar_none);
            }
        }
    }

    public String getSDHeaderImageUrl() {
        return new StringBuffer(application.getStoreDir()).append("header").append(".jpg").toString();
    }

    public void findView() {
        name = (TextView) mainView.findViewById(R.id.name);
        ll_myinfo = (RelativeLayout) mainView.findViewById(R.id.ll_myinfo);
        ll_myfamily = (RelativeLayout) mainView.findViewById(R.id.ll_myfamily);
        ll_orderhistory = (RelativeLayout) mainView.findViewById(R.id.ll_orderhistroy);
        ll_sweep = (RelativeLayout) mainView.findViewById(R.id.ll_sweep);
        ll_setting = (RelativeLayout) mainView.findViewById(R.id.ll_setting);
        ll_visit = (RelativeLayout) mainView.findViewById(R.id.ll_visit);
        ll_insuranceinfo = (RelativeLayout) mainView.findViewById(R.id.ll_insuranceinfo);
        ll_mynote = (RelativeLayout) mainView.findViewById(R.id.ll_mynote);
        header = (RoundImageView) mainView.findViewById(R.id.header);
        ll_goods_address = mainView.findViewById(R.id.ll_goods_address);
    }

    public void initData() {
        ll_myinfo.setOnClickListener(this);
        ll_myfamily.setOnClickListener(this);
        ll_sweep.setOnClickListener(this);
        ll_orderhistory.setOnClickListener(this);
        ll_setting.setOnClickListener(this);
        ll_visit.setOnClickListener(this);
        ll_insuranceinfo.setOnClickListener(this);
        ll_mynote.setOnClickListener(this);
        header.setOnClickListener(this);
        ll_goods_address.setOnClickListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != bitmap) {
            bitmap.recycle();
            bitmap = null;
        }
        if (null != this.broadcastReceiver && isReceiver) {
            baseContext.unregisterReceiver(this.broadcastReceiver);
            broadcastReceiver = null;
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.ll_goods_address://我的收货地址
                intent = new Intent(baseContext, CloudExpressReceiverActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_myinfo://完善信息
                intent = new Intent(baseContext, MyInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_myfamily:
                intent = new Intent(baseContext, MyFamilyActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_sweep:
                intent = new Intent(baseContext, SettingAboutActivity.class);
                intent.putExtra("title", "扫一扫");
                startActivity(intent);
                break;
            case R.id.ll_orderhistroy:
                intent = new Intent(baseContext, AppointHistoryActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_setting:
                intent = new Intent(baseContext, SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_visit:
                intent = new Intent(baseContext, VisitListActivity.class);
                startActivity(intent);
                break;
            case R.id.header:
                ((MainTabActivity) getActivity()).showCamera();
                break;
            case R.id.ll_insuranceinfo:
                if (loginUser.natureJudje() && TextUtils.equals("0", loginUser.isvalidate)) {
                    Toast.makeText(baseContext, "该功能需要签约苏州银行线上支付并认证通过后使用", Toast.LENGTH_LONG).show();
                } else {
                    intent = new Intent(baseContext, InsuranceInfoActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.ll_mynote:
                intent = new Intent(baseContext, MyNoteActivity.class);
                startActivity(intent);
                break;
        }
    }
}
