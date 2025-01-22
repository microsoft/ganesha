package com.ganesha.models;

import java.util.List;

public class LightModel {
    public LightModel() {
        this.id = 0;
        this.name = "";
        _isOn = false;
    }
    public LightModel(int id, String name, boolean isOn) {
        this.id = id;
        this.name = name;
        _isOn = isOn;
    }

    public int id;
    public String name;
    public List<String> tags;
    private boolean _isOn;

    public void setIsOn(boolean isOn) {
        _isOn = isOn;
    }

    public boolean isOn() {
        return _isOn;
    }
}
