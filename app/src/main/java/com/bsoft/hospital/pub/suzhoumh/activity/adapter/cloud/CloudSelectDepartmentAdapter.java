package com.bsoft.hospital.pub.suzhoumh.activity.adapter.cloud;

import android.view.View;

import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.adapter.cloud.listener.CloudSelectDepartmentListener;
import com.bsoft.hospital.pub.suzhoumh.model.cloud.SelectDeptModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;


/**
 * @author :lizhengcao
 * @date :2019/1/7
 * E-mail:lizc@bsoft.com.cn
 * @类说明 云诊室选择科室适配器
 */
public class CloudSelectDepartmentAdapter extends BaseQuickAdapter<SelectDeptModel, BaseViewHolder> {

    private int[] bgDept = new int[]{
            R.drawable.bg_dept_text_1,
            R.drawable.bg_dept_text_2,
            R.drawable.bg_dept_text_3};
    private CloudSelectDepartmentListener mListener;

    public CloudSelectDepartmentAdapter(CloudSelectDepartmentListener listener) {
        super(R.layout.adapter_department_select_cloud);
        this.mListener = listener;
    }

    @Override
    protected void convert(BaseViewHolder holder, final SelectDeptModel item) {
        holder.setText(R.id.tv_department_name, item.ksmc);
        int pos = holder.getAdapterPosition();
        holder.setBackgroundRes(R.id.tv_bg, bgDept[pos % 3]);
        holder.setText(R.id.tv_bg, item.ksmc.substring(0, 1));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.onCouldSelectDepartmentListener(item);
            }
        });
    }
}
