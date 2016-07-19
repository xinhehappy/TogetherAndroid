package com.hust.together.party;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.hust.together.service.friend.AddFriendService;
import com.hust.together.service.friend.GetFriendInfoServicec;
import com.hust.together.ui.R;

public class UserInfoActivity extends Activity implements OnClickListener{
	private TextView uname, usign,ugender, uhome , ubirth, uemail;
	private Button btn_back, btn_add;
	private String list_id = "";
	private SharedPreferences sp_user_info = null;
	String myid;
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.userinfo);

		sp_user_info = getSharedPreferences("user_info", MODE_PRIVATE);
		myid = sp_user_info.getString("uid", "");
		Intent intent = getIntent();
		list_id = intent.getStringExtra("list_id");
		
		initView();

		UserInfoTask userInfoTask = new UserInfoTask();
		userInfoTask.execute();
	}
	
	private void initView(){
		uname = (TextView) findViewById(R.id.tv_showneckname);
		usign = (TextView) findViewById(R.id.tv_showintroduction);
		ugender = (TextView) findViewById(R.id.tv_showgengder);
		uhome = (TextView) findViewById(R.id.tv_showaddress);
		ubirth = (TextView) findViewById(R.id.tv_showbirthday);
		uemail =(TextView) findViewById(R.id.tv_showemail);
		btn_back = (Button) findViewById(R.id.aboutus_btn_back);
		btn_add = (Button) findViewById(R.id.btn_addfriend_partymember);
		
		btn_back.setOnClickListener(this);
		btn_add.setOnClickListener(this);
	}

	class UserInfoTask extends AsyncTask<String, Integer, JSONObject> {
		String url = getResources().getString(R.string.GetFriendInfoServletUrl);
		@Override
		protected void onPostExecute(JSONObject object) {
			try {
				uname.setText(object.getString("name"));
				usign.setText(object.getString("sign"));
				ugender.setText(object.getString("gender"));
				uhome.setText(object.getString("home"));
				ubirth.setText(object.getString("birthday"));
				uemail.setText(object.getString("email"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		protected JSONObject doInBackground(String... params){
			// TODO Auto-generated method stub
			JSONObject object = null;
			try {
				object = GetFriendInfoServicec.sendDataByHttpClientPost(url,
						list_id);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return object;
		}

	}
	
	class AddFriendTask extends AsyncTask<String, Integer, String> {
		String url = getResources().getString(R.string.AddFriendServletUrl);
		@Override
		protected void onPostExecute(String result) {
			if (result.equals("1")) {
				finish();
			} 
		}

		@Override
		protected String doInBackground(String... params){
			// TODO Auto-generated method stub
			String result = null;
			try {
				result = AddFriendService.sendDataByHttpClientPost(url, myid, list_id).trim();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.aboutus_btn_back:{
			finish();
			break;
		}
		case R.id.btn_addfriend_partymember:{
			AddFriendTask addFriendTask =new AddFriendTask();
			addFriendTask.execute();
			break;
		}
		}
	}

}
