package com.hust.together.setting;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.hust.together.ui.LoginActivity;
import com.hust.together.ui.R;
import com.hust.together.util.TogetherApplication;

public class SettingActivity extends Activity {

	private Button btn_changeaccount, btn_myinfo, btn_help, btn_aboutus,btn_modifyuserpwd;
	
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
//            	ActivityManager activityMgr = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE ); 
//
//            	activityMgr.restartPackage(getPackageName()); 
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

		setContentView(R.layout.setting);

		btn_changeaccount = (Button) findViewById(R.id.setting_btn_changeaccount);
		btn_changeaccount.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SettingActivity.this,
						LoginActivity.class);
				startActivity(intent);
				finish();
			}
		});

		btn_modifyuserpwd = (Button) findViewById(R.id.setting_btn_modifyuserpwd);
		btn_modifyuserpwd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SettingActivity.this,
						ModifyUserPwd.class);
				startActivity(intent);
			}
		});
		
		btn_myinfo = (Button) findViewById(R.id.setting_btn_myinfo);
		btn_myinfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SettingActivity.this, Myinfo.class);
				startActivity(intent);
			}
		});

		btn_help = (Button) findViewById(R.id.setting_btn_help);
		btn_help.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SettingActivity.this, Help.class);
				startActivity(intent);
			}
		});

		btn_aboutus = (Button) findViewById(R.id.setting_btn_aboutus);
		btn_aboutus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SettingActivity.this, Aboutus.class);
				startActivity(intent);
			}
		});
	}
}
