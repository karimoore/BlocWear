package com.karimoore.android.whattowearapp.model.network;

import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;
import com.karimoore.android.whattowearapp.model.data.ForecastData;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.TimeUnit;

/**
 * Created by kari on 12/7/15.
 * This class will listen for the wearable Messages
 */
public class MyWearListenerService extends WearableListenerService implements WeatherNetworkListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = "kariPhoneListenService";
    private String nodeId;
    private WeatherNetwork mWeatherNetwork;
    private Location mLastLocation;
    private GoogleApiClient googleApiClient;

    @Override
    public void onCreate() {
        super.onCreate();


    }

    @Override
    public void onDestroy() {
        googleApiClient.disconnect();
        super.onDestroy();
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);
        Log.d(TAG, "Received the message from node (watch) ");

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();

    }



    private Asset createAssetFromBitmap(Bitmap bitmap) {
        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
        return Asset.createFromBytes(byteStream.toByteArray());

    }

    @Override
    public void networkSuccess(final ForecastData fData) {

        for (int i = 0; i < fData.getForecast().size(); i++){
            Log.d(TAG, "Sending over this forecast- " + "Temp: " + fData.getForecast().get(i).getTemperature()
                    + " weather desc: " + fData.getForecast().get(i).getDescription());
        }

        PutDataMapRequest mapRequest = PutDataMapRequest.create("/FORECAST_DATA");
        mapRequest.getDataMap().putDataMapArrayList("wearWeatherList", ForecastData.toDataMap(fData));
        PutDataRequest request = mapRequest.asPutDataRequest();
        request.setUrgent();
        // send data over to watch
        Wearable.DataApi.putDataItem(googleApiClient, request)
                .setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
                    @Override
                    public void onResult(DataApi.DataItemResult dataItemResult) {
                        if (!dataItemResult.getStatus().isSuccess()) {
                            Log.d(TAG, "Failed to send updated weather data item " + fData.getForecast().get(0).getTemperature());

                        } else {
                            Log.d(TAG, "Successfully sent updated weather forecast data item " + fData.getForecast().get(0).getTemperature());

                        }

                    }
                });

    }

    @Override
    public void networkFailure(Exception e) {
        Log.d(TAG, "Unable to get weather data: network failure ");

    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "Connected to google API Client ");


        mWeatherNetwork = new WeatherNetwork(this);

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (mLastLocation != null){
            mWeatherNetwork.refreshWeather(mLastLocation);
        } else {
            Log.d(TAG, "Location is returning null");
        }


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "Google Connection to APIClient Failed ");

    }
}
