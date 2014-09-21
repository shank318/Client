package com.retails.customer;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class CallNetwork {

	public static final String LOGIN_URL = "http://49.50.76.122/Retails/Android/login.php";


	public static final String SYNC_URL = "http://49.50.76.122/Retails/Android/appSync.php";
	
	private static AsyncHttpClient client = new AsyncHttpClient();



	public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
	
		client.post(url, params, responseHandler);
		Log.e("URL", client+"");

	}

	

	// 
}
