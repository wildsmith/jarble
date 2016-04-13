package com.wildsmith.jarble.ui.bitmap;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v4.util.LruCache;
import android.text.TextUtils;

/**
 * Leverages an {@link LruCache} to store {@link Bitmap}s.
 */
public class BitmapCacheManager {

    private static final int CACHE_MAX_MEMORY_DENOMINATOR = 8;

    private static LruCache<String, Bitmap> bitmapCache;

    static {
        initializeBitmapCache();
    }

    /**
     * This static method creates the LRU Bitmap Cache for the application
     */
    private static void initializeBitmapCache() {
        // Get max available VM memory, exceeding this amount will throw an OutOfMemory exception. Stored in kilobytes as LruCache takes
        // an int in its constructor.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / CACHE_MAX_MEMORY_DENOMINATOR;

        bitmapCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than number of items.
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    /**
     * This method adds a bitmap to the LRU Cache in a <Key,Value> pair combination into a LinkedHashMap
     *
     * @param key    Key of the bitmap should be unique
     * @param bitmap bitmap that should be added to the LRU Cache
     */
    public static void addBitmapToCache(String key, Bitmap bitmap) {
        if (TextUtils.isEmpty(key) || bitmap == null) {
            return;
        }

        synchronized (bitmapCache) {
            bitmapCache.put(key, bitmap);
        }
    }

    /**
     * This method adds a bitmap to the LRU Cache in a <Key,Value> pair combination into a LinkedHashMap
     *
     * @param key Key of the bitmap should be unique
     */
    public static void removeBitmapFromCache(String key) {
        if (TextUtils.isEmpty(key)) {
            return;
        }

        synchronized (bitmapCache) {
            bitmapCache.remove(key);
        }
    }

    /**
     * This method gets the Bitmap from the LRU Cache for the provided Key
     *
     * @param key Key of the Bitmap that needs to be fetched from the LRU Cache
     */
    @Nullable
    public static Bitmap getBitmapFromCache(String key) {
        synchronized (bitmapCache) {
            return bitmapCache.get(key);
        }
    }

    /**
     * Clears the LRU Bitmap Cache.
     */
    public static void clearBitmapCache() {
        synchronized (bitmapCache) {
            bitmapCache.evictAll();
        }
    }
}
