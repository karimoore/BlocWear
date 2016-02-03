package com.karimoore.android.whattowearapp.model.network;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.karimoore.android.whattowearapp.model.data.ForecastData;

/**
 * Created by kari on 2/1/16.
 */
public class HandleNotificationService extends Service implements WeatherNetworkListener,  GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
{
    private static final String TAG = "kariHandleNotifService";
    private GoogleApiClient googleApiClient;

    public HandleNotificationService() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        googleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
        Log.d(TAG, "Trying to connect to Google;");

    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Destroy Service, should disconnect googleAPI");
        googleApiClient.disconnect();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "Connected GoogleAPiClient preparing for notification");
        WeatherNetwork weatherNetwork = new WeatherNetwork(this);
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (location != null) {
            weatherNetwork.refreshWeather(location);
        } else {
            Log.d(TAG, "Location is null, make sure location services is turned on ");
        }


    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "Connection Suspended");

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "Connection Failed: " + connectionResult.toString());

    }

    @Override
    public void networkSuccess(ForecastData fData) {

        Log.d(TAG, "I have data for the notification");
        NotificationSender notificationSender = new NotificationSender();
        notificationSender.sendNotification(fData, getApplicationContext());
        stopSelf();  // no longer need service

    }

    @Override
    public void networkFailure(Exception e) {
        Log.d(TAG, "Failed to get data for Notification:" + e.toString());

    }
}
