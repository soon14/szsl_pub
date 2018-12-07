package com.bsoft.hospital.pub.suzhoumh.activity.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * @author Tank E-mail:zkljxq@126.com
 * 
 * @类说明
 */
public class TouristsAdapter extends FragmentPagerAdapter {

	private List<Fragment> fragments = new ArrayList<Fragment>();

	public TouristsAdapter(FragmentManager fm) {
		super(fm);
	}

	public void addItem(Fragment fragment) {
		fragments.add(fragment);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

	@Override
	public Fragment getItem(int position) {
		return fragments.get(position);
	}
}
