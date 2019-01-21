package com.bsoft.hospital.pub.suzhoumh.activity.cloud;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.view.BsoftActionBar;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.adapter.cloud.CloudExpertOrDateVPAdapter;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.cloud.fragment.CloudAppointmentRecordFragment;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.model.cloud.CloudAppointmentRecordModel;
import com.bsoft.hospital.pub.suzhoumh.model.cloud.CloudSelectExpertModel;
import com.bsoft.hospital.pub.suzhoumh.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * @author :lizhengcao
 * @date :2019/1/21
 * E-mail:lizc@bsoft.com.cn
 * @类说明 云诊室预约记录
 */
public class CloudAppointmentRecordActivity extends BaseActivity {

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    private List<Fragment> frags;
    private String[] titles = {"当前预约", "历史预约"};
    private CloudExpertOrDateVPAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_appointment_cloud);
        mUnbinder = ButterKnife.bind(this);
        findView();
        initData();
    }

    private void initData() {
        frags = new ArrayList<>();
        frags.add(CloudAppointmentRecordFragment.getInstance(CloudAppointmentRecordFragment.currentFlag));
        frags.add(CloudAppointmentRecordFragment.getInstance(CloudAppointmentRecordFragment.historyFlag));
        adapter = new CloudExpertOrDateVPAdapter(getSupportFragmentManager(), frags, titles);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager, false);
    }

    @Override
    public void findView() {
        findActionBar();
        actionBar.setTitle("预约记录");
        actionBar.setBackAction(new BsoftActionBar.Action() {
            @Override
            public int getDrawable() {
                return R.drawable.btn_back;
            }

            @Override
            public void performAction(View view) {
                finish();
            }

        });
    }
}
