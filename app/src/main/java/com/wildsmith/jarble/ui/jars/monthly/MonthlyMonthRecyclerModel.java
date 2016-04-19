package com.wildsmith.jarble.ui.jars.monthly;

import android.support.annotation.NonNull;

import com.wildsmith.jarble.R;
import com.wildsmith.recyclerview.dynamic.DynamicRecyclerModel;
import com.wildsmith.utils.DateUtils;

class MonthlyMonthRecyclerModel implements DynamicRecyclerModel {

    private static final String TAG = MonthlyMonthRecyclerModel.class.getSimpleName();

    private String title;

    public MonthlyMonthRecyclerModel(@NonNull String timestamp) {
        this(timestamp, "");
    }

    public MonthlyMonthRecyclerModel(@NonNull String timestamp, String year) {
        this.title = DateUtils.reformatTimestamp(timestamp, DateUtils.FULL_MONTH_FORMAT) + " " + year;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int getLayoutId() {
        return R.layout.monthly_month_layout;
    }
}