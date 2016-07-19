package com.hust.together.service.friend;

import org.json.JSONObject;

import com.hust.together.ui.HttpCon;

public class GetUserLocService{

	public static JSONObject sendDataByHttpClientPost(String url, String uid){
		JSONObject object = null;
		String url1 = url + "&myid=" + uid;
		System.out.println(url1);
		try {
			String body = HttpCon.getContent(url1);
			object = new JSONObject(body);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return object;
	}
		
}
