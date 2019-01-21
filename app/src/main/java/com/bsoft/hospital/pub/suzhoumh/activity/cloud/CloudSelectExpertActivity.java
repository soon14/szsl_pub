package com.bsoft.hospital.pub.suzhoumh.activity.cloud;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.app.tanklib.view.BsoftActionBar;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.cloud.event.AppointConfirmEvent;
import com.bsoft.hospital.pub.suzhoumh.activity.cloud.fragment.CloudScheduleFragment;
import com.bsoft.hospital.pub.suzhoumh.activity.cloud.fragment.CloudSelectDateFragment;
import com.bsoft.hospital.pub.suzhoumh.activity.cloud.fragment.CloudSelectExpertFragment;
import com.bsoft.hospital.pub.suzhoumh.fragment.index.BaseFragment;
import com.bsoft.hospital.pub.suzhoumh.model.cloud.CloudSelectExpertModel;
import com.bsoft.hospital.pub.suzhoumh.model.cloud.SelectDeptModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @author :lizhengcao
 * @date :2019/1/16
 * E-mail:lizc@bsoft.com.cn
 * @类说明 云诊室通过日历日期来选择专家/普通
 */
public class CloudSelectExpertActivity extends BaseActivity {

    private SelectDeptModel selectDept;
    private String date;
    private String cloudType;
    private CloudSelectExpertModel expert;
    private BaseFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert_select_cloud);
        EventBus.getDefault().register(this);
        selectDept = (SelectDeptModel) getIntent().getSerializableExtra("selectDept");
        date = getIntent().getStringExtra("date");
        cloudType = getIntent().getStringExtra(Constants.CLOUD_TYPE);
        expert = (CloudSelectExpertModel) getIntent().getSerializableExtra("expert");

        findView();

        if (expert != null)
            fragment = CloudScheduleFragment.getInstance(expert, selectDept, cloudType, date);
        else
            switch (cloudType) {
                case CloudSelectionDepartmentActivity.ORDINARY_CLINIC_TYPE:
                case CloudSelectionDepartmentActivity.FEATURE_CLINIC_TYPE:
                    fragment = CloudScheduleFragment.getInstance(null, selectDept, cloudType, date);
                    break;
                case CloudSelectionDepartmentActivity.EXPERT_CLINIC_TYPE:
                    fragment = CloudSelectExpertFragment.getInstance(selectDept, date, cloudType);
                    break;
            }


        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.fl_expert, fragment);
        t.commit();
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
        actionBar.setTitle(selectDept.ksmc);
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
