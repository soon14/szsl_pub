package com.bsoft.hospital.pub.suzhoumh.activity.my.info;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.activity.adapter.MyInfoAddressProvinceAdapter;
import com.bsoft.hospital.pub.suzhoumh.activity.base.BaseActivity;
import com.bsoft.hospital.pub.suzhoumh.cache.CityCache;
import com.bsoft.hospital.pub.suzhoumh.model.my.CityCode;
import com.app.tanklib.view.BsoftActionBar.Action;

/**
 * @author Tank E-mail:zkljxq@126.com
 * 
 * @类说明
 */
public class MyInfoAddressChooseActivity extends BaseActivity {

	MyInfoAddressProvinceAdapter adapter;
	ListView listView;
	ProgressBar emptyProgress;
	CityCode city1, city2, city3;

	int step = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.base_list);
		findView();
		setClick();
	}

	void setClick() {
		adapter = new MyInfoAddressProvinceAdapter(this);
		adapter.addData(CityCache.getInstance().cityList);
		listView.setAdapter(adapter);
	}

	@Override
	public void findView() {
		findActionBar();
		actionBar.setTitle("省选择");
		actionBar.setBackAction(new Action() {
			@Override
			public void performAction(View view) {
				switch (step) {
				case 1:
					back();
					break;
				case 2:
					adapter.addData(CityCache.getInstance().cityList);
					actionBar.setTitle("省选择");
					step--;
					break;
				case 3:
					adapter.addData(CityCache.getInstance().cityMap1
							.get(city1.ID));
					actionBar.setTitle(city1.Title);
					step--;
					break;
				default:
					break;
				}

			}

			@Override
			public int getDrawable() {
				return R.drawable.btn_back;
			}
		});
		listView = (ListView) findViewById(R.id.listView);
		emptyProgress = (ProgressBar) findViewById(R.id.emptyProgress);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				switch (step) {
				case 1:
					city1 = adapter.getItem(position);
					actionBar.setTitle(city1.Title);
					adapter.addData(CityCache.getInstance().cityMap1
							.get(adapter.getItem(position).ID));
					step++;
					break;
				case 2:
					city2 = adapter.getItem(position);
					actionBar.setTitle(city1.Title + ""
							+ adapter.getItem(position).Title);
					adapter.addData(CityCache.getInstance().cityMap2
							.get(adapter.getItem(position).ID));
					step++;
					break;
				case 3:
					city3 = adapter.getItem(position);
					Intent intent = new Intent(Constants.MyAddress_ACTION);
					intent.putExtra("type", 1);
					intent.putExtra("city1", city1);
					intent.putExtra("city2", city2);
					intent.putExtra("city3", city3);
					sendBroadcast(intent);
					back();
					break;
				default:
					break;
				}
			}
		});
	}

}
