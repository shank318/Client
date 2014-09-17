package com.example.client;


import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class BaseActivity extends Activity{

	public  SharedPreferences mSharedPreferences;

	protected ImageLoader imageLoader = ImageLoader.getInstance();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// Shared Preferences
		mSharedPreferences= getSharedPreferences(AppConstants.KEY, 0);



		if(mSharedPreferences.getString("USERID", "").equals("")){
			Intent i = new Intent(getApplicationContext(),MainActivity.class);
			startActivity(i);
		}

		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
			if(menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);

		return super.onCreateOptionsMenu(menu);

	}	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub


		switch(item.getItemId())
		{

		case R.id.action_settings:
			// Here if you wish to do future process for ex. move to another activi

			
			SharedPreferences sharedPref= getApplicationContext().getSharedPreferences(AppConstants.KEY,0);			 
			SharedPreferences.Editor editor = sharedPref.edit();
			editor.clear();  
			editor.commit();
			Intent i=new Intent(getApplicationContext(),MainActivity.class);
			// Closing all the Activities
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		
			// Add new Flag to start new Activity
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			startActivity(i);
			overridePendingTransition  (R.anim.right_slide_in, R.anim.right_slide_out);
			finish();


		}

		return super.onOptionsItemSelected(item);
	}

	
	public void getRecent() {

		Type type = new TypeToken<ArrayList<HashMap<String, String>>>(){}.getType();
		Gson gson = new Gson();

		String inspectionArray = mSharedPreferences.getString("inspectionPhotoType", null);
		String installationArray = mSharedPreferences.getString("installationPhotoType", null);
		String checklist = mSharedPreferences.getString("checklist", null);
		if(inspectionArray!=null && installationArray!=null && checklist!=null ){
			AppConstants.getInstance().inspectionPhotoTypeArray = gson.fromJson(inspectionArray, type);	
			AppConstants.getInstance().installationPhotoTypeArray = gson.fromJson(installationArray, type);		
			AppConstants.getInstance().checkListArray = gson.fromJson(checklist, type);		
			
		}

	}
}
