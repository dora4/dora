package dora.permission.checker;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.List;

@Deprecated
public final class StandardChecker implements PermissionChecker {

    private static final int MODE_ASK = 4;

    public StandardChecker() {
    }

    @Override
    public boolean hasPermission(Context context, String... permissions) {
        return hasPermission(context, Arrays.asList(permissions));
    }

    @Override
    public boolean hasPermission(Context context, List<String> permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        AppOpsManager opsManager = null;
        for (String permission : permissions) {
            int result = context.checkPermission(permission, android.os.Process.myPid(), android.os.Process.myUid());
            if (result == PackageManager.PERMISSION_DENIED) {
                return false;
            }

            String op = AppOpsManager.permissionToOp(permission);
            if (TextUtils.isEmpty(op)) {
                continue;
            }

            if (opsManager == null) {
                opsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            }
            result = opsManager.checkOpNoThrow(op, android.os.Process.myUid(), context.getPackageName());
            if (result != AppOpsManager.MODE_ALLOWED && result != MODE_ASK) {
                return false;
            }
        }
        return true;
    }
}