package com.hust.together.util;

import android.content.SharedPreferences;

public class SharedPreTool {
	
	public static void saveUserInfo(String result,String username,String pwd,SharedPreferences sp){             
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("uid", result);
		editor.putString("uname", username);
		editor.putString("pwd", pwd);
		editor.commit();
	}
}
