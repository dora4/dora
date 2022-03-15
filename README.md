Dora SDK官方文档 ![Release](https://jitpack.io/v/dora4/dora.svg)
--------------------------------

一、SDK介绍

这是一个用于Android应用开发的，凝聚SDK作者多年Android行业从业经验的，一个高效开发和构建Android手机APP的框架。一个刚接触到Android应用开发的初学者也可以快速上手。无论你使用Java语言，亦或是Kotlin语言，都可以使用本SDK。

![avatar](https://github.com/dora4/dora/blob/master/Dora.gif)



二、功能介绍

- 在Android手机系统进行文件读写的API，比JDK更简洁
- Android6.0以上手机需要的运行时权限的申请
- 不同分辨率Android手机的显示效果适配
- 更换APP皮肤
- 使用ORM将数据存储在Android手机系统数据库SQLite
- 自动监听手机网络的变化
- 可使用内存低的时候自动杀死本APP进程，在下次重启APP的时候不会有黑屏或白屏现象
- 处理加载在Android手机上的图形，图像变换
- 基于RecyclerView的列表视图API
- 代码任意位置使用Toast，无需关心当前是否是子线程
- 系统状态栏和导航栏的沉浸式
- 汉字转拼音的API



三、开始使用（教程 https://github.com/dora4/dora_samples ）

由于此SDK仅支持Android Studio的开发，不支持Eclipse的开发。如果你使用Eclipse开发的话，可以从Github下载一份最新的源代码，添加到项目中。

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

Git命令克隆一份源代码到本地： git clone https://github.com/dora4/dora
