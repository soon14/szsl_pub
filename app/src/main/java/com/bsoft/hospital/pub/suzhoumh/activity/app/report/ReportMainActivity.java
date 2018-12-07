package com.bsoft.hospital.pub.suzhoumh.activity.app.report;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.app.tanklib.view.BsoftActionBar.Action;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.model.my.MyFamilyVo;
import com.bsoft.hospital.pub.suzhoumh.model.my.PersonVo;

/**
 * 报告主界面
 *
 * @author Administrator
 */
public class ReportMainActivity extends BaseActivity implements OnClickListener {

    private RelativeLayout rl_jc;
    private RelativeLayout rl_jy;

    private MyFamilyVo familyVo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.report_main);
        findView();
        initData();
    }

    @Override
    public void findView() {
        findActionBar();
        actionBar.setTitle("报告查询");
        actionBar.setBackAction(new Action() {

            @Override
            public int getDrawable() {
                return R.drawable.btn_back;
            }

            @Override
            public void performAction(View view) {
                finish();
            }

        });

        rl_jc = (RelativeLayout) findViewById(R.id.rl_jc);
        rl_jy = (RelativeLayout) findViewById(R.id.rl_jy);
    }

    private void initData() {
        rl_jc.setOnClickListener(this);
        rl_jy.setOnClickListener(this);

        familyVo = (MyFamilyVo) getIntent().getSerializableExtra("familyVo");
        if (familyVo != null) {

            personVo.idcard = familyVo.idcard;
            personVo.type = PersonVo.PersonType.FAMILY;
        } else {
            personVo.idcard = loginUser.idcard;
            personVo.type = PersonVo.PersonType.SELF;
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.rl_jc://检查报告
                intent = new Intent(ReportMainActivity.this, RisReportActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_jy://检验报包
                intent = new Intent(ReportMainActivity.this, LisReportActivity.class);
                startActivity(intent);
                break;
        }
    }
}
