package com.bsoft.hospital.pub.suzhoumh.push;

import java.util.List;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.app.tanklib.Preferences;
import com.app.tanklib.http.BsoftNameValuePair;
import com.baidu.android.pushservice.PushMessageReceiver;
import com.bsoft.hospital.pub.suzhoumh.AppApplication;
import com.bsoft.hospital.pub.suzhoumh.Constants;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.activity.LoadingActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.MainTabActivity;
import com.bsoft.hospital.pub.suzhoumh.activity.account.LoginActivity;
import com.bsoft.hospital.pub.suzhoumh.api.HttpApi;
import com.bsoft.hospital.pub.suzhoumh.model.LoginUser;
import com.bsoft.hospital.pub.suzhoumh.util.NotificationUtil;

/**
 * Push消息处理receiver。请编写您需要的回调函数， 一般来说： onBind是必须的，用来处理startWork返回值；
 * onMessage用来接收透传消息； onSetTags、onDelTags、onListTags是tag相关操作的回调；
 * onNotificationClicked在通知被点击时回调； onUnbind是stopWork接口的返回值回调
 * 
 * 返回值中的errorCode，解释如下： 0 - Success 10001 - Network Problem 30600 - Internal
 * Server Error 30601 - Method Not Allowed 30602 - Request Params Not Valid
 * 30603 - Authentication Failed 30604 - Quota Use Up Payment Required 30605 -
 * Data Required Not Found 30606 - Request Time Expires Timeout 30607 - Channel
 * Token Timeout 30608 - Bind Relation Not Found 30609 - Bind Number Too Many
 * 
 * 当您遇到以上返回错误时，如果解释不了您的问题，请用同一请求的返回值requestId和errorCode联系我们追查问题。
 * 
 */
public class MyPushMessageReceiver extends PushMessageReceiver {
	/** TAG to Log */
	public static final String TAG = MyPushMessageReceiver.class.getSimpleName();

	/**
	 * 调用PushManager.startWork后，sdk将对push
	 * server发起绑定请求，这个过程是异步的。绑定请求的结果通过onBind返回。 如果您需要用单播推送，需要把这里获取的channel
	 * id和user id上传到应用server中，再调用server接口用channel id和user id给单个手机或者用户推送。
	 * 
	 * @param context
	 *            BroadcastReceiver的执行Context
	 * @param errorCode
	 *            绑定接口返回值，0 - 成功
	 * @param appid
	 *            应用id。errorCode非0时为null
	 * @param userId
	 *            应用user id。errorCode非0时为null
	 * @param channelId
	 *            应用channel id。errorCode非0时为null
	 * @param requestId
	 *            向服务端发起的请求id。在追查问题时有用；
	 * @return none
	 */
	@Override
	public void onBind(Context context, int errorCode, String appid,
			String userId, String channelId, String requestId) {
		// 绑定成功，设置已绑定flag，可以有效的减少不必要的绑定请求
		String responseString = "onBind errorCode=" + errorCode + " appid="
                + appid + " userId=" + userId + " channelId=" + channelId
                + " requestId=" + requestId;
        Log.d(TAG, responseString);
		Log.i("baidupush", responseString);
		if (errorCode == 0) {
			Preferences.getInstance().setStringData("userId", userId);
			Preferences.getInstance().setStringData("channelId", channelId);
			final LoginUser loginUser = ((AppApplication)context.getApplicationContext()).getLoginUser();
//			System.out.println("loginUser=="+loginUser);
			//if(!ContextUtils.hasBind(context))//没有绑定就绑定数据到我们服务器
			//{
				if(loginUser!=null)
				{
					Utils.setBind(context, true);
					new Thread()
					{
						@Override
						public void run() {
							HttpApi.getInstance().post(
									"auth/device",
									new BsoftNameValuePair("userId", Preferences.getInstance()
											.getStringData("userId")),
									new BsoftNameValuePair("channelId", Preferences
											.getInstance().getStringData("channelId")),
									new BsoftNameValuePair("deviceId", Preferences
											.getInstance().getStringData("deviceId")),
									new BsoftNameValuePair("type", "2"),
									new BsoftNameValuePair("id", loginUser.id),
									new BsoftNameValuePair("sn", loginUser.sn));
							Log.i("baidupush", "pushmessagereceiver->提交数据到服务器");
						}
					}.start();
				}
			//}
		}
	}

	/**
	 * 接收透传消息的函数。
	 * 
	 * @param context
	 *            上下文
	 * @param message
	 *            推送的消息
	 * @param customContentString
	 *            自定义内容,为空或者json字符串
	 */
	@Override
	public void onMessage(Context context, String message,
			String customContentString) {
		String messageString = "透传消息 message=\"" + message
				+ "\" customContentString=" + customContentString;
		//Log.d(TAG, messageString);
		System.out.println("messageString:"+messageString);

		if (!TextUtils.isEmpty(message)) {
			try {
				PushInfo info = new PushInfo();
				info.buideJson(new org.json.JSONObject(message));
				// 判断是否运行在前台
				if (!isRunningForeground(context)) {
					if (info.kinds == 200) {
						// 退出登录，清除用户信息
						Preferences.getInstance().setStringData("loginUser",
								null);
					}

					Intent intent = new Intent(context, LoadingActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
					intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
					intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					NotificationUtil.notif(context, R.drawable.ic_sllogo_update, info.title, info.description, intent);
				} else {
					// 1 消息 2 事件 3 消息+事件
					Intent messIntent = null;
					Intent homeIntent = null;
					switch (info.kinds) {
					case 1:
						AppApplication.messageCount = AppApplication.messageCount + 1;
						context.sendBroadcast(new Intent(
								Constants.HomeMessageCount_ACTION));
						messIntent = new Intent(Constants.PushMessage_ACTION);
						messIntent.putExtra("pushInfo", info);
						context.sendBroadcast(messIntent);
						break;
					case 2:
						homeIntent = new Intent(Constants.PushHome_ACTION);
						homeIntent.putExtra("pushInfo", info);
						context.sendBroadcast(homeIntent);
						break;
					case 3:
						AppApplication.messageCount = AppApplication.messageCount + 1;
						context.sendBroadcast(new Intent(
								Constants.HomeMessageCount_ACTION));
						messIntent = new Intent(Constants.PushMessage_ACTION);
						messIntent.putExtra("pushInfo", info);
						context.sendBroadcast(messIntent);
						homeIntent = new Intent(Constants.PushHome_ACTION);
						homeIntent.putExtra("pushInfo", info);
						context.sendBroadcast(homeIntent);

						break;
					case 200:
						// 单点登录
						Intent logoutIntent = new Intent(
								Constants.Logout_ACTION);

						logoutIntent.putExtra("pushInfo", info);
						context.sendBroadcast(logoutIntent);
						context.sendBroadcast(new Intent(Constants.CLOSE_ACTION));
						break;
					default:
						break;
					}
				}
			} catch (org.json.JSONException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 接收通知点击的函数。注：推送通知被用户点击前，应用无法通过接口获取通知的内容。
	 * 
	 * @param context
	 *            上下文
	 * @param title
	 *            推送的通知的标题
	 * @param description
	 *            推送的通知的描述
	 * @param customContentString
	 *            自定义内容，为空或者json字符串
	 */
	@Override
	public void onNotificationClicked(Context context, String title,
			String description, String customContentString) {
		String notifyString = "通知点击 title=\"" + title + "\" description=\""
				+ description + "\" customContent=" + customContentString;
		Log.d(TAG, notifyString);
		 // 自定义内容获取方式，type对象消息内容的类型
		String type = "";
		 if (!TextUtils.isEmpty(customContentString)) {
		 JSONObject customJson = null;
		 try {
		 customJson = JSON.parseObject(customContentString);
		 if (customJson.containsKey("type")) {
			 type = customJson.getString("type");
		 }
		 } catch (Exception e) {
		 // TODO Auto-generated catch block
		 e.printStackTrace();
		 	}
		 }
		 if(AppApplication.loginUser!=null)//已经是登录状态了
		 {
			 Intent intent = new Intent(context, MainTabActivity.class);
			 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			 intent.putExtra("type", type);
			 intent.putExtra("message", description);
			 context.startActivity(intent);
		 }
		 else
		 {
			 Intent intent = new Intent(context,LoginActivity.class);
			 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			 context.startActivity(intent);
		 }
		 // Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
		// updateContent(context, notifyString);
	}
	
	

	/**
	 * setTags() 的回调函数。
	 * 
	 * @param context
	 *            上下文
	 * @param errorCode
	 *            错误码。0表示某些tag已经设置成功；非0表示所有tag的设置均失败。
	 * @param successTags
	 *            设置成功的tag
	 * @param failTags
	 *            设置失败的tag
	 * @param requestId
	 *            分配给对云推送的请求的id
	 */
	@Override
	public void onSetTags(Context context, int errorCode,
			List<String> sucessTags, List<String> failTags, String requestId) {
		StringBuffer buffer = new StringBuffer();
		if(errorCode == 0)
		{
			for(int i=0;i<sucessTags.size();i++)
			{
				buffer.append(sucessTags.get(i)+";");
			}
			Log.i("baidupush", buffer.toString());
		}
	}

	/**
	 * delTags() 的回调函数。
	 * 
	 * @param context
	 *            上下文
	 * @param errorCode
	 *            错误码。0表示某些tag已经删除成功；非0表示所有tag均删除失败。
	 * @param successTags
	 *            成功删除的tag
	 * @param failTags
	 *            删除失败的tag
	 * @param requestId
	 *            分配给对云推送的请求的id
	 */
	@Override
	public void onDelTags(Context context, int errorCode,
			List<String> sucessTags, List<String> failTags, String requestId) {
	}

	/**
	 * listTags() 的回调函数。
	 * 
	 * @param context
	 *            上下文
	 * @param errorCode
	 *            错误码。0表示列举tag成功；非0表示失败。
	 * @param tags
	 *            当前应用设置的所有tag。
	 * @param requestId
	 *            分配给对云推送的请求的id
	 */
	@Override
	public void onListTags(Context context, int errorCode, List<String> tags,
			String requestId) {
	}

	/**
	 * PushManager.stopWork() 的回调函数。
	 * 
	 * @param context
	 *            上下文
	 * @param errorCode
	 *            错误码。0表示从云推送解绑定成功；非0表示失败。
	 * @param requestId
	 *            分配给对云推送的请求的id
	 */
	@Override
	public void onUnbind(Context context, int errorCode, String requestId) {
		// 解绑定成功，设置未绑定flag，
		if (errorCode == 0) {
			Utils.setBind(context, false);
		}
	}

	private boolean isRunningForeground(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
		String currentPackageName = cn.getPackageName();
		if (!TextUtils.isEmpty(currentPackageName)
				&& currentPackageName.equals(context.getPackageName())) {
			return true;
		}
		return false;
	}

	@Override
	public void onNotificationArrived(Context arg0, String arg1, String arg2,
			String arg3) {
		// TODO Auto-generated method stub
		//System.out.println("内容:"+arg1+","+arg2+","+arg3);
		String type = "";
		if (!TextUtils.isEmpty(arg3)) {
			JSONObject customJson = null;
			try {
				customJson = JSON.parseObject(arg3);
				if (customJson.containsKey("type")) {
					type = customJson.getString("type");
					if(type.equals(Constants.MESSAGE_TYPE_4))//帐号在其他设备上登录了
					{
						Preferences.getInstance().setStringData("loginUser", null);
						AppApplication.loginUser = null;
						// 单点登录
						Intent logoutIntent = new Intent(
								Constants.Logout_ACTION);
						logoutIntent.putExtra("message", arg2);
						arg0.sendBroadcast(logoutIntent);
						//arg0.sendBroadcast(new Intent(Constants.CLOSE_ACTION));
					}
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
