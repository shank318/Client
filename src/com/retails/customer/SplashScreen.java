package com.retails.customer;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.retails.customer.R;

public class SplashScreen extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.splash_layout);
		RelativeLayout view =(RelativeLayout)findViewById(R.id.root_layout);

		view.postDelayed(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Intent intent= new Intent(getApplicationContext(), MainActivity.class);
				startActivity(intent);
				
			}

		}, 1500);
	}

}
