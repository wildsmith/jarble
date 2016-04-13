package com.wildsmith.jarble.ui.bitmap;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Assists with loading {@link Bitmap}s off of the ui thread. Once the {@link Bitmap} has been loaded, it will be assigned to the passed
 * {@link ImageView}. For {@link android.widget.ListView}s, {@link android.widget.GridView}s, {@link
 * android.support.v7.widget.RecyclerView}s, and other views that recycle their items, there is an added check to ensure that the image is
 * NOT assigned to the {@link ImageView} that has been recycled and has hence fetched a new/different {@link Bitmap} from {@link
 * Resources}.
 * Loaded {@link Bitmap}s will also be added to an image cache. NOTE: the cache is kept per instance of this class.
 * <p/>
 * For more information:
 *
 * @see <a href="http://developer.android.com/training/displaying-bitmaps/process-bitmap.html">Processing Bitmaps Off the UI Thread</a>
 * @see <a href="http://developer.android.com/training/displaying-bitmaps/cache-bitmap.html">Caching Bitmaps</a>
 */
public class BitmapLoader {

    private static final String TAG = BitmapLoader.class.getSimpleName();

    private static final int DEFAULT_REQUESTED_BITMAP_WIDTH = 0;

    private static final int DEFAULT_REQUESTED_BITMAP_HEIGHT = 0;

    private static final int PLACE_HOLDER_BITMAP_WIDTH = 1;

    private static final int PLACE_HOLDER_BITMAP_HEIGHT = 1;

    private enum BitmapSource {
        ARRAY,
        RESOURCE,
        FILE
    }

    private enum Index {
        BITMAP_SOURCE,
        RESOURCE_ID,
        IMAGE_BYTE_ARRAY,
        IMAGE_NAME,
        IMAGE_PATH,
        LOADER_CALLBACK
    }

    private static Bitmap placeHolderBitmap;

    /**
     * Loads {@link Bitmap}s off of the ui thread. Once the {@link Bitmap} has been loaded, it will be assigned to the passed {@link
     * ImageView}. For {@link android.widget.ListView}s, {@link android.widget.GridView}s, {@link android.support.v7.widget.RecyclerView}s,
     * and other views that recycle their items, there is an added check to ensure that the image is NOT assigned to the {@link ImageView}
     * that has been recycled and has hence fetched a new/different {@link Bitmap} from {@link Resources}. Loaded {@link Bitmap}s will also
     * be added to an image cache.
     *
     * @param resources  used to pull the desired {@link Bitmap} from resources
     * @param resourceId used to pull the desired {@link Bitmap} from resources
     * @param imageView  to assign the {@link Bitmap} to after it has been loaded from resources
     * @param width      the specific width desired for the {@link Bitmap}. Specifying this will help to sample the {@link Bitmap} and
     *                   ultimately conserve system memory.
     * @param height     the specific height desired for the {@link Bitmap}. Specifying this will help to sample the {@link Bitmap} and
     *                   ultimately conserve system memory.
     */
    private void loadBitmap(Bitmap placeHolderBitmap, @NonNull BitmapSource source, @NonNull Resources resources,
        @DrawableRes int resourceId, byte[] imageByteArray, String imageName, String imagePath, @NonNull ImageView imageView, int width,
        int height, LoaderCallback callback) {
        final Bitmap bitmap = BitmapCacheManager.getBitmapFromCache(getCacheKey(resourceId, imageName));
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else if (cancelPotentialWork(getCacheKey(resourceId, imageName), imageView)) {
            final BitmapWorkerTask task = new BitmapWorkerTask(imageView, width, height);
            imageView.setImageDrawable(new AsyncDrawable(resources, getPlaceHolderBitmap(placeHolderBitmap), task));
            task.execute(source, resourceId, imageByteArray, imageName, imagePath, callback);
        }
    }

    /**
     * @return the key used to cache the image. If an imageName was specified, then return that, otherwise return the resourceId.
     */
    private static String getCacheKey(@DrawableRes int resourceId, String imageName) {
        return (TextUtils.isEmpty(imageName)) ? String.valueOf(resourceId) : imageName;
    }

    /**
     * @return the default {@link #placeHolderBitmap}, which is a simple 1x1 {@link Bitmap} with no contents.
     */
    private Bitmap getPlaceHolderBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            return bitmap;
        }

        return (placeHolderBitmap != null) ? placeHolderBitmap :
            (placeHolderBitmap = Bitmap.createBitmap(PLACE_HOLDER_BITMAP_WIDTH, PLACE_HOLDER_BITMAP_HEIGHT, Bitmap.Config.ARGB_8888));
    }

    /**
     * Cancel any currently executing {@link Bitmap} loading that differs from the current load request for the given {@link ImageView}.
     */
    private static boolean cancelPotentialWork(String cacheKey, @NonNull ImageView imageView) {
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
        if (bitmapWorkerTask != null) {
            final String bitmapResourceId = getCacheKey(bitmapWorkerTask.resourceId, bitmapWorkerTask.imageName);
            // If bitmapResourceId is not yet set or it differs from the new resourceId
            if ("0".equals(bitmapResourceId) || !bitmapResourceId.equals(cacheKey)) {
                // Cancel previous task
                bitmapWorkerTask.cancel(true);
            } else {
                // The same work is already in progress
                return false;
            }
        }
        // No task associated with the ImageView, or an existing task was cancelled
        return true;
    }

    @Nullable
    private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                return ((AsyncDrawable) drawable).getBitmapWorkerTask();
            }
        }
        return null;
    }

    /**
     * Builder used to construct the correct {@link Bitmap} loader configurations. The load of the {@link Bitmap} doesn't occur until
     * {@link
     * #loadBitmap()} has been called.
     */
    public static class Builder {

        private enum BUILDER_KEYS {
            PLACEHOLDER_BITMAP,
            RESOURCES,
            RESOURCE_ID,
            IMAGE_BYTE_ARRAY,
            IMAGE_NAME,
            IMAGE_PATH,
            IMAGE_VIEW,
            WIDTH,
            HEIGHT,
            LOADER_CALLBACK
        }

        private Map<BUILDER_KEYS, Object> dataMap = new HashMap<>(10);

        /**
         * It is important to note that a placeholder {@link Bitmap} is not required but may be preferred in some situations. Ensure that
         * the load time for the placeholder is accounted for either by utilizing {@link BitmapLoader} to pull it from resources OR that
         * the
         * {@link Bitmap} itself is not large (hence the upfront cost of loading it is low).
         */
        public Builder setPlaceHolderBitmap(Bitmap bitmap) {
            dataMap.put(BUILDER_KEYS.PLACEHOLDER_BITMAP, bitmap);
            return this;
        }

        public Builder setResources(Resources resources) {
            dataMap.put(BUILDER_KEYS.RESOURCES, resources);
            return this;
        }

        public Builder setResourceId(@DrawableRes int resourceId) {
            dataMap.put(BUILDER_KEYS.RESOURCE_ID, resourceId);
            return this;
        }

        public Builder setImageByteArray(byte[] imageByteArray) {
            dataMap.put(BUILDER_KEYS.IMAGE_BYTE_ARRAY, imageByteArray);
            return this;
        }

        public Builder setImageName(String imageName) {
            dataMap.put(BUILDER_KEYS.IMAGE_NAME, imageName);
            return this;
        }

        public Builder setImagePath(@NonNull String imagePath) {
            dataMap.put(BUILDER_KEYS.IMAGE_PATH, imagePath);
            return this;
        }

        public Builder setImageView(@NonNull ImageView imageView) {
            dataMap.put(BUILDER_KEYS.IMAGE_VIEW, imageView);
            return this;
        }

        public Builder setWidth(int width) {
            dataMap.put(BUILDER_KEYS.WIDTH, width);
            return this;
        }

        public Builder setHeight(int height) {
            dataMap.put(BUILDER_KEYS.HEIGHT, height);
            return this;
        }

        public Builder setLoaderCallback(LoaderCallback callback) {
            dataMap.put(BUILDER_KEYS.LOADER_CALLBACK, callback);
            return this;
        }

        public void loadBitmap() {
            Bitmap placeholderBitmap = null;
            Resources resources = null;
            int resourceId = 0;
            String imageName = null;
            byte[] imageByteArray = null;
            BitmapSource source = BitmapSource.RESOURCE;
            String imagePath = null;
            ImageView imageView = null;
            int width = DEFAULT_REQUESTED_BITMAP_WIDTH;
            int height = DEFAULT_REQUESTED_BITMAP_HEIGHT;
            LoaderCallback callback = null;

            for (Map.Entry<BUILDER_KEYS, Object> entry : dataMap.entrySet()) {
                switch (entry.getKey()) {
                    case PLACEHOLDER_BITMAP:
                        placeholderBitmap = (Bitmap) dataMap.get(BUILDER_KEYS.PLACEHOLDER_BITMAP);
                        break;
                    case RESOURCES:
                        resources = (Resources) dataMap.get(BUILDER_KEYS.RESOURCES);
                        break;
                    case RESOURCE_ID:
                        resourceId = (int) dataMap.get(BUILDER_KEYS.RESOURCE_ID);
                        break;
                    case IMAGE_BYTE_ARRAY:
                        imageByteArray = (byte[]) dataMap.get(BUILDER_KEYS.IMAGE_BYTE_ARRAY);
                        source = BitmapSource.ARRAY;
                        break;
                    case IMAGE_NAME:
                        imageName = (String) dataMap.get(BUILDER_KEYS.IMAGE_NAME);
                        break;
                    case IMAGE_PATH:
                        imagePath = (String) dataMap.get(BUILDER_KEYS.IMAGE_PATH);
                        source = BitmapSource.FILE;
                        break;
                    case IMAGE_VIEW:
                        imageView = (ImageView) dataMap.get(BUILDER_KEYS.IMAGE_VIEW);
                        break;
                    case WIDTH:
                        width = (int) dataMap.get(BUILDER_KEYS.WIDTH);
                        break;
                    case HEIGHT:
                        height = (int) dataMap.get(BUILDER_KEYS.HEIGHT);
                        break;
                    case LOADER_CALLBACK:
                        callback = (LoaderCallback) dataMap.get(BUILDER_KEYS.LOADER_CALLBACK);
                        break;
                }
            }

            if (resources == null) {
                throw new IllegalArgumentException("Missing valid Resources argument.");
            }

            if (resourceId == 0 && TextUtils.isEmpty(imageName)) {
                throw new IllegalArgumentException("Missing valid resource id and image name argument.");
            }

            if (imageView == null) {
                throw new IllegalArgumentException("Missing valid ImageView argument.");
            }

            new BitmapLoader().loadBitmap(placeholderBitmap, source, resources, resourceId, imageByteArray, imageName, imagePath,
                imageView, width, height, callback);
        }
    }

    private class BitmapWorkerTask extends AsyncTask<Object, Void, Bitmap> {

        private final WeakReference<ImageView> imageViewReference;

        private int width, height;

        private int resourceId;

        private byte[] imageByteArray;

        private String imageName;

        private WeakReference<LoaderCallback> callbackWeakReference;

        public BitmapWorkerTask(@NonNull ImageView imageView, int width, int height) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new WeakReference<>(imageView);
            this.width = width;
            this.height = height;
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(Object... params) {
            resourceId = (int) params[Index.RESOURCE_ID.ordinal()];
            imageByteArray = (byte[]) params[Index.IMAGE_BYTE_ARRAY.ordinal()];
            imageName = (String) params[Index.IMAGE_NAME.ordinal()];
            callbackWeakReference = new WeakReference<>((LoaderCallback) params[Index.LOADER_CALLBACK.ordinal()]);

            Bitmap bitmap = null;
            switch ((BitmapSource) params[Index.BITMAP_SOURCE.ordinal()]) {
                case ARRAY:
                    bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
                    break;
                case RESOURCE:
                    bitmap = decodeSameBitmapFromResource(resourceId);
                    break;
                case FILE:
                    try {
                        BitmapUtils
                            .decodeSampledBitmapFromFile((String) params[Index.IMAGE_PATH.ordinal()], width, height);
                    } catch (Exception e) {
                        if (resourceId != 0) {
                            Log.d(TAG, "Could not load the bitmap from file. Attempting to use resourceId.", e);
                            bitmap = decodeSameBitmapFromResource(resourceId);
                        } else {
                            Log.d(TAG, "Could not load the bitmap from file.", e);
                        }
                    }
                    break;
            }

            BitmapCacheManager.addBitmapToCache(getCacheKey(resourceId, imageName), bitmap);

            return bitmap;
        }

        // Use resources to retrieve the bitmap
        private Bitmap decodeSameBitmapFromResource(int resourceId) {
            final Resources resources = getResources();
            if (resources != null) {
                return BitmapUtils.decodeSampledBitmapFromResource(resources, resourceId, width, height);
            }
            return null;
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }

            if (bitmap != null) {
                final ImageView imageView = imageViewReference.get();
                final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
                if (this == bitmapWorkerTask) {
                    imageView.setImageBitmap(bitmap);
                }
            }

            if (callbackWeakReference != null && callbackWeakReference.get() != null) {
                callbackWeakReference.get().onSetImageBitmapComplete(bitmap);
            }
        }

        @Nullable
        private Resources getResources() {
            final ImageView imageView = imageViewReference.get();
            return (imageView != null) ? imageView.getResources() : null;
        }
    }

    /**
     * Dedicated {@link Drawable} subclass to store a reference back to {@link BitmapWorkerTask}. In this case, a {@link BitmapDrawable} is
     * used so that a placeholder image can be displayed in the {@link ImageView} while the load task completes.
     */
    private static class AsyncDrawable extends BitmapDrawable {

        private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

        public AsyncDrawable(Resources resources, Bitmap bitmap, BitmapWorkerTask bitmapWorkerTask) {
            super(resources, bitmap);
            bitmapWorkerTaskReference = new WeakReference<>(bitmapWorkerTask);
        }

        @Nullable
        public BitmapWorkerTask getBitmapWorkerTask() {
            return bitmapWorkerTaskReference.get();
        }
    }

    public static abstract class LoaderCallback {

        public void onSetImageBitmapComplete(Bitmap bitmap) {

        }
    }
}
