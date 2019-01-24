package com.bsoft.hospital.pub.suzhoumh.activity.cloud.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.tanklib.bitmap.view.RoundImageView;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.fragment.index.BaseFragment;
import com.bsoft.hospital.pub.suzhoumh.model.cloud.CloudSelectExpertModel;
import com.bsoft.hospital.pub.suzhoumh.model.cloud.SelectDeptModel;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * @author :lizhengcao
 * @date :2019/1/16
 * E-mail:lizc@bsoft.com.cn
 * @类说明 云诊室专家简介
 */
public class CloudIntroduceFragment extends BaseFragment {

    @BindView(R.id.riv_doctor_info_head)
    RoundImageView rivDoctorInfoHead;
    @BindView(R.id.tv_appoint_doctor_name)
    TextView tvAppointDoctorName;
    @BindView(R.id.tv_doctor_info_profession)
    TextView tvDoctorInfoProfession;
    @BindView(R.id.tv_doctor_info_dept)
    TextView tvDocInfoDept;
    @BindView(R.id.tv_doctor_info_intro)
    TextView tvDoctorInfoIntro;
    private CloudSelectExpertModel expert;
    private SelectDeptModel selectDept;

    public static CloudIntroduceFragment getInstance(CloudSelectExpertModel expert, SelectDeptModel selectDept) {
        CloudIntroduceFragment fragment = new CloudIntroduceFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("expert", expert);
        bundle.putSerializable("selectDept", selectDept);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_introduce_cloud, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnbinder = ButterKnife.bind(this, view);
        initData();
    }

    private void initData() {
        Bundle bundle = getArguments();
        expert = (CloudSelectExpertModel) bundle.getSerializable("expert");
        selectDept = (SelectDeptModel) bundle.getSerializable("selectDept");
        assert expert != null;
        tvAppointDoctorName.setText(expert.ygxm);
        tvDoctorInfoProfession.setText(expert.ygjb);
        tvDocInfoDept.setText(selectDept.ksmc);
        tvDoctorInfoIntro.setText(expert.ysjj);

    }

    @Override
    public void startHint() {

    }

    @Override
    public void endHint() {

    }
}
