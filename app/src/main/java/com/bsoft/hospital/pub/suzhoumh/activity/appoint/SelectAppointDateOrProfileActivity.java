package com.bsoft.hospital.pub.suzhoumh.activity.appoint;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.tanklib.util.AsyncTaskUtil;
import com.app.tanklib.view.BsoftActionBar;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.appoint.model.ProfileFragment;
import com.bsoft.hospital.pub.suzhoumh.activity.appoint.model.SingleExpertDateFragment;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.model.app.appoint.AppointDeptVo;
import com.bsoft.hospital.pub.suzhoumh.model.app.appoint.AppointDoctorVo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author :lizhengcao
 * @date :2017/3/9
 * E-mail:lizc@bsoft.com.cn
 * @类说明
 */

public class SelectAppointDateOrProfileActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout llProfile, llDate;
    private TextView tvProfile, tvDate;
    private ImageView ivProfile, ivDate;
    private List<Fragment> fragsList;
    private AppointDoctorVo doctor;
    private AppointDeptVo dept;
    private boolean isShowFirst;
    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.ACTION_APPOINT_CLOSE)) {
                finish();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_date_select);
        initView();
        findView();
        initData();
        setListener();
    }

    private void setListener() {
        llProfile.setOnClickListener(this);
        llDate.setOnClickListener(this);
    }

    private void initData() {
        fragsList = new ArrayList<>();
        fragsList.add(new ProfileFragment());
        fragsList.add(new SingleExpertDateFragment());

        if (isShowFirst){
            showFragment(fragsList.get(0));
            ivProfile.setBackgroundResource(R.drawable.line_title);
            tvProfile.setTextColor(getResources().getColor(R.color.blue));
            ivDate.setVisibility(View.INVISIBLE);
            tvDate.setTextColor(getResources().getColor(R.color.gray_color));
        }else {
            showFragment(fragsList.get(1));
            ivDate.setBackgroundResource(R.drawable.line_title);
            tvDate.setTextColor(getResources().getColor(R.color.blue));
            ivProfile.setVisibility(View.INVISIBLE);
            tvProfile.setTextColor(getResources().getColor(R.color.gray_color));
        }

    }

    private void initView() {
        doctor= (AppointDoctorVo) getIntent().getSerializableExtra("doctor");
        dept = (AppointDeptVo) getIntent().getSerializableExtra("dept");
        isShowFirst=getIntent().getBooleanExtra("isShowFirst",true);

        llProfile = (LinearLayout) findViewById(R.id.ll_profile);
        llDate = (LinearLayout) findViewById(R.id.ll_date);
        tvProfile = (TextView) findViewById(R.id.tv_profile);
        tvDate = (TextView) findViewById(R.id.tv_date);
        ivProfile = (ImageView) findViewById(R.id.iv_profile);
        ivDate = (ImageView) findViewById(R.id.iv_date);

        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.ACTION_APPOINT_CLOSE);
        this.registerReceiver(receiver, filter);
    }

    public void showFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!fragment.isAdded()) {
            transaction.add(R.id.fl, fragment).commit();
        } else {
            transaction.show(fragment).commit();
        }
    }

    public void hideFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!fragment.isAdded()) {
            transaction.add(R.id.fl, fragment).hide(fragment).commit();
        } else {
            transaction.hide(fragment).commit();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_profile:
                showFragment(fragsList.get(0));
                ivProfile.setBackgroundResource(R.drawable.line_title);
                ivProfile.setVisibility(View.VISIBLE);
                tvProfile.setTextColor(getResources().getColor(R.color.blue));
                ivDate.setVisibility(View.INVISIBLE);
                tvDate.setTextColor(getResources().getColor(R.color.gray_color));
                hideFragment(fragsList.get(1));
                break;
            case R.id.ll_date:
                showFragment(fragsList.get(1));
                ivProfile.setVisibility(View.INVISIBLE);
                tvProfile.setTextColor(getResources().getColor(R.color.gray_color));
                ivDate.setBackgroundResource(R.drawable.line_title);
                ivDate.setVisibility(View.VISIBLE);
                tvDate.setTextColor(getResources().getColor(R.color.blue));
                hideFragment(fragsList.get(0));
                break;
            default:
                break;
        }
    }

    @Override
    public void findView() {
        findActionBar();
        actionBar.setTitle(dept.ksmc+"-"+doctor.ygxm);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }
}

