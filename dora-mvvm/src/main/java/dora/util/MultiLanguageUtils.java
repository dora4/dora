package dora.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.util.DisplayMetrics;

import java.util.Locale;

/**
 * 多语言与国际化相关工具。
 */
public final class MultiLanguageUtils {

    private static MultiLanguageUtils sInstance;

    public static final String PREFS_LANGUAGE = "dora_lang";

    public static final String ENGLISH = "en";

    public static final String FRENCH = "fr";

    public static final String GERMAN = "de";

    public static final String ITALIAN = "it";

    public static final String JAPANESE = "ja";

    public static final String KOREAN = "ko";

    public static final String SIMPLIFIED_CHINESE = "zh_CN";

    public static final String TRADITIONAL_CHINESE = "zh_TW";

    private MultiLanguageUtils() {
    }

    private static MultiLanguageUtils getInstance() {
        if (sInstance == null) {
            synchronized (MultiLanguageUtils.class) {
                if (sInstance == null) {
                    sInstance = new MultiLanguageUtils();
                }
            }
        }
        return sInstance;
    }

    /**
     * 在Application中替换Context。
     */
    public static Context attachBaseContext(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return createConfigurationResources(context);
        } else {
            onUpdateConfiguration(context);
            return context;
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    private static Context createConfigurationResources(Context context) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        Locale locale = getInstance().getSavedLangLocale(context);
        configuration.setLocale(locale);
        return context.createConfigurationContext(configuration);
    }

    /**
     * 在Activity的onResume()和Fragment的setUserVisibleHint()中调用。
     */
    public static void onUpdateConfiguration(Context context) {
        getInstance().updateConfiguration(context);
    }

    private void updateConfiguration(Context context) {
        Locale targetLocale = getInstance().getSavedLangLocale(context);
        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(targetLocale);
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        resources.updateConfiguration(configuration, dm);
    }

    private Locale getSavedLangLocale(Context context) {
        String lang = SPUtils.readString(context, MultiLanguageUtils.PREFS_LANGUAGE, "");
        if (TextUtils.isEmpty(lang)) {  // 跟随系统
            return getSysLocale();
        } else if (lang.equals(ENGLISH)) {
            return Locale.ENGLISH;
        } else if (lang.equals(SIMPLIFIED_CHINESE)) {
            return Locale.SIMPLIFIED_CHINESE;
        } else if (lang.equals(TRADITIONAL_CHINESE)) {
            return Locale.TRADITIONAL_CHINESE;
        } else if (lang.equals(FRENCH)) {
            return Locale.FRENCH;
        } else if (lang.equals(GERMAN)) {
            return Locale.GERMAN;
        } else if (lang.equals(ITALIAN)) {
            return Locale.ITALIAN;
        } else if (lang.equals(JAPANESE)) {
            return Locale.JAPANESE;
        } else if (lang.equals(KOREAN)) {
            return Locale.KOREAN;
        } else {
            return Locale.ENGLISH;
        }
    }

    private Locale getSysLocale() {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = LocaleList.getDefault().get(0);
        } else {
            locale = Locale.getDefault();
        }
        return locale;
    }

    /**
     * 更新语言。
     */
    public static void updateLang(Context context, String lang) {
        SPUtils.writeString(context, MultiLanguageUtils.PREFS_LANGUAGE, lang);
        onUpdateConfiguration(context);
    }

    /**
     * 获取到用户保存的语言类型。
     */
    public static String getLangTag(Context context) {
        return SPUtils.readString(context, MultiLanguageUtils.PREFS_LANGUAGE, "");
    }
}
