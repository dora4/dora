<a href="./README.zh-CN.md">简体中文</a> ｜ <a href="./README.md">English</a>

Dora SDK Official Documentation![Release](https://jitpack.io/v/dora4/dora.svg)
--------------------------------

Introduction to the SDK

This is a framework for Android application development, which incorporates the author's years of experience in the Android industry. It provides an efficient way to develop and build Android mobile apps. Even beginners who have just started with Android app development can quickly get started. Whether you use the Java or Kotlin language, you can use this SDK.
![avatar](https://github.com/dora4/dora/blob/master/Dora.gif)

Features
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
  
  6. showPage(), nextPage(), getFlowFragment(), getFlowFragmentContainerId(), getFlowFragmentPageKeys(), and isLoop()
  
     This framework automatically manages the switching of Fragments within BaseActivity. It is commonly used in scenarios where there is no need for activity transition animations but rather direct changes to the overall layout of the interface. A detailed explanation is not provided here, but you can refer to the source code if you're interested.

- DoraCrash allows configuring crash information to be written into files with just one line of code, making bugs easier to identify.

  ```java
          DoraCrash.initCrash(this, "YourLogFolder/log");
  ```

- The app process can be automatically killed when the memory is low, and there won't be a black or white screen issue when the app is restarted.

- Various utility classes are provided for worry-free development. For example, ToastUtils automatically handles thread switching for displaying toasts, LogUtils eliminates the hassle of naming tags, and there are also encapsulations for complex operations such as Java file reading and writing, system status bar and navigation bar adaptation tools, internationalization tools for multi-language support, image processing tools, reflection-related tools, etc.

Getting Started (Tutorial: https://github.com/dora4/dora_samples)

```groovy
// Add the following code to the root build.gradle.kts file of your project
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
// Add the following code to the build.gradle.kts file of your app module
dependencies {
    implementation 'com.github.dora4:dora:latest-version'
}
```
Replace "latest-version" with the latest stable version available on JitPack, such as 1.0.0. So the code will become implementation 'com.github.dora4:dora:1.0.0'.

Android Studio IDE Plugin (Jar: [dora-studio-plugin-1.2.jar](https://github.com/dora4/dora-studio-plugin/blob/main/art/dora-studio-plugin-1.2.jar))<a href="./README.zh-CN.md">简体中文</a> ｜ <a href="./README.md">English</a>

Dora SDK官方文档 ![Release](https://jitpack.io/v/dora4/dora.svg)
--------------------------------

一、SDK介绍

这是一个用于Android应用开发的，凝聚SDK作者多年Android行业从业经验的，一个高效开发和构建Android手机APP的框架。一个刚接触到Android应用开发的初学者也可以快速上手。无论你使用Java语言，亦或是Kotlin语言，都可以使用本SDK。
![avatar](https://github.com/dora4/dora/blob/master/Dora.gif)

二、功能介绍

- 全局生命周期配置DefaultGlobalConfig、TaskStackGlobalConfig以及自定义配置，支持配置Application、Activity和Fragment的生命周期，一次编写，所有项目复用。
    
       <!-- 全局生命周期配置，value配置为GlobalConfig，name为映射的配置类即可，可配置多个 -->
       <application>
            <!-- dora.lifecycle.config.DefaultGlobalConfig为默认配置，即使不配置任何GlobalConfig，也至少配置了它，请不要重复配置，让Activity自动监听了网络状况。继承并使用[dora.BaseApplication]自动配置 -->
            
            <!-- 调用BaseActivity的openActivity系列方法必须配置TaskStackGlobalConfig -->
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
   
- BaseActivity、BaseFragment封装，基于MVVM架构。支持Activity中Fragment的流式切换，完美的Fragment切换方案，无Fragment重叠情况。并支持在Activity中监听手机网络的变化。
  1. showShortToast()和showLongToast()

     方便你在任意代码处弹出Toast，自动帮你切换线程，所以无需担心会报错

  2. openActivity()和openActivityForResult()系列，过时，已移到IntentUtils

     替代startActivity和startActivityForResult，传递参数更为方便

  3. onGetExtras()

     方便获取intent传递过来的参数，在initData()之前调用，所以在initData的时候，该有的参数都有值了

  4. onNetworkConnected()和onNetworkDisconnected()

     网络断开和连接状态的监听

  5. onSetStatusBar()和onSetNavigationBar()
    
     方便初始化系统状态栏和导航栏
  
  6. showPage()、nextPage()、getFlowFragment()、getFlowFragmentContainerId()、getFlowFragmentPageKeys()和isLoop()
  
     这个是自动管理BaseActivity内部Fragment切换的框架，常用于不需要activity的转场动画，而直接改变整体界面布局的场景。这里暂不做详细介绍，有兴趣的可以阅读源代码。
- DoraCrash一行代码配置将Crash信息写入文件，让BUG无处遁形。

  ```java
          DoraCrash.initCrash(this, "YourLogFolder/log");
  ```

- 可使用内存低的时候自动杀死本APP进程，在下次重启APP的时候不会有黑屏或白屏现象。
- 各种各样的工具类，开发无忧。如自动处理线程切换的ToastUtils让你更爽地弹吐司，LogUtils让你不再为tag命名而烦恼。还有复杂的Java文件读写等操作的封装、系统状态栏和导航栏适配工具、多语言国际化相关工具、图像处理相关工具、反射相关工具等。

三、开始使用（教程 https://github.com/dora4/dora_samples ）

```groovy
//添加以下代码到项目根目录下的build.gradle
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
//添加以下代码到app模块的build.gradle
dependencies {
    implementation 'com.github.dora4:dora:latest-version'
}
```
latest-version换成JitPack编译出来的最新绿色可用版本，如1.0.0，这样代码就变成了implementation 'com.github.dora4:dora:1.0.0'。

四、Android Studio IDE插件 (Jar包: [dora-studio-plugin-1.2.jar](https://github.com/dora4/dora-studio-plugin/blob/main/art/dora-studio-plugin-1.2.jar)) Aliyun OSS Backup: https://dorachat-sdk.oss-cn-hongkong.aliyuncs.com/dora-studio-plugin-1.2.jar

如果你觉得有用的话，不妨点击Github网页右上角的Fork按钮，让更多的人受益！


If you find it useful, you may wish to click the Fork button in the upper right corner of the Github page to benefit more people!
