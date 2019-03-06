package com.bsoft.hospital.pub.suzhoumh.activity.cloud;

import android.os.Bundle;
import android.view.View;

import com.app.tanklib.view.BsoftActionBar;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * @author :lizhengcao
 * @date :2019/3/6
 * E-mail:lizc@bsoft.com.cn
 * @类说明 互联网支付
 */
public class CloudHandheldPaymentActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_handheld_cloud);
        mUnbinder = ButterKnife.bind(this);
        findView();
    }

    @OnClick({R.id.tv_pending_item, R.id.tv_paid_item})
    public void doClick(View view) {
        switch (view.getId()) {
            case R.id.tv_pending_item:
                //待支付项目
                break;
            case R.id.tv_paid_item:
                //已支付项目
                break;
        }
    }

    @Override
    public void findView() {
        findActionBar();
        actionBar.setTitle("互联网支付");
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
