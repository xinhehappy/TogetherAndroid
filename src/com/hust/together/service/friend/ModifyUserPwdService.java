package com.hust.together.service.friend;


import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;


public class ModifyUserPwdService{

	public static String sendDataByHttpClientPost(String url, String myid, String pwd)throws Exception{
		String url1 = url + "&myid=" + myid +"&pwd=" +pwd;
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