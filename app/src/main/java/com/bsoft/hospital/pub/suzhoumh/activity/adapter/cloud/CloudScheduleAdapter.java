package com.bsoft.hospital.pub.suzhoumh.activity.adapter.cloud;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.adapter.cloud.listener.CloudScheduleListener;
import com.bsoft.hospital.pub.suzhoumh.model.cloud.CloudScheduleModel;
import com.bsoft.hospital.pub.suzhoumh.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * @author :lizhengcao
 * @date :2019/1/17
 * E-mail:lizc@bsoft.com.cn
 * @类说明 排班表的适配器
 */
public class CloudScheduleAdapter extends BaseQuickAdapter<CloudScheduleModel, BaseViewHolder> {
    @BindView(R.id.tv_time_period)
    TextView tvTimePeriod;
    @BindView(R.id.tv_total_amount)
    TextView tvTotalAmount;
    @BindView(R.id.tv_remind_amount)
    TextView tvRemindAmount;
    @BindView(R.id.tv_state)
    TextView tvState;
    @BindView(R.id.btn_appoint)
    Button btnAppoint;
    private static final String totalAmount = "1";
    private static final String state = "正常";
    private CloudScheduleListener listener;

    public CloudScheduleAdapter(CloudScheduleListener listener) {
        super(R.layout.adapter_schedule_cloud);
        this.listener = listener;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void convert(BaseViewHolder helper, final CloudScheduleModel item) {
        ButterKnife.bind(this, helper.itemView);
        tvTimePeriod.setText(item.qssj + "-" + item.jzsj);
        tvTotalAmount.setText(totalAmount);
        tvRemindAmount.setText(item.syhy);
        tvState.setText(state);
        if ("0".equals(item.syhy)) {
            btnAppoint.setBackgroundResource(R.drawable.bg_single_gray);
            btnAppoint.setEnabled(false);
            btnAppoint.setText("已满");
        } else {
            btnAppoint.setEnabled(true);
            btnAppoint.setBackgroundResource(R.drawable.bigbut_blue);
            btnAppoint.setText("预约");
        }
        //预约
        btnAppoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onCloudScheduleAppointListener(item);
            }
        });
    }
}
