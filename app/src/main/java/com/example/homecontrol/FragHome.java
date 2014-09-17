package com.example.homecontrol;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.TextView;

import com.example.homecontrol.db.ZonesDataSource;
import com.example.homecontrol.model.HomeZoneIconAdapter;
import com.example.homecontrol.model.Zone;
import com.example.homecontrol.model.ZoneList;

public class FragHome extends Fragment {
	private final static String LOGTAG = "HOMECONTROL";

	ZoneList listZones;
	TextView tvNumZones;
	GridView gvZones;
	
	CheckBox cbAllZones;

	HomeZoneIconAdapter iconAdapter;
	
	ZonesDataSource zoneSource;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedState) {
		
		View view = inflater.inflate(R.layout.frag_home, container, false);
		
		// get zone database
		zoneSource = new ZonesDataSource(getActivity());
		//zoneSource.open();
		
		tvNumZones = (TextView) view.findViewById(R.id.tvZoneStatus);
		gvZones = (GridView) view.findViewById(R.id.gvZones);
		cbAllZones = (CheckBox) view.findViewById(R.id.cbAll);
	
		if (savedState != null){
			boolean checked = savedState.getBoolean("allZones");
			Log.d(LOGTAG, "(RestoreState) ALL ZONES:" + checked);
			// comment there
			cbAllZones.setChecked(checked);
			Log.d(LOGTAG, "(RestoreState) setChecked:" + savedState.getBoolean("allZones"));
			
		}
		
		listZones = new ZoneList();
	
		iconAdapter = new HomeZoneIconAdapter(getActivity(), listZones);
		gvZones.setAdapter(iconAdapter);
		
		// get all zones from database
		populateZonesFromDB();
		
		gvZones.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				
			}
		});
		
		cbAllZones.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (listZones.size() > 0){
					// if we check "all zones" icon, we disable selection of
					// individual zones in the grid view and vice versa
					if (isChecked){
						for (int i = 0; i < listZones.size(); i++){
							View v = gvZones.getChildAt(i);
							v.findViewById(R.id.cbZone).setEnabled(false);
							v.findViewById(R.id.tvZoneName).setEnabled(false);
						}
					} else {
						for (int i = 0; i < listZones.size(); i++){
							View v = gvZones.getChildAt(i);
							v.findViewById(R.id.cbZone).setEnabled(true);
							v.findViewById(R.id.tvZoneName).setEnabled(true);
						}
					}
				}
			}
		});

		// get dimensions of the grid view pictures from resource file
		// and set the column width accordingly
		Resources res = getActivity().getResources();
		int picSize = (int) res.getDimension(R.dimen.gridstd_imageview_size);
		gvZones.setColumnWidth(picSize);
		
		return view;
	}
		
	public int populateZonesFromDB(){
		zoneSource.open();
		
		if (listZones == null)
			listZones = new ZoneList();
	
		listZones.clear();
		ZoneList templist = new ZoneList(zoneSource.getAllZones());

		for (Zone z: templist){
			listZones.add(z);
			iconAdapter.notifyDataSetChanged();
		}
		
		if (listZones.size() == 1)
			tvNumZones.setText("You have 1 zone");
		else if (listZones.size() > 0)
			tvNumZones.setText("You have " + listZones.size() + " zones.");
		else
			tvNumZones.setText("You currently have no active zones.  Go to the \"Zones\" tab to add zones.");
		
		zoneSource.close();
		return listZones.size();
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Log.d(LOGTAG, "(SaveState) ALL ZONES: " + cbAllZones.isChecked());
		outState.putBoolean("allZones", cbAllZones.isChecked());
	
	}

}
