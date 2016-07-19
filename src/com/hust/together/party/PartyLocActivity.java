//package com.hust.together.party;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.graphics.drawable.Drawable;
//import android.os.Bundle;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.Window;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.baidu.mapapi.BMapManager;
//import com.baidu.mapapi.map.ItemizedOverlay;
//import com.baidu.mapapi.map.MapController;
//import com.baidu.mapapi.map.MapView;
//import com.baidu.mapapi.map.MapView.LayoutParams;
//import com.baidu.mapapi.map.OverlayItem;
//import com.baidu.mapapi.map.PopupClickListener;
//import com.baidu.mapapi.map.PopupOverlay;
//import com.baidu.platform.comapi.basestruct.GeoPoint;
//import com.hust.together.ui.R;
//import com.hust.together.ui.RouteActivity;
//
//public class PartyLocActivity extends Activity {
//
//	private BMapManager mapManager;
//	private static MapView mMapView = null;
//	private MapController mMapController = null;
//	private String partyLat,partyLon,partyName;
//	private View mPopView;
//	private static GeoPoint point;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//
//		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//		mapManager = new BMapManager(getApplication());
//		mapManager.init("6A2FDB2DDA25BD9EF8031BEA5E59A47BE83A8053", null);
//
//		setContentView(R.layout.activity_friend_loc);
//
//		
//		Intent intent = getIntent();
//		partyName = intent.getStringExtra("partyName");
//		partyLat = intent.getStringExtra("partyLat");
//		partyLon = intent.getStringExtra("partyLon");
//		
//		System.out.println(partyName +"-----"+ partyLat +"-----"+ partyLon);
//		
//		// ���� 114.423925,30.515522
//		// ��� 114.369523,30.542462
//
//		int lat = (int) (30.542462 * 1e6);
//		int lng = (int) (114.369523 * 1e6);
//		
////		
////		int lat = (int) (Double.parseDouble(partyLat)*1e6);
////		int lng = (int) (Double.parseDouble(partyLon)*1e6);
//
//		point = new GeoPoint(lat, lng);
//
//		mMapView = (MapView) findViewById(R.id.bmapView);
//		mMapController = mMapView.getController();
//
//		// 113.327988,23.136733;
//
//		/*
//		 * int lon = (int) (113.327988 * 1e6); int lat = (int) (23.136733 *
//		 * 1e6);
//		 * 
//		 * GeoPoint point = new GeoPoint(lat, lon);
//		 */
//
//		mMapView.setBuiltInZoomControls(true);
//		mMapController.setCenter(point);
//		mMapController.setZoom(17);
//
//		mPopView = getLayoutInflater().inflate(R.layout.popview, null);
//		mMapView.addView(mPopView, new MapView.LayoutParams(
//				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, null,
//				MapView.LayoutParams.TOP_LEFT));
//		mPopView.setVisibility(View.GONE);
//
//		Drawable marker = getResources().getDrawable(R.drawable.icon_marka);
//		OverlayItem item = new OverlayItem(point, "����������", "���Ǻ���λ�ã�");
//
//		FriendLocOverlay overlay = new FriendLocOverlay(marker, mMapView);
//		mMapView.getOverlays().clear();
//		mMapView.getOverlays().add(overlay);
//
//		overlay.addItem(item);
//		mMapView.refresh();
//	}
//
//	protected boolean isRouteDisplayed() {
//		return false;
//
//	}
//
//	@Override
//	protected void onDestroy() {
//		if (mapManager != null) {
//			mapManager.destroy();
//			mapManager = null;
//		}
//		super.onDestroy();
//	}
//
//	@Override
//	protected void onPause() {
//		if (mapManager != null) {
//			mapManager.stop();
//		}
//		super.onPause();
//	}
//
//	@Override
//	protected void onResume() {
//		if (mapManager != null) {
//			mapManager.start();
//		}
//		super.onResume();
//	}
//
//	class FriendLocOverlay extends ItemizedOverlay<OverlayItem> {
//
//		PopupOverlay pop = null;
//		Toast mToast = null;
//
//		public FriendLocOverlay(Drawable drawable, MapView mapView) {
//			super(drawable, mapView);
//			pop = new PopupOverlay(mapView, new PopupClickListener() {
//
//				@Override
//				public void onClickedPopup(int index) {
//					if (null == mToast)
//						mToast = Toast.makeText(getBaseContext(),
//								"popup item :" + index + " is clicked.",
//								Toast.LENGTH_SHORT);
//					else
//						mToast.setText("popup item :" + index + " is clicked.");
//					mToast.show();
//				}
//			});
//			// TODO Auto-generated constructor stub
//		}
//
//		public boolean onTap(GeoPoint pt, MapView mapView) {
//			mPopView.setVisibility(View.GONE);
//			return super.onTap(pt, mapView);
//		}
//
//		@Override
//		protected boolean onTap(int index) {
//			
//
//			int lat = (int) (30.542462 * 1e6);
//			int lng = (int) (114.369523 * 1e6);
//			
//			
////			int lat = (int) (Double.parseDouble(partyLat)*1e6);
////			int lng = (int) (Double.parseDouble(partyLon)*1e6);
//
//			GeoPoint point1 = new GeoPoint(lat, lng);
//			// pop.showPopup(bmps, point1, 32);
//			//
//			// return true;
//
//			mMapView.updateViewLayout(mPopView, new MapView.LayoutParams(
//					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
//					point1, MapView.LayoutParams.BOTTOM_CENTER));
//			mPopView.setVisibility(View.VISIBLE);
//
//			TextView title = (TextView) mPopView
//					.findViewById(R.id.txt_shopname);
//			title.setText(partyName);
//
//			Button btnCar = (Button) mPopView.findViewById(R.id.btn_car);
//			Button btnGo = (Button) mPopView.findViewById(R.id.btn_go_info);
//
//			btnCar.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//
//				}
//			});
//			btnGo.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					Intent intent = new Intent(PartyLocActivity.this,
//							RouteActivity.class);
//					startActivity(intent);
//					finish();
//				}
//			});
//
//			return true;
//
//		}
//
//	}
//}
//
//
//

package com.hust.together.party;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.RouteOverlay;
import com.baidu.mapapi.map.TransitOverlay;
import com.baidu.mapapi.map.MapView.LayoutParams;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.PopupOverlay;
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
import com.hust.together.ui.R;

public class PartyLocActivity extends Activity {
	private Button btn_back;
	private TextView title;
	public MKMapViewListener mMapListener = null;
	private BMapManager mapManager;
	private static MapView mMapView = null;
	private MapController mMapController = null;
	private String partyLat, partyLon, partyName;
	private View mPopView;
	private static GeoPoint point;
	private MKSearch mkSearch;
	private LocationData locData;
	private MyLocationOverlay mLocationOverlay;
	
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();

	private GeoPoint myLocGeopoint;
	private GeoPoint endGeopoint;
	private String city;

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		mapManager = new BMapManager(getApplication());
		mapManager.init("6A2FDB2DDA25BD9EF8031BEA5E59A47BE83A8053", null);

		setContentView(R.layout.activity_friend_loc);
		
		title=(TextView) findViewById(R.id.txt_vb_title);
		title.setText("�ۻ�ص�");
		btn_back =(Button) findViewById(R.id.aboutus_btn_back);
		btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		mMapView = (MapView) findViewById(R.id.bmapView);
		mMapController = mMapView.getController();
		mMapController.enableClick(true);
		mMapView.setBuiltInZoomControls(true);

		mPopView = getLayoutInflater().inflate(R.layout.popview, null);
		mMapView.addView(mPopView, new MapView.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, null,
				MapView.LayoutParams.TOP_LEFT));
		mPopView.setVisibility(View.GONE);

		Intent intent = getIntent();
		partyName = intent.getStringExtra("partyName");
		partyLat = intent.getStringExtra("partyLat");
		partyLon = intent.getStringExtra("partyLon");

		System.out.println(partyName + "-----" + partyLat + "-----" + partyLon);

		int lat = (int) (Integer.parseInt(partyLat));
		int lng = (int) (Integer.parseInt(partyLon));
		System.out.println(lat);
		System.out.println(lng);

		point = new GeoPoint(lat, lng);
		endGeopoint = point;

		mMapListener = new MKMapViewListener() {

			@Override
			public void onMapMoveFinish() {
				// TODO Auto-generated method stub
				Log.d("hjtest", "hjtest" + "onMapMoveFinish");

			}

			@Override
			public void onClickMapPoi(MapPoi mapPoiInfo) {
				// TODO Auto-generated method stub
				String title = "";
				if (mapPoiInfo != null) {
					title = mapPoiInfo.strText;
					System.out.println(title);
					Toast.makeText(PartyLocActivity.this, title,
							Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onGetCurrentMap(Bitmap b) {
				// TODO Auto-generated method stub
				Log.d("hjtest", "hjtest" + "getmap OK");
			}

			@Override
			public void onMapAnimationFinish() {
				// TODO Auto-generated method stub
				Log.d("hjtest", "hjtest" + "onMapAnimationFinish");

			}
		};
		mMapView.regMapViewListener(mapManager, mMapListener);

		/**
		 * ����Ҫ���Overlay�ĵط�ʹ�����´��룬 ����Activity��onCreate()��
		 */
		Drawable mark = getResources().getDrawable(R.drawable.icon_marka);
		mark.setBounds(0, 0, mark.getIntrinsicWidth(),
				mark.getIntrinsicHeight());
		// ��OverlayItem׼��Overlay����
		OverlayItem item1 = new OverlayItem(point, "item1", "item1");
		// ����IteminizedOverlay
		OverlayTest itemOverlay = new OverlayTest(mark, mMapView);
		// ��IteminizedOverlay��ӵ�MapView��
		// ע�⣺ĿǰIteminizedOverlay��֧�ֶ�ʵ����MapView��ֻ����һ��IteminizedOverlayʵ��
		mMapView.getOverlays().clear();
		mMapView.getOverlays().add(itemOverlay);

		// ��������׼��������׼���ã�ʹ�����·�������overlay.
		// ���overlay, ���������Overlayʱʹ��addItem(List<OverlayItem>)Ч�ʸ���
		itemOverlay.addItem(item1);
		mMapView.refresh();
		// ɾ��overlay .
		// itemOverlay.removeItem(itemOverlay.getItem(0));
		mMapView.refresh();
		// ���overlay
		// itemOverlay.removeAll();
		// mMapView.refresh();

		mMapController.setCenter(point);
		mMapController.setZoom(13);
	}

	protected boolean isRouteDisplayed() {
		return false;

	}

	@Override
	protected void onDestroy() {
		if (mapManager != null) {
			mapManager.destroy();
			mapManager = null;
		}
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		if (mapManager != null) {
			mapManager.stop();
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		if (mapManager != null) {
			mapManager.start();
		}
		super.onResume();
	}

	/*
	 * Ҫ����overlay����¼�ʱ��Ҫ�̳�ItemizedOverlay ���������¼�ʱ��ֱ������ItemizedOverlay.
	 */
	class OverlayTest extends ItemizedOverlay<OverlayItem> {

		PopupOverlay pop = null;
		Toast mToast = null;

		// ��MapView����ItemizedOverlay
		public OverlayTest(Drawable marker, MapView mapView) {
			super(marker, mapView);
		}

		protected boolean onTap(int index) {
			// �ڴ˴���item����¼�
			int lat = (int) (Integer.parseInt(partyLat));
			int lng = (int) (Integer.parseInt(partyLon));

			GeoPoint point1 = new GeoPoint(lat, lng);

			mMapView.updateViewLayout(mPopView, new MapView.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
					point1, MapView.LayoutParams.BOTTOM_CENTER));
			mPopView.setVisibility(View.VISIBLE);

			TextView title = (TextView) mPopView
					.findViewById(R.id.txt_shopname);
			title.setText(partyName);

			Button btnGo = (Button) mPopView.findViewById(R.id.btn_go_info);

			btnGo.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					mkSearch = new MKSearch();
					mkSearch.init(mapManager, new SearchListener());

					mkSearch.reverseGeocode(endGeopoint);
					
					
					mLocClient = new LocationClient(PartyLocActivity.this);
					mLocClient.registerLocationListener(myListener);

					LocationClientOption option = new LocationClientOption();
					option.setOpenGps(true);// ��gps
					option.setCoorType("bd09ll"); // ������������
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
					
					MKPlanNode start = new MKPlanNode();
					start.pt = myLocGeopoint;

					MKPlanNode end = new MKPlanNode();
					end.pt = endGeopoint;
					mkSearch.drivingSearch(city, start, city, end);
				}
			});

			return true;
		}

		public boolean onTap(GeoPoint pt, MapView mapView) {
			// �ڴ˴���MapView�ĵ���¼��������� trueʱ
			mPopView.setVisibility(View.GONE);
			return super.onTap(pt, mapView);
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
			// ����ſɲο�MKEvent�еĶ���
			if (error != 0 || res == null) {
				Toast.makeText(PartyLocActivity.this, "��Ǹ��δ�ҵ����", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			RouteOverlay routeOverlay = new RouteOverlay(PartyLocActivity.this,
					mMapView);
			// ��ʾ�ڵ���Ϣ
			MKRoute route = res.getPlan(0).getRoute(0);
			int distanceM = route.getDistance();
			String distanceKm = String.valueOf(distanceM / 1000) + "."
					+ String.valueOf(distanceM % 1000);
			System.out.println("����:" + distanceKm + "����---�ڵ�����:"
					+ route.getNumSteps());
			for (int i = 0; i < route.getNumSteps(); i++) {
				MKStep step = route.getStep(i);
				System.out.println("�ڵ���Ϣ��" + step.getContent());
			}
			// �˴���չʾһ��������Ϊʾ��
			routeOverlay.setData(res.getPlan(0).getRoute(0));
			mMapView.getOverlays().clear();
			mMapView.getOverlays().add(routeOverlay);
			mMapView.refresh();
			// ʹ��zoomToSpan()���ŵ�ͼ��ʹ·������ȫ��ʾ�ڵ�ͼ��
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
				Toast.makeText(PartyLocActivity.this, "��Ǹ��δ�ҵ����", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			TransitOverlay routeOverlay = new TransitOverlay(PartyLocActivity.this,
					mMapView);
			// ��ʾ�ڵ���Ϣ
			// MKRoute route = res.getPlan(0).getRoute(0);
			// int distanceM = route.getDistance();
			// String distanceKm = String.valueOf(distanceM / 1000)
			// +"."+String.valueOf(distanceM % 1000);
			// System.out.println("����:"+distanceKm+"����---�ڵ�����:"+route.getNumSteps());
			// for (int i = 0; i < route.getNumSteps(); i++) {
			// MKStep step = route.getStep(i);
			// System.out.println("�ڵ���Ϣ��"+step.getContent());
			// }

			// �˴���չʾһ��������Ϊʾ��
			routeOverlay.setData(res.getPlan(0));
			mMapView.getOverlays().clear();
			mMapView.getOverlays().add(routeOverlay);
			mMapView.refresh();
			// ʹ��zoomToSpan()���ŵ�ͼ��ʹ·������ȫ��ʾ�ڵ�ͼ��
			mMapView.getController().zoomToSpan(routeOverlay.getLatSpanE6(),
					routeOverlay.getLonSpanE6());
			mMapView.getController().animateTo(res.getStart().pt);
		}

		@Override
		public void onGetWalkingRouteResult(MKWalkingRouteResult arg0, int arg1) {
			// TODO Auto-generated method stub

		}

	}

}
