package com.bsoft.hospital.pub.suzhoumh.activity.app.healthtool;

import java.util.List;

import com.bsoft.hospital.pub.suzhoumh.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MedicineRemindAdapter extends BaseAdapter{

	LayoutInflater inflater;
	Context context;
	List<MedicineRemindModel> list;

	public MedicineRemindAdapter(Context context, List<MedicineRemindModel> list) {
		super();
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_medicine, null);
			viewHolder = new ViewHolder();
			viewHolder.tv_medicine_name = (TextView)convertView.findViewById(
					R.id.tv_medicine_name);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.tv_medicine_name.setText(list.get(position).medname);
		return convertView;
	}

	public class ViewHolder {
		TextView tv_medicine_name;
	}

	public void refresh(List<MedicineRemindModel> l) {
		if(l!=null && l.size()>0){
			this.list = l;
		}else{
			this.list.clear();
		}
		notifyDataSetChanged();
	}

	public void add(MedicineRemindModel doc) {
		list.add(doc);
		notifyDataSetChanged();
	}

	public void add(List<MedicineRemindModel> docs) {
		list.addAll(docs);
		notifyDataSetChanged();

	}
}
