package com.hust.together.tool;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;

public class MyDateTime {
	@SuppressLint("SimpleDateFormat")
	public String getDateTimeByMillisecond(String str) {  
	    Date date = new Date(Long.valueOf(str));  
	    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	    String time = format.format(date);  
	    return time;  
	}  
	
}

