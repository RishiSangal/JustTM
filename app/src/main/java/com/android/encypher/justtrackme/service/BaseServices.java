package com.android.encypher.justtrackme.service;

import android.content.SharedPreferences;
import android.provider.Settings;

import com.android.encypher.justtrackme.JustTrackMeApplication;
import com.android.encypher.justtrackme.common.ICommonData;


public class BaseServices implements ICommonData {
	protected static SharedPreferences preferences;
	//	1 = stocks
	//	2 = futures
	//	10 = forex
	//	7 = ETFs
	//	5 = mutual fund
	protected static final String USER = "=XHSDSDFHWDFH";
	protected static final String PRIVACY="XSASDAJNUKJG";
    protected static final String CONSTANT = "=XTRAKDSJHEYSK";
	protected static final String LAST_CONST_API_CALL = "=XDFHSDFSADLFSKD";
    private static String DeviceId;
	static {
        preferences = JustTrackMeApplication.getContext().getSharedPreferences(
                "piyush", 0);
        DeviceId = Settings.Secure.getString(JustTrackMeApplication.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static String getDeviceId(){
        return DeviceId;
    }
	public static void saveBoolean(String key,boolean value){
		SharedPreferences.Editor editor=preferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
	public static void saveLong(String key,long value){
		SharedPreferences.Editor editor=preferences.edit();
		editor.putLong(key, value);
		editor.commit();
	}
	public static void saveInt(String key,int value){
		SharedPreferences.Editor editor=preferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}
	public static void saveString(String key,String value){
		SharedPreferences.Editor editor=preferences.edit();
		editor.putString(key, value);
		editor.commit();
	}
}
