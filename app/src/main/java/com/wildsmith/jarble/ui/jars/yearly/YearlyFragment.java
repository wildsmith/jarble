package com.wildsmith.jarble.ui.jars.yearly;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.SharedElementCallback;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.wildsmith.jarble.R;
import com.wildsmith.jarble.jar.JarTableModel;
import com.wildsmith.jarble.ui.jars.JarsFragment;
import com.wildsmith.recyclerview.dynamic.DynamicRecyclerAdapter;
import com.wildsmith.recyclerview.dynamic.DynamicRecyclerModel;
import com.wildsmith.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class YearlyFragment extends JarsFragment {

    public static final String TAG = YearlyFragment.class.getSimpleName();

    public static YearlyFragment newInstance() {
        return new YearlyFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return YearlyViewWrapper.onCreateView(inflater.inflate(R.layout.recycler_view_linear, container, false));
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

        if (recyclerAdapter == null) {
            recyclerAdapter = new DynamicRecyclerAdapter<>(this);
        }

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

            List<JarTableModel> monthlyJarTableModels = new ArrayList<>();
            int previousYear = -1;
            int previousMonth = -1;
            for (JarTableModel jarTableModel : jarTableModels) {
                if (previousYear == -1 || previousMonth == -1) {
                    previousYear = jarTableModel.getYear();
                    recyclerItems.add(new YearlyYearRecyclerModel("" + previousYear));

                    previousMonth = jarTableModel.getMonth();
                    recyclerItems.add(new YearlyMonthRecyclerModel(jarTableModel.getTimestamp(), monthlyJarTableModels));
                }

                final int newYear = jarTableModel.getYear();
                if (previousYear != newYear) {
                    recyclerItems.add(new YearlyYearRecyclerModel("" + newYear));
                }

                final int newMonth = jarTableModel.getMonth();
                if (previousMonth != newMonth) {
                    recyclerItems.add(new YearlyMonthRecyclerModel(jarTableModel.getTimestamp(), monthlyJarTableModels));
                    monthlyJarTableModels = new ArrayList<>();
                } else {
                    monthlyJarTableModels.add(jarTableModel);
                }

                previousYear = jarTableModel.getYear();
                previousMonth = jarTableModel.getMonth();
            }
        }

        return recyclerItems;
    }

    @Override
    public int getColumnWidthFromChild(Resources resources) {
        return getColumnWidth(resources);
    }

    public static int getColumnWidth(@NonNull Resources resources) {
        return (int) resources.getDimension(R.dimen.yearly_jars_gridview_jar_size);
    }

    @TargetApi(21)
    @Override
    public void buildTransitionNames(@NonNull FragmentTransaction fragmentTransaction, int newSpanCount,
        final JarsFragment destinationFragment) {

        for (int index = 0; index < recyclerView.getChildCount(); index++) {
            View child = recyclerView.getChildAt(index);
            if (child instanceof YearlyMonthRecyclerModelView) {
                int previousSpan = -1;
                int row = -1;
                for (int monthIndex = 0; monthIndex < ((YearlyMonthRecyclerModelView) child).getChildCount(); monthIndex++) {
                    View monthChild = ((YearlyMonthRecyclerModelView) child).getChildAt(monthIndex);
                    if (monthChild instanceof TextView) {
                        final String transitionName = "0" + row;
                        monthChild.setTransitionName(transitionName);
                        fragmentTransaction.addSharedElement(monthChild, transitionName);
                        transitionNames.add(transitionName);
                        row++;
                    } else if (monthChild instanceof YearlyMonthGridView) {
                        for (int gridIndex = 0; gridIndex < ((YearlyMonthGridView) monthChild).getChildCount(); gridIndex++) {
                            View gridChild = ((YearlyMonthGridView) monthChild).getChildAt(gridIndex);
                            final int currentSpan = getCurrentSpan(gridChild);
                            if (previousSpan == -1) {
                                previousSpan = currentSpan;
                            }
                            if (previousSpan >= currentSpan) {
                                row++;
                            }
                            final String transitionName = "" + currentSpan + row;
                            gridChild.setTransitionName(transitionName);
                            fragmentTransaction.addSharedElement(gridChild, transitionName);
                            transitionNames.add(transitionName);
                        }
                        break;
                    }
                }
                break;
            }
        }

        destinationFragment.setTransitionNames(transitionNames);

        final View sceneRoot = getActivity().findViewById(R.id.content);
        destinationFragment.setEnterSharedElementCallback(new SharedElementCallback() {
            @Override
            public void onSharedElementStart(List<String> sharedElementNames, List<View> sharedElements,
                List<View> sharedElementSnapshots) {
                destinationFragment.reBuildTransitionNames(sceneRoot);
            }
        });
    }

    @TargetApi(21)
    @Override
    public void reBuildTransitionNames(final View sceneRoot) {
        if (sceneRoot == null || CollectionUtils.isEmpty(transitionNames) || recyclerView == null) {
            return;
        }

        sceneRoot.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                sceneRoot.getViewTreeObserver().removeOnPreDrawListener(this);

                for (int index = 0; index < recyclerView.getChildCount(); index++) {
                    View child = recyclerView.getChildAt(index);
                    if (child instanceof YearlyMonthRecyclerModelView) {
                        for (int monthIndex = 0; monthIndex < ((YearlyMonthRecyclerModelView) child).getChildCount(); monthIndex++) {
                            View monthChild = ((YearlyMonthRecyclerModelView) child).getChildAt(monthIndex);
                            if (monthChild instanceof TextView) {
                                monthChild.setTransitionName(transitionNames.get(monthIndex));
                                transitionNames.remove(monthIndex);
                            } else if (monthChild instanceof YearlyMonthGridView) {
                                for (int gridIndex = 0; gridIndex < ((YearlyMonthGridView) monthChild).getChildCount(); gridIndex++) {
                                    if (gridIndex >= transitionNames.size()) {
                                        break;
                                    }

                                    View gridChild = ((YearlyMonthGridView) monthChild).getChildAt(gridIndex);
                                    gridChild.setTransitionName(transitionNames.get(gridIndex));
                                }
                                break;
                            }
                        }
                        break;
                    }
                }

                transitionNames.clear();
                return true;
            }
        });
    }

    //TODO switch out of using a GridView and simply use the GridLayoutManager
    @Override
    protected boolean isIgnorableTransitionView(View transitionView) {
        return transitionView instanceof YearlyMonthRecyclerModelView || transitionView instanceof YearlyYearRecyclerModelView;
    }
}
