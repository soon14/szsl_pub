package com.bsoft.hospital.pub.suzhoumh.activity.my.note;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.app.tanklib.view.BsoftActionBar;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.adapter.my.ChargeDetailGridAdapter;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.model.my.ChargeDetailVo;
import com.bsoft.hospital.pub.suzhoumh.model.my.ChargeVo;
import com.bsoft.hospital.pub.suzhoumh.view.UnscrollableGridView;

import java.util.ArrayList;

/**
 * Author: 嘿嘿抛物线
 * Email: 497635745@qq.com
 * Date: 2016-9-9 22:44
 */
public class ChargeDetailActivity extends BaseActivity {

    private TextView fphm;
    private TextView rylb;
    private UnscrollableGridView gridView;
    private TextView fpsm;

    private String fpsmString;
    private ChargeVo chargeVo;

    private ChargeDetailGridAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge_detail);
        findView();
        initData();
        initView();
    }

    private void initView() {
        fphm.setText("发票号：" + chargeVo.fphm);
        rylb.setText("人员类别：" + chargeVo.rylb);
        fpsm.setText(fpsmString);
    }

    private void initData() {
        chargeVo = (ChargeVo) getIntent().getSerializableExtra("vo");

        fpsmString = chargeVo.fpsm.replaceAll(",", "\r\n");

        adapter = new ChargeDetailGridAdapter(this);
        adapter.setData((ArrayList<ChargeDetailVo>) chargeVo.fpxq);
        gridView.setAdapter(adapter);
    }

    @Override
    public void findView() {
        findActionBar();
        actionBar.setTitle("收费详情");
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

        fphm = (TextView) findViewById(R.id.tv_fphm);
        rylb = (TextView) findViewById(R.id.tv_rylb);
        gridView = (UnscrollableGridView) findViewById(R.id.gv_fpxq);
        fpsm = (TextView) findViewById(R.id.tv_fpsm);
    }

}
