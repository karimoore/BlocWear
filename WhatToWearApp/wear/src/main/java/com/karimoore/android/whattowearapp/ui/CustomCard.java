package com.karimoore.android.whattowearapp.ui;

import android.os.Bundle;
import android.support.wearable.view.CardFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.karimoore.android.whattowearapp.R;

/**
 * Created by kari on 1/18/16.
 */
public class CustomCard extends CardFragment {

    public static final CustomCard newInstance(String day, String temp, String condition, int weatherIcon, String wearText, int clothesIcon)
    {
        CustomCard f = new CustomCard();
        Bundle bdl = new Bundle();
        bdl.putString("day", day);
        bdl.putString("temperature", temp);
        bdl.putString("condition", condition);
        bdl.putInt("icon", weatherIcon);
        bdl.putString("wear", wearText);
        bdl.putInt("clothesIcon", clothesIcon);
        f.setArguments(bdl);
        return f;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // show custom card
        View v =  inflater.inflate(R.layout.custom_forecast_card, container, false);

        // get data from the bundle
        Bundle bundle = getArguments();
        String day = bundle.getString("day");
        String temp = bundle.getString("temperature");
        String condition = bundle.getString("condition");
        int icon = bundle.getInt("icon");
        String wear = bundle.getString("wear");
        int clothesIcon = bundle.getInt("clothesIcon");



        // show text and images on card
        TextView tvDate = (TextView) v.findViewById(R.id.date);
        tvDate.setText(day);
        TextView tvTemp = (TextView) v.findViewById(R.id.tv_card_temp);
        tvTemp.setText(temp + "\u00b0");
        TextView tvCondition = (TextView) v.findViewById(R.id.tv_card_condition);
        tvCondition.setText(condition);
        ImageView iv = (ImageView) v.findViewById(R.id.iv_weather_image);
        iv.setImageResource(icon);
        TextView tvWear = (TextView) v.findViewById(R.id.tv_card_clothes);
        tvWear.setText(wear);
        ImageView ivApparel = (ImageView) v.findViewById(R.id.iv_card_apparel);
        ivApparel.setImageResource(clothesIcon);
        return v;
    }
}
