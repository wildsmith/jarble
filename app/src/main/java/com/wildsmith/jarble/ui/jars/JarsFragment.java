package com.wildsmith.jarble.ui.jars;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.SharedElementCallback;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;

import com.wildsmith.jarble.BaseApplication;
import com.wildsmith.jarble.R;
import com.wildsmith.jarble.provider.jar.JarTableInMemoryCache;
import com.wildsmith.jarble.provider.jar.JarTableModel;
import com.wildsmith.jarble.ui.BaseFragment;
import com.wildsmith.jarble.ui.jar.SingleJarActivity;
import com.wildsmith.jarble.ui.recyclerview.dynamic.DynamicRecyclerAdapter;
import com.wildsmith.jarble.ui.recyclerview.dynamic.DynamicRecyclerModel;
import com.wildsmith.jarble.ui.recyclerview.dynamic.DynamicRecyclerModelView;
import com.wildsmith.jarble.utils.BroadcastHelper;
import com.wildsmith.jarble.utils.CollectionUtils;
import com.wildsmith.jarble.utils.GridAutofitLayoutManager;
import com.wildsmith.jarble.utils.ThreadUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public abstract class JarsFragment extends BaseFragment implements JarsModifiedBroadcastReceiver.Listener, AnimatingFragment,
    JarViewRecyclerModelListener {

    private static int orientation;

    private static int width;

    private static int height;

    private static int paddingLeft;

    private static int paddingTop;

    private static int paddingRight;

    private static int paddingBottom;

    private JarsModifiedBroadcastReceiver jarReceiver;

    protected RecyclerView recyclerView;

    protected DynamicRecyclerAdapter<DynamicRecyclerModel, DynamicRecyclerModelView> recyclerAdapter;

    protected List<String> transitionNames = new ArrayList<>();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        jarReceiver = new JarsModifiedBroadcastReceiver(this);
        BroadcastHelper.registerReceiver(context, jarReceiver, JarsModifiedBroadcastReceiver.IntentFilter.ON_JAR_CREATED.name(),
            JarsModifiedBroadcastReceiver.IntentFilter.ON_JAR_UPDATED.name());
    }

    @Override
    public void onDetach() {
        super.onDetach();

        BroadcastHelper.unregisterReceiver(getActivity(), jarReceiver);
    }

    protected void grabJarTableModelsForYear() {
        ThreadUtils.runOffUiThread(new Runnable() {
            @Override
            public void run() {
                final List<JarTableModel> jarTableModels = JarTableInMemoryCache.getJarTableModelsForYear(getContext(),
                    String.valueOf(BaseApplication.getCalendar(false).get(Calendar.YEAR)));

                synchronized (jarTableModels) {
                    final List<DynamicRecyclerModel> recyclerItemsFinal = convertYearlyTableModelsToRecyclerItems(jarTableModels);
                    ThreadUtils.postOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerAdapter.setRecyclerModels(recyclerItemsFinal);
                        }
                    });
                }
            }
        });
    }

    protected abstract List<DynamicRecyclerModel> convertYearlyTableModelsToRecyclerItems(List<JarTableModel> jarTableModelsForYear);

    @Override
    public void onJarsModified() {
        grabJarTableModelsForYear();
    }

    @TargetApi(21)
    @Override
    public void buildTransitionNames(@NonNull FragmentTransaction fragmentTransaction, int newSpanCount,
        final JarsFragment destinationFragment) {
        int previousSpan = -1;
        int row = -1;
        for (int index = 0; index < recyclerView.getChildCount(); index++) {
            View startingView = recyclerView.getChildAt(index);
            final int currentSpan = getCurrentSpan(startingView);
            if (previousSpan == -1) {
                previousSpan = currentSpan;
            }
            if (previousSpan >= currentSpan) {
                row++;
            }
            final String transitionName = "" + currentSpan + row;
            startingView.setTransitionName(transitionName);
            fragmentTransaction.addSharedElement(startingView, transitionName);
            transitionNames.add(transitionName);
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
                    if (index >= transitionNames.size()) {
                        break;
                    }

                    recyclerView.getChildAt(index).setTransitionName(transitionNames.get(index));
                }

                transitionNames.clear();
                return true;
            }
        });
    }

    protected int getCurrentSpan(@NonNull View view) {
        return (int) view.getX() / getColumnWidthFromChild(view.getResources());
    }

    protected abstract int getColumnWidthFromChild(Resources resources);

    public static int getSpanCount(int columnWidth) {
        return GridAutofitLayoutManager.buildSpanCount(columnWidth, orientation, width, height, paddingLeft, paddingTop, paddingRight,
            paddingBottom);
    }

    protected void setupRecyclerViewAnimationPointers() {
        final LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

        recyclerView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                recyclerView.getViewTreeObserver().removeOnPreDrawListener(this);

                orientation = layoutManager.getOrientation();
                width = layoutManager.getWidth();
                height = layoutManager.getHeight();
                paddingLeft = layoutManager.getPaddingLeft();
                paddingTop = layoutManager.getPaddingTop();
                paddingRight = layoutManager.getPaddingRight();
                paddingBottom = layoutManager.getPaddingBottom();
                return true;
            }
        });
    }

    public void setTransitionNames(List<String> transitionNames) {
        this.transitionNames = transitionNames;
    }

    @Override
    public void onJarClicked(JarTableModel model) {
        Intent intent = new Intent(getContext(), SingleJarActivity.class);
        intent.putExtra(SingleJarActivity.Extra.MODEL.name(), model);
        intent.putExtra(SingleJarActivity.Extra.MODE.name(), (model.isInProgress()) ? SingleJarActivity.Mode.EDIT :
            SingleJarActivity.Mode.VIEW);
        startActivity(intent);
    }
}