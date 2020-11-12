package dora.permission.overlay.setting;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import dora.permission.source.Source;

public class LSettingPage {

    private static final String MARK = Build.MANUFACTURER.toLowerCase();

    private Source mSource;

    public LSettingPage(Source source) {
        this.mSource = source;
    }

    public void start(int requestCode) {
        Intent intent;
        if (MARK.contains("huawei")) {
            intent = huaweiApi(mSource.getContext());
        } else if (MARK.contains("xiaomi")) {
            intent = xiaomiApi(mSource.getContext());
        } else if (MARK.contains("oppo")) {
            intent = oppoApi(mSource.getContext());
        } else if (MARK.contains("vivo")) {
            intent = vivoApi(mSource.getContext());
        } else if (MARK.contains("meizu")) {
            intent = meizuApi(mSource.getContext());
        } else {
            intent = defaultApi(mSource.getContext());
        }

        try {
            mSource.startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            intent = defaultApi(mSource.getContext());
            mSource.startActivityForResult(intent, requestCode);
        }
    }

    private static Intent defaultApi(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        return intent;
    }

    private Intent huaweiApi(Context context) {
        Intent intent = new Intent();
        intent.setClassName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity");
        if (hasActivity(context, intent)) {
            return intent;
        }

        intent.setClassName("com.huawei.systemmanager", "com.huawei.systemmanager.addviewmonitor.AddViewMonitorActivity");
        if (hasActivity(context, intent)) {
            return intent;
        }

        intent.setClassName("com.huawei.systemmanager", "com.huawei.notificationmanager.ui.NotificationManagmentActivity");
        if (hasActivity(context, intent)) {
            return intent;
        }

        return defaultApi(context);
    }

    private Intent xiaomiApi(Context context) {
        Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
        intent.putExtra("extra_pkgname", context.getPackageName());
        if (hasActivity(context, intent)) {
            return intent;
        }

        intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
        if (hasActivity(context, intent)) {
            return intent;
        }

        return defaultApi(context);
    }

    private Intent oppoApi(Context context) {
        Intent intent = new Intent();
        intent.putExtra("packageName", context.getPackageName());
        intent.setClassName("com.color.safecenter",
                "com.color.safecenter.permission.floatwindow.FloatWindowListActivity");
        if (hasActivity(context, intent)) {
            return intent;
        }

        intent.setClassName("com.coloros.safecenter", "com.coloros.safecenter.sysfloatwindow.FloatWindowListActivity");
        if (hasActivity(context, intent)) {
            return intent;
        }

        intent.setClassName("com.oppo.safe", "com.oppo.safe.permission.PermissionAppListActivity");
        if (hasActivity(context, intent)) {
            return intent;
        }

        return defaultApi(context);
    }

    private Intent vivoApi(Context context) {
        Intent intent = new Intent();
        intent.setClassName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.FloatWindowManager");
        intent.putExtra("packagename", context.getPackageName());
        if (hasActivity(context, intent)) {
            return intent;
        }

        intent.setClassName("com.iqoo.secure", "com.iqoo.secure.safeguard.SoftPermissionDetailActivity");
        if (hasActivity(context, intent)) {
            return intent;
        }

        return defaultApi(context);
    }

    private Intent meizuApi(Context context) {
        Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
        intent.putExtra("packageName", context.getPackageName());
        intent.setComponent(new ComponentName("com.meizu.safe", "com.meizu.safe.security.AppSecActivity"));
        if (hasActivity(context, intent)) {
            return intent;
        }

        return defaultApi(context);
    }

    private static boolean hasActivity(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        return packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0;
    }
}