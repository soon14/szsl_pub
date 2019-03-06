package com.bsoft.hospital.pub.suzhoumh.activity.cloud.dialog;

import android.content.Context;
import android.content.DialogInterface;

import com.bsoft.hospital.pub.suzhoumh.activity.adapter.cloud.listener.CloudWaitingListener;
import com.bsoft.hospital.pub.suzhoumh.activity.cloud.dialog.listener.CloudWaitingDialogListener;
import com.bsoft.hospital.pub.suzhoumh.model.cloud.CloudWaitingModel;
import com.bsoft.hospital.pub.suzhoumh.util.ToastUtils;

/**
 * @author :lizhengcao
 * @date :2019/3/6
 * E-mail:lizc@bsoft.com.cn
 * @类说明 等待对话框的工具类
 */
public class CloudWaitingDialogUtil {

    private Context mContext;
    private CloudWaitingModel data;
    private CloudWaitingDialogListener mListener;

    public CloudWaitingDialogUtil(Context context, CloudWaitingModel data, CloudWaitingDialogListener listener) {
        this.mContext = context;
        this.data = data;
        this.mListener = listener;
    }

    public void showCloudWaitingDialog() {
        new CloudWaitingDialog.Builder(mContext)
                .setVideoDoctorName(data.doctorName)
                .setAcceptButton(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mListener != null)
                            mListener.onWaitingAccept(data);
                        dialog.dismiss();
                    }
                })
                .setRefuceButton(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mListener != null)
                            mListener.onWaitingRefuse(data);
                        dialog.dismiss();
                    }
                })
                .setDoctorHead("")//暂时还没有，先不传
                .build()
                .show();
    }
}
