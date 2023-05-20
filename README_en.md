Dora SDK Official Documentation Release
Introduction to the SDK
This is a framework for Android application development, which incorporates the author's years of experience in the Android industry. It provides an efficient way to develop and build Android mobile apps. Even beginners who have just started with Android app development can quickly get started. Whether you use the Java or Kotlin language, you can use this SDK.

avatar

Features
Global lifecycle configuration: DefaultGlobalConfig, TaskStackGlobalConfig, and custom configuration. It supports configuring the lifecycles of Application, Activity, and Fragment, allowing you to write once and reuse in all projects.

php
Copy code
 <!-- Global lifecycle configuration, value is configured as GlobalConfig, name is the mapping configuration class, multiple configurations can be set -->
 <application>
      <!-- DefaultGlobalConfig is the default configuration, even if no GlobalConfig is configured, it should be configured at least. It allows Activity to automatically listen to network conditions. Inherit and use [dora.BaseApplication] for automatic configuration -->
      
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
BaseActivity and BaseFragment encapsulation based on the MVVM architecture. It supports seamless switching of Fragments in Activity, providing a perfect solution for Fragment switching without overlap. It also supports monitoring network changes in Activity. You can use the IDE plugin for more convenient development, available at https://github.com/dora4/dora-studio-plugin.

DoraCrash allows configuring crash information to be written into files with just one line of code, making bugs easier to identify.

kotlin
Copy code
  		DoraCrash.initCrash(this, "YourLogFolder/log");
The app process can be automatically killed when the memory is low, and there won't be a black or white screen issue when the app is restarted.

Various utility classes are provided for worry-free development. For example, ToastUtils automatically handles thread switching for displaying toasts, LogUtils eliminates the hassle of naming tags, and there are also encapsulations for complex operations such as Java file reading and writing, system status bar and navigation bar adaptation tools, internationalization tools for multi-language support, image processing tools, reflection-related tools, etc.

Getting Started (Tutorial: https://github.com/dora4/dora_samples)
groovy
Copy code
// Add the following code to the root build.gradle file of your project
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
// Add the following code to the build.gradle file of your app module
dependencies {
    implementation 'com.github.dora4:dora:latest'
}
Replace "latest" with the latest stable version available on JitPack, such as 1.0.0. So the code will become implementation 'com.github.dora4:dora:1.0.0'.

If you find it useful, you may wish to click the Fork button in the upper right corner of the Github page to benefit more people!
