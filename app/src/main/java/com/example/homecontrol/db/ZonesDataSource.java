package com.example.homecontrol.db;

import com.example.homecontrol.model.Zone;
import com.example.homecontrol.model.ZoneList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ZonesDataSource {
	private static final String LOGTAG = "HOMECONTROL";
	
	/*private static final String[] allColumns = new String{
		HomeDBOpenHelper.
	}*/
    
	SQLiteOpenHelper dbhelper;
	SQLiteDatabase database;
	
	public ZonesDataSource(Context context){
		// create database and tables if they don't exist
		dbhelper = new HomeDBOpenHelper(context);
	}
	
	public void open(){
		//open the database
		database = dbhelper.getWritableDatabase();
        Log.i(LOGTAG, "(ZonesDataSource) open() - Database opened.");
	}
	
	public void close(){
		Log.i(LOGTAG, "(ZonesDataSource) close() - Database closed.");
		dbhelper.close();
	}
	
	/*
	 * Add a new Zone to the databas with unique
	 * ID given through SQL
	 */
	public void addZone(Zone z){		

		ContentValues cv = new ContentValues();
		cv.put(HomeDBOpenHelper.COLUMN_ZONE_NAME, z.getName());
		cv.put(HomeDBOpenHelper.COLUMN_RESOURCEID, z.getImgResId());

		/* db.insert returns -1 if there was an error.
		 * We'll check this in the activity
		 */

		long insertId = database.insert(HomeDBOpenHelper.TABLE_ZONES, null, cv);
		 
		//set the new id to the corresponding zone

		Log.i(LOGTAG, "Zone inserted");

	}
	
	/*
	 * Get a list of all zones and return to calling fnc
	 */
	public ZoneList getAllZones(){
		ZoneList list = new ZoneList();
		
		// get all entries from zone table
		String query = "SELECT * FROM " + HomeDBOpenHelper.TABLE_ZONES;
		Cursor cursor = database.rawQuery(query, null);
		Log.i(LOGTAG, "(ZonesDataSource) getAllZones() - getting records..");
		/* populate "list" with all the zones from the 
		 * database.  Return this to the caller
		 */
		cursor.moveToFirst();
		while (!cursor.isAfterLast()){
			String name = cursor.getString(0);
			int imgResId = cursor.getInt(1);
			Zone z = new Zone(name, imgResId);
			list.add(z);
			cursor.moveToNext();
		}
		
		if ((cursor != null) && (!cursor.isClosed()))
			cursor.close();
		
		return list;
	}
	
	public boolean removeZone(Zone z){
		boolean result = false;
		
		/*String query = "SELECT * FROM " + HomeDBOpenHelper.TABLE_ZONES + " WHERE " +
				HomeDBOpenHelper.COLUMN_ID + " = \"" + 
				String.valueOf(z.getId()) + "\"";
		
		Cursor cursor = database.rawQuery(query, null);
		if (cursor.moveToFirst()){
			//database.delete(HomeDBOpenHelper.TABLE_ZONES, HomeDBOpenHelper.COLUMN_ID + " = ?",
			//		new String[] {String.valueOf(z.getId())});
			*/
            String qdelete = "DELETE " +
                    "FROM " + HomeDBOpenHelper.TABLE_ZONES + " " +
                    "WHERE " + HomeDBOpenHelper.COLUMN_ZONE_NAME + " = \"" + z.getName() + "\"";

            database.execSQL(qdelete);
			
			Log.i(LOGTAG, "(ZonesDataSource) removeZone() - Zone \"" + z.getName() + "\" deleted!");
			//cursor.close();
			result = true;
		//}
		
		return result;
	}
	
	
	/*
	 * USED FOR DEBUGGING ONLY!
	 * REMOVE BEFORE FINAL IMPLEMENTATION
	 */
	public void deleteDB(Context c){
		c.deleteDatabase(HomeDBOpenHelper.DATABASE_NAME);
	}
	
}
