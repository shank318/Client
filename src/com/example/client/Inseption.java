package com.example.client;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class Inseption extends Activity{
	private static final int RESULT_LOAD_IMAGE = 1;
	Button upload;
	Spinner type;
	ImageButton camra,attach;
	List< String> listviewlist=new ArrayList<String>();
	Listadapter adapter;
	ListView lv;
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
		list.add("Side View");
		list.add("Top View");
		
		
		
		adapter=new Listadapter(getApplicationContext(), R.id.list);
		lv.setAdapter(adapter);
		ArrayAdapter<String> arr=new ArrayAdapter<String>(this, android.R.layout.simple_selectable_list_item,list);
		arr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		type.setAdapter(arr);
        camra.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i=new Intent(getApplicationContext(), Cam.class) ;
				startActivityForResult(i, 5);
				overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
				//startActivity(i);
			}
		});
        attach.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				openGallery();
			}
		});
	}
	private void openGallery() {
		Intent i = new Intent(
				Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				 
				startActivityForResult(i, RESULT_LOAD_IMAGE);
	}
	@Override
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	     super.onActivityResult(requestCode, resultCode, data);
	   
	     
	     
	     
	      if(requestCode==5){
	    	  
	    	  if(resultCode==6){
	    	 
               if(data!=null)
               {
            	   Log.e("helo", data.getStringExtra("res"));
	    	          listviewlist.add(data.getStringExtra("res"));
	    	 
	    	  adapter.notifyDataSetChanged();
               }
	    	  }
	    	  

		     		
		     
	      }
	      if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
	    	  Log.e("Shank", "okkkkkkkkkkkkkkkk");
		         Uri selectedImage = data.getData();
		         String[] filePathColumn = { MediaStore.Images.Media.DATA };
		 
		         Cursor cursor = getContentResolver().query(selectedImage,
		                 filePathColumn, null, null, null);
		         cursor.moveToFirst();
		 
		         int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		         String picturePath = cursor.getString(columnIndex);
		         Log.e("Shank", picturePath);
		         listviewlist.add(picturePath);
	
		         adapter.notifyDataSetChanged();
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
					listviewlist.remove(position);
					adapter.notifyDataSetChanged();
					
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
