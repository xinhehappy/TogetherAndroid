package com.hust.together.ui;

import android.annotation.SuppressLint;
import android.app.Activity;

import android.content.SharedPreferences;

import android.content.Intent;

import android.os.Bundle;

/**
 * 
 * ���ܣ�ʹ��ViewPagerʵ�ֳ��ν���Ӧ��ʱ������ҳ
 * 
 * 
 * 
 * (1)�ж��Ƿ����״μ���Ӧ��--��ȡ��ȡSharedPreferences�ķ���
 * 
 * (2)�ǣ����������activity���������MainActivity
 * 
 * (3)5s��ִ��(2)����
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

		// ��ȡSharedPreferences����Ҫ������

		preferences = getSharedPreferences("count", MODE_PRIVATE);

		int count = preferences.getInt("count", 0);

		// �жϳ�����ڼ������У�����ǵ�һ����������ת������ҳ��

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
