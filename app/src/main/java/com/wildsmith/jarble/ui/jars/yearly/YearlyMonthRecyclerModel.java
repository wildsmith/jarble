package com.wildsmith.jarble.ui.jars.yearly;

import android.support.annotation.NonNull;

import com.wildsmith.jarble.R;
import com.wildsmith.recyclerview.dynamic.DynamicRecyclerModel;
import com.wildsmith.utils.DateUtils;

import java.lang.ref.WeakReference;

class YearlyMonthRecyclerModel implements DynamicRecyclerModel {

    private WeakReference<Listener> listenerWeakReference;

    private String title;

    private int jarsInMonth;

    public YearlyMonthRecyclerModel(@NonNull Listener listener, @NonNull String timestamp) {
        this.listenerWeakReference = new WeakReference<>(listener);
        this.title = DateUtils.reformatTimestamp(timestamp, DateUtils.HALF_MONTH_FORMAT);
    }

    public String getTitle() {
        return title;
    }

    public void setJarsInMonth(int jarsInMonth) {
        this.jarsInMonth = jarsInMonth;
    }

    public int getHeight() {
        final Listener listener = getListener();
        return (listener == null) ? 0 : listener.getHeight(jarsInMonth);
    }

    @Override
    public int getLayoutId() {
        return R.layout.yearly_month_layout;
    }

    private Listener getListener() {
        return (listenerWeakReference == null) ? null : listenerWeakReference.get();
    }

    public interface Listener {

        int getHeight(int jarsInMonth);
    }
}