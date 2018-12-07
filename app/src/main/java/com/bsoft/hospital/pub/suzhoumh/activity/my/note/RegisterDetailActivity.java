package com.bsoft.hospital.pub.suzhoumh.activity.my.note;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.app.tanklib.view.BsoftActionBar;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.model.my.RegisterVo;

/**
 * Author: 嘿嘿抛物线
 * Email: 497635745@qq.com
 * Date: 2016-9-9 22:43
 */
public class RegisterDetailActivity extends BaseActivity {

    private TextView ghsj;
    private TextView rylb;
    private TextView jzks;
    private TextView jzys;
    private TextView zlf;
    private TextView zhzf;
    private TextView xjzf;
    private TextView grzfei;
    private TextView grzfu;

    private RegisterVo registerVo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_detail);
        findView();
        initData();
        initView();
    }

    private void initView() {

        ghsj.setText(registerVo.ghsj);
        rylb.setText(registerVo.rylb);
        jzks.setText(registerVo.jzks);
        jzys.setText(registerVo.jzys);
        zlf.setText(registerVo.zlf);
        zhzf.setText(registerVo.zhzf);
        xjzf.setText(registerVo.xjzf);
        grzfei.setText(registerVo.grzfei);
        grzfu.setText(registerVo.grzfu);
    }

    private void initData() {
        registerVo = (RegisterVo) getIntent().getSerializableExtra("vo");
    }

    @Override
    public void findView() {
        findActionBar();
        actionBar.setTitle("挂号详情");
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

        ghsj = (TextView) findViewById(R.id.tv_ghsj);
        rylb = (TextView) findViewById(R.id.tv_rylb);
        jzks = (TextView) findViewById(R.id.tv_jzks);
        jzys = (TextView) findViewById(R.id.tv_jzys);
        zlf = (TextView) findViewById(R.id.tv_zlf);
        zhzf = (TextView) findViewById(R.id.tv_zhzf);
        xjzf = (TextView) findViewById(R.id.tv_xjzf);
        grzfei = (TextView) findViewById(R.id.tv_grzfei);
        grzfu = (TextView) findViewById(R.id.tv_grzfu);
    }
}