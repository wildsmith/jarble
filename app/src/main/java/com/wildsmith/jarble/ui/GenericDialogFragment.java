package com.wildsmith.jarble.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wildsmith.jarble.R;

import java.lang.ref.WeakReference;

public class GenericDialogFragment extends DialogFragment {

    public static final String TAG = GenericDialogFragment.class.getSimpleName();

    private String title, message;

    private WeakReference<Listener> listenerWeakReference;

    public void setContent(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public void setListener(Listener listener) {
        this.listenerWeakReference = new WeakReference<>(listener);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.generic_dialog_layout, container);

        TextView title = (TextView) view.findViewById(R.id.title);
        if (title != null) {
            title.setText(this.title);
        }

        TextView message = (TextView) view.findViewById(R.id.message);
        if (message != null) {
            message.setText(this.message);
        }

        View noButton = view.findViewById(R.id.left_button);
        if (noButton != null) {
            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Listener listener = getListener();
                    if (listener != null) {
                        listener.onRightButtonClicked();
                    }

                    dismiss();
                }
            });
        }
        View yesButton = view.findViewById(R.id.right_button);
        if (yesButton != null) {
            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Listener listener = getListener();
                    if (listener != null) {
                        listener.onLeftButtonClicked();
                    }

                    dismiss();
                }
            });
        }

        return view;
    }

    public Listener getListener() {
        return (listenerWeakReference != null && listenerWeakReference.get() != null) ? listenerWeakReference.get() : null;
    }

    public interface Listener {

        void onRightButtonClicked();

        void onLeftButtonClicked();
    }
}