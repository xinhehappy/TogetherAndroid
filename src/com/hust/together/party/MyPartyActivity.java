package com.hust.together.party;

import java.util.ArrayList;
import java.util.Map;

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

import com.hust.together.service.party.GetPartyListService;
import com.hust.together.ui.MainActivity;
import com.hust.together.ui.R;
import com.hust.together.util.TogetherApplication;

public class MyPartyActivity extends Activity implements OnClickListener{
	private Button btn_back, btn_add;
	private ArrayList<Map<String, Object>> listViewRes = null;
	private ListView listView;
	String list_id = "";
	String list_name = "";
	String myid;
	private SharedPreferences sp_user_info = null;
	
	

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(MyPartyActivity.this,
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
		setContentView(R.layout.myparty);

		TogetherApplication.getInstance().addActivity(this);
		sp_user_info = getSharedPreferences("user_info", MODE_PRIVATE);
		myid = sp_user_info.getString("uid", "");

		initView();
		
		GetPartyListTask getPartyListTask = new GetPartyListTask();
		getPartyListTask.execute();
	}

	private void initView() {
		btn_back = (Button) findViewById(R.id.aboutus_btn_back);
		btn_add = (Button) findViewById(R.id.myparty_add);
		
		btn_back.setOnClickListener(this);
		btn_add.setOnClickListener(this);
	}

	class GetPartyListTask extends AsyncTask<String, Integer, ArrayList<Map<String, Object>>> {
		String url = getResources().getString(R.string.GetMyPartyServletUrl);
		@Override
		protected void onPostExecute(final ArrayList<Map<String, Object>> listViewRes) {
			listView = (ListView) findViewById(R.id.lv_partys);
			SimpleAdapter sSchedule = new SimpleAdapter(MyPartyActivity.this, listViewRes,
					R.layout.myparty_item, new String[] { "partyname" },
					new int[] { R.id.tv_showmyparty });

			listView.setAdapter(sSchedule);

			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					list_id = listViewRes.get(position).get("partyid").toString();
					list_name = listViewRes.get(position).get("partyname")
							.toString();
					Intent intent = new Intent(MyPartyActivity.this,
							PartyInfoActivity.class);
					intent.putExtra("list_id", list_id);
					intent.putExtra("list_name", list_name);
					startActivity(intent);
					finish();
				}

			});
		}

		@Override
		protected ArrayList<Map<String, Object>> doInBackground(String... params){
			// TODO Auto-generated method stub
			listViewRes = GetPartyListService.sendDataByHttpClientPost(url, myid);
			return listViewRes;
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.aboutus_btn_back:{
			Intent intent = new Intent(MyPartyActivity.this,
					MainActivity.class);
			intent.putExtra("tabNum", "2");
			startActivity(intent);
			finish();
			break;
		}
		case R.id.myparty_add:{
			Intent intent = new Intent(MyPartyActivity.this, AddParty.class);
			intent.putExtra("intent_type", false);
			startActivity(intent);
			finish();
			break;
		}
		}
	}
}