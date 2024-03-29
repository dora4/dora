/*
 * Copyright (C) 2023 The Dora Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
 * SharedPreferences storage and retrieval related utility.
 * 简体中文：SharedPreferences存取相关工具。
 */
public final class SPUtils {

    private SPUtils() {
    }

    private static SharedPreferences sPreferences;
    private static SPUtils sInstance;

    private SharedPreferences.Editor getEditor() {
        return sPreferences.edit();
    }

    public static SharedPreferences getPreferences() {
        return sPreferences;
    }

    private SPUtils(Context context) {
        sPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    private static SPUtils getInstance(Context context) {
        if (sInstance == null) {
            synchronized (SPUtils.class) {
                if (sInstance == null) sInstance = new SPUtils(context);
            }
        }
        return sInstance;
    }

    public static boolean hasKey(Context context, String key) {
        getInstance(context);
        return getPreferences().contains(key);
    }

    public static void writeString(Context context, String key, String value) {
        getInstance(context).getEditor().putString(key, value).apply();
    }

    public static void writeStringSync(Context context, String key, String value) {
        getInstance(context).getEditor().putString(key, value).commit();
    }

    public static String readString(Context context, String key, String defValue) {
        getInstance(context);
        return getPreferences().getString(key, defValue);
    }

    public static String readString(Context context, String key) {
        return readString(context, key, null);
    }

    public static void writeInteger(Context context, String key, int value) {
        getInstance(context).getEditor().putInt(key, value).apply();
    }

    public static void writeIntegerSync(Context context, String key, int value) {
        getInstance(context).getEditor().putInt(key, value).commit();
    }

    public static int readInteger(Context context, String key, int defValue) {
        getInstance(context);
        return getPreferences().getInt(key, defValue);
    }

    public static int readInteger(Context context, String key) {
        return readInteger(context, key, 0);
    }

    public static void writeBoolean(Context context, String key, boolean value) {
        getInstance(context).getEditor().putBoolean(key, value).apply();
    }

    public static void writeBooleanSync(Context context, String key, boolean value) {
        getInstance(context).getEditor().putBoolean(key, value).commit();
    }

    public static boolean readBoolean(Context context, String key, boolean defValue) {
        getInstance(context);
        return getPreferences().getBoolean(key, defValue);
    }

    public static boolean readBoolean(Context context, String key) {
        return readBoolean(context, key, false);
    }

    public <T> boolean writeObject(Context context, String key, T value) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(value);
            // 将字节流编码成base64的字符串
            String base64 = new String(Base64.encode(baos
                    .toByteArray(), Base64.DEFAULT));
            getInstance(context).getEditor().putString(key, base64).apply();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            IoUtils.close(baos);
            IoUtils.close(oos);
        }
    }

    public <T> boolean writeObjectSync(Context context, String key, T value) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(value);
            String base64 = new String(Base64.encode(baos
                    .toByteArray(), Base64.DEFAULT));
            getInstance(context).getEditor().putString(key, base64).commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            IoUtils.close(baos);
            IoUtils.close(oos);
        }
    }

    public <T> T readObject(Context context, String key, String defValue) {
        getInstance(context);
        T value = null;
        String base64Val = getPreferences().getString(key, defValue);
        if (base64Val == null) {
            return null;
        }
        byte[] base64 = Base64.decode(base64Val.getBytes(), Base64.DEFAULT);
        ByteArrayInputStream bais = new ByteArrayInputStream(base64);
        ObjectInputStream bis;
        try {
            bis = new ObjectInputStream(bais);
            value = (T) bis.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IoUtils.close(bais);
        }
        return value;
    }

    public static void remove(Context context, String key) {
        getInstance(context).getEditor().remove(key).apply();
    }

    public static void removeSync(Context context, String key) {
        getInstance(context).getEditor().remove(key).commit();
    }

    public static void clear(Context context) {
        getInstance(context).getEditor().clear().apply();
    }

    public static void clearSync(Context context) {
        getInstance(context).getEditor().clear().commit();
    }
}
