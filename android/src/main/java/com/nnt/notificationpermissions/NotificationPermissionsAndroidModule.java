package com.nnt.notificationpermissions;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.annotation.TargetApi;
import androidx.core.app.NotificationManagerCompat;
import android.app.NotificationChannel;
import android.app.NotificationManager;

public class NotificationPermissionsAndroidModule extends ReactContextBaseJavaModule {
    private static final int REQUEST_CODE = 1;

    private final ReactApplicationContext reactContext;

    public NotificationPermissionsAndroidModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "NotificationPermissionsAndroid";
    }

    @ReactMethod
    public void openSettings() {
        String packageName = this.reactContext.getPackageName();
        Intent intent = new Intent();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("android.provider.extra.APP_PACKAGE", packageName);
            intent.putExtra("app_package", packageName);
            intent.putExtra("app_uid", this.reactContext.getApplicationInfo().uid);
        } else {
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setData(Uri.parse("package:" + packageName));
        }

        getReactApplicationContext().startActivityForResult(intent, REQUEST_CODE, null);
    }

    @ReactMethod
    public void isEnabled(final Promise promise) {
        try {
            Boolean areEnabled = NotificationManagerCompat.from(getReactApplicationContext()).areNotificationsEnabled();
            promise.resolve(areEnabled);
        } catch (Exception e) {
            promise.reject(e);
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void getNotificationChannelsOnAndroidOreo(Promise promise) {
        NotificationManager notificationManager = getReactApplicationContext().getSystemService(NotificationManager.class);
        if (notificationManager == null) {
            promise.reject("no_notification_manager_service", "Could not retrieve Android NotificationManager service");
            return;
        }

        try {
            WritableArray notificationsChannels = Arguments.createArray();
            for (NotificationChannel notificationChannel : notificationManager.getNotificationChannels()) {
                WritableMap channel = Arguments.createMap();
                channel.putBoolean("isEnabled", notificationChannel.getImportance() != NotificationManager.IMPORTANCE_NONE);
                channel.putString("id", notificationChannel.getId());
                channel.putBoolean("isBadgeEnabled", notificationChannel.canShowBadge());
                channel.putBoolean("isSoundEnabled", notificationChannel.getSound() != null);
                channel.putBoolean("shownInNotificationCenter", false);
                channel.putBoolean("shownInLockScreen", notificationChannel.getLockscreenVisibility() > 0);
                channel.putBoolean("shownAsHeadsupDisplay", notificationChannel.getImportance() >= NotificationManager.IMPORTANCE_HIGH);
                notificationsChannels.pushMap(channel);
            }
            promise.resolve(notificationsChannels);
        } catch (Exception e) {
            promise.reject(e);
        }
    }

    @ReactMethod
    public void getNotificationChannels(Promise promise) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            WritableArray notificationsChannels = Arguments.createArray();
            promise.resolve(notificationsChannels);
            return;
        }

        getNotificationChannelsOnAndroidOreo(promise);
    }

    @ReactMethod
    public void getGlobalNotificationSettings(Promise promise) {
        int importance;
        try {
            importance = NotificationManagerCompat.from(getReactApplicationContext()).getImportance();
        } catch (Exception e) {
            promise.reject(e);
            return;
        }

        WritableMap settings = Arguments.createMap();
        switch (importance) {
            default:
                promise.reject(new RuntimeException("unknown importance value: " + importance));
                return;
            case NotificationManagerCompat.IMPORTANCE_NONE:
                settings.putBoolean("isEnabled", false);
                settings.putBoolean("isBadgeEnabled", false);
                settings.putBoolean("isSoundEnabled", false);
                settings.putBoolean("shownInNotificationCenter", false);
                settings.putBoolean("shownInLockScreen", false);
                settings.putBoolean("shownAsHeadsupDisplay", false);
                break;
            case NotificationManagerCompat.IMPORTANCE_MIN:
            case NotificationManagerCompat.IMPORTANCE_LOW:
                settings.putBoolean("isEnabled", true);
                settings.putBoolean("isBadgeEnabled", true);
                settings.putBoolean("isSoundEnabled", false);
                settings.putBoolean("shownInNotificationCenter", true);
                settings.putBoolean("shownInLockScreen", true);
                settings.putBoolean("shownAsHeadsupDisplay", false);
                break;
            case NotificationManagerCompat.IMPORTANCE_DEFAULT:
            case NotificationManagerCompat.IMPORTANCE_UNSPECIFIED:
                settings.putBoolean("isEnabled", true);
                settings.putBoolean("isBadgeEnabled", true);
                settings.putBoolean("isSoundEnabled", true);
                settings.putBoolean("shownInNotificationCenter", true);
                settings.putBoolean("shownInLockScreen", true);
                settings.putBoolean("shownAsHeadsupDisplay", false);
                break;
            case NotificationManagerCompat.IMPORTANCE_HIGH:
            case NotificationManagerCompat.IMPORTANCE_MAX:
                settings.putBoolean("isEnabled", true);
                settings.putBoolean("isBadgeEnabled", true);
                settings.putBoolean("isSoundEnabled", true);
                settings.putBoolean("shownInNotificationCenter", true);
                settings.putBoolean("shownInLockScreen", true);
                settings.putBoolean("shownAsHeadsupDisplay", true);
                break;
        }
        promise.resolve(settings);
    }
}
