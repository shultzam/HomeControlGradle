package com.example.homecontrol;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.homecontrol.db.ZonesDataSource;
import com.example.homecontrol.model.Zone;
import com.example.homecontrol.model.ZoneAdapter;
import com.example.homecontrol.model.ZoneList;

public class FragZones extends Fragment {
	private static final String LOGTAG = "HOMECONTROL";
	
	public static final String SAVED_ZONE_LIST = "homecontrol.zones";
	private static final int REQ_ZONE_NAME = 0;
	
	ListView lvZones;
	ImageButton ibtnAddZone, ibtnRemoveZone;
	ZoneList listZones;  // list of all current zones
	ZoneAdapter zoneAdapter;  // adapter for lvZones to display icons and text
	
	ZonesDataSource zoneSource;
	
	/*
	 * FOR TESTING - REMOVE BEFORE FINAL IMPLEMENTATION
	 */
	Button btnMoveSd, btnDeleteDB;
	/*
	 * END OF TESTING
	 */
	
	/* variable for deleting entries from listZones */
	Zone selZone = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedState) {
		//setRetainInstance(true);
		View view = inflater.inflate(R.layout.frag_zones, container, false);
		
		ibtnAddZone = (ImageButton) view.findViewById(R.id.ibtnAdd);
		ibtnRemoveZone = (ImageButton) view.findViewById(R.id.ibtnRemove);
		lvZones = (ListView) view.findViewById(R.id.lvZones);
		listZones = new ZoneList();
		
		zoneSource = new ZonesDataSource(getActivity());
		zoneSource.open();
		
		/*
		 * FOR TESTING - REMOVE BEFORE FINAL IMPLEMENTATION
		 */
		btnMoveSd = (Button) view.findViewById(R.id.btnMoveSD);
		btnDeleteDB = (Button) view.findViewById(R.id.btnDeleteDB);
		btnMoveSd.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					moveDBtoSD();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnDeleteDB.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				deleteDB();
			}
		});
		/*
		 * END OF TESTING
		 */
		
		// set ListView adapter
		zoneAdapter = new ZoneAdapter(getActivity(), listZones);
		lvZones.setAdapter(zoneAdapter);
				
		// restore data from last state
		if ( savedState != null){
			ZoneList tempList = new ZoneList((ZoneList) savedState.getParcelable("zones"));
			listZones.clear();
			zoneAdapter.notifyDataSetChanged();
			for (int i = 0; i < tempList.size(); i++){
				listZones.add(tempList.get(i));
				zoneAdapter.notifyDataSetChanged();
			}
		} else {
			populateFromDB();
		}
		
		/*
		 *  On click listeners
		 */
		lvZones.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){
				// for now this variable is used for removeZone() only
				selZone = listZones.get(position);
			}
		});
		
		ibtnAddZone.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				addZone();
			}
		});
		
		ibtnRemoveZone.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				removeZone();
			}
		});
		return view;
	}

	
	@Override
	public void onPause() {
		super.onPause();
		Log.i(LOGTAG, "(FragZones) onPause() - database closed.");
		zoneSource.close();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		Log.i(LOGTAG, "(FragZones) onResume() - database opened.");
		zoneSource.open();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {

        outState.putParcelable("zones", listZones);
	}
	
	/*
	 * Implemented to recieve the new Zone name from a 
	 * custom dialog.
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode != Activity.RESULT_OK) return;
		
		if (requestCode == REQ_ZONE_NAME){
					
			String zoneName = data.getSerializableExtra(AddZoneDialog.EXTRA_ZONE_NAME).toString();
			//int imgResId = (int) data.getSerializableExtra(AddZoneDialog.EXTRA_ZONE_ICON);
            int imgResId = Integer.parseInt((String)data.getSerializableExtra(AddZoneDialog.EXTRA_ZONE_ICON));
			
			Zone z = new Zone(zoneName, imgResId);
			// add zone to database...
			zoneSource.addZone(z);
			// ... and to the list
			listZones.add(z);
			// notify adapter to populate ListView
			zoneAdapter.notifyDataSetChanged();
		}
	}

	/*
	 * Functions to manipulate the currently stored zones
	 */
	public void addZone(){
		
		FragmentManager fm = getActivity().getSupportFragmentManager();
		AddZoneDialog dialog = new AddZoneDialog().newInstance(listZones);
		dialog.setTargetFragment(FragZones.this, REQ_ZONE_NAME);
		dialog.show(fm, "addzone");
	} 
	
	public void removeZone(){
		if (selZone != null){
			listZones.remove(selZone);
			zoneAdapter.notifyDataSetChanged();
			
			// remove from database
			zoneSource.removeZone(selZone);
			
			selZone = null;
		} else
			Toast.makeText(getActivity(), "Select zone to remove", Toast.LENGTH_SHORT).show();
	}
	
	
	/*
	 * Interract with database
	 */
	public void populateFromDB(){
		// make sure listZones is clear before populating
		listZones.clear();
		
		/* get the list of zones from our database
		 * and add them to our display
		 */
		ZoneList tempList = new ZoneList(zoneSource.getAllZones());
		for (Zone z : tempList){
			listZones.add(z);
			zoneAdapter.notifyDataSetChanged();
		}
		
	}
	
	/*
	 * FOR TESTING - REMOVE BEFORE FINAL IMPLEMENTATION
	 */
	public void moveDBtoSD() throws IOException{
		InputStream myInput = null;
		OutputStream myOutput = null;
		Log.e("*INPUT**", "** Getting input file **");
		myInput = new FileInputStream("/data/data/com.example.homecontrol/databases/homeControlDB.db");
		Log.e("*INPUT**", "** Got input file **");
		File dir = new File("/sdcard/homecontrol/");
		//if (!dir.exists())
			dir.mkdirs();
		Log.e("*DIR**", "** Made Dir **");
		
		File newFile = new File(dir.getPath(),"backup.db");
		if (!newFile.exists())
			newFile.createNewFile(); 
		Log.e("*CHECK FILE**", "** Made File **");
		myOutput = new FileOutputStream(newFile);
		Log.e("*OUTPUT**", "** Got output file **");
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer))> 0)
			myOutput.write(buffer, 0 , length);
		Log.e("*WRITING**", "** Wrote file **");
		
		myOutput.flush();
		myOutput.close();
		myInput.close();
		
		Toast.makeText(getActivity(), "DB Moved!", Toast.LENGTH_SHORT).show();
	}
	public void deleteDB(){
		Context c = getActivity();
		zoneSource.deleteDB(c);
		listZones.clear();
		zoneAdapter.notifyDataSetChanged();
	}
	/*
	 * END OF TESTING
	 */

}
