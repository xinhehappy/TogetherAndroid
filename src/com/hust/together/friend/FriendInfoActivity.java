package com.hust.together.friend;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hust.together.party.PartyMember;
import com.hust.together.service.friend.DeleteFriendService;
import com.hust.together.service.friend.GetFriendInfoServicec;
import com.hust.together.service.friend.GetUserLocService;
import com.hust.together.service.friend.ModifyBeizhuService;
import com.hust.together.tool.MyDateTime;
import com.hust.together.ui.MainActivity;
import com.hust.together.ui.R;

public class FriendInfoActivity extends Activity implements OnClickListener{
	private TextView uname, usign, ubeizhu, ugender, uhome , ubirth, uemail;
	private Button btn_back, btn_delete,btn_friendloc;
	private String list_id = "";
	private String list_neckname = "";
	private String intent_type = "";
	private String leaderId="";
	private String partyId="";
	private String partyName="";
	private LinearLayout linearLayout_beizhu;
	private SharedPreferences sp_user_info = null;
	JSONObject object = null;
	String myid, info, result;

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (intent_type.equals("FromFriendActivity")) {
			Intent intent = new Intent(FriendInfoActivity.this,
					MainActivity.class);
			intent.putExtra("tabNum", "3");
			startActivity(intent);
			finish();
		} else {
			Intent intent = new Intent(FriendInfoActivity.this,
					PartyMember.class);
			intent.putExtra("partyName", partyName);
			intent.putExtra("partyId", partyId);
			intent.putExtra("leaderId", leaderId);
			startActivity(intent);
			finish();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.friendinfo);

		sp_user_info = getSharedPreferences("user_info", MODE_PRIVATE);
		myid = sp_user_info.getString("uid", "");

		Intent intent = getIntent();

		list_id = intent.getStringExtra("list_id");
		list_neckname = intent.getStringExtra("list_neckname");
		intent_type = intent.getStringExtra("intent_type");
		if (intent_type.equals("FromPartyMember")) {
			partyName = intent.getStringExtra("partyName");
			partyId = intent.getStringExtra("partyId");
			leaderId = intent.getStringExtra("leaderId");
		}else{
			partyName =null;
			partyId =null;
			leaderId =null;
		}

		initView();

		FriendInfoTask friendInfoTask = new FriendInfoTask();
		friendInfoTask.execute();
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.aboutus_btn_back: {
			if (intent_type.equals("FromFriendActivity")) {
				Intent intent = new Intent(FriendInfoActivity.this,
						MainActivity.class);
				intent.putExtra("tabNum", "3");
				startActivity(intent);
				finish();
			} else {
				Intent intent = new Intent(FriendInfoActivity.this,
						PartyMember.class);
				intent.putExtra("partyName", partyName);
				intent.putExtra("partyId", partyId);
				intent.putExtra("leaderId", leaderId);
				startActivity(intent);
				finish();
			}
			break;
		}
		case R.id.btn_friendloc: {
			FriendLocTask friendLocTask = new FriendLocTask();
			friendLocTask.execute();
			break;
		}
		case R.id.btn_delfriend: {
			DelFriendTask delFriendTask = new DelFriendTask();
			delFriendTask.execute();
			break;
		}
		case R.id.userinfo_ll_beizhu: {
			final EditText editText = new EditText(FriendInfoActivity.this);
			editText.setText(info);

			new AlertDialog.Builder(FriendInfoActivity.this)
					.setTitle("设置备注")
					.setIcon(android.R.drawable.ic_dialog_info)
					.setView(editText)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									String newbeizhu = editText.getText()
											.toString();
									ubeizhu.setText(newbeizhu);
									info = newbeizhu;
									ModifyRemarkTask modifyRemarkTask = new ModifyRemarkTask();
									modifyRemarkTask.execute();
								}
							}).setNegativeButton("取消", null).show();
		}
			
		}
	}
	
	private void initView(){
		uname = (TextView) findViewById(R.id.tv_showneckname);
		usign = (TextView) findViewById(R.id.tv_showintroduction);
		ubeizhu = (TextView) findViewById(R.id.tv_showbeizhu);
		ugender = (TextView) findViewById(R.id.tv_showgengder);
		uhome = (TextView) findViewById(R.id.tv_showaddress);
		ubirth = (TextView) findViewById(R.id.tv_showbirthday);
		uemail =(TextView) findViewById(R.id.tv_showemail);
		btn_back = (Button) findViewById(R.id.aboutus_btn_back);
		btn_friendloc =(Button)findViewById(R.id.btn_friendloc);
		btn_delete = (Button) findViewById(R.id.btn_delfriend);
		linearLayout_beizhu = (LinearLayout) findViewById(R.id.userinfo_ll_beizhu);
		
		ubeizhu.setText(list_neckname);
		info = ubeizhu.getText().toString();
		
		btn_back.setOnClickListener(this);
		btn_friendloc.setOnClickListener(this);		
		btn_delete.setOnClickListener(this);
		linearLayout_beizhu.setOnClickListener(this);
	}

	class FriendInfoTask extends AsyncTask<String, Integer, JSONObject> {
		String url = getResources().getString(R.string.GetFriendInfoServletUrl);
		@Override
		protected void onPostExecute(JSONObject object) {
			try {
				uname.setText(object.getString("name"));
				ubeizhu.setText(list_neckname);
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
	
	class DelFriendTask extends AsyncTask<String, Integer, String> {
		String url = getResources().getString(
				R.string.DeleteFriendServletUrl);
		@Override
		protected void onPostExecute(String result) {
			if (result.equals("1")) {
				if (intent_type.equals("FromFriendActivity")) {
					Intent intent = new Intent(FriendInfoActivity.this,
							MainActivity.class);
					intent.putExtra("tabNum", "3");
					startActivity(intent);
					finish();
				} else {
					Intent intent = new Intent(FriendInfoActivity.this,
							PartyMember.class);
					intent.putExtra("partyName", partyName);
					intent.putExtra("partyId", partyId);
					intent.putExtra("leaderId", leaderId);
					startActivity(intent);
					finish();
				}
			}
		}

		@Override
		protected String doInBackground(String... params){
			// TODO Auto-generated method stub
			String result = null;
			try {
				result = DeleteFriendService.sendDataByHttpClientPost(url,
						myid, list_id);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;
		}

	}
	
	class ModifyRemarkTask extends AsyncTask<String, Integer, String> {
		String neckname = (ubeizhu.getText().toString()).trim();

		String url = getResources().getString(R.string.ModifyBeizhuServletUrl);
		@Override
		protected void onPostExecute(String result) {
			if (result.equals("1")) {
				ubeizhu.setText(neckname);
				return;
			}else{
				
			}
		}

		@Override
		protected String doInBackground(String... params){
			// TODO Auto-generated method stub
			String result = null;
			try {
				result = ModifyBeizhuService.sendDataByHttpClientPost(url,
						myid, list_id, neckname).trim();
				System.out.println("成功没有？"+ result);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;
		}

	}
	
	class FriendLocTask extends AsyncTask<String, Integer, JSONObject> {
		String url = getResources().getString(R.string.GetUserLocServletUrl);

		@Override
		protected void onPostExecute(JSONObject object) {
			String locTime1 = null,lat = null,lon = null;
			try {
				String locTime = object.getString("locTime");
				JSONObject locTimeObject= new JSONObject(locTime);
				String locTimeMillisecond =locTimeObject.getString("time");
				locTime1 = new MyDateTime().getDateTimeByMillisecond(locTimeMillisecond);
				
				lat =object.getString("poslat");
				lon =object.getString("poslon");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Intent intent = new Intent(FriendInfoActivity.this,
					FriendLocActivity.class);
			intent.putExtra("fName", list_neckname);
			intent.putExtra("locTime", locTime1);
			intent.putExtra("poslat", lat);
			intent.putExtra("poslon", lon);
			startActivity(intent);
		}

		@Override
		protected JSONObject doInBackground(String... params){
			// TODO Auto-generated method stub
			JSONObject object = null;
			try {
				object = GetUserLocService.sendDataByHttpClientPost(url, list_id);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return object;
		}

	}

}
