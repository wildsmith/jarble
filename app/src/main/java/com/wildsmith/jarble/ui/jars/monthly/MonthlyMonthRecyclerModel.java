package com.wildsmith.jarble.ui.jars.monthly;

import com.wildsmith.jarble.R;
import com.wildsmith.jarble.ui.recyclerview.dynamic.DynamicRecyclerModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

class MonthlyMonthRecyclerModel implements DynamicRecyclerModel {

    private String title;

    public MonthlyMonthRecyclerModel(long timestamp) {
        this(timestamp, "");
    }

    public MonthlyMonthRecyclerModel(long timestamp, String year) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        Date date = calendar.getTime();

        this.title = new SimpleDateFormat("LLLL", Locale.getDefault()).format(date) + " " + year;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int getLayoutId() {
        return R.layout.monthly_month_layout;
    }
}