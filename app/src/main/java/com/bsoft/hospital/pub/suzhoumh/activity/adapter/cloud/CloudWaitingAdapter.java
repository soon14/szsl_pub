package com.bsoft.hospital.pub.suzhoumh.activity.adapter.cloud;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.adapter.cloud.listener.CloudWaitingListener;
import com.bsoft.hospital.pub.suzhoumh.model.cloud.CloudWaitingModel;
import com.bsoft.hospital.pub.suzhoumh.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author :lizhengcao
 * @date :2019/3/5
 * E-mail:lizc@bsoft.com.cn
 * @类说明 候诊列表适配器
 */
public class CloudWaitingAdapter extends BaseQuickAdapter<CloudWaitingModel, BaseViewHolder> {

    @BindView(R.id.tv_dept)
    TextView tvDept;
    @BindView(R.id.tv_doctor)
    TextView tvDoctor;
    @BindView(R.id.tv_number)
    TextView tvNumber;
    @BindView(R.id.tv_myself_number)
    TextView tvMySelfNumber;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.btn_refresh)
    Button btnRefresh;

    private CloudWaitingListener mListener;

    public CloudWaitingAdapter(CloudWaitingListener listener) {
        super(R.layout.adapter_waiting_cloud);
        this.mListener = listener;
    }

    @Override
    protected void convert(BaseViewHolder helper, CloudWaitingModel item) {
        ButterKnife.bind(this, helper.itemView);
        tvDept.setText(String.format("%s(%s)", item.deptName, item.visitAddress));
        tvDoctor.setText(item.doctorName);
        int pos = helper.getAdapterPosition();
        if (pos == 0) {
            item.currentNumber = "1";
        }
        tvNumber.setText(item.currentNumber);

        tvMySelfNumber.setText(item.visitNumber);
        tvDate.setText(item.regTime);

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.onCloudWaitingRefresh();
            }
        });

        if (item.currentNumber.equals(item.visitNumber)) {
            if (mListener != null) {
                mListener.onCloudWaitingShowDialog(item);
            }
        }

    }
}
