package com.bsoft.hospital.pub.suzhoumh.activity.app.queue;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.model.app.queue.PDQKVo;

/**
 * 排队叫号详情列表
 * @author Administrator
 *
 */
public class QueueDetailAdapter extends BaseAdapter{

	public ArrayList<PDQKVo> list;
	private Context context;
	private int state = 0;//0是所有排队情况,1是我的排队情况
	public QueueDetailAdapter(Context context,ArrayList<PDQKVo> list,int state)
	{
		this.context = context;
		this.list = list;
		this.state = state;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}
	
	public void refresh(ArrayList<PDQKVo> list)
	{
		this.list = list;
		this.notifyDataSetChanged();
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if(convertView == null)
		{
			convertView = LayoutInflater.from(context).inflate(R.layout.queue_detail_item,null);
			holder = new ViewHolder();
			holder.tv_ksmc = (TextView)convertView.findViewById(R.id.tv_ksmc);
			holder.tv_dqxh = (TextView)convertView.findViewById(R.id.tv_dqxh);
			holder.tv_syhys = (TextView)convertView.findViewById(R.id.tv_syhys);
			holder.ll_my_queue = (LinearLayout)convertView.findViewById(R.id.ll_my_queue);
			holder.tv_wdxh = (TextView)convertView.findViewById(R.id.tv_wdxh);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder)convertView.getTag();
		}
		
		if(state == 1)
		{
			holder.ll_my_queue.setVisibility(View.VISIBLE);
			holder.tv_wdxh.setText(list.get(position).wdxh);
		}
		else
		{
			holder.ll_my_queue.setVisibility(View.GONE);
		}
		
		holder.tv_ksmc.setText(list.get(position).ksmc);
		holder.tv_dqxh.setText(list.get(position).dqxh); 
		holder.tv_syhys.setText(list.get(position).syhys);
		return convertView;
	}

	class ViewHolder
	{
		TextView tv_ksmc;
		TextView tv_dqxh;
		TextView tv_syhys;
		TextView tv_wdxh;
		LinearLayout ll_my_queue;
	}
}
