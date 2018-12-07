package com.bsoft.hospital.pub.suzhoumh.activity.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.tanklib.util.StringUtil;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.app.map.RouteActivity;
import com.bsoft.hospital.pub.suzhoumh.model.my.HosVo;

public class SeekAdapter extends BaseAdapter {
	private LayoutInflater mLayoutInflater;
	private List<HosVo> dataList = new ArrayList<HosVo>();
	Activity baseActivity;

	public SeekAdapter(Activity baseActivity) {
		this.baseActivity = baseActivity;
		this.mLayoutInflater = (LayoutInflater) baseActivity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void addData(List<HosVo> msgs) {
		if (null != msgs) {
			dataList = msgs;
		} else {
			dataList = new ArrayList<HosVo>();
		}
		notifyDataSetChanged();
	}

	public void addMoreData(List<HosVo> msgs) {
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
	public HosVo getItem(int position) {
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
			convertView = mLayoutInflater.inflate(R.layout.seekhos_item, null);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.distance = (TextView) convertView
					.findViewById(R.id.distance);
			holder.address = (TextView) convertView.findViewById(R.id.address);
			holder.go = (LinearLayout) convertView.findViewById(R.id.go);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final HosVo item = dataList.get(position);
		holder.title.setText(item.title);
		if (!StringUtil.isEmpty(item.distance)) {
			holder.distance.setText(String.format("%.2f",
					Double.valueOf(item.distance) / 1000.0)
					+ "KM");
		} else {
			holder.distance.setText("");
		}
		holder.address.setText(item.address);
		holder.go.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(baseActivity, RouteActivity.class);
				intent.putExtra("title", item.title);
				intent.putExtra("latitude", item.latitude);
				intent.putExtra("longitude", item.longitude);
				baseActivity.startActivity(intent);
			}
		});
		return convertView;
	}

	public static class ViewHolder {
		public TextView title, distance, address;
		public LinearLayout go;
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
