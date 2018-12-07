package com.bsoft.hospital.pub.suzhoumh.activity.adapter;

import java.util.ArrayList;
import java.util.List;

import com.bsoft.hospital.pub.suzhoumh.activity.adapter.row.DynamicPicRowAdapter;
import com.bsoft.hospital.pub.suzhoumh.activity.adapter.row.DynamicTextRowAdapter;
import com.bsoft.hospital.pub.suzhoumh.model.DynamicShow;
import com.app.tanklib.bitmap.IndexUrlCache;
import com.app.tanklib.util.StringUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * @author Tank E-mail:zkljxq@126.com
 * 
 * @类说明
 */
public class DynamicAdapter extends BaseAdapter {

	LayoutInflater mInflater;
	Context activity;
	ArrayList<DynamicShow> datas = new ArrayList<DynamicShow>();
	IndexUrlCache urlMap;

	public DynamicAdapter(Context context, IndexUrlCache urlMap) {
		this.mInflater = LayoutInflater.from(context);
		this.activity = context;
		this.urlMap = urlMap;
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
		View v = null;
		DynamicShow vo = getItem(position);
		int type = getItemViewType(position);
		if (null == convertView) {
			switch (type) {
			case 0:
				v = DynamicTextRowAdapter.newView(activity, parentViewGroup);
				break;
			case 1:
				v = DynamicPicRowAdapter.newView(activity, parentViewGroup);
				break;
			default:
				break;
			}
		} else {
			v = convertView;
		}

		switch (type) {
		case 0:
			DynamicTextRowAdapter.bindView(v, vo, activity, urlMap, position);
			break;
		case 1:
			DynamicPicRowAdapter.bindView(v, vo, activity, urlMap, position);
			break;
		default:
			break;
		}
		return v;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		return StringUtil.isEmpty(getItem(position).imgurl) ? 0 : 1;
	}

}
