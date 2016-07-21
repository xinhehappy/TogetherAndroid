package com.hust.together.party;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.hust.together.ui.MainActivity;
import com.hust.together.ui.R;

public class FriendPartyActivity extends Activity {

	Button btn_back;
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(FriendPartyActivity.this,
				MainActivity.class);
		intent.putExtra("tabNum", "2");
		startActivity(intent);
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.friendparty);
		
		btn_back = (Button) findViewById(R.id.aboutus_btn_back);
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch(v.getId()){
				case R.id.aboutus_btn_back:{
					Intent intent = new Intent(FriendPartyActivity.this,
							MainActivity.class);
					intent.putExtra("tabNum", "2");
					startActivity(intent);
					finish();
					break;
				}}
			}
		});
	}

}
