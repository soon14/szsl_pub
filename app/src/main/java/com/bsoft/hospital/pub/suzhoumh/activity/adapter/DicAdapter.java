package com.bsoft.hospital.pub.suzhoumh.activity.adapter;

import java.util.ArrayList;
import java.util.List;

import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.model.ChoiceItem;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class DicAdapter extends BaseAdapter {
	LayoutInflater mLayoutInflater;
	List<ChoiceItem> dataList;

	public DicAdapter(Activity baseActivity, ArrayList<ChoiceItem> list) {
		this.dataList = list;
		mLayoutInflater = (LayoutInflater) baseActivity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public void change(int pos){
		for(int i=0;i<dataList.size();i++){
			if(pos==i){
				dataList.get(i).isChoice=true;
			}else{
				dataList.get(i).isChoice=false;
			}
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public ChoiceItem getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewholder;
		if (convertView == null) {
			viewholder = new ViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.dic_item,
					null);
			viewholder.tvName = (TextView) convertView
					.findViewById(R.id.tv_name);
			viewholder.ibCheck = (ImageView) convertView
					.findViewById(R.id.ib_check);
			convertView.setTag(viewholder);
		} else {
			viewholder = (ViewHolder) convertView.getTag();
		}
		ChoiceItem item = dataList.get(position);
		viewholder.tvName.setText(item.itemName);
		viewholder.ibCheck
				.setImageResource(item.isChoice ? R.drawable.btn_checked_n
						: R.drawable.btn_checked_p);
		return convertView;
	}

	public static class ViewHolder {
		public TextView tvName;// 姓名
		public ImageView ibCheck;

	}

}
