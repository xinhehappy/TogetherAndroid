package com.hust.together.setting;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hust.together.service.friend.ModifyUserPwdService;
import com.hust.together.ui.R;

public class ModifyUserPwd extends Activity implements OnClickListener{
	private Button btn_back,btn_ok;
	private EditText oldpwd,newpwd1,newpwd2;
	private String strOld,strNew1,strNew2;
	private String myid,userPwd;
	private SharedPreferences sp_user_info = null;
	Toast toast;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modifyuserpwd);
		
		sp_user_info = getSharedPreferences("user_info", MODE_PRIVATE);
		userPwd = sp_user_info.getString("pwd", "");
		myid = sp_user_info.getString("uid", "");
		
		initView();
	}
	
	private void initView(){
		btn_back = (Button) findViewById(R.id.aboutus_btn_back);
		btn_ok = (Button) findViewById(R.id.btn_submit);
		oldpwd = (EditText) findViewById(R.id.et_oldpwd);
		newpwd1 = (EditText) findViewById(R.id.et_newpwd);
		newpwd2 = (EditText) findViewById(R.id.et_newpwd2);
		
		btn_back.setOnClickListener(this);
		btn_ok.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		toast = Toast.makeText(ModifyUserPwd.this, "null", Toast.LENGTH_SHORT);
		switch(v.getId()){
		case R.id.aboutus_btn_back:{
			finish();
			break;
		}
		case R.id.btn_submit:{
			strOld = oldpwd.getText().toString().trim();
			strNew1 = newpwd1.getText().toString().trim();
			strNew2 = newpwd2.getText().toString().trim();

			if (strOld.equals("") == true || strNew1.equals("") == true
					|| strNew2.equals("") == true) {
				toast.setText("请确保账号与密码输入不为空！");
				toast.show();
				return;
			} else if (strOld.equals(userPwd) == false) {
				toast.setText("输入的旧密码不正确！");
				toast.show();
				return;
			} else if (strNew1.equals(strNew2) == false) {
				toast.setText("两次输入密码不一致！");
				toast.show();
				return;
			} else {
				ModifyUserPwdTask modifyUserPwdTask = new ModifyUserPwdTask();
				modifyUserPwdTask.execute();
			}
			break;
		}
		}
	}
	
	class ModifyUserPwdTask extends AsyncTask<String, Integer, String> {
		String url = getResources().getString(R.string.ModifyUserPwdServletUrl);
		// 获取新密码
		String password = newpwd1.getText().toString().trim();;

		@Override
		protected void onPostExecute(String result) {
			if (result.equals("1")) {
				toast.setText("密码修改成功！");
				toast.show();
				finish();
				return;

			} else {

			}
		}

		@Override
		protected String doInBackground(String... params){
			// TODO Auto-generated method stub
			String result = null;
			try {
				result = ModifyUserPwdService.sendDataByHttpClientPost(url,myid,password).trim();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;
		}

	}

	
	
}
