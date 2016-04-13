package com.wildsmith.jarble.ui.jars.yearly;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.wildsmith.jarble.R;
import com.wildsmith.jarble.provider.jar.JarTableModel;
import com.wildsmith.jarble.ui.bitmap.BitmapLoader;
import com.wildsmith.jarble.ui.jars.JarViewRecyclerModelListener;

import java.util.List;

class YearlyMonthGridViewAdapter extends BaseAdapter {

    private static final String TAG = YearlyMonthGridViewAdapter.class.getSimpleName();

    private Context context;

    private List<JarTableModel> jars;

    private JarViewRecyclerModelListener listener;

    public YearlyMonthGridViewAdapter(Context context) {
        this.context = context;
    }

    public int getCount() {
        return jars.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.yearly_jar_layout, parent, false);
        }

        final JarTableModel tableModel = jars.get(position);
        if (tableModel == null) {
            return convertView;
        }

        setupImageButtonSource(convertView.getResources(), convertView, tableModel);
        setupProgressIndicator(convertView, tableModel);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onJarClicked(tableModel);
            }
        });

        return convertView;
    }

    private void setupProgressIndicator(View view, JarTableModel model) {
        FloatingActionButton floatingActionButton = (FloatingActionButton) view.findViewById(R.id.floating_action_button);
        if (floatingActionButton == null) {
            return;
        }

        if (model.isInProgress()) {
            floatingActionButton.setImageResource(R.drawable.jar_in_progress_background);
        } else {
            floatingActionButton.setImageResource(R.drawable.icon_puzzle_piece);
        }
    }

    private void setupImageButtonSource(Resources resources, View view, JarTableModel model) {
        FloatingActionButton floatingActionButton = (FloatingActionButton) view.findViewById(R.id.floating_action_button);
        if (floatingActionButton == null) {
            return;
        }

        if (model == null || model.getImage() == null || model.getImage().length == 0) {
            return;
        }

//        imageView.post(new BitmapLoaderRunnable(resources, model, floatingActionButton));
    }

    private class BitmapLoaderRunnable implements Runnable {

        public Resources resources;

        private JarTableModel model;

        private ImageView imageView;

        BitmapLoaderRunnable(Resources resources, JarTableModel model, ImageView imageView) {
            this.resources = resources;
            this.model = model;
            this.imageView = imageView;
        }

        @Override
        public void run() {
            new BitmapLoader.Builder().setResources(resources)
                .setPlaceHolderBitmap(BitmapFactory.decodeResource(resources, R.drawable.icon_puzzle_piece)).
                setImageName(model.getTimestamp() + TAG).setImageByteArray(model.getImage()).setImageView(imageView)
                .setWidth(imageView.getWidth()).setHeight(imageView.getHeight()).loadBitmap();
        }
    }

    ;

    public void setJars(List<JarTableModel> jars) {
        this.jars = jars;
    }

    public void setListener(JarViewRecyclerModelListener listener) {
        this.listener = listener;
    }
}