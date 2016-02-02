package com.karimoore.android.whattowearapp.model.network;

import android.app.Notification;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.karimoore.android.whattowearapp.R;
import com.karimoore.android.whattowearapp.model.data.ForecastData;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kari on 1/26/16.
 */
public class NotificationSender {
    private static final String TAG = "kariNotificationSender";

    private static final Map<Integer,Integer> ICON_MAP;
    static {
        Map<Integer,Integer> aMap = new HashMap<Integer, Integer>();
        aMap.put(1,R.drawable.sun24);
        aMap.put(2,R.drawable.sun_cloud24);
        aMap.put(3,R.drawable.cloud24);
        aMap.put(4,R.drawable.sun_cloud24);
        aMap.put(9,R.drawable.rain24);
        aMap.put(10,R.drawable.rain24);
        aMap.put(11,R.drawable.storm24);
        aMap.put(13,R.drawable.snow24);
        aMap.put(50,R.drawable.rain24);
        ICON_MAP = Collections.unmodifiableMap(aMap);
    }
    private static final Map<Integer,Integer> OUTFIT_COLD_MAP;
    static {
        Map<Integer,Integer> aMap = new HashMap<Integer, Integer>();
        aMap.put(1,R.drawable.beanie_50);
        aMap.put(2,R.drawable.beanie_50);
        aMap.put(3,R.drawable.beanie_50);
        aMap.put(4,R.drawable.beanie_50);
        aMap.put(9,R.drawable.umbrella); //rain boot
        aMap.put(10,R.drawable.umbrella);
        aMap.put(11,R.drawable.umbrella);
        aMap.put(13,R.drawable.boots);//snow boot
        aMap.put(50,R.drawable.umbrella);
        OUTFIT_COLD_MAP = Collections.unmodifiableMap(aMap);
    }
    private static final Map<Integer,String> OUTFIT_COLD_WHATTOWEAR_MAP;
    static {
        Map<Integer,String> aMap = new HashMap<Integer, String>();
        aMap.put(1,"Wear beanie.");
        aMap.put(2,"Wear beanie.");
        aMap.put(3,"Wear beanie.");
        aMap.put(4,"Wear beanie.");
        aMap.put(9,"Bring an umbrealla"); //rain boot
        aMap.put(10,"Bring an umbrella.");
        aMap.put(11,"Bring an umbrella.");
        aMap.put(13,"Wear snow boots.");//snow boot
        aMap.put(50,"Bring an umbrella.");
        OUTFIT_COLD_WHATTOWEAR_MAP = Collections.unmodifiableMap(aMap);
    }
    private static final Map<Integer,Integer> OUTFIT_HOT_MAP;
    static {
        Map<Integer,Integer> aMap = new HashMap<Integer, Integer>();
        aMap.put(1,R.drawable.shorts);
        aMap.put(2,R.drawable.shorts);
        aMap.put(3,R.drawable.shorts);
        aMap.put(4,R.drawable.shorts);
        aMap.put(9,R.drawable.umbrella); //rain boot
        aMap.put(10,R.drawable.umbrella);
        aMap.put(11,R.drawable.umbrella);
        aMap.put(13,R.drawable.boots);//snow boot
        aMap.put(50,R.drawable.umbrella);
        OUTFIT_HOT_MAP = Collections.unmodifiableMap(aMap);
    }

    private static final Map<Integer,String> OUTFIT_HOT_WHATTOWEAR_MAP;
    static {
        Map<Integer,String> aMap = new HashMap<Integer, String>();
        aMap.put(1,"Wear shorts.");
        aMap.put(2,"Wear shorts.");
        aMap.put(3,"Wear shorts.");
        aMap.put(4,"Wear shorts.");
        aMap.put(9,"Bring an umbrealla"); //rain boot
        aMap.put(10,"Bring an umbrella.");
        aMap.put(11,"Bring an umbrella.");
        aMap.put(13,"Wear snow boots.");//snow boot
        aMap.put(50,"Bring an umbrella.");
        OUTFIT_HOT_WHATTOWEAR_MAP = Collections.unmodifiableMap(aMap);
    }
    private static final Map<Integer,Integer> OUTFIT_MID_MAP;
    static {
        Map<Integer,Integer> aMap = new HashMap<Integer, Integer>();
        aMap.put(1,R.drawable.scarf);
        aMap.put(2,R.drawable.scarf);
        aMap.put(3,R.drawable.scarf);
        aMap.put(4,R.drawable.scarf);
        aMap.put(9,R.drawable.umbrella); //rain boot
        aMap.put(10,R.drawable.umbrella);
        aMap.put(11,R.drawable.umbrella);
        aMap.put(13,R.drawable.boots);//snow boot
        aMap.put(50,R.drawable.umbrella);
        OUTFIT_MID_MAP = Collections.unmodifiableMap(aMap);
    }

    private static final Map<Integer,String> OUTFIT_MID_WHATTOWEAR_MAP;
    static {
        Map<Integer,String> aMap = new HashMap<Integer, String>();
        aMap.put(1,"Wear layers.");
        aMap.put(2,"Wear layers.");
        aMap.put(3,"Wear layers.");
        aMap.put(4,"Wear layers.");
        aMap.put(9,"Bring an umbrealla"); //rain boot
        aMap.put(10,"Bring an umbrella.");
        aMap.put(11,"Bring an umbrella.");
        aMap.put(13,"Wear snow boots.");//snow boot
        aMap.put(50,"Bring an umbrella.");
        OUTFIT_MID_WHATTOWEAR_MAP = Collections.unmodifiableMap(aMap);
    }

    public void sendNotification(ForecastData fData, Context context) {

        int iconNumber = fData.getForecast().get(0).getIcon();
        int temperature = Integer.parseInt(fData.getForecast().get(0).getTemperature());
        int drawableID = getClothingIcon(temperature, iconNumber);
        String wearString = getWhatToWearString(temperature, iconNumber);


        NotificationCompat.WearableExtender wearableExtender = new NotificationCompat.WearableExtender()
                .setBackground(BitmapFactory.decodeResource(context.getResources(),drawableID))
                .setHintAvoidBackgroundClipping(true)
                .setContentIcon(ICON_MAP.get(iconNumber))
                .setHintHideIcon(true);

        Notification notif  = new NotificationCompat.Builder(context)
                        .setSmallIcon(ICON_MAP.get(iconNumber))
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                               ICON_MAP.get(iconNumber)))
                        .setContentText(fData.getForecast().get(0).getTemperature() + "\u00b0" )
                        .setContentTitle(wearString)
                        .extend(wearableExtender)
                        .build();


        // Get an instance of the NotificationManager service
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(context);

        // issues notification with notification manager.
        notificationManager.notify(1, notif);

    }

    private String getWhatToWearString(int temperature, int iconNumber) {
        if (temperature > 70) {
            return OUTFIT_HOT_WHATTOWEAR_MAP.get(iconNumber);
        } else if (temperature < 40) {
            return OUTFIT_COLD_WHATTOWEAR_MAP.get(iconNumber);
        } else {
            return OUTFIT_MID_WHATTOWEAR_MAP.get(iconNumber);
        }
    }

    private int getClothingIcon(int temperature, int iconNumber) {
        if (temperature > 70) {
            return OUTFIT_HOT_MAP.get(iconNumber);
        } else if (temperature < 40) {
            return OUTFIT_COLD_MAP.get(iconNumber);
        } else {
            return OUTFIT_MID_MAP.get(iconNumber);
        }

    }
}
