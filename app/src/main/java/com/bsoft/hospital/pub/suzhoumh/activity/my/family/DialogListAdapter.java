package com.bsoft.hospital.pub.suzhoumh.activity.my.family;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bsoft.hospital.pub.suzhoumh.R;

/**
 * 有列表显示的对话框
 * @author Administrator
 *
 */
public class DialogListAdapter extends BaseAdapter
{

	private List<String> m_titles;
	private int m_position;
	private Context m_context;
	
	public DialogListAdapter(Context context,List<String> titles,int position)
	{
		m_titles = titles;
		m_position = position;
		m_context = context;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return m_titles.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return m_titles.get(position);
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
			convertView = LayoutInflater.from(m_context).inflate(R.layout.dialog_list_item, null);
			holder.tv_title = (TextView)convertView.findViewById(R.id.tv_title);
			holder.iv_select = (ImageView)convertView.findViewById(R.id.iv_select);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder)convertView.getTag();
		}
		if(m_position == position)
		{
			holder.iv_select.setVisibility(View.VISIBLE);
		}
		else
		{
			holder.iv_select.setVisibility(View.GONE);
		}
		holder.tv_title.setText(m_titles.get(position));
		
		return convertView;
	}
	
	class ViewHolder
	{
		TextView tv_title;
		ImageView iv_select;
	}
}
