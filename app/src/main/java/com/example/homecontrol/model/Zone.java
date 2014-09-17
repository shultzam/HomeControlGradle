package com.example.homecontrol.model;

import java.util.ArrayList;


public class Zone{

	private long _id;
	private String _name;	// name of zone
	private int _imgResId;	// resource id for icon
	ArrayList<WirelessComponent> _components;	// list of components associated with zone (i.e. lights)
	
	/* constructors */
	public Zone(){}
	
	public Zone(String name){
		this._name = name;
	}
	
	public Zone(String name, int imgResId){
		this._name = name;
		this._imgResId = imgResId;
	}
	
	public Zone(long id, String name, int imgResId){
		this._name = name;
		this._imgResId = imgResId;
		this._id = id;
	}
	
	/* Set Methods */
	public void setName(String name){
		this._name = name;
	}
	
	public void setImgResId(int imgResId){
		this._imgResId = imgResId;
	}
	
	public void setId(long id){
		this._id = id;
	}
	
	/* Get Methods */
	public String getName(){
		return this._name;
	}
	
	public int getImgResId(){
		return this._imgResId;
	}
	
	public long getId(){
		return this._id;
	}
	
}
