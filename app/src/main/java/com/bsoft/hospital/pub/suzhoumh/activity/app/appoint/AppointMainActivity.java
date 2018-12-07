package com.bsoft.hospital.pub.suzhoumh.activity.app.appoint;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.app.tanklib.view.BsoftActionBar.Action;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.activity.appoint.SelectDoctorOrDateActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.model.my.MyFamilyVo;

/**
 * 预约主界面
 *
 * @author Administrator
 */
public class AppointMainActivity extends BaseActivity implements OnClickListener {


    private RelativeLayout rl_pt;
    private RelativeLayout rl_zj;
    private RelativeLayout mRLZK;

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.ACTION_APPOINT_CLOSE)) {
                finish();
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appoint_main);
        findView();

        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.ACTION_APPOINT_CLOSE);
        this.registerReceiver(receiver, filter);
    }

    @Override
    public void findView() {
        findActionBar();
        actionBar.setTitle("预约挂号");
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
        rl_pt = (RelativeLayout) findViewById(R.id.rl_pt);
        rl_zj = (RelativeLayout) findViewById(R.id.rl_zj);
        mRLZK = (RelativeLayout) findViewById(R.id.rl_zk);

        rl_pt.setOnClickListener(this);
        rl_zj.setOnClickListener(this);
        mRLZK.setOnClickListener(this);

        MyFamilyVo familyVo = (MyFamilyVo) getIntent().getSerializableExtra("familyVo");
        if (familyVo != null) {
            personVo.idcard = familyVo.idcard;
            personVo.realname = familyVo.realname;
            personVo.sexcode = familyVo.sexcode;
            personVo.nature = familyVo.nature;
            personVo.mobile = familyVo.mobile;
            //personVo.card = familyVo.accountcards.get(0).cardnum;
        } else {
            personVo.idcard = loginUser.idcard;
            personVo.realname = loginUser.realname;
            personVo.sexcode = loginUser.sexcode;
            personVo.nature = loginUser.nature;
            personVo.mobile = loginUser.mobile;
            //personVo.card = loginUser.cards.get(0).cardNum;
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.rl_zj:
                intent = new Intent(AppointMainActivity.this, AppointSelectDeptActivity.class);
                intent.putExtra("type", 1);//专家
                startActivity(intent);
                break;
            case R.id.rl_pt:
                intent = new Intent(AppointMainActivity.this, AppointSelectDeptActivity.class);
                intent.putExtra("type", 2);//普通
                startActivity(intent);
                break;
            case R.id.rl_zk:  //专科
                intent = new Intent(AppointMainActivity.this, AppointSelectDeptActivity.class);
                intent.putExtra("type", 3);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            this.unregisterReceiver(receiver);
        }
    }


}
