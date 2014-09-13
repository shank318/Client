package com.example.client;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class Cam extends Activity implements SurfaceHolder.Callback,OnClickListener
{
	ImageButton capture,retake,ok;
	SurfaceView suf;
	Camera cam;
	String filename;
	String path;
	
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
		capture.setOnClickListener(this);



		retake.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				retake.setVisibility(View.INVISIBLE);
				ok.setVisibility(View.INVISIBLE);
				capture.setVisibility(View.VISIBLE);
				
				releaseCamera();
				cam=Camera.open();
				try {
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
				Log.e("hello00000", filename);
				setResult(6, i);
				finish();
			}
		});
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

		myStartPreview();
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		cam=Camera.open();
		try {
			cam.setDisplayOrientation(90);
			cam.setPreviewDisplay(hold);
			//cam.startPreview();


		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}


	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub

		//		cam.stopPreview();
		//		cam.release();
		//		cam=null;
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		retake.setVisibility(View.VISIBLE);
		ok.setVisibility(View.VISIBLE);
		capture.setVisibility(View.INVISIBLE);

		cam.takePicture(null,null,jpegCallback);

	}

	/** The jpeg callback. */
    PictureCallback jpegCallback = new PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            FileOutputStream outStream = null;
            try {
                File miDirs = new File(
                        Environment.getExternalStorageDirectory() + "/Retails");
                if (!miDirs.exists())
                    miDirs.mkdirs();

                final Calendar c = Calendar.getInstance();
                String new_Date = c.get(Calendar.DAY_OF_MONTH) + "-"
                        + ((c.get(Calendar.MONTH)) + 1) + "-"
                        + c.get(Calendar.YEAR) + " " + c.get(Calendar.HOUR)
                        + "-" + c.get(Calendar.MINUTE) + "-"
                        + c.get(Calendar.SECOND);

                filename = String.format(
                        Environment.getExternalStorageDirectory() + "/Retails"
                                + "/%s.jpg", "(" + new_Date + ")");
                
                File file = new File(filename);
                
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                
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
               
//                showImageView.setVisibility(View.GONE);
//				showImageView.setImageBitmap(bitmap);
				cam.stopPreview();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
            }
        }

    };
	
    
	
	

	public void myStartPreview() {
		if ( (cam != null)) {
			cam.startPreview();

		}
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		hold=suf.getHolder();
		hold.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		hold.addCallback(this);

	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub

		super.onPause();
		releaseCamera();

	}
	private void releaseCamera(){

		if(cam!=null){
			cam.stopPreview(); 
			cam.setPreviewCallback(null);
			cam.release();        // release the camera for other applications
			cam = null;
		}

	}
	
	
	

}

