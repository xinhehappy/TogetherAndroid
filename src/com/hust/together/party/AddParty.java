package com.hust.together.party;

import java.text.DecimalFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hust.together.myview.wheelview.DatePickWheelDialog;
import com.hust.together.service.party.AddPartyMemberService;
import com.hust.together.service.party.AddPartyService;
import com.hust.together.ui.MainActivity;
import com.hust.together.ui.R;

public class AddParty extends Activity implements OnClickListener {

	private LinearLayout linearLayout_partyName, linearLayout_partyTime,
			linearLayout_partyAddress, linearLayout_partyIntro;
	private TextView newPartyName, newPartyTime, newPartyIntro,
			newPartyAddress;
	private DatePickWheelDialog datePickWheelDialog;
	private Button btn_addok, btn_addcancel, btn_back, btn_backtomain;
	private String partyAddress = "";
	private String partyName = "";
	private String partyTime = "";
	private String partyIntro = "";
	private String lat = "";
	private String lon = "";
	private Boolean intent_type;
	Toast toast;
	String myid,info,resultId;
	private SharedPreferences sp_user_info = null;

	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(AddParty.this, MyPartyActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addparty);

		sp_user_info = getSharedPreferences("user_info", MODE_PRIVATE);
		myid = sp_user_info.getString("uid", "");

		Intent intent = getIntent();
		intent_type = intent.getBooleanExtra("intent_type", false);
		System.out.println(intent_type);
		
		initView();
		if (intent_type) {
			partyAddress = intent.getStringExtra("address");
			partyName = intent.getStringExtra("partyName");
			partyTime = intent.getStringExtra("partyTime");
			partyIntro = intent.getStringExtra("partyIntro");
			lat = intent.getStringExtra("lat");
			lon = intent.getStringExtra("lon");
//			System.out.println("使用"+partyName+partyTime+partyIntro+partyAddress+lat+lon);
			newPartyName.setText(partyName);
			newPartyTime.setText(partyTime);
			newPartyIntro.setText(partyIntro);
			newPartyAddress.setText(partyAddress);
		}

		
	}
	
	private void initView(){
		newPartyName = (TextView) findViewById(R.id.tv_newpartyname);
		info = newPartyName.getText().toString();
		
		newPartyTime = (TextView) findViewById(R.id.tv_newjuhuitime);
		newPartyIntro = (TextView) findViewById(R.id.tv_newpartyintroduction);
		newPartyAddress = (TextView) findViewById(R.id.tv_newjuhuiaddress);
		linearLayout_partyName = (LinearLayout) findViewById(R.id.addparty_ll1);
		linearLayout_partyTime = (LinearLayout) findViewById(R.id.addparty_ll2);
		linearLayout_partyAddress = (LinearLayout) findViewById(R.id.addparty_ll3);
		linearLayout_partyIntro = (LinearLayout) findViewById(R.id.addparty_ll5);
		btn_addok = (Button) findViewById(R.id.addparty_ok);
		btn_addcancel = (Button) findViewById(R.id.addparty_cancel);
		btn_back = (Button) findViewById(R.id.aboutus_btn_back);
		btn_backtomain = (Button) findViewById(R.id.btn_backtomain);
		
		linearLayout_partyName.setOnClickListener(this);
		linearLayout_partyTime.setOnClickListener(this);
		linearLayout_partyAddress.setOnClickListener(this);
		linearLayout_partyIntro.setOnClickListener(this);
		btn_addok.setOnClickListener(this);
		btn_addcancel.setOnClickListener(this);
		btn_back.setOnClickListener(this);
		btn_backtomain.setOnClickListener(this);
	}

	public static String getFormatTime(Calendar c) {
		String parten = "00";
		DecimalFormat decimal = new DecimalFormat(parten);
		// 设置日期的显示
		Calendar calendar = c;
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DATE);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		return year + "-" + decimal.format(month + 1) + "-"
				+ decimal.format(day) + " " + decimal.format(hour) + ":"
				+ decimal.format(minute);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		toast = Toast.makeText(AddParty.this, "null", Toast.LENGTH_SHORT);
		switch (v.getId()) {
		case R.id.addparty_ll1: {
			final EditText editText = new EditText(AddParty.this);
			// editText.setText(UserInfoActivity.this.findViewById(R.id.tv_showbeizhu).);
			editText.setHint(info);

			new AlertDialog.Builder(AddParty.this)
					.setTitle("设置聚会名称")
					.setIcon(android.R.drawable.ic_dialog_info)
					.setView(editText)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									String newpartyname = editText
											.getText().toString();
									newPartyName.setText(newpartyname);
								}
							}).setNegativeButton("取消", null).show();
			break;
		}
		case R.id.addparty_ll2: {
			datePickWheelDialog = new DatePickWheelDialog.Builder(
					AddParty.this)
					.setPositiveButton("确定", new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Calendar c = DatePickWheelDialog
									.getSetCalendar();
							newPartyTime.setText(getFormatTime(c));
							datePickWheelDialog.dismiss();
						}
					}).setTitle("请选择日期与时间").setNegativeButton("取消", null)
					.create();
			datePickWheelDialog.show();
			break;
		}
		case R.id.addparty_ll3: {
			Intent intent = new Intent(AddParty.this, AddPartyAddress.class);
			intent.putExtra("partyName", newPartyName.getText().toString());
			intent.putExtra("partyTime", newPartyTime.getText().toString());
			intent.putExtra("partyAddress", newPartyAddress.getText()
					.toString());
			intent.putExtra("partyIntro", newPartyIntro.getText()
					.toString());
			startActivity(intent);
			finish();
			break;
		}
		case R.id.addparty_ll5: {
			final EditText editText = new EditText(AddParty.this);
			editText.setHint("在此输入聚会简介");
			editText.setMaxLines(4);

			new AlertDialog.Builder(AddParty.this)
					.setTitle("设置聚会介绍")
					.setIcon(android.R.drawable.ic_dialog_info)
					.setView(editText)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									String newpartyintro = editText
											.getText().toString();
									newPartyIntro.setText(newpartyintro);
								}
							}).setNegativeButton("取消", null).show();
		break;
		}
		case R.id.addparty_ok:{
			AddPartyTask addPartyTask = new AddPartyTask();
			addPartyTask.execute();
			break;
		}
		case R.id.addparty_cancel:{
			Intent intent = new Intent(AddParty.this, MyPartyActivity.class);
			startActivity(intent);
			finish();
			break;
		}
		case R.id.aboutus_btn_back:{
			Intent intent = new Intent(AddParty.this, MyPartyActivity.class);
			startActivity(intent);
			finish();
			break;
		}
		case R.id.btn_backtomain:{
			Intent intent = new Intent(AddParty.this, MainActivity.class);
			intent.putExtra("tabNum", "2");
			startActivity(intent);
			finish();
			break;
		}
		}
	}
	
	class AddPartyTask extends AsyncTask<String, Integer, String> {
		String name = newPartyName.getText().toString().trim();
		String starttime = newPartyTime.getText().toString().trim();
		String note = newPartyIntro.getText().toString().trim();
		String address = newPartyAddress.getText().toString().trim();
		String url = getResources().getString(
				R.string.AddPartyServletUrl);

		@Override
		protected void onPostExecute(String result) {
			resultId = result;
			if (result.equals("0")) {
				toast.setText("添加失败！");
				toast.show();

			} else {
				toast.setText("添加成功！");
				toast.show();
				AddLeaderToMemberTask addLeaderToMemberTask = new AddLeaderToMemberTask();
				addLeaderToMemberTask.execute();
				Intent intent = new Intent(AddParty.this,
						MyPartyActivity.class);
				startActivity(intent);
				finish();
			}
		}

		@Override
		protected String doInBackground(String... params){
			// TODO Auto-generated method stub
			String result = null;
			try {
				result = AddPartyService
						.sendDataByHttpClientPost(url, myid, name, address,
								starttime, note, lat, lon);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;
		}

	}
	
	class AddLeaderToMemberTask extends AsyncTask<String, Integer, String> {
		String url = getResources().getString(
				R.string.AddPartyMemberServletUrl);

		@Override
		protected void onPostExecute(String result) {
		}

		@Override
		protected String doInBackground(String... params){
			// TODO Auto-generated method stub
			String result = null;
			try {
				result = AddPartyMemberService
						.sendDataByHttpClientPost(url, resultId,
								myid);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;
		}

	}

}
