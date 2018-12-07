package com.bsoft.hospital.pub.suzhoumh.update;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import com.bsoft.hospital.pub.suzhoumh.BuildConfig;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.api.AppHttpClient;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * @author Tank E-mail:zkljxq@126.com
 * 
 * @类说明
 */
public class UpdateThread extends Thread {

	private Context mContext;
	private String mDownloadUrl; // 文件下载url，已做非空检查
	private String dir;
	private String mFileName = "m_new.apk";

	private NotificationManager mNotifManager;
	private Notification mDownNotification;
	private RemoteViews mContentView; // 下载进度View
	private PendingIntent mDownPendingIntent;

	public UpdateThread(Context context, String dir, UpdateInfo updateInfoVo) {
		this.mContext = context;
		this.mDownloadUrl = HttpApi.getUrl(updateInfoVo.appurl);
		this.dir = dir;
		this.mNotifManager = (NotificationManager) mContext
				.getSystemService(Context.NOTIFICATION_SERVICE);
	}

	@Override
	public void run() {
		try {
			if (null != this.dir) {
				File folder = new File(this.dir);
				if (!folder.exists()) {
					// 创建存放目录
					folder.mkdir();
				}
				File saveFilePath = new File(folder, mFileName);
				// if(!saveFilePath.exists()){
				// saveFilePath.createNewFile();
				// }
				mDownNotification = new Notification(R.drawable.ic_sllogo_update,
						mContext.getString(R.string.notif_down_file),
						System.currentTimeMillis());
				mDownNotification.flags = Notification.FLAG_ONGOING_EVENT;
				mDownNotification.flags = Notification.FLAG_AUTO_CANCEL;
				mContentView = new RemoteViews(mContext.getPackageName(),
						R.layout.update_notification);
				mContentView.setImageViewResource(R.id.downLoadIcon,
						R.drawable.ic_sllogo_update);
				mDownPendingIntent = PendingIntent.getActivity(mContext, 0,
						new Intent(), 0);
				boolean downSuc = downloadFile(mDownloadUrl, saveFilePath);
				if (downSuc) {
//					Notification notification = new Notification(
//							R.drawable.appicon,
//							mContext.getString(R.string.downloadSuccess),
//							System.currentTimeMillis());
//					notification.flags = Notification.FLAG_ONGOING_EVENT;
//					notification.flags = Notification.FLAG_AUTO_CANCEL;
//					Intent intent = new Intent(Intent.ACTION_VIEW);
//					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//					intent.setDataAndType(Uri.fromFile(saveFilePath),
//							"application/vnd.android.package-archive");
//					PendingIntent contentIntent = PendingIntent.getActivity(
//							mContext, 0, intent, 0);
//					notification.setLatestEventInfo(mContext,
//							mContext.getString(R.string.downloadSuccess), null,
//							contentIntent);
//					mNotifManager.notify(R.drawable.appicon, notification);
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					Uri uri;
					if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
						uri = FileProvider.getUriForFile(mContext,
								BuildConfig.APPLICATION_ID + ".provider.FileProvider", saveFilePath);
						intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
					}else{
						uri = Uri.fromFile(saveFilePath);
					}
					intent.setDataAndType(uri,
							"application/vnd.android.package-archive");
					List<ResolveInfo> resInfoList = mContext.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
					for (ResolveInfo resolveInfo : resInfoList) {
						String packageName = resolveInfo.activityInfo.packageName;
						mContext.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
					}
					PendingIntent pendingIntent = PendingIntent.getActivity(
							mContext, 0, intent, 0);

					Notification notification = new Notification.Builder(mContext)
							.setAutoCancel(true)
							.setOngoing(true)
							.setContentTitle(mContext.getString(R.string.downloadSuccess))
							.setContentIntent(pendingIntent)
							.setSmallIcon(R.drawable.ic_sllogo_update)
							.setWhen(System.currentTimeMillis()).setTicker(mContext.getString(R.string.downloadSuccess))
							.build();
					mNotifManager.notify(R.drawable.ic_sllogo_update, notification);
				} else {
//					Notification notification = new Notification(
//							R.drawable.appicon,
//							mContext.getString(R.string.downloadFailure),
//							System.currentTimeMillis());
//					notification.flags = Notification.FLAG_AUTO_CANCEL;
//					PendingIntent contentIntent = PendingIntent.getActivity(
//							mContext, 0, new Intent(), 0);
//					notification.setLatestEventInfo(mContext,
//							mContext.getString(R.string.downloadFailure), null,
//							contentIntent);
//					mNotifManager.notify(R.drawable.appicon, notification);
					PendingIntent pendingIntent = PendingIntent.getActivity(
							mContext, 0, new Intent(), 0);

					Notification notification = new Notification.Builder(mContext)
							.setAutoCancel(true)
							.setContentTitle(mContext.getString(R.string.downloadFailure))
							.setContentIntent(pendingIntent)
							.setSmallIcon(R.drawable.ic_sllogo_update)
							.setWhen(System.currentTimeMillis()).setTicker( mContext.getString(R.string.downloadFailure))
							.build();
					mNotifManager.notify(R.drawable.ic_sllogo_update, notification);
				}

			} else {
				Toast.makeText(mContext, "SD卡不可用！", Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * Desc:文件下载
	 * 
	 * @param downloadUrl
	 *            下载URL
	 * @param saveFilePath
	 *            保存文件路径
	 * @return ture:下载成功 false:下载失败
	 */
	public boolean downloadFile(String downloadUrl, File saveFilePath) {
		double fileSize = -1;
		int downFileSize = 0;
		boolean result = false;
		int progress = 0;
		HttpResponse response;
		try {
			HttpGet get = new HttpGet(downloadUrl);
			response = new AppHttpClient().executeHttpRequest(get);
			HttpEntity entity = response.getEntity();
			if (response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK) {
				fileSize = entity.getContentLength();
				InputStream is = entity.getContent();
				FileOutputStream fos = new FileOutputStream(saveFilePath);
				byte[] buffer = new byte[1024 * 16];
				int i = 0;
				int tempProgress = -1;
				while ((i = is.read(buffer)) != -1) {
					downFileSize = downFileSize + i;
					// 下载进度
					progress = (int) (downFileSize * 100.0 / fileSize);
					fos.write(buffer, 0, i);

					synchronized (this) {
						if (downFileSize == fileSize) {
							// 下载完成
							mNotifManager.cancel(R.id.downLoadIcon);
						} else if (tempProgress != progress) {
							// 下载进度发生改变，则发送Message
							mContentView.setTextViewText(R.id.progressPercent,
									progress + "%");
							mContentView.setProgressBar(R.id.downLoadProgress,
									100, progress, false);
							mDownNotification.contentView = mContentView;
							mDownNotification.contentIntent = mDownPendingIntent;
							mNotifManager.notify(R.id.downLoadIcon,
									mDownNotification);
							tempProgress = progress;
							// 防止过快更新造成的卡顿
							Thread.sleep(200);
						}
					}
				}
				fos.flush();
				fos.close();
				is.close();
				result = true;
			} else {
				result = false;
			}
		} catch (Exception e) {
			result = false;
			e.printStackTrace();
		}
		return result;
	}

	// /**
	// *
	// * Desc:文件下载
	// *
	// * @param downloadUrl
	// * 下载URL
	// * @param saveFilePath
	// * 保存文件路径
	// * @return ture:下载成功 false:下载失败
	// */
	// public boolean downloadFile(String downloadUrl, File saveFilePath) {
	// int fileSize = -1;
	// int downFileSize = 0;
	// boolean result = false;
	// int progress = 0;
	// try {
	// URL newUrl = new URL(downloadUrl);
	// HttpURLConnection conn = (HttpURLConnection) newUrl.openConnection();
	// if (null == conn) {
	// return false;
	// }
	// // 读取超时时间 毫秒级
	// conn.setReadTimeout(10000);
	// conn.setRequestMethod("GET");
	// conn.setDoInput(true);
	// conn.connect();
	// if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
	// fileSize = conn.getContentLength();
	// InputStream is = conn.getInputStream();
	// FileOutputStream fos = new FileOutputStream(saveFilePath);
	// byte[] buffer = new byte[1024 * 16];
	// int i = 0;
	// int tempProgress = -1;
	// while ((i = is.read(buffer)) != -1) {
	// downFileSize = downFileSize + i;
	// // 下载进度
	// progress = (int) (downFileSize * 100.0 / fileSize);
	// fos.write(buffer, 0, i);
	//
	// synchronized (this) {
	// if (downFileSize == fileSize) {
	// // 下载完成
	// mNotifManager.cancel(R.id.downLoadIcon);
	// } else if (tempProgress != progress) {
	// // 下载进度发生改变，则发送Message
	// mContentView.setTextViewText(R.id.progressPercent,
	// progress + "%");
	// mContentView.setProgressBar(R.id.downLoadProgress,
	// 100, progress, false);
	// mDownNotification.contentView = mContentView;
	// mDownNotification.contentIntent = mDownPendingIntent;
	// mNotifManager.notify(R.id.downLoadIcon,
	// mDownNotification);
	// tempProgress = progress;
	// // 防止过快更新造成的卡顿
	// Thread.sleep(200);
	// }
	// }
	// }
	// fos.flush();
	// fos.close();
	// is.close();
	// result = true;
	// } else {
	// result = false;
	// }
	// } catch (Exception e) {
	// result = false;
	// e.printStackTrace();
	// }
	// return result;
	// }

}
