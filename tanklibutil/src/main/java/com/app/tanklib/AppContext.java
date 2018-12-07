package com.app.tanklib;

import android.content.Context;

public class AppContext {
	
	private static Context context;

	public static Context getContext() {
		return context;
	}

	public static void setContext(Context paramContext) {
		context = paramContext;
	}
}