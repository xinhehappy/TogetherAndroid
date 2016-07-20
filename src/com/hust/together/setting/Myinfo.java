package com.hust.together.setting;

import java.text.DecimalFormat;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hust.together.myview.wheelview.DatePickWheelDialog1;
import com.hust.together.service.friend.GetFriendInfoServicec;
import com.hust.together.service.friend.ModifyUserInfoService;
import com.hust.together.ui.R;

@SuppressLint("ShowToast")
public class Myinfo extends Activity {
	private DatePickWheelDialog1 datePickWheelDialog1;
	private TextView uname,addr,gender,intro,birth,email;
	private LinearLayout addr1,gender1,intro1,birth1,email1;
	private Button btn_back,btn_ok;
	private Spinner province_spinner;
	private Spinner city_spinner;
	private Integer provinceId;
	private Toast toast;
	private String myid,myname, strProvince, strCity ,strHomeAddr;
	
	private int[] city = {R.array.beijin_province_item, R.array.tianjin_province_item, R.array.heibei_province_item, R.array.shanxi1_province_item, R.array.neimenggu_province_item, R.array.liaoning_province_item, R.array.jilin_province_item, R.array.heilongjiang_province_item, R.array.shanghai_province_item, R.array.jiangsu_province_item, R.array.zhejiang_province_item, R.array.anhui_province_item, R.array.fujian_province_item, R.array.jiangxi_province_item, R.array.shandong_province_item, R.array.henan_province_item, R.array.hubei_province_item, R.array.hunan_province_item, R.array.guangdong_province_item,  R.array.guangxi_province_item, R.array.hainan_province_item, R.array.chongqing_province_item, R.array.sichuan_province_item, R.array.guizhou_province_item, R.array.yunnan_province_item, R.array.xizang_province_item, R.array.shanxi2_province_item, R.array.gansu_province_item, R.array.qinghai_province_item, R.array.linxia_province_item, R.array.xinjiang_province_item, R.array.hongkong_province_item, R.array.aomen_province_item, R.array.taiwan_province_item};
	
	private ArrayAdapter<CharSequence> province_adapter;
	private ArrayAdapter<CharSequence> city_adapter;
	
	private SharedPreferences sp_user_info = null;
	
	

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myinfo);
		
		toast = Toast.makeText(Myinfo.this, "null", Toast.LENGTH_SHORT);
		
		sp_user_info = getSharedPreferences("user_info", MODE_PRIVATE);
		myname = sp_user_info.getString("uname", "");
		myid = sp_user_info.getString("uid", "");
		
		uname = (TextView) findViewById(R.id.tv_showneckname);
		uname.setText(myname);

		btn_back = (Button) findViewById(R.id.aboutus_btn_back);
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		btn_ok = (Button) findViewById(R.id.btn_add);
		btn_ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ModifyUserInfoTask modifyUserInfoTask = new ModifyUserInfoTask();
				modifyUserInfoTask.execute();
			}
		});
		
		gender=(TextView) findViewById(R.id.tv_showgengder);
		gender1= (LinearLayout) findViewById(R.id.ll_gender);
		gender1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(Myinfo.this).setTitle("性别").setSingleChoiceItems(R.array.gender, 0, new DialogInterface.OnClickListener(){
	                public void onClick(DialogInterface dialog, int which) {
	                	String genders=getResources().getStringArray(R.array.gender)[which];
	                    gender.setText(genders);
	                }
	            }).setPositiveButton("确定", new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
								}
							}).show();
			}
		});
		
		addr = (TextView) findViewById(R.id.tv_showaddress);
		addr1= (LinearLayout) findViewById(R.id.ll_addr);
		addr1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LayoutInflater factory=LayoutInflater.from(Myinfo.this);
				  final View v1=factory.inflate(R.layout.homespinner,null);
				  province_spinner = (Spinner) v1.findViewById(R.id.province_spinner);
				  city_spinner = (Spinner) v1.findViewById(R.id.city_spinner);
				  AlertDialog.Builder dialog=new AlertDialog.Builder(Myinfo.this);
				  
				  dialog.setTitle("所在地");
				  dialog.setView(v1);        
				  dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				            public void onClick(DialogInterface dialog, int whichButton) {
				                addr.setText(strHomeAddr);
				            }});
				        dialog.setNegativeButton("取消",new DialogInterface.OnClickListener() {

				   @Override
				   public void onClick(DialogInterface dialog, int which) {
				    // TODO Auto-generated method stub

				   }
				  });
				        
				       
				          
				dialog.show();
				loadSpinner();

			}
		});
		
		intro =(TextView) findViewById(R.id.tv_showintroduction);
		intro1= (LinearLayout) findViewById(R.id.ll_intro);
		intro1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final EditText editText = new EditText(Myinfo.this);
				editText.setHint("在此输入简介");
				editText.setMaxLines(4);

				new AlertDialog.Builder(Myinfo.this)
						.setTitle("设置个人介绍")
						.setIcon(android.R.drawable.ic_dialog_info)
						.setView(editText)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										String newintro = editText
												.getText().toString();
										intro.setText(newintro);
									}
								}).setNegativeButton("取消", null).show();
			}
		});
		
		birth = (TextView) findViewById(R.id.tv_showbirthday);
		birth1= (LinearLayout) findViewById(R.id.ll_birth);
		birth1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				datePickWheelDialog1 = new DatePickWheelDialog1.Builder(
						Myinfo.this)
						.setPositiveButton("确定", new View.OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								Calendar c = DatePickWheelDialog1
										.getSetCalendar();
								birth.setText(getFormatTime(c));
								datePickWheelDialog1.dismiss();
							}
						}).setTitle("请选择生日日期").setNegativeButton("取消", null)
						.create();
				datePickWheelDialog1.show();
			}
		});
		
		email = (TextView) findViewById(R.id.tv_showemail);
		email1= (LinearLayout) findViewById(R.id.ll_email);
		email1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final EditText editText = new EditText(Myinfo.this);
				editText.setHint("输入您的邮箱");
				editText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

				new AlertDialog.Builder(Myinfo.this)
						.setTitle("设置邮箱")
						.setIcon(android.R.drawable.ic_dialog_info)
						.setView(editText)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										String newemail = editText.getText()
												.toString();
										email.setText(newemail);
									}
								}).setNegativeButton("取消", null).show();
			}
		});
		
		GetMyInfoTask getMyInfoTask = new GetMyInfoTask();
		getMyInfoTask.execute();
	}
	
	public static String getFormatTime(Calendar c) {
		String parten = "00";
		DecimalFormat decimal = new DecimalFormat(parten);
		// 设置日期的显示
		Calendar calendar = c;
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DATE);
		return year + "-" + decimal.format(month + 1) + "-"
				+ decimal.format(day);

	}
	
	private void loadSpinner()
	{
		province_spinner.setPrompt("请选择省份");
		province_adapter = ArrayAdapter.createFromResource(this, R.array.province_item, android.R.layout.simple_spinner_item);
		province_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	province_spinner.setAdapter(province_adapter);
    	province_spinner.setOnItemSelectedListener(new OnItemSelectedListener() 
    	{	
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) 
			{					
				provinceId = province_spinner.getSelectedItemPosition();
				strProvince = province_spinner.getSelectedItem().toString();
				if(true)
				{	
					Log.v("test", "province: "+province_spinner.getSelectedItem().toString()+provinceId.toString());
					city_spinner.setPrompt("请选择城市");
					select(city_spinner, city_adapter, city[provinceId]);
					city_spinner.setOnItemSelectedListener(new OnItemSelectedListener() 
					{

						@Override
						public void onItemSelected(AdapterView<?> arg0,
								View arg1, int arg2, long arg3) {
							strCity = city_spinner.getSelectedItem().toString();
							strHomeAddr = strProvince+"-"+strCity;
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
							// TODO Auto-generated method stub
							
						}

					});							
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});
		
	}
		
	private void select(Spinner spin, ArrayAdapter<CharSequence> adapter, int arry)
	{
		adapter = ArrayAdapter.createFromResource(this, arry, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spin.setAdapter(adapter);
		//spin.setSelection(0,true);
	}
	
	class ModifyUserInfoTask extends AsyncTask<String, Integer, String> {
		String url = getResources().getString(R.string.ModifyUserInfoServletUrl);
		String strGender = (gender.getText().toString()).trim();
		String strAddr = (addr.getText().toString()).trim();
		String strIntro = (intro.getText().toString()).trim();
		String strBirth = (birth.getText().toString()).trim();
		String strEmail = (email.getText().toString()).trim();

		@Override
		protected void onPostExecute(String result) {
			if (result.equals("0")) {
				return;

			} else {
				System.out.println("Success!");
				toast.setText("您的个人资料已更新！");
				toast.show();
				finish();

			}
		}

		@Override
		protected String doInBackground(String... params){
			// TODO Auto-generated method stub
			String result = null;
			try {
				result = ModifyUserInfoService.sendDataByHttpClientPost(url, myid, strGender, strAddr, strBirth, strEmail, strIntro).trim();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;
		}

	}
	
	class GetMyInfoTask extends AsyncTask<String, Integer, JSONObject> {
		String url = getResources().getString(R.string.GetFriendInfoServletUrl);
		@Override
		protected void onPostExecute(JSONObject object) {
			try {
				intro.setText(object.getString("sign"));
				gender.setText(object.getString("gender"));
				addr.setText(object.getString("home"));
				birth.setText(object.getString("birthday"));
				email.setText(object.getString("email"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		protected JSONObject doInBackground(String... params){
			// TODO Auto-generated method stub
			JSONObject object = null;
			try {
				object = GetFriendInfoServicec.sendDataByHttpClientPost(url,
						myid);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return object;
		}

	}
}
