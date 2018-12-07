package com.app.tanklib.util;


import android.os.AsyncTask;

/**
 * 异步任务控制类
 * @author Administrator
 *
 */
public class AsyncTaskUtil {
	public static boolean isTaskRunning(AsyncTask<?, ?, ?> task) {
		boolean result = false;
		
		if (task != null && task.getStatus() == AsyncTask.Status.RUNNING) {
			result = true;
		}
		
		return result;
	}
	
	public static boolean isTaskFinished(AsyncTask<?, ?, ?> task) {
		boolean result = false;
		
		if (task == null 
				|| task.getStatus() == AsyncTask.Status.FINISHED
				|| task.isCancelled()) {
			result = true;
		}
		
		return result;
	}
	
	public static void cancelTask(AsyncTask<?, ?, ?> task) {
		if (isTaskRunning(task)) {
			task.cancel(true);
		}
	}
	
}
