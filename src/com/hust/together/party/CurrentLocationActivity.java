//package com.hust.together.party;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.graphics.Bitmap;
//import android.os.Bundle;
//import android.os.Handler;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.FrameLayout;
//import android.widget.Toast;
//
//import com.baidu.location.BDLocation;
//import com.baidu.location.BDLocationListener;
//import com.baidu.location.BDNotifyListener;
//import com.baidu.location.LocationClient;
//import com.baidu.location.LocationClientOption;
//import com.baidu.mapapi.BMapManager;
//import com.baidu.mapapi.map.LocationData;
//import com.baidu.mapapi.map.MKMapViewListener;
//import com.baidu.mapapi.map.MapController;
//import com.baidu.mapapi.map.MapPoi;
//import com.baidu.mapapi.map.MapView;
//import com.baidu.mapapi.map.MyLocationOverlay;
//import com.baidu.platform.comapi.basestruct.GeoPoint;
//import com.hust.together.ui.R;
//@SuppressLint("HandlerLeak")
//public class CurrentLocationActivity extends Activity {
//	
//	static MapView mMapView = null;
//	
//	private MapController mMapController = null;
//
//	public MKMapViewListener mMapListener = null;
//	FrameLayout mMapViewContainer = null;
//	
//	LocationClient mLocClient;
//	public MyLocationListenner myListener = new MyLocationListenner();
//    public NotifyLister mNotifyer=null;
//	
//    private Button btn_back;
//	
//	EditText indexText = null;
//	MyLocationOverlay myLocationOverlay = null;
//	int index =0;
//	LocationData locData = null;
//	
//	Handler mHandler = new Handler() {
//        public void handleMessage(android.os.Message msg) {
//            Toast.makeText(CurrentLocationActivity.this, "msg:" +msg.what, Toast.LENGTH_SHORT).show();
//        };
//    };
//    
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        this.getApplication();
//        if (TogetherApplication.mBMapManager == null) {
//            TogetherApplication.mBMapManager = new BMapManager(this);
//            TogetherApplication.mBMapManager.init(TogetherApplication.strKey,new TogetherApplication.MyGeneralListener());
//        }
//        setContentView(R.layout.currentlocation);
//        mMapView = (MapView)findViewById(R.id.bmapsView);
//        mMapController = mMapView.getController();
//        
//        initMapView();
//        
//        mLocClient = new LocationClient( this );
//        mLocClient.registerLocationListener( myListener );
//        
//        LocationClientOption option = new LocationClientOption();
//        option.setOpenGps(true);
//        option.setCoorType("bd09ll");     
//        option.setScanSpan(5000);
//        mLocClient.setLocOption(option);
//        mLocClient.start();
//        mMapView.getController().setZoom(14);
//        mMapView.getController().enableClick(true);
//        
//        mMapView.setBuiltInZoomControls(true);
//        mMapListener = new MKMapViewListener() {
//			
//			@Override
//			public void onMapMoveFinish() {
//				// TODO Auto-generated method stub
//			}
//			
//			@Override
//			public void onClickMapPoi(MapPoi mapPoiInfo) {
//				// TODO Auto-generated method stub
//				String title = "";
//				if (mapPoiInfo != null){
//					title = mapPoiInfo.strText;
//					Toast.makeText(CurrentLocationActivity.this,title,Toast.LENGTH_SHORT).show();
//				}
//			}
//
//			@Override
//			public void onGetCurrentMap(Bitmap b) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void onMapAnimationFinish() {
//				// TODO Auto-generated method stub
//				
//			}
//		};
//		TogetherApplication.getInstance();
//		mMapView.regMapViewListener(TogetherApplication.mBMapManager, mMapListener);
//		myLocationOverlay = new MyLocationOverlay(mMapView);
//		locData = new LocationData();
//	    myLocationOverlay.setData(locData);
//		mMapView.getOverlays().add(myLocationOverlay);
//		myLocationOverlay.enableCompass();
//		mMapView.refresh();
//		
//	    btn_back = (Button) findViewById(R.id.aboutus_btn_back);
//	    btn_back.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				finish();
//			}
//		});
//	   
//    }
//    
//    @Override
//    protected void onPause() {
//        mMapView.onPause();
//        super.onPause();
//    }
//    
//    @Override
//    protected void onResume() {
//        mMapView.onResume();
//        super.onResume();
//    }
//    
//    
//    @Override
//    protected void onDestroy() {
//        if (mLocClient != null)
//            mLocClient.stop();
//        mMapView.destroy();
//        this.getApplication();
//        if (TogetherApplication.mBMapManager != null) {
//            TogetherApplication.mBMapManager.destroy();
//            TogetherApplication.mBMapManager = null;
//        }
//        super.onDestroy();
//    }
//    
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//    	super.onSaveInstanceState(outState);
//    	mMapView.onSaveInstanceState(outState);
//    	
//    }
//    
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//    	super.onRestoreInstanceState(savedInstanceState);
//    	mMapView.onRestoreInstanceState(savedInstanceState);
//    }
//    
//    public void testUpdateClick(){
//        mLocClient.requestLocation();
//    }
//    private void initMapView() {
//        mMapView.setLongClickable(true);
//        //mMapController.setMapClickEnable(true);
//        //mMapView.setSatellite(false);
//    }
//   
//
//
//	
//    public class MyLocationListenner implements BDLocationListener {
//        @Override
//        public void onReceiveLocation(BDLocation location) {
//            if (location == null)
//                return ;
//            
//            locData.latitude = location.getLatitude();
//            locData.longitude = location.getLongitude();
//            locData.accuracy = location.getRadius();
//            locData.direction = location.getDerect();
//            myLocationOverlay.setData(locData);
//            mMapView.refresh();
//            mMapController.animateTo(new GeoPoint((int)(locData.latitude* 1e6), (int)(locData.longitude *  1e6)), mHandler.obtainMessage(1));
//        }
//        
//        public void onReceivePoi(BDLocation poiLocation) {
//            if (poiLocation == null){
//                return ;
//            }
//        }
//    }
//    
//    public class NotifyLister extends BDNotifyListener{
//        public void onNotify(BDLocation mlocation, float distance) {
//        }
//    }
//
//}
//
//

package com.hust.together.party;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.PoiOverlay;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPoiInfo;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.hust.together.MyAdapter.MyAdapter;
import com.hust.together.ui.MainActivity;
import com.hust.together.ui.R;

@SuppressLint("ShowToast")
public class CurrentLocationActivity extends Activity {
	private GridView gv;
	private PopupWindow mPop1;

	private View layout1;
	private static boolean isWeiXing = false;
	private static boolean isSimple = true;
	private boolean flag_new = true;
	private static LocationData locationData = new LocationData();

	private TextView more;
	private PopupWindow popupWindow;
	private ImageView img_arrow;
	private LinearLayout layout;
	private ListView listView;
	private String title[] = { "附近热点", "切换视图", "刷新位置" };
	Toast toast;

	private MKSearch mkSearch;

	private int[] icons = { R.drawable.icon_class_ktv,
			R.drawable.icon_class_hotel, R.drawable.icon_class_meishi,
			R.drawable.icon_class_petrolstation,
			R.drawable.icon_class_supermarket, R.drawable.icon_company,
			R.drawable.icon_class_viewspot,
			R.drawable.icon_class_xiaochikuaican,
			R.drawable.icon_class_xingjijiudian, R.drawable.icon_class_bath,
			R.drawable.icon_class_atm, R.drawable.icon_class_bank };
	private String[] items = { "KTV", "宾馆", "美食", "加油站", "超市", "公司", "景点",
			"快餐", "酒店", "洗浴", "取款机", "银行" };

	private Button btn_back;

	// 定位相关
	LocationClient mLocClient;
	public MyLocationListener myListener = new MyLocationListener();
	private BMapManager mapManager;
	private MapView mMapView;
	private MapController mMapController;
	private GeoPoint pt1;

	private LocationData locData;
	private MyLocationOverlay mLocationOverlay;
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(CurrentLocationActivity.this,
				MainActivity.class);
		intent.putExtra("tabNum", "2");
		startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.currentlocation);

		toast = Toast.makeText(CurrentLocationActivity.this, "null",
				Toast.LENGTH_SHORT);

		mapManager = new BMapManager(getApplication());
		mapManager.init("6A2FDB2DDA25BD9EF8031BEA5E59A47BE83A8053", null);

		mMapView = (MapView) this.findViewById(R.id.bmapsView);
		mMapController = mMapView.getController();

		mMapView.setBuiltInZoomControls(true);
		mMapController.enableClick(true);
		mMapController.setZoom(12);
		mMapView.setBuiltInZoomControls(true);
		mMapView.setDoubleClickZooming(true);

		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);

		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(100);
		option.setAddrType("all");

		mLocClient.setLocOption(option);
		mLocClient.start();
		
		mkSearch = new MKSearch();
		mkSearch.init(mapManager, new MKSearchListener() {

			@Override
			public void onGetWalkingRouteResult(MKWalkingRouteResult res,
					int error) {
			}

			@Override
			public void onGetTransitRouteResult(MKTransitRouteResult res,
					int error) {
			}

			@Override
			public void onGetSuggestionResult(MKSuggestionResult arg0, int arg1) {
			}

			@Override
			public void onGetPoiResult(MKPoiResult res, int type, int error) {

				if (error == MKEvent.ERROR_RESULT_NOT_FOUND) {
					return;
				} else if (error != 0 || res == null) {
					return;
				}

				// 将poi结果显示到地图上
				PoiOverlay poiOverlay = new PoiOverlay(
						CurrentLocationActivity.this, mMapView);
				poiOverlay.setData(res.getAllPoi());
				mMapView.getOverlays().clear();
				mMapView.getOverlays().add(poiOverlay);
				mMapView.refresh();
				mPop1.dismiss();
				// 当ePoiType为2（公交线路）或4（地铁线路）时， poi坐标为空
				for (MKPoiInfo info : res.getAllPoi()) {
					if (info.pt != null) {
						mMapView.getController().animateTo(info.pt);
						break;
					}
				}
			}

			@Override
			public void onGetPoiDetailSearchResult(int arg0, int arg1) {
			}

			@Override
			public void onGetDrivingRouteResult(MKDrivingRouteResult arg0,
					int arg1) {
			}

			@Override
			public void onGetBusDetailResult(MKBusLineResult arg0, int arg1) {
			}

			@Override
			public void onGetAddrResult(MKAddrInfo arg0, int arg1) {
			}
		});

		locData = new LocationData();
		mLocationOverlay = new MyLocationOverlay(mMapView);
		mLocationOverlay.setData(locData);
		mLocationOverlay.enableCompass();

		mMapView.getOverlays().add(mLocationOverlay);

		img_arrow = (ImageView) findViewById(R.id.iv_arrows);
		layout1 = View.inflate(this, R.layout.window, null);

		gv = (GridView) layout1.findViewById(R.id.gridview);
		MyAdapter adapter = new MyAdapter(this, items, icons);
		gv.setAdapter(adapter);
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long id) {
				// 每个item点击进行搜索
				mkSearch.poiSearchNearBy(items[position], pt1, 5000);
			}
		});

		more = (TextView) findViewById(R.id.txt_vb_title);
		more.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				more.getTop();
				int y = more.getBottom() * 3 / 2;
				int x = getWindowManager().getDefaultDisplay().getWidth() / 4;
				showPopupWindow(x, y);
				if (popupWindow.isShowing()) {
					img_arrow.setImageDrawable(getResources().getDrawable(
							R.drawable.grouplist_title_arrow_up));
				}
			}
		});

		btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(CurrentLocationActivity.this,
						MainActivity.class);
				intent.putExtra("tabNum", "2");
				startActivity(intent);
			}
		});

	}

	@SuppressWarnings("deprecation")
	protected void showPopupWindow(int x, int y) {
		// TODO Auto-generated method stub
		layout = (LinearLayout) LayoutInflater.from(
				CurrentLocationActivity.this).inflate(R.layout.dialog, null);
		listView = (ListView) layout.findViewById(R.id.lv_dialog);
		listView.setAdapter(new ArrayAdapter<String>(
				CurrentLocationActivity.this, R.layout.text, R.id.tv_text,
				title));

		popupWindow = new PopupWindow(CurrentLocationActivity.this);
		popupWindow.setBackgroundDrawable(new ColorDrawable(0));
		popupWindow
				.setWidth(getWindowManager().getDefaultDisplay().getWidth() / 2);
		popupWindow.setHeight(200);
		popupWindow.setTouchable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.setContentView(layout);
		// showAsDropDown会把里面的view作为参照物，所以要那满屏幕parent
		// popupWindow.showAsDropDown(findViewById(R.id.tv_title), x, 10);
		popupWindow.showAtLocation(
				findViewById(R.id.title_bar_currentlocation), Gravity.LEFT
						| Gravity.TOP, x, y);// 需要指定Gravity，默认情况是center.
		popupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				img_arrow.setImageDrawable(getResources().getDrawable(
						R.drawable.grouplist_title_arrow_down));
			}
		});

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				more.setText(title[position]);
				popupWindow.dismiss();
				popupWindow = null;
				switch (position) {
				// 附近热点
				case 0: {
					initPopWindow();
					mPop1.showAtLocation(
							CurrentLocationActivity.this.findViewById(R.id.rl),
							Gravity.CENTER, 0, 0);// 在屏幕居中，无偏移
					break;
				}
				// 切换视图
				case 1: {
					if (isWeiXing) {
						isWeiXing = false;
						isSimple = true;
						mMapView.setSatellite(isWeiXing);
						mMapView.setTraffic(isSimple);
					} else if (isSimple) {
						isSimple = false;
						isWeiXing = true;
						mMapView.setSatellite(isWeiXing);
						mMapView.setTraffic(isSimple);
					}
					break;
				}
				// 刷新位置
				case 2: {
					flag_new = true;
					break;
				}
				}
			}
		});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		mMapView.destroy();
		if (mapManager != null) {
			mapManager.destroy();
			mapManager = null;
		}
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		if (mapManager != null) {
			mapManager.stop();
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		if (mapManager != null) {
			mapManager.start();
		}
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

	private class MyLocationListener implements BDLocationListener {

		// 大概一秒钟定位一次
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;
			if (flag_new == true) {
				pt1 = new GeoPoint((int) (location.getLatitude() * 1E6),
						(int) (location.getLongitude() * 1E6));
				locationData.latitude = location.getLatitude();
				locationData.longitude = location.getLongitude();
				locationData.direction = 2.0f;
				locationData.accuracy = location.getRadius();// 获取服务
				locationData.direction = location.getDerect();
				// myLocationOverlay.setData(locationData);//异步加载locationData,必须异步加载，否则myLocationOverlay不会显示
				mMapView.refresh();// 此处刷新必须有
				// 定位本地位置，此句没有，则无法定位
				mMapController.animateTo(new GeoPoint(
						(int) (locationData.latitude * 1e6),
						(int) (locationData.longitude * 1e6)));
				MyLocationOverlay myLocationOverlay = new MyLocationOverlay(
						mMapView);
				// LocationData locData = new LocationData();
				// 手动将位置源置为天安门，在实际应用中，请使用百度定位SDK获取位置信息，要在SDK中显示一个位置，需要
				// 使用百度经纬度坐标（bd09ll）
				// locData.latitude = location.getLatitude();
				// locData.longitude = location.getLongitude();
				locationData.direction = 2.0f;
				myLocationOverlay.setData(locationData);
				mMapView.getOverlays().add(myLocationOverlay);
				mMapView.refresh();
				mMapView.getController().animateTo(
						new GeoPoint((int) (locationData.latitude * 1e6),
								(int) (locationData.longitude * 1e6)));
				flag_new = false;
			}

		}

		@Override
		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null) {
				Toast.makeText(getApplicationContext(), "null", 1).show();
				return;
			}
			Toast.makeText(getApplicationContext(), "111111", 1).show();
			Log.i("onReceivePoi", "______________________");
			// 初始化mkSearch
		}
	}

	/* 初始化一个弹窗 */
	@SuppressWarnings("deprecation")
	private void initPopWindow() {
		if (mPop1 == null) {
			mPop1 = new PopupWindow(layout1, LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			mPop1.setBackgroundDrawable(new BitmapDrawable());
			mPop1.setOutsideTouchable(true);
			mPop1.setFocusable(true);
		}
		if (mPop1.isShowing()) {
			mPop1.dismiss();
		}
	}

}
