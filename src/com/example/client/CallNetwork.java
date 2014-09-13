package com.example.client;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class CallNetwork {

	private static final String BASE_URL = "http://49.50.76.122/Retails/Android/login.php";

	  private static AsyncHttpClient client = new AsyncHttpClient();

	  public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
	      client.get(getAbsoluteUrl(url), params, responseHandler);
	      
	  }

	  public static void post(Context context,String url, JSONObject params, AsyncHttpResponseHandler responseHandler) {
	     // client.post(getAbsoluteUrl(url), params, responseHandler);
	      
		try {
			StringEntity entity = new StringEntity(params.toString());
			client.post(context, getAbsoluteUrl(url), entity, "application/json", responseHandler);
		      Log.e("URL", client+"");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      
	  }

	  private static String getAbsoluteUrl(String relativeUrl) {
	      return BASE_URL + relativeUrl;
	  }

	  // 
}
