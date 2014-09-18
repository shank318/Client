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

import java.util.ArrayList;
import java.util.HashMap;

public class AppConstants {
	protected  ArrayList<HashMap<String, String>> installationPhotoTypeArray;
	protected  ArrayList<HashMap<String, String>> inspectionPhotoTypeArray;
	protected  ArrayList<HashMap<String, String>> checkListArray;
	protected  ArrayList<HashMap<String, String>> transactions;
	
	
	
   protected static final String KEY = "mypref";
	 // Database Name
    protected static final String DATABASE_NAME = "Chance_database";
    
    protected static final String MASTER ="master";
    protected static final String PHOTO_DETAILS ="pd";
    protected static final String CHECKLIST ="cl";
    protected static final String IMAGES = "images";
    
    
	private  AppConstants() {
		inspectionPhotoTypeArray=new ArrayList<HashMap<String,String>>();
		installationPhotoTypeArray=new ArrayList<HashMap<String,String>>();
		checkListArray=new ArrayList<HashMap<String, String>>();
		transactions = new ArrayList<HashMap<String,String>>();
		
		
	}
	
	private static AppConstants instance;

	  public static AppConstants getInstance() {
		  
	    if (instance == null) instance = new AppConstants();
	    return instance;
	  }
	 
}
