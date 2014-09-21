package com.retails.customer;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.retails.customer.R;
import com.retails.customer.Inseption.Listadapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;

public class Instalation extends BaseActivity{
	private static final int RESULT_LOAD_IMAGE = 1;
	Button next;
	Spinner type;
	ImageButton camra,attach;
	List< String> listviewlist=new ArrayList<String>();
	Listadapter adapter;
	ListView lv;
	ArrayList<HashMap<String, String>> photoType = new ArrayList<HashMap<String,String>>();
    public static Activity fa;
	String master="";
	String photoD="";
	String images="";
	String checkList="";
	EditText uniqueCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		fa=this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.instalation);
		next=(Button) findViewById(R.id.idnext);
		type=(Spinner) findViewById(R.id.idspi);
		camra=(ImageButton) findViewById(R.id.camerainstal);
		attach=(ImageButton) findViewById(R.id.attachinstal);
		lv=(ListView) findViewById(R.id.list);

		uniqueCode =(EditText) findViewById(R.id.idunicode);


		if(mSharedPreferences.getString("USERID", "").equals("")){
			Intent i = new Intent(getApplicationContext(),Selection.class);
			startActivity(i);
		}

		List<String> list=new ArrayList<String>();

		if(AppConstants.getInstance().installationPhotoTypeArray.size()==0){
			getRecent();
		}




		for(int i=0;i<AppConstants.getInstance().installationPhotoTypeArray.size();i++){
			list.add(AppConstants.getInstance().installationPhotoTypeArray.get(i).get("attachmenttype"));

		}
		list.add(0,"Select");
		adapter=new Listadapter(getApplicationContext(), R.id.list);
		lv.setAdapter(adapter);
		ArrayAdapter<String> arr=new ArrayAdapter<String>(this, android.R.layout.simple_selectable_list_item,list);
		arr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		type.setAdapter(arr);
		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if(photoType.size()==0){
					Toast.makeText(getApplicationContext(), "Please select at least one type", Toast.LENGTH_LONG).show();	

				}else if (uniqueCode.getText().toString().trim().length()>0){

					constructArray();

					Intent i=new Intent(getApplicationContext(), Checklist.class);
					i.putExtra("master", master);
					i.putExtra("photod", photoD);
					i.putExtra("images", images);
					startActivity(i);
					overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);


				}else{

					uniqueCode.setError("Please enter the code");
				}

			}
		});
		camra.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!type.getSelectedItem().toString().equals("Select")){
					Intent i=new Intent(getApplicationContext(), Cam.class) ;
					startActivityForResult(i, 5);
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

				if(data!=null && data.getStringExtra("res")!=null)
				{
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("id", AppConstants.getInstance().inspectionPhotoTypeArray.get(type.getSelectedItemPosition()-1).get("attachmenttypeid"));
					map.put("value", data.getStringExtra("res"));
					photoType.add(map);

					listviewlist.add(type.getSelectedItem().toString()+":-  "+data.getStringExtra("res"));
					adapter.notifyDataSetChanged();
					type.setSelection(0);
				}else{
					Toast.makeText(getApplicationContext(), "NO Photo taken please try again", Toast.LENGTH_LONG).show();
				}
			}




		}
		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {

			ArrayList<String> filePathColumn = data.getStringArrayListExtra("res");


			for(int i=0;i<filePathColumn.size();i++){
				HashMap<String, String> map = new HashMap<String, String>();
				File file = new File(filePathColumn.get(i));
				listviewlist.add(type.getSelectedItem().toString()+":-  "+file.getName());
				map.put("id", AppConstants.getInstance().inspectionPhotoTypeArray.get(type.getSelectedItemPosition()-1).get("attachmenttypeid"));
				map.put("value", filePathColumn.get(i));
				photoType.add(map);
			}



			adapter.notifyDataSetChanged();
			type.setSelection(0);
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
					AlertDialog.Builder builder1 = new AlertDialog.Builder(Instalation.this,AlertDialog.THEME_HOLO_LIGHT);
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



	void constructArray(){


		for(int i =0;i< photoType.size();i++){
			photoD= photoD+photoType.get(i).get("id")+"-"+photoType.get(i).get("value")+",";
			images= images+photoType.get(i).get("value")+",";
		}


		photoD = photoD.replaceAll(",$", "");
		images = images.replaceAll(",$", "");

		String userId = mSharedPreferences.getString("USERID", "");
		Date date = new Date();
		SimpleDateFormat dateformatyyyyMMdd = new SimpleDateFormat("ddMMyyyy");


		String datesting = dateformatyyyyMMdd.format(date);

		master = "Unique_Code-"+uniqueCode.getText().toString()+","+
				"Transaction_Type-installation,"+"User_Id-"+userId+
				",Transaction_Date-"+datesting;




	}
}
