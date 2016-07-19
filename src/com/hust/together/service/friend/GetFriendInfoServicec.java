package com.hust.together.service.friend;

import org.json.JSONObject;

import com.hust.together.ui.HttpCon;

public class GetFriendInfoServicec{

	public static JSONObject sendDataByHttpClientPost(String url, String uid)throws Exception{
		String url1 = url + "&fid=" + uid;
		System.out.println(url1);
		String body = HttpCon.getContent(url1);
		JSONObject object = new JSONObject(body);
		
		return object;
	}
		
}
