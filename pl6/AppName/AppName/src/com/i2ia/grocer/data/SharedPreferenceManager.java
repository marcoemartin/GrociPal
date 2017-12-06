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
public class SharedPreferenceManager {
	private Context context;
	public SharedPreferenceManager(Context i_context){
		context=i_context;
	}
	
	/**
	 * Return saved state of preference
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
	
	public void saveAddress(String address){
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = sharedPref.edit();
		editor.putString("Address", address);
		editor.commit();
	}
	
	public String getAddress(){
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		String addressString =  sharedPref.getString("Address", null);
		return addressString;
	}
	
	public void saveWarning(String warning){
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = sharedPref.edit();
		editor.putString("Warning", warning);
		editor.commit();
	}
	
	public String getWarning(){
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		String warningString =  sharedPref.getString("Warning", null);
		return warningString;
	}
	
	//Possible Preference to display splash screen only first time user uses app?

}
