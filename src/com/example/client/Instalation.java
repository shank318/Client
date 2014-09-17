package com.example.client;

import java.util.ArrayList;
import java.util.List;

import com.example.client.R;
import com.example.client.Inseption.Listadapter;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.instalation);
		next=(Button) findViewById(R.id.idnext);
		type=(Spinner) findViewById(R.id.idspi);
		camra=(ImageButton) findViewById(R.id.camerainstal);
		attach=(ImageButton) findViewById(R.id.attachinstal);
		lv=(ListView) findViewById(R.id.list);



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
				Intent i=new Intent(getApplicationContext(), Checklist.class);
				startActivity(i);
				overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);

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
		Intent i = new Intent(
				Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		i.setType("image/*");
		
		startActivityForResult(Intent.createChooser(i, "Select Picture"), RESULT_LOAD_IMAGE);
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
				}
			}




		}
		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
		
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Log.e("Shank",filePathColumn.length+"");
			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			
			listviewlist.add(type.getSelectedItem().toString()+":-  "+picturePath);

			adapter.notifyDataSetChanged();
			type.setSelection(0);
			cursor.close();
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

}
