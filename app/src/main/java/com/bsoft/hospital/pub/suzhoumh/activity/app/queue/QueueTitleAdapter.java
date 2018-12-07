package com.bsoft.hospital.pub.suzhoumh.activity.app.queue;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.model.app.queue.QueueVo;

/**
 * 排队叫号列表Adapter
 * @author Administrator
 *
 */
public class QueueTitleAdapter extends BaseAdapter{

	private ArrayList<QueueVo> list;
	private Context context;
	
	public QueueTitleAdapter(Context context,ArrayList<QueueVo> list)
	{
		this.context = context;
		this.list = list;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	public void refresh(ArrayList<QueueVo> list)
	{
		this.list = list;
		this.notifyDataSetChanged();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if(convertView == null)
		{
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.queue_all_list_item, null);
			holder.tv_title = (TextView)convertView.findViewById(R.id.tv_title);
			holder.iv_arrow = (ImageView)convertView.findViewById(R.id.iv_arrow);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder)convertView.getTag();
		}
		holder.tv_title.setText(list.get(position).pmmc);
		return convertView;
	}
	
	
	class ViewHolder
	{
		TextView tv_title;
		ImageView iv_arrow;
		ListView lv_item;
	}
}