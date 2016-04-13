package com.wildsmith.jarble.ui.jar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.wildsmith.jarble.R;
import com.wildsmith.jarble.provider.jar.JarTableMarbleModel;

import java.lang.ref.WeakReference;
import java.util.Locale;

public class MarbleInfoDialogFragment extends DialogFragment {

    static final String TAG = MarbleInfoDialogFragment.class.getSimpleName();

    enum Mode {
        VIEW,
        EDIT
    }

    private WeakReference<Listener> listenerWeakReference;

    private JarTableMarbleModel model;

    public void setListener(Listener listener) {
        this.listenerWeakReference = new WeakReference<>(listener);
    }

    public void setModel(JarTableMarbleModel model) {
        this.model = model;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.marble_info_dialog_layout, container);

        View purposeNotesInputContainer = view.findViewById(R.id.purpose_notes_input_container);
        View performanceNotesInputContainer = view.findViewById(R.id.performance_notes_input_container);
        final EditText purposeNotesInput = (EditText) view.findViewById(R.id.purpose_notes_input);
        final EditText performanceNotesInput = (EditText) view.findViewById(R.id.performance_notes_input);
        TextView purposeNotesTextView = (TextView) view.findViewById(R.id.purpose_notes);
        TextView performanceNotesTextView = (TextView) view.findViewById(R.id.performance_notes);
        TextView achievementTextView = (TextView) view.findViewById(R.id.achieved_subtitle);

        final String purposeNotes = model.getPurposeNotes();
        final String performanceNotes = model.getPerformanceNotes();

        final Mode mode;
        switch (JarTableMarbleModel.State.fromInt(model.getState())) {
            case IN_PROGRESS:
            case EDITING:
                mode = Mode.EDIT;
                break;
            default:
                mode = Mode.VIEW;
                break;
        }

        switch (mode) {
            case VIEW:
                if (purposeNotesTextView != null && !TextUtils.isEmpty(purposeNotes)) {
                    purposeNotesTextView.setText(purposeNotes);
                }
                if (performanceNotesTextView != null && !TextUtils.isEmpty(performanceNotes)) {
                    performanceNotesTextView.setText(performanceNotes);
                }
                if (purposeNotesInputContainer != null) {
                    purposeNotesInputContainer.setVisibility(View.GONE);
                }
                if (performanceNotesInputContainer != null) {
                    performanceNotesInputContainer.setVisibility(View.GONE);
                }
                if (achievementTextView != null) {
                    String achievement = (TextUtils.isEmpty(model.getColor())) ? getString(R.string.marble_info_not_achieved) :
                        getString(R.string.marble_info_achieved);
                    String achievementString = String.format(Locale.getDefault(), getString(R.string.marble_info_achievement_subtitle),
                        achievement);
                    achievementTextView.setText(achievementString);
                }
                break;
            case EDIT:
                if (purposeNotesTextView != null) {
                    purposeNotesTextView.setVisibility(View.GONE);
                }
                if (performanceNotesTextView != null) {
                    performanceNotesTextView.setVisibility(View.GONE);
                }
                if (purposeNotesInput != null && !TextUtils.isEmpty(purposeNotes)) {
                    purposeNotesInput.setText(purposeNotes);
                }
                if (performanceNotesInput != null && !TextUtils.isEmpty(performanceNotes)) {
                    performanceNotesInput.setText(performanceNotes);
                }
                if (achievementTextView != null) {
                    achievementTextView.setVisibility(View.GONE);
                }
                break;
        }

        View dismissButton = view.findViewById(R.id.dismiss_button);
        if (dismissButton != null) {
            dismissButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
        View okButton = view.findViewById(R.id.ok_button);
        if (okButton != null) {
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (mode) {
                        case EDIT:
                            final Listener listener = getListener();
                            if (purposeNotesInput == null || performanceNotesInput == null || listener == null) {
                                return;
                            }

                            model.setPurposeNotes(purposeNotesInput.getText().toString());
                            model.setPerformanceNotes(performanceNotesInput.getText().toString());
                            listener.onJarTableMarbleModelUpdated(model);
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

        void onJarTableMarbleModelUpdated(JarTableMarbleModel model);
    }
}