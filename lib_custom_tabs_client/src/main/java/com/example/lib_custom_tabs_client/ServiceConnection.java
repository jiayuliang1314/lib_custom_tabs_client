package com.example.lib_custom_tabs_client;

import android.content.ComponentName;

import androidx.browser.customtabs.CustomTabsClient;
import androidx.browser.customtabs.CustomTabsServiceConnection;

import java.lang.ref.WeakReference;

public class ServiceConnection extends CustomTabsServiceConnection {
    private WeakReference<ServiceConnectionCallback> mConnectionCallback;

    public ServiceConnection(ServiceConnectionCallback serviceConnectionCallback) {
        this.mConnectionCallback = new WeakReference<>(serviceConnectionCallback);
    }

    @Override
    public void onCustomTabsServiceConnected(ComponentName componentName, CustomTabsClient cVar) {
        ServiceConnectionCallback serviceConnectionCallback = this.mConnectionCallback.get();
        if (serviceConnectionCallback != null) {
            serviceConnectionCallback.onServiceConnected(cVar);
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        ServiceConnectionCallback serviceConnectionCallback = this.mConnectionCallback.get();
        if (serviceConnectionCallback != null) {
            serviceConnectionCallback.onServiceDisconnected();
        }
    }
}
