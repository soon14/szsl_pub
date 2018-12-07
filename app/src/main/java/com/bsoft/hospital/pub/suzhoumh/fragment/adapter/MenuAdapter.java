package com.bsoft.hospital.pub.suzhoumh.fragment.adapter;

import java.util.ArrayList;

import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.model.MemuVo;



import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuAdapter extends BaseAdapter {

	Context activity;
	LayoutInflater inflater;
	ArrayList<MemuVo> list;
	private boolean isPressed[];

	public MenuAdapter(Activity activity,ArrayList<MemuVo> list) {
		this.activity=activity;
		this.inflater = LayoutInflater.from(activity);
		this.list = list;
		this.isPressed = new boolean[list.size()];
		this.isPressed[0]=true;
	}
	

	public void changeState(int position) {
		for (int i = 0; i < getCount(); i++) {
			isPressed[i] = false;
		}
		isPressed[position] = true;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return this.list.size();
	}

	@Override
	public MemuVo getItem(int position) {
		return this.list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ListItemsView listItemsView;
		if (convertView == null) {
			listItemsView = new ListItemsView();
			convertView = this.inflater.inflate(R.layout.menu_item, null);
			listItemsView.menuIcon = (ImageView) convertView
					.findViewById(R.id.menuIcon);
			listItemsView.menuText = (TextView) convertView
					.findViewById(R.id.menuText);
			convertView.setTag(listItemsView);
		} else {
			listItemsView = (ListItemsView) convertView.getTag();
		}

		listItemsView.menuIcon.setImageResource(getItem(position).headIconId);
		listItemsView.menuText.setText(getItem(position).name);
		
		if (this.isPressed[position] == true) {
			convertView.setBackgroundColor(activity.getResources().getColor(R.color.menu_select));
		} else {
			convertView.setBackgroundColor(Color.TRANSPARENT);
		}
		return convertView;
	}

	public final class ListItemsView {
		public ImageView menuIcon;
		public TextView menuText;
	}


}
