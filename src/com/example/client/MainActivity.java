package com.example.client;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.client.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;



import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		
		// Shared Preferences
		mSharedPreferences= getSharedPreferences(AppConstants.KEY, 0);
		e = mSharedPreferences.edit();
		
		
		if(!mSharedPreferences.getString("USERID", "").equals("")){
			Intent i = new Intent(getApplicationContext(),Selection.class);
			startActivity(i);
		}


		username=(EditText) findViewById(R.id.iduser);
		password=(EditText) findViewById(R.id.idpass);
		login=(Button) findViewById(R.id.idlog);
		final SharedPreferences sh=getSharedPreferences("login", MODE_PRIVATE);
		Editor e=sh.edit();
		e.putString("username", "abc");
		e.putString("password", "123");
		e.commit();


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


		CallNetwork.post("", params, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				// If the response is JSONObject instead of expected JSONArray
                
				Log.e("DEBUG3", response+"");

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
	
	
	
}
