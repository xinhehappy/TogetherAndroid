package com.hust.together.party;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hust.together.service.party.GetPartyInfoServicec;
import com.hust.together.service.party.QuitPartyService;
import com.hust.together.service.party.RemovePartyService;
import com.hust.together.tool.MyDateTime;
import com.hust.together.ui.R;

@SuppressLint("ShowToast")
public class PartyInfoActivity extends Activity implements OnClickListener{
	private TextView tv_partyname, tv_showfaqiren, tv_showfaqitime,
			tv_showjuhuitime, tv_showjuhuiaddress, tv_showpartyintroduction;
	private Button btn_back, btn_del;
	private String list_id, list_name, leaderId, leaderName, partyLat,
			partyLon, myid;
	private LinearLayout getMember;
	private LinearLayout getPartyLoc;
	private SharedPreferences sp_user_info = null;
	Toast toast;
	
	

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(PartyInfoActivity.this,
				MyPartyActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.partyinfo);
		
		
		
		sp_user_info = getSharedPreferences("user_info", MODE_PRIVATE);
		myid = sp_user_info.getString("uid", "");

		Intent intent = getIntent();
		list_id = intent.getStringExtra("list_id");
		list_name = intent.getStringExtra("list_name");
		
		initView();

		PartyInfoTask partyInfoTask = new PartyInfoTask();
		partyInfoTask.execute();
		
	}
	
	private void initView(){
		tv_partyname = (TextView) findViewById(R.id.tv_partyname);
		tv_showfaqiren = (TextView) findViewById(R.id.tv_showfaqiren);
		tv_showfaqitime = (TextView) findViewById(R.id.tv_showfaqitime);
		tv_showjuhuitime = (TextView) findViewById(R.id.tv_showjuhuitime);
		tv_showjuhuiaddress = (TextView) findViewById(R.id.tv_showjuhuiaddress);
		tv_showpartyintroduction = (TextView) findViewById(R.id.tv_showpartyintroduction);
		
		btn_back = (Button) findViewById(R.id.aboutus_btn_back);
		btn_del = (Button) findViewById(R.id.btn_delparty);
		
		getPartyLoc = (LinearLayout) findViewById(R.id.ll_getpartyloc);
		getMember = (LinearLayout) findViewById(R.id.ll_getmemberlist);
		
		btn_back.setOnClickListener(this);
		btn_del.setOnClickListener(this);
		getPartyLoc.setOnClickListener(this);
		getMember.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		toast = Toast.makeText(PartyInfoActivity.this, "null", Toast.LENGTH_SHORT);
		switch(v.getId()){
		case R.id.aboutus_btn_back:{
			Intent intent = new Intent(PartyInfoActivity.this,
					MyPartyActivity.class);
			startActivity(intent);
			finish();
			break;
		}
		case R.id.btn_delparty:{
			// 若用户是该聚会的发起者，则为取消聚会
			if (leaderId.equals(myid)) {
				RemovePartyTask removePartyTask = new RemovePartyTask();
				removePartyTask.execute();
			}
			// 若用户不是该聚会的发起者，仅为参与者，则为退出聚会
			else {
				QuitPartyTask quitPartyTask = new QuitPartyTask();
				quitPartyTask.execute();
			}
			break;
		}
		case R.id.ll_getpartyloc:{
			Intent intent = new Intent(PartyInfoActivity.this,
					PartyLocActivity.class);
			System.out.println(list_name + "-----" + partyLat + "-----"
					+ partyLon);
			intent.putExtra("partyName", list_name);
			intent.putExtra("partyLat", partyLat);
			intent.putExtra("partyLon", partyLon);
			startActivity(intent);
			break;
		}
		case R.id.ll_getmemberlist:{
			System.out.println(tv_partyname.getText().toString() + list_id + leaderId);

			Intent intent = new Intent(PartyInfoActivity.this,
					PartyMember.class);
			intent.putExtra("partyName", tv_partyname.getText().toString());
			intent.putExtra("partyId", list_id);
			intent.putExtra("leaderId", leaderId);
			startActivity(intent);
			finish();
			break;
		}
		}

	}
	
	class PartyInfoTask extends AsyncTask<String, Integer, JSONObject> {
		String url = getResources().getString(R.string.GetPartyInfoServletUrl);
		@Override
		protected void onPostExecute(JSONObject object) {
			try {
				tv_partyname.setText(list_name);
				leaderId = object.getString("leaderId");
				if(leaderId.equals(myid)){
					btn_del.setText("取消聚会");
				}else{
					btn_del.setText("退出聚会");
				}
				System.out.println(leaderId);
				
				//服务端传回的时间为时间戳的格式，因此取其中的毫秒数来利用工具类转换为指定格式
				String createTime = object.getString("createTime");
				System.out.println(createTime);
				JSONObject createTimeObject = new JSONObject(createTime);
				String createTimeMillisecond =createTimeObject.getString("time");
				tv_showfaqitime.setText(new MyDateTime().getDateTimeByMillisecond(createTimeMillisecond));
				
				String startTime = object.getString("startTime");
				String startTime1 =startTime.substring(0, 19);
				tv_showjuhuitime.setText(startTime1);
				
				tv_showjuhuiaddress.setText(object.getString("address"));
				System.out.println(object.getString("address"));
				tv_showpartyintroduction.setText(object.getString("note"));
				System.out.println(object.getString("note"));
				partyLat = object.getString("posLat");
				partyLon = object.getString("posLon");
				Log.d("经纬度", partyLat + partyLon);

				String member = object.getString("member");
				JSONArray memberArray = new JSONArray(member);
				for (int i = 0; i < memberArray.length(); i++) {
					JSONObject object1 = memberArray.getJSONObject(i);
					if ((object1.getString("userId")).equals(leaderId)) {
						leaderName = object1.getString("userName");
					}
				}
				tv_showfaqiren.setText(leaderName);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		@Override
		protected JSONObject doInBackground(String... params){
			// TODO Auto-generated method stub
			JSONObject object = null;
			try {
				object = GetPartyInfoServicec.sendDataByHttpClientPost(
						url, list_id);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return object;
		}
	}
	
	class RemovePartyTask extends AsyncTask<String, Integer, String> {
		String url = getResources().getString(
				R.string.RemovePartyServletUrl);

		@Override
		protected void onPostExecute(String result) {
			if (result.equals("1")) {
				toast.setText("该聚会已取消！");
				toast.show();
				Intent intent = new Intent(PartyInfoActivity.this,
						MyPartyActivity.class);
				startActivity(intent);
				finish();
			} else {
			}
		}

		@Override
		protected String doInBackground(String... params){
			// TODO Auto-generated method stub
			String result = null;
			try {
				result = RemovePartyService
						.sendDataByHttpClientPost(url, myid, list_id);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;
		}

	}
	
	class QuitPartyTask extends AsyncTask<String, Integer, String> {
		String url = getResources().getString(
				R.string.QuitPartyServletUrl);

		@Override
		protected void onPostExecute(String result) {
			if (result.equals("1")) {
				toast.setText("已退出该聚会！");
				toast.show();
				Intent intent = new Intent(PartyInfoActivity.this,
						MyPartyActivity.class);
				startActivity(intent);
				finish();
			} else {
			}
		}

		@Override
		protected String doInBackground(String... params){
			// TODO Auto-generated method stub
			String result = null;
			try {
				result = QuitPartyService
						.sendDataByHttpClientPost(url, myid, list_id);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;
		}

	}

}
