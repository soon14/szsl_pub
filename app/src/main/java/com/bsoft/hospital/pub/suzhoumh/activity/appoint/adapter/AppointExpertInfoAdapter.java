package com.bsoft.hospital.pub.suzhoumh.activity.appoint.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.app.tanklib.bitmap.view.RoundImageView;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.app.appoint.AppointDoctorInfoActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.app.appoint.AppointSelectDateActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.appoint.AppointTimeActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.appoint.SelectProfileOrAppointActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.appoint.model.RoundImageUtils;
import com.bsoft.hospital.pub.suzhoumh.model.app.appoint.AppointDeptVo;
import com.bsoft.hospital.pub.suzhoumh.model.app.appoint.AppointDoctorVo;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

/**
 * @author :lizhengcao
 * @date :2017/3/8
 * E-mail:lizc@bsoft.com.cn
 * @类说明
 */

public class AppointExpertInfoAdapter extends BaseAdapter {

    private List<AppointDoctorVo> mDoctorList;
    private Context mContext;
    private AppointDeptVo dept;
    private String zblb;
    private String yysj;
    public ImageLoader imageLoader = ImageLoader.getInstance();
    private Activity mActivity;

    public AppointExpertInfoAdapter(Context context, List<AppointDoctorVo> mDoctorList,AppointDeptVo dept,String zblb,String yysj,Activity activity) {
        this.mContext = context;
        this.mDoctorList = mDoctorList;
        this.dept = dept;
        this.zblb = zblb;
        this.yysj = yysj;
        this.mActivity = activity;
        imageLoader.init(ImageLoaderConfiguration.createDefault(activity));
    }

    @Override
    public int getCount() {
        return mDoctorList == null ? 0 : mDoctorList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDoctorList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView ==null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.select_doctor_item_appoint,parent,false);
            holder = new ViewHolder(convertView);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        AppointDoctorVo vo = mDoctorList.get(position);
        holder.tvName.setText(vo.ygxm);
        holder.tvProfession.setText(vo.ygjb);
        holder.tvZjfy.setText("专家费 "+vo.zjfy);
        holder.btnSeeDocinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(mContext, AppointDoctorInfoActivity.class);
                Intent intent = new Intent(mContext, SelectProfileOrAppointActivity.class);
                intent.putExtra("ygdm", mDoctorList.get(position).ygdm);
                intent.putExtra("doctor", mDoctorList.get(position));
                intent.putExtra("dept", dept);
                intent.putExtra("yysj",yysj);
                intent.putExtra("zblb",zblb);
                intent.putExtra("type", 1);//专家挂号
                intent.putExtra("isShowFirst",true);
                mContext.startActivity(intent);
            }
        });

        holder.btnAppoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(mContext, AppointTimeActivity.class);
                Intent intent = new Intent(mContext, SelectProfileOrAppointActivity.class);
                intent.putExtra("ygdm", mDoctorList.get(position).ygdm);
                intent.putExtra("doctor", mDoctorList.get(position));
                intent.putExtra("dept", dept);
                intent.putExtra("yysj",yysj);
                intent.putExtra("zblb",zblb);
                intent.putExtra("type", 1);//专家挂号
                intent.putExtra("isShowFirst",false);
                mContext.startActivity(intent);
            }
        });


        if (!mDoctorList.get(position).ysjj.equals("")) {
            holder.tvSeeAll.setVisibility(View.VISIBLE);
            holder.tvSeeAll.setOnClickListener(new View.OnClickListener() {
                Boolean flag = true;

                @Override
                public void onClick(View v) {
                    if (flag) {

                        flag = false;
                        holder.tvIntro.setEllipsize(null); // 展开
                        holder.tvIntro.setSingleLine(flag);
                        Drawable drawable = mContext.getResources().getDrawable(
                                R.drawable.info_up);
                        // / 这一步必须要做,否则不会显示.
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                                drawable.getMinimumHeight());
                        holder.tvSeeAll.setCompoundDrawables(null, null,
                                drawable, null);
                    } else {
                        flag = true;
                        holder.tvIntro.setEllipsize(TextUtils.TruncateAt.END); // 收缩
                        holder.tvIntro.setMaxLines(2);
                        Drawable drawable = mContext.getResources().getDrawable(
                                R.drawable.info_down);
                        // / 这一步必须要做,否则不会显示.
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                                drawable.getMinimumHeight());
                        holder.tvSeeAll.setCompoundDrawables(null, null,
                                drawable, null);
                    }
                }
            });
        } else {
            holder.tvSeeAll.setVisibility(View.GONE);
        }


        String url = Constants.getHeadUrl() +Constants.getHospitalID() + "/" + mDoctorList.get(position).ygdm + "_150x150" + ".png";
        RoundImageUtils.setRoundImageView(holder.ivHead,url,mActivity,imageLoader);

        return convertView;
    }

    private static class ViewHolder {
        TextView tvName,tvProfession,tvZjfy,tvSeeAll,tvIntro;
        Button btnSeeDocinfo,btnAppoint;
        RoundImageView ivHead;
        public ViewHolder(View v){
            ivHead = (RoundImageView) v.findViewById(R.id.iv_head);
            tvName = (TextView) v.findViewById(R.id.tv_name);
            tvIntro = (TextView) v.findViewById(R.id.tv_intro);
            tvProfession = (TextView) v.findViewById(R.id.tv_profession);
            tvSeeAll = (TextView) v.findViewById(R.id.tv_see_all);
            tvZjfy = (TextView) v.findViewById(R.id.tv_zjfy);
            btnSeeDocinfo = (Button) v.findViewById(R.id.btn_see_doctinfo);
            btnAppoint = (Button) v.findViewById(R.id.btn_appoint);
            v.setTag(this);
        }
    }
}
