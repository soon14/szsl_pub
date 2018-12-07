package com.bsoft.hospital.pub.suzhoumh.activity.app.monitor;

import com.bsoft.hospital.pub.suzhoumh.R;

public class MonitorUtils {

	public static int[] ids  = new int[]{R.drawable.icon_monitor_xy_big,R.drawable.icon_monitor_xt_big,R.drawable.icon_monitor_tz_big,R.drawable.icon_monitor_sg_big,
			R.drawable.icon_monitor_xd_big,R.drawable.icon_monitor_xl_big,
			R.drawable.icon_monitor_yw_big,R.drawable.icon_monitor_xd_big,
			R.drawable.icon_monitor_xyang_big};
	
	public static int getImageId(int id)
	{
		return ids[id-1];
	}
	
}
