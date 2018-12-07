package com.bsoft.hospital.pub.suzhoumh.activity.appoint;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.tanklib.util.AsyncTaskUtil;
import com.app.tanklib.view.BsoftActionBar;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.view.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author :lizhengcao
 * @date :2017/3/7
 * E-mail:lizc@bsoft.com.cn
 * @类说明
 */

public class SelectDoctorOrDateActivity extends BaseActivity implements View.OnClickListener {


    private LinearLayout llExpert, llDate;
    private TextView tvExpert, tvDate;
    private ImageView ivExpert, ivDate;
    private List<Fragment> fragsList;
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
        setContentView(R.layout.activity_doctor_date);
        findView();
        initView();
        initData();

        setListener();
    }

    private void setListener() {
        llExpert.setOnClickListener(this);
        llDate.setOnClickListener(this);
    }

    private void initData() {
        fragsList = new ArrayList<>();
        fragsList.add(new DoctorFragment());
        fragsList.add(new DateFragment());

        showFragment(fragsList.get(0));
        ivExpert.setBackgroundResource(R.drawable.line_title);
        tvExpert.setTextColor(getResources().getColor(R.color.blue));
        ivDate.setVisibility(View.INVISIBLE);
        tvDate.setTextColor(getResources().getColor(R.color.gray_color));

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

    private void initView() {
        llExpert = (LinearLayout) findViewById(R.id.ll_expert);
        llDate = (LinearLayout) findViewById(R.id.ll_date);
        tvExpert = (TextView) findViewById(R.id.tv_expert);
        tvDate = (TextView) findViewById(R.id.tv_date);
        ivExpert = (ImageView) findViewById(R.id.iv_expert);
        ivDate = (ImageView) findViewById(R.id.iv_date);

        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.ACTION_APPOINT_CLOSE);
        this.registerReceiver(receiver, filter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_expert:
                showFragment(fragsList.get(0));
                ivExpert.setBackgroundResource(R.drawable.line_title);
                ivExpert.setVisibility(View.VISIBLE);
                tvExpert.setTextColor(getResources().getColor(R.color.blue));
                ivDate.setVisibility(View.INVISIBLE);
                tvDate.setTextColor(getResources().getColor(R.color.gray_color));
                hideFragment(fragsList.get(1));
                break;
            case R.id.ll_date:
                showFragment(fragsList.get(1));
                ivExpert.setVisibility(View.INVISIBLE);
                tvExpert.setTextColor(getResources().getColor(R.color.gray_color));
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
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }

    @Override
    public void findView() {
        findActionBar();
//        actionBar.setTitle("苏州市立医院东区");
        actionBar.setTitle(Constants.getHospitalName());
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
