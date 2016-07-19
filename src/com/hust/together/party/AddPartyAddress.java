package com.hust.together.party;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.BDNotifyListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.hust.together.myview.MyIcon;
import com.hust.together.myview.MyIcon2;
import com.hust.together.myview.MyMapView;
import com.hust.together.ui.R;
import com.hust.together.util.TogetherApplication;

@SuppressLint("HandlerLeak")
public class AddPartyAddress extends Activity implements OnClickListener{

	public TogetherApplication app;
	static MyMapView mMapView = null;
	String partyAddr = "";
	String partyLat = "";
	String partyLon = "";
	private String partyName,partyTime,partyIntro,partyAddrNew;
	private Button btn_back, btn_add;

	public MKMapViewListener mMapListener = null;
	MyLocationOverlay myLocationOverlay = null;
	// 定位相关
	LocationClient mLocClient;
	public NotifyLister mNotifyer = null;
	public MyLocationListenner myListener = new MyLocationListenner();
	LocationData locData = null;
	private MapController mMapController = null;

	static MKSearch mkSerach;
	static TextView showAddr;

	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Toast.makeText(AddPartyAddress.this, "msg:" + msg.what,
					Toast.LENGTH_SHORT).show();
		};
	};
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		Intent intent =getIntent();
		partyName = intent.getStringExtra("partyName");
		System.out.println(partyName);
		partyTime = intent.getStringExtra("partyTime");
		partyIntro = intent.getStringExtra("partyIntro");
		partyAddr = intent.getStringExtra("partyAddress");
		partyAddrNew = partyAddr;

		
		MyIcon mi = new MyIcon(this);

		// 在屏幕中心点添加接我图标
		getWindow().addContentView(
				mi,
				new LayoutParams(LayoutParams.FILL_PARENT,
						LayoutParams.FILL_PARENT));
		MyIcon2 m2 = new MyIcon2(this);
		getWindow().addContentView(
				m2,
				new LayoutParams(LayoutParams.FILL_PARENT,
						LayoutParams.FILL_PARENT));

		initView();
		app = TogetherApplication.getInstance();
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);

		// 搜索初始化
		mkSerach = new MKSearch();
		mkSerach.init(app.mBMapManager, new MKSearchListener() {

			@Override
			public void onGetWalkingRouteResult(MKWalkingRouteResult arg0,
					int arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onGetTransitRouteResult(MKTransitRouteResult arg0,
					int arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onGetSuggestionResult(MKSuggestionResult arg0, int arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onGetPoiResult(MKPoiResult arg0, int arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onGetPoiDetailSearchResult(int arg0, int arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onGetDrivingRouteResult(MKDrivingRouteResult arg0,
					int arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onGetBusDetailResult(MKBusLineResult arg0, int arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onGetAddrResult(MKAddrInfo info, int arg1) {
				showAddr.setText(info.strAddr);
				partyAddrNew = info.strAddr;
				partyLat = info.geoPt.getLatitudeE6() + "";
				partyLon = info.geoPt.getLongitudeE6() + "";
			}
		});

		// 设置地图相关
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setCoorType("bd09ll");
		option.setScanSpan(300000);
		mLocClient.setLocOption(option);
		mLocClient.start();
		mMapView.getController().setZoom(16);
		mMapView.getController().enableClick(true);
		mMapView.displayZoomControls(true);
		mMapListener = new MKMapViewListener() {

			public void onMapMoveFinish() {

			}

			public void onClickMapPoi(MapPoi mapPoiInfo) {
				// TODO Auto-generated method stub
				String title = "";
				if (mapPoiInfo != null) {
					title = mapPoiInfo.strText;
					Toast.makeText(AddPartyAddress.this, title,
							Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onGetCurrentMap(Bitmap arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onMapAnimationFinish() {
				// TODO Auto-generated method stub

			}
		};
		TogetherApplication.getInstance();
		mMapView.regMapViewListener(app.mBMapManager, mMapListener);
		myLocationOverlay = new MyLocationOverlay(mMapView);
		locData = new LocationData();
		myLocationOverlay.setData(locData);
		// mMapView.getOverlays().add(myLocationOverlay);
		mMapView.getController().setCenter(
				new GeoPoint((int) (locData.latitude * 1e6),
						(int) (locData.longitude * 1e6)));
		myLocationOverlay.enableCompass();
		mMapView.refresh();
	}
	
	private void initView(){
		btn_back = (Button) findViewById(R.id.aboutus_btn_back);
		btn_add = (Button) findViewById(R.id.btn_addpartyaddress);
		showAddr = (TextView) findViewById(R.id.showAddr);
		
		mMapView = (MyMapView) findViewById(R.id.bmapsView);
		
		btn_back.setOnClickListener(this);
		btn_add.setOnClickListener(this);

		mMapView.setLongClickable(true);
		mMapController = mMapView.getController();
	}

	/**
	 * 监听函数，又新位置的时候，格式化成字符串，输出到屏幕中
	 */
	public class MyLocationListenner implements BDLocationListener {
		public void onReceiveLocation(BDLocation location) {
			Log.i("================", " null ============ null" + location);
			if (location == null)
				return;
			Log.i("================", "not null ============not null");
			// 31.192695,121.42712,3000
			locData.latitude = location.getLatitude();
			locData.longitude = location.getLongitude();
			locData.direction = 2.0f;
			locData.accuracy = location.getRadius();
			locData.direction = location.getDerect();
			Log.d("loctest",
					String.format("before: lat: %f lon: %f",
							location.getLatitude(), location.getLongitude()));
			myLocationOverlay.setData(locData);
			mMapView.refresh();

			mMapController
					.animateTo(new GeoPoint((int) (locData.latitude * 1e6),
							(int) (locData.longitude * 1e6)), mHandler
							.obtainMessage(1));
		}

		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null) {
				return;
			}
		}
	}

	public class NotifyLister extends BDNotifyListener {
		public void onNotify(BDLocation mlocation, float distance) {
		}
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mMapView.onRestoreInstanceState(savedInstanceState);
	}

	public static void getPosition(GeoPoint g) {
		mkSerach.reverseGeocode(g);
		showAddr.setText("获取位置中...");
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.aboutus_btn_back:{
			Intent intent = new Intent(AddPartyAddress.this,AddParty.class);
			System.out.println("后退"+partyName+partyTime+partyIntro+partyAddr);
			intent.putExtra("intent_type", true);
			intent.putExtra("partyName", partyName);
			intent.putExtra("partyTime", partyTime);
			intent.putExtra("partyIntro", partyIntro);
			intent.putExtra("address", partyAddr);
			intent.putExtra("lat", "");
			intent.putExtra("lon", "");
			startActivity(intent);
			finish();
			break;
		}
		case R.id.btn_addpartyaddress:{
			Intent intent = new Intent(AddPartyAddress.this,AddParty.class);
			System.out.println("使用"+partyName+partyTime+partyIntro+partyAddrNew+partyLat+partyLon);
			intent.putExtra("intent_type", true);
			intent.putExtra("partyName", partyName);
			intent.putExtra("partyTime", partyTime);
			intent.putExtra("partyIntro", partyIntro);
			intent.putExtra("address", partyAddrNew);
			intent.putExtra("lat", partyLat);
			intent.putExtra("lon", partyLon);
			startActivity(intent);
			finish();
			break;
		}
		}
	}

}
