package com.bsoft.hospital.pub.suzhoumh.activity.app.healthtool;

/* import相关class */
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.app.tanklib.Preferences;
import com.bsoft.hospital.pub.suzhoumh.R;

/* 调用闹钟Alert的Receiver */
public class CallAlarmReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		System.out.println("CallAlarm ..........................");
		/* create Intent，调用AlarmAlert.class */
		// Intent i = new Intent(context, AlarmAlert.class);
		//
		// Bundle bundleRet = new Bundle();
		// bundleRet.putString("STR_CALLER", "");
		// i.putExtras(bundleRet);
		// i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// context.startActivity(i);

		//String clock_parent_id=intent.getStringExtra("clock_parent_id");
		String clock_child_id=intent.getStringExtra("clock_child_id");
		String strDays=Preferences.getInstance().getStringData("clock_"+clock_child_id+"_days");
		int days = 0;
		if(strDays!=null&&!strDays.equals(""))
		{
			days = Integer.parseInt(strDays);
		}
		if(days>0)
		{
			
//		CharSequence title = "用药提醒";
//		int icon = R.drawable.appicon;
//		long when = System.currentTimeMillis();
//		Notification noti = new Notification(icon, title, when);
//		noti.flags = Notification.FLAG_INSISTENT;
//
//		// 创建一个通知
//		Notification mNotification = new Notification();
//
//		// 设置属性值
//		mNotification.icon = R.drawable.appicon;
//		mNotification.tickerText = "你用药的时间到了，以免影响你的病情！";
//		mNotification.when = System.currentTimeMillis(); // 立即发生此通知

		// 带参数的构造函数,属性值如上
		// Notification mNotification = = new
		// Notification(R.drawable.icon,"NotificationTest",
		// System.currentTimeMillis()));

		// 添加声音效果
//		mNotification.defaults |= Notification.DEFAULT_SOUND;
//		mNotification.defaults |= Notification.DEFAULT_VIBRATE ;

		// 添加震动,后来得知需要添加震动权限 : Virbate Permission
		// mNotification.defaults |= Notification.DEFAULT_VIBRATE ;

		// 添加状态标志

		// FLAG_AUTO_CANCEL 该通知能被状态栏的清除按钮给清除掉
		// FLAG_NO_CLEAR 该通知能被状态栏的清除按钮给清除掉
		// FLAG_ONGOING_EVENT 通知放置在正在运行
		// FLAG_INSISTENT 通知的音乐效果一直播放
		//mNotification.flags = Notification.FLAG_INSISTENT;
//		mNotification.flags = Notification.FLAG_AUTO_CANCEL;
		// 将该通知显示为默认View
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				new Intent(context,MedicineRemindActivity.class), 0);
//		mNotification.setLatestEventInfo(context, "用药提醒", "你用药的时间到了，以免影响你的病情！",
//				contentIntent);

			Notification notify3 = new Notification.Builder(context)
					.setSmallIcon( R.drawable.ic_sllogo_update)
					.setTicker("你用药的时间到了，以免影响你的病情！")
					.setContentTitle("用药提醒")
					.setContentText("你用药的时间到了，以免影响你的病情！")
					.setContentIntent(contentIntent).setNumber(1).build(); // 需要注意build()是在API
			notify3.defaults |= Notification.DEFAULT_SOUND;
			notify3.defaults |= Notification.DEFAULT_VIBRATE ;
			// level16及之后增加的，API11可以使用getNotificatin()来替代
			notify3.flags |= Notification.FLAG_AUTO_CANCEL; // FLAG_AUTO_CANCEL表明当通知被用户点击时，通知将被清除。
//			manager.notify(NOTIFICATION_FLAG, notify3);// 步骤4：通过通知管理器来发起通知。如果id不同，则每click，在status哪里增加一个提示

		// 设置setLatestEventInfo方法,如果不设置会App报错异常
		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		// 注册此通知
		// 如果该NOTIFICATION_ID的通知已存在，会显示最新通知的相关信息 ，比如tickerText 等
		mNotificationManager.notify(999, notify3);
		
		days--;
		Preferences.getInstance().setStringData("clock_"+clock_child_id+"_days",String.valueOf(days));
		}
		else
		{
			Intent deleteclock = new Intent(context, CallAlarmReceiver.class);
			deleteclock.setAction("com.bsoft.hospital.pub.medicineremind");
			PendingIntent sender = PendingIntent.getBroadcast(
					context, Integer.parseInt(clock_child_id), deleteclock, 0);
			/* 由AlarmManager中删除 */
			AlarmManager am;
			am = (AlarmManager)context.getSystemService("alarm");
			am.cancel(sender);
		}
	}
}
