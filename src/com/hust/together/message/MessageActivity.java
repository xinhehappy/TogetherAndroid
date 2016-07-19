package com.hust.together.message;

import java.util.ArrayList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

import com.hust.together.MyAdapter.ListItemAdapter;
import com.hust.together.service.message.ChangeMessageStatusService;
import com.hust.together.service.message.GetHistoryMessageService;
import com.hust.together.service.message.GetUnreadMessageService;
import com.hust.together.ui.R;
import com.hust.together.util.TogetherApplication;

@SuppressLint("ShowToast")
public class MessageActivity extends Activity {
	private TextView message_title;
	private PopupWindow popupWindow;
	private ImageView img_arrow;
	private LinearLayout layout;
	private ListView listView,listViewMsg;
	private String title[] = { "未读消息", "历史消息","系统通知"};
	String myid,mid;
	private SharedPreferences sp_user_info = null;
	Toast toast;
	private static boolean isExit=false;
    private static boolean hasTask=false;
    Timer tExit=new Timer();
    TimerTask task=new TimerTask() {
        
        @Override
        public void run() {
            // TODO Auto-generated method stub
            isExit=false;
            hasTask=true;
        }
    };
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            //exitDialog();
            if(isExit==false)
            {
                isExit=true;
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show(); 
                if(!hasTask) {
                    tExit.schedule(task, 2000);
                }
            }
            else
            {
            	TogetherApplication.getInstance().exit();
//            	ActivityManager activityMgr = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE ); 
//
//            	activityMgr.restartPackage(getPackageName()); 
            }
            
        }
        //return true;
        return false;
        
    }
    
    

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_message);
		
		toast = Toast.makeText(MessageActivity.this, "null", Toast.LENGTH_SHORT);
		
		TogetherApplication.getInstance().addActivity(this);
		sp_user_info = getSharedPreferences("user_info", MODE_PRIVATE);
		myid = sp_user_info.getString("uid", "");
		
		img_arrow=(ImageView) findViewById(R.id.iv_arrows);
		
		message_title = (TextView) findViewById(R.id.txt_vb_title);
		message_title.setOnClickListener(new OnClickListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				message_title.getTop();
				int y = message_title.getBottom() * 3 / 2;
				int x = getWindowManager().getDefaultDisplay().getWidth() / 4;
				showPopupWindow(x, y);
				if(popupWindow.isShowing()){
					img_arrow.setImageDrawable(getResources().getDrawable(R.drawable.grouplist_title_arrow_up));
				}
			}
		});
	}

	@SuppressWarnings("deprecation")
	public void showPopupWindow(int x, int y) {
		layout = (LinearLayout) LayoutInflater.from(MessageActivity.this).inflate(
				R.layout.dialog, null);
		listView = (ListView) layout.findViewById(R.id.lv_dialog);
		listView.setAdapter(new ArrayAdapter<String>(MessageActivity.this,
				R.layout.text, R.id.tv_text, title));

		popupWindow = new PopupWindow(MessageActivity.this);
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
		popupWindow.showAtLocation(findViewById(R.id.title_bar_message), Gravity.LEFT
				| Gravity.TOP, x, y);//需要指定Gravity，默认情况是center.
		popupWindow.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				img_arrow.setImageDrawable(getResources().getDrawable(R.drawable.grouplist_title_arrow_down));
			}
		});

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				message_title.setText(title[position]);
				popupWindow.dismiss();
				popupWindow = null;
				switch(position){
				//未读消息
				case 0:{
					GetUnreadMessageTask getUnreadMessageTask = new GetUnreadMessageTask();
					getUnreadMessageTask.execute();
					break;
				}
				//历史消息
				case 1:{
					GetHistoryMessageTask getHistoryMessageTask = new GetHistoryMessageTask();
					getHistoryMessageTask.execute();
					break;
				}
				}
			}
		});
	}
	
	//获取未读消息
	class GetUnreadMessageTask extends AsyncTask<String, Integer, ArrayList<Map<String, Object>>> {
		String url = getResources().getString(R.string.GetUnreadMessageServletUrl);

		@Override
		protected void onPostExecute(final ArrayList<Map<String, Object>> listViewRes) {
			listViewMsg = (ListView) findViewById(R.id.lv_messages);
			final ListItemAdapter sSchedule =new ListItemAdapter(MessageActivity.this, listViewRes,
					R.layout.message_item, new String[] { "content" }, 
					new int[] {  R.id.tv_showmessage });

			listViewMsg.setAdapter(sSchedule);

			listViewMsg.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					sSchedule.setIndex(position);
					sSchedule.notifyDataSetChanged();
//					mid = listViewRes.get(position).get("mid").toString();
//					ChangeMessageStatueTask changeMessageStatueTask = new ChangeMessageStatueTask();
//					changeMessageStatueTask.execute();
				}

			});
			listViewMsg.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					mid = listViewRes.get(position).get("mid").toString();
					ChangeMessageStatueTask changeMessageStatueTask = new ChangeMessageStatueTask();
					changeMessageStatueTask.execute();
					return false;
				}
				
			});
		}

		@Override
		protected ArrayList<Map<String, Object>> doInBackground(String... params){
			// TODO Auto-generated method stub
			ArrayList<Map<String, Object>> listViewRes = GetUnreadMessageService.sendDataByHttpClientPost(url, myid);
			return listViewRes;
		}

	}
	
	//获取历史消息
	class GetHistoryMessageTask extends AsyncTask<String, Integer, ArrayList<Map<String, Object>>> {
		String url = getResources().getString(R.string.GetHistoryMessageServletUrl);

		@Override
		protected void onPostExecute(final ArrayList<Map<String, Object>> listViewRes) {
			listViewMsg = (ListView) findViewById(R.id.lv_messages);
			final ListItemAdapter sSchedule1 =new ListItemAdapter(MessageActivity.this, listViewRes,
					R.layout.message_item, new String[] {  "content" }, 
					new int[] { R.id.tv_showmessage });

			listViewMsg.setAdapter(sSchedule1);

			listViewMsg.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					sSchedule1.setIndex(position);
					sSchedule1.notifyDataSetChanged();
				}

			});
		}

		@Override
		protected ArrayList<Map<String, Object>> doInBackground(String... params){
			// TODO Auto-generated method stub
			ArrayList<Map<String, Object>> listViewRes = GetHistoryMessageService.sendDataByHttpClientPost(url, myid);
			return listViewRes;
		}

	}
	
	//更改消息状态，未读消息点击后转为历史消息
	class ChangeMessageStatueTask extends AsyncTask<String, Integer, String> {
		String url = getResources().getString(R.string.ChangeMessageStatusServletUrl);

		@Override
		protected void onPostExecute(String result) {
			if (result.equals("1")) {
				toast.setText("标记为已读！");
				toast.show();
				GetUnreadMessageTask getUnreadMessageTask = new GetUnreadMessageTask();
				getUnreadMessageTask.execute();
				return;

			} else {

			}
		}

		@Override
		protected String doInBackground(String... params){
			// TODO Auto-generated method stub
			
			String result = null;
			try {
				result = ChangeMessageStatusService.sendDataByHttpClientPost(url, mid).trim();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;
		}

	}
	
}
