package com.karimoore.android.whattowearapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.wearable.view.FragmentGridPagerAdapter;
import android.util.Log;

import com.karimoore.android.common.Weather;
import com.karimoore.android.whattowearapp.ui.CustomCard;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by kari on 1/8/16.
 */
public class WearGridPagerAdapter extends FragmentGridPagerAdapter {
    private static final String TAG = "kariGridPagerAdapter";

    //private final Context mContext;
    private Row mRow;

    public WearGridPagerAdapter(Context ctx, FragmentManager fm, List<Weather> forecast) {
        super(fm);
        //mContext = ctx;

        mRow = new Row();
        Calendar c = Calendar.getInstance();
        int dayInt = c.get(Calendar.DAY_OF_WEEK);


        for (int i = 0; i < forecast.size(); i++){
            Weather weather = forecast.get(i);
            mRow.add(cardFragment(dayOfWeek(i, dayInt++), weather.temperature, weather.description, weather.icon, weather.wear, weather.clothingIcon));
            Log.d(TAG, " adding a row- " + "Temp: " + weather.temperature
                    + " weather desc: " + weather.description
                    + " weather icon: " + weather.icon
                            + " clothing: "+ weather.wear
                            + " clothing icon: " + weather.clothingIcon);

        }
    }

    private String dayOfWeek(int i, int dayInt) {

        if (i == 0) {
            return "Today";
        } else if (i == 1) {
            return "Tomorrow";
        }
        String[] strDays = new String[] { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday",
                "Friday", "Saturday" };
        if (dayInt > 7) dayInt = dayInt % 7;
        // Day_OF_WEEK starts from 1 while array index starts from 0
        Log.d(TAG, String.valueOf(i) + " days from now is: "  + strDays[dayInt-1]);
        return strDays[dayInt-1];
    }

    private Fragment cardFragment(String day, String temp, String condition, int icon, String wear, int clothesIcon) {
        CustomCard fragment = CustomCard.newInstance(day, temp, condition, icon, wear, clothesIcon);
        // Add some extra bottom margin to leave room for the page indicator
        fragment.setCardMarginBottom(3);
        return fragment;
    }


    /** A convenient container for a row of fragments. */
    private class Row {
        final List<Fragment> columns = new ArrayList<Fragment>();

        public Row(Fragment... fragments) {
            for (Fragment f : fragments) {
                add(f);
            }
        }

        public void add(Fragment f) {
            columns.add(f);
        }

        Fragment getColumn(int i) {
            return columns.get(i);
        }

        public int getColumnCount() {
            return columns.size();
        }
    }

    @Override
    public Fragment getFragment(int row, int col) {

        return mRow.getColumn(col);
    }


    @Override
    public int getRowCount() {
        return 1;
    }

    @Override
    public int getColumnCount(int rowNum) {
        return mRow.getColumnCount();
    }

}

