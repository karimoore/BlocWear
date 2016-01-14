package com.karimoore.android.whattowearapp.model.network;

import com.karimoore.android.whattowearapp.model.data.ForecastData;
import com.karimoore.android.whattowearapp.model.data.WeatherData;

/**
 * Created by kari on 12/16/15.
 */
public interface WeatherNetworkListener {
    void networkSuccess(ForecastData fData);
    void networkFailure(Exception e);
}
