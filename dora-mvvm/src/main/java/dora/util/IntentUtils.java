package dora.util;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;

import androidx.annotation.AnimRes;
import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * It is used to connect various components within Android and facilitate data transfer between
 * components.
 * 简体中文：用于连接Android各大组件以及组件直接的传参。
 */
public final class IntentUtils {

    private IntentUtils() {
    }

    public static boolean hasExtra(@NonNull Intent intent, @NonNull String key) {
        return intent.hasExtra(key);
    }

    public static boolean hasExtras(@NonNull Intent intent, @NonNull String[] keys) {
        int length = keys.length;
        int count = 0;
        for (String key : keys) {
            if (hasExtra(intent, key)) {
                count++;
            }
        }
        return count == length;
    }

    public static Intent getActivityIntent(@NonNull Context context, @NonNull Class<? extends Activity> activityClazz) {
        return new Intent(context, activityClazz);
    }

    public static Intent getActivityIntent(@NonNull Class<? extends Activity> activityClazz) {
        Activity topActivity = TaskStackManager.getInstance().getTopActivity();
        if (topActivity == null) {
            throw new IllegalStateException("You need to configure dora.TaskStackGlobalConfig first.");
        }
        return new Intent(topActivity, activityClazz);
    }

    public static void startActivity(@NonNull Context context, @NonNull Class<? extends Activity> activityClazz) {
        context.startActivity(getActivityIntent(context, activityClazz));
    }

    public static void startActivity(@NonNull Class<? extends Activity> activityClazz) {
        Activity topActivity = TaskStackManager.getInstance().getTopActivity();
        if (topActivity == null) {
            throw new IllegalStateException("You need to configure dora.TaskStackGlobalConfig first.");
        }
        startActivity(topActivity, activityClazz);
    }

    public static void startActivityForResult(@NonNull Activity activity, @NonNull Class<? extends
            Activity> activityClazz, int requestCode) {
        activity.startActivityForResult(getActivityIntent(activity, activityClazz), requestCode);
    }

    public static void startActivityForResult(@NonNull Class<? extends Activity> activityClazz, int requestCode) {
        Activity topActivity = TaskStackManager.getInstance().getTopActivity();
        if (topActivity == null) {
            throw new IllegalStateException("You need to configure dora.TaskStackGlobalConfig first.");
        }
        startActivityForResult(topActivity, activityClazz, requestCode);
    }

    public static void startActivity(@NonNull Context context,
                                     @NonNull Class<? extends Activity> activityClazz, Extras extras) {
        Intent intent = getActivityIntent(context, activityClazz);
        intent = extras.parseData(intent);
        context.startActivity(intent);
    }

    public static void startActivity(@NonNull Class<? extends Activity> activityClazz, Extras extras) {
        Activity topActivity = TaskStackManager.getInstance().getTopActivity();
        if (topActivity == null) {
            throw new IllegalStateException("You need to configure dora.TaskStackGlobalConfig first.");
        }
        startActivity(topActivity, activityClazz, extras);
    }

    public static void startActivityForResult(@NonNull Activity activity, @NonNull Class<? extends Activity>
            activityClazz, Extras extras, int requestCode) {
        Intent intent = getActivityIntent(activity, activityClazz);
        intent = extras.parseData(intent);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void startActivityForResult(@NonNull Class<? extends Activity>
            activityClazz, Extras extras, int requestCode) {
        Activity topActivity = TaskStackManager.getInstance().getTopActivity();
        if (topActivity == null) {
            throw new IllegalStateException("You need to configure dora.TaskStackGlobalConfig first.");
        }
        startActivityForResult(topActivity, activityClazz, extras, requestCode);
    }

    public static void startActivityWithString(@NonNull Activity activity, Class<? extends Activity> activityClazz, String name, String extra) {
        Map<String, Object> map = new HashMap<>();
        map.put(name, extra);
        Intent intent = getActivityIntent(activity, activityClazz);
        activity.startActivity(Extras.fromMap(map).parseData(intent));
    }

    public static void startActivityWithInteger(@NonNull Activity activity, Class<? extends Activity> activityClazz, String name, int extra) {
        Map<String, Object> map = new HashMap<>();
        map.put(name, extra);
        Intent intent = getActivityIntent(activity, activityClazz);
        activity.startActivity(Extras.fromMap(map).parseData(intent));
    }

    public static void startActivityWithBoolean(@NonNull Activity activity, Class<? extends Activity> activityClazz, String name, boolean extra) {
        Map<String, Object> map = new HashMap<>();
        map.put(name, extra);
        Intent intent = getActivityIntent(activity, activityClazz);
        activity.startActivity(Extras.fromMap(map).parseData(intent));
    }

    public static void startActivityWithSerializable(@NonNull Activity activity, Class<? extends Activity> activityClazz, String name, Serializable extra) {
        Map<String, Object> map = new HashMap<>();
        map.put(name, extra);
        Intent intent = getActivityIntent(activity, activityClazz);
        activity.startActivity(Extras.fromMap(map).parseData(intent));
    }

    public static void startActivity(@NonNull Activity activity, Class<? extends Activity> activityClazz,
                                     @AnimRes int inAnim, @AnimRes int outAnim) {
        startActivity(activityClazz);
        activity.overridePendingTransition(inAnim, outAnim);
    }

    public static void finishActivity(@NonNull Activity activity, @AnimRes int inAnim, @AnimRes int outAnim) {
        activity.finish();
        activity.overridePendingTransition(inAnim, outAnim);
    }

    public static void startService(@NonNull String action) {
        startService(GlobalContext.get(), action);
    }

    public static void startService(Context context, @NonNull String action) {
        Intent intent = new Intent();
        intent.setAction(action);
        context.startService(intent);
    }

    public static void startService(@NonNull Class<? extends Service> serviceClazz) {
        startService(GlobalContext.get(), serviceClazz);
    }

    public static void startService(Context context, @NonNull Class<? extends Service> serviceClazz) {
        Intent intent = new Intent();
        intent.setClass(context, serviceClazz);
        context.startService(intent);
    }

    public static void sendBroadcast(@NonNull String action) {
        sendBroadcast(GlobalContext.get(), action);
    }

    public static void sendBroadcast(Context context, @NonNull String action) {
        Intent intent = new Intent();
        intent.setAction(action);
        context.sendBroadcast(intent);
    }

    public static void sendBroadcast(@NonNull Class<? extends BroadcastReceiver> broadcastClazz) {
        sendBroadcast(GlobalContext.get(), broadcastClazz);
    }

    public static void sendBroadcast(Context context, @NonNull Class<? extends BroadcastReceiver> broadcastClazz) {
        Intent intent = new Intent();
        intent.setClass(context, broadcastClazz);
        context.sendBroadcast(intent);
    }

    public static boolean getBooleanExtra(@NonNull Intent intent, @NonNull String name, boolean defaultValue) {
        if (!hasExtra(intent, name)) {
            return defaultValue;
        }
        return intent.getBooleanExtra(name, defaultValue);
    }

    public static byte getByteExtra(@NonNull Intent intent, @NonNull String name) {
        if (!hasExtra(intent, name)) {
            return 0;
        }
        return intent.getByteExtra(name, (byte) 0);
    }

    public static short getShortExtra(@NonNull Intent intent, @NonNull String name) {
        if (!hasExtra(intent, name)) {
            return 0;
        }
        return intent.getShortExtra(name, (short) 0);
    }

    public static char getCharExtra(@NonNull Intent intent, @NonNull String name) {
        if (!hasExtra(intent, name)) {
            return '\n';
        }
        return intent.getCharExtra(name, '\n');
    }

    public static int getIntExtra(@NonNull Intent intent, @NonNull String name) {
        if (!hasExtra(intent, name)) {
            return 0;
        }
        return intent.getIntExtra(name, 0);
    }

    public static float getFloatExtra(@NonNull Intent intent, @NonNull String name) {
        if (!hasExtra(intent, name)) {
            return 0;
        }
        return intent.getFloatExtra(name, 0);
    }

    public static double getDoubleExtra(@NonNull Intent intent, @NonNull String name) {
        if (!hasExtra(intent, name)) {
            return 0;
        }
        return intent.getDoubleExtra(name, 0);
    }

    public static String getStringExtra(@NonNull Intent intent, @NonNull String name) {
        if (!hasExtra(intent, name)) {
            return "";
        }
        return intent.getStringExtra(name);
    }

    public static CharSequence getCharSequenceExtra(@NonNull Intent intent, @NonNull String name) {
        if (!hasExtra(intent, name)) {
            return "";
        }
        return intent.getCharSequenceExtra(name);
    }

    public static <T extends Parcelable> T getParcelableExtra(@NonNull Intent intent, @NonNull String name) {
        if (!hasExtra(intent, name)) {
            return null;
        }
        return intent.getParcelableExtra(name);
    }

    public static Parcelable[] getParcelableArrayExtra(@NonNull Intent intent, @NonNull String name) {
        if (!hasExtra(intent, name)) {
            return null;
        }
        return intent.getParcelableArrayExtra(name);
    }

    public static Serializable getSerializableExtra(@NonNull Intent intent, @NonNull String name) {
        if (!hasExtra(intent, name)) {
            return null;
        }
        return intent.getSerializableExtra(name);
    }

    public static ArrayList<Integer> getIntegerArrayListExtra(@NonNull Intent intent, @NonNull String name) {
        if (!hasExtra(intent, name)) {
            return new ArrayList<>();
        }
        return intent.getIntegerArrayListExtra(name);
    }

    public static ArrayList<CharSequence> getCharSequenceArrayListExtra(@NonNull Intent intent, @NonNull String name) {
        if (!hasExtra(intent, name)) {
            return new ArrayList<>();
        }
        return intent.getCharSequenceArrayListExtra(name);
    }

    public static String[] getStringArrayExtra(@NonNull Intent intent, @NonNull String name) {
        if (!hasExtra(intent, name)) {
            return null;
        }
        return intent.getStringArrayExtra(name);
    }

    public static boolean[] getBooleanArrayExtra(@NonNull Intent intent, @NonNull String name) {
        if (!hasExtra(intent, name)) {
            return null;
        }
        return intent.getBooleanArrayExtra(name);
    }

    public static byte[] getByteArrayExtra(@NonNull Intent intent, @NonNull String name) {
        if (!hasExtra(intent, name)) {
            return null;
        }
        return intent.getByteArrayExtra(name);
    }

    public static short[] getShortArrayExtra(@NonNull Intent intent, @NonNull String name) {
        if (!hasExtra(intent, name)) {
            return null;
        }
        return intent.getShortArrayExtra(name);
    }

    public static char[] getCharArrayExtra(@NonNull Intent intent, @NonNull String name) {
        if (!hasExtra(intent, name)) {
            return null;
        }
        return intent.getCharArrayExtra(name);
    }

    public static int[] getIntArrayExtra(@NonNull Intent intent, @NonNull String name) {
        if (!hasExtra(intent, name)) {
            return null;
        }
        return intent.getIntArrayExtra(name);
    }

    public static long[] getLongArrayExtra(@NonNull Intent intent, @NonNull String name) {
        if (!hasExtra(intent, name)) {
            return null;
        }
        return intent.getLongArrayExtra(name);
    }

    public static float[] getFloatArrayExtra(@NonNull Intent intent, @NonNull String name) {
        if (!hasExtra(intent, name)) {
            return null;
        }
        return intent.getFloatArrayExtra(name);
    }

    public static double[] getDoubleArrayExtra(@NonNull Intent intent, @NonNull String name) {
        if (!hasExtra(intent, name)) {
            return null;
        }
        return intent.getDoubleArrayExtra(name);
    }

    public static CharSequence[] getCharSequenceArrayExtra(@NonNull Intent intent, @NonNull String name) {
        if (!hasExtra(intent, name)) {
            return null;
        }
        return intent.getCharSequenceArrayExtra(name);
    }

    public static Bundle getBundleExtra(@NonNull Intent intent, @NonNull String name) {
        if (!hasExtra(intent, name)) {
            return null;
        }
        return intent.getBundleExtra(name);
    }

    public long getLongExtra(@NonNull Intent intent, @NonNull String name) {
        if (!hasExtra(intent, name)) {
            return 0;
        }
        return intent.getLongExtra(name, 0);
    }

    public <T extends Parcelable> ArrayList<T> getParcelableArrayListExtra(@NonNull Intent intent, @NonNull String name) {
        if (!hasExtra(intent, name)) {
            return null;
        }
        return intent.getParcelableArrayListExtra(name);
    }

    public ArrayList<String> getStringArrayListExtra(@NonNull Intent intent, @NonNull String name) {
        if (!hasExtra(intent, name)) {
            return new ArrayList<>();
        }
        return intent.getStringArrayListExtra(name);
    }

    /**
     * Return the intent of launch app details settings.
     * 简体中文：返回启动应用程序详细设置的意图。
     *
     * @param pkgName The name of the package.
     * @return the intent of launch app details settings
     */
    public static Intent getLaunchAppDetailsSettingsIntent(final String pkgName) {
        return getLaunchAppDetailsSettingsIntent(pkgName, false);
    }

    /**
     * Return the intent of launch app details settings.
     * 简体中文：返回启动应用程序详细设置的意图。
     *
     * @param pkgName The name of the package.
     * @return the intent of launch app details settings
     */
    public static Intent getLaunchAppDetailsSettingsIntent(final String pkgName, final boolean isNewTask) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + pkgName));
        if (isNewTask) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        return intent;
    }

    public static class Extras implements Serializable {

        private static Extras instance;
        private Map<String, Object> extrasMap;

        private Extras(Map<String, Object> map) {
            this.extrasMap = map;
        }

        public static Extras fromMap(Map<String, Object> map) {
            instance = new Extras(map);
            return instance;
        }

        public Bundle convertToBundle() {
            Bundle bundle = new Bundle();
            Set<String> keys = extrasMap.keySet();
            for (String key : keys) {
                Object value = extrasMap.get(key);
                if (String.class.isAssignableFrom(value.getClass())) {
                    String val = (String) value;
                    bundle.putString(key, val);
                } else if (int.class.isAssignableFrom(value.getClass())) {
                    int val = (int) value;
                    bundle.putInt(key, val);
                } else if (Serializable.class.isAssignableFrom(value.getClass())) {
                    Serializable val = (Serializable) value;
                    bundle.putSerializable(key, val);
                } else if (boolean.class.isAssignableFrom(value.getClass())) {
                    boolean val = (boolean) value;
                    bundle.putBoolean(key, val);
                } else if (long.class.isAssignableFrom(value.getClass())) {
                    long val = (long) value;
                    bundle.putLong(key, val);
                } else if (short.class.isAssignableFrom(value.getClass())) {
                    short val = (short) value;
                    bundle.putShort(key, val);
                } else if (byte.class.isAssignableFrom(value.getClass())) {
                    byte val = (byte) value;
                    bundle.putByte(key, val);
                } else if (float.class.isAssignableFrom(value.getClass())) {
                    float val = (float) value;
                    bundle.putFloat(key, val);
                } else if (double.class.isAssignableFrom(value.getClass())) {
                    double val = (double) value;
                    bundle.putDouble(key, val);
                } else if (char.class.isAssignableFrom(value.getClass())) {
                    char val = (char) value;
                    bundle.putChar(key, val);
                } else if (int[].class.isAssignableFrom(value.getClass())) {
                    int[] val = (int[]) value;
                    bundle.putIntArray(key, val);
                } else if (char[].class.isAssignableFrom(value.getClass())) {
                    char[] val = (char[]) value;
                    bundle.putCharArray(key, val);
                } else if (byte[].class.isAssignableFrom(value.getClass())) {
                    byte[] val = (byte[]) value;
                    bundle.putByteArray(key, val);
                } else if (short[].class.isAssignableFrom(value.getClass())) {
                    short[] val = (short[]) value;
                    bundle.putShortArray(key, val);
                } else if (long[].class.isAssignableFrom(value.getClass())) {
                    long[] val = (long[]) value;
                    bundle.putLongArray(key, val);
                } else if (float[].class.isAssignableFrom(value.getClass())) {
                    float[] val = (float[]) value;
                    bundle.putFloatArray(key, val);
                } else if (double[].class.isAssignableFrom(value.getClass())) {
                    double[] val = (double[]) value;
                    bundle.putDoubleArray(key, val);
                } else if (Serializable[].class.isAssignableFrom(value.getClass())) {
                    Serializable[] val = (Serializable[]) value;
                    bundle.putSerializable(key, val);
                } else if (String[].class.isAssignableFrom(value.getClass())) {
                    String[] val = (String[]) value;
                    bundle.putStringArray(key, val);
                } else if (Parcelable.class.isAssignableFrom(value.getClass())) {
                    Parcelable val = (Parcelable) value;
                    bundle.putParcelable(key, val);
                } else if (Parcelable[].class.isAssignableFrom(value.getClass())) {
                    Parcelable[] val = (Parcelable[]) value;
                    bundle.putParcelableArray(key, val);
                }
            }
            return bundle;
        }

        public Intent parseData(Intent intent) {
            Set<String> keys = extrasMap.keySet();
            for (String key : keys) {
                Object value = extrasMap.get(key);
                if (String.class.isAssignableFrom(value.getClass())) {
                    String val = (String) value;
                    intent.putExtra(key, val);
                } else if (int.class.isAssignableFrom(value.getClass())) {
                    int val = (int) value;
                    intent.putExtra(key, val);
                } else if (Serializable.class.isAssignableFrom(value.getClass())) {
                    Serializable val = (Serializable) value;
                    intent.putExtra(key, val);
                } else if (boolean.class.isAssignableFrom(value.getClass())) {
                    boolean val = (boolean) value;
                    intent.putExtra(key, val);
                } else if (long.class.isAssignableFrom(value.getClass())) {
                    long val = (long) value;
                    intent.putExtra(key, val);
                } else if (short.class.isAssignableFrom(value.getClass())) {
                    short val = (short) value;
                    intent.putExtra(key, val);
                } else if (byte.class.isAssignableFrom(value.getClass())) {
                    byte val = (byte) value;
                    intent.putExtra(key, val);
                } else if (float.class.isAssignableFrom(value.getClass())) {
                    float val = (float) value;
                    intent.putExtra(key, val);
                } else if (double.class.isAssignableFrom(value.getClass())) {
                    double val = (double) value;
                    intent.putExtra(key, val);
                } else if (char.class.isAssignableFrom(value.getClass())) {
                    char val = (char) value;
                    intent.putExtra(key, val);
                } else if (int[].class.isAssignableFrom(value.getClass())) {
                    int[] val = (int[]) value;
                    intent.putExtra(key, val);
                } else if (char[].class.isAssignableFrom(value.getClass())) {
                    char[] val = (char[]) value;
                    intent.putExtra(key, val);
                } else if (byte[].class.isAssignableFrom(value.getClass())) {
                    byte[] val = (byte[]) value;
                    intent.putExtra(key, val);
                } else if (short[].class.isAssignableFrom(value.getClass())) {
                    short[] val = (short[]) value;
                    intent.putExtra(key, val);
                } else if (long[].class.isAssignableFrom(value.getClass())) {
                    long[] val = (long[]) value;
                    intent.putExtra(key, val);
                } else if (float[].class.isAssignableFrom(value.getClass())) {
                    float[] val = (float[]) value;
                    intent.putExtra(key, val);
                } else if (double[].class.isAssignableFrom(value.getClass())) {
                    double[] val = (double[]) value;
                    intent.putExtra(key, val);
                } else if (Serializable[].class.isAssignableFrom(value.getClass())) {
                    Serializable[] val = (Serializable[]) value;
                    intent.putExtra(key, val);
                } else if (String[].class.isAssignableFrom(value.getClass())) {
                    String[] val = (String[]) value;
                    intent.putExtra(key, val);
                } else if (Parcelable.class.isAssignableFrom(value.getClass())) {
                    Parcelable val = (Parcelable) value;
                    intent.putExtra(key, val);
                } else if (Parcelable[].class.isAssignableFrom(value.getClass())) {
                    Parcelable[] val = (Parcelable[]) value;
                    intent.putExtra(key, val);
                }
            }
            return intent;
        }
    }
}
