package com.hust.together.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class HttpCon {
	public static String getContent(String url) throws Exception {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();

		HttpClient client = new DefaultHttpClient();
//		HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
//		HttpConnectionParams.setSoTimeout(httpParams, 5000);
		HttpResponse response = client.execute(new HttpPost(url));
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					entity.getContent()));
			String lineString = null;
			while ((lineString = reader.readLine()) != null) {
				sb.append(lineString);
			}
			reader.close();
		}
		return sb.toString();
	}

	public static Bitmap returnBitMap(String url) {
		URL myFileUrl = null;
		Bitmap bitmap = null;
		try {
			myFileUrl = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		try {
			HttpURLConnection conn = (HttpURLConnection) myFileUrl
					.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

}
