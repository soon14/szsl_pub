package com.bsoft.hospital.pub.suzhoumh.activity.cloud;

import android.media.DrmInitData;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.app.tanklib.view.BsoftActionBar;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.adapter.cloud.CloudExpertOrDateVPAdapter;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.cloud.event.AppointConfirmEvent;
import com.bsoft.hospital.pub.suzhoumh.activity.cloud.fragment.CloudIntroduceFragment;
import com.bsoft.hospital.pub.suzhoumh.activity.cloud.fragment.CloudScheduleFragment;
import com.bsoft.hospital.pub.suzhoumh.activity.cloud.fragment.CloudSelectDateFragment;
import com.bsoft.hospital.pub.suzhoumh.model.cloud.CloudSelectExpertModel;
import com.bsoft.hospital.pub.suzhoumh.model.cloud.SelectDeptModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author :lizhengcao
 * @date :2019/1/16
 * E-mail:lizc@bsoft.com.cn
 * @类说明 云诊室简介和排班表
 */
public class CloudIntroduceOrScheduleActivity extends BaseActivity {

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    private List<Fragment> frags;
    private String[] titles = {"简介", "排班表"};
    private CloudExpertOrDateVPAdapter adapter;
    private CloudSelectExpertModel expert;
    private SelectDeptModel selectDept;
    private int pos;
    private String cloudType;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_introdece_cloud);
        mUnbinder = ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initData();
        findView();
        setAdapter();
    }

    private void setAdapter() {
        adapter = new CloudExpertOrDateVPAdapter(getSupportFragmentManager(), frags, titles);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.getTabAt(pos).select(); //默认选中某项放在加载viewpager之后
    }

    private void initData() {

        expert = (CloudSelectExpertModel) getIntent().getSerializableExtra("expert");
        selectDept = (SelectDeptModel) getIntent().getSerializableExtra("selectDept");
        pos = getIntent().getIntExtra("pos", 0);
        cloudType = getIntent().getStringExtra(Constants.CLOUD_TYPE);
        date = getIntent().getStringExtra("date");

        frags = new ArrayList<>();
        frags.add(CloudIntroduceFragment.getInstance());
        if ("".equals(date))
            frags.add(CloudSelectDateFragment.getInstance(selectDept, cloudType,expert));
        else
            frags.add(CloudScheduleFragment.getInstance(expert, selectDept, cloudType, date));

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAppointConfirmEvent(AppointConfirmEvent event) {
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void findView() {
        findActionBar();
        actionBar.setTitle(selectDept.ksmc + "-" + expert.ygxm);
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
