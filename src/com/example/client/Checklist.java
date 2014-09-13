package com.example.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.client.R;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class Checklist extends Activity{
	
	ListView checkList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.checklist);
		
		checkList =(ListView) findViewById(R.id.checkListView);
		checkList.setAdapter(new Listadapter(getApplicationContext(),R.id.checkListView));
		
		
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
		   
		    View rowView = inflater.inflate(R.layout.checklistviewrow, parent, false);
		    
		    HashMap<String, String> map = AppConstants.getInstance().checkListArray.get(position);
		    
		    TextView textView = (TextView) rowView.findViewById(R.id.text);
		    textView.setText(map.get("description"));
		    
		    Spinner spinner=(Spinner) rowView.findViewById(R.id.idspiner);
		    
		  

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

}
