package dora.util;

import android.app.Application;

/**
 * 如果是动态加载到JVM的Application，如插件化，会读取不到Context，使用需谨慎。
 */
public class GlobalContext {

    private static final Application mInstance;

    static {
        Application app = null;
        try {
            app = (Application) Class.forName("android.app.AppGlobals")
                    .getMethod("getInitialApplication").invoke(null);
            if (app == null)
                throw new IllegalStateException("Static initialization of Applications must be on main thread.");
        } catch (final Exception e) {
            e.printStackTrace();
            try {
                app = (Application) Class.forName("android.app.ActivityThread")
                        .getMethod("currentApplication").invoke(null);
            } catch (final Exception ex) {
                e.printStackTrace();
            }
        } finally {
            mInstance = app;
        }
    }

    public static Application get() {
        return mInstance;
    }
}
