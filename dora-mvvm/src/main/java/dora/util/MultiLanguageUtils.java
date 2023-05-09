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

    private static final String PREFS_LANGUAGE = "dora_lang";

    /**
     * 阿姆哈拉语.
     */
    public static final String LANG_AM = "am";

    /**
     * 阿拉伯语.
     */
    public static final String LANG_AR = "ar";

    /**
     * 阿萨姆语.
     */
    public static final String LANG_AS = "as";

    /**
     * 阿塞拜疆语.
     */
    public static final String LANG_AZ = "az";

    /**
     * 白俄罗斯语.
     */
    public static final String LANG_BE = "be";

    /**
     * 保加利亚语.
     */
    public static final String LANG_BG = "bg";

    /**
     * 孟加拉语.
     */
    public static final String LANG_BN = "bn";

    /**
     * 藏语.
     */
    public static final String LANG_BO = "bo";

    /**
     * 波斯尼亚语.
     */
    public static final String LANG_BS = "bs";

    /**
     * 加泰罗尼亚语.
     */
    public static final String LANG_CA = "ca";

    /**
     * 捷克语.
     */
    public static final String LANG_CS = "cs";

    /**
     * 丹麦语.
     */
    public static final String LANG_DA = "da";

    /**
     * 德语.
     */
    public static final String LANG_DE = "de";

    /**
     * 希腊语.
     */
    public static final String LANG_EL = "el";

    /**
     * 英文.
     */
    public static final String LANG_EN = "en";

    /**
     * 英语/美国.
     */
    public static final String LANG_EN_US = "en_US";

    /**
     * 西班牙语.
     */
    public static final String LANG_ES = "es";

    /**
     * 西班牙语/拉美.
     */
    public static final String LANG_ES_LA = "es_LA";

    /**
     * 爱沙尼亚语.
     */
    public static final String LANG_ET = "et";

    /**
     * 巴斯克语.
     */
    public static final String LANG_EU = "eu";

    /**
     * 波斯语.
     */
    public static final String LANG_FA = "fa";

    /**
     * 芬兰语.
     */
    public static final String LANG_FI = "fi";

    /**
     * 法语.
     */
    public static final String LANG_FR = "fr";

    /**
     * 加利西亚语.
     */
    public static final String LANG_GL = "gl";

    /**
     * 古吉拉特语.
     */
    public static final String LANG_GU = "gu";

    /**
     * 印地语.
     */
    public static final String LANG_HI = "hi";

    /**
     * 克罗地亚语.
     */
    public static final String LANG_HR = "hr";

    /**
     * 匈牙利语
     */
    public static final String LANG_HU = "hu";

    /**
     * 印尼语.
     */
    public static final String LANG_ID = "id";

    /**
     * 意大利语.
     */
    public static final String LANG_IT = "it";

    /**
     * 希伯来语.
     */
    public static final String LANG_HE = "he";

    /**
     * 日文.
     */
    public static final String LANG_JA = "ja";

    /**
     * 爪哇语.
     */
    public static final String LANG_JV = "jv";

    /**
     * 格鲁吉亚语.
     */
    public static final String LANG_KA = "ka";

    /**
     * 哈萨克语.
     */
    public static final String LANG_KK = "kk";

    /**
     * 高棉语.
     */
    public static final String LANG_KM = "km";

    /**
     * 卡纳达语.
     */
    public static final String LANG_KN = "kn";

    /**
     * 韩文.
     */
    public static final String LANG_KO = "ko";

    /**
     * 老挝语.
     */
    public static final String LANG_LO = "lo";

    /**
     * 立陶宛语.
     */
    public static final String LANG_LT = "lt";

    /**
     * 拉脱维亚语.
     */
    public static final String LANG_LV = "lv";

    /**
     * 迈蒂利语.
     */
    public static final String LANG_MAI = "mai";

    /**
     * 毛利语.
     */
    public static final String LANG_MI = "mi";

    /**
     * 马其顿语.
     */
    public static final String LANG_MK = "mk";

    /**
     * 马拉亚拉姆语.
     */
    public static final String LANG_ML = "ml";

    /**
     * 蒙古语.
     */
    public static final String LANG_MN = "mn";

    /**
     * 马拉地语.
     */
    public static final String LANG_MR = "mr";

    /**
     * 马来西亚语.
     */
    public static final String LANG_MS = "ms";

    /**
     * 缅甸语.
     */
    public static final String LANG_MY = "my";

    /**
     * 挪威语.
     */
    public static final String LANG_NO = "no";

    /**
     * 尼泊尔语.
     */
    public static final String LANG_NE = "ne";

    /**
     * 荷兰语.
     */
    public static final String LANG_NL = "nl";

    /**
     * 欧里亚语.
     */
    public static final String LANG_OR = "or";

    /**
     * 旁遮普语.
     */
    public static final String LANG_PA = "pa";

    /**
     * 波兰语.
     */
    public static final String LANG_PL = "pl";

    /**
     * 葡萄牙语.
     */
    public static final String LANG_PT = "pt";

    /**
     * 葡萄牙语/巴西.
     */
    public static final String LANG_PT_BR = "pt_BR";

    /**
     * 罗马西亚语.
     */
    public static final String LANG_RO = "ro";

    /**
     * 俄语.
     */
    public static final String LANG_RU = "ru";

    /**
     * 僧加罗语.
     */
    public static final String LANG_SI = "si";

    /**
     * 斯洛伐克语.
     */
    public static final String LANG_SK = "sk";

    /**
     * 斯洛文尼亚语.
     */
    public static final String LANG_SL = "sl";

    /**
     * 塞尔维亚语.
     */
    public static final String LANG_SR = "sr";

    /**
     * 瑞典语.
     */
    public static final String LANG_SV = "sv";

    /**
     * 斯瓦希里语.
     */
    public static final String LANG_SW = "sw";

    /**
     * 泰米尔语.
     */
    public static final String LANG_TA = "ta";

    /**
     * 泰卢固语.
     */
    public static final String LANG_TE = "te";

    /**
     * 泰语.
     */
    public static final String LANG_TH = "th";

    /**
     * 菲律宾语.
     */
    public static final String LANG_FIL = "fil";

    /**
     * 土耳其语.
     */
    public static final String LANG_TR = "tr";

    /**
     * 维吾尔语.
     */
    public static final String LANG_UG = "ug";

    /**
     * 乌克兰语.
     */
    public static final String LANG_UK = "uk";

    /**
     * 乌尔都语.
     */
    public static final String LANG_UR = "ur";

    /**
     * 乌兹别克语.
     */
    public static final String LANG_UZ = "uz";

    /**
     * 越南语.
     */
    public static final String LANG_VI = "vi";

    /**
     * 中文简体.
     */
    public static final String LANG_ZH_CN = "zh_CN";

    /**
     * 中文繁体/香港.
     */
    public static final String LANG_ZH_HK = "zh_HK";

    /**
     * 中文繁体/台湾.
     */
    public static final String LANG_ZH_TW = "zh_TW";//

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
     * 在Application和Activity中替换Context。
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

    private static void onUpdateConfiguration(Context context) {
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
        } else if (lang.equals(LANG_AM)) {
            return new Locale("am", "");
        } else if (lang.equals(LANG_AR)) {
            return new Locale("ar", "");
        } else if (lang.equals(LANG_AS)) {
            return new Locale("as", "");
        } else if (lang.equals(LANG_AZ)) {
            return new Locale("az", "");
        } else if (lang.equals(LANG_BE)) {
            return new Locale("be", "");
        } else if (lang.equals(LANG_BG)) {
            return new Locale("bg", "");
        } else if (lang.equals(LANG_BN)) {
            return new Locale("bn", "");
        } else if (lang.equals(LANG_BO)) {
            return new Locale("bo", "");
        } else if (lang.equals(LANG_BS)) {
            return new Locale("bs", "");
        } else if (lang.equals(LANG_CA)) {
            return new Locale("ca", "");
        } else if (lang.equals(LANG_CS)) {
            return new Locale("cs", "");
        } else if (lang.equals(LANG_DA)) {
            return new Locale("da", "");
        } else if (lang.equals(LANG_DE)) {
            return Locale.GERMAN;
        } else if (lang.equals(LANG_EL)) {
            return new Locale("el", "");
        } else if (lang.equals(LANG_EN)) {
            return Locale.ENGLISH;
        } else if (lang.equals(LANG_EN_US)) {
            return Locale.US;
        } else if (lang.equals(LANG_ES)) {
            return new Locale("es", "");
        } else if (lang.equals(LANG_ES_LA)) {
            return new Locale("es", "LA");
        } else if (lang.equals(LANG_ET)) {
            return new Locale("et", "");
        } else if (lang.equals(LANG_EU)) {
            return new Locale("eu", "");
        } else if (lang.equals(LANG_FA)) {
            return new Locale("fa", "");
        } else if (lang.equals(LANG_FI)) {
            return new Locale("fi", "");
        } else if (lang.equals(LANG_FR)) {
            return Locale.FRENCH;
        } else if (lang.equals(LANG_GL)) {
            return new Locale("gl", "");
        } else if (lang.equals(LANG_GU)) {
            return new Locale("gu", "");
        } else if (lang.equals(LANG_HI)) {
            return new Locale("hi", "");
        } else if (lang.equals(LANG_HR)) {
            return new Locale("hr", "");
        } else if (lang.equals(LANG_HU)) {
            return new Locale("hu", "");
        } else if (lang.equals(LANG_ID)) {
            return new Locale("id", "");
        } else if (lang.equals(LANG_IT)) {
            return Locale.ITALIAN;
        } else if (lang.equals(LANG_HE)) {
            return new Locale("he", "");
        } else if (lang.equals(LANG_JA)) {
            return Locale.JAPANESE;
        } else if (lang.equals(LANG_JV)) {
            return new Locale("jv", "");
        } else if (lang.equals(LANG_KA)) {
            return new Locale("ka", "");
        } else if (lang.equals(LANG_KK)) {
            return new Locale("kk", "");
        } else if (lang.equals(LANG_KM)) {
            return new Locale("km", "");
        } else if (lang.equals(LANG_KN)) {
            return new Locale("km", "");
        } else if (lang.equals(LANG_KO)) {
            return Locale.KOREAN;
        } else if (lang.equals(LANG_LO)) {
            return new Locale("lo", "");
        } else if (lang.equals(LANG_LT)) {
            return new Locale("lt", "");
        } else if (lang.equals(LANG_LV)) {
            return new Locale("lv", "");
        } else if (lang.equals(LANG_MAI)) {
            return new Locale("mai", "");
        } else if (lang.equals(LANG_MI)) {
            return new Locale("mi", "");
        } else if (lang.equals(LANG_MK)) {
            return new Locale("mk", "");
        } else if (lang.equals(LANG_ML)) {
            return new Locale("ml", "");
        } else if (lang.equals(LANG_MN)) {
            return new Locale("mn", "");
        } else if (lang.equals(LANG_MR)) {
            return new Locale("mr", "");
        } else if (lang.equals(LANG_MS)) {
            return new Locale("ms", "");
        } else if (lang.equals(LANG_MY)) {
            return new Locale("my", "");
        } else if (lang.equals(LANG_NO)) {
            return new Locale("no", "");
        } else if (lang.equals(LANG_NE)) {
            return new Locale("ne", "");
        } else if (lang.equals(LANG_NL)) {
            return new Locale("nl", "");
        } else if (lang.equals(LANG_OR)) {
            return new Locale("or", "");
        } else if (lang.equals(LANG_PA)) {
            return new Locale("pa", "");
        } else if (lang.equals(LANG_PL)) {
            return new Locale("pl", "");
        } else if (lang.equals(LANG_PT)) {
            return new Locale("pt", "");
        } else if (lang.equals(LANG_PT_BR)) {
            return new Locale("pt", "BR");
        } else if (lang.equals(LANG_RO)) {
            return new Locale("ro", "");
        } else if (lang.equals(LANG_RU)) {
            return new Locale("ru", "");
        } else if (lang.equals(LANG_SI)) {
            return new Locale("si", "");
        } else if (lang.equals(LANG_SK)) {
            return new Locale("sk", "");
        } else if (lang.equals(LANG_SL)) {
            return new Locale("sl", "");
        } else if (lang.equals(LANG_SR)) {
            return new Locale("sr", "");
        } else if (lang.equals(LANG_SV)) {
            return new Locale("sv", "");
        } else if (lang.equals(LANG_SW)) {
            return new Locale("sw", "");
        } else if (lang.equals(LANG_TA)) {
            return new Locale("ta", "");
        } else if (lang.equals(LANG_TE)) {
            return new Locale("te", "");
        } else if (lang.equals(LANG_TH)) {
            return new Locale("th", "");
        } else if (lang.equals(LANG_FIL)) {
            return new Locale("fil", "");
        } else if (lang.equals(LANG_TR)) {
            return new Locale("tr", "");
        } else if (lang.equals(LANG_UG)) {
            return new Locale("ug", "");
        } else if (lang.equals(LANG_UK)) {
            return new Locale("uk", "");
        } else if (lang.equals(LANG_UR)) {
            return new Locale("ur", "");
        } else if (lang.equals(LANG_UZ)) {
            return new Locale("uz", "");
        } else if (lang.equals(LANG_VI)) {
            return new Locale("vi", "");
        } else if (lang.equals(LANG_ZH_CN)) {
            return Locale.SIMPLIFIED_CHINESE;
        } else if (lang.equals(LANG_ZH_HK)) {
            return Locale.TRADITIONAL_CHINESE;
        } else if (lang.equals(LANG_ZH_TW)) {
            return Locale.TAIWAN;
        } else {
            return Locale.ENGLISH;
        }
    }

    public static Locale getSysLocale() {
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
        SPUtils.writeStringAsync(context, MultiLanguageUtils.PREFS_LANGUAGE, lang);
        onUpdateConfiguration(context);
    }

    /**
     * 获取到用户保存的语言类型。
     */
    public static String getLangTag(Context context) {
        return SPUtils.readString(context, MultiLanguageUtils.PREFS_LANGUAGE, "");
    }

    /**
     * 清除用户保存的语言类型。
     */
    public static void clearLangTag(Context context) {
        SPUtils.clearAsync(context);
    }
}
