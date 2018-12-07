package com.bsoft.hospital.pub.suzhoumh.activity.adapter.my;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.model.my.ChargeVo;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: 嘿嘿抛物线
 * Email: 497635745@qq.com
 * Date: 2016-9-10 9:38
 */
public class ChargeAdapter extends BaseAdapter {

    private List<ChargeVo> list = new ArrayList<ChargeVo>();
    private Context context;

    public ChargeAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<ChargeVo> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public ChargeVo getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parentViewGroup) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_my_note_charge_list, null);
            viewHolder.fphm = (TextView) convertView.findViewById(R.id.tv_fphm);
            viewHolder.sfrq = (TextView) convertView.findViewById(R.id.tv_sfrq);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ChargeVo vo = getItem(position);
        viewHolder.fphm.setText(vo.fphm);
        viewHolder.sfrq.setText(vo.sfrq);

        return convertView;
    }

    class ViewHolder {
        TextView fphm;
        TextView sfrq;
    }
}