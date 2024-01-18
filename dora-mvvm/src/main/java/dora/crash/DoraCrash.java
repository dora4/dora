package dora.crash;

import android.content.Context;
import dora.crash.policy.StoragePolicy;

public class DoraCrash {

    public static void initCrash(Context context, String crashInfoFolder) {
        new DoraCrashConfig.Builder(context)
                .crashReportPolicy(new StoragePolicy(crashInfoFolder)).build();
    }
}
