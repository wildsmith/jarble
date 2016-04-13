package com.wildsmith.jarble.ui.info;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wildsmith.jarble.R;
import com.wildsmith.jarble.ui.BaseFragment;

public class InfoFragment extends BaseFragment {

    public static final String TAG = InfoFragment.class.getSimpleName();

    public enum Extras {
        INFO
    }

    private String info;

    public static InfoFragment newInstance(Bundle bundle) {
        InfoFragment infoFragment = new InfoFragment();
        infoFragment.setArguments(bundle);
        return infoFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setInfoFromArguments();
    }

    private void setInfoFromArguments() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            info = arguments.getString(Extras.INFO.name());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.info_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView infoTextView = (TextView) view.findViewById(R.id.info_text_view);
        if (infoTextView != null) {
            infoTextView.setText(Html.fromHtml(info));
        }
    }
}