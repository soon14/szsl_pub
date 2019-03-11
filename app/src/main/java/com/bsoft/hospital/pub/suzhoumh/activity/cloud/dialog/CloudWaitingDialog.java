package com.bsoft.hospital.pub.suzhoumh.activity.cloud.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.tanklib.util.DensityUtil;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bumptech.glide.Glide;
import com.qiangxi.checkupdatelibrary.utils.ScreenUtil;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author :lizhengcao
 * @date :2019/3/6
 * E-mail:lizc@bsoft.com.cn
 * @类说明 候诊等待对话框
 */
public class CloudWaitingDialog extends Dialog {

    public CloudWaitingDialog(@NonNull Context context) {
        super(context);
    }

    public CloudWaitingDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected CloudWaitingDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    //用Builder模式来构造Dialog
    public static class Builder {
        private Context mContext;
        private String headUrl;
        private String doctor;
        private OnClickListener refuseOnclickListener;
        private OnClickListener acceptOnclickListener;

        public Builder(Context mContext) {
            this.mContext = mContext;
        }

        public Builder setDoctorHead(String headUrl) {
            this.headUrl = headUrl;
            return this;
        }

        public Builder setVideoDoctorName(String doctor) {
            this.doctor = doctor;
            return this;
        }

        public Builder setRefuseButton(OnClickListener refuseOnclickListener) {
            this.refuseOnclickListener = refuseOnclickListener;
            return this;
        }

        public Builder setAcceptButton(OnClickListener acceptOnclickListener) {
            this.acceptOnclickListener = acceptOnclickListener;
            return this;
        }


        public CloudWaitingDialog build() {
            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final CloudWaitingDialog mCloudWaitingDialog = new CloudWaitingDialog(mContext, R.style.cloudWaitingDialog);//默认调用带style的构造
            mCloudWaitingDialog.setCanceledOnTouchOutside(false);//默认点击布局外不能取消dialog
            View view = mInflater.inflate(R.layout.dialog_waiting_cloud, null);


            ViewGroup.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    DensityUtil.dip2px(mContext, 300), LinearLayout.LayoutParams.WRAP_CONTENT);
            mCloudWaitingDialog.setContentView(view, layoutParams);

            CircleImageView civHead = view.findViewById(R.id.civ_head);
            TextView videoDoctor = view.findViewById(R.id.tv_video_doctor);
            Button btnRefuse = view.findViewById(R.id.btn_refuse);
            Button btnAccept = view.findViewById(R.id.btn_accept);


            Glide.with(mContext)
                    .load(headUrl)
                    .asBitmap()
                    .placeholder(R.mipmap.avtar)
                    .into(civHead);

            videoDoctor.setText(doctor);
            if (refuseOnclickListener != null) {
                //拒绝按钮的监听
                btnRefuse.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        refuseOnclickListener.onClick(mCloudWaitingDialog, BUTTON_NEGATIVE);
                    }
                });
            }

            if (acceptOnclickListener != null) {
                //接受按钮的监听
                btnAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        acceptOnclickListener.onClick(mCloudWaitingDialog, BUTTON_POSITIVE);
                    }
                });
            }
            return mCloudWaitingDialog;
        }
    }


}
