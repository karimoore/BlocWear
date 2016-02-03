package com.karimoore.android.whattowearapp.model.data;

import android.util.Log;

import com.google.android.gms.wearable.DataMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by kari on 1/12/16.
 */
public class ForecastData implements JSONPopulator {
    private static final String TAG = "kariForecastData";

    private List <WeatherData> forecast;

    public List<WeatherData> getForecast() {
        return forecast;
    }

    @Override
    public void populate(JSONObject data) {


        int count = 0;

        JSONArray forecastList = null;
        try {
            count = data.getInt("cnt");
            forecastList = data.getJSONArray("list");//.getJSONObject(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject forecastItem = null;
        Log.d(TAG, "Forecast has " + count + " days in forecast");
        forecast = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            WeatherData wd = new WeatherData();
            JSONObject obj = null;
            JSONObject temperatureObject = null;
            try {
                forecastItem = forecastList.getJSONObject(i);
                obj = forecastItem.getJSONArray("weather").getJSONObject(0);
                temperatureObject = forecastItem.getJSONObject("temp");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            wd.setDescription(obj.optString("description"));
            String strIcon = obj.optString("icon");
            //get the int value for the icon to send to the watch
            wd.setIcon(wd.getIntIcon(strIcon));
            int temp = temperatureObject.optInt("day");
            wd.setTemperature(String.valueOf(temp));

            Log.d(TAG, "Storing- " + "Temp: " + wd.getTemperature()
                    + " weather desc: " + wd.getDescription());

            forecast.add(i, wd);
        }


    }
    public static final ArrayList<DataMap> toDataMap(ForecastData forecast){
        int count = forecast.getForecast().size();
        ArrayList<DataMap> forecastData = new ArrayList<>(count);

        for (int i=0; i<count; i++) {
            WeatherData wData = forecast.getForecast().get(i);
            DataMap dataMap = new DataMap();
// For testing - so there shows a change in the data
            Random rand = new Random();
            int zeroToTen = rand.nextInt(11);
            dataMap.putInt("wearTemperature", Integer.parseInt(wData.getTemperature())+zeroToTen);
            dataMap.putString("wearDescription", wData.getDescription());
            //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image);
            // Asset asset = createAssetFromBitmap(bitmap);
            dataMap.putInt("wearIcon", wData.getIcon());
             forecastData.add(dataMap);

        }
        return forecastData;

    }

}
