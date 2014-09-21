package com.retails.customer;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.retails.customer.R;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;


public class NetworkReceiver extends BroadcastReceiver {   

	public Builder mBuilder;
	public NotificationManager mNotifyManager ;

	public  SharedPreferences mSharedPreferences;
	SharedPreferences.Editor e;
	ArrayList<HashMap<String, String>> temp = new ArrayList<HashMap<String,String>>();


	@Override
	public void onReceive(Context context, Intent intent) {


		// Shared Preferences
		mSharedPreferences= context.getSharedPreferences(AppConstants.KEY, 0);
		e = mSharedPreferences.edit();
	

		mNotifyManager =
				(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mBuilder = new NotificationCompat.Builder(context);

		mBuilder
		.setContentTitle("Uploading failed transactions....")                  
		.setSmallIcon(R.drawable.ic_launcher)
		.setAutoCancel(true);

		if(mSharedPreferences.getString("USERID", "").equals("")){
			return ;
		}
		
		
		


		ConnectionDetector cd = new ConnectionDetector(context);

		Log.e("DEBUG", "Onreceive..");

		if(cd.isConnectingToInternet() && AppConstants.flag==0){
			
			temp = getFailedTransactions();

			if(temp.size()>0){
				try {
					uplaodTransaction(context);
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
			
		}else{
			
		}



	}




	void uplaodTransaction(final Context context) throws JSONException{

		RequestParams params = new RequestParams();

		Log.e("DETAILS", temp.get(0).get("master")+"   "+temp.get(0).get("photod")+"  "+temp.get(0).get("checklist"));
		params.put("sync_master",temp.get(0).get("master"));
		params.put("sync_photo",temp.get(0).get("photod"));
		params.put("sync_checklist",temp.get(0).get("checklist"));


		String[] img = temp.get(0).get("images").split(",");


		for(int i =0;i<img.length;i++){

			Log.e("DETAILS", img[i]);
			File file = new File(img[i]);

			try {
				params.put("img_"+i,file );
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		CallNetwork.post(CallNetwork.SYNC_URL, params, new JsonHttpResponseHandler(){

			@Override
			public void onProgress(int bytesWritten, int totalSize) {
				// TODO Auto-generated method stub
				super.onProgress(bytesWritten, totalSize);
				Log.e("ONPROGRESS", bytesWritten+"");
				mBuilder.setProgress(100, (int) ((bytesWritten / (float) totalSize) * 100), false);
				// Displays the progress bar for the first time.
				mNotifyManager.notify(0, mBuilder.build());
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				Log.e("ONFALIURE", responseString+"aaaaaa");


				mBuilder.setAutoCancel(true);
				mBuilder.setContentTitle("Failed to uplaod");
				mBuilder.setContentText("Please check the internet connection")
				// Removes the progress bar
				.setProgress(0,0,false);
				mNotifyManager.notify(0, mBuilder.build());

				AppConstants.flag=0;

			}

			
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				// If the response is JSONObject instead of expected JSONArray

				String result="";

				try{

					result = response.getString("result");


				}catch(Exception e){
					e.printStackTrace();
				}



				AppConstants.flag=0;

				if(result.equals("true")){
					temp.remove(0);
					deleteAndAddTransactions(temp);

					mBuilder.setAutoCancel(true);
					mBuilder.setContentTitle("Successfully uploaded");
					mBuilder.setContentText("Done")
					// Removes the progress bar
					.setProgress(0,0,false);
					mNotifyManager.notify(0, mBuilder.build());
					
					

					if(temp.size()>0){
						try {
							uplaodTransaction(context);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					Log.e("DEBUG", "Failed Transaction left"+temp.size());
					
				}else{
					mBuilder.setAutoCancel(true);
					mBuilder.setContentTitle("Failed to uplaod");
					mBuilder.setContentText("Please check the internet connection")
					// Removes the progress bar
					.setProgress(0,0,false);
					mNotifyManager.notify(0, mBuilder.build());
				}

				Log.e("SUCCESS", response+"");





			}


			@Override
			public void onStart() {

				AppConstants.flag=1;
			}




			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub
				AppConstants.flag=0;

				Toast toast =Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				toast.show();

				Log.e("FALIURE", errorResponse+"");
				
				mBuilder.setAutoCancel(true);
				mBuilder.setContentTitle("Failed to uplaod");
				mBuilder.setContentText("Please check the internet connection")
				// Removes the progress bar
				.setProgress(0,0,false);
				mNotifyManager.notify(0, mBuilder.build());

				AppConstants.flag=0;

			}





		});
	}
	
	
public ArrayList<HashMap<String, String>> getFailedTransactions(){
		
		
		Type type = new TypeToken<ArrayList<HashMap<String, String>>>(){}.getType();
		Gson gson = new Gson();

		ArrayList<HashMap<String, String>> temp = new ArrayList<HashMap<String,String>>();

		
		String inspectionArray = mSharedPreferences.getString("failedtransactions", null);

		if(inspectionArray!=null  ){
			temp = gson.fromJson(inspectionArray, type);	

			Log.e("DEBUG", "getting failed transactions."+temp.size());

		}

		return temp;
	}


	public void addTransactions(HashMap<String, String> temp){
		Gson gson = new Gson();
		

		ArrayList<HashMap<String, String>> gettemp = getFailedTransactions();

		gettemp.add(temp);
		
		if(gettemp.size()>0){
			String a = gson.toJson(gettemp);
			e.putString("failedtransactions", a);
			e.apply();
		}



		Log.e("DEBUG", "Added transaction."+gettemp.size());
	}

	public void deleteAndAddTransactions(ArrayList<HashMap<String, String>> temp){
		Gson gson = new Gson();
		

			String a = gson.toJson(temp);
			e.putString("failedtransactions", a);
			e.apply();
		
			Log.e("DEBUG", "delected and Saved data.");
	}

}