/**
 * 
 */
package com.hust.together.util;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;

/**
 * @author tang
 *
 */
public class TogetherApplication extends Application {
	public List<Activity> myList = new LinkedList<Activity>();
	static TogetherApplication instance;
	
	public boolean m_bKeyRight = true;
	public BMapManager mBMapManager = null;

	public static final String strKey = "6A2FDB2DDA25BD9EF8031BEA5E59A47BE83A8053";
	
	public TogetherApplication(){
		
	}
	
	public synchronized static TogetherApplication getInstance() {
		return instance;
	}
	// 添加activity到列表中
	public void addActivity(Activity activity) {
		myList.add(activity);
	}
	// 依次结束activity
    public void exit() { 
        try { 
            for (Activity activity : myList) { 
                if (activity != null) 
                    activity.finish(); 
            } 
        } catch (Exception e) { 
            e.printStackTrace(); 
        } finally { 
            System.exit(0); 
        } 
    } 

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		instance =this;
		initEngineManager(this);
		super.onCreate();
	}

	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		if (mBMapManager != null) {
			mBMapManager.destroy();
			mBMapManager = null;
		}
		super.onTerminate();
	}
	public void initEngineManager(Context context) {
		if (mBMapManager == null) {
			mBMapManager = new BMapManager(context);
		}

		if (!mBMapManager.init(strKey, new MyGeneralListener())) {
			Toast.makeText(
					TogetherApplication.getInstance().getApplicationContext(),
					"BMapManager  初始化错误!", Toast.LENGTH_LONG).show();
		}
	}
	
	// 常用事件监听，用来处理通常的网络错误，授权验证错误等
		public static class MyGeneralListener implements MKGeneralListener {

			@Override
			public void onGetNetworkState(int iError) {
				if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
					Toast.makeText(
							TogetherApplication.getInstance().getApplicationContext(),
							"您的网络出错啦！", Toast.LENGTH_LONG).show();
				} else if (iError == MKEvent.ERROR_NETWORK_DATA) {
					Toast.makeText(
							TogetherApplication.getInstance().getApplicationContext(),
							"输入正确的检索条件！", Toast.LENGTH_LONG).show();
				}
				// ...
			}

			@Override
			public void onGetPermissionState(int iError) {
				if (iError == MKEvent.ERROR_PERMISSION_DENIED) {
					// 授权Key错误：
					Toast.makeText(
							TogetherApplication.getInstance().getApplicationContext(),
							"请在 TogetherApplication.java文件输入正确的授权Key！",
							Toast.LENGTH_LONG).show();
					TogetherApplication.getInstance().m_bKeyRight = false;
				}
			}
		}
    
}
