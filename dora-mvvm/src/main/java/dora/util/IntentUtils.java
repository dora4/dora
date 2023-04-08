package dora.util;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public final class IntentUtils {

    private IntentUtils() {
    }

    public static boolean hasExtra(@NonNull Intent intent, @NonNull String key) {
        return intent.hasExtra(key);
    }

    public static boolean hasExtras(@NonNull Intent intent, @NonNull String[] keys) {
        int length = keys.length;
        int count = 0;
        for (int i = 0; i < length; i++) {
            if (hasExtra(intent, keys[i])) {
                count++;
            }
        }
        return count == length;
    }

    public static void startActivity(@NonNull Class<? extends Activity> activityClazz) {
        Activity topActivity = AppManager.getInstance().getTopActivity();
        if (topActivity != null) {
            Intent intent = new Intent(AppManager.getInstance().getTopActivity(), activityClazz);
            topActivity.startActivity(intent);
        } else throw new IllegalStateException("dora.TaskStackGlobalConfig未被配置");
    }

    public static void startActivityForResult(@NonNull Class<? extends Activity> activityClazz, int requestCode) {
        Activity topActivity = AppManager.getInstance().getTopActivity();
        if (topActivity != null) {
            Intent intent = new Intent(AppManager.getInstance().getTopActivity(), activityClazz);
            topActivity.startActivityForResult(intent, requestCode);
        } else throw new IllegalStateException("dora.TaskStackGlobalConfig未被配置");
    }

    /**
     * 使用{@link #startActivity(Class, Extras)}替代。
     *
     * @param activityClazz
     * @param bundle
     */
    @Deprecated
    public static void startActivity(@NonNull Class<? extends Activity> activityClazz, Bundle bundle) {
        Activity topActivity = AppManager.getInstance().getTopActivity();
        if (topActivity != null) {
            Intent intent = new Intent(AppManager.getInstance().getTopActivity(), activityClazz);
            intent.putExtras(bundle);
            topActivity.startActivity(intent);
        } else throw new IllegalStateException("dora.TaskStackGlobalConfig未被配置");
    }

    public static void startActivity(@NonNull Class<? extends Activity> activityClazz, Extras extras) {
        Activity topActivity = AppManager.getInstance().getTopActivity();
        if (topActivity != null) {
            Intent intent = new Intent(topActivity, activityClazz);
            intent = extras.parseData(intent);
            topActivity.startActivity(intent);
        } else throw new IllegalStateException("dora.TaskStackGlobalConfig未被配置");
    }

    public static void startActivityForResult(@NonNull Class<? extends Activity> activityClazz, Extras extras, int requestCode) {
        Activity topActivity = AppManager.getInstance().getTopActivity();
        if (topActivity != null) {
            Intent intent = new Intent(topActivity, activityClazz);
            intent = extras.parseData(intent);
            topActivity.startActivityForResult(intent, requestCode);
        } else throw new IllegalStateException("dora.TaskStackGlobalConfig未被配置");
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

    public static class Extras implements Serializable {

        private Map<String, Object> extrasMap;

        public Extras(Map<String, Object> map) {
            this.extrasMap = map;
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
