package com.bsoft.hospital.pub.suzhoumh.activity.adapter;

import java.util.ArrayList;
import java.util.List;

import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.model.my.HosVo;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SeekHosAdapter extends BaseAdapter {
	private LayoutInflater mLayoutInflater;
	private List<HosVo> dataList = new ArrayList<HosVo>();
	Activity baseActivity;
	// 1 历史 2 搜索
	int type = 1;

	public SeekHosAdapter(Activity baseActivity) {
		this.baseActivity = baseActivity;
		this.mLayoutInflater = (LayoutInflater) baseActivity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void clear() {
		dataList = new ArrayList<HosVo>();
		notifyDataSetChanged();
	}

	public void addData(ArrayList<HosVo> msgs) {
		if (null != msgs) {
			dataList = msgs;
		} else {
			dataList = new ArrayList<HosVo>();
		}
		notifyDataSetChanged();
	}

	public void addMoreData(ArrayList<HosVo> msgs) {
		if (null != msgs) {
			dataList.addAll(msgs);
		}
		notifyDataSetChanged();
	}

	public void addData(HosVo vo) {
		dataList.add(0, vo);
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
			convertView = mLayoutInflater.inflate(R.layout.seek_hos_item, null);
			holder.name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.left = (ImageView) convertView.findViewById(R.id.left);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		HosVo item = dataList.get(position);
		holder.name.setText(item.title);
		if (type == 1) {
			holder.left.setImageResource(R.drawable.seekhos_left);
		} else {
			holder.left.setImageResource(R.drawable.seekhos_leftsearch);
		}
		return convertView;
	}

	public static class ViewHolder {
		public TextView name;
		public ImageView left;
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
