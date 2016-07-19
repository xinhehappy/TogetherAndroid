package com.hust.together.service.friend;


import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;


public class ModifyUserInfoService{

	public static String sendDataByHttpClientPost(String url, String myid, String gender, String home, String birthday, String email, String sign)throws Exception{
		String url1 = url + "&myid=" + myid +"&gender=" +gender+"&home=" +home+"&birthday=" +birthday+"&email=" +email+"&sign=" +sign;
		System.out.println(url1);
		//准备请求数据类型
		HttpPost request = new HttpPost(url1);
		HttpResponse response;
		//获取浏览器实例
		response =new DefaultHttpClient().execute(request);
		String result = EntityUtils.toString(response
				.getEntity());
	    return result;
	}
		
}
