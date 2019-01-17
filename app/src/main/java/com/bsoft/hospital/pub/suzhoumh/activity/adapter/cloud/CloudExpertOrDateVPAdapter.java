package com.bsoft.hospital.pub.suzhoumh.activity.adapter.cloud;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * @author :lizhengcao
 * @date :2019/1/11
 * E-mail:lizc@bsoft.com.cn
 * @类说明 选专家OR选日期的viewpager/选择简介OR排班表
 */
public class CloudExpertOrDateVPAdapter extends FragmentPagerAdapter {

    private List<Fragment> frags;
    private String[] titles;

    public CloudExpertOrDateVPAdapter(FragmentManager fm, List<Fragment> frags, String[] titles) {
        super(fm);
        this.frags = frags;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return frags.get(position);
    }

    @Override
    public int getCount() {
        return frags == null ? 0 : frags.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
