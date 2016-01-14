package com.karimoore.android.common;

/**
 * Created by kari on 1/13/16.
 */
public class Weather {

    public String description;
    public String temperature;
    public int icon;
    public int clothingIcon;

    public Weather() {
    }

    public Weather(String description, String temperature, int icon, int clothingIcon) {
        this.description = description;
        this.temperature = temperature;
        this.icon = icon;
        this.clothingIcon = clothingIcon;
    }
}
