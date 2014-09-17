package com.example.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
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

	String path="";
	
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
				try {
					validateUserNamePassword();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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

					listviewlist.add(type.getSelectedItem().toString()+":-  "+data.getStringExtra("res"));
					adapter.notifyDataSetChanged();
					type.setSelection(0);
					
					path = data.getStringExtra("res");
					
				}
			}




		}
		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {

	//		Uri selectedImage = data.getData();
			ArrayList<String> filePathColumn = data.getStringArrayListExtra("res");

//			Cursor cursor = getContentResolver().query(selectedImage,
//					filePathColumn, null, null, null);
//			cursor.moveToFirst();
//
//			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//			String picturePath = cursor.getString(columnIndex);
//			Log.e("Shank", picturePath);
			
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

//		Log.e("DEBUG", new FileBody(new File(path))+"");
		File file = new File(path);
		
		try {
			params.put("app_image",file);
			params.put("app_image",file);
			params.put("app_image",file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		CallNetwork.post("", params, new JsonHttpResponseHandler(){
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				Log.e("DEBUG1", responseString+"");
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				// If the response is JSONObject instead of expected JSONArray
                
				Log.e("DEBUG", response+"");

			}

			@Override
			public void onStart() {
				
			}




			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub
				

				Toast toast =Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				toast.show();

				Log.e("DEBUG1", errorResponse+"");
			}





		});
	}

}
