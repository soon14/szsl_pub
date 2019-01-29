package com.bsoft.hospital.pub.suzhoumh.activity.cloud;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.app.tanklib.view.BsoftActionBar;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * @author :lizhengcao
 * @date :2019/1/4
 * E-mail:lizc@bsoft.com.cn
 * @类说明 云诊室
 */
public class CloudClinicActivity extends BaseActivity {

    @BindString(R.string.cloud_clinic)
    String cloudClinic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_cloud);
        mUnbinder = ButterKnife.bind(this);
        findView();
    }


    @OnClick({R.id.ll_cloud_reservation, R.id.ll_cloud_appointment_record})
    public void doClick(View v) {
        Intent intent;
        Class<?> clazz = null;
        switch (v.getId()) {
            case R.id.ll_cloud_reservation:
                //云预约
                clazz = CloudAppointmentRegistrationActivity.class;
                break;
            //预约记录
            case R.id.ll_cloud_appointment_record:
                clazz = CloudAppointmentRecordActivity.class;
                break;

        }
        intent = new Intent(baseContext, clazz);
        startActivity(intent);
    }

    @Override
    public void findView() {
        findActionBar();
        actionBar.setTitle(cloudClinic);
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
