package com.wildsmith.jarble.ui.jars.yearly;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wildsmith.jarble.R;
import com.wildsmith.jarble.jar.JarTableModel;
import com.wildsmith.jarble.ui.jars.JarsFragment;
import com.wildsmith.layoutmanager.StaggeredGridAutoFitLayoutManager;
import com.wildsmith.recyclerview.dynamic.DynamicRecyclerAdapter;
import com.wildsmith.recyclerview.dynamic.DynamicRecyclerModel;
import com.wildsmith.utils.CollectionUtils;
import com.wildsmith.utils.RecyclerViewItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class YearlyFragment extends JarsFragment implements StaggeredGridAutoFitLayoutManager.Listener, YearlyMonthRecyclerModel.Listener {

    public static final String TAG = YearlyFragment.class.getSimpleName();

    private int spanCount;

    private int spanSize;

    public static YearlyFragment newInstance() {
        return new YearlyFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return YearlyViewWrapper.onCreateView(inflater.inflate(R.layout.recycler_view_grid, container, false));
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
        YearlyViewWrapper.onDestroyView();

        super.onDestroyView();
    }

    private void setupRecyclerView() {
        recyclerView = YearlyViewWrapper.findViewById(R.id.recycler_view);
        if (recyclerView == null) {
            return;
        }

        recyclerView.setHasFixedSize(true);

        if (spanCount == 0) {
            recyclerView.setLayoutManager(new StaggeredGridAutoFitLayoutManager(this, recyclerView, getSpanSizeFromChild(getResources())));
        } else {
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL));
        }

        if (recyclerAdapter == null) {
            recyclerAdapter = new DynamicRecyclerAdapter<>(this);
        }

        recyclerView.addItemDecoration(new RecyclerViewItemDecoration(getResources(),
            R.dimen.yearly_recycler_view_item_decoration_space));

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

            //In order to have the month column size properly the number of jars in the month AND the pointer to the previous month
            // recycler model need to be retained.
            int jarsInMonth = 0;
            YearlyMonthRecyclerModel monthRecyclerModel = null;

            int previousYear = -1;
            int previousMonth = -1;
            for (JarTableModel jarTableModel : jarTableModels) {
                if (previousYear == -1 || previousMonth == -1) {
                    previousYear = jarTableModel.getYear();
                    recyclerItems.add(new YearlyYearRecyclerModel("" + previousYear));

                    previousMonth = jarTableModel.getMonth();
                    monthRecyclerModel = new YearlyMonthRecyclerModel(this, jarTableModel.getTimestamp());
                    recyclerItems.add(monthRecyclerModel);
                }

                final int newYear = jarTableModel.getYear();
                if (previousYear != newYear) {
                    recyclerItems.add(new YearlyYearRecyclerModel("" + newYear));
                }

                final int newMonth = jarTableModel.getMonth();
                if (previousMonth != newMonth) {
                    monthRecyclerModel.setJarsInMonth(jarsInMonth);
                    monthRecyclerModel = new YearlyMonthRecyclerModel(this, jarTableModel.getTimestamp());
                    recyclerItems.add(monthRecyclerModel);
                    jarsInMonth = 0;
                }

                recyclerItems.add(new YearlyJarRecyclerModel(jarTableModel));

                previousYear = jarTableModel.getYear();
                previousMonth = jarTableModel.getMonth();

                jarsInMonth++;
            }

            //for the last month recycler model
            if (monthRecyclerModel != null) {
                monthRecyclerModel.setJarsInMonth(jarsInMonth);
            }
        }

        return recyclerItems;
    }

    @Override
    public int getSpanSizeFromChild(Resources resources) {
        if (spanSize == 0) {
            spanSize = getSpanSize(resources);
        }
        return spanSize;
    }

    public static int getSpanSize(@NonNull Resources resources) {
        return (int) resources.getDimension(R.dimen.yearly_jars_gridview_jar_size);
    }

    @Override
    protected boolean isIgnorableTransitionView(View transitionView) {
        return transitionView instanceof YearlyMonthRecyclerModelView || transitionView instanceof YearlyYearRecyclerModelView;
    }

    @Override
    public void onSpanCountSet(int spanCount) {
        this.spanCount = spanCount;
    }

    @Override
    public int getHeight(int jarsInMonth) {
        StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
        //Due to the span taken up by the month the real span count is spanCount - 1
        final float spanCount = layoutManager.getSpanCount() - 1;
        final double rowCount = Math.ceil(jarsInMonth / spanCount);
        return (int) rowCount * getSpanSize(getResources());
    }
}
