package dora.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * SharedPreferences存取相关工具。
 */
public final class SPUtils {

    private SPUtils() {
    }

    private static SharedPreferences sPreferences;
    private static SPUtils sInstance;

    private SharedPreferences.Editor getEditor() {
        return sPreferences.edit();
    }

    private SPUtils(Context context) {
        sPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static SPUtils getInstance() {
        return getInstance(GlobalContext.get());
    }

    public static SPUtils getInstance(Context context) {
        if (sInstance == null) {
            synchronized (SPUtils.class) {
                if (sInstance == null) sInstance = new SPUtils(context);
            }
        }
        return sInstance;
    }

    private void _writeString(String key, String value) {
        getEditor().putString(key, value).apply();
    }

    public static void writeString(String key, String value) {
        writeString(GlobalContext.get(), key, value);
    }

    public static void writeString(Context context, String key, String value) {
        getInstance(context)._writeString(key, value);
    }

    private String _readString(String key) {
        return sPreferences.getString(key, null);
    }

    public static String readString(String key) {
        return readString(GlobalContext.get(), key);
    }

    public static String readString(Context context, String key) {
        return getInstance(context)._readString(key);
    }

    private void _writeInteger(String key, int value) {
        getEditor().putInt(key, value).apply();
    }

    public static void writeInteger(String key, int value) {
        writeInteger(GlobalContext.get(), key, value);
    }

    public static void writeInteger(Context context, String key, int value) {
        getInstance(context)._writeInteger(key, value);
    }

    private int _readInteger(String key) {
        return sPreferences.getInt(key, 0);
    }

    public static int readInteger(String key) {
        return readInteger(GlobalContext.get(), key);
    }

    public static int readInteger(Context context, String key) {
        return getInstance(context)._readInteger(key);
    }

    private void _writeBoolean(String key, boolean value) {
        getEditor().putBoolean(key, value).apply();
    }

    public static void writeBoolean(String key, boolean value) {
        writeBoolean(GlobalContext.get(), key, value);
    }

    public static void writeBoolean(Context context, String key, boolean value) {
        getInstance(context)._writeBoolean(key, value);
    }

    private boolean _readBoolean(String key, boolean defValue) {
        return sPreferences.getBoolean(key, defValue);
    }

    public static boolean readBoolean(String key, boolean defValue) {
        return readBoolean(GlobalContext.get(), key, defValue);
    }

    public static boolean readBoolean(Context context, String key, boolean defValue) {
        return getInstance(context)._readBoolean(key, defValue);
    }

    private <T> boolean _writeObject(String key, T value) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(value);
            // 将字节流编码成base64的字符串
            String base64 = new String(Base64.encode(baos
                    .toByteArray(), Base64.DEFAULT));
            getEditor().putString(key, base64).apply();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            IoUtils.close(baos);
            IoUtils.close(oos);
        }
    }

    public static <T> boolean writeObject(String key, T value) {
        return writeObject(GlobalContext.get(), key, value);
    }

    public static <T> boolean writeObject(Context context, String key, T value) {
        return getInstance(context)._writeObject(key, value);
    }

    private <T> T _readObject(String key) {
        T value = null;
        String base64Val = sPreferences.getString(key, null);
        if (base64Val == null) {
            return null;
        }
        // 读取字节
        byte[] base64 = Base64.decode(base64Val.getBytes(), Base64.DEFAULT);
        // 封装到字节流
        ByteArrayInputStream bais = new ByteArrayInputStream(base64);
        ObjectInputStream bis;
        try {
            bis = new ObjectInputStream(bais);
            // 读取对象
            value = (T) bis.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IoUtils.close(bais);
        }
        return value;
    }

    public static <T> T readObject(String key) {
        return readObject(GlobalContext.get(), key);
    }

    public static <T> T readObject(Context context, String key) {
        return getInstance(context)._readObject(key);
    }

    private void _remove(String key) {
        getEditor().remove(key).apply();
    }

    public static void remove(String key) {
        remove(GlobalContext.get(), key);
    }

    public static void remove(Context context, String key) {
        getInstance(context)._remove(key);
    }

    private void _clear() {
        getEditor().clear().apply();
    }

    public static void clear() {
        clear(GlobalContext.get());
    }

    public static void clear(Context context) {
        getInstance(context)._clear();
    }
}
