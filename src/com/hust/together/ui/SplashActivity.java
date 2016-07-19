package com.hust.together.ui;

import android.annotation.SuppressLint;
import android.app.Activity;

import android.content.SharedPreferences;

import android.content.Intent;

import android.os.Bundle;

/**
 * 
 * 功能：使用ViewPager实现初次进入应用时的引导页
 * 
 * 
 * 
 * (1)判断是否是首次加载应用--采取读取SharedPreferences的方法
 * 
 * (2)是，则进入引导activity；否，则进入MainActivity
 * 
 * (3)5s后执行(2)操作
 * 
 * 
 */

@SuppressLint("WorldReadableFiles")
public class SplashActivity extends Activity {

	SharedPreferences preferences;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_splash);

		// 读取SharedPreferences中需要的数据

		preferences = getSharedPreferences("count", MODE_PRIVATE);

		int count = preferences.getInt("count", 0);

		// 判断程序与第几次运行，如果是第一次运行则跳转到引导页面

		if (count == 0) {

			Intent intent = new Intent(SplashActivity.this, GuideActivity.class);


			startActivity(intent);

			finish();

		}else{
			Intent intent = new Intent(SplashActivity.this, TogetherActivity.class);


			startActivity(intent);

			finish();
		}


	}
}
