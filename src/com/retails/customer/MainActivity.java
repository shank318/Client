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

import com.retails.customer.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;



import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;


public class MainActivity extends Activity
{








	EditText username,password;
	Button login;
	public  SharedPreferences mSharedPreferences;
	SharedPreferences.Editor e;
	ProgressDialog pd;

	public Builder mBuilder;
	public NotificationManager mNotifyManager ;

	ArrayList<HashMap<String, String>> temp = new ArrayList<HashMap<String,String>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);


		// Shared Preferences
		mSharedPreferences= getSharedPreferences(AppConstants.KEY, 0);
		e = mSharedPreferences.edit();

		mNotifyManager =
				(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mBuilder = new NotificationCompat.Builder(this);

		mBuilder
		.setContentTitle("Uploading filed transactions....")                  
		.setSmallIcon(R.drawable.ic_launcher)
		.setAutoCancel(true);

		if(!mSharedPreferences.getString("USERID", "").equals("")){
			Intent i = new Intent(getApplicationContext(),Selection.class);
			startActivity(i);
		}


		username=(EditText) findViewById(R.id.iduser);
		password=(EditText) findViewById(R.id.idpass);
		login=(Button) findViewById(R.id.idlog);



		password.setOnEditorActionListener(new TextView.OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView text, int actionId, KeyEvent arg2) {
				// TODO Auto-generated method stub
				if (actionId == EditorInfo.IME_ACTION_DONE) {

					checkCredentials();
					return true;
				}
				return false;
			}
		});


		/*
		 * To prevent user to press enter and new line in edittext
		 */
		password.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// If the event is a key-down event on the "enter" button
				if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
						(keyCode == KeyEvent.KEYCODE_ENTER)) {
					// Perform action on key press

					return true;
				}
				return false;
			}
		});


		/*
		 * To prevent user to press enter and new line in edittext
		 */
		username.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// If the event is a key-down event on the "enter" button
				if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
						(keyCode == KeyEvent.KEYCODE_ENTER)) {
					// Perform action on key press

					return true;
				}
				return false;
			}
		});

		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				checkCredentials();
			}
		});







	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		Log.e("DEBUG", "CHECKING FAILED TR..");
		temp = getFailedTransactions();

		if(temp.size()>0 &&  AppConstants.flag==0){
			try {
				uplaodTransaction();
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}



	void checkCredentials(){
		if(username.getText().toString().trim().length()!=0 && password.getText().toString().trim().length()!=0)
		{

			try {
				validateUserNamePassword();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(username.getText().toString().trim().length()==0)
		{
			username.setError("Please enter your username");
		}else if(password.getText().toString().trim().length()==0)
		{
			password.setError("Please enter your password");
		}
	}

	void validateUserNamePassword() throws JSONException{

		RequestParams params = new RequestParams();

		params.put("app_usr", username.getText().toString());
		params.put("app_pwd", password.getText().toString());


		CallNetwork.post(CallNetwork.LOGIN_URL, params, new JsonHttpResponseHandler(){


			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				// If the response is JSONObject instead of expected JSONArray

				Log.e("DEBUG3", response+""+statusCode);

				if(response.toString().contains("status")){

					Toast toast =Toast.makeText(MainActivity.this, "Wrong username or password", Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
					toast.show();

				}else{

					try {
						JSONArray installation = response.getJSONArray("installation_photo_type");

						for(int i=0;i<installation.length();i++){

							JSONObject obj = installation.getJSONObject(i);

							String id = obj.getString("attachmenttypeid");
							String type = obj.getString("attachmenttype");
							HashMap<String, String> map = new HashMap<String, String>();
							map.put("attachmenttypeid", id);
							map.put("attachmenttype", type);

							AppConstants.getInstance().installationPhotoTypeArray.add(map);

						}

						JSONArray inspection = response.getJSONArray("inspection_photo_type");
						for(int i=0;i<inspection.length();i++){

							JSONObject obj = inspection.getJSONObject(i);

							String id = obj.getString("attachmenttypeid");
							String type = obj.getString("attachmenttype");
							HashMap<String, String> map = new HashMap<String, String>();
							map.put("attachmenttypeid", id);
							map.put("attachmenttype", type);

							AppConstants.getInstance().inspectionPhotoTypeArray.add(map);

						}

						JSONArray checklist = response.getJSONArray("installation_checklist");
						for(int i=0;i<checklist.length();i++){

							JSONObject obj = checklist.getJSONObject(i);

							String id = obj.getString("description");
							String type = obj.getString("checklistid");
							HashMap<String, String> map = new HashMap<String, String>();
							map.put("description", id);
							map.put("checklistid", type);

							AppConstants.getInstance().checkListArray.add(map);

						}

						String user_id = response.getString("user_id");

						e.putString("USERID", user_id);
						e.apply();

						Intent i=new Intent(getApplicationContext(),Selection.class);
						startActivity(i);
						overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);

						saveRecentSearches();

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Toast toast =Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_LONG);
						toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
						toast.show();
					}


				}
				pd.cancel();
			}

			@Override
			public void onStart() {
				pd = new ProgressDialog(MainActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
				pd.setMessage("Signing in...");
				pd.show();
			}




			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub
				pd.cancel();

				Toast toast =Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				toast.show();

				Log.e("DEBUG1", errorResponse+"");
			}





		});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		if(pd!=null && pd.isShowing())
			pd.cancel();
	}

	public void onClick(View view) {



		InputMethodManager imm = (InputMethodManager) view.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	void saveRecentSearches(){

		// Check if the same term already exists


		Gson gson = new Gson();
		String installation = gson.toJson(AppConstants.getInstance().installationPhotoTypeArray);
		String inspection = gson.toJson(AppConstants.getInstance().inspectionPhotoTypeArray);
		String checklist = gson.toJson(AppConstants.getInstance().checkListArray);

		e.putString("inspectionPhotoType", inspection);
		e.putString("installationPhotoType", installation);
		e.putString("checklist", checklist);

		e.apply();

		Log.e("DEBUG", "Saved data.");
	}

	public ArrayList<HashMap<String, String>> getFailedTransactions(){
		Type type = new TypeToken<ArrayList<HashMap<String, String>>>(){}.getType();
		Gson gson = new Gson();

		ArrayList<HashMap<String, String>> temp = new ArrayList<HashMap<String,String>>();

		String inspectionArray = mSharedPreferences.getString("failedtransactions", null);
		Log.e("DEBUG", "getting failed transactions."+temp.size());
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



		Log.e("DEBUG", "Saved data.");
	}

	public void deleteAndAddTransactions(ArrayList<HashMap<String, String>> temp){
		Gson gson = new Gson();


		String a = gson.toJson(temp);
		e.putString("failedtransactions", a);
		e.apply();

		Log.e("DEBUG", "delected and Saved data.");
	}



	void uplaodTransaction() throws JSONException{

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
				Log.e("ONFALIURE", responseString+statusCode);


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


					Log.e("DEBUG", "Failed Transaction left"+temp.size());

					if(temp.size()>0){
						try {
							uplaodTransaction();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}


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


		});
	}



}
