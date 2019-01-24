package com.bsoft.hospital.pub.suzhoumh.activity.app.queue;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bsoft.hospital.pub.suzhoumh.AppApplication;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.model.app.queue.PDQKVo;
import com.bsoft.hospital.pub.suzhoumh.util.ToastUtils;
import com.daoyixun.ipsmap.IpsMapSDK;

import java.util.ArrayList;

/**
 * 我的排队，新
 * Created by Administrator on 2016/5/19.
 */
public class MyQueueNewAdapter extends BaseAdapter {

    private ArrayList<PDQKVo> my_list;
    private Context context;

    public MyQueueNewAdapter(Context context, ArrayList<PDQKVo> list) {
        this.my_list = list;
        this.context = context;
    }

    public void refresh(ArrayList<PDQKVo> list) {
        this.my_list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return my_list.size();
    }

    @Override
    public Object getItem(int position) {
        return my_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_queue_my, null);
            holder.tv_dept = (TextView) convertView.findViewById(R.id.tv_dept);
            holder.tv_consult = (TextView) convertView.findViewById(R.id.tv_consult);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_doctor = (TextView) convertView.findViewById(R.id.tv_doctor);
            holder.tv_num = (TextView) convertView.findViewById(R.id.tv_num);
            holder.tv_wait = (TextView) convertView.findViewById(R.id.tv_wait);
            holder.tv_floor = (TextView) convertView.findViewById(R.id.tv_floor);
            holder.tvYndh = convertView.findViewById(R.id.tv_yndh);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_dept.setText(my_list.get(position).ksmc);
        holder.tv_consult.setText(my_list.get(position).zsmc);
        if (new AppApplication().loginUser.realname != null) {
            holder.tv_name.setText(new AppApplication().loginUser.realname);
        }
        holder.tv_doctor.setText(my_list.get(position).ygxm);
        holder.tv_num.setText(my_list.get(position).wdxh);
        holder.tv_wait.setText(my_list.get(position).ddrs);
        holder.tv_floor.setText(my_list.get(position).zslcxx);

        holder.tvYndh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(my_list.get(position).ksdm)) {
                    ToastUtils.showToastShort("目标地址为空，不能进行院导航");
                    return;
                }

                IpsMapSDK.openIpsMapActivity(context, Constants.IPSMAP_MAP_ID, my_list.get(position).ksdm);
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView tv_dept;
        TextView tv_name;
        TextView tv_doctor;
        TextView tv_num;//我的序号
        TextView tv_wait;//前面等待
        TextView tv_consult;
        TextView tv_floor;
        TextView tvYndh;

    }
}
