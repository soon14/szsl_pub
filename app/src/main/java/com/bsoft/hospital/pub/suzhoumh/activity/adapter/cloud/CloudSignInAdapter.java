package com.bsoft.hospital.pub.suzhoumh.activity.adapter.cloud;

import android.support.annotation.Nullable;
import android.support.v7.view.menu.MenuView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.adapter.cloud.listener.CloudSignInListener;
import com.bsoft.hospital.pub.suzhoumh.model.app.appoint.AppointInfoVo;
import com.bsoft.hospital.pub.suzhoumh.model.cloud.CloudAppointmentRecordModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author :lizhengcao
 * @date :2019/2/26
 * E-mail:lizc@bsoft.com.cn
 * @类说明 互联网诊室签到取号适配器
 */
public class CloudSignInAdapter extends BaseQuickAdapter<CloudAppointmentRecordModel, BaseViewHolder> {

    @BindView(R.id.tv_dept)
    TextView tvDept;
    @BindView(R.id.tv_yyrq)
    TextView tvYyrq;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_doctor)
    TextView tvDoctor;
    @BindView(R.id.tv_sjd)
    TextView tvSjd;
    @BindView(R.id.tv_sign)
    TextView tvSign;
    @BindView(R.id.tv_msg)
    TextView tvMsg;
    @BindView(R.id.ll_floor_address)
    LinearLayout llFloorAddress;
    @BindView(R.id.tv_floor_address)
    TextView tvFloorAddress;
    @BindView(R.id.ll_doctor)
    LinearLayout llDoctor;

    private String name;
    private CloudSignInListener listener;

    public CloudSignInAdapter(String name, CloudSignInListener listener) {
        super(R.layout.adapter_in_sign_cloud);
        this.name = name;
        this.listener = listener;
    }

    @Override
    protected void convert(BaseViewHolder helper, final CloudAppointmentRecordModel item) {
        ButterKnife.bind(this, helper.itemView);
        tvDept.setText(item.ksmc);
        tvYyrq.setText(item.yyrq);
        tvName.setText(name);
        tvDoctor.setText(item.ysxm);
        tvSjd.setText(item.sjd);
        if (TextUtils.equals(item.yyzt, "已取号")) {
            tvSign.setVisibility(View.GONE);
            tvMsg.setVisibility(View.VISIBLE);
            llFloorAddress.setVisibility(View.VISIBLE);
            tvFloorAddress.setText(item.jzwz);
        } else if (TextUtils.equals(item.yyzt, "未取号")) {
            tvSign.setVisibility(View.VISIBLE);
            tvMsg.setVisibility(View.GONE);
            llFloorAddress.setVisibility(View.GONE);
        } else {
            tvSign.setVisibility(View.GONE);
            tvMsg.setVisibility(View.GONE);
            llFloorAddress.setVisibility(View.GONE);
        }
        llDoctor.setVisibility(("0".equals(item.yylx)) ? View.GONE : View.VISIBLE);

        tvSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onCloudSignListener(item);
            }
        });

        tvMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onCloudRegistrationFormListener(item);
            }
        });
    }
}
