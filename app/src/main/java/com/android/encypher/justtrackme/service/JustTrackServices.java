package com.android.encypher.justtrackme.service;

public class JustTrackServices extends BaseServices {

	public static void saveUserData(String data){
		saveString("userId", String.valueOf(data));
	}
	public static String getUser(){
		return (preferences.getString("userId", ""));
	}

	//	public static String getToken() {
//		return getUser().getToken();
//	}
//	public static boolean isUserLogin(){
//		return getUser().isLogin();
//	}
	//
	public static void logoutUser(){
		saveString(USER, "{}");
		saveString(FIRST_TIME_OPEN, null);
	}

	public static void saveUpdateUserProfile(String userProfile) {
//		try {
//			JSONObject object = new JSONObject(preferences.getString(USER, "{}"));
//			object.put(APPIC_USER_IMAGE, userProfile);
//			saveString(USER, object.toString());
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
	}

	public static String FIRST_TIME_OPEN="first_time";
	public static boolean FirstTimeLogin() {
		if(preferences.getString(FIRST_TIME_OPEN, null) != null){
			return false;
		}
		else {
			saveString(FIRST_TIME_OPEN,"oldUser");
			return true;
		}
	}

	public static String GCM_KEY="gcm_key";
	public static boolean SaveGCMKey(String key) {
		if(preferences.getString(GCM_KEY, null) != null){
			return false;
		}
		else {
			saveString(GCM_KEY,key);
			return true;
		}
	}

	public static String getGCMKey(){
		return preferences.getString(GCM_KEY,"");
	}

	public static String ContactUpload="contact_upload";
	public static void ContactUploadStarted(String key) {
		saveString(ContactUpload,key);
	}

	public static String getContactUploadStats(){
		return preferences.getString(ContactUpload,"");
	}
//	public static long getLastConstantApiCallTime(){
//		return preferences.getLong(LAST_CONST_API_CALL,0);
//	}
//	public static void saveLastConstantApiCallTime(long time){
//		saveLong(LAST_CONST_API_CALL,time);
//	}
//
//
//    public static void saveConstantData(String data) {
//        saveString(CONSTANT, String.valueOf(data));
//    }
//
//    public static String getConstantData(){
//        return preferences.getString(CONSTANT, null);
//    }


}
