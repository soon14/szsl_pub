package com.bsoft.hospital.pub.suzhoumh.activity.adapter;

import java.util.ArrayList;
import java.util.List;

import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.model.my.StreetVo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyInfoAddressStreetAdapter extends BaseAdapter {
	private LayoutInflater mLayoutInflater;
	private List<StreetVo> dataList = new ArrayList<StreetVo>();

	public MyInfoAddressStreetAdapter(Context baseActivity) {
		this.mLayoutInflater = (LayoutInflater) baseActivity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void addData(List<StreetVo> msgs) {
		if (null != msgs) {
			dataList = msgs;
		} else {
			dataList = new ArrayList<StreetVo>();
		}
		notifyDataSetChanged();
	}

	public void addMoreData(List<StreetVo> msgs) {
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
	public StreetVo getItem(int position) {
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
			convertView = mLayoutInflater.inflate(R.layout.address_item, null);
			holder.text = (TextView) convertView.findViewById(R.id.text);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		StreetVo item = dataList.get(position);
		holder.text.setText(item.title);
		// holder.time.setText(DateUtil.getDateTime(item.date));
		return convertView;
	}

	public static class ViewHolder {
		public TextView text;
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
