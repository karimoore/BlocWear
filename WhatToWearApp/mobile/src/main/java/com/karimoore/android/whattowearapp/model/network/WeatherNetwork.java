package com.karimoore.android.whattowearapp.model.network;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.karimoore.android.whattowearapp.model.data.ForecastData;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by kari on 12/15/15.
 */
public class WeatherNetwork {

    private static final String TAG = "kariPhoneWeatherNetwork";
    private WeatherNetworkListener listener;
    private Exception error;

    private static final String WEATHER_API = "http://api.openweathermap.org/data/2.5/weather?";
    private static final String FORECAST_API = "http://api.openweathermap.org/data/2.5/forecast/daily?";
    private static final String LAT = "lat";
    private static final String LON = "lon";
    private static final String UNITS = "units";
    private static final String UNIT_TYPE = "imperial";
    private static final String COUNT = "cnt";
    private static final String NUM_DAYS = "5";
    private static final String WEATHER_API_KEY= "appid=27102c50da8d94840c144bb2fe0831bc";



    public WeatherNetwork(WeatherNetworkListener listener) {

        this.listener = listener;


    }

    public void refreshWeather(final Location location) {

//        new AsyncTask<Location, Void, WeatherData>() {
        Log.d(TAG, "refresh weather with Location:  " + location.toString());
        new AsyncTask<Location, Void, ForecastData>() {
            @Override
            protected ForecastData doInBackground(Location[] locations) {


//                String urlWeather = "http://api.openweathermap.org/data/2.5/weather?lat=42&lon=-88&units=imperial&appid=27102c50da8d94840c144bb2fe0831bc";
                StringBuilder sBuilder = new StringBuilder(FORECAST_API);
                sBuilder.append(LAT+"=");

                long roundLat = Math.round(location.getLatitude());
                sBuilder.append(String.valueOf(roundLat));
                sBuilder.append("&"+LON+"=");
                long roundLon = Math.round(location.getLongitude());
                sBuilder.append(String.valueOf(roundLon));
                sBuilder.append("&" + UNITS + "=" + UNIT_TYPE);
                sBuilder.append("&" + COUNT + "=" + NUM_DAYS);
                sBuilder.append("&" + WEATHER_API_KEY);
                Log.d(TAG, "Getting data:  " + sBuilder);
                try {
                    URL url = new URL(sBuilder.toString());

                    URLConnection connection = url.openConnection();
                    connection.setUseCaches(false);

                    InputStream inputStream = connection.getInputStream();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    ForecastData networkForecastData = new ForecastData();
                    networkForecastData.populate(new JSONObject(result.toString()));
                    return networkForecastData;

                } catch (Exception e) {
                    error = e;
                    Log.d(TAG, "Failed to read data with Exception:" + e.toString());
                }
                return null;
            }

            @Override
//            protected void onPostExecute(WeatherData weatherData) {
            protected void onPostExecute(ForecastData forecastData) {

                // send back data to activity
//                Log.d(TAG, "Here is the WeatherData(temp/description: " + weatherData.getTemperature()
//                                                            + "/" + weatherData.getDescription());

                if (forecastData == null && error != null) {
                    listener.networkFailure(error);
                } else {
                    listener.networkSuccess(forecastData);
                }


            }

        }.execute(location);
    }
}
