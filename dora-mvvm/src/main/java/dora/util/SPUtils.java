package dora.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

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

    private static SPUtils getInstance() {
        if (sInstance == null) {
            synchronized (SPUtils.class) {
                if (sInstance == null) sInstance = new SPUtils(GlobalContext.get());
            }
        }
        return sInstance;
    }

    private void _putString(String key, String value) {
        getEditor().putString(key, value).apply();
    }

    public static void putString(String key, String value) {
        getInstance()._putString(key, value);
    }

    private String _obtainString(String key) {
        return sPreferences.getString(key, null);
    }

    public static String obtainString(String key) {
        return getInstance()._obtainString(key);
    }

    private void _putInteger(String key, int value) {
        getEditor().putInt(key, value).apply();
    }

    public static void putInteger(String key, int value) {
        getInstance()._putInteger(key, value);
    }

    private int _obtainInteger(String key) {
        return sPreferences.getInt(key, 0);
    }

    public static int obtainInteger(String key) {
        return getInstance()._obtainInteger(key);
    }

    private void _putBoolean(String key, boolean value) {
        getEditor().putBoolean(key, value).apply();
    }

    public static void putBoolean(String key, boolean value) {
        getInstance()._putBoolean(key, value);
    }

    private boolean _obtainBoolean(String key, boolean defValue) {
        return sPreferences.getBoolean(key, defValue);
    }

    public static boolean obtainBoolean(String key, boolean defValue) {
        return getInstance()._obtainBoolean(key, defValue);
    }

    private <T> boolean _putObject(String key, T value) {
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

    public static <T> boolean putObject(String key, T value) {
        return getInstance()._putObject(key, value);
    }

    private <T> T _obtainObject(String key) {
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

    public static <T> T obtainObject(String key) {
        return getInstance()._obtainObject(key);
    }

    private void _remove(String key) {
        getEditor().remove(key).apply();
    }

    public static void remove(String key) {
        getInstance()._remove(key);
    }

    private void _clear() {
        getEditor().clear().apply();
    }

    public static void clear() {
        getInstance()._clear();
    }
}
