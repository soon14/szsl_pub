package com.bsoft.hospital.pub.suzhoumh.activity.adapter.physical;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.model.app.physical.PhysicalVo;

import java.util.ArrayList;
import java.util.List;

public class PhysicalListAdapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater;
    private List<PhysicalVo> dataList = new ArrayList<>();

    public PhysicalListAdapter(Context baseActivity) {
        this.mLayoutInflater = (LayoutInflater) baseActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addData(List<PhysicalVo> physicalVos) {
        if (null != physicalVos) {
            dataList = physicalVos;
        } else {
            dataList = new ArrayList<>();
        }
        notifyDataSetChanged();
    }

    public void addMoreData(List<PhysicalVo> physicalVos) {
        if (null != physicalVos) {
                dataList.addAll(physicalVos);
        }
        notifyDataSetChanged();
    }

    public void remove(int index) {
        dataList.remove(index);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public PhysicalVo getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.item_physical_list, null);
            holder.dateTv = (TextView) convertView.findViewById(R.id.tv_date);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        PhysicalVo item = dataList.get(position);
        holder.dateTv.setText(item.tjrq);

        return convertView;
    }

    class ViewHolder {
        TextView dateTv;
    }
}
