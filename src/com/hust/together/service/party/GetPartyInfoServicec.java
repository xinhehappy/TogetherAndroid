package com.hust.together.service.party;

import org.json.JSONObject;

import com.hust.together.ui.HttpCon;

public class GetPartyInfoServicec{

	public static JSONObject sendDataByHttpClientPost(String url, String partyid)throws Exception{
		String url1 = url + "&partyid=" + partyid;
		System.out.println(url1);
		String body = HttpCon.getContent(url1);
		JSONObject object = new JSONObject(body);
		
		return object;
	}
		
}
