package com.retails.customer;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;


public class Cam extends Activity implements SurfaceHolder.Callback
{
	ImageButton capture,retake,ok;
	SurfaceView suf;
	Camera cam;
	String filename;
	String path;
	PhotoHandler handler;
	SurfaceHolder hold;
	byte []d;
	//	Bitmap bt;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camrea);



		suf=(SurfaceView)findViewById(R.id.surfacview);
		capture=(ImageButton) findViewById(R.id.capture);
		retake=(ImageButton) findViewById(R.id.retake);
		ok=(ImageButton) findViewById(R.id.ok);
		hold=suf.getHolder();
		hold.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		hold.addCallback(this);

		//	showImageView = (ImageView) findViewById(R.id.capturedImage);
		retake.setVisibility(View.INVISIBLE);
		ok.setVisibility(View.INVISIBLE);

		handler = new PhotoHandler();

		retake.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				retake.setVisibility(View.INVISIBLE);
				ok.setVisibility(View.INVISIBLE);
				capture.setVisibility(View.VISIBLE);

				releaseCamera();
				
				try {
					cam=Camera.open();
					cam.setDisplayOrientation(90);
					cam.setPreviewDisplay(hold);
					//cam.startPreview();


				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				myStartPreview();

			}
		});
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//releaseCamera();

				Intent i=new Intent();
				i.putExtra("res", filename);
				Log.e("hello00000", filename+"");
				setResult(6, i);
				finish();
			}
		});
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub

		Log.e("DEBUG", "camera opening..");
		
		try {
			cam=Camera.open();
			cam.setDisplayOrientation(90);
			cam.setPreviewDisplay(hold);
			cam.startPreview();
			Log.e("DEBUG", "camera opened..");

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			capture.setVisibility(View.INVISIBLE);
			Log.e("DEBUG", "not opened..");
			Toast.makeText(getApplicationContext(), "Failed to connect camera", Toast.LENGTH_LONG).show();
		}


	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		

	}


	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Log.e("DEBUG", "clicked");

		retake.setVisibility(View.VISIBLE);
		ok.setVisibility(View.VISIBLE);
		capture.setVisibility(View.INVISIBLE);

		cam.takePicture(null,null,handler);

	}




	public void myStartPreview() {
		if ( (cam != null)) {
			cam.startPreview();

		}
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();


	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub

		super.onPause();
		releaseCamera();

	}
	private void releaseCamera(){

		Log.e("DEBUG", "released");
		if(cam!=null){
			cam.stopPreview(); 
			cam.setPreviewCallback(null);
			cam.release();        // release the camera for other applications
			cam = null;
		}

	}

	class PhotoHandler implements PictureCallback{


		@Override
		public void onPictureTaken(byte[] data, Camera arg1) {
			// TODO Auto-generated method stub
			Log.e("DEBUG", "aaa");
			FileOutputStream outStream = null;
			try {
				File miDirs = new File(
						Environment.getExternalStorageDirectory() + "/Retails");
				if (!miDirs.exists())
					miDirs.mkdirs();



				filename = System.currentTimeMillis()+".jpg";

				File file = new File(miDirs.toString(),filename);

				filename = file.getAbsolutePath();

				BitmapFactory.Options options=new BitmapFactory.Options();
				options.inSampleSize = 8;
				options.inScaled=false;
				Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length,options);

				Matrix matrix = new Matrix();
				matrix.postRotate(90);
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

				ByteArrayOutputStream bos = new ByteArrayOutputStream();


				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
				byte[] bitmapdata = bos.toByteArray();

				//write the bytes in file
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(bitmapdata);
				fos.close();


				cam.stopPreview();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
			}
		}


	}


}

