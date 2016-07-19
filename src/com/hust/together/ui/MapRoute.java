package com.hust.together.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.RouteOverlay;
import com.baidu.mapapi.map.TransitOverlay;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPlanNode;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKRoute;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKStep;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.hust.together.util.TogetherApplication;

public class MapRoute extends Activity {
	public TogetherApplication app;
	private MKSearch mkSearch;

	private GeoPoint myLocGeopoint;
	private GeoPoint endGeopoint;

	private Button btn_back, btn_get;
	private String city;

	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private BMapManager mapManager;
	private MapView mMapView;
	private MapController mMapController;

	private LocationData locData;
	private MyLocationOverlay mLocationOverlay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.map_route);

		mapManager = new BMapManager(getApplication());
		mapManager.init("6A2FDB2DDA25BD9EF8031BEA5E59A47BE83A8053", null);
		// 华科 114.423925,30.515522
		// 武大 114.369523,30.542462
		int lat = (int) (30.542462 * 1e6);
		int lng = (int) (114.369523 * 1e6);
		endGeopoint = new GeoPoint(lat, lng);

		mMapView = (MapView) this.findViewById(R.id.bmapView);
		mMapController = mMapView.getController();

		mMapView.setBuiltInZoomControls(true);
		mMapController.setCenter(endGeopoint);
		mMapController.enableClick(true);
		mMapController.setZoom(19);
		mMapView.setBuiltInZoomControls(true);
		mMapView.setDoubleClickZooming(true);

		mkSearch = new MKSearch();
		mkSearch.init(mapManager, new SearchListener());

		mkSearch.reverseGeocode(endGeopoint);

		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);

		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(100);
		option.setAddrType("all");

		mLocClient.setLocOption(option);
		mLocClient.start();

		if (mLocClient.isStarted())
			mLocClient.requestLocation();

		locData = new LocationData();
		mLocationOverlay = new MyLocationOverlay(mMapView);
		mLocationOverlay.setData(locData);
		mLocationOverlay.enableCompass();

		mMapView.getOverlays().add(mLocationOverlay);

		btn_back = (Button) findViewById(R.id.aboutus_btn_back);
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		btn_get = (Button) findViewById(R.id.btn_get);
		btn_get.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				MKPlanNode start = new MKPlanNode();
				start.pt = myLocGeopoint;

				MKPlanNode end = new MKPlanNode();
				end.pt = endGeopoint;
				mkSearch.drivingSearch(city, start, city, end);
				// mkSearch.transitSearch(city, start, end);
			}
		});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		mMapView.destroy();
		this.getApplication();
		if (app.mBMapManager != null) {
			app.mBMapManager.destroy();
			app.mBMapManager = null;
		}
		super.onDestroy();
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

	class SearchListener implements MKSearchListener {

		@Override
		public void onGetAddrResult(MKAddrInfo info, int errCode) {
			// System.out.println("onGetAddrResult.....................errCode ="
			// + errCode);
			// mTxtShopLocation.setText(info.strAddr);
		}

		@Override
		public void onGetBusDetailResult(MKBusLineResult arg0, int arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onGetDrivingRouteResult(MKDrivingRouteResult res, int error) {
			// TODO Auto-generated method stub
			// 错误号可参考MKEvent中的定义
			if (error != 0 || res == null) {
				Toast.makeText(MapRoute.this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			RouteOverlay routeOverlay = new RouteOverlay(MapRoute.this,
					mMapView);
			// 显示节点信息
			MKRoute route = res.getPlan(0).getRoute(0);
			int distanceM = route.getDistance();
			String distanceKm = String.valueOf(distanceM / 1000) + "."
					+ String.valueOf(distanceM % 1000);
			System.out.println("距离:" + distanceKm + "公里---节点数量:"
					+ route.getNumSteps());
			for (int i = 0; i < route.getNumSteps(); i++) {
				MKStep step = route.getStep(i);
				System.out.println("节点信息：" + step.getContent());
			}
			// 此处仅展示一个方案作为示例
			routeOverlay.setData(res.getPlan(0).getRoute(0));
			mMapView.getOverlays().clear();
			mMapView.getOverlays().add(routeOverlay);
			mMapView.refresh();
			// 使用zoomToSpan()绽放地图，使路线能完全显示在地图上
			mMapView.getController().zoomToSpan(routeOverlay.getLatSpanE6(),
					routeOverlay.getLonSpanE6());
			mMapView.getController().animateTo(res.getStart().pt);
		}

		@Override
		public void onGetPoiDetailSearchResult(int arg0, int arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onGetPoiResult(MKPoiResult arg0, int arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onGetSuggestionResult(MKSuggestionResult arg0, int arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onGetTransitRouteResult(MKTransitRouteResult res, int error) {
			// TODO Auto-generated method stub
			if (error != 0 || res == null) {
				Toast.makeText(MapRoute.this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			TransitOverlay routeOverlay = new TransitOverlay(MapRoute.this,
					mMapView);
			// 显示节点信息
			// MKRoute route = res.getPlan(0).getRoute(0);
			// int distanceM = route.getDistance();
			// String distanceKm = String.valueOf(distanceM / 1000)
			// +"."+String.valueOf(distanceM % 1000);
			// System.out.println("距离:"+distanceKm+"公里---节点数量:"+route.getNumSteps());
			// for (int i = 0; i < route.getNumSteps(); i++) {
			// MKStep step = route.getStep(i);
			// System.out.println("节点信息："+step.getContent());
			// }

			// 此处仅展示一个方案作为示例
			routeOverlay.setData(res.getPlan(0));
			mMapView.getOverlays().clear();
			mMapView.getOverlays().add(routeOverlay);
			mMapView.refresh();
			// 使用zoomToSpan()绽放地图，使路线能完全显示在地图上
			mMapView.getController().zoomToSpan(routeOverlay.getLatSpanE6(),
					routeOverlay.getLonSpanE6());
			mMapView.getController().animateTo(res.getStart().pt);
		}

		@Override
		public void onGetWalkingRouteResult(MKWalkingRouteResult arg0, int arg1) {
			// TODO Auto-generated method stub

		}

	}

	public class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;

			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());

			sb.append("\naddr : ");
			sb.append(location.getAddrStr());

			System.out.println(sb.toString());

			city = location.getCity();
			myLocGeopoint = new GeoPoint((int) (location.getLatitude() * 1e6),
					(int) (location.getLongitude() * 1e6));

			// mTxtMyLocation.setText(location.getAddrStr());

			locData.latitude = location.getLatitude();
			locData.longitude = location.getLongitude();
			// locData.accuracy = location.getRadius();
			locData.direction = location.getDerect();
			mLocationOverlay.setData(locData);

			mMapView.refresh();
			mMapController.animateTo(new GeoPoint(
					(int) (locData.latitude * 1e6),
					(int) (locData.longitude * 1e6)));
		}

		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null) {
				return;
			}
		}
	}

}
