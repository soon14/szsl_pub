package com.bsoft.hospital.pub.suzhoumh.activity.adapter;

import java.util.ArrayList;
import java.util.List;

import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.cache.ModelCache;
import com.bsoft.hospital.pub.suzhoumh.model.my.MyContactVo;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyContactsAdapter extends BaseAdapter {
	private LayoutInflater mLayoutInflater;
	private List<MyContactVo> dataList = new ArrayList<MyContactVo>();
	Activity baseActivity;

	public MyContactsAdapter(Activity baseActivity) {
		this.baseActivity = baseActivity;
		this.mLayoutInflater = (LayoutInflater) baseActivity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	public void addData(ArrayList<MyContactVo> msgs) {
		if (null != msgs) {
			dataList = msgs;
		} else {
			dataList = new ArrayList<MyContactVo>();
		}
		notifyDataSetChanged();
	}

	public void addMoreData(ArrayList<MyContactVo> msgs) {
		if (null != msgs) {
			dataList.addAll(msgs);
		}
		notifyDataSetChanged();
	}

	public void addData(MyContactVo vo) {
		dataList.add(0, vo);
		notifyDataSetChanged();
	}
	
	public void changeData(MyContactVo vo) {
		int i=dataList.indexOf(vo);
		if(-1!=i){
			dataList.set(i, vo);
			notifyDataSetChanged();
		}
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
	public MyContactVo getItem(int position) {
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
			convertView = mLayoutInflater.inflate(R.layout.mycontacts_item,
					null);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.mobile = (TextView) convertView.findViewById(R.id.mobile);
			holder.relation = (TextView) convertView
					.findViewById(R.id.relation);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		MyContactVo item = dataList.get(position);
		holder.name.setText(item.name);
		holder.mobile.setText(item.mobile);
		holder.relation.setText(ModelCache.getInstance().getRelationName(
				item.relation));

		return convertView;
	}

	public static class ViewHolder {
		public TextView name, mobile, relation;
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
