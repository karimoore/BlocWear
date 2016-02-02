package com.karimoore.android.common;

/**
 * Created by kari on 1/13/16.
 */
public class Weather {

    public String temperature;
    public String description;
    public int icon;
    public String wear;
    public int clothingIcon;

    public Weather() {
    }

    public Weather(String description, String temperature, int icon, String wear, int clothingIcon) {
        this.description = description;
        this.temperature = temperature;
        this.icon = icon;
        this.wear = wear;
        this.clothingIcon = clothingIcon;
    }
}
