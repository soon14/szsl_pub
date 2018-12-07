package com.bsoft.hospital.pub.suzhoumh.activity.adapter;

import java.util.ArrayList;
import java.util.List;

import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.model.RemindVo;
import com.bsoft.hospital.pub.suzhoumh.util.DateUtil;
import com.app.tanklib.util.StringUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PregnantRemindAdapter extends BaseAdapter {
	private LayoutInflater mLayoutInflater;
	private List<RemindVo> dataList = new ArrayList<RemindVo>();
	Context baseActivity;

	public PregnantRemindAdapter(Context baseActivity) {
		this.baseActivity = baseActivity;
		this.mLayoutInflater = (LayoutInflater) baseActivity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void addData(List<RemindVo> msgs) {
		if (null != msgs) {
			dataList = msgs;
		} else {
			dataList = new ArrayList<RemindVo>();
		}
		notifyDataSetChanged();
	}

	public void addMoreData(List<RemindVo> msgs) {
		if (null != msgs) {
			dataList.addAll(msgs);
		}
		notifyDataSetChanged();
	}

	public void addData(RemindVo vo) {
		dataList.add(vo);
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
	public RemindVo getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.remind_item1, null);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.text1 = (TextView) convertView.findViewById(R.id.text1);
			holder.text2 = (TextView) convertView.findViewById(R.id.text2);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final RemindVo item = dataList.get(position);
		holder.name.setText(item.name);
		holder.text1.setText("末次月经:"
				+ DateUtil.getDateTime("yyyy-MM-dd", item.pam1));
		holder.text2.setText("预产期:"
				+ DateUtil.getDateTime("yyyy-MM-dd", item.pam2));
		return convertView;
	}

	// 隐藏后4位
	public String getCardStr(String idcard) {
		if (StringUtil.isEmpty(idcard)) {
			return "";
		}
		if (idcard.length() <= 4) {
			return "";
		}
		try {
			String s = idcard.substring(0, idcard.length() - 4);
			return s + "****";
		} catch (Exception e) {
			return "";
		}
	}

	public static class ViewHolder {
		public TextView name, text1, text2;
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
