package com.hust.together.tool;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.hust.together.service.friend.UpdateLocationService;
import com.hust.together.ui.R;

public class LocationService extends Service{
	private static final String TAG = "LocationService";
	private LocationClient locationClient = null;
	private static final int UPDATE_TIME = 10000;
	private String userId,lat,lon;
	private SharedPreferences sp_user_info = null;
	Toast toast = null;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		Log.i(TAG, "LocationService-->onCreate");
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		Log.i(TAG, "LocationService-->onDestroy");
		locationClient.stop();
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(TAG, "LocationService-->onStartCommand");
		
		sp_user_info = getSharedPreferences("user_info", MODE_PRIVATE);
		userId = sp_user_info.getString("uid", "");
		
		locationClient = new LocationClient(this);
        //设置定位条件
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);		//是否打开GPS
        option.setCoorType("bd09ll");		//设置返回值的坐标类型。
        option.setPriority(LocationClientOption.NetWorkFirst);	//设置定位优先级
        option.setProdName("LocationDemo");	//设置产品线名称。强烈建议您使用自定义的产品线名称，方便我们以后为您提供更高效准确的定位服务。
        option.setScanSpan(UPDATE_TIME);//设置定时定位的时间间隔。单位毫秒
        locationClient.setLocOption(option);
        
        //注册位置监听器
        locationClient.registerLocationListener(new BDLocationListener() {
			
			@Override
			public void onReceiveLocation(BDLocation location) {
				// TODO Auto-generated method stub
				if (location == null) {
					return;
				}
				lat = (int)(location.getLatitude()*1e6)+"";
				lon = (int)(location.getLongitude()*1e6)+"";
				LocationUpdateTask locationUpdateTask = new LocationUpdateTask();
				locationUpdateTask.execute();
			}
			
			@Override
			public void onReceivePoi(BDLocation location) {
			}
			
		});
        locationClient.start();
       
		return super.onStartCommand(intent, flags, startId);
	}
	
	class LocationUpdateTask extends AsyncTask<String, Integer, String> {
		String url = getResources().getString(R.string.UpdateUserLocServletUrl);

		@Override
		protected void onPostExecute(String result) {
//			if(result != null){
//				if (result.equals("1")) {
//					System.out.println("位置更新成功！");
//					toast.setText("位置更新成功");
//					return;
//
//				} else {
//					System.out.println("位置更新失败！");
//					toast.setText("位置更新失败");
//
//				}
//			}else{
//				toast.setText("服务器返回结果为空");
//			}
			
		}

		@Override
		protected String doInBackground(String... params){
			String result = null;
			try {
				result = UpdateLocationService.sendDataByHttpClientPost(url, userId, lat, lon).trim();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

	}
	
	

}
