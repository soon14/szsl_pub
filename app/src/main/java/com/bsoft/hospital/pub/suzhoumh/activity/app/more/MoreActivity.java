package com.bsoft.hospital.pub.suzhoumh.activity.app.more;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.tanklib.view.BsoftActionBar;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.app.fee.OneDayListActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.app.pay.MyInHospitalActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;

/**
 * 更多
 * Created by Administrator on 2016/5/17.
 */
public class MoreActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout ll_home_1;
    private LinearLayout ll_home_2;
    private LinearLayout ll_home_3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_more);
        findView();
        initData();
    }

    @Override
    public void findView() {
        findActionBar();
        actionBar.setTitle("住院业务");
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

        ll_home_1 = (LinearLayout)findViewById(R.id.ll_home_1);
        ll_home_2 = (LinearLayout)findViewById(R.id.ll_home_2);
        ll_home_3 = (LinearLayout)findViewById(R.id.ll_home_3);


        ll_home_1.setOnClickListener(this);
        ll_home_2.setOnClickListener(this);
        ll_home_3.setOnClickListener(this);
    }

    private void initData()
    {

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch(v.getId())
        {
            case R.id.ll_home_1://住院预缴金
                intent = new Intent(baseContext, MyInHospitalActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_home_2://出院结算
                Toast.makeText(baseContext, "此功能正在建设中,敬请期待", Toast.LENGTH_LONG).show();
                break;
            case R.id.ll_home_3://一日清单
//                Toast.makeText(baseContext, "此功能正在建设中,敬请期待", Toast.LENGTH_LONG).show();
                intent = new Intent(baseContext, OneDayListActivity.class);
                startActivity(intent);
                break;
        }
    }

}
