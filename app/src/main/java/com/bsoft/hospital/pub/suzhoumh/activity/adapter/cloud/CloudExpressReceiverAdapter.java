package com.bsoft.hospital.pub.suzhoumh.activity.adapter.cloud;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.adapter.cloud.listener.CloudExpressReceiverListener;
import com.bsoft.hospital.pub.suzhoumh.model.CloudExpressReceiverModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author :lizhengcao
 * @date :2019/3/13
 * E-mail:lizc@bsoft.com.cn
 * @类说明 收件人地址适配器
 */
public class CloudExpressReceiverAdapter extends BaseQuickAdapter<CloudExpressReceiverModel, BaseViewHolder> {
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_default)
    TextView tvDefault;
    @BindView(R.id.tv_mobile)
    TextView tvMobile;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_edit)
    TextView tvEdit;
    @BindView(R.id.iv_select)
    ImageView ivSelect;
    @BindView(R.id.fl_operation)
    FrameLayout flOperation;
    @BindView(R.id.ll_info)
    LinearLayout llInfo;

    private CloudExpressReceiverListener listener;

    public CloudExpressReceiverAdapter(CloudExpressReceiverListener listener) {
        super(R.layout.adapter_receiver_express_cloud);
        this.listener = listener;
    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, final CloudExpressReceiverModel cloudExpressReceiverModel) {
        ButterKnife.bind(this, baseViewHolder.itemView);
        tvName.setText(cloudExpressReceiverModel.collectName);
        /*0-常规1-默认*/
        tvDefault.setVisibility("1".equals(cloudExpressReceiverModel.tolerate) ? View.VISIBLE : View.INVISIBLE);
        tvMobile.setText(cloudExpressReceiverModel.collectPhone);
        tvAddress.setText(String.format("%s%s%s", cloudExpressReceiverModel.collectProvincial, cloudExpressReceiverModel.collectCity, cloudExpressReceiverModel.collectDetailAdress));
        if (cloudExpressReceiverModel.isEdit) {
            tvEdit.setVisibility(View.GONE);
            ivSelect.setVisibility(View.VISIBLE);
            ivSelect.setSelected(cloudExpressReceiverModel.select);
        } else {
            tvEdit.setVisibility(View.VISIBLE);
            ivSelect.setVisibility(View.GONE);
        }


        flOperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cloudExpressReceiverModel.isEdit) {
                    cloudExpressReceiverModel.select = !cloudExpressReceiverModel.select;
                    notifyDataSetChanged();
                } else {
                    if (listener != null)
                        listener.onEdit(cloudExpressReceiverModel);
                }
            }
        });

        llInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null && "0".equals(cloudExpressReceiverModel.tolerate))
                    listener.onDefaultAddress(cloudExpressReceiverModel);
            }
        });
    }
}
