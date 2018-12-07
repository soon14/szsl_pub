package com.bsoft.hospital.pub.suzhoumh.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Tank on 2015/12/7.
 */
public class NotificationUtil {

    public static void notif(Context context, int appicon, String title, String des, Intent intent) {
        notif(context, appicon, title, des, intent, 11111);
    }


    public static void notif(Context context, int appicon, String title, String des, Intent intent,int notifId) {
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, notifId, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(context)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(des)
                .setContentIntent(pendingIntent)
                .setSmallIcon(appicon)
                .setWhen(System.currentTimeMillis()).setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE).setTicker(des)
                .build();
        notificationManager.notify(notifId, notification);

    }


}
