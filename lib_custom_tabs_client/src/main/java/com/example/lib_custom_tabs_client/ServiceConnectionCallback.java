package com.example.lib_custom_tabs_client;

import androidx.browser.customtabs.CustomTabsClient;

public interface ServiceConnectionCallback {
    void onServiceConnected(CustomTabsClient customTabsClient);

    void onServiceDisconnected();
}
