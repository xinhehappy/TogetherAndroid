package com.hust.together.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hust.together.service.friend.RegisterService;

public class RegisterActivity extends Activity implements OnClickListener {

	private EditText usernameEditText, passwordEditText,
			passwordConfirmEditText;
	private Button btn_back, btn_submit;
	String username, password, passwordConfirm, url;
	Toast toast =null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		usernameEditText = (EditText) findViewById(R.id.et_register_mail);
		passwordEditText = (EditText) findViewById(R.id.et_register_pwd);
		passwordConfirmEditText = (EditText) findViewById(R.id.et_register_pwd2);

		btn_submit = (Button) findViewById(R.id.btn_register_submit);
		btn_submit.setOnClickListener(this);

		btn_back = (Button) findViewById(R.id.aboutus_btn_back);
		btn_back.setOnClickListener(this);
	}

	public void onClick(View v) {
		toast = Toast.makeText(RegisterActivity.this, "null", Toast.LENGTH_SHORT);
		switch (v.getId()) {
		case R.id.btn_register_submit: {
			username = usernameEditText.getText().toString().trim();
			password = passwordEditText.getText().toString().trim();
			passwordConfirm = passwordConfirmEditText.getText().toString()
					.trim();

			if (username.equals("") == true || password.equals("") == true
					|| passwordConfirm.equals("") == true) {
				toast.setText("请确保账号与密码输入不为空！");
				toast.show();
				return;
			} else if (password.equals(passwordConfirm) == false) {
				toast.setText("两次输入密码不一致！");
				toast.show();
				return;
			} else {
				RegisterTask registerTask = new RegisterTask();
				registerTask.execute();
			}
			break;
		}
		case R.id.aboutus_btn_back: {
			Intent intent = new Intent(RegisterActivity.this,
					TogetherActivity.class);
			startActivity(intent);
			finish();
		}
		}
	}
	
	class RegisterTask extends AsyncTask<String, Integer, String> {
		String url = getResources().getString(R.string.RegisterServletUrl);
		// 获取用户名
		String username = (usernameEditText.getText().toString()).trim();
		// 获取密码
		String password = (passwordEditText.getText().toString()).trim();

		@Override
		protected void onPostExecute(String result) {
//			if (result.equals("0")) {
//				toast.setText("该用户已存在！");
//				toast.show();
//				return;
//
//			} else {
//				toast.setText("注册成功！");
//				toast.show();
				Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
				startActivity(intent);
				finish();
//
//			}
		}

		@Override
		protected String doInBackground(String... params){
			String result = null;
			try {
				result = RegisterService.sendDataByHttpClientPost(url, username, password).trim();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

	}
	
}
