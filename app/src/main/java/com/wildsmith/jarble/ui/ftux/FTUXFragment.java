package com.wildsmith.jarble.ui.ftux;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wildsmith.jarble.R;
import com.wildsmith.jarble.preferences.JarblePreferencesHelper;
import com.wildsmith.jarble.ui.BaseFragment;
import com.wildsmith.jarble.ui.jars.JarsActivity;

public class FTUXFragment extends BaseFragment {

    public static final String TAG = FTUXFragment.class.getSimpleName();

    public static FTUXFragment newInstance() {
        return new FTUXFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ftux_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView message = (TextView) view.findViewById(R.id.message);
        if (message != null) {
            message.setText(Html.fromHtml(getString(R.string.description_info)));
        }

        View continueButton = view.findViewById(R.id.continue_button);
        if (continueButton != null) {
            continueButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    continueFlow();
                }
            });
        }
    }

    private void continueFlow() {
        startActivity(new Intent(getContext(), JarsActivity.class));
        getActivity().finish();
        JarblePreferencesHelper.setFTUX(getContext(), false);
    }
}