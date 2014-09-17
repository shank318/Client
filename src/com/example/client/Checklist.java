package com.example.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.client.R;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
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

public class Checklist extends BaseActivity{
	
	ListView checkList;
	List<String> list=new ArrayList<String>();
	ArrayAdapter<String> arr;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.checklist);
		
		checkList =(ListView) findViewById(R.id.checkListView);
		checkList.setAdapter(new Listadapter(getApplicationContext(),R.id.checkListView));
		list.add("YES");
		list.add("NO");
		list.add("NA");
	    arr=new ArrayAdapter<String>(getApplicationContext(), R.layout.splnner_background,list);
	    
	    findViewById(R.id.idupload).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
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
		    
			spinner.setAdapter(arr);
		   

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
