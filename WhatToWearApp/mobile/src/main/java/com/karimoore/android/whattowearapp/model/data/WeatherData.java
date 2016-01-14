package com.karimoore.android.whattowearapp.model.data;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by kari on 12/15/15.
 */
public class WeatherData implements JSONPopulator {


    private String description;
    private String temperature;
    private int icon;

    public String getTemperature() {

        return temperature;
    }

    public String getDescription() {

        return description;
    }

    public int getIcon() {

        return icon;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
    @Override
    public void populate(JSONObject data) {
        JSONObject obj = null;
        try {
            obj = data.getJSONArray("weather").getJSONObject(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        description = obj.optString("description");
        String strIcon = obj.optString("icon");
        //get the int value for the icon to send to the watch
        icon = getIntIcon(strIcon);
        JSONObject temp = data.optJSONObject("main");
        int val = temp.optInt("temp");
        temperature = String.valueOf(val);


    }

    public int getIntIcon(String strIcon) {
        // the string will be in the form of "13d" or "13n" based on
        // openweatherAPI
        if (strIcon != null){
            return Integer.parseInt(strIcon.substring(0,strIcon.length()-1));
        }
        return 0;
    }

}
