package com.wildsmith.utils;

import android.os.Handler;
import android.os.Looper;

public class ThreadUtils {

    private static Handler uiHandler;

    /**
     * Check to see whether current thread is also the UI Thread
     *
     * @return TRUE when current thread is the UI Thread
     */
    public static boolean isOnUiThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }

    /**
     * Determine if we are on the UI Thread or not.  If we are NOT on the UI Thread, we will execute the action in the current thread.  If
     * we are on the UI Thread, we will spawn a new thread to execute ACTION
     *
     * @param action procedure to run off the UI Thread
     */
    public static void runOffUiThread(final Runnable action) {
        if (isOnUiThread()) {
            runOnNewThread(action);
        } else {
            action.run();
        }
    }

    /**
     * Always spawn a new, non-UI thread process to execute ACTION
     *
     * @param action procedure to run on the new Thread
     */
    public static void runOnNewThread(final Runnable action) {
        final Runnable threadRunnable = new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    action.run();
                }
            }
        };

        synchronized (threadRunnable) {
            new Thread(threadRunnable).start();
        }
    }

    /**
     * Same as calling #postOnUiThread(action, 0, false)
     */
    public static void postOnUiThread(final Runnable action) {
        postOnUiThread(action, 0);
    }

    /**
     * Always post action to the UI Thread for execution
     *
     * @param action procedure to run on the UI Thread
     */
    public static void postOnUiThread(final Runnable action, final int delayTimeInMS) {
        if (uiHandler == null) {
            uiHandler = new Handler(Looper.getMainLooper());
        }

        uiHandler.postDelayed(action, delayTimeInMS);
    }

    public static void removeCallbacks(final Runnable action) {
        if (uiHandler == null) {
            uiHandler = new Handler(Looper.getMainLooper());
        }

        uiHandler.removeCallbacks(action);
    }
}
