package com.bsoft.hospital.pub.suzhoumh.activity.adapter.cloud;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.adapter.cloud.listener.CloudMedicalRecordPictureListener;
import com.bsoft.hospital.pub.suzhoumh.activity.cloud.CloudConditionDescriptionActivity;
import com.bsoft.hospital.pub.suzhoumh.view.SquaredImageView;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author :lizhengcao
 * @date :2019/3/1
 * E-mail:lizc@bsoft.com.cn
 * @类说明 病历图片上传适配器
 */
public class CloudMedicalRecordPictureAdapter extends RecyclerView.Adapter<CloudMedicalRecordPictureAdapter.MedicalRecordViewHodler> {

    private Context mContext;
    private List<String> mMatisseSelectPath;
    private CloudMedicalRecordPictureListener mListener;

    public CloudMedicalRecordPictureAdapter(Context context, List<String> matisseSelectPath, CloudMedicalRecordPictureListener listener) {
        this.mContext = context;
        this.mMatisseSelectPath = matisseSelectPath;
        this.mListener = listener;
    }

    @Override
    public MedicalRecordViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_picture_record_medical, parent, false);
        return new MedicalRecordViewHodler(view);
    }

    @Override
    public void onBindViewHolder(MedicalRecordViewHodler holder, final int position) {
        Glide.with(mContext)
                .load(position == mMatisseSelectPath.size() ? "" : mMatisseSelectPath.get(position))
                .asBitmap()
                .placeholder(R.drawable.add_pic)
                .into(holder.mSquaredImageView);

        holder.mIvDelete.setVisibility(position == mMatisseSelectPath.size() ? View.GONE : View.VISIBLE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMatisseSelectPath.size() == position) {
                    mListener.showAlumPic();
                }
            }
        });

        holder.mIvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.deletePicItem(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMatisseSelectPath.size() == CloudConditionDescriptionActivity.MAX_COUNT ?
                mMatisseSelectPath.size() : mMatisseSelectPath.size() + 1;
    }

    class MedicalRecordViewHodler extends RecyclerView.ViewHolder {

        @BindView(R.id.squared_image_view)
        SquaredImageView mSquaredImageView;
        @BindView(R.id.iv_delete)
        ImageView mIvDelete;

        public MedicalRecordViewHodler(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
