///*
// * Copyright (C) 2014 Chance Guild, Inc
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// * http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package com.example.client;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.HashMap;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//
//public class DatabaseHandler extends SQLiteOpenHelper {
//	 
//    // All Static variables
//    // Database Version
//    private static final int DATABASE_VERSION = 2;
// 
// 
//    // Feeds table name
//    private static final String TABLE_NAME = "feeds";
//    
// 
// // feeds table name
//    private static final String TABLE_NAME_RESUMEUPLOAD = "resume";
// 
//    // feeds Table Columns names
//    private static final String TAG_ID = "id";
//    
// 
//    public DatabaseHandler(Context context) {
//        super(context, AppConstants.DATABASE_NAME, null, DATABASE_VERSION);
//    }
// 
//    
//    
//    // Creating Tables
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        String CREATE_FEEDS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
//                + TAG_ID + " INTEGER PRIMARY KEY," + AppConstants.TAG_TOPIC + " TEXT,"
//                + AppConstants.TAG_INFLUENCER_TWITTER_ID +" TEXT," + AppConstants.TAG_IFN + " TEXT,"+ AppConstants.TAG_FOLLOWER_TWITTER_ID + " TEXT,"+ AppConstants.TAG_FFN + " TEXT,"+ AppConstants.TAG_CHANCE_ID + " TEXT,"+ AppConstants.TAG_LIKED + " TEXT,"+ AppConstants.TAG_LIKERS + " TEXT,"+ AppConstants.TAG_SHARED + " TEXT,"+ AppConstants.TAG_SHARERS + " TEXT,"
//                + AppConstants.TAG_LASTMODIFIED + " TEXT,"+ AppConstants.TAG_URLFTID  + " TEXT,"+ AppConstants.TAG_URLITID + " TEXT,"+ AppConstants.TAG_FOLLOWERSCREENNAME+ " TEXT,"+ AppConstants.TAG_INFLUENCERSCREENNAME + " TEXT,"+AppConstants.TAG_VIDEOURLONE + " TEXT,"+AppConstants.TAG_VIDEOURLTWO + " TEXT,"+AppConstants.TAG_DURATIONONE + " TEXT,"+AppConstants.TAG_DURATIONTWO + " TEXT,"+ AppConstants.TAG_TWEETID + " TEXT,"+ AppConstants.TAG_UNTWEETID + " TEXT"+")";
//        
//        /*
//         * Here i am using AppConstants.TAG_DURATIONTWO,AppConstants.TAG_VIDEOURLTWO as flag,(request or reply) respectively for resume table used for resuming the failed to uplaod videos
//         */
//        String CREATE_RESUME_TABLE = "CREATE TABLE " + TABLE_NAME_RESUMEUPLOAD + "("
//                + TAG_ID + " INTEGER PRIMARY KEY," + AppConstants.TAG_TOPIC + " TEXT,"
//                + AppConstants.TAG_CHANCE_ID + " TEXT,"
//                + AppConstants.TAG_INFLUENCERSCREENNAME + " TEXT,"+AppConstants.TAG_VIDEOURLONE + " TEXT," +AppConstants.TAG_DURATIONONE + " TEXT,"+AppConstants.TAG_DURATIONTWO + " TEXT,"+AppConstants.TAG_VIDEOURLTWO + " TEXT"+")";
//        db.execSQL(CREATE_FEEDS_TABLE);
//        db.execSQL(CREATE_RESUME_TABLE);
//    }
// 
//    // Upgrading database
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        // Drop older table if existed
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_RESUMEUPLOAD);
//        // Create tables again
//        onCreate(db);
//    }
//    
//    
//    
//    // Adding new feeds
//    void addFeeds(ArrayList<HashMap<String, String>> tmp) {
//        SQLiteDatabase db = this.getWritableDatabase();
//      
//        for(int i=0;i<(tmp.size()>10?10:tmp.size());i++)
//        {
//        ContentValues values = new ContentValues();
//        values.put(AppConstants.TAG_TOPIC, tmp.get(i).get(AppConstants.TAG_TOPIC)); 
//        values.put(AppConstants.TAG_INFLUENCER_TWITTER_ID, tmp.get(i).get(AppConstants.TAG_INFLUENCER_TWITTER_ID)); 
//        values.put(AppConstants.TAG_IFN, tmp.get(i).get(AppConstants.TAG_IFN)); 
//        values.put(AppConstants.TAG_FOLLOWER_TWITTER_ID, tmp.get(i).get(AppConstants.TAG_FOLLOWER_TWITTER_ID)); 
//        values.put(AppConstants.TAG_FFN, tmp.get(i).get(AppConstants.TAG_FFN)); 
//        values.put(AppConstants.TAG_CHANCE_ID, tmp.get(i).get(AppConstants.TAG_CHANCE_ID)); 
//        values.put(AppConstants.TAG_LIKED, tmp.get(i).get(AppConstants.TAG_LIKED)); 
//        values.put(AppConstants.TAG_LIKERS , tmp.get(i).get(AppConstants.TAG_LIKERS)); 
//        values.put(AppConstants.TAG_SHARED, tmp.get(i).get(AppConstants.TAG_SHARED)); 
//        values.put(AppConstants.TAG_SHARERS, tmp.get(i).get(AppConstants.TAG_SHARERS));
//        values.put(AppConstants.TAG_LASTMODIFIED , tmp.get(i).get(AppConstants.TAG_LASTMODIFIED)); 
//        values.put(AppConstants.TAG_URLFTID, tmp.get(i).get(AppConstants.TAG_URLFTID)); 
//        values.put(AppConstants.TAG_URLITID, tmp.get(i).get(AppConstants.TAG_URLITID));
//        values.put(AppConstants.TAG_FOLLOWERSCREENNAME, tmp.get(i).get(AppConstants.TAG_FOLLOWERSCREENNAME)); 
//        values.put(AppConstants.TAG_INFLUENCERSCREENNAME, tmp.get(i).get(AppConstants.TAG_INFLUENCERSCREENNAME));
//        values.put(AppConstants.TAG_VIDEOURLONE, tmp.get(i).get(AppConstants.TAG_VIDEOURLONE)); 
//        values.put(AppConstants.TAG_VIDEOURLTWO, tmp.get(i).get(AppConstants.TAG_VIDEOURLTWO));
//        values.put(AppConstants.TAG_DURATIONONE, tmp.get(i).get(AppConstants.TAG_DURATIONONE)); 
//        values.put(AppConstants.TAG_DURATIONTWO, tmp.get(i).get(AppConstants.TAG_DURATIONTWO));
//        values.put(AppConstants.TAG_TWEETID, tmp.get(i).get(AppConstants.TAG_TWEETID));
//        values.put(AppConstants.TAG_UNTWEETID, tmp.get(i).get(AppConstants.TAG_UNTWEETID));
//        
//     // Inserting Row
//        db.insert(TABLE_NAME, null, values);
//        
//        }
// 
//        
//        db.close(); // Closing database connection
//    }
//   
//    //Add the failed uplaod details
//    void addresumedetails(String path,String influencertwittername,String duration,String topic,String flag,String chanceid,String identifier)
//    {
//    	SQLiteDatabase db = this.getWritableDatabase();
//        
//        ContentValues values = new ContentValues();
//        values.put(AppConstants.TAG_VIDEOURLONE, path); 
//        values.put(AppConstants.TAG_INFLUENCERSCREENNAME, influencertwittername);
//        values.put(AppConstants.TAG_DURATIONONE, duration); 
//        values.put(AppConstants.TAG_TOPIC, topic);
//        values.put(AppConstants.TAG_DURATIONTWO, flag);
//        values.put(AppConstants.TAG_CHANCE_ID, chanceid); 
//        values.put(AppConstants.TAG_VIDEOURLTWO, identifier); 
//               
//     // Inserting Row
//        db.insert(TABLE_NAME_RESUMEUPLOAD, null, values);
//        
//       db.close(); // Closing database connection
//    }
//    
//    public Boolean updateEtiquette(String key1,String value1,int position,String key2,String value2) {
//        SQLiteDatabase db = this.getWritableDatabase();
//    	position=position+1;
//        ContentValues args = new ContentValues();
//        args.put(key1, value1);   
//        args.put(key2, value2);
//      return  db.update(TABLE_NAME, args,TAG_ID+ "=?",new String[]{position+""})>0;
//              
//    		     
//    }
//    
//    public Boolean updateuntweetid(String key1,String value1,int position) {
//        SQLiteDatabase db = this.getWritableDatabase();
//    	position=position+1;
//        ContentValues args = new ContentValues();
//        args.put(key1, value1);   
//      return  db.update(TABLE_NAME, args,TAG_ID+ "=?",new String[]{position+""})>0;
//              
//    		    
//    }
//
//    // Getting All feedss
//    public ArrayList<HashMap<String, String>> getAllfeeds() {
//    	ArrayList<HashMap<String, String>> feeds = new ArrayList<HashMap<String, String>>();
//        // Select All Query
//        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " ORDER BY " + AppConstants.TAG_LASTMODIFIED+ " DESC" ;
// 
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//        try
//        {
//        // looping through all rows and adding to list
//        if (cursor.moveToFirst()) {
//            do {
//                HashMap<String, String> map=new HashMap<String, String>();
//                map.put(AppConstants.TAG_TOPIC, cursor.getString(1)); 
//                map.put(AppConstants.TAG_INFLUENCER_TWITTER_ID, cursor.getString(2)); 
//                map.put(AppConstants.TAG_IFN, cursor.getString(3)); 
//                map.put(AppConstants.TAG_FOLLOWER_TWITTER_ID, cursor.getString(4)); 
//                map.put(AppConstants.TAG_FFN, cursor.getString(5)); 
//                map.put(AppConstants.TAG_CHANCE_ID, cursor.getString(6)); 
//                map.put(AppConstants.TAG_LIKED, cursor.getString(7)); 
//                map.put(AppConstants.TAG_LIKERS , cursor.getString(8)); 
//                map.put(AppConstants.TAG_SHARED, cursor.getString(9)); 
//                map.put(AppConstants.TAG_SHARERS, cursor.getString(10));
//                map.put(AppConstants.TAG_LASTMODIFIED , cursor.getString(11)); 
//                map.put(AppConstants.TAG_URLFTID, cursor.getString(12)); 
//                map.put(AppConstants.TAG_URLITID, cursor.getString(13));
//                map.put(AppConstants.TAG_FOLLOWERSCREENNAME, cursor.getString(14)); 
//                map.put(AppConstants.TAG_INFLUENCERSCREENNAME, cursor.getString(15));
//                map.put(AppConstants.TAG_VIDEOURLONE , cursor.getString(16)); 
//                map.put(AppConstants.TAG_VIDEOURLTWO, cursor.getString(17)); 
//                map.put(AppConstants.TAG_DURATIONONE, cursor.getString(18));
//                map.put(AppConstants.TAG_DURATIONTWO, cursor.getString(19)); 
//                map.put(AppConstants.TAG_TWEETID, cursor.getString(20));
//                map.put(AppConstants.TAG_UNTWEETID, cursor.getString(21));
//                
//               
//                
//                // Adding feeds to list
//                feeds.add(map);
//            } while (cursor.moveToNext());
//        }
//        }
//        finally {
//            cursor.close();
//            db.close();
//        }
//        
//        // return feeds list
//        return feeds;
//    }
//
//    
//    // Getting All failed uploads
//    public ArrayList<HashMap<String, String>> getAllfaileduploads() {
//    	ArrayList<HashMap<String, String>> feeds = new ArrayList<HashMap<String, String>>();
//        // Select All Query
//        String selectQuery = "SELECT  * FROM " + TABLE_NAME_RESUMEUPLOAD ;
// 
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//        System.out.println("Size of faileduplads "+cursor.getCount());
//        try
//        {
//        // looping through all rows and adding to list
//        if (cursor.moveToFirst()) {
//            do {
//            	System.out.println("Curser position starting  "+cursor.getPosition());
//            	File file =new File(cursor.getString(4));
//            	if(file.exists())
//            	{
//                HashMap<String, String> map=new HashMap<String, String>();
//                map.put(AppConstants.TAG_VIDEOURLONE, cursor.getString(4)); 
//                map.put(AppConstants.TAG_INFLUENCERSCREENNAME, cursor.getString(3));
//                map.put(AppConstants.TAG_DURATIONONE, cursor.getString(5)); 
//                map.put(AppConstants.TAG_TOPIC, cursor.getString(1));
//                map.put(AppConstants.TAG_DURATIONTWO, cursor.getString(6));
//                map.put(AppConstants.TAG_CHANCE_ID, cursor.getString(2)); 
//                map.put(AppConstants.TAG_VIDEOURLTWO, cursor.getString(7)); 
//                
//                // Adding feeds to list
//                feeds.add(map);
//            	}
//            	else
//            	{
//            		deleteresumedetails(cursor.getPosition());
//            	}
//            	
//            } while (cursor.moveToNext());
//        }
// 
//        }
//        finally {
//            cursor.close();
//            db.close();
//        }
//        // return feeds list
//        return feeds;
//    }
//    
//    
// // Deleting single upoad
//    public void deleteresumedetails(int position) {
//    	
//    	position=position+1;
//        SQLiteDatabase db = this.getWritableDatabase();
//        System.out.println("Detete result "+position+db.delete(TABLE_NAME_RESUMEUPLOAD, TAG_ID + " = ?",
//        		new String[]{position+""}));
//        
//        db.close();
//    }
//}