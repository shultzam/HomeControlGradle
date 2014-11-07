package com.example.homecontrol.model;

/**
 * Created by James on 11/5/2014.
 */
public class Component {
    private String _type;
    private String _zone;
    private String _ip;

    /**
     * Empty Constructor
     */
    public Component(){}

    /**
     * Initialization constructor.
     * @param type  Type of component: AC_LIGHT, DC_LED_LIGHT, etc.
     * @param zone  Zone the component belongs to.
     * @param ip    IP address of the component.
     */
    public Component(String type, String zone, String ip){
        this._type = type;
        this._zone = zone;
        this._ip = ip;
    }

    /*
        Setter methods
     */
    public void setType(String type){
        this._type = type;
    }

    public void setZone(String zone){
        this._zone = zone;
    }

    public void setIP(String ip){
        this._ip = ip;
    }

    /*
        Getter methods
     */
    public String getType(){
        return this._type;
    }

    public String getZone(){
        return this._zone;
    }

    public String getIP(){
        return this._ip;
    }
}
