package com.hust.together.party;

import java.util.ArrayList;
import java.util.Map;

import org.json.JSONArray;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.hust.together.friend.FriendInfoActivity;
import com.hust.together.service.friend.JudgeIsFriendService;
import com.hust.together.service.party.GetPartyMemberServicec;
import com.hust.together.ui.HttpCon;
import com.hust.together.ui.R;

public class PartyMember extends Activity implements OnClickListener{

	private String partyName, partyId, leaderId, myid;
	private TextView title_top;
	private ArrayList<Map<String, Object>> listViewRes = null;
	private ListView listView;
	private Button btn_back, btn_add;
	private String list_id = "";
	String list_neckname;
	private SharedPreferences sp_user_info = null;

	
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(PartyMember.this,
				PartyInfoActivity.class);
		intent.putExtra("list_name", partyName);
		intent.putExtra("list_id", partyId);
		startActivity(intent);
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.partymember);

		sp_user_info = getSharedPreferences("user_info", MODE_PRIVATE);
		myid = sp_user_info.getString("uid", "");

		Intent intent = getIntent();
		partyName = intent.getStringExtra("partyName");
		partyId = intent.getStringExtra("partyId");
		leaderId = intent.getStringExtra("leaderId");

		initView();
		
		GetPartyMemberTask getPartyMemberTask = new GetPartyMemberTask();
		getPartyMemberTask.execute();

	}
	
	private void initView(){
		title_top = (TextView) findViewById(R.id.txt_vb_title);
		title_top.setText(partyName);
		
		btn_back = (Button) findViewById(R.id.aboutus_btn_back);
		btn_back.setOnClickListener(this);

		btn_add = (Button) findViewById(R.id.add_partymember);
		if (leaderId.equals(myid)) {
			btn_add.setOnClickListener(this);
		}else{
			btn_add.setVisibility(View.GONE);
		}
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.aboutus_btn_back:{
			Intent intent = new Intent(PartyMember.this,
					PartyInfoActivity.class);
			intent.putExtra("list_name", partyName);
			intent.putExtra("list_id", partyId);
			startActivity(intent);
			finish();
			break;
		}
		case R.id.add_partymember:{
			Intent intent = new Intent(PartyMember.this,
					InviteMember.class);
			intent.putExtra("partyName", partyName);
			intent.putExtra("partyId", partyId);
			intent.putExtra("leaderId", leaderId);
			startActivity(intent);
			finish();
			break;
		}
		}
	}
	
	class GetPartyMemberTask extends AsyncTask<String, Integer, ArrayList<Map<String, Object>>> {
		String url = getResources().getString(R.string.GetPartyInfoServletUrl);
		
		@Override
		protected void onPostExecute(final ArrayList<Map<String, Object>> listViewRes) {
			listView = (ListView) findViewById(R.id.lv_partymembers);

			SimpleAdapter sSchedule = new SimpleAdapter(PartyMember.this, listViewRes,
					R.layout.partymember_item, new String[] { "userName" },
					new int[] { R.id.tv_showmember });

			listView.setAdapter(sSchedule);

			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					list_id = listViewRes.get(position).get("userId").toString();
					JudgeIsFriendTask judgeIsFriendTask = new JudgeIsFriendTask();
					judgeIsFriendTask.execute();
				}
			});

		}

		@Override
		protected ArrayList<Map<String, Object>> doInBackground(String... params){
			// TODO Auto-generated method stub
			try {
				listViewRes = GetPartyMemberServicec.sendDataByHttpClientPost(url,
						partyId);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return listViewRes;
		}

	}
	
	class JudgeIsFriendTask extends AsyncTask<String, Integer, String> {
		String url = getResources().getString(R.string.JudgeIsFriendServletUrl);
		@Override
		protected void onPostExecute(String result) {
			if (result.equals("0")) {
				Intent intent = new Intent(PartyMember.this,
						UserInfoActivity.class);
				intent.putExtra("list_id", list_id);
				startActivity(intent);
			} else {
				GetFriendListTask getFriendListTask = new GetFriendListTask();
				getFriendListTask.execute();
			}
		}

		@Override
		protected String doInBackground(String... params){
			// TODO Auto-generated method stub
			String result = null;
			try {
				result = JudgeIsFriendService.sendDataByHttpClientPost(url, myid, list_id).trim();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;
		}

	}
	
	class GetFriendListTask extends AsyncTask<String, Integer, JSONArray> {
		String url = getResources().getString(R.string.GetFriendListServletUrl)+ "&myid=" + myid;
		@Override
		protected void onPostExecute(JSONArray array) {
			for (int i = 0; i < array.length(); i++) {
				try {
					JSONObject object = array.getJSONObject(i);
					if(object.getString("fid").equals(list_id)){
						list_neckname = object.getString("name");
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			Intent intent = new Intent(PartyMember.this,
					FriendInfoActivity.class);
			intent.putExtra("list_id", list_id);
			intent.putExtra("partyName", partyName);
			intent.putExtra("partyId", partyId);
			intent.putExtra("leaderId", leaderId);
			intent.putExtra("list_neckname", list_neckname);
			intent.putExtra("intent_type", "FromPartyMember");
			startActivity(intent);
		}

		@Override
		protected JSONArray doInBackground(String... params){
			// TODO Auto-generated method stub
			JSONArray array = null ;
			try {
				String body = HttpCon.getContent(url);
				array = new JSONArray(body);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return array;
		}

	}

}
