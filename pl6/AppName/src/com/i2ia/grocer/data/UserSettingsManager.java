package com.i2ia.grocer.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/**
 * Saves user settings using SharedPreferences
 * @author Daniel
 *
 */
public class UserSettingsManager {
	private Context context;
	public UserSettingsManager(Context i_context){
		context=i_context;
	}
	/**
	 * 
	 * @return boolean switch ON/OFF
	 */
	public boolean getPreference(String key){
		//Return preference (ON/Off) 
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		boolean toggleValue = sharedPreferences.getBoolean(key, false);//returns false if no preference
		return toggleValue;	
	}
	
	/**
	 * For managing toggalable preferences
	 */
	public void SetPreference(String key, Boolean value){
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = sharedPref.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
	
	//Possible Preference to display plash screen only first time user uses app?

}
