package com.hust.together.service.party;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hust.together.ui.HttpCon;

public class GetPartyMemberServicec{

	public static ArrayList<Map<String, Object>> sendDataByHttpClientPost(String url, String partyid)throws Exception{
		ArrayList<Map<String, Object>> listViewRes = new ArrayList<Map<String, Object>>();
		String url1 = url + "&partyid=" + partyid;
		System.out.println(url1);
		String body = HttpCon.getContent(url1);
		JSONObject object = new JSONObject(body);
		String member = object.getString("member");
		
		JSONArray memberArray = new JSONArray(member); 
		
		for (int i = 0; i < memberArray.length(); i++) {
			JSONObject object1 = memberArray.getJSONObject(i);
			
			Map<String, Object> map = new HashMap<String, Object>();

			map.put("userName", object1.getString("userName"));
			map.put("userId", object1.getString("userId"));
			listViewRes.add(map);
		}
		
		return listViewRes;
	}
		
}
