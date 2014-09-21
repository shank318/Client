package com.retails.customer;


import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.retails.customer.R;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;
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
	SharedPreferences.Editor e;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	public Builder mBuilder;
	public NotificationManager mNotifyManager ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// Shared Preferences
		mSharedPreferences= getSharedPreferences(AppConstants.KEY, 0);
		e = mSharedPreferences.edit();
		mNotifyManager =
				(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mBuilder = new NotificationCompat.Builder(this);

		


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

			if(AppConstants.flag==0 && getFailedTransactions().size()==0){
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
			}else if(AppConstants.flag==1){

				Toast.makeText(getApplicationContext(), "Please wait until your transaction is completed", Toast.LENGTH_LONG).show();
			}else if(getFailedTransactions().size()>0){
				Toast.makeText(getApplicationContext(), "Your previous transaction is not completed", Toast.LENGTH_LONG).show();
			}


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
