package com.example.homecontrol;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.example.homecontrol.model.ZoneIconAdapter;
import com.example.homecontrol.model.ZoneList;

public class AddZoneDialog extends DialogFragment {
	private static final String LOGTAG = "HOMECONTROL";
	
	public static final String EXTRA_ZONE_NAME = "homecontrol.zonename";
	public static final String EXTRA_NAME_LIST = "homecontrol.namelist";
	public static final String EXTRA_IMG_LIST = "homecontrol.imglist";
	public static final String EXTRA_ZONE_ICON = "homecontrol.zoneicon";
	
	private ZoneList zoneList;	
	private EditText etZoneName;
	private GridView gvIcons;
	private TypedArray ids;
	private int selectedIcon;
	
	/*
	 *  Constructor for dialog fragment that receives the list of zone names from
	 *  FragZones fragment
	 */
	public AddZoneDialog newInstance(ZoneList zones){
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_NAME_LIST, zones);
		
		AddZoneDialog newDialog = new AddZoneDialog();
		newDialog.setArguments(args);
		
		return newDialog;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedState) {
		View v = getActivity().getLayoutInflater()
				.inflate(R.layout.frag_add_zone_dialog, null);
		
		zoneList =  (ZoneList) getArguments().getSerializable(EXTRA_NAME_LIST);
		gvIcons = (GridView) v.findViewById(R.id.gvZonesIcons);
		etZoneName = (EditText) v.findViewById(R.id.etZonesPopupName);
		etZoneName.requestFocus();
		
		//	populate image id's from valuse array "zone_icons" 
		ids = getActivity().getResources().obtainTypedArray(R.array.zone_icons);

		// configure GridView adapter
		ZoneIconAdapter iconAdapter = new ZoneIconAdapter(getActivity(), ids);
		
		// get width of images in gridview and set the column
		// width accordingly
		Resources res = getActivity().getResources();
		int cellWidth = (int) res.getDimension(R.dimen.grid_imageview_size);
		gvIcons.setColumnWidth(cellWidth);
		gvIcons.setAdapter(iconAdapter);
		iconAdapter.notifyDataSetChanged();
		
		// set GridView itemClickListener
		gvIcons.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				selectedIcon = position;
			}
			
		});
		
		return new AlertDialog.Builder(getActivity())
			.setView(v)
			.setTitle(R.string.zones_add_dialog_title)
			.setPositiveButton(R.string.ok, new AlertDialog.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					sendResult(Activity.RESULT_OK);
				}
			})
			.setNegativeButton(R.string.cancel, null)
			.create();
	}
	
	
	
	@Override
	public void onActivityCreated(Bundle bundle) {
		getDialog().getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		
		super.onActivityCreated(bundle);
	}

	public void sendResult(int resultCode){
		String newName = etZoneName.getText().toString();
		// gets resource id of the icon selected
		int iconResource = ids.getResourceId(selectedIcon, -1);
		
		if ((newName.length() < 1) || (newName == null) || (newName == "")){
			Toast.makeText(getActivity(), "You must enter a name.", Toast.LENGTH_SHORT).show();
			return;
		}
		else{
			if (nameOnList(newName)){
				Toast.makeText(getActivity(), "That name is already in use.  Please enter another name.",
						Toast.LENGTH_LONG).show();
				return;
			}
		}
		
		if(getTargetFragment() == null)
			return;
		
		Intent i = new Intent();
		i.putExtra(EXTRA_ZONE_NAME, newName);
		i.putExtra(EXTRA_ZONE_ICON, iconResource);
		
		getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
	}
	
	boolean nameOnList(String name){
		for (int i = 0; i < zoneList.size(); i++){
			if (zoneList.get(i).getName().equals(name))
				return true;
		}
		return false;
	}

	@Override
	public void onStop() {
		ids.recycle();
		super.onStop();
	}
}

