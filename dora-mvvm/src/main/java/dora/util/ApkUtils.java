package dora.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.util.DisplayMetrics;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * APK file-related tools, capable of reading file information, installing, and launching, among
 * other functions.
 * 简体中文：APK文件相关工具，读取文件信息、安装、启动等。
 */
public final class ApkUtils {

    private static final String CLASS_PACKAGE_PARSER = "android.content.pm.PackageParser";
    private static final String METHOD_PARSE_PACKAGE = "parsePackage";
    private static final String METHOD_COLLECT_CERTIFICATES = "collectCertificates";
    private static final String FIELD_SIGNATURES = "mSignatures";
    private static final String URI_INSTALL_PACKAGE = "application/vnd.android.package-archive";

    private ApkUtils() {
    }

    // <editor-folder desc="Get APK package information">

    public static String getAppName() {
        return getAppName(GlobalContext.get());
    }

    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getVersionName() {
        return getVersionName(GlobalContext.get());
    }

    public static String getVersionName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo;
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getVersionCode() {
        return getVersionCode(GlobalContext.get());
    }

    public static int getVersionCode(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo;
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static PackageInfo getPackageInfo(String packageName) {
        return getPackageInfo(GlobalContext.get(), packageName);
    }

    public static PackageInfo getPackageInfo(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        try {
            return packageManager.getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Signature[] getSignatures() {
        return getSignatures(GlobalContext.get());
    }

    public static Signature[] getSignatures(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo;
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_SIGNATURES);
            return packageInfo.signatures;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static File extractApk() {
        return extractApk(GlobalContext.get());
    }

    public static File extractApk(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationContext().getApplicationInfo();
        String apkPath = applicationInfo.sourceDir;
        File apkFile = new File(apkPath);
        return apkFile;
    }

    public static List<String> getAllPackageNames() {
        return getAllPackageNames(GlobalContext.get());
    }

    public static List<String> getAllPackageNames(Context context) {
        PackageManager packManager = context.getPackageManager();
        List<PackageInfo> packInfos = packManager
                .getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        if (packInfos == null || packInfos.size() == 0) {
            return null;
        }
        List<String> pkList = new ArrayList<>();
        for (PackageInfo packInfo : packInfos) {
            String packageName = packInfo.packageName;
            pkList.add(packageName);
        }
        return pkList;
    }

    public static Drawable getUninstalledApkIcon(String apkPath) {
        return getUninstalledApkIcon(GlobalContext.get(), apkPath);
    }

    public static Drawable getUninstalledApkIcon(Context context, String apkPath) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = getUninstalledApkPackageInfo(context, apkPath);
        if (packageInfo == null) {
            return null;
        }
        ApplicationInfo applicationInfo = packageInfo.applicationInfo;
        applicationInfo.sourceDir = apkPath;
        applicationInfo.publicSourceDir = apkPath;
        return packageManager.getApplicationIcon(applicationInfo);
    }

    public static CharSequence getUninstalledApkLabel(String apkPath) {
        return getUninstalledApkLabel(GlobalContext.get(), apkPath);
    }

    public static CharSequence getUninstalledApkLabel(Context context, String apkPath) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = getUninstalledApkPackageInfo(context, apkPath);
        if (packageInfo == null) {
            return null;
        }
        ApplicationInfo applicationInfo = packageInfo.applicationInfo;
        applicationInfo.sourceDir = apkPath;
        applicationInfo.publicSourceDir = apkPath;
        return packageManager.getApplicationLabel(applicationInfo);
    }

    public static Signature[] getUninstalledApkSignatures(String apkPath) {
        try {
            Class<?> packageParserClass = Class.forName(CLASS_PACKAGE_PARSER);
            Class[] typeArgs = new Class[1];
            typeArgs[0] = String.class;
            Constructor packageParserConstructor = packageParserClass.getConstructor(typeArgs);
            Object[] valueArgs = new Object[1];
            valueArgs[0] = apkPath;
            Object packageParser = packageParserConstructor.newInstance(valueArgs);
            DisplayMetrics metrics = new DisplayMetrics();
            metrics.setToDefaults();
            typeArgs = new Class[4];
            typeArgs[0] = File.class;
            typeArgs[1] = String.class;
            typeArgs[2] = DisplayMetrics.class;
            typeArgs[3] = Integer.TYPE;
            Method parsePackageMethod = packageParserClass.getDeclaredMethod(METHOD_PARSE_PACKAGE,
                    typeArgs);
            valueArgs = new Object[4];
            valueArgs[0] = new File(apkPath);
            valueArgs[1] = apkPath;
            valueArgs[2] = metrics;
            valueArgs[3] = PackageManager.GET_SIGNATURES;
            Object packageParserPackage = parsePackageMethod.invoke(packageParser, valueArgs);
            typeArgs = new Class[2];
            typeArgs[0] = packageParserPackage.getClass();
            typeArgs[1] = Integer.TYPE;
            Method collectCertificatesMethod = packageParserClass
                    .getDeclaredMethod(METHOD_COLLECT_CERTIFICATES, typeArgs);
            valueArgs = new Object[2];
            valueArgs[0] = packageParserPackage;
            valueArgs[1] = PackageManager.GET_SIGNATURES;
            collectCertificatesMethod.invoke(packageParser, valueArgs);
            Field packageInfoField = packageParserPackage.getClass()
                    .getDeclaredField(FIELD_SIGNATURES);
            Signature[] signatures = (Signature[]) packageInfoField.get(packageParserPackage);
            return signatures;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static PackageInfo getUninstalledApkPackageInfo(String apkPath) {
        return getUninstalledApkPackageInfo(GlobalContext.get(), apkPath);
    }

    public static PackageInfo getUninstalledApkPackageInfo(Context context, String apkPath) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = packageManager.getPackageArchiveInfo(
                apkPath, 0);
        return packageInfo;
    }

    // </editor-folder>

    // <editor-folder desc="安装和启动">

    public static void install(File file, Uri contentUri) {
        install(GlobalContext.get(), file, contentUri);
    }

    /**
     * Uri contentUri = FileProvider.getUriForFile(context,
     * BuildConfig.APPLICATION_ID+".file-provider", file);
     */
    public static void install(Context context, File file, Uri contentUri) {
        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(contentUri, URI_INSTALL_PACKAGE);
        } else {
            uri = Uri.fromFile(file);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(uri, URI_INSTALL_PACKAGE);
        }
        context.startActivity(intent);
    }

    public static void launch(String packageName, String className) {
        launch(GlobalContext.get(), packageName, className);
    }

    public static void launch(Context context, String packageName, String className) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setComponent(new ComponentName(packageName, className));
        context.startActivity(intent);
    }

    // </editor-folder>
}
