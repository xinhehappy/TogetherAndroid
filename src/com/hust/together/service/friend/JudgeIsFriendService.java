package com.hust.together.service.friend;


import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;


public class JudgeIsFriendService{

	public static String sendDataByHttpClientPost(String url, String myid, String fid)throws Exception{
		String url1 = url + "&myid=" + myid +"&fid=" +fid;
		System.out.println(url1);
		//׼��������������
		HttpPost request = new HttpPost(url1);
		HttpResponse response;
		//��ȡ�����ʵ��
		response =new DefaultHttpClient().execute(request);
		String result = EntityUtils.toString(response
				.getEntity());
	    return result;
	}
		
}