package com.karimoore.android.whattowearapp.model.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;
import com.karimoore.android.whattowearapp.MainActivity;
import com.karimoore.android.whattowearapp.R;
import com.karimoore.android.whattowearapp.model.data.WeatherData;
import com.karimoore.android.whattowearapp.model.network.WeatherNetwork;
import com.karimoore.android.whattowearapp.model.network.WeatherNetworkListener;

import java.io.ByteArrayOutputStream;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

/**
 * Created by kari on 12/7/15.
 * This class will listen for the wearable Messages
 */
public class MyWearListenerService extends WearableListenerService implements WeatherNetworkListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "kariPhoneListenService";
    private String nodeId;
    private GoogleApiClient mGoogleClient;
    private WeatherNetwork mWeatherNetwork;
    private Location mLastLocation;

    @Override
    public void onCreate() {
        super.onCreate();

        mGoogleClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleClient.connect();

        // could get the Location at startup?

    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);
        messageEvent.getData();
        nodeId = messageEvent.getSourceNodeId(); // in case we need to hang on to this connected wearable/watch
        String path = messageEvent.getPath();
        Toast.makeText(getApplicationContext(), "Received:  " + path, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Received the message from node: "
                + path + " " + nodeId);


        mWeatherNetwork = new WeatherNetwork(this);

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleClient);
        if (mLastLocation != null){
            mWeatherNetwork.refreshWeather(mLastLocation);
        }
    }


    @Override
    public void networkSuccess(final WeatherData wData) {
        PutDataMapRequest mapRequest = PutDataMapRequest.create("/WEATHER_DATA");
        // add random number temporarily so OnDataChanged is called each time
        Random rand = new Random();
        int zeroToTen = rand.nextInt(11);
        mapRequest.getDataMap().putInt("wearTemperature", Integer.parseInt(wData.getTemperature())+zeroToTen);
        mapRequest.getDataMap().putString("wearDescription", wData.getDescription());
        //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image);
       // Asset asset = createAssetFromBitmap(bitmap);
        mapRequest.getDataMap().putInt("wearIcon", wData.getIcon());
        PutDataRequest request = mapRequest.asPutDataRequest();
        request.setUrgent();
        Wearable.DataApi.putDataItem(mGoogleClient, request)
                .setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
                    @Override
                    public void onResult(DataApi.DataItemResult dataItemResult) {
                        if (!dataItemResult.getStatus().isSuccess()) {
                            Log.d(TAG,"Failed to send updated weather data item " + wData.getTemperature() );

                        } else {
                            Log.d(TAG, "Successfully sent updated weather data item " + wData.getTemperature());
                            Toast.makeText(getApplicationContext(), "Sending temperature:  " + wData.getTemperature(), Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }

    private Asset createAssetFromBitmap(Bitmap bitmap) {
        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
        return Asset.createFromBytes(byteStream.toByteArray());

    }

    @Override
    public void networkFailure(Exception e) {
        Toast.makeText(getApplicationContext(), "Unable to get weather data", Toast.LENGTH_SHORT).show();

    }

//    Google API ---------------------------
    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(getApplicationContext(), "Connected to Google API", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(), "Connection Failed", Toast.LENGTH_SHORT).show();

    }
}
