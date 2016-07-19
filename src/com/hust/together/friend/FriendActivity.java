package com.hust.together.friend;

import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
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
import com.hust.together.ui.R;

public class FriendActivity extends Activity {
	private Button btn_add;
	private ArrayList<Map<String, Object>> listViewRes = null;
	private ListView listView;
	String list_id = "";
	String list_neckname = "";
	String myid;
	private String userId;
	private SharedPreferences sp_user_info = null;
	private static boolean isExit=false;
    private static boolean hasTask=false;
    Timer tExit=new Timer();
    TimerTask task=new TimerTask() {
        
        @Override
        public void run() {
            // TODO Auto-generated method stub
            isExit=false;
            hasTask=true;
        }
    };
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            //exitDialog();
            if(isExit==false)
            {
                isExit=true;
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show(); 
                if(!hasTask) {
                    tExit.schedule(task, 2000);
                }
            }
            else
            {
            	System.exit(0);
            }
            
        }
        //return true;
        return false;
        
    }

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_friend);
		
		sp_user_info = getSharedPreferences("user_info", MODE_PRIVATE);
		userId = sp_user_info.getString("uid", "");
	  
		GetFriendListTask getFriendListTask = new GetFriendListTask();
		getFriendListTask.execute();

		btn_add = (Button) findViewById(R.id.btn_add);
		btn_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(FriendActivity.this,
						AddFriendActivity.class);
				startActivity(intent);
				finish();
			}
		});

	}

	class GetFriendListTask extends AsyncTask<String, Integer, ArrayList<Map<String, Object>>> {
		String url = getResources().getString(R.string.GetFriendListServletUrl);
		

		@Override
		protected void onPostExecute(final ArrayList<Map<String, Object>> listViewRes) {
			listView = (ListView) findViewById(R.id.lv_friends);
			SimpleAdapter sSchedule =new SimpleAdapter(FriendActivity.this, listViewRes,
					R.layout.friend_item, new String[] { "fid", "name" }, 
					new int[] { R.id.friendlist_fid, R.id.friendlist_neckname });

			listView.setAdapter(sSchedule);

			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					list_id = listViewRes.get(position).get("fid").toString();
					list_neckname = listViewRes.get(position).get("name")
							.toString();
					// System.out.println("list_id------->"+list_id);
					Intent intent = new Intent(FriendActivity.this,
							FriendInfoActivity.class);
					intent.putExtra("list_id", list_id);
					intent.putExtra("list_neckname", list_neckname);
					intent.putExtra("intent_type", "FromFriendActivity");
					startActivity(intent);
					finish();
				}

			});
		}

		@Override
		protected ArrayList<Map<String, Object>> doInBackground(String... params){
			// TODO Auto-generated method stub
			listViewRes = GetFriendListService.sendDataByHttpClientPost(url, userId);
			return listViewRes;
		}

	}
}