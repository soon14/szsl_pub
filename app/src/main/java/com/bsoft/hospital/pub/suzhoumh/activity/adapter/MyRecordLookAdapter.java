package com.bsoft.hospital.pub.suzhoumh.activity.adapter;

import java.util.ArrayList;
import java.util.List;

import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.model.record.RecordVo;
import com.bsoft.hospital.pub.suzhoumh.util.DateUtil;
import com.app.tanklib.util.StringUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyRecordLookAdapter extends BaseAdapter {
	private LayoutInflater mLayoutInflater;
	private List<RecordVo> dataList = new ArrayList<RecordVo>();
	int type = 0;

	public MyRecordLookAdapter(Context baseActivity, int type) {
		this.mLayoutInflater = (LayoutInflater) baseActivity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.type = type;
	}
	
	public void addMore(List<RecordVo> msgs){
		if(null!=msgs&&msgs.size()>0){
			dataList.addAll(msgs);
			notifyDataSetChanged();
		}
	}

	public void addData(List<RecordVo> msgs) {
		if (null != msgs) {
			dataList = msgs;
		} else {
			dataList = new ArrayList<RecordVo>();
		}
		notifyDataSetChanged();
	}

	public void addMoreData(List<RecordVo> msgs) {
		if (null != msgs) {
			dataList.addAll(msgs);
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
	public RecordVo getItem(int position) {
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
			switch (type) {
			case 1:
				convertView = mLayoutInflater.inflate(
						R.layout.myrecordlook_item1, null);
				break;
			case 2:
				convertView = mLayoutInflater.inflate(
						R.layout.myrecordlook_item3, null);
				break;
			case 3:
				convertView = mLayoutInflater.inflate(
						R.layout.myrecordlook_item2, null);
				break;
			default:
				break;
			}

			holder.chiefcomplaint = (TextView) convertView
					.findViewById(R.id.chiefcomplaint);
			holder.deptname = (TextView) convertView
					.findViewById(R.id.deptname);
			holder.hospname = (TextView) convertView
					.findViewById(R.id.hospname);
			holder.visitdatetime = (TextView) convertView
					.findViewById(R.id.visitdatetime);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		RecordVo item = dataList.get(position);
		switch (type) {
		case 1:
			holder.chiefcomplaint.setText(StringUtil.getTextLimit(item.chiefcomplaint,8));
			holder.deptname.setText(item.deptname);
			holder.hospname.setText(item.hospname);
			holder.visitdatetime.setText(DateUtil
					.getBirthDateTime(item.visitdatetime));
			break;
		case 2:
			holder.chiefcomplaint.setText(StringUtil.getTextLimit(item.chiefcomplaint,8));
			holder.deptname.setText(item.deptname);
			holder.visitdatetime.setText(DateUtil
					.getBirthDateTime(item.visitdatetime));
			break;
		case 3:
			holder.deptname.setText(item.deptname);
			holder.hospname.setText(item.hospname);
			holder.visitdatetime.setText(DateUtil
					.getBirthDateTime(item.visitdatetime));
			break;
		default:
			break;
		}
		return convertView;
	}

	public static class ViewHolder {
		public TextView chiefcomplaint, deptname, hospname, visitdatetime;
	}

	public static String text(String input) {
		if (null == input) {
			return "";
		}
		// if (input.indexOf("上海市闵行区") != -1) {
		// input = input.replace("上海市闵行区", "");
		// }
		if (input.length() > 20) {
			return input.substring(0, 20);
		} else {
			return input;
		}
	}

	public static String ToDBC(String input) {
		if (null == input) {
			return "";
		}
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}

}
