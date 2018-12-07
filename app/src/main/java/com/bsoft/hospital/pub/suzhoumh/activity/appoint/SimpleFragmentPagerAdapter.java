package com.bsoft.hospital.pub.suzhoumh.activity.appoint;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * @author :lizhengcao
 * @date :2017/3/7
 * E-mail:lizc@bsoft.com.cn
 * @类说明
 */

public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragsList;
    private String[] titles;

    public SimpleFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragsList, String[] titles) {
        super(fm);
        this.fragsList = fragsList;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return fragsList.get(position);
    }

    @Override
    public int getCount() {
        return fragsList == null ? 0 : fragsList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
