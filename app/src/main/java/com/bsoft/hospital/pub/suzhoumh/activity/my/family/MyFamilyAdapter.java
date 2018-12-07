package com.bsoft.hospital.pub.suzhoumh.activity.my.family;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.tanklib.util.StringUtil;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.app.appoint.AppointHistoryActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.app.appoint.AppointMainActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.app.report.ReportMainActivity;
import com.bsoft.hospital.pub.suzhoumh.model.my.MyFamilyVo;
import com.bsoft.hospital.pub.suzhoumh.util.IDCard;

public class MyFamilyAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<MyFamilyVo> list = new ArrayList<MyFamilyVo>();

	public MyFamilyAdapter(Context context,ArrayList<MyFamilyVo> list) {
		this.context = context;
		this.list = list;
	}

	public void refresh(ArrayList<MyFamilyVo> msgs) {
		if (null != msgs) {
			list = msgs;
		} else {
			list = new ArrayList<MyFamilyVo>();
		}
		notifyDataSetChanged();
	}

	public void activate(int index) {
		list.get(index).activated = 1;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public MyFamilyVo getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null)
		{
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.myfamily_list_item, null);
			holder.tv_name = (TextView)convertView.findViewById(R.id.tv_name);
			holder.tv_age = (TextView)convertView.findViewById(R.id.tv_age);
			holder.tv_phone = (TextView)convertView.findViewById(R.id.tv_phone);
			holder.tv_idcard = (TextView)convertView.findViewById(R.id.tv_idcard);
			holder.tv_sex = (TextView)convertView.findViewById(R.id.tv_sex);
			holder.ll_history = (LinearLayout)convertView.findViewById(R.id.ll_history);
			holder.ll_guahao = (LinearLayout)convertView.findViewById(R.id.ll_guahao);
			holder.ll_report = (LinearLayout)convertView.findViewById(R.id.ll_report);
			
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder)convertView.getTag();
		}
		holder.tv_name.setText(list.get(position).realname);
		holder.tv_idcard.setText(IDCard.getHideCardStr(list.get(position).idcard));
		holder.tv_phone.setText(IDCard.getHideMobileStr(list.get(position).mobile));
		holder.tv_age.setText(IDCard.getAge(list.get(position).idcard)+"岁");
		holder.tv_sex.setText(IDCard.getSex(list.get(position).idcard));
		
		holder.ll_history.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context,AppointHistoryActivity.class);
				intent.putExtra("familyVo", list.get(position));
				context.startActivity(intent);
			}
			
		});
		
		holder.ll_guahao.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context,AppointMainActivity.class);
				intent.putExtra("familyVo", list.get(position));
				context.startActivity(intent);
			}
			
		});
		
		holder.ll_report.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context,ReportMainActivity.class);
				intent.putExtra("familyVo", list.get(position));
				context.startActivity(intent);
			}
			
		});
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
		public TextView tv_name;
		public TextView tv_age;
		public TextView tv_sex;
		public TextView tv_idcard;
		public TextView tv_phone;
		public LinearLayout ll_history;
		public LinearLayout ll_guahao;
		public LinearLayout ll_report;
	}

	public static String text(String input) {
		if (null == input) {
			return "";
		}
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
