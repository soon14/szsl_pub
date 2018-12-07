package com.bsoft.hospital.pub.suzhoumh.activity.adapter.my;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.model.my.ChargeDetailVo;

import java.util.ArrayList;

/**
 * Author: 嘿嘿抛物线
 * Email: 497635745@qq.com
 * Date: 2016-9-10 12:16
 */
public class ChargeDetailGridAdapter extends BaseAdapter {

    private ArrayList<ChargeDetailVo> dataList = new ArrayList<>();
    private Context context;

    public ChargeDetailGridAdapter(Context context) {
        this.context = context;
    }

    public void setData(ArrayList<ChargeDetailVo> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public ChargeDetailVo getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_charge_detail_grid, null, false);
            viewHolder = new ViewHolder();
            viewHolder.xmmc = (TextView) convertView.findViewById(R.id.tv_xmmc);
            viewHolder.je = (TextView) convertView.findViewById(R.id.tv_je);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.xmmc.setText(getItem(position).xmmc);
        viewHolder.je.setText(getItem(position).zjje);

        return convertView;
    }

    class ViewHolder {
        TextView xmmc;
        TextView je;
    }
}
