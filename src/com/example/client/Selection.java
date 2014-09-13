package com.example.client;

import com.example.client.R;

import android.app.Activity;
import android.content.Intent;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Selection extends Activity{
	Button inseption,installation;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.selection);
		inseption=(Button) findViewById(R.id.idincept);
		installation=(Button) findViewById(R.id.idinstal);
		inseption.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i=new Intent(getApplicationContext(), Inseption.class);
				startActivity(i);
				
			}
		});
        installation.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i=new Intent(getApplicationContext(), Instalation.class);
				startActivity(i);
				
			}
		});
		
	}
}
