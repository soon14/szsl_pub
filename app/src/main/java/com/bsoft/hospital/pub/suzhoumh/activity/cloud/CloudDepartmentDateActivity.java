package com.bsoft.hospital.pub.suzhoumh.activity.cloud;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.app.tanklib.view.BsoftActionBar;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.cloud.event.AppointConfirmEvent;
import com.bsoft.hospital.pub.suzhoumh.activity.cloud.fragment.CloudSelectDateFragment;
import com.bsoft.hospital.pub.suzhoumh.model.cloud.SelectDeptModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;


/**
 * @author :lizhengcao
 * @date :2019/1/7
 * E-mail:lizc@bsoft.com.cn
 * @类说明 云诊室科室进入到日历界面
 */
public class CloudDepartmentDateActivity extends BaseActivity {

    private SelectDeptModel selectDept;
    private String cloudType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_department_cloud);
        mUnbinder = ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        selectDept = (SelectDeptModel) getIntent().getSerializableExtra("selectDept");
        cloudType = getIntent().getStringExtra(Constants.CLOUD_TYPE);
        findView();

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.fl_calender, CloudSelectDateFragment.getInstance(selectDept, cloudType, null));
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
