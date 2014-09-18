/*
 * Copyright (C) 2014 Chance Guild, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.client;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
	 
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
 
    // Feeds table name
    private static final String TABLE_NAME = "transactions";
    
 
 
 
    // feeds Table Columns names
    private static final String TAG_ID = "id";
    
 
    public DatabaseHandler(Context context) {
        super(context, AppConstants.DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    
    
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TRANSACTION_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + TAG_ID + " INTEGER PRIMARY KEY," + AppConstants.MASTER + " TEXT,"
                + AppConstants.PHOTO_DETAILS +" TEXT,"+ AppConstants.CHECKLIST +" TEXT," + AppConstants.IMAGES + 
                " TEXT"+")";
        
       
        db.execSQL(CREATE_TRANSACTION_TABLE);
        
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
      
        // Create tables again
        onCreate(db);
    }
    
    
    
    // Adding new feeds
    void addFeeds(String master, String photod, String image, String checklist) {
        SQLiteDatabase db = this.getWritableDatabase();
      
        
        ContentValues values = new ContentValues();
        values.put(AppConstants.MASTER, master); 
        values.put(AppConstants.PHOTO_DETAILS, photod); 
        values.put(AppConstants.IMAGES, image); 
        values.put(AppConstants.CHECKLIST, checklist); 
        
        
     // Inserting Row
        db.insert(TABLE_NAME, null, values);
        
        
        db.close(); // Closing database connection
    }
   
   
    
    
    
   

    
    // Getting All failed uploads
    public ArrayList<HashMap<String, String>> getAllTransactions() {
    	ArrayList<HashMap<String, String>> feeds = new ArrayList<HashMap<String, String>>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME ;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        System.out.println("Size of faileduplads "+cursor.getCount());
        try
        {
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	System.out.println("Curser position starting  "+cursor.getPosition());
            	
            	String images = cursor.getString(4);
            	String[] Array = images.split(",");
            	
            	int flag=0;
            	
            	for(int i=0;i<Array.length;i++){
            	   File file =new File(Array[i]);
            	   
            	   if(!file.exists()){
            		   deleteresumedetails(cursor.getPosition());
            		   flag=1;
            		   break;
            	   }
            	}
            	
            	
            	if(flag==0)
            	{
                HashMap<String, String> map=new HashMap<String, String>();
                map.put(AppConstants.MASTER, cursor.getString(1)); 
                map.put(AppConstants.PHOTO_DETAILS, cursor.getString(2));
                map.put(AppConstants.CHECKLIST, cursor.getString(3)); 
                map.put(AppConstants.IMAGES, cursor.getString(4));
                
                
                // Adding feeds to list
                feeds.add(map);
            	}
            	
            	
            } while (cursor.moveToNext());
        }
 
        }
        finally {
            cursor.close();
            db.close();
        }
        // return feeds list
        return feeds;
    }
    
    
 // Deleting single upoad
    public void deleteresumedetails(int position) {
    	
    	position=position+1;
        SQLiteDatabase db = this.getWritableDatabase();
        System.out.println("Detete result "+position+db.delete(TABLE_NAME, TAG_ID + " = ?",
        		new String[]{position+""}));
        
        db.close();
    }
}