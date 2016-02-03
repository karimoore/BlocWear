package com.karimoore.android.whattowearapp.model.network;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by kari on 1/27/16.
 */
public class BootReceiver extends BroadcastReceiver {

    private static final String TAG = "kariBootReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        // set the alarm for 8AM

        Log.d(TAG, "onReceive: Booted and set alarm");
        Calendar eightAM = Calendar.getInstance();
        eightAM.setTimeInMillis(System.currentTimeMillis());
        eightAM.set(Calendar.HOUR_OF_DAY, 8);

        Intent refreshIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent notifPendingIntent = PendingIntent.getBroadcast(context, 0, refreshIntent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 15 * 1000,
  //              notifPendingIntent);  // testing

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                eightAM.getTimeInMillis(), AlarmManager.INTERVAL_DAY,
                notifPendingIntent);


    }
}
