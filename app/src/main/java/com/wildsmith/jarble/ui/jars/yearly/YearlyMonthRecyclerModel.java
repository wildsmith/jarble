package com.wildsmith.jarble.ui.jars.yearly;

import com.wildsmith.jarble.R;
import com.wildsmith.jarble.provider.jar.JarTableModel;
import com.wildsmith.jarble.ui.recyclerview.dynamic.DynamicRecyclerModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

class YearlyMonthRecyclerModel implements DynamicRecyclerModel {

    private List<JarTableModel> jars;

    private String title;

    public YearlyMonthRecyclerModel(long timestamp, List<JarTableModel> jars) {
        this.jars = jars;

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        Date date = calendar.getTime();

        this.title = new SimpleDateFormat("MMM", Locale.getDefault()).format(date);
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