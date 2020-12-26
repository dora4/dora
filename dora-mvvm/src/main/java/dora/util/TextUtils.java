package dora.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;

public final class TextUtils {

    private TextUtils() {
    }

    // <editor-folder desc="编码转换">

    public static String toASCIIString(String str) {
        try {
            return transferCharset(str, "US-ASCII");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String toISO8859_1String(String str) {
        try {
            return transferCharset(str, "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String toUTF8String(String str) {
        try {
            return transferCharset(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String toUTF16BEString(String str) {
        try {
            return transferCharset(str, "UTF-16BE");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String toUTF16LEString(String str) {
        try {
            return transferCharset(str, "UTF-16LE");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String toUTF16String(String str) {
        try {
            return transferCharset(str, "UTF-16");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String toGBKString(String str) {
        try {
            return transferCharset(str, "GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String toGB2312String(String str) {
        try {
            return transferCharset(str, "GB2312");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    private static String transferCharset(String str, String newCharset)
            throws UnsupportedEncodingException {
        if (str != null) {
            byte[] bs = str.getBytes();
            return new String(bs, newCharset);
        }
        return null;
    }

    // </editor-folder>

    // <editor-folder desc="字符格式校验或比对">

    public static boolean isEmpty(CharSequence str) {
        return android.text.TextUtils.isEmpty(str);
    }

    public static boolean isNotEmpty(CharSequence str) {
        return !isEmpty(str);
    }

    public static boolean checkAllEmpty(String... text) {
        for (String element : text) {
            boolean isSucceed = isEmpty(element);
            if (!isSucceed) {
                return false;
            }
        }
        return true;
    }

    public static String combineString(Iterator<String> strings) {
        return combineString(strings, "");
    }

    public static String combineString(Iterator<String> strings, String separator) {
        StringBuilder sb = new StringBuilder();
        while (strings.hasNext()) {
            String next = strings.next();
            sb.append(next).append(separator);
        }
        return sb.toString().substring(0, sb.length() - 1);
    }

    public static boolean checkAllNotEmpty(String... text) {
        for (String element : text) {
            boolean isSucceed = isNotEmpty(element);
            if (!isSucceed) {
                return false;
            }
        }
        return true;
    }

    public static boolean isEqualTo(String lhss, String rhss) {
        return android.text.TextUtils.equals(lhss, rhss);
    }

    public static boolean isNotEqualTo(String lhss, String rhss) {
        return !isEqualTo(lhss, rhss);
    }

    public static boolean match(String text, String regex) {
        return RegexUtils.match(text, regex);
    }

    // </editor-folder>
}
