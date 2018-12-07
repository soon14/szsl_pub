package com.bsoft.hospital.pub.suzhoumh.activity.adapter;

import java.util.ArrayList;
import java.util.List;

import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.model.MessageDetail;
import com.bsoft.hospital.pub.suzhoumh.util.DateUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MessageDetailAdapter extends BaseAdapter {
	private LayoutInflater mLayoutInflater;
	private List<MessageDetail> dataList = new ArrayList<MessageDetail>();
	int kinds=0;

	public MessageDetailAdapter(Context baseActivity,int kinds) {
		this.mLayoutInflater = (LayoutInflater) baseActivity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.kinds=kinds;
	}

	public void addMoreData(List<MessageDetail> msgs) {
		if (null != msgs && msgs.size() > 0) {
			dataList.addAll(msgs);
			notifyDataSetChanged();
		}

	}

	public void addData(List<MessageDetail> msgs) {
		if (null != msgs) {
			dataList = msgs;
		} else {
			dataList = new ArrayList<MessageDetail>();
		}
		notifyDataSetChanged();
	}

//	public void addMoreData(List<MessageDetail> msgs) {
//		if (null != msgs) {
//			dataList.addAll(msgs);
//		}
//		notifyDataSetChanged();
//	}

	public void remove(int index) {
		dataList.remove(index);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public MessageDetail getItem(int position) {
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
			convertView = mLayoutInflater.inflate(R.layout.message_detail_item,
					null);
			holder.text = (TextView) convertView.findViewById(R.id.text);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.header = (ImageView) convertView.findViewById(R.id.header);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		MessageDetail item = dataList.get(position);
		holder.text.setText(item.content);
		holder.time.setText(DateUtil.getDateTime("yyyy-MM-dd HH:mm:ss", item.date));
		switch (kinds) {
		case 1:
			holder.header.setImageResource(R.drawable.icon_message1);
			break;
		case 2:
			holder.header.setImageResource(R.drawable.icon_message2);
			break;
		case 3:
			holder.header.setImageResource(R.drawable.icon_message3);
			break;
		case 4:
			holder.header.setImageResource(R.drawable.icon_message4);
			break;
		case 5:
			holder.header.setImageResource(R.drawable.icon_message5);
			break;
		case 6:
			holder.header.setImageResource(R.drawable.icon_message7);
			break;
		case 7:
			holder.header.setImageResource(R.drawable.icon_message6);
			break;
		case 11:
			holder.header.setImageResource(R.drawable.icon_message4);
			break;
		default:
			break;
		}
		return convertView;
	}

	public static class ViewHolder {
		public TextView name, text, time;
		public ImageView header;
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
