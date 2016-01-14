package com.karimoore.android.whattowearapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.FragmentGridPagerAdapter;
import android.support.wearable.view.GridPagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.wearable.DataMap;
import com.karimoore.android.common.Weather;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kari on 1/8/16.
 */
public class WearGridPagerAdapter extends FragmentGridPagerAdapter {
    private final Context mContext;
    private Row mRow;

    public WearGridPagerAdapter(Context ctx, FragmentManager fm, List<Weather> forecast) {
        super(fm);
        mContext = ctx;

        mRow = new Row();

        for (int i = 0; i < forecast.size(); i++){
            Weather weather = forecast.get(i);
            mRow.add(cardFragment(weather.temperature, weather.description, weather.icon));
        }
    }
    private Fragment cardFragment(String titleRes, String textRes, int iconRes) {
        Resources res = mContext.getResources();
        CardFragment fragment =
                CardFragment.create(titleRes, textRes, iconRes);
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

    class DrawableLoadingTask extends AsyncTask<Integer, Void, Drawable> {
        private static final String TAG = "Loader";
        private Context context;

        DrawableLoadingTask(Context context) {
            this.context = context;
        }

        @Override
        protected Drawable doInBackground(Integer... params) {
            Log.d(TAG, "Loading asset 0x" + Integer.toHexString(params[0]));
            return context.getResources().getDrawable(params[0]);
        }
    }
}

