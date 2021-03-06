package com.wildsmith.jarble.ui.jars.monthly;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wildsmith.jarble.R;
import com.wildsmith.jarble.jar.JarTableModel;
import com.wildsmith.jarble.ui.jars.JarsFragment;
import com.wildsmith.recyclerview.dynamic.DynamicRecyclerAdapter;
import com.wildsmith.recyclerview.dynamic.DynamicRecyclerModel;
import com.wildsmith.utils.CollectionUtils;
import com.wildsmith.layoutmanager.GridAutoFitLayoutManager;
import com.wildsmith.utils.RecyclerViewItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class MonthlyFragment extends JarsFragment {

    public static final String TAG = MonthlyFragment.class.getSimpleName();

    private int spanSize;

    public static MonthlyFragment newInstance() {
        return new MonthlyFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return MonthlyViewWrapper.onCreateView(inflater.inflate(R.layout.recycler_view_grid, container, false));
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
        MonthlyViewWrapper.onDestroyView();

        super.onDestroyView();
    }

    private void setupRecyclerView() {
        recyclerView = MonthlyViewWrapper.findViewById(R.id.recycler_view);
        if (recyclerView == null) {
            return;
        }

        recyclerView.setHasFixedSize(true);

        final GridLayoutManager layoutManager = new GridAutoFitLayoutManager(getActivity(), getSpanSize(getResources()));
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (recyclerAdapter.getItemViewType(position)) {
                    case R.layout.monthly_month_layout:
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

        recyclerView.addItemDecoration(new RecyclerViewItemDecoration(getResources(),
            R.dimen.monthly_recycler_view_item_decoration_space));

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

            int previousYear = -1;
            int previousMonth = -1;
            for (JarTableModel jarTableModel : jarTableModels) {
                if (previousYear == -1 || previousMonth == -1) {
                    previousYear = jarTableModel.getYear();

                    previousMonth = jarTableModel.getMonth();
                    recyclerItems.add(new MonthlyMonthRecyclerModel(jarTableModel.getTimestamp()));
                }

                final int newMonth = jarTableModel.getMonth();
                if (previousMonth != newMonth) {
                    final int newYear = jarTableModel.getYear();
                    if (previousYear != newYear) {
                        recyclerItems.add(new MonthlyMonthRecyclerModel(jarTableModel.getTimestamp(), "" + newYear));
                    } else {
                        recyclerItems.add(new MonthlyMonthRecyclerModel(jarTableModel.getTimestamp()));
                    }
                }

                recyclerItems.add(new MonthlyJarRecyclerModel(jarTableModel));

                previousYear = jarTableModel.getYear();
                previousMonth = jarTableModel.getMonth();
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
        return (int) resources.getDimension(R.dimen.monthly_jar_size);
    }

    @Override
    protected boolean isIgnorableTransitionView(View transitionView) {
        return transitionView instanceof MonthlyMonthRecyclerModelView;
    }
}
