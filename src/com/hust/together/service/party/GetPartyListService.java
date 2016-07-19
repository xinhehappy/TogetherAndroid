package com.hust.together.service.party;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hust.together.ui.HttpCon;


public class GetPartyListService{

	public static ArrayList<Map<String, Object>> sendDataByHttpClientPost(String url, String myid){
		ArrayList<Map<String, Object>> listViewRes = new ArrayList<Map<String, Object>>();
		String url1 = url + "&myid=" + myid;
		System.out.println(url1);
		try {
			String body = HttpCon.getContent(url1);
			JSONArray array = new JSONArray(body);
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = array.getJSONObject(i);
				Map<String, Object> map = new HashMap<String, Object>();
				System.out.println(object.getString("id") + object.get("name"));
				map.put("partyid", object.getString("id"));
				map.put("partyname", object.get("name"));

				listViewRes.add(map);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			e.getMessage();
		}
		return listViewRes;
	}
		
}
