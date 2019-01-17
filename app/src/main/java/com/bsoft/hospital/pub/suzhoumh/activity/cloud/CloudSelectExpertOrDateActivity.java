package com.bsoft.hospital.pub.suzhoumh.activity.cloud;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.app.tanklib.view.BsoftActionBar;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.adapter.cloud.CloudExpertOrDateVPAdapter;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.cloud.fragment.CloudSelectDateFragment;
import com.bsoft.hospital.pub.suzhoumh.activity.cloud.fragment.CloudSelectExpertFragment;
import com.bsoft.hospital.pub.suzhoumh.model.cloud.SelectDeptModel;
import com.bsoft.hospital.pub.suzhoumh.util.KeyboardUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTouch;

/**
 * @author :lizhengcao
 * @date :2019/1/11
 * E-mail:lizc@bsoft.com.cn
 * @类说明 云诊室内专家医生进入的选专家和选日期
 */
public class CloudSelectExpertOrDateActivity extends BaseActivity {

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    private List<Fragment> frags;
    private String[] titles = {"选专家", "选日期"};
    private SelectDeptModel selectDept;
    private String cloudType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_expert_select_cloud);
        mUnbinder = ButterKnife.bind(this);
        findView();
        initData();
        setAdapter();
    }

    private void setAdapter() {
        CloudExpertOrDateVPAdapter cloudAdapter = new CloudExpertOrDateVPAdapter(getSupportFragmentManager(), frags, titles);
        mViewPager.setAdapter(cloudAdapter);
        mTabLayout.setupWithViewPager(mViewPager, false);
    }

    private void initData() {
        selectDept = (SelectDeptModel) getIntent().getSerializableExtra("selectDept");
        cloudType = getIntent().getStringExtra(Constants.CLOUD_TYPE);

        frags = new ArrayList<>();
        frags.add(CloudSelectExpertFragment.getInstance(selectDept, "", cloudType));
        frags.add(CloudSelectDateFragment.getInstance(selectDept, cloudType, null));
    }


    @Override
    public void findView() {
        findActionBar();
        actionBar.setTitle("苏州市立医院");
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
