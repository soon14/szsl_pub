package com.bsoft.hospital.pub.suzhoumh.activity.app.physical;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.tanklib.view.BsoftActionBar;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;

/**
 * 健康体检
 * <p>
 * Author: 嘿嘿抛物线
 * Email: 497635745@qq.com
 * Date: 2016-8-17 12:07
 */
public class PhysicalActivity extends BaseActivity {

    private LinearLayout mIntroLayout;
    private LinearLayout mReportLayout;
    private LinearLayout mVipIntro, mTumourIntro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physical);

        findView();
        setClick();
    }

    private void setClick() {
        mIntroLayout.setOnClickListener(mCheckListener);

        mVipIntro.setOnClickListener(mVipListener);
        mTumourIntro.setOnClickListener(mTumourListener);


        mReportLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loginUser != null && loginUser.idcard != null) {
                    Intent intent = new Intent(PhysicalActivity.this, PhysicalListActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(baseContext, "请先完善个人信息", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 体检中心
     */
    private View.OnClickListener mCheckListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            enterNextH5(CenterIntroActivity.PHYSICAL_TYPE);
        }
    };
    /**
     * 特检中心
     */
    private View.OnClickListener mVipListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            enterNextH5(CenterIntroActivity.VIP_TYPE);
        }
    };
    /**
     * 肿瘤筛查
     */
    private View.OnClickListener mTumourListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            enterNextH5(CenterIntroActivity.TUMOR_TYPE);
        }
    };


    public void enterNextH5(int type) {
        Intent intent = new Intent(PhysicalActivity.this, CenterIntroActivity.class);
        intent.putExtra("type", type);
        startActivity(intent);
    }

    @Override
    public void findView() {
        findActionBar();
        actionBar.setTitle("健康体检");
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

        mIntroLayout = (LinearLayout) findViewById(R.id.ll_intro);
        mReportLayout = (LinearLayout) findViewById(R.id.ll_report);
        mVipIntro = (LinearLayout) findViewById(R.id.ll_vip_intro);
        mTumourIntro = (LinearLayout) findViewById(R.id.ll_tumour_intro);
    }


}
