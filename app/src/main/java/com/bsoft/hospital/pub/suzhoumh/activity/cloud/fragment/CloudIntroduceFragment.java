package com.bsoft.hospital.pub.suzhoumh.activity.cloud.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.fragment.index.BaseFragment;


/**
 * @author :lizhengcao
 * @date :2019/1/16
 * E-mail:lizc@bsoft.com.cn
 * @类说明 云诊室专家简介
 */
public class CloudIntroduceFragment extends BaseFragment {


    public static CloudIntroduceFragment getInstance() {
        CloudIntroduceFragment fragment = new CloudIntroduceFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_introduce_cloud, container, false);
    }

    @Override
    public void startHint() {

    }

    @Override
    public void endHint() {

    }
}
