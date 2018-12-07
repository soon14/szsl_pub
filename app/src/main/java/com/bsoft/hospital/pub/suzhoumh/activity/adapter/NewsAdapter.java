package com.bsoft.hospital.pub.suzhoumh.activity.adapter;

import java.util.ArrayList;
import java.util.List;

import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.model.DynamicShow;
import com.bsoft.hospital.pub.suzhoumh.util.DateUtil;
import com.app.tanklib.util.StringUtil;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author Tank E-mail:zkljxq@126.com
 * 
 * @类说明 医生健康资讯
 */
public class NewsAdapter extends BaseAdapter {

	LayoutInflater mInflater;
	Context activity;
	ArrayList<DynamicShow> datas = new ArrayList<DynamicShow>();

	public NewsAdapter(Context context) {
		this.mInflater = LayoutInflater.from(context);
		this.activity = context;
	}

	public void addData(ArrayList<DynamicShow> msgs) {
		if (null != msgs) {
			datas = msgs;
		} else {
			datas = new ArrayList<DynamicShow>();
		}
		notifyDataSetChanged();
	}

	public void addMoreData(List<DynamicShow> msgs) {
		if (null != msgs && msgs.size() > 0) {
			datas.addAll(msgs);
			notifyDataSetChanged();
		}
	}

	public void remove(int index) {
		datas.remove(index);
		notifyDataSetChanged();
	}
	
	public void addCommentCount(String drid) {
		DynamicShow show = new DynamicShow();
		show.drid = drid;
		int index = datas.indexOf(show);
		if (index != -1) {
			datas.get(index).replycount++;
			notifyDataSetChanged();
		}
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public DynamicShow getItem(int position) {
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
		DynamicShow vo = getItem(position);
		holder.title.setText(StringUtil.getTextLimit(vo.title,12));
		holder.des.setText(Html.fromHtml(vo.content));
		holder.time.setText(DateUtil.getDateTime(vo.createdate));
		return convertView;
	}

	public static class ViewHolder {
		public TextView title, des, time;
	}

}
