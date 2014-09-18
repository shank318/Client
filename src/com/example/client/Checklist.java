package com.example.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.client.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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
	String checklist;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.checklist);


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

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) getApplicationContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View rowView = inflater.inflate(R.layout.checklistviewrow, parent, false);

			final HashMap<String, String> map = AppConstants.getInstance().checkListArray.get(position);

			TextView textView = (TextView) rowView.findViewById(R.id.text);
			textView.setText(map.get("description"));

			final   Spinner spinner=(Spinner) rowView.findViewById(R.id.idspiner);

			if(temp.get(position)==null){
			spinner.setAdapter(arr);
			}else{
				spinner.setAdapter(arr);
				spinner.setSelection(temp.get(position));
			}

			spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int pos, long arg3) {
					// TODO Auto-generated method stub
					
					temp.put(position, spinner.getSelectedItemPosition());

					if(pos!=0){
						Log.e("DEBUG", "ADDED");
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("id", map.get("checklistid"));
						map.put("value", spinner.getSelectedItem().toString());
						selectedSpinner.add(map);

					}

				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub

				}
			});


			return rowView;
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

			checklist = checklist+ selectedSpinner.get(i).get("id")+"-"+
					selectedSpinner.get(i).get("value");

		}



	}

}
