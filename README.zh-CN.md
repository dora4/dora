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
    implementation 'com.github.dora4:dora:latest'
}
```
latest换成JitPack编译出来的最新绿色可用版本，如1.0.0，这样代码就变成了implementation 'com.github.dora4:dora:1.0.0'。



如果你觉得有用的话，不妨点击Github网页右上角的Fork按钮，让更多的人受益！
