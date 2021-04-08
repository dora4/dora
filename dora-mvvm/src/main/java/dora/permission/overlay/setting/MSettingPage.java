package dora.permission.overlay.setting;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import dora.permission.source.Source;

@Deprecated
public class MSettingPage {

    private static final String MARK = Build.MANUFACTURER.toLowerCase();

    private Source mSource;

    public MSettingPage(Source source) {
        this.mSource = source;
    }

    public void start(int requestCode) {
        Intent intent;
        if (MARK.contains("meizu")) {
            intent = meiZuApi(mSource.getContext());
        } else {
            intent = defaultApi(mSource.getContext());
        }

        try {
            mSource.startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            intent = appDetailsApi(mSource.getContext());
            mSource.startActivityForResult(intent, requestCode);
        }
    }

    private static Intent appDetailsApi(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        return intent;
    }

    private static Intent defaultApi(Context context) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        if (hasActivity(context, intent)) {
            return intent;
        }

        return appDetailsApi(context);
    }

    private static Intent meiZuApi(Context context) {
        Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
        intent.putExtra("packageName", context.getPackageName());
        intent.setClassName("com.meizu.safe", "com.meizu.safe.security.AppSecActivity");
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