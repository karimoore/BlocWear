package com.karimoore.android.whattowearapp;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.DotsPageIndicator;
import android.support.wearable.view.GridViewPager;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.view.View.OnApplyWindowInsetsListener;
import android.view.WindowInsets;
import android.widget.ProgressBar;
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
import com.karimoore.android.common.Weather;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks,
                                                    GoogleApiClient.OnConnectionFailedListener,
                                                    DataApi.DataListener {

    private static final Map<Integer,Integer> ICON_MAP;
    static {
        Map<Integer,Integer> aMap = new HashMap<Integer, Integer>();
        aMap.put(1,R.drawable.gold_sun_48);
        aMap.put(2,R.drawable.sun_cloud);
        aMap.put(3,R.drawable.cloud);
        aMap.put(4,R.drawable.sun_cloud);
        aMap.put(9,R.drawable.rain);
        aMap.put(10,R.drawable.rain);
        aMap.put(11,R.drawable.storm);
        aMap.put(13,R.drawable.snow);
        aMap.put(50,R.drawable.rain);
        ICON_MAP = Collections.unmodifiableMap(aMap);
    }
    private static final String TAG = "kariWatchMainActivity";
    private static final long CONNECTION_TIMEOUT_MS = 100;
    private static final String MESSAGE = "Send Weather";


    private ProgressBar mProgressBar;
    private TextView mTemperatureTextView;
    private TextView mKariTextView;
    private CardFragment myCardFragment;
    private GoogleApiClient mGoogleClient;
    private String nodeId;
    private GridViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initApi();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Resources res = getResources();
        mProgressBar = (ProgressBar) findViewById(R.id.pb_wait_for_weather_main);
        mProgressBar.setVisibility(ProgressBar.VISIBLE);
        pager = (GridViewPager) findViewById(R.id.pager);
        pager.setOnApplyWindowInsetsListener(new OnApplyWindowInsetsListener() {
            @Override
            public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
                // Adjust page margins:
                //   A little extra horizontal spacing between pages looks a bit
                //   less crowded on a round display.
                // applied since this listener has taken them over.
                pager.setPageMargins(100, 50);

                // 1GridViewPager relies on insets to properly handle
                // layout for round displays. They must be explicitly
                pager.onApplyWindowInsets(insets);
                return insets;
            }
        });
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
                mGoogleClient.blockingConnect(CONNECTION_TIMEOUT_MS, TimeUnit.MILLISECONDS);
                NodeApi.GetConnectedNodesResult result =
                        Wearable.NodeApi.getConnectedNodes(mGoogleClient).await();
                List<Node> nodes = result.getNodes();
                if (nodes.size() > 0){
                    nodeId = nodes.get(0).getId();
                    Wearable.MessageApi.sendMessage(mGoogleClient,nodeId, "Please Update the weather on the Wearable", new byte[0])
                            .setResultCallback(
                                    new ResultCallback<MessageApi.SendMessageResult>() {
                                        @Override
                                        public void onResult(MessageApi.SendMessageResult sendMessageResult) {
                                            if (!sendMessageResult.getStatus().isSuccess()) {
                                                Log.d(TAG, "Failed to send the message with status code: "
                                                        + sendMessageResult.getStatus().getStatusCode());
                                            }
                                            else {
                                                Log.d(TAG, "Successfully sent the message with status code: "
                                                        + sendMessageResult.getStatus().getStatusCode());
                                                }
                                        }
                                    }
                            );
                }
            }

        }).start();
    }


    @Override
    protected void onStop() {
        if (mGoogleClient!= null && mGoogleClient.isConnected()){
            Wearable.DataApi.removeListener(mGoogleClient, this);
            mGoogleClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(MainActivity.this, "Connected to google API", Toast.LENGTH_SHORT).show();
        Wearable.DataApi.addListener(mGoogleClient, this);
        Log.d(TAG, "AddListener (this) ");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, connectionResult.toString());

    }


    public void updateTemperature(int displayTemp, String displayDescription, int iconCode){
        mProgressBar.setVisibility(ProgressBar.INVISIBLE);
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        CardFragment fragment = CardFragment.create(String.valueOf(displayTemp),
                displayDescription, ICON_MAP.get(iconCode));

        transaction.add(R.id.frame_layout, fragment);
        transaction.commit();

    }
    public void updateForecast(ArrayList<DataMap> weatherList){
        mProgressBar.setVisibility(ProgressBar.INVISIBLE);
        pager.setAdapter(new WearGridPagerAdapter(this, getFragmentManager(), fromDataMap(weatherList)));
        DotsPageIndicator dotsPageIndicator = (DotsPageIndicator) findViewById(R.id.page_indicator);
        dotsPageIndicator.setPager(pager);

    }

    private List<Weather> fromDataMap(ArrayList<DataMap> weatherList) {
        int size = weatherList.size();
        List<Weather> forecast = new ArrayList<Weather>(size);
        for (int i=0; i<size; i++){
            //iterate through array - populate Object for UI
            DataMap dm = weatherList.get(i);
            int temp = dm.getInt("wearTemperature");
            String description = dm.getString("wearDescription");
            int icon = dm.getInt("wearIcon");
            forecast.add(new Weather(description, String.valueOf(temp), ICON_MAP.get(icon), 0));
        }
        return forecast;

    }

    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {
        for (DataEvent event : dataEventBuffer){
            if (event.getType() == DataEvent.TYPE_CHANGED){
                Log.d(TAG, "DataItem changed: " + event.getDataItem().getUri());
                DataItem dataItem = event.getDataItem();
                if (dataItem.getUri().getPath().compareTo("/FORECAST_DATA")==0){ //UGH - hard code!
                    DataMap dataMap = DataMapItem.fromDataItem(dataItem).getDataMap();
                    //update the ui on the wearable with dataMap.getInt("key")
                    updateForecast(dataMap.getDataMapArrayList("wearWeatherList"));
                }
            } else if (event.getType() == DataEvent.TYPE_DELETED) {
                // DataItem has been deleted
                Log.d(TAG, "DataItem deleted: " + event.getDataItem().getUri());

            }
        }

    }}
