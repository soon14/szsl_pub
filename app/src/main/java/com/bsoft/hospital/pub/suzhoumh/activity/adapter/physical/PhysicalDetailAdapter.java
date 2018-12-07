package com.bsoft.hospital.pub.suzhoumh.activity.adapter.physical;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.tanklib.util.StringUtil;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.model.app.physical.PhysicalDetailVo;
import com.bsoft.hospital.pub.suzhoumh.model.app.physical.PhysicalItemVo;

import java.util.ArrayList;

/**
 * 体检详情的可展开列表的适配器
 */
public class PhysicalDetailAdapter extends BaseExpandableListAdapter {

    private ArrayList<PhysicalDetailVo> dataList;
    private Context context;

    public PhysicalDetailAdapter(Context context, ArrayList<PhysicalDetailVo> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    public void addData(ArrayList<PhysicalDetailVo> dataList) {
        if (null != dataList) {
            this.dataList = dataList;
        } else {
            this.dataList.clear();
        }
        this.notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return dataList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (hasReference(groupPosition)) {
            return getGroup(groupPosition).zx.size() + 1;
        } else {
            return getGroup(groupPosition).zx.size();
        }
    }

    @Override
    public PhysicalDetailVo getGroup(int groupPosition) {
        return dataList.get(groupPosition);
    }

    @Override
    public PhysicalItemVo getChild(int groupPosition, int childPosition) {
        if (hasReference(groupPosition)) {
            return getGroup(groupPosition).zx.get(childPosition - 1);
        } else {
            return getGroup(groupPosition).zx.get(childPosition);
        }
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        if (hasReference(groupPosition) && childPosition == 0) {
            return 0;
        }
        return getChild(groupPosition, childPosition).hashCode();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder;
        if (null == convertView) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_physical_detail_parent, null);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.nameTv = (TextView) convertView.findViewById(R.id.tv_name);
            groupViewHolder.indicatorIv = (ImageView) convertView.findViewById(R.id.iv_indicator);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        groupViewHolder.indicatorIv.setBackgroundResource(R.drawable.arrow_up);
        if (isExpanded) {
            groupViewHolder.indicatorIv.setBackgroundResource(R.drawable.arrow_down);
        }
        groupViewHolder.nameTv.setText(dataList.get(groupPosition).zhxm);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View
            convertView, ViewGroup parent) {
        if (hasReference(groupPosition)) {
            if (childPosition == 0) {
                convertView = LayoutInflater.from(context).inflate(R.layout.header_physical_detail_child, null);
                return convertView;
            }
            convertView = LayoutInflater.from(context).inflate(R.layout.item_physical_detail_child_two, null);
            TextView nameTv = (TextView) convertView.findViewById(R.id.tv_name);
            TextView resultTv = (TextView) convertView.findViewById(R.id.tv_result);
            TextView referenceTv = (TextView) convertView.findViewById(R.id.tv_reference);

            PhysicalItemVo item = getChild(groupPosition, childPosition);
            nameTv.setText(item.jcxm);
            if (StringUtil.isEmpty(item.ts)) {
                resultTv.setTextColor(Color.BLACK);
                resultTv.setText(item.jcjg);
            } else {
                resultTv.setTextColor(Color.RED);
                resultTv.setText(item.jcjg + " " + item.ts);
            }
            if (!StringUtil.isEmpty(item.ckfw)) {
                referenceTv.setText(item.ckfw + item.dw);
            }

//        //字体加粗
//        if (child.sfzx.equals("1")) {
//            TextPaint tp;
//
//            tp = tv_name.getPaint();
//            tp.setFakeBoldText(true);
//        }
        } else {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_physical_detail_child_one, null);
            TextView nameTv = (TextView) convertView.findViewById(R.id.tv_name);
            TextView resultTv = (TextView) convertView.findViewById(R.id.tv_result);

            PhysicalItemVo item = getChild(groupPosition, childPosition);
            nameTv.setText(item.jcxm);
            resultTv.setTextColor(Color.BLACK);
            resultTv.setText(item.jcjg + item.dw);
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private class GroupViewHolder {
        TextView nameTv;
        ImageView indicatorIv;
    }

    private boolean hasReference(int groupPosition) {
        for (PhysicalItemVo itemVo : dataList.get(groupPosition).zx) {
            if (!StringUtil.isEmpty(itemVo.ckfw)) {
                return true;
            }
        }
        return false;
    }
}
