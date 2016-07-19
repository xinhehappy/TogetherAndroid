package com.hust.together.party;

import java.util.ArrayList;
import java.util.Map;

import android.annotation.SuppressLint;
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
import android.widget.Toast;

import com.hust.together.service.friend.GetFriendListService;
import com.hust.together.service.party.AddPartyMemberService;
import com.hust.together.ui.R;

@SuppressLint("ShowToast")
public class InviteMember extends Activity {
	private Button btn_back;
	private ArrayList<Map<String, Object>> listViewRes = null;
	private ListView listView;
	String list_id = "";
	String list_neckname = "";
	String partyId, partyName, leaderId, myid;
	private SharedPreferences sp_user_info = null;
	Toast toast;
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(InviteMember.this, PartyMember.class);
		intent.putExtra("partyId", partyId);
		intent.putExtra("leaderId", leaderId);
		intent.putExtra("partyName", partyName);
		startActivity(intent);
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.invitemember_list);

		sp_user_info = getSharedPreferences("user_info", MODE_PRIVATE);
		myid = sp_user_info.getString("uid", "");

		Intent intent = getIntent();
		partyId = intent.getStringExtra("partyId");
		leaderId = intent.getStringExtra("leaderId");
		partyName = intent.getStringExtra("partyName");

		GetFriendListTask getFriendListTask = new GetFriendListTask();
		getFriendListTask.execute();

		btn_back = (Button) findViewById(R.id.aboutus_btn_back);
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(InviteMember.this, PartyMember.class);
				intent.putExtra("partyId", partyId);
				intent.putExtra("leaderId", leaderId);
				intent.putExtra("partyName", partyName);
				startActivity(intent);
				finish();
			}
		});

	}
	
	class GetFriendListTask extends AsyncTask<String, Integer, ArrayList<Map<String, Object>>> {
		String url = getResources().getString(R.string.GetFriendListServletUrl);
		

		@Override
		protected void onPostExecute(final ArrayList<Map<String, Object>> listViewRes) {
			toast = Toast.makeText(InviteMember.this, "null", Toast.LENGTH_SHORT);
			listView = (ListView) findViewById(R.id.lv_addmember_friends);
			SimpleAdapter sSchedule = new SimpleAdapter(InviteMember.this, listViewRes,
					R.layout.friend_item, new String[] { "fid", "name" },
					new int[] { R.id.friendlist_fid, R.id.friendlist_neckname });

			listView.setAdapter(sSchedule);

			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					list_id = listViewRes.get(position).get("fid").toString();
					try {
						AddPartyMemberTask addPartyMemberTask = new AddPartyMemberTask();
						addPartyMemberTask.execute();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		}

		@Override
		protected ArrayList<Map<String, Object>> doInBackground(String... params){
			// TODO Auto-generated method stub
			listViewRes = GetFriendListService.sendDataByHttpClientPost(url, myid);
			return listViewRes;
		}

	}
	
	class AddPartyMemberTask extends AsyncTask<String, Integer, String> {
		String url = getResources().getString(
				R.string.AddPartyMemberServletUrl);

		@Override
		protected void onPostExecute(String result) {
			if (result.equals("1")) {
				toast.setText("已添加成功！");
				toast.show();
			} else {
				toast.setText("添加失败！");
				toast.show();
			}
		}

		@Override
		protected String doInBackground(String... params){
			// TODO Auto-generated method stub
			String result = null;
			try {
				result = AddPartyMemberService
						.sendDataByHttpClientPost(url, partyId,
								list_id);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;
		}

	}

}