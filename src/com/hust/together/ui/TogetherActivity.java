package com.hust.together.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.hust.together.util.TogetherApplication;

public class TogetherActivity extends Activity {
	private Button bt_register;
	private Button bt_login;

	private ImageView showPicture;
	private Animation fadeIn;
	private Animation fadeInScale;
	private Animation fadeOut;

	private Drawable picture_1;
	private Drawable picture_2;
	private Drawable picture_3;
	private Drawable picture_4;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.together);
		
		TogetherApplication.getInstance().addActivity(this);
		System.out.println("Hello");
		bt_login = (Button) this.findViewById(R.id.bt_sns4a_login);
		bt_login.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(TogetherActivity.this, LoginActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
		bt_register = (Button) this.findViewById(R.id.bt_sns4a_register);
		bt_register.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(TogetherActivity.this, RegisterActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
		showPicture = (ImageView) findViewById(R.id.iv_sns4a_picture);
		
		init();
		setListener();
		
	}

	private void setListener() {
		fadeIn.setAnimationListener(new AnimationListener() {

			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				showPicture.startAnimation(fadeInScale);
			}
		});

		fadeInScale.setAnimationListener(new AnimationListener() {

			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				showPicture.startAnimation(fadeOut);
			}
		});

		fadeOut.setAnimationListener(new AnimationListener() {

			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				if (showPicture.getDrawable().equals(picture_1)) {
					showPicture.setImageDrawable(picture_2);
				} else if (showPicture.getDrawable().equals(picture_2)) {
					showPicture.setImageDrawable(picture_3);
				} else if (showPicture.getDrawable().equals(picture_3)) {
					showPicture.setImageDrawable(picture_4);
				} else if (showPicture.getDrawable().equals(picture_4)) {
					showPicture.setImageDrawable(picture_1);
				}
				showPicture.startAnimation(fadeIn);
			}

			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void init() {
		initAnim();
		initPicture();

		showPicture.setImageDrawable(picture_1);
		showPicture.startAnimation(fadeIn);
	}

	private void initAnim() {
		fadeIn = AnimationUtils.loadAnimation(TogetherActivity.this,
				R.anim.login_fade_in);
		fadeIn.setDuration(1000);
		fadeInScale = AnimationUtils.loadAnimation(TogetherActivity.this,
				R.anim.login_fade_in_scale);
		fadeInScale.setDuration(6000);
		fadeOut = AnimationUtils.loadAnimation(TogetherActivity.this,
				R.anim.login_fade_out);
		fadeOut.setDuration(1000);
	}

	private void initPicture() {
		picture_1 = getResources().getDrawable(R.drawable.guide_350_01);
		picture_2 = getResources().getDrawable(R.drawable.guide_350_02);
		picture_3 = getResources().getDrawable(R.drawable.guide_350_03);
		picture_4 = getResources().getDrawable(R.drawable.guide_350_04);
	}
	
}