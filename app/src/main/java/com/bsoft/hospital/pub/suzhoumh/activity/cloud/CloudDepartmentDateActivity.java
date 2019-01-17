package com.bsoft.hospital.pub.suzhoumh.activity.cloud;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.app.tanklib.http.BsoftNameValuePair;
import com.app.tanklib.view.BsoftActionBar;
import com.bsoft.calendar.caldroid.CaldroidFragment;
import com.bsoft.calendar.caldroid.CaldroidListener;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.cloud.fragment.CaldroidFragmentUtil;
import com.bsoft.hospital.pub.suzhoumh.activity.cloud.fragment.CloudSelectDateFragment;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.ResultModel;
import com.bsoft.hospital.pub.suzhoumh.model.Statue;
import com.bsoft.hospital.pub.suzhoumh.model.cloud.CloudDoctorModel;
import com.bsoft.hospital.pub.suzhoumh.model.cloud.SchedulingDateModel;
import com.bsoft.hospital.pub.suzhoumh.model.cloud.SelectDeptModel;
import com.bsoft.hospital.pub.suzhoumh.util.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindDrawable;
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
        selectDept = (SelectDeptModel) getIntent().getSerializableExtra("selectDept");
        cloudType = getIntent().getStringExtra(Constants.CLOUD_TYPE);
        findView();

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.fl_calender, CloudSelectDateFragment.getInstance(selectDept, cloudType, null));
        t.commit();
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
