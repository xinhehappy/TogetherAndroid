package com.hust.together.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.hust.together.myview.PullToRefresh;
import com.hust.together.util.TogetherApplication;

@SuppressLint("HandlerLeak")
public class HomeActivity extends Activity implements
		PullToRefresh.UpdateHandle {
	PullToRefresh refresh;
	private String names[] = { "���ӱ�", "Ф��","�����","�ų�","����","����"};
	private String contents[] = { "��������Android����Ŀ��ʽ�����ˣ���ѧҲ����������������Ҷ���һ�вŸոտ�ʼ��", 
			"��������Android����Ŀ�����ˣ���ѧҲ����������",
			"��������Android����Ŀ�����ˣ���ѧҲ����������",
			"��������Android����Ŀ�����ˣ���ѧҲ����������",
			"��������Android����Ŀ�����ˣ���ѧҲ����������",
			"��������Android����Ŀ�����ˣ���ѧҲ����������"};
	private String times[] = {"5����ǰ","10����ǰ","10����ǰ","��Сʱǰ","��Сʱǰ","1Сʱǰ"};
	private String froms[] = {"���ԣ����� I9100�ͻ���","���ԣ�LG Nexus4�ͻ���",
								"���ԣ���������Android�ͻ���","���ԣ�HTC One�ͻ���",
								"���ԣ�IPhone 5�ͻ���","���ԣ�MI 2�ͻ���"};
	private String redirects[] = {"7","3","4","2","1","6"};
	private String comments[] = {"24","11","13","8","12","19"};
	
	
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
                Toast.makeText(this, "�ٰ�һ���˳�����", Toast.LENGTH_SHORT).show(); 
                if(!hasTask) {
                    tExit.schedule(task, 2000);
                }
            }
            else
            {
            	TogetherApplication.getInstance().exit();
            }
            
        }
        //return true;
        return false;
        
    }
    
    

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		refresh = (PullToRefresh) this.findViewById(R.id.pullDownView1);
		refresh.setUpdateHandle(this);
		ListView view = (ListView) this.findViewById(R.id.listview1);
		ArrayList<Map<String, Object>> listViewRes = new ArrayList<Map<String, Object>>();
		
		for (int i = 0; i < 6; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", names[i]);
			map.put("content", contents[i]);
			map.put("time", times[i]);
			map.put("from", froms[i]);
			map.put("redirect", redirects[i]);
			map.put("comment", comments[i]);

			listViewRes.add(map);
		}
		
		SimpleAdapter sSchedule =new SimpleAdapter(HomeActivity.this, listViewRes,
				R.layout.news_item_templater, new String[] { "name", "content","time", "from","redirect" ,"comment"}, 
				new int[] { R.id.txt_wb_item_uname, R.id.txt_wb_item_content,
				R.id.txt_wb_item_time, R.id.txt_wb_item_from,
				R.id.txt_wb_item_redirect, R.id.txt_wb_item_comment });
		view.setAdapter(sSchedule);
		
	}


	@Override
	public void onUpdate() {
		new MyThread(handler).start();
	}

	class MyThread extends Thread {
		private Handler handler;

		public MyThread(Handler handler) {
			this.handler = handler;

		}

		@Override
		public void run() {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
			Message msg = handler.obtainMessage();
			handler.sendMessage(msg);
			super.run();
		}

	}

	private final Handler handler = new Handler(Looper.getMainLooper()) {
		@SuppressLint("SimpleDateFormat")
		public void handleMessage(Message msg) { 
			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy��M��d��  HH:mm:ss");

			Date curDate = new Date(System.currentTimeMillis());
			String str = formatter.format(curDate);
			refresh.endUpdate(str);
		}
	};
}