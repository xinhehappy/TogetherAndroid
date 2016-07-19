package com.hust.together.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
/**
 * 启动引导界面，当用户初次安装此应用时进行启动引导。
 * @author adam
 *
 */
public class GuideActivity extends Activity {
	ViewPager viewPager;
	ArrayList<View> list;
	ViewGroup main, group;
	ImageView imageView;
	ImageView[] imageViews;
	SharedPreferences preferences;
	int count;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = getLayoutInflater();
		
		preferences = getSharedPreferences("count", MODE_PRIVATE);
		count = preferences.getInt("count", 0);
		
		View v = inflater.inflate(R.layout.item5, null);
		Button btn_startapp = (Button) v.findViewById(R.id.btn_startapp);
		btn_startapp.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(GuideActivity.this,TogetherActivity.class);
				startActivity(intent);
				finish();
				
				Editor editor = preferences.edit();
				// 存入数据
				editor.putInt("count", ++count);
				// 提交修改
				editor.commit();
			}
		});
		
		list = new ArrayList<View>();
		list.add(inflater.inflate(R.layout.item1, null));
		list.add(inflater.inflate(R.layout.item2, null));
		list.add(inflater.inflate(R.layout.item3, null));
		list.add(inflater.inflate(R.layout.item4, null));
		list.add(v);
		
		imageViews = new ImageView[list.size()];
		ViewGroup main = (ViewGroup) inflater.inflate(R.layout.guide_main, null);
		// group是R.layou.main中的负责包裹小圆点的LinearLayout.
		ViewGroup group = (ViewGroup) main.findViewById(R.id.viewGroup);
		

		viewPager = (ViewPager) main.findViewById(R.id.viewPager);

		for (int i = 0; i < list.size(); i++) {
			imageView = new ImageView(GuideActivity.this);
			imageView.setLayoutParams(new LayoutParams(10,10));
			imageView.setPadding(10, 0, 10, 0);
			imageViews[i] = imageView;
			if (i == 0) {
				// 默认进入程序后第一张图片被选中;
				imageViews[i].setBackgroundResource(R.drawable.guide_dot_white);
			} else {
				imageViews[i].setBackgroundResource(R.drawable.guide_dot_black);
			}
			group.addView(imageView);
		}

		setContentView(main);

		viewPager.setAdapter(new MyAdapter());
		viewPager.setOnPageChangeListener(new MyListener());
	}

	class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getItemPosition(Object object) {
			// TODO Auto-generated method stub
			return super.getItemPosition(object);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			// TODO Auto-generated method stub
			((ViewPager) arg0).removeView(list.get(arg1));
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			// TODO Auto-generated method stub
			((ViewPager) arg0).addView(list.get(arg1));
			return list.get(arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public Parcelable saveState() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void finishUpdate(View arg0) {
			// TODO Auto-generated method stub

		}
	}

	class MyListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageSelected(int arg0) {
			for (int i = 0; i < imageViews.length; i++) {
				imageViews[arg0]
						.setBackgroundResource(R.drawable.guide_dot_white);
				if (arg0 != i) {
					imageViews[i]
							.setBackgroundResource(R.drawable.guide_dot_black);
				}
			}

		}

	}
}