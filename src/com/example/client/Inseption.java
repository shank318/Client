package com.example.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;




import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Inseption extends BaseActivity{
	private static final int RESULT_LOAD_IMAGE = 1;
	Button upload;
	Spinner type;
	ImageButton camra,attach;
	List< String> listviewlist=new ArrayList<String>();
	Listadapter adapter;
	ListView lv;
	ProgressDialog pd;
	EditText uniqueCode;

	ArrayList<HashMap<String, String>> photoType = new ArrayList<HashMap<String,String>>();
	

	String master="";
	String photoD="";
	String images="";
	String checkList="";

	DatabaseHandler db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inception);
		upload=(Button) findViewById(R.id.idupload);
		camra=(ImageButton) findViewById(R.id.camerainseption);
		attach=(ImageButton) findViewById(R.id.attachinseption);
		type=(Spinner) findViewById(R.id.idspiner);
		lv=(ListView) findViewById(R.id.list);
		uniqueCode =(EditText) findViewById(R.id.idunicode);

		db = new DatabaseHandler(this);



		List<String> list=new ArrayList<String>();

		if(AppConstants.getInstance().inspectionPhotoTypeArray.size()==0){
			getRecent();
		}

		for(int i=0;i<AppConstants.getInstance().inspectionPhotoTypeArray.size();i++){
			list.add(AppConstants.getInstance().inspectionPhotoTypeArray.get(i).get("attachmenttype"));

		}

		list.add(0,"Select");
		adapter=new Listadapter(getApplicationContext(), R.id.list);
		lv.setAdapter(adapter);
		ArrayAdapter<String> arr=new ArrayAdapter<String>(this, android.R.layout.simple_selectable_list_item,list);
		arr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		type.setAdapter(arr);
		camra.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if(!type.getSelectedItem().toString().equals("Select")){
					Intent i=new Intent(getApplicationContext(), Cam.class) ;
					startActivityForResult(i, 5);
					overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
				}else{
					Toast.makeText(getApplicationContext(), "Please select the type of the photo", Toast.LENGTH_LONG).show();
				}
			}
		});
		attach.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!type.getSelectedItem().toString().equals("Select")){
					openGallery();
				}else{
					Toast.makeText(getApplicationContext(), "Please select the type of the photo", Toast.LENGTH_LONG).show();
				}
			}
		});



		findViewById(R.id.idupload).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//new uploadvideo(getApplicationContext()).execute();

				if(uniqueCode.getText().toString().trim().length()>0){

				try {
					validateUserNamePassword();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				}else{
					uniqueCode.setError("Please enter unique code");
				}
				
			}
		});
	}
	private void openGallery() {
		Intent i = new Intent(getApplicationContext(),MultiPhotoSelectActivity.class);

		startActivityForResult(i, RESULT_LOAD_IMAGE);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);




		if(requestCode==5){

			if(resultCode==6){

				if(data!=null)
				{


					HashMap<String, String> map = new HashMap<String, String>();
					map.put("id", AppConstants.getInstance().inspectionPhotoTypeArray.get(type.getSelectedItemPosition()-1).get("attachmenttypeid"));
					map.put("value", data.getStringExtra("res"));
					photoType.add(map);

					listviewlist.add(type.getSelectedItem().toString()+":-  "+data.getStringExtra("res"));
					adapter.notifyDataSetChanged();
					type.setSelection(0);

					

				}
			}




		}
		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {


			ArrayList<String> filePathColumn = data.getStringArrayListExtra("res");


			for(int i=0;i<filePathColumn.size();i++){
				HashMap<String, String> map = new HashMap<String, String>();
				File file = new File(filePathColumn.get(i));

				map.put("id", AppConstants.getInstance().inspectionPhotoTypeArray.get(type.getSelectedItemPosition()-1).get("attachmenttypeid"));
				map.put("value", file.getName());
				photoType.add(map);
			}


			for(int i=0;i<filePathColumn.size();i++){
				listviewlist.add(type.getSelectedItem().toString()+":-  "+filePathColumn.get(i));
			}

			adapter.notifyDataSetChanged();
			type.setSelection(0);
			//		cursor.close();
		}


		// String picturePath contains the path of selected Image
	}



	public class Listadapter extends BaseAdapter {


		public Listadapter(Context context, int resource) {
			super();
			// TODO Auto-generated constructor stub
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) getApplicationContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View rowView = inflater.inflate(R.layout.listviewxml, parent, false);
			TextView textView = (TextView) rowView.findViewById(R.id.text);
			textView.setText(listviewlist.get(position));

			ImageButton remove=(ImageButton) rowView.findViewById(R.id.remov);
			remove.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub


					AlertDialog.Builder builder1 = new AlertDialog.Builder(Inseption.this,AlertDialog.THEME_HOLO_LIGHT);
					builder1.setMessage("Do you want to delete this image?");
					builder1.setCancelable(true);
					builder1.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							listviewlist.remove(position);
							adapter.notifyDataSetChanged();
							type.setSelection(0);
						}
					});
					builder1.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});

					AlertDialog alert11 = builder1.create();
					alert11.show();

				}
			});
			// Change the icon for Windows and iPhone


			return rowView;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub

			return listviewlist.size();
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

	public void onClick(View view) {


		InputMethodManager imm = (InputMethodManager) view.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}




	void validateUserNamePassword() throws JSONException{

		RequestParams params = new RequestParams();

		Log.e("DETAILS", master+"   "+photoD+"  "+checkList);
		params.put("sync_master",master);
		params.put("sync_photo",photoD);
		params.put("sync_checklist",checkList);



		CallNetwork.post("", params, new JsonHttpResponseHandler(){

			@Override
			public void onProgress(int bytesWritten, int totalSize) {
				// TODO Auto-generated method stub
				super.onProgress(bytesWritten, totalSize);
				Log.e("ONPROGRESS", bytesWritten+"");
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				Log.e("ONFALIURE", responseString+"aaaaaa");
				pd.cancel();
				
				db.addFeeds(master, photoD, images, checkList);

			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
				// If the response is JSONObject instead of expected JSONArray
				pd.cancel();
				Log.e("SUCCESS", response+"");

			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				// If the response is JSONObject instead of expected JSONArray
				pd.cancel();
				Log.e("SUCCESS", response+"");

			}


			@Override
			public void onStart() {

				pd = new ProgressDialog(Inseption.this, ProgressDialog.THEME_HOLO_LIGHT);
				pd.setMessage("Signing in...");
				pd.show();

				constructArray();
			}




			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub

				pd.cancel();
				Toast toast =Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				toast.show();
				db.addFeeds(master, photoD, images, checkList);
				Log.e("FALIURE", errorResponse+"");
			}





		});
	}


	void constructArray(){


		for(int i =0;i< photoType.size();i++){
			photoD= type+photoType.get(i).get("id")+"-"+photoType.get(i).get("value")+",";
			images= images+photoType.get(i).get("value")+",";
		}

		String userId = mSharedPreferences.getString("USERID", "");
		Date date = new Date();
		String datesting = date.toString();

		master = "Unique_Code-"+uniqueCode.getText().toString()+","+
				"Transaction_Type-inspection,"+"User_Id-"+userId+
				",Transaction_Date-"+datesting;




	}

}
