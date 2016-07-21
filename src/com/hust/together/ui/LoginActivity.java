package com.hust.together.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hust.together.service.friend.LoginService;
import com.hust.together.util.SharedPreTool;

public class LoginActivity extends Activity implements OnClickListener {
	private EditText usernameEditText, passwordEditText;
	private Button btn_signin, btn_back;
	public SharedPreferences sp;
	Toast toast =null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);

		sp = getSharedPreferences("user_info", MODE_PRIVATE);

		usernameEditText = (EditText) findViewById(R.id.login_et_username);
		passwordEditText = (EditText) findViewById(R.id.login_et_password);
		btn_signin = (Button) findViewById(R.id.login_btn_signin);
		btn_back = (Button) findViewById(R.id.aboutus_btn_back);

		btn_back.setOnClickListener(this);
		btn_signin.setOnClickListener(this);

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.aboutus_btn_back: {
			Intent intent = new Intent(LoginActivity.this,
					TogetherActivity.class);
			startActivity(intent);
			finish();
			break;
		}
		case R.id.login_btn_signin: {
			// 获取用户名
			String username = (usernameEditText.getText().toString()).trim();
			// 获取密码
			String password = (passwordEditText.getText().toString()).trim();
			toast = Toast.makeText(LoginActivity.this, "null", Toast.LENGTH_SHORT); 
			if ("".equals(username) || "".equals(password)) {
				toast.setText("用户名或密码不能为空！");
				toast.show();
				return;
			}
			LoginTask loginTask = new LoginTask();
			loginTask.execute();
		}
		}
	}

	class LoginTask extends AsyncTask<String, Integer, String> {
		String url = getResources().getString(R.string.LoginServletUrl);
		// 获取用户名
		String username = (usernameEditText.getText().toString()).trim();
		// 获取密码
		String password = (passwordEditText.getText().toString()).trim();

		@Override
		protected void onPostExecute(String result) {
//			if (result.equals("0")) {
//				toast.setText("邮箱或密码错误！");
//				toast.show();
//				return;
//
//			} else {
				toast.setText("成功登陆！");
				toast.show();
				SharedPreTool.saveUserInfo(result,username,password,sp);
				Intent intent = new Intent(LoginActivity.this,
						MainActivity.class);
				startActivity(intent);
				finish();
//
//			}
		}

		@Override
		protected String doInBackground(String... params){
			// TODO Auto-generated method stub
			String result = null;
			try {
				result = LoginService.sendDataByHttpClientPost(url, username, password).toString().trim();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;
		}

	}
	

}
