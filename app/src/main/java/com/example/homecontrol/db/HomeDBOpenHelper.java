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

	/*  ZONE TABLE COLUMN NAMES */
	public final static String COLUMN_ZONE_NAME = "zone";
    public final static String COLUMN_RESOURCEID = "resourceId";
	
	/* COMPONENETS TABLE COLUMN NAMES */
    public final static String COLUMN_COMP_ZONE_NAME = "czone"; // references the associated zone
	public final static String COLUMN_COMP_NAME = "component";
	public final static String COLUMN_IP = "ip";
	
	private static final String CREATE_ZONES_TABLE = "CREATE TABLE " + TABLE_ZONES + " (" +
			COLUMN_ZONE_NAME + " STRING, " + 
			COLUMN_RESOURCEID + " INTEGER, " +
            "PRIMARY KEY (" + COLUMN_ZONE_NAME + "))";
	private static final String CREATE_COMPONENTS_TABLE = "CREATE TABLE " + TABLE_COMPONENTS + " (" +
            COLUMN_COMP_ZONE_NAME + " STRING, " +
			COLUMN_COMP_NAME + " STRING, " + 
			COLUMN_IP + "STRING, " +
            "PRIMARY KEY (" + COLUMN_COMP_ZONE_NAME + ", " + COLUMN_IP + "), " +
            "FOREIGN KEY (" + COLUMN_COMP_ZONE_NAME + ") REFERENCES " + TABLE_ZONES + " (" + COLUMN_ZONE_NAME + ") " +
                "ON DELETE CASCADE )";

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
