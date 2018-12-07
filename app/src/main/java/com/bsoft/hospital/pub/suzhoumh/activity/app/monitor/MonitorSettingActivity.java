package com.bsoft.hospital.pub.suzhoumh.activity.app.monitor;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.tanklib.dialog.AlertDialogWithButton;
import com.app.tanklib.view.BsoftActionBar.Action;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.model.moitor.MonitorSetting;

/**
 * 
 * @author Administrator
 * 
 */
public class MonitorSettingActivity extends BaseActivity {

	ListView listView;
	MonitorSettingAdapter adapter;
	ProgressBar emptyProgress;
	LayoutInflater mLayoutInflater;
	ArrayList<MonitorSetting> datas;
	AlertDialogWithButton dialog;
	boolean firstLoad = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.base_list);
		this.mLayoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		datas = application.getMonitorSetting();
		if (null == datas) {
			finish();
			return;
		}
		findView();
	}

	@Override
	public void findView() {
		findActionBar();
		actionBar.setTitle("健康监测");
		actionBar.setBackAction(new Action() {

			@Override
			public void performAction(View view) {
				back();
			}

			@Override
			public int getDrawable() {
				return R.drawable.btn_back;
			}
		});
		emptyProgress = (ProgressBar) findViewById(R.id.emptyProgress);
		listView = (ListView) findViewById(R.id.listView);
		adapter = new MonitorSettingAdapter();
		listView.setAdapter(adapter);
	}

	class MonitorSettingAdapter extends BaseAdapter {

		public MonitorSettingAdapter() {
		}

		@Override
		public int getCount() {
			return datas.size();
		}

		@Override
		public MonitorSetting getItem(int position) {
			return datas.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parentViewGroup) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mLayoutInflater.inflate(
						R.layout.monitor_settng_item, null);
				holder.icon = (ImageView) convertView.findViewById(R.id.icon);
				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.flag = (TextView) convertView.findViewById(R.id.flag);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final MonitorSetting vo = getItem(position);
			holder.name.setText(vo.name);
			holder.icon.setImageResource(MonitorUtils.getImageId(vo.id));
			if (vo.flag == 1) {
				holder.flag.setText("取消");
				holder.flag
						.setTextColor(getResources().getColor(R.color.black));
				holder.flag
						.setBackgroundResource(R.drawable.bigbut_white_linegray);
			} else {
				holder.flag.setText("添加");
				holder.flag
						.setTextColor(getResources().getColor(R.color.white));
				holder.flag.setBackgroundResource(R.drawable.bigbut_green);
			}
			holder.flag.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					if (vo.flag == 1) {
						// 选中状态

						dialog = new AlertDialogWithButton(baseContext)
								.build(false, getDialogWidth())
								.message("确定不再使用该监测工具？")
								.color(R.color.actionbar_bg)
								.setPositiveButton("取消", new OnClickListener() {
									@Override
									public void onClick(View v) {
										dialog.dismiss();
									}
								})
								.setNegativeButton("确定", new OnClickListener() {
									@Override
									public void onClick(View v) {
										application.changeMonitor(position,
												0);
										sendBroadcast(new Intent(
												Constants.MonitorChange_ACTION));
										notifyDataSetChanged();
										dialog.dismiss();
									}
								});
						dialog.show();

					} else {
						application.changeMonitor(position, 1);
						sendBroadcast(new Intent(Constants.MonitorChange_ACTION));
						notifyDataSetChanged();
					}
				}
			});
			return convertView;
		}

		public class ViewHolder {
			public TextView name, flag;
			public ImageView icon;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
