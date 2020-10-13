使用CustomTabsClient，利用Chrome内核在app内部打开网页，代替Webview
=============================================

## 使用方法：
```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}

implementation 'com.github.jiayuliang1314:lib_custom_tabs_client:1.0.0'
```

## 1.使用CustomTabsClient，利用Chrome内核在app内部打开网页，代替Webview
```
    /**
     *
     * @param link link
     * @param context
     * @param in anim
     * @param out anim
     * @param toolbarcolor color
     */
    public static void open(String link, Activity context, int in, int out, int toolbarcolor)
```