package com.wildsmith.jarble.ui.jars.weekly;

import com.wildsmith.jarble.R;
import com.wildsmith.jarble.ui.recyclerview.dynamic.DynamicRecyclerModel;

class WeeklyDayRecyclerModel implements DynamicRecyclerModel {

    private String title;

    public WeeklyDayRecyclerModel(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int getLayoutId() {
        return R.layout.weekly_day_layout;
    }
}