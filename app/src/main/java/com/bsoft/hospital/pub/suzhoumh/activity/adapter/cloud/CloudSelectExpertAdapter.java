package com.bsoft.hospital.pub.suzhoumh.activity.adapter.cloud;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.app.tanklib.bitmap.view.RoundImageView;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.adapter.cloud.listener.CloudSelectExpertListener;
import com.bsoft.hospital.pub.suzhoumh.model.cloud.CloudSelectExpertModel;
import com.bsoft.hospital.pub.suzhoumh.util.IDCard;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;


import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author :lizhengcao
 * @date :2019/1/14
 * E-mail:lizc@bsoft.com.cn
 * @类说明 云诊室选择专家适配器
 */
public class CloudSelectExpertAdapter extends BaseQuickAdapter<CloudSelectExpertModel, BaseViewHolder> {

    @BindView(R.id.iv_head)
    RoundImageView ivHead;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_profession)
    TextView tvProfession;
    @BindView(R.id.tv_zjfy)
    TextView tvZjfy;
    @BindView(R.id.btn_see_doctinfo)
    Button btnSeeDocInfo;
    @BindView(R.id.btn_appoint)
    Button btnAppoint;
    private CloudSelectExpertListener listener;
    private static final int EXPERT_SCHEDULE = 1; //排班
    private static final int EXPERT_INTRODUCE = 0;  //简介

    public CloudSelectExpertAdapter(CloudSelectExpertListener listener) {
        super(R.layout.adapter_expert_select_cloud);
        this.listener = listener;
    }

    @Override
    protected void convert(BaseViewHolder helper, final CloudSelectExpertModel item) {
        ButterKnife.bind(this, helper.itemView);
        tvName.setText(item.ygxm);
        tvZjfy.setText(getZjfy(item.zjfy));
        tvProfession.setText(item.ygjb);

        //预约
        btnAppoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onCloudSelectExpertAppointmentORIntroduceListener(item, EXPERT_SCHEDULE);
            }
        });
        //简介
        btnSeeDocInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onCloudSelectExpertAppointmentORIntroduceListener(item, EXPERT_INTRODUCE);
            }
        });
    }

    private String getZjfy(String zjfy) {
        if (TextUtils.isEmpty(zjfy))
            return "";
        else
            return "专家费\u0020" + zjfy;
    }
}
