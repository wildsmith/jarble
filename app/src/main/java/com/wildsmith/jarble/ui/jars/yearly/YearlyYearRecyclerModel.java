package com.wildsmith.jarble.ui.jars.yearly;

import com.wildsmith.jarble.R;
import com.wildsmith.recyclerview.dynamic.DynamicRecyclerModel;

class YearlyYearRecyclerModel implements DynamicRecyclerModel {

    private String title;

    public YearlyYearRecyclerModel(String year) {
        this.title = year;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int getLayoutId() {
        return R.layout.yearly_year_layout;
    }
}