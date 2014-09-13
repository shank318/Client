package com.example.client;


import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.client.R;

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
	
	ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
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
			Intent i=new Intent(getApplicationContext(),Selection.class);
			startActivity(i);
			overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);

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

		JSONObject params = new JSONObject();
        
		params.put("app_usr", username.getText().toString());
		params.put("app_pwd", password.getText().toString());

		CallNetwork.post(getApplicationContext(),"", params, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				// If the response is JSONObject instead of expected JSONArray
//				Intent i=new Intent(getApplicationContext(),Selection.class);
//				startActivity(i);
//				overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
				Log.e("DEBUG3", response+"");
			}
			
			@Override
		    public void onStart() {
//				pd = new ProgressDialog(MainActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
//				pd.show();
		    }

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
				
			//	pd.cancel();
				// If the response is JSONObject instead of expected JSONArray
//				Intent i=new Intent(getApplicationContext(),Selection.class);
//				startActivity(i);
//				overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
				Toast toast =Toast.makeText(MainActivity.this, "Wrong username or password", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.TOP, 0, 300);
				toast.show();
				Log.e("DEBUG3", response+"");
			}
			

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub
			//	pd.cancel();
				
				Toast toast =Toast.makeText(MainActivity.this, "Wrong username or password", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.TOP, 0, 0);
				toast.show();
				
				Log.e("DEBUG1", errorResponse+"");
			}

			
			
			

		});
	}
	
public void onClick(View view) {

		

		InputMethodManager imm = (InputMethodManager) view.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

}
