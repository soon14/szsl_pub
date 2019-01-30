package com.bsoft.hospital.pub.suzhoumh.activity.cloud;

import android.os.Bundle;
import android.view.View;

import com.app.tanklib.view.BsoftActionBar;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;

import butterknife.ButterKnife;


/**
 * @author :lizhengcao
 * @date :2019/1/30
 * E-mail:lizc@bsoft.com.cn
 * @类说明 云诊室签到取号
 */
public class CloudSignInActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_sign_cloud);
        mUnbinder = ButterKnife.bind(this);
        findView();
    }

    @Override
    public void findView() {
        findActionBar();
        actionBar.setTitle("签到取号");
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
