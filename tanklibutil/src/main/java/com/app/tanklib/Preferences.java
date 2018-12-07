package com.app.tanklib;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * 本地持久化类
 * 
 * @author zkl
 */
public class Preferences {
	private SharedPreferences mPrefs;

	public Preferences(Context paramContext) {
		this.mPrefs = PreferenceManager
				.getDefaultSharedPreferences(paramContext);
	}

	public static Preferences getInstance() {
		Context paramContext = AppContext.getContext();
		Preferences localPreferences = (Preferences) paramContext
				.getSystemService("com.szzy.app.preferences");
		if (localPreferences == null)
			localPreferences = (Preferences) paramContext
					.getApplicationContext().getSystemService(
							"com.szzy.app.preferences");
		if (localPreferences == null)
			throw new IllegalStateException("Preferences not available");
		return localPreferences;
	}

	
	public void setStringData(String key,String value){
		this.mPrefs.edit().putString(key, value).commit();
	}
	 
	public String getStringData(String key){
		return this.mPrefs.getString(key, null);
	}
}
