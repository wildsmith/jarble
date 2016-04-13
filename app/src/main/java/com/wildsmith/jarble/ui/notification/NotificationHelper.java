package com.wildsmith.jarble.ui.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import com.wildsmith.jarble.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationHelper {

    private enum BUILDER_KEYS {
        ID,
        ICON,
        COLOR,
        TITLE,
        TEXT,
        STYLE,
        ACTIVITY_TO_LAUNCH,
        NOTIFICATION_FLAGS,
        CONTENT_INTENT,
        ACTIONS
    }

    public static class Builder {

        private Map<BUILDER_KEYS, Object> dataMap = new HashMap<>();

        public Builder setId(int notificationId) {
            dataMap.put(BUILDER_KEYS.ID, notificationId);
            return this;
        }

        public Builder setIcon(@DrawableRes int iconResourceId) {
            dataMap.put(BUILDER_KEYS.ICON, iconResourceId);
            return this;
        }

        public Builder setColor(@ColorInt int color) {
            dataMap.put(BUILDER_KEYS.COLOR, color);
            return this;
        }

        public Builder setTitle(@NonNull String title) {
            dataMap.put(BUILDER_KEYS.TITLE, title);
            return this;
        }

        public Builder setText(@NonNull String text) {
            dataMap.put(BUILDER_KEYS.TEXT, text);
            return this;
        }

        public Builder setStyle(@NonNull NotificationCompat.Style style) {
            dataMap.put(BUILDER_KEYS.STYLE, style);
            return this;
        }

        public Builder setActivityToLaunch(@NonNull Class<?> clazz) {
            dataMap.put(BUILDER_KEYS.ACTIVITY_TO_LAUNCH, clazz);
            return this;
        }

        public Builder setNotificationFlags(int flags) {
            dataMap.put(BUILDER_KEYS.NOTIFICATION_FLAGS, flags);
            return this;
        }

        public Builder setContentIntent(PendingIntent intent) {
            dataMap.put(BUILDER_KEYS.CONTENT_INTENT, intent);
            return this;
        }

        @SuppressWarnings("unchecked")
        public Builder addAction(@NonNull NotificationCompat.Action action) {
            List<NotificationCompat.Action> actions = (List<NotificationCompat.Action>) dataMap.get(BUILDER_KEYS.ACTIONS);
            if (CollectionUtils.isEmpty(actions)) {
                actions = new ArrayList<>();
            }

            actions.add(action);

            dataMap.put(BUILDER_KEYS.ACTIONS, actions);
            return this;
        }

        public void show(@NonNull Context context) {
            int notificationId = 0;
            int iconResourceId = 0;
            int color = 0;
            String title = null;
            String text = null;
            NotificationCompat.Style style = null;
            Class<?> activityToLaunch = null;
            int notificationFlags = -1;
            PendingIntent contentIntent = null;
            List<NotificationCompat.Action> actions = null;

            for (Map.Entry<BUILDER_KEYS, Object> entry : dataMap.entrySet()) {
                switch (entry.getKey()) {
                    case ID:
                        notificationId = (int) dataMap.get(BUILDER_KEYS.ID);
                    case ICON:
                        iconResourceId = (int) dataMap.get(BUILDER_KEYS.ICON);
                        break;
                    case COLOR:
                        color = (int) dataMap.get(BUILDER_KEYS.COLOR);
                        break;
                    case TITLE:
                        title = (String) dataMap.get(BUILDER_KEYS.TITLE);
                        break;
                    case TEXT:
                        text = (String) dataMap.get(BUILDER_KEYS.TEXT);
                        break;
                    case STYLE:
                        style = (NotificationCompat.Style) dataMap.get(BUILDER_KEYS.STYLE);
                        break;
                    case ACTIVITY_TO_LAUNCH:
                        activityToLaunch = (Class<?>) dataMap.get(BUILDER_KEYS.ACTIVITY_TO_LAUNCH);
                        break;
                    case NOTIFICATION_FLAGS:
                        notificationFlags = (int) dataMap.get(BUILDER_KEYS.NOTIFICATION_FLAGS);
                        break;
                    case CONTENT_INTENT:
                        contentIntent = (PendingIntent) dataMap.get(BUILDER_KEYS.CONTENT_INTENT);
                        break;
                    case ACTIONS:
                        @SuppressWarnings("unchecked")
                        List<NotificationCompat.Action> tempActions = (List<NotificationCompat.Action>) dataMap.get(BUILDER_KEYS.ACTIONS);
                        actions = tempActions;
                        break;
                }
            }

            buildNotification(context, notificationId, iconResourceId, color, title, text, style, activityToLaunch, notificationFlags,
                contentIntent, actions);

            dataMap.clear();
        }
    }

    private static void buildNotification(@NonNull Context context, int notificationId, @DrawableRes int iconResourceId,
        @ColorInt int color, String title, String text, NotificationCompat.Style style, Class<?> activityToLaunch, int notificationFlags,
        PendingIntent contentIntent, List<NotificationCompat.Action> actions) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
            .setSmallIcon(iconResourceId)
            .setContentTitle(title)
            .setContentText(text);

        if (style != null) {
            builder.setStyle(style);
        }

        if (activityToLaunch != null) {
            builder.setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, activityToLaunch),
                PendingIntent.FLAG_UPDATE_CURRENT));
        }

        if (color != 0) {
            builder.setColor(color);
        }

        if (contentIntent != null) {
            builder.setContentIntent(contentIntent);
        }

        if (!CollectionUtils.isEmpty(actions)) {
            for (NotificationCompat.Action action : actions) {
                builder.addAction(action);
            }
        }

        Notification notification = builder.build();

        if (notificationFlags != -1) {
            notification.flags = notificationFlags;
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId, notification);
    }
}