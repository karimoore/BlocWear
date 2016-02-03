package com.karimoore.android.whattowearapp;

import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by kari on 12/7/15.
 * This class will listen for the wearable Messages
 */
public class MyWearListenerService extends WearableListenerService {

    private String nodeId;
    private GoogleApiClient mGoogleClient;  //  new instance or grab old?

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);
        messageEvent.getData();
        nodeId = messageEvent.getSourceNodeId(); // in case we need to hang on to this connected wearable/watch
        String path = messageEvent.getPath();
        Toast.makeText(MyWearListenerService.this, "Received:  " + path, Toast.LENGTH_SHORT).show();

        // get update from network for current weather
        sendWeatherUpdate(80);
    }

    // Send weather data to watch
    private void sendWeatherUpdate(int temperature) {
        PutDataMapRequest mapRequest = PutDataMapRequest.create("/temperature");
        mapRequest.getDataMap().putInt("key",temperature);
        PutDataRequest request = mapRequest.asPutDataRequest();
        PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi.putDataItem(mGoogleClient, request);
    }
}
