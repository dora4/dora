package dora.crash;

import android.content.Context;
import dora.crash.policy.StoragePolicy;

public class DoraCrash {

    /**
     * 可以在启动页中调用，需要存储权限。
     *
     * @param context
     * @param crashInfoFolder
     */
    public static void initCrash(Context context, String crashInfoFolder) {
        new DoraConfig.Builder(context)
                .crashReportPolicy(new StoragePolicy(crashInfoFolder)).build();
    }
}
