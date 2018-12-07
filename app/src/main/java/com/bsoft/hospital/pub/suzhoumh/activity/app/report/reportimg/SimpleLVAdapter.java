package com.bsoft.hospital.pub.suzhoumh.activity.app.report.reportimg;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.model.app.report.RisDetailVo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author :lizhengcao
 * @date :2017/3/16
 * E-mail:lizc@bsoft.com.cn
 * @类说明 影像图片对应列表适配器
 */

public class SimpleLVAdapter extends BaseAdapter {


    private List<RisDetailVo.DataTpdzBean> mDataTpdzList;
    private Context mContext;

    public SimpleLVAdapter(Context context, List<RisDetailVo.DataTpdzBean> dataTpdzList) {
        this.mContext = context;
        this.mDataTpdzList = dataTpdzList;
    }

    @Override
    public int getCount() {
        if (mDataTpdzList.size() % 3 == 1) {
            return mDataTpdzList == null ? 0 : mDataTpdzList.size() + 2;
        } else if (mDataTpdzList.size() % 3 == 2) {
            return mDataTpdzList == null ? 0 : mDataTpdzList.size() + 1;
        }

        return mDataTpdzList == null ? 0 : mDataTpdzList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataTpdzList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_lv_item, parent, false);
            holder = new ViewHolder(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position<mDataTpdzList.size()){

            holder.tvPic.setText(mContext.getString(R.string.str_picture) + (position + 1));
        }else {
            holder.tvPic.setText("");
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView tvPic;

        public ViewHolder(View v) {
            tvPic = (TextView) v.findViewById(R.id.tv_pic);
            v.setTag(this);
        }
    }
}
