package com.bsoft.hospital.pub.suzhoumh.activity.cloud;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.app.tanklib.view.BsoftActionBar;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;

import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * @author :lizhengcao
 * @date :2019/1/4
 * E-mail:lizc@bsoft.com.cn
 * @类说明 云诊室预约挂号
 */
public class CloudAppointmentRegistrationActivity extends BaseActivity {

    @BindString(R.string.cloud_appointment_registration)
    String mCloudAppointmentRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_appointment_cloud);
        mUnbinder = ButterKnife.bind(this);
        findView();
    }

    /**
     * 科室类型说明： 1-专科， 2-专家，3-特色
     *
     * @param v
     */
    private String cloudType;//云诊室预约类型

    @OnClick({R.id.ll_expert_clinic, R.id.ll_ordinary_clinic, R.id.ll_feature_clinic})
    public void doClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.ll_ordinary_clinic:
                //专科门诊
                cloudType = CloudSelectionDepartmentActivity.ORDINARY_CLINIC_TYPE;
                break;
            case R.id.ll_expert_clinic:
                //专家门诊
                cloudType = CloudSelectionDepartmentActivity.EXPERT_CLINIC_TYPE;
                break;
            case R.id.ll_feature_clinic:
                //特色门诊
                cloudType = CloudSelectionDepartmentActivity.FEATURE_CLINIC_TYPE;
                break;
        }

        intent = new Intent(baseContext, CloudSelectionDepartmentActivity.class);
        intent.putExtra(Constants.CLOUD_TYPE, cloudType);
        startActivity(intent);
    }

    @Override
    public void findView() {
        findActionBar();
        actionBar.setTitle(mCloudAppointmentRegistration);
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
