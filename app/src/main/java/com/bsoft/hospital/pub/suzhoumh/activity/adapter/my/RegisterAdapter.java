package com.bsoft.hospital.pub.suzhoumh.activity.adapter.my;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.model.my.RegisterVo;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: 嘿嘿抛物线
 * Email: 497635745@qq.com
 * Date: 2016-9-10 9:39
 */
public class RegisterAdapter extends BaseAdapter {

    private List<RegisterVo> list = new ArrayList<RegisterVo>();
    private Context context;

    public RegisterAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<RegisterVo> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public RegisterVo getItem(int position) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_my_note_register_list, null);
            viewHolder.jzks = (TextView) convertView.findViewById(R.id.tv_jzks);
            viewHolder.ghsj = (TextView) convertView.findViewById(R.id.tv_ghsj);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        RegisterVo vo = getItem(position);
        viewHolder.jzks.setText(vo.jzks);
        viewHolder.ghsj.setText(vo.ghsj);

        return convertView;
    }

    class ViewHolder {
        TextView jzks;
        TextView ghsj;
    }

}
