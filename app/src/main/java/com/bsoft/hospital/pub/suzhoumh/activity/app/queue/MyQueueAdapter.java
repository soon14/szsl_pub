package com.bsoft.hospital.pub.suzhoumh.activity.app.queue;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.AppApplication;
import com.bsoft.hospital.pub.suzhoumh.model.app.queue.PDQKVo;

public class MyQueueAdapter extends BaseAdapter{

	private ArrayList<PDQKVo> my_list;
	private Context context;
	
	public MyQueueAdapter(Context context,ArrayList<PDQKVo> list)
	{
		this.my_list = list;
		this.context = context;
	}
	
	public void refresh(ArrayList<PDQKVo> list)
	{
		this.my_list = list;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return my_list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return my_list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if(convertView == null)
		{
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.queue_my_list_item, null);
			holder.tv_current_num = (TextView)convertView.findViewById(R.id.tv_current_num);
			holder.tv_my_num = (TextView)convertView.findViewById(R.id.tv_my_num);
			holder.tv_dept = (TextView)convertView.findViewById(R.id.tv_dept);
			holder.tv_name = (TextView)convertView.findViewById(R.id.tv_name);
			holder.tv_shengyu_num = (TextView)convertView.findViewById(R.id.tv_shenyu_num);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder)convertView.getTag();
		}
		holder.tv_current_num.setText(my_list.get(position).dqxh);
		holder.tv_my_num.setText(my_list.get(position).wdxh);
		holder.tv_dept.setText(my_list.get(position).ksmc);
		if(new AppApplication().loginUser.realname!=null)
		{
			holder.tv_name.setText(new AppApplication().loginUser.realname);
		}
		holder.tv_shengyu_num.setText(my_list.get(position).syhys);
		return convertView;
	}
	
	class ViewHolder
	{
		TextView tv_current_num;
		TextView tv_my_num;
		TextView tv_dept;
		TextView tv_name;
		TextView tv_shengyu_num;
	}

}
