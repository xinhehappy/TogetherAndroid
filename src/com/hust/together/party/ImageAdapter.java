package com.hust.together.party;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hust.together.ui.R;

public class ImageAdapter extends BaseAdapter {

	private Context context;

	ImageAdapter(Context context) {
		this.context = context;
	}

	private Integer[] images = { R.drawable.my_party,
			R.drawable.my_frient, R.drawable.my_city,
			R.drawable.my_focus, R.drawable.my_history,
			R.drawable.my_location };

	private String[] texts = { "�ҵľۻ�", "���Ѿۻ�", "ͬ�Ǿۻ�", "��ע�ۻ�", "��ʷ�ۻ�", "Ŀǰλ��" };

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return images.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup viewgroup) {
		ImgTextWrapper wrapper;
		if (view == null) {
			wrapper = new ImgTextWrapper();
			LayoutInflater inflater = LayoutInflater.from(context);
			view = inflater.inflate(R.layout.party_gv, null);
			view.setTag(wrapper);
			view.setPadding(15, 15, 15, 15); // ÿ��ļ��
		} else {
			wrapper = (ImgTextWrapper) view.getTag();
		}
		wrapper.imageView = (ImageView) view.findViewById(R.id.party_gv_icon);
		wrapper.imageView.setBackgroundResource(images[position]);
		wrapper.textView = (TextView) view.findViewById(R.id.party_gv_txt);
		wrapper.textView.setText(texts[position]);
		return view;
	}

}

class ImgTextWrapper {
	ImageView imageView;
	TextView textView;
}
