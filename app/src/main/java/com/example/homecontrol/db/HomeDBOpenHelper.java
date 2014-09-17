package com.example.homecontrol.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HomeDBOpenHelper extends SQLiteOpenHelper {
	//private final static String LOGTAG = "HOMECONTROL";

	public final static String DATABASE_NAME = "homeControlDB.db";
	public final static String TABLE_ZONES = "zones";
	public final static String TABLE_COMPONENTS = "components";
	private final static int DATABASE_VERSION = 1; 
	
	/* COMMON COLUMN NAMES */
	public final static String COLUMN_ID = "_id";
	public final static String COLUMN_RESOURCEID = "resourceId";
	
	/*  ZONE TABLE COLUMN NAMES */
	public final static String COLUMN_ZONE_NAME = "zoneName";
	
	/* COMPONENETS TABLE COLUMN NAMES */
	public final static String COLUMN_COMP_NAME = "compName";
	public final static String COLUMN_ZONE_ID = "zoneId";
	
	private static final String CREATE_ZONES_TABLE = "CREATE TABLE " + TABLE_ZONES + " (" + 
			COLUMN_ID + " INTEGER PRIMARY KEY, " + 
			COLUMN_ZONE_NAME + " STRING, " + 
			COLUMN_RESOURCEID + " INTEGER)"; 
	private static final String CREATE_COMPONENTS_TABLE = "CREATE TABLE " + TABLE_COMPONENTS + " (" +
			COLUMN_ID + " INTEGER PRIMARY KEY, " + 
			COLUMN_ZONE_ID + " INTEGER, " +
			COLUMN_COMP_NAME + " STRING, " + 
			COLUMN_RESOURCEID + " INTEGER)";

	/* constructor */
	public HomeDBOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_ZONES_TABLE);
		db.execSQL(CREATE_COMPONENTS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ZONES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPONENTS);
		onCreate(db);
	}

}
