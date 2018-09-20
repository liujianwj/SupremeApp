package zs.com.supremeapp.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;

public class ShareUtils {
	public static final String SHARE_PARAMS = "share_params";

	public static SharedPreferences getSP(String appName, ContextWrapper context) {
		return context.getSharedPreferences(appName, Context.MODE_PRIVATE);
	}
	public static String getValue(SharedPreferences sp, String key) {
		return sp.getString(key, null);
	}
	
	public static String getValue(SharedPreferences sp, String key, String defValue) {
		return sp.getString(key, defValue);
	}
	
	public static long getLongValue(SharedPreferences sp, String key, long defValue) {
		return sp.getLong(key, defValue);
	}

	public static void clearValue(SharedPreferences sp, String key) {
		SharedPreferences.Editor editor = sp.edit();
		editor.remove(key);
		editor.commit();
	}
	
	public static void updateValue(SharedPreferences sp, String key, String value) {
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	public static void updateValue(SharedPreferences sp, String key, long value) {
		SharedPreferences.Editor editor = sp.edit();
		editor.putLong(key, value);
		editor.commit();
	}

	public static void setBooleanValue(String shopName,String key,boolean state,Context context){
		SharedPreferences.Editor editor = getSharedPreferences(context,shopName).edit();
		editor.putBoolean(key,state);
		editor.commit();
	}

	public static void setStringValue(String fileName,String key,String value,Context context){
		SharedPreferences.Editor editor = getSharedPreferences(context,fileName).edit();
		editor.putString(key,value);
		editor.commit();
	}

	public static String getStringValue(String fileName,String key,String defaultValue,Context context){
		return getSharedPreferences(context,fileName).getString(key,defaultValue);
	}

	public static boolean getBooleanValue(String shopName,String key,boolean defaultValue,Context context){
		return getSharedPreferences(context,shopName).getBoolean(key,defaultValue);
	}

	public static SharedPreferences getSharedPreferences(Context context,String shopName){
		return context.getSharedPreferences(shopName, Context.MODE_PRIVATE);
	}

	//判断首页"重要通知"的弹框
	public static void setBooleanEntityId(String preferenceName, String entityId, boolean value, Context context) {
		SharedPreferences.Editor editor = getSharedPreferences(context,preferenceName).edit();
		editor.putBoolean(entityId,value);
		editor.commit();
	}
	public static boolean getBooleanEntityId(String preferenceName, String entityId, boolean value, Context context) {
		return getSharedPreferences(context,preferenceName).getBoolean(entityId,value);
	}
}
