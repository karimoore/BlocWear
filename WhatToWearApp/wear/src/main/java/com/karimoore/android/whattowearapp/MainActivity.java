package com.karimoore.android.whattowearapp;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks,
                                                    GoogleApiClient.OnConnectionFailedListener,
                                                    DataApi.DataListener {

    private TextView mTemperatureTextView;
    private TextView mKariTextView;
    private CardFragment myCardFragment;
    private GoogleApiClient mGoogleClient;
    private String nodeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initApi();
        sendMessageToUpdateWeather();

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        CardFragment fragment = CardFragment.create(
                getString(R.string.tv_temperature),
                getString(R.string.tv_kari_description),R.drawable.gold_sun_48);

        transaction.add(R.id.frame_layout, fragment);
        transaction.commit();

    }

    private void sendMessageToUpdateWeather() {

        if (nodeId != null){
            // connect and send message
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mGoogleClient.blockingConnect();
                    Wearable.MessageApi.sendMessage(mGoogleClient,nodeId, "Please Update the weather on the Wearable", null)
                    .setResultCallback(
                            new ResultCallback<MessageApi.SendMessageResult>() {
                                @Override
                                public void onResult(MessageApi.SendMessageResult sendMessageResult) {
                                    if (!sendMessageResult.getStatus().isSuccess()){
                                        Log.d("KariWear", "Failed to send the message with status code: "
                                                            + sendMessageResult.getStatus().getStatusCode());
                                    }
                                }
                            }
                    );
                    mGoogleClient.disconnect();
                }
            }).start();
        }
    }

    private void initApi() {
        // setup the instance of googleApiClient
        // set the node Id

        mGoogleClient = new GoogleApiClient.Builder(this)
            .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
            .build();


        new Thread(new Runnable() {
            @Override
            public void run() {
                mGoogleClient.blockingConnect();
                NodeApi.GetConnectedNodesResult result =
                        Wearable.NodeApi.getConnectedNodes(mGoogleClient).await();
                List<Node> nodes = result.getNodes();
                if (nodes.size() > 0){
                    nodeId = nodes.get(0).getId();
                }
                mGoogleClient.disconnect();
            }

        }).start();
    }


    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(MainActivity.this, "Connected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("KariWear", connectionResult.toString());

    }

    // DataApi.DataListener
    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {
        for (DataEvent event : dataEventBuffer){
            if (event.getType() == DataEvent.TYPE_CHANGED){
                DataItem dataItem = event.getDataItem();
                if (dataItem.getUri().getPath().compareTo("/temperature")==0){
                    DataMap dataMap = DataMapItem.fromDataItem(dataItem).getDataMap();
                    //update the ui on the wearable with dataMap.getInt("key")
                }
            } else if (event.getType() == DataEvent.TYPE_DELETED) {
                // DataItem has been deleted

            }
        }

    }
}
