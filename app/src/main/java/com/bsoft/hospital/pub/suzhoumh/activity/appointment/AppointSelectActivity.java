package com.bsoft.hospital.pub.suzhoumh.activity.appointment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.tanklib.view.BsoftActionBar;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.fragment.appointment.SelectDoctorFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author :lizhengcao
 * @date :2017/2/16
 * E-mail:lizc@bsoft.com.cn
 * @类说明
 */

public class AppointSelectActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout mRL1, mRL2;
    private TextView mTv1, mTv2;
    private ImageView mIv1, mIv2;
    private List<Fragment> frags;


    @Override
    public void findView() {
        findActionBar();
        actionBar.setTitle("选择医生");
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_appooint);
        findView();
        //找控件
        initView();
        setListener();


    }

    private void setListener() {
        mRL1.setOnClickListener(this);
        mRL2.setOnClickListener(this);
    }

    private void initView() {
        mRL1 = (RelativeLayout) findViewById(R.id.rl_1);
        mRL2 = (RelativeLayout) findViewById(R.id.rl_2);
        mIv1 = (ImageView) findViewById(R.id.iv_1);
        mIv2 = (ImageView) findViewById(R.id.iv_2);
        mTv1 = (TextView) findViewById(R.id.tv_1);
        mTv2 = (TextView) findViewById(R.id.tv_2);

        frags  =new ArrayList<>();
        frags.add(new SelectDoctorFragment());

        getSupportFragmentManager().beginTransaction().add(R.id.frame_layout,frags.get(0)).commit();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_1:
                mTv1.setTextColor(getResources().getColor(R.color.blue));
                mIv1.setVisibility(View.VISIBLE);
                mTv2.setTextColor(getResources().getColor(R.color.black));
                mIv2.setVisibility(View.INVISIBLE);
                break;
            case R.id.rl_2:
                mTv2.setTextColor(getResources().getColor(R.color.blue));
                mIv2.setVisibility(View.VISIBLE);
                mTv1.setTextColor(getResources().getColor(R.color.black));
                mIv1.setVisibility(View.INVISIBLE);
                break;
        }
    }
}
