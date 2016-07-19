package com.hust.together.friend;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MapView.LayoutParams;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.hust.together.ui.R;

public class FriendLocActivity extends Activity {

	private Button btn_back;
	private String locTime,fName,poslat,poslon;
	private BMapManager mapManager;
	private static MapView mMapView = null;
	private MapController mMapController = null;
	
	private View mPopView;
	private static GeoPoint point;

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mapManager = new BMapManager(getApplication());
		mapManager.init("6A2FDB2DDA25BD9EF8031BEA5E59A47BE83A8053", null);

		setContentView(R.layout.activity_friend_loc);
		
		btn_back=(Button) findViewById(R.id.aboutus_btn_back);
		btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				finish();
			}
		});

		Intent intent = getIntent();
		locTime = intent.getStringExtra("locTime");
		fName = intent.getStringExtra("fName");
		poslat = intent.getStringExtra("poslat");
		poslon = intent.getStringExtra("poslon");
		
		int lat = (int) (Integer.parseInt(poslat));
		int lon = (int) (Integer.parseInt(poslon));

		point = new GeoPoint(lat, lon);

		mMapView = (MapView) findViewById(R.id.bmapView);
		mMapController = mMapView.getController();


		mMapView.setBuiltInZoomControls(true);
		mMapController.setCenter(point);
		mMapController.setZoom(17);

		mPopView = getLayoutInflater().inflate(R.layout.popviewtextonly, null);
		mMapView.addView(mPopView, new MapView.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, null,
				MapView.LayoutParams.TOP_LEFT));
		mPopView.setVisibility(View.GONE);

		Drawable marker = getResources().getDrawable(R.drawable.icon_marka);
		OverlayItem item = new OverlayItem(point, "好友在这里", "这是好友位置！");

		FriendLocOverlay overlay = new FriendLocOverlay(marker, mMapView);
		mMapView.getOverlays().clear();
		mMapView.getOverlays().add(overlay);

		overlay.addItem(item);
		mMapView.refresh();
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

	class FriendLocOverlay extends ItemizedOverlay<OverlayItem> {

		PopupOverlay pop = null;
		Toast mToast = null;

		public FriendLocOverlay(Drawable drawable, MapView mapView) {
			super(drawable, mapView);
			pop = new PopupOverlay(mapView, new PopupClickListener() {

				@Override
				public void onClickedPopup(int index) {
					if (null == mToast)
						mToast = Toast.makeText(getBaseContext(),
								"popup item :" + index + " is clicked.",
								Toast.LENGTH_SHORT);
					else
						mToast.setText("popup item :" + index + " is clicked.");
					mToast.show();
				}
			});
			// TODO Auto-generated constructor stub
		}

		public boolean onTap(GeoPoint pt, MapView mapView) {
			mPopView.setVisibility(View.GONE);
			return super.onTap(pt, mapView);
		}

		@Override
		protected boolean onTap(int index) {
			int lat = (int) (Integer.parseInt(poslat));
			int lon = (int) (Integer.parseInt(poslon));
			GeoPoint point1 = new GeoPoint(lat, lon);

			mMapView.updateViewLayout(mPopView, new MapView.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
					point1, MapView.LayoutParams.BOTTOM_CENTER));
			mPopView.setVisibility(View.VISIBLE);

			TextView title = (TextView) mPopView
					.findViewById(R.id.txt_info);
			title.setText(fName+" "+locTime+" 在这里");

			return true;

		}

	}
}