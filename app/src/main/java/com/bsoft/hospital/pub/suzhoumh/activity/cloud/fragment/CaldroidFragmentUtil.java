package com.bsoft.hospital.pub.suzhoumh.activity.cloud.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.bsoft.calendar.caldroid.CaldroidFragment;
import com.bsoft.hospital.pub.suzhoumh.R;

import java.util.Calendar;

/**
 * @author :lizhengcao
 * @date :2019/1/15
 * E-mail:lizc@bsoft.com.cn
 * @类说明 日历fragment的相关工具类
 */
public class CaldroidFragmentUtil {

    /**
     * 日历控件实现
     */
    public static void caldroidFragmentgetInstance(CaldroidFragment caldroidFragment) {
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
        args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);
        caldroidFragment.setArguments(args);
    }

}
