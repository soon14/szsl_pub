package com.bsoft.hospital.pub.suzhoumh.activity.adapter.cloud;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.adapter.cloud.listener.CloudCancelAppointmentListener;
import com.bsoft.hospital.pub.suzhoumh.model.cloud.CloudAppointmentRecordModel;
import com.bsoft.hospital.pub.suzhoumh.util.DateUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author :lizhengcao
 * @date :2019/1/21
 * E-mail:lizc@bsoft.com.cn
 * tv_yyrq tv_del tv_state
 * @类说明 云诊室预约记录适配器
 */
public class CloudAppointmentRecordAdapter extends BaseQuickAdapter<CloudAppointmentRecordModel, BaseViewHolder> {

    @BindView(R.id.ll_doctor)
    LinearLayout llDoctor;
    @BindView(R.id.tv_doctor)
    TextView tvDoctor;
    @BindView(R.id.tv_dept)
    TextView tvDept;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_yyrq)
    TextView yyrq;
    @BindView(R.id.tv_del)
    TextView tvDel;
    @BindView(R.id.tv_state)
    TextView tvState;
    @BindView(R.id.tv_djrq)
    TextView tvDjrq;
    private CloudCancelAppointmentListener mListener;

    public CloudAppointmentRecordAdapter(CloudCancelAppointmentListener listener) {
        super(R.layout.adapter_record_appointment_cloud);
        this.mListener = listener;
    }

    @Override
    protected void convert(BaseViewHolder helper, final CloudAppointmentRecordModel record) {
        ButterKnife.bind(this, helper.itemView);
        tvDept.setText(record.ksmc);
        tvName.setText(record.brxm);
        yyrq.setText(record.yyrq);
        tvDjrq.setText(DateUtil.dateFormate(record.djsj, "yyyy-MM-dd"));

        if (TextUtils.isEmpty(record.ysid)) {
            llDoctor.setVisibility(View.GONE);
        } else {
            llDoctor.setVisibility(View.VISIBLE);
            tvDoctor.setText(record.ysxm);
        }

        switch (record.yyzt) {
            case "未取号":
                //预约成功
                tvState.setText("预约成功");
                if (DateUtil.isToday(record.yyrq))
                    tvDel.setVisibility(View.GONE);
                else
                    tvDel.setVisibility(View.VISIBLE);
                break;
            case "已退号":
                tvState.setText("已取消预约");
                tvDel.setVisibility(View.GONE);
                break;
            case "已取号":
                tvState.setText("已取号");
                tvDel.setVisibility(View.GONE);
                break;
        }

        final int pos = helper.getAdapterPosition();
        tvDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //取消预约
                if (mListener != null)
                    mListener.onCloudCancelAppointmentListener(record, pos);
            }
        });

    }
}
