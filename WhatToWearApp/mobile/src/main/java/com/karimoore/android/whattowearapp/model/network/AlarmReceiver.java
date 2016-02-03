package com.karimoore.android.whattowearapp.model.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by kari on 1/17/16.
 */

public class AlarmReceiver extends BroadcastReceiver
{
    private static final String TAG = "kariAlarmReceiver";
    public AlarmReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Received Broadcast alarm, " + context.toString() + " Intent: " + intent.getAction());

        Intent myIntent = new Intent( context, HandleNotificationService.class );
        context.startService(myIntent);
        Log.d(TAG, "Start the HandleNotificationService!");

    }

}
