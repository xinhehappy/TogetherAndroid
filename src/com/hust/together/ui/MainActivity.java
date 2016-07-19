package com.hust.together.ui;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.hust.together.friend.FriendActivity;
import com.hust.together.message.MessageActivity;
import com.hust.together.party.PartyActivity;
import com.hust.together.setting.SettingActivity;
import com.hust.together.tool.LocationService;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {

	private String tabNum;
	private RadioButton rb_home, rb_party, rb_friend, rb_message, rb_setting;
	private static final String HOME_TAB = "home";
	private static final String FRIEND_TAB = "friend";
	private static final String PARTY_TAB = "party";
	private static final String MSG_TAB = "msg";
	private static final String SETTING_TAB = "setting";
	private TabHost tabHost;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		Intent locationService = new Intent(MainActivity.this,
				LocationService.class);
		startService(locationService);

		Intent intent = getIntent();
		tabNum = intent.getStringExtra("tabNum");

		tabHost = this.getTabHost();

		TabSpec homeSpec = tabHost.newTabSpec(HOME_TAB).setIndicator(HOME_TAB)
				.setContent(new Intent(this, HomeActivity.class));
		TabSpec friendSpec = tabHost.newTabSpec(FRIEND_TAB)
				.setIndicator(FRIEND_TAB)
				.setContent(new Intent(this, FriendActivity.class));
		TabSpec partySpec = tabHost.newTabSpec(PARTY_TAB)
				.setIndicator(PARTY_TAB)
				.setContent(new Intent(this, PartyActivity.class));
		TabSpec msgSpec = tabHost.newTabSpec(MSG_TAB).setIndicator(MSG_TAB)
		 .setContent(new Intent(this, MessageActivity.class));
		TabSpec settingSpec = tabHost.newTabSpec(SETTING_TAB)
				.setIndicator(SETTING_TAB)
				.setContent(new Intent(this, SettingActivity.class));

		tabHost.addTab(homeSpec);
		tabHost.addTab(friendSpec);
		tabHost.addTab(partySpec);
		tabHost.addTab(msgSpec);
		tabHost.addTab(settingSpec);

		RadioGroup radioGroup = (RadioGroup) this
				.findViewById(R.id.rg_main_btns);

		while (tabNum != null) {
			if (tabNum.equals("1")) {
				tabHost.setCurrentTabByTag(HOME_TAB);
				rb_home = (RadioButton) findViewById(R.id.rb_home);
				rb_home.setChecked(true);
				tabNum = null;
			} else if (tabNum.equals("2")) {
				tabHost.setCurrentTabByTag(PARTY_TAB);
				rb_party = (RadioButton) findViewById(R.id.rb_party);
				rb_party.setChecked(true);
				tabNum = null;
			} else if (tabNum.equals("3")) {
				tabHost.setCurrentTabByTag(FRIEND_TAB);
				rb_friend = (RadioButton) findViewById(R.id.rb_friend);
				rb_friend.setChecked(true);
				tabNum = null;
			} else if (tabNum.equals("4")) {
				tabHost.setCurrentTabByTag(MSG_TAB);
				rb_message = (RadioButton) findViewById(R.id.rb_message);
				rb_message.setChecked(true);
				tabNum = null;
			} else if (tabNum.equals("5")) {
				tabHost.setCurrentTabByTag(SETTING_TAB);
				rb_setting = (RadioButton) findViewById(R.id.rb_setting);
				rb_setting.setChecked(true);
				tabNum = null;
			}

		}
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub

				switch (checkedId) {
				case R.id.rb_home:
					tabHost.setCurrentTabByTag(HOME_TAB);
					break;

				case R.id.rb_party:
					tabHost.setCurrentTabByTag(PARTY_TAB);
					break;

				case R.id.rb_friend:
					tabHost.setCurrentTabByTag(FRIEND_TAB);
					break;

				case R.id.rb_message:
					tabHost.setCurrentTabByTag(MSG_TAB);
					break;

				case R.id.rb_setting:
					tabHost.setCurrentTabByTag(SETTING_TAB);
					break;

				default:
					break;
				}

			}
		});

	}

}
