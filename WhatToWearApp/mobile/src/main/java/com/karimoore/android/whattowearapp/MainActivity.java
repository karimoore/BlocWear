package com.karimoore.android.whattowearapp;

import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.karimoore.android.whattowearapp.model.data.WeatherData;
import com.karimoore.android.whattowearapp.model.network.WeatherNetwork;
import com.karimoore.android.whattowearapp.model.network.WeatherNetworkListener;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "kariPhoneMainActivity";
    private GoogleApiClient mGoogleApiClient;
    private WeatherNetwork mWeatherNetwork;
    private Location mLastLocation;
    private TextView mLatitudeText;
    private TextView mLongitudeText;

    private TextView temperature;
    private TextView condition;
    private ImageView weatherIcon;


    private String mLongitude;
    private String mLatitude;

    WeatherData weatherData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Kari, Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                // Go get the weather from OpenWeatherApi
                //getWeatherFromWeb(mLongitude, mLatitude);


            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onStop() {
        super.onStop();

    }
}

/*

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(MainActivity.this, "Connection Suspended", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(MainActivity.this, "Connection Failed", Toast.LENGTH_SHORT).show();
    }

    // WeatherNetworkListener---------------------
    @Override
    public void networkSuccess(WeatherData wData) {
        weatherData = wData;  // this is current weather at start of app
        // update the screen
        condition.setText(wData.getDescription());
        temperature.setText(wData.getTemperature());

        // if sending a bitmap use the asset class
        //Asset asset - createAssetFromBitmap(bitmap); 100 kByte
    }

    @Override
    public void networkFailure(Exception e) {
        //post error

    }
    // WeatherNetworkListener ----------------------------
}
*/
