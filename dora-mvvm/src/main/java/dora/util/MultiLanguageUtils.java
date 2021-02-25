package dora.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.Locale;

public class MultiLanguageUtils {

    public static final String SAVE_LANGUAGE = "dora.util.LanguageUtils";
    private static final String TAG = "LanguageUtils";
    private static MultiLanguageUtils instance;
    private Context mContext;

    private MultiLanguageUtils(Context context) {
        this.mContext = context;
    }

    public interface LanguageType {
        int LANGUAGE_FOLLOW_SYSTEM = 0; //跟随系统
        int LANGUAGE_RW = 1;    //繁体中文
        int LANGUAGE_CHINESE_SIMPLIFIED = 2; //简体
    }

    public static void init(Context context) {
        if (instance == null) {
            synchronized (MultiLanguageUtils.class) {
                if (instance == null) {
                    instance = new MultiLanguageUtils(context.getApplicationContext());
                }
            }
        }
    }

    public static MultiLanguageUtils getInstance() {
        if (instance == null) {
            throw new IllegalStateException("You must be init MultiLanguageUtil first");
        }
        return instance;
    }

    public static Context attachBaseContext(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return createConfigurationResources(context);
        } else {
            MultiLanguageUtils.getInstance().setConfiguration();
            return context;
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    private static Context createConfigurationResources(Context context) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        Locale locale = getInstance().getLanguageLocale();
        configuration.setLocale(locale);
        return context.createConfigurationContext(configuration);
    }

    /**
     * 设置语言
     */
    public void setConfiguration() {
        Locale targetLocale = getLanguageLocale();
        Configuration configuration = mContext.getResources().getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(targetLocale);
        } else {
            configuration.locale = targetLocale;
        }
        Resources resources = mContext.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        resources.updateConfiguration(configuration, dm);//语言更换生效的代码!
    }

    /**
     * 如果不是英文、简体中文、繁体中文，默认返回简体中文
     *
     * @return
     */
    private Locale getLanguageLocale() {
        int languageType = SPUtils.obtainInteger(MultiLanguageUtils.SAVE_LANGUAGE);
        if (languageType == LanguageType.LANGUAGE_FOLLOW_SYSTEM) {
            return Locale.TRADITIONAL_CHINESE;
        } else if (languageType == LanguageType.LANGUAGE_RW) {
            return Locale.TRADITIONAL_CHINESE;
        } else if (languageType == LanguageType.LANGUAGE_CHINESE_SIMPLIFIED) {
            return Locale.SIMPLIFIED_CHINESE;
        }
        getSystemLanguage(getSysLocale());
        Log.e(TAG, "getLanguageLocale" + languageType + languageType);
        return Locale.TRADITIONAL_CHINESE;
    }

    private String getSystemLanguage(Locale locale) {
        return locale.getLanguage() + "_" + locale.getCountry();

    }

    //以上获取方式需要特殊处理一下
    public Locale getSysLocale() {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = LocaleList.getDefault().get(0);
        } else {
            locale = Locale.getDefault();
        }
        return locale;
    }

    /**
     * 更新语言
     *
     * @param languageType
     */
    public void updateLanguage(int languageType) {
        SPUtils.putInteger(MultiLanguageUtils.SAVE_LANGUAGE, languageType);
        MultiLanguageUtils.getInstance().setConfiguration();
    }

    /**
     * 获取到用户保存的语言类型
     *
     * @return
     */
    public int getLanguageType() {
        int languageType = SPUtils.obtainInteger(MultiLanguageUtils.SAVE_LANGUAGE);
        if (languageType == LanguageType.LANGUAGE_CHINESE_SIMPLIFIED) {
            return LanguageType.LANGUAGE_CHINESE_SIMPLIFIED;
        } else if (languageType == LanguageType.LANGUAGE_FOLLOW_SYSTEM) {
            return LanguageType.LANGUAGE_FOLLOW_SYSTEM;
        }
        Log.e(TAG, "getLanguageType" + languageType);
        return languageType;
    }
}
