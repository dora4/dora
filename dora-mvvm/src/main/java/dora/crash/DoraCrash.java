package dora.crash;

import android.content.Context;

import androidx.annotation.RequiresPermission;

import dora.crash.policy.StoragePolicy;
import dora.permission.runtime.Permission;

public class DoraCrash {

    /**
     * 可以在启动页中调用，需要存储权限。
     *
     * @param context
     * @param crashInfoFolder
     */
    @RequiresPermission(Permission.WRITE_EXTERNAL_STORAGE)
    public static void initCrash(Context context, String crashInfoFolder) {
        new DoraConfig.Builder(context)
                .crashReportPolicy(new StoragePolicy(crashInfoFolder)).build();
    }
}
