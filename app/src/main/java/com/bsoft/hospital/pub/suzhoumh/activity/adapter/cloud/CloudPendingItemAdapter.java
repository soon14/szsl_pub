package com.bsoft.hospital.pub.suzhoumh.activity.adapter.cloud;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.adapter.cloud.listener.CloudPendingItemListener;
import com.bsoft.hospital.pub.suzhoumh.model.FeeVo;
import com.bsoft.hospital.pub.suzhoumh.model.cloud.CloudPendingItemModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * @author :lizhengcao
 * @date :2019/3/7
 * E-mail:lizc@bsoft.com.cn
 * @类说明 互联网待支付项目适配器
 */
public class CloudPendingItemAdapter extends BaseQuickAdapter<CloudPendingItemModel, BaseViewHolder> {

    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_mzlx)
    TextView tvMzlx;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.iv_select)
    ImageView ivSelect;

    private CloudPendingItemListener mListener;

    public CloudPendingItemAdapter(CloudPendingItemListener listener) {
        super(R.layout.adapter_item_pending_cloud);
        this.mListener = listener;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, final CloudPendingItemModel data) {
        ButterKnife.bind(this, baseViewHolder.itemView);

        tvName.setText(String.format("%s%s", data.xmlx, TextUtils.isEmpty(data.xzmc()) ? "" : "(" + data.xzmc() + ")"));
        tvMzlx.setText(String.format("%s(%s)", data.ksmc, TextUtils.isEmpty(data.ysxm) ? "未知医生" : data.ysxm));
        DecimalFormat df = new DecimalFormat("0.00");
        String hjje = df.format(Double.parseDouble(data.hjje));
        tvMoney.setText(String.format("¥%s", hjje));
        tvDate.setText(data.fyrq);
        if (TextUtils.equals(data.sfgh, "1")) {
            ivSelect.setImageResource(R.drawable.icon_list_select);
        } else {
            ivSelect.setImageResource(data.isSelected ? R.drawable.icon_list_select : R.drawable.icon_list_unselect);
        }
        ivSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.onSelect(data);
            }
        });

        baseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null)
                    mListener.onSelect(data);
            }
        });

    }
}
