package com.wildsmith.jarble.ui.jars.yearly;

import android.support.annotation.NonNull;

import com.wildsmith.jarble.R;
import com.wildsmith.jarble.jar.JarTableModel;
import com.wildsmith.recyclerview.dynamic.DynamicRecyclerModel;
import com.wildsmith.utils.DateUtils;

import java.util.List;

class YearlyMonthRecyclerModel implements DynamicRecyclerModel {

    private List<JarTableModel> jars;

    private String title;

    public YearlyMonthRecyclerModel(@NonNull String timestamp, List<JarTableModel> jars) {
        this.jars = jars;
        this.title = DateUtils.reformatTimestamp(timestamp, DateUtils.HALF_MONTH_FORMAT);
    }

    public String getTitle() {
        return title;
    }

    public List<JarTableModel> getJars() {
        return jars;
    }

    @Override
    public int getLayoutId() {
        return R.layout.yearly_month_layout;
    }
}