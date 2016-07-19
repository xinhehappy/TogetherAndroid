package com.hust.together.MyAdapter;

import java.util.ArrayList;
import java.util.Map;

import com.hust.together.ui.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ListItemAdapter extends SimpleAdapter{
	
	private LayoutInflater inflater;
	private ArrayList<Map<String, Object>> items;
    private int index = 0; 

	public ListItemAdapter(Context context,
			ArrayList<Map<String, Object>> items, int resource, String[] from,
			int[] to) {
		super(context, items, resource, from, to);
		this.inflater = LayoutInflater.from(context);
		this.items = items;
		// TODO Auto-generated constructor stub
	}
	
	public void setIndex(int selected){
		index = selected;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;  
        if (convertView == null) {  
            convertView = inflater.inflate(R.layout.message_item, null);  
            holder = new ViewHolder();  
            holder.tv_msg = (TextView) convertView  
                    .findViewById(R.id.tv_showmessage);  
        } else {  
            holder = (ViewHolder) convertView.getTag();  
        }  
        if (index == position) {  
          
//            convertView.setBackgroundColor(Color.GREEN);  
                             //此处就是设置textview为选中状态，方可以实现效果          
             convertView.findViewById(R.id.tv_showmessage)  
             .setSelected(true);  
  
        } else {  
//            convertView.setBackgroundColor(Color.BLUE);  
            //没选中的就不用设置了                              
                        convertView.findViewById(R.id.tv_showmessage)  
             .setSelected(false);  
        }  
        convertView.setTag(holder);  
        holder.tv_msg.setText(items.get(position).get("content").toString());  
        return convertView;  
	}
	
	private class ViewHolder{
		private TextView tv_msg;
	}
	
}