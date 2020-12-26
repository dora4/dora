package dora.util;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

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
        Intent intent = new Intent(GlobalContext.get(), activityClazz);
        GlobalContext.get().startActivity(intent);
    }

    public static void startActivity(@NonNull Class<? extends Activity> activityClazz, String name, Serializable serializable) {
        Intent intent = new Intent(GlobalContext.get(), activityClazz);
        intent.putExtra(name, serializable);
        GlobalContext.get().startActivity(intent);
    }

    public static void startService(@NonNull String action) {
        Intent intent = new Intent();
        intent.setAction(action);
        GlobalContext.get().startService(intent);
    }

    public static void startService(@NonNull Class<? extends Service> serviceClazz) {
        Intent intent = new Intent();
        intent.setClass(GlobalContext.get(), serviceClazz);
        GlobalContext.get().startService(intent);
    }

    public static void sendBroadcast(@NonNull String action) {
        Intent intent = new Intent();
        intent.setAction(action);
        GlobalContext.get().sendBroadcast(intent);
    }

    public static void sendBroadcast(@NonNull Class<? extends BroadcastReceiver> broadcastClazz) {
        Intent intent = new Intent();
        intent.setClass(GlobalContext.get(), broadcastClazz);
        GlobalContext.get().sendBroadcast(intent);
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
}
