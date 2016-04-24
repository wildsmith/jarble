package com.wildsmith.jarble.ui.jars;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewAnimationUtils;

import com.wildsmith.bitmap.BitmapCacheManager;
import com.wildsmith.bitmap.BitmapLoader;
import com.wildsmith.jarble.R;
import com.wildsmith.jarble.jar.JarTableModel;
import com.wildsmith.recyclerview.dynamic.DynamicRecyclerModelView;

import java.util.HashMap;
import java.util.Map;

public class JarFloatingActionButton extends FloatingActionButton
    implements DynamicRecyclerModelView<JarViewRecyclerModel, JarViewRecyclerModelListener> {

    private JarTableModel model;

    public JarFloatingActionButton(Context context) {
        this(context, null);
    }

    public JarFloatingActionButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JarFloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setListener(final JarViewRecyclerModelListener listener) {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onJarClicked(model);
            }
        });
    }

    @Override
    public void populateView(@NonNull JarViewRecyclerModel model, int position, int size) {
        this.model = model.getJarTableModel();

        setupProgressIndicator(model.getJarTableModel());
        setupImageViewBitmap(model.getJarTableModel());
    }

    private void setupProgressIndicator(JarTableModel model) {
        if (model.isInProgress()) {
            setImageResource(R.drawable.jar_in_progress_background);
        } else {
            setImageResource(R.drawable.icon_puzzle_piece);
        }
    }

    private void setupImageViewBitmap(JarTableModel model) {
        if (model == null || model.getImage() == null || model.getImage().length == 0) {
            return;
        }

        // Short circuit the bitmap loader to avoid loading the bitmap off of the main thread when the bitmap already exists in the cache
        final Bitmap bitmap = BitmapCacheManager.getBitmapFromCache(model.getTimestamp());
        if (bitmap != null) {
            setImageBitmap(bitmap);
            setBackgroundTintList(PaletteColorStateCache.getColorStateList(model.getTimestamp()));
        } else {
            post(bitmapLoader);
        }
    }

    @TargetApi(21)
    @Override
    public void applyAnimation(int position, int size) {
//        setVisibility(View.INVISIBLE);
//        post(revealRunnable);
    }

    private Runnable bitmapLoader = new Runnable() {
        @Override
        public void run() {
            new BitmapLoader.Builder().setResources(getResources())
                .setPlaceHolderBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_puzzle_piece))
                .setImageName(model.getTimestamp()).setImageByteArray(model.getImage())
                .setImageView(JarFloatingActionButton.this).setWidth(getWidth()).setHeight(getHeight())
                .setLoaderCallback(new BitmapLoader.LoaderCallback() {
                    @Override
                    public void onSetImageBitmapComplete(@NonNull Bitmap bitmap) {
                        grabBackgroundColor(model.getTimestamp(), bitmap);
                    }
                })
                .loadBitmap();
        }
    };

    private void grabBackgroundColor(@NonNull final String timestamp, @NonNull Bitmap bitmap) {
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            public void onGenerated(Palette palette) {
                ColorStateList colorStateList = ColorStateList.valueOf(getPaletteColor(palette));
                PaletteColorStateCache.addPaletteCache(timestamp, colorStateList);
                setBackgroundTintList(colorStateList);
            }
        });
    }

    private int getPaletteColor(@NonNull Palette palette) {
        final int defaultColor = ContextCompat.getColor(getContext(), R.color.medium_green);
        int paletteColor = palette.getLightVibrantColor(defaultColor);
        if (paletteColor == defaultColor) {
            paletteColor = palette.getLightVibrantColor(defaultColor);
        }
        if (paletteColor == defaultColor) {
            paletteColor = palette.getVibrantColor(defaultColor);
        }
        if (paletteColor == defaultColor) {
            paletteColor = palette.getLightMutedColor(defaultColor);
        }
        if (paletteColor == defaultColor) {
            paletteColor = palette.getVibrantColor(defaultColor);
        }
        if (paletteColor == defaultColor) {
            paletteColor = palette.getDarkVibrantColor(defaultColor);
        }
        if (paletteColor == defaultColor) {
            paletteColor = palette.getDarkMutedColor(defaultColor);
        }
        return paletteColor;
    }

    private Runnable revealRunnable = new Runnable() {
        @TargetApi(21)
        @Override
        public void run() {
            // get the center for the clipping circle
            int centerX = getMeasuredWidth() / 2;
            int centerY = getMeasuredHeight() / 2;

            // get the final radius for the clipping circle
            int finalRadius = Math.max(getWidth(), getHeight()) / 2;

            // create the animator for this view (the start radius is zero)
            ViewAnimationUtils.createCircularReveal(JarFloatingActionButton.this, centerX, centerY, 0, finalRadius).start();
            setVisibility(View.VISIBLE);
        }
    };

    private static class PaletteColorStateCache {

        private static final Map<String, ColorStateList> paletteCache = new HashMap<>();

        public static ColorStateList getColorStateList(@NonNull String timestamp) {
            synchronized (paletteCache) {
                return paletteCache.get(timestamp);
            }
        }

        public static void addPaletteCache(String timestamp, ColorStateList colorStateList) {
            synchronized (paletteCache) {
                paletteCache.put(timestamp, colorStateList);
            }
        }
    }
}