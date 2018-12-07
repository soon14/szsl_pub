package com.bsoft.hospital.pub.suzhoumh.activity.adapter;

import java.util.ArrayList;
import java.util.List;

import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.model.ActivityVo;
import com.bsoft.hospital.pub.suzhoumh.util.DateUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author Tank E-mail:zkljxq@126.com
 * 
 * @类说明
 */
public class ActivityAdapter extends BaseAdapter {

	LayoutInflater mInflater;
	Context activity;
	ArrayList<ActivityVo> datas = new ArrayList<ActivityVo>();

	public ActivityAdapter(Context context) {
		this.mInflater = LayoutInflater.from(context);
		this.activity = context;
	}

	public void addData(ArrayList<ActivityVo> msgs) {
		if (null != msgs) {
			datas = msgs;
		} else {
			datas = new ArrayList<ActivityVo>();
		}
		notifyDataSetChanged();
	}

	public void addMoreData(List<ActivityVo> msgs) {
		if (null != msgs && msgs.size() > 0) {
			datas.addAll(msgs);
			notifyDataSetChanged();
		}
	}

	public void remove(int index) {
		datas.remove(index);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public ActivityVo getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView,
			ViewGroup parentViewGroup) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.activity_item, null);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.des = (TextView) convertView.findViewById(R.id.des);
			holder.time = (TextView) convertView.findViewById(R.id.time);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ActivityVo vo = getItem(position);
		holder.title.setText(vo.title);
		holder.des.setText(vo.des);
		holder.time.setText(DateUtil.getDateTime(vo.createdate));
		return convertView;
	}

	public static class ViewHolder {
		public TextView title, des, time;
	}

}
