package com.hust.together.friend;

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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.hust.together.party.UserInfoActivity;
import com.hust.together.service.friend.JudgeIsFriendService;
import com.hust.together.service.friend.SearchUserService;
import com.hust.together.ui.HttpCon;
import com.hust.together.ui.MainActivity;
import com.hust.together.ui.R;

public class AddFriendActivity extends Activity implements OnClickListener{
	private EditText usernameEditText;
	private Button btn_search, btn_back;
	private ArrayList<Map<String, Object>> listViewRes = null;
	private ListView listView;
	String list_id = "";
	String myid;
	private SharedPreferences sp_user_info = null;

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(AddFriendActivity.this,MainActivity.class);
		intent.putExtra("tabNum", "3");
		startActivity(intent);
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.addfriend);
		
		sp_user_info = getSharedPreferences("user_info", MODE_PRIVATE);
		myid = sp_user_info.getString("uid", "");
		
		initView();
	}
	
	private void initView(){
		usernameEditText = (EditText) findViewById(R.id.addfriend_et_username);
		btn_back = (Button) findViewById(R.id.aboutus_btn_back);
		btn_search = (Button) findViewById(R.id.addfriend_btn_search);
		
		btn_back.setOnClickListener(this);
		btn_search.setOnClickListener(this);
	}

	
	class SearchUserTask extends AsyncTask<String, Integer, ArrayList<Map<String, Object>>> {
		// 获取需要查找的用户名username
		String username = usernameEditText.getText().toString().trim();
		
		String url = getResources().getString(R.string.SearchUserServletUrl);
		@Override
		protected void onPostExecute(final ArrayList<Map<String, Object>> listViewRes) {
			listView = (ListView)findViewById(R.id.lv_add_searchresult);
			SimpleAdapter sSchedule = new SimpleAdapter(AddFriendActivity.this, listViewRes,
					R.layout.search_user_item, new String[] { "uid", "name" },
					new int[] { R.id.userlist_userid, R.id.userlist_username });

			listView.setAdapter(sSchedule);
			listView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					list_id = listViewRes.get(position).get("uid").toString();

					JudgeIsFriendTask judgeIsFriendTask =new JudgeIsFriendTask();
					judgeIsFriendTask.execute();
				}
			});

		}

		@Override
		protected ArrayList<Map<String, Object>> doInBackground(String... params){
			// TODO Auto-generated method stub
			try {
				listViewRes = SearchUserService.sendDataByHttpClientPost(url, myid, username);
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
				Intent intent = new Intent(AddFriendActivity.this,
						UserInfoActivity.class);
				intent.putExtra("list_id", list_id);
				intent.putExtra("intent_type", "FromAddFriendActivity");
				startActivity(intent);
			}else{
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
		String list_neckname = "";
		String url1 = getResources().getString(R.string.GetFriendListServletUrl)+ "&myid=" + myid;
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
			Intent intent = new Intent(AddFriendActivity.this,
					FriendInfoActivity.class);
			intent.putExtra("list_id", list_id);
			intent.putExtra("list_neckname", list_neckname);
			intent.putExtra("intent_type", "FromAddFriendActivity");
			startActivity(intent);
		}

		@Override
		protected JSONArray doInBackground(String... params){
			// TODO Auto-generated method stub
			JSONArray array = null ;
			try {
				String body = HttpCon.getContent(url1);
				array = new JSONArray(body);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return array;
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.aboutus_btn_back:{
			Intent intent = new Intent(AddFriendActivity.this,MainActivity.class);
			intent.putExtra("tabNum", "3");
			startActivity(intent);
			finish();
			break;
		}
		case R.id.addfriend_btn_search:{
			SearchUserTask searchUserTask = new SearchUserTask();
			searchUserTask.execute();
		}
		}
	}

}
