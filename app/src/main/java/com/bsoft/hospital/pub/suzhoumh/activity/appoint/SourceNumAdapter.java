package com.bsoft.hospital.pub.suzhoumh.activity.appoint;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.app.appoint.AppointConfirmActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.app.appoint.AppointSelectDateActivity;
import com.bsoft.hospital.pub.suzhoumh.model.app.appoint.AppointDeptVo;
import com.bsoft.hospital.pub.suzhoumh.model.app.appoint.AppointDoctorVo;
import com.bsoft.hospital.pub.suzhoumh.model.app.appoint.AppointNumberSourceVo;
import com.bsoft.hospital.pub.suzhoumh.util.DateUtil;

import java.util.List;

/**
 * @author :lizhengcao
 * @date :2017/2/24
 * E-mail:lizc@bsoft.com.cn
 * @类说明
 */

public class SourceNumAdapter extends BaseAdapter {

    private List<AppointNumberSourceVo> mSourceNumList;
    private Context mContext;
    private AppointDeptVo dept;
    private AppointDoctorVo doctor;
    private int type;
    private String yysj;
    private String zblb;

    public SourceNumAdapter(List<AppointNumberSourceVo> mSourceNumList, Context context,
                            AppointDeptVo dept, AppointDoctorVo doctor, int type, String yysj, String zblb) {
        this.mSourceNumList = mSourceNumList;
        this.mContext = context;
        this.dept = dept;
        this.doctor = doctor;
        this.type = type;
        this.yysj = yysj;
        this.zblb = zblb;
    }

    @Override
    public int getCount() {
        return mSourceNumList == null ? 0 : mSourceNumList.size();
    }

    @Override
    public Object getItem(int position) {
        return mSourceNumList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_num_source, parent, false);
            holder = new ViewHolder(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        AppointNumberSourceVo data = mSourceNumList.get(position);
        holder.tvSd.setText(data.qssj + "-" + data.jzsj);
        holder.tvZh.setText("1");
        holder.tvZt.setText("正常");
        holder.tvYh.setText(data.syhy);
        if (Integer.parseInt(data.syhy) == 0) {
            //不可进行预约
            holder.btnAppoint.setText("已满");
            holder.btnAppoint.setBackgroundResource(R.drawable.bg_single_gray);
        }else if(Integer.parseInt(data.syhy) == 1){
            holder.btnAppoint.setText("预约");
            holder.btnAppoint.setBackgroundResource(R.drawable.bigbut_blue);
        }

        holder.btnAppoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!"0".equals(mSourceNumList.get(position).syhy)) {
                    Intent intent = new Intent(mContext, AppointConfirmActivity.class);
                    intent.putExtra("ksmc", dept.ksmc);
                    if (type == 2 || type == 3)//普通
                    {
                        intent.putExtra("ygdm", "");
                        intent.putExtra("ygxm", "");
                        intent.putExtra("ksdm", dept.ksdm);

                    }
                    if (type == 1)//专家
                    {
                        intent.putExtra("ygdm", doctor.ygdm);
                        intent.putExtra("ygxm", doctor.ygxm);
                        intent.putExtra("ksdm", doctor.ksdm);
                    }
                    intent.putExtra("yysj", DateUtil.dateFormate(yysj, "yyyy-MM-dd"));
                    intent.putExtra("num_source", mSourceNumList.get(position));
                    intent.putExtra("jzxh", mSourceNumList.get(position).jzxh);
                    intent.putExtra("type", type);
                    intent.putExtra("zblb", zblb);
                    mContext.startActivity(intent);
                } else {
                    Toast.makeText(mContext, "当前已无号源", Toast.LENGTH_SHORT).show();
                }

            }
        });
        return convertView;
    }

    private static class ViewHolder {
        TextView tvSd, tvZh, tvYh, tvZt;
        Button btnAppoint;

        public ViewHolder(View v) {
            tvSd = (TextView) v.findViewById(R.id.tv_sd);
            tvZh = (TextView) v.findViewById(R.id.tv_zh);
            tvYh = (TextView) v.findViewById(R.id.tv_yh);
            tvZt = (TextView) v.findViewById(R.id.tv_zt);
            btnAppoint = (Button) v.findViewById(R.id.btn_appoint);
            v.setTag(this);
        }
    }
}
