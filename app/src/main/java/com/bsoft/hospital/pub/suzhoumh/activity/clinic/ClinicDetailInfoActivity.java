package com.bsoft.hospital.pub.suzhoumh.activity.clinic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.tanklib.view.BsoftActionBar;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author :lizhengcao
 * @date :2017/3/10
 * E-mail:lizc@bsoft.com.cn
 * @类说明 就诊明细、门诊病历
 */

public class ClinicDetailInfoActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout llClinic, llPatient;
    private TextView tvClinic, tvPatient;
    private ImageView ivClinic, ivPatient;
    private List<Fragment> fragsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_detail_clinic);
        findView();
        initView();
        initData();
        setListener();
    }

    private void setListener() {
        llClinic.setOnClickListener(this);
        llPatient.setOnClickListener(this);
    }

    private void initData() {
        fragsList = new ArrayList<>();
        fragsList.add(new ClinicDetailFragment());
        fragsList.add(new OutPatientSickFragment());

        showFragment(fragsList.get(0));
        ivClinic.setBackgroundResource(R.drawable.line_title);
        tvClinic.setTextColor(getResources().getColor(R.color.blue));
        ivPatient.setVisibility(View.INVISIBLE);
        tvPatient.setTextColor(getResources().getColor(R.color.gray_color));
    }

    private void initView() {
        llClinic = (LinearLayout) findViewById(R.id.ll_clinic);
        llPatient = (LinearLayout) findViewById(R.id.ll_patient);
        tvClinic = (TextView) findViewById(R.id.tv_clinic);
        tvPatient = (TextView) findViewById(R.id.tv_patient);
        ivClinic = (ImageView) findViewById(R.id.iv_clinic);
        ivPatient = (ImageView) findViewById(R.id.iv_patient);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_clinic:
                //就诊明细
                showFragment(fragsList.get(0));
                ivClinic.setBackgroundResource(R.drawable.line_title);
                ivClinic.setVisibility(View.VISIBLE);
                tvClinic.setTextColor(getResources().getColor(R.color.blue));
                ivPatient.setVisibility(View.INVISIBLE);
                tvPatient.setTextColor(getResources().getColor(R.color.gray_color));
                hideFragment(fragsList.get(1));
                break;
            case R.id.ll_patient:
                //门诊病例
                showFragment(fragsList.get(1));
                ivClinic.setVisibility(View.INVISIBLE);
                tvClinic.setTextColor(getResources().getColor(R.color.gray_color));
                ivPatient.setBackgroundResource(R.drawable.line_title);
                ivPatient.setVisibility(View.VISIBLE);
                tvPatient.setTextColor(getResources().getColor(R.color.blue));
                hideFragment(fragsList.get(0));
                break;
            default:
                break;
        }
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
    public void findView() {
        findActionBar();
        actionBar.setTitle("就诊历史");
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
