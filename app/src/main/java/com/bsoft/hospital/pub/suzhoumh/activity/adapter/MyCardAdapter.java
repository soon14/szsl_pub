package com.bsoft.hospital.pub.suzhoumh.activity.adapter;

import java.util.ArrayList;
import java.util.List;

import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.model.my.MyCardVo;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyCardAdapter extends BaseAdapter {
	private LayoutInflater mLayoutInflater;
	private List<MyCardVo> dataList = new ArrayList<MyCardVo>();
	Activity baseActivity;

	public MyCardAdapter(Activity baseActivity) {
		this.baseActivity = baseActivity;
		this.mLayoutInflater = (LayoutInflater) baseActivity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
	}
	
	

	public void addData(ArrayList<MyCardVo> msgs) {
		if (null != msgs) {
			dataList = msgs;
		} else {
			dataList = new ArrayList<MyCardVo>();
		}
		notifyDataSetChanged();
	}

	public void addMoreData(ArrayList<MyCardVo> msgs) {
		if (null != msgs) {
			dataList.addAll(msgs);
		}
		notifyDataSetChanged();
	}
	
	public void addData(MyCardVo vo) {
		dataList.add(0, vo);
		notifyDataSetChanged();
	}
	
	public void changeData(MyCardVo vo) {
		int i=dataList.indexOf(vo);
		if(-1!=i){
			dataList.set(i, vo);
			notifyDataSetChanged();
		}
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
	public MyCardVo getItem(int position) {
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
			convertView = mLayoutInflater.inflate(R.layout.mycard_item, null);
			holder.cardtext = (TextView) convertView.findViewById(R.id.cardtext);
			holder.address = (TextView) convertView.findViewById(R.id.address);
			holder.cardnum = (TextView) convertView.findViewById(R.id.cardnum);
			holder.mainView = (RelativeLayout) convertView.findViewById(R.id.mainView);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		MyCardVo item = dataList.get(position);
		holder.cardtext.setText(item.cardType==1?"医保卡":"就诊卡");
		holder.address.setText(item.belongName);
		holder.cardnum.setText("NO "+item.cardNum);
		if(item.cardType==1){
			holder.mainView.setBackgroundResource(R.drawable.card1_bg);
		}else{
			holder.mainView.setBackgroundResource(R.drawable.card2_bg);
		}

		return convertView;
	}

	public static class ViewHolder {
		public TextView cardtext, address, cardnum;
		public RelativeLayout mainView;
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
