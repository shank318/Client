package com.retails.customer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.retails.customer.R;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Checklist extends BaseActivity{

	ListView checkList;
	List<String> list=new ArrayList<String>();
	ArrayAdapter<String> arr;

	Listadapter adapter;
	ArrayList<HashMap<String, String>> selectedSpinner = new ArrayList<HashMap<String,String>>();
	String master;
	String photoD;
	String images;
	String checklist="";

	HashMap<String, String> tMap = new HashMap<String, String>();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.checklist);

		if(mSharedPreferences.getString("USERID", "").equals("")){
			Intent i = new Intent(getApplicationContext(),Selection.class);
			startActivity(i);
		}





		getActionBar().setDisplayHomeAsUpEnabled(true); 

		Intent i = getIntent();

		if(i!=null){

			master = i.getStringExtra("master");
			images = i.getStringExtra("images");
			photoD = i.getStringExtra("photod");

		}else{
			return;
		}

		checkList =(ListView) findViewById(R.id.checkListView);
		adapter= new Listadapter(getApplicationContext(),R.id.checkListView);

		checkList.setAdapter(adapter);
		list.add("SELECT");
		list.add("YES");
		list.add("NO");
		list.add("NA");
		arr=new ArrayAdapter<String>(getApplicationContext(), R.layout.splnner_background,list);

		findViewById(R.id.idupload).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if(selectedSpinner.size()!=adapter.getCount()){
					Toast.makeText(getApplicationContext(), "Please select all the choices", Toast.LENGTH_LONG).show();
				}else{
					constructCheckList();


					try {
						uplaodTransaction();
						Instalation.fa.finish();
						finish();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}



			}
		});

	}





	public class Listadapter extends BaseAdapter {


		HashMap<Integer, Integer> temp = new HashMap<Integer, Integer>();

		public Listadapter(Context context, int resource) {
			super();
			// TODO Auto-generated constructor stub
		}


		class ViewHolder{
			public TextView textView;
			public Spinner spinner;
		}

		@Override
		public View getView(final int position, View view, ViewGroup parent) {


			View v = view;
			final ViewHolder holder;

			LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			if (v ==null ) {

				holder = new ViewHolder();

				v = inflater.inflate(R.layout.checklistviewrow, parent, false);
				holder.textView = (TextView) v.findViewById(R.id.text);
				holder.spinner =(Spinner) v.findViewById(R.id.idspiner);
				holder.spinner.setAdapter(arr);



				v.setTag(holder);
			} else {
				// Get the ViewHolder back to get fast access to the TextView
				// and the ImageView.
				holder = (ViewHolder) v.getTag();
			}

			final HashMap<String, String> map = AppConstants.getInstance().checkListArray.get(position);


			holder.textView.setText(map.get("description"));



			if(temp.get(position)==null){
				holder.spinner.setSelection(0);
			}else{

				holder.spinner.setSelection(temp.get(position));
			}

			holder.spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int pos, long arg3) {
					// TODO Auto-generated method stub



					if(pos!=0 ){


						Log.e("DEBUG", "ADDED"+map.get("checklistid")+selectedSpinner.size());
						HashMap<String, String> map2 = new HashMap<String, String>();
						map2.put("id", map.get("checklistid"));
						map2.put("value", holder.spinner.getSelectedItem().toString());
						if(temp.get(position)==null){
							selectedSpinner.add(position,map2);
							temp.put(position, holder.spinner.getSelectedItemPosition());
						}
						else{
							Log.e("DEBUG", "CHANGED"+temp.get(position));
							selectedSpinner.set(position,map2);
						}



					}else if(pos==0){

						if(temp.get(position)!=null){
							Log.e("DEBUG", "REMOVED"+temp.get(position));
							selectedSpinner.remove(position);
							temp.remove(position);
						}else{
							Log.e("DEBUG", "NOTADDED");
						}


					}

				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub

				}
			});


			return v;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub

			return AppConstants.getInstance().checkListArray.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

	}



	void constructCheckList(){



		for(int i=0;i<selectedSpinner.size();i++){

			Log.e("DEBUG", checklist);
			Log.e("DEBUG", selectedSpinner.get(i).get("id"));
			checklist = checklist+ selectedSpinner.get(i).get("id")+"-"+
					selectedSpinner.get(i).get("value")+",";

		}


		checklist = checklist.replaceAll(",$", "");

	}




	void uplaodTransaction() throws JSONException{

		RequestParams params = new RequestParams();

		Log.e("DETAILS", master+"   "+photoD+"  "+checklist);


		tMap.put("master", master);
		tMap.put("images", images);
		tMap.put("checklist", checklist);
		tMap.put("photod", photoD);

		params.put("sync_master",master);
		params.put("sync_photo",photoD);
		params.put("sync_checklist",checklist);

		String[] img = images.split(",");


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
				AppConstants.flag=0;

				if(throwable.getCause() instanceof ConnectTimeoutException){
					mBuilder.setAutoCancel(true);
					mBuilder.setContentTitle("Time out connection error");
					mBuilder.setContentText("Your internet seems soo slow")
					// Removes the progress bar
					.setProgress(0,0,false);
					mNotifyManager.notify(0, mBuilder.build());
				}else{




					mBuilder.setAutoCancel(true);
					mBuilder.setContentTitle("Failed to uplaod");
					mBuilder.setContentText("Please check the internet connection")
					// Removes the progress bar
					.setProgress(0,0,false);
					mNotifyManager.notify(0, mBuilder.build());
				}
				addTransactions(tMap);

			}



			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				// If the response is JSONObject instead of expected JSONArray

				AppConstants.flag=0;
				Log.e("SUCCESS", response+"");
				String result="";

				try{

					result = response.getString("result");


				}catch(Exception e){
					e.printStackTrace();
				}


				if(result.equals("true")){


					mBuilder.setAutoCancel(true);
					mBuilder.setContentTitle("Successfully uploaded");
					mBuilder.setContentText("Done")
					// Removes the progress bar
					.setProgress(0,0,false);
					mNotifyManager.notify(0, mBuilder.build());


				}else{

					addTransactions(tMap);
					mBuilder.setAutoCancel(true);
					mBuilder.setContentTitle("Failed to upload");
					mBuilder.setContentText("Please check your internet connection")
					// Removes the progress bar
					.setProgress(0,0,false);
					mNotifyManager.notify(0, mBuilder.build());

				}


			}


			@Override
			public void onStart() {
				AppConstants.flag=1;
				mBuilder
				.setContentTitle("Uploading....")                  
				.setSmallIcon(R.drawable.ic_launcher)
				.setAutoCancel(true);

			}




			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub

				AppConstants.flag=0;

				if(throwable.getCause() instanceof ConnectTimeoutException){
					mBuilder.setAutoCancel(true);
					mBuilder.setContentTitle("Time out connection error");
					mBuilder.setContentText("Your internet seems soo slow")
					// Removes the progress bar
					.setProgress(0,0,false);
					mNotifyManager.notify(0, mBuilder.build());
				}else{




					mBuilder.setAutoCancel(true);
					mBuilder.setContentTitle("Failed to uplaod");
					mBuilder.setContentText("Please check the internet connection")
					// Removes the progress bar
					.setProgress(0,0,false);
					mNotifyManager.notify(0, mBuilder.build());
				}

				Toast toast =Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				toast.show();
				addTransactions(tMap);

				Log.e("FALIURE", errorResponse+"");
			}





		});
	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub


		switch(item.getItemId())
		{

		case android.R.id.home:
			finish();
			break;
		case R.id.action_settings:
			// Here if you wish to do future process for ex. move to another activi

			if(AppConstants.flag==0){
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
			}


		}

		return super.onOptionsItemSelected(item);
	}


}
