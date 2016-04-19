package com.wildsmith.jarble.ui.jars.weekly;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wildsmith.jarble.BaseApplication;
import com.wildsmith.jarble.R;
import com.wildsmith.jarble.jar.JarTableModel;
import com.wildsmith.jarble.ui.jars.JarsFragment;
import com.wildsmith.recyclerview.dynamic.DynamicRecyclerAdapter;
import com.wildsmith.recyclerview.dynamic.DynamicRecyclerModel;
import com.wildsmith.utils.CollectionUtils;
import com.wildsmith.utils.DateUtils;
import com.wildsmith.utils.GridAutofitLayoutManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class WeeklyFragment extends JarsFragment {

    public static final String TAG = WeeklyFragment.class.getSimpleName();

    public static WeeklyFragment newInstance() {
        return new WeeklyFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return WeeklyViewWrapper.onCreateView(inflater.inflate(R.layout.recycler_view_grid, container, false));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupRecyclerView();
        setupRecyclerViewAnimationPointers();
        setupRecyclerViewItems();
    }

    @Override
    public void onDestroyView() {
        WeeklyViewWrapper.onDestroyView();

        super.onDestroyView();
    }

    private void setupRecyclerView() {
        recyclerView = WeeklyViewWrapper.findViewById(R.id.recycler_view);
        if (recyclerView == null) {
            return;
        }

        recyclerView.setHasFixedSize(true);

        final GridLayoutManager layoutManager = new GridAutofitLayoutManager(getActivity(), getColumnWidth(getResources()));
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (recyclerAdapter.getItemViewType(position)) {
                    case R.layout.weekly_day_layout:
                        return layoutManager.getSpanCount();
                    default:
                        return 1;
                }
            }
        });
        recyclerView.setLayoutManager(layoutManager);

        if (recyclerAdapter == null) {
            recyclerAdapter = new DynamicRecyclerAdapter<>(this);
        }

        recyclerView.addItemDecoration(new WeeklyRecyclerViewItemDecoration(getResources(),
            R.dimen.weekly_recycler_view_item_decoration_space));

        recyclerView.setAdapter(recyclerAdapter);
    }

    private void setupRecyclerViewItems() {
        List<DynamicRecyclerModel> recyclerModels = recyclerAdapter.getRecyclerModels();
        if (!recyclerModels.isEmpty()) {
            return;
        }

        grabJarTableModelsForYear();
    }

    @Override
    protected List<DynamicRecyclerModel> convertYearlyTableModelsToRecyclerItems(List<JarTableModel> jarTableModels) {
        List<DynamicRecyclerModel> recyclerItems;
        if (CollectionUtils.isEmpty(jarTableModels)) {
            recyclerItems = new ArrayList<>();
        } else {
            recyclerItems = new ArrayList<>(jarTableModels.size());

            final int currentYear = BaseApplication.getCalendar(false).get(Calendar.YEAR);

            int storedYear = -1;
            int storedDayOfMonth = -1;
            for (JarTableModel jarTableModel : jarTableModels) {
                if (storedYear == -1) {
                    storedYear = jarTableModel.getYear();
                    storedDayOfMonth = jarTableModel.getDayOfMonth();

                    jarTableModel.getTimestamp();

                    recyclerItems.add(new WeeklyDayRecyclerModel(buildDayTitle(jarTableModel.getDayOfMonth(), jarTableModel.getTimestamp(),
                        storedYear, currentYear)));
                }

                int newDayOfMonth = jarTableModel.getDayOfMonth();
                if (newDayOfMonth != storedDayOfMonth) {
                    recyclerItems.add(new WeeklyDayRecyclerModel(buildDayTitle(newDayOfMonth, jarTableModel.getTimestamp(),
                        storedYear, currentYear)));
                }

                recyclerItems.add(new WeeklyJarRecyclerModel(jarTableModel));

                storedYear = jarTableModel.getYear();
                storedDayOfMonth = jarTableModel.getDayOfMonth();
            }
        }

        return recyclerItems;
    }

    private String buildDayTitle(int dayOfMonth, String timestamp, int previousYear, int currentYear) {
        final String yearString = (previousYear == currentYear) ? "" : ", " + previousYear;
        final String monthString = DateUtils.reformatTimestamp(timestamp, DateUtils.HALF_MONTH_FORMAT);
        final String dayString = DateUtils.reformatTimestamp(timestamp, DateUtils.FULL_DAY_FORMAT);
        return dayString + ", " + monthString + " " + dayOfMonth + " " + yearString;
    }

    @Override
    public int getColumnWidthFromChild(Resources resources) {
        return getColumnWidth(resources);
    }

    public static int getColumnWidth(@NonNull Resources resources) {
        return (int) resources.getDimension(R.dimen.weekly_jar_size);
    }
}
