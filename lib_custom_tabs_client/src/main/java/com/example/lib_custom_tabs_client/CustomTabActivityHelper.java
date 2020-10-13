package com.example.lib_custom_tabs_client;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.browser.customtabs.CustomTabsClient;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.browser.customtabs.CustomTabsServiceConnection;
import androidx.browser.customtabs.CustomTabsSession;
import androidx.core.content.ContextCompat;

import java.util.List;


public class CustomTabActivityHelper implements ServiceConnectionCallback {
    private CustomTabsClient mClient;
    private CustomTabsServiceConnection mConnection;
    private ConnectionCallback mConnectionCallback;
    private CustomTabsSession mCustomTabsSession;

    /**
     *
     * @param link link
     * @param context
     * @param in anim
     * @param out anim
     * @param toolbarcolor color
     */
    public static void open(String link, Activity context, int in, int out, int toolbarcolor) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.addDefaultShareMenuItem();
        builder.setToolbarColor(toolbarcolor);
        builder.setShowTitle(true);
        builder.setStartAnimations(context, in, out);
        CustomTabActivityHelper.openCustomTab(context, builder.build(), Uri.parse(link), new CustomTabActivityHelper.CustomTabFallback() {
            @Override
            public void openUri(Activity activity, Uri uri) {
                activity.startActivity(new Intent("android.intent.action.VIEW", uri));
            }
        });
    }

    private static void openCustomTab(Activity activity, CustomTabsIntent customTabsIntent, Uri uri, CustomTabFallback customTabFallback) {
        String packageNameToUse = CustomTabsHelper.getPackageNameToUse(activity);
        if (packageNameToUse != null) {
            customTabsIntent.intent.setPackage(packageNameToUse);
            customTabsIntent.intent.setData(uri);
            ContextCompat.startActivity(activity, customTabsIntent.intent, customTabsIntent.startAnimationBundle);
        } else if (customTabFallback != null) {
            customTabFallback.openUri(activity, uri);
        }
    }

    public void unbindCustomTabsService(Activity activity) {
        CustomTabsServiceConnection customTabsServiceConnection = this.mConnection;
        if (customTabsServiceConnection != null) {
            activity.unbindService(customTabsServiceConnection);
            this.mClient = null;
            this.mCustomTabsSession = null;
            this.mConnection = null;
        }
    }

    public CustomTabsSession getSession() {
        CustomTabsClient customTabsClient = this.mClient;
        if (customTabsClient == null) {
            this.mCustomTabsSession = null;
        } else if (this.mCustomTabsSession == null) {
            this.mCustomTabsSession = customTabsClient.newSession(null);
        }
        return this.mCustomTabsSession;
    }

    public void setConnectionCallback(ConnectionCallback connectionCallback) {
        this.mConnectionCallback = connectionCallback;
    }

    public void bindCustomTabsService(Activity activity) {
        if (this.mClient == null) {
            String packageNameToUse = CustomTabsHelper.getPackageNameToUse(activity);
            if (packageNameToUse != null) {
                this.mConnection = new ServiceConnection(this);
                CustomTabsClient.bindCustomTabsService(activity, packageNameToUse, this.mConnection);
            }
        }
    }

    public boolean mayLaunchUrl(Uri uri, Bundle bundle, List<Bundle> list) {
        if (this.mClient == null) {
            return false;
        }
        CustomTabsSession session = getSession();
        if (session == null) {
            return false;
        }
        return session.mayLaunchUrl(uri, bundle, list);
    }

    @Override
    public void onServiceConnected(CustomTabsClient customTabsClient) {
        this.mClient = customTabsClient;
        this.mClient.warmup(0);
        ConnectionCallback connectionCallback = this.mConnectionCallback;
        if (connectionCallback != null) {
            connectionCallback.onCustomTabsConnected();
        }
    }

    @Override
    public void onServiceDisconnected() {
        this.mClient = null;
        this.mCustomTabsSession = null;
        ConnectionCallback connectionCallback = this.mConnectionCallback;
        if (connectionCallback != null) {
            connectionCallback.onCustomTabsDisconnected();
        }
    }

    public interface ConnectionCallback {
        void onCustomTabsConnected();

        void onCustomTabsDisconnected();
    }

    public interface CustomTabFallback {
        void openUri(Activity activity, Uri uri);
    }
}
