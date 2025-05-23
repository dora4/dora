<a href="./README.zh-CN.md">简体中文</a> ｜ <a href="./README.md">English</a>

Dora SDK Official Document![Release](https://jitpack.io/v/dora4/dora.svg)
--------------------------------

**Introduction to the SDK**

This is a framework for Android application development, which incorporates the author's years of experience in the Android industry. It provides an efficient way to develop and build Android mobile apps. Even beginners who have just started with Android app development can quickly get started. Whether you use the Java or Kotlin language, you can use this SDK.
![avatar](https://github.com/dora4/dora/blob/master/Dora.gif)

**Features**
- Global lifecycle configuration: TaskStackGlobalConfig, and custom configuration. It supports configuring the lifecycles of Application, Activity, and Fragment, allowing you to write once and reuse in all projects.

 <!-- Global lifecycle configuration, value is configured as GlobalConfig, name is the mapping configuration class, multiple configurations can be set -->
 <application>
      <!-- TaskStackGlobalConfig must be configured for invoking the openActivity series methods of BaseActivity -->
      <meta-data
          android:name="dora.lifecycle.config.TaskStackGlobalConfig"
          android:value="GlobalConfig" />
      <meta-data
          android:name="dora.lifecycle.config.EventBusGlobalConfig"
          android:value="GlobalConfig" />
      <meta-data
          android:name="dora.lifecycle.config.ARouterGlobalConfig"
          android:value="GlobalConfig" />
      <meta-data
          android:name="com.example.dora.lifecycle.RetrofitGlobalConfig"
          android:value="GlobalConfig" />
      <meta-data
          android:name="com.example.dora.lifecycle.YourCustomGlobalConfig"
          android:value="GlobalConfig" />
  </application>

- BaseActivity and BaseFragment encapsulation based on the MVVM architecture. It supports seamless switching of Fragments in Activity, providing a perfect solution for Fragment switching without overlap. It also supports monitoring network changes in Activity. You can use the IDE plugin for more convenient development, available at https://github.com/dora4/dora-studio-plugin.
    1. showShortToast() and showLongToast().

       Convenient methods to display a Toast message at any point in your code. Automatically handles thread switching, so you don't need to worry about errors.
    2. openActivity() and openActivityForResult() serie, deprecated, moved to IntentUtils.

       Alternative methods to startActivity and startActivityForResult, providing a more convenient way to pass parameters.

    3. onGetExtras()

       Convenient method to retrieve parameters passed through an intent. Should be called before initData(). This ensures that all the required parameters are available during the initData() process.

    4. onNetworkConnected() and onNetworkDisconnected()

       Listeners for network connection status, indicating when the network is connected or disconnected.

    5. onSetStatusBar() and onSetNavigationBar()

       Convenient method for initializing system status bar and navigation bar.

    6. showPage(), lastPage(), nextPage(), getFlowFragment(), getFlowFragmentContainerId(), getFlowFragmentPageKeys(), and isPageLoop()

       This framework automatically manages the switching of Fragments within BaseActivity. It is commonly used in scenarios where there is no need for activity transition animations but rather direct changes to the overall layout of the interface. A detailed explanation is not provided here, but you can refer to the source code if you're interested.

- DoraCrash allows configuring crash information to be written into files with just one line of code, making bugs easier to identify.

  ```java
  DoraCrash.initCrash(this, "YourAppFolder/log");
  ```

- The app process can be automatically killed when the memory is low.

- Various utility classes are provided for worry-free development. For example, ToastUtils automatically handles thread switching for displaying toasts, LogUtils eliminates the hassle of naming tags, and there are also encapsulations for complex operations such as Java file reading and writing, system status bar and navigation bar adaptation tools, internationalization tools for multi-language support, image processing tools, reflection-related tools, etc.

**Getting Started** (Tutorial: https://github.com/dora4/dora_samples)

```groovy
// Add the following code to the root build.gradle file of your project
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
// Add the following code to the build.gradle file of your app module
dependencies {
    implementation 'com.github.dora4:dora:latest-version'
}
```
Replace "latest-version" with the latest stable version available on JitPack, such as 1.0.0. So the code will become implementation 'com.github.dora4:dora:1.0.0'.

Add proguard rules.
```pro
# Preserve all implementation classes of the GlobalConfig interface.
-keep class * implements dora.lifecycle.config.GlobalConfig { *; }
-keep class dora.BaseVMActivity { *; }
-keep class dora.BaseVMFragment { *; }
-keep class com.android.internal.R$styleable { *; }
# Keep the ActivityInfo class and its isTranslucentOrFloating method
-keep class android.content.pm.ActivityInfo { 
    boolean isTranslucentOrFloating(android.content.res.TypedArray);
}
```

If you find it useful, you may wish to click the Fork button in the upper right corner of the Github page to benefit more people!

**Best Practices**

Dora Chat (https://dorachat.com)
Dora Music ([Dora Music](https://github.com/dora4/DoraMusic))
