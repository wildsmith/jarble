package com.wildsmith.jarble.ui.jar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wildsmith.jarble.R;
import com.wildsmith.jarble.jar.JarTableInMemoryCache;
import com.wildsmith.jarble.jar.JarTableModel;
import com.wildsmith.jarble.marble.MarbleTableInteractionHelper;
import com.wildsmith.jarble.marble.MarbleTableModel;
import com.wildsmith.jarble.ui.BaseFragment;
import com.wildsmith.jarble.ui.jars.JarsModifiedBroadcastReceiver;
import com.wildsmith.utils.BroadcastHelper;

public class SingleJarFragment extends BaseFragment implements SingleJarView.Listener, MarbleInfoDialogFragment.Listener,
    JarsModifiedBroadcastReceiver.Listener {

    public static final String TAG = SingleJarFragment.class.getSimpleName();

    private JarsModifiedBroadcastReceiver jarReceiver;

    private JarTableModel model;

    private Listener listener;

    public static SingleJarFragment newInstance(Bundle bundle) {
        SingleJarFragment fragment = new SingleJarFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (Listener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement Listener");
        }

        jarReceiver = new JarsModifiedBroadcastReceiver(this);
        BroadcastHelper.registerReceiver(context, jarReceiver, JarsModifiedBroadcastReceiver.IntentFilter.ON_JAR_CREATED.name(),
            JarsModifiedBroadcastReceiver.IntentFilter.ON_JAR_UPDATED.name());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        setModelFromArguments();
    }

    private void setModelFromArguments() {
        Bundle arguments = getArguments();
        if (arguments == null) {
            return;
        }

        model = arguments.getParcelable(SingleJarActivity.Extra.MODEL.name());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return SingleJarViewWrapper.onCreateView(inflater.inflate(R.layout.jarble_fragment_layout, container, false));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listener.setupBottomSheetViews();

        setupAvailableMarbles();
    }

    private void setupAvailableMarbles() {
        SingleJarView jarView = SingleJarViewWrapper.findViewById(R.id.single_jar_view);
        if (jarView == null) {
            return;
        }

        jarView.setListener(this);
        jarView.buildViewUsingModel(model);
    }

    @Override
    public void onDetach() {
        super.onDetach();

        BroadcastHelper.unregisterReceiver(getActivity(), jarReceiver);
    }

    @Override
    public void onDestroyView() {
        SingleJarViewWrapper.onDestroyView();

        super.onDestroyView();
    }

    @Override
    public void onMarbleClicked(@NonNull MarbleTableModel model, @NonNull FloatingActionButton marbleView) {
        switch (MarbleTableModel.State.fromInt(model.getState())) {
            case EDITING:
                listener.onEditableMarbleClicked(model, marbleView);
                break;
            default:
                MarbleInfoDialogFragment dialogFragment = new MarbleInfoDialogFragment();
                dialogFragment.setListener(this);
                dialogFragment.setModel(model);
                dialogFragment.show(getActivity().getSupportFragmentManager(), MarbleInfoDialogFragment.TAG);
                break;
        }
    }

    @Override
    public void onJarTableMarbleModelUpdated(MarbleTableModel model) {
        this.model.update(model);
        MarbleTableInteractionHelper.updateMarbleTableModels(getContext(), this.model.getMarbles());

        BroadcastHelper.sendBroadcast(getContext(), new Intent(JarsModifiedBroadcastReceiver.IntentFilter.ON_JAR_UPDATED.name()));
    }

    @Override
    public void onJarsModified() {
        model = JarTableInMemoryCache.getUpdatedJarTableModel(getContext(), model);
        setupAvailableMarbles();
    }

    interface Listener {

        void onEditableMarbleClicked(@NonNull MarbleTableModel model, @NonNull FloatingActionButton marbleView);

        //Fixes some issues with the component not measuring until later
        void setupBottomSheetViews();
    }
}