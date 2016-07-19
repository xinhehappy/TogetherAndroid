package com.hust.together.party;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.hust.together.ui.R;
import com.hust.together.util.TogetherApplication;

public class PartyActivity extends Activity {

	private GridView gridview;
	
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
            	TogetherApplication.getInstance().exit();
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
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_party);

		ImageAdapter adapter = new ImageAdapter(this);

		gridview = (GridView) findViewById(R.id.party_gv);
		gridview.setAdapter(adapter);

		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				switch (arg2) {
				case 0:
					Intent intent0 = new Intent(PartyActivity.this,
							MyPartyActivity.class);
					startActivity(intent0);
					finish();
					break;
				case 1:
					Intent intent1 = new Intent(PartyActivity.this,
							FriendPartyActivity.class);
					startActivity(intent1);
					finish();
					break;
				case 2:
					Intent intent2 = new Intent(PartyActivity.this,
							CityPartyActivity.class);
					startActivity(intent2);
					finish();
					break;
				case 3:
					Intent intent3 = new Intent(PartyActivity.this,
							FocusPartyActivity.class);
					startActivity(intent3);
					finish();
					break;
				case 4:
					Intent intent4 = new Intent(PartyActivity.this,
							HistoryPartyActivity.class);
					startActivity(intent4);
					finish();
					break;
				case 5:
					Intent intent5 = new Intent(PartyActivity.this,
							CurrentLocationActivity.class);
					startActivity(intent5);
					finish();
					break;
				}
			}

		});

	}

}
