package dora.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public final class Math implements Number {

    private Math() {
    }

    // <editor-folder desc="进制转换">

    public static int H2D(String hexadecimal) {
        return Integer.parseInt(hexadecimal, SIXTEEN);
    }

    public static int O2D(String octal) {
        return Integer.parseInt(octal, EIGHT);
    }

    public static int B2D(String binary) {
        return Integer.parseInt(binary, TWO);
    }

    public static String D2B(int decimal) {
        return Integer.toBinaryString(decimal);
    }

    public static String D2O(int decimal) {
        return Integer.toOctalString(decimal);
    }

    public static String D2H(int decimal) {
        return Integer.toHexString(decimal);
    }

    public static String H2B(String hexadecimal) {
        return D2B(H2D(hexadecimal));
    }

    public static String H2O(String hexadecimal) {
        return D2O(H2D(hexadecimal));
    }

    public static String O2H(String octal) {
        return D2H(O2D(octal));
    }

    public static String O2B(String octal) {
        return D2B(O2D(octal));
    }

    public static String B2O(String binary) {
        return D2O(B2D(binary));
    }

    public static String B2H(String binary) {
        return D2H(B2D(binary));
    }

    public static String b2H(byte b) {
        String H = Integer.toHexString(b & 0xFF);
        if (H.length() == 1) {
            H = '0' + H;
        }
        return H;
    }

    public static String bs2H(byte[] src, String separator) {
        if (src == null) {
            return null;
        }
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < src.length; i++) {
            int value = src[i] & 0xFF;
            String H = D2H(value);
            if (H.length() < 2) {
                buffer.append(0);
            }
            buffer.append(H).append(separator);
        }
        return buffer.substring(0, buffer.length() - separator.length());
    }

    public static String bs2H(byte[] src) {
        return bs2H(src, "");
    }

    public static byte[] H2bs(String H, String separator) {
        if (separator != null) {
            String[] HS = H.split(separator);
            byte[] bs = new byte[HS.length];
            int i = 0;
            for (String b : HS) {
                bs[i++] = Integer.valueOf(b, 16).byteValue();
            }
            return bs;
        }
        throw new IllegalArgumentException("Separator can\'t be null.");
    }

    // </editor-folder>

    // <editor-folder desc="给字符串的前后补0">

    public static String zeroH(String num, int requiredLength) {
        if (requiredLength < 0) {
            throw new IllegalArgumentException("requiredLength must be more than 0.");
        }
        StringBuffer sb = new StringBuffer();
        if (requiredLength > num.length()) {
            int length = requiredLength - num.length();
            for (int i = 0; i < length; i++) {
                sb.append(0);
            }
        }
        return sb.append(num).toString();
    }

    public static String zeroL(String num, int requiredLength) {
        if (requiredLength < 0) {
            throw new IllegalArgumentException("requiredLength must be more than 0.");
        }
        StringBuffer sb = new StringBuffer(num);
        if (requiredLength > num.length()) {
            int length = requiredLength - num.length();
            for (int i = 0; i < length; i++) {
                sb.append(0);
            }
        }
        return sb.toString();
    }

    // </editor-folder>

    // <editor-folder desc="随机数生成">

    public static int getRandom(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

    public static int[] getRandoms(int min, int max, int count) {
        int[] randoms = new int[count];
        if (max < min || count <= 0) {
            return null;
        } else {
            for (int i = 0; i < count; i++) {
                randoms[i] = getRandom(min, max);
            }
            return randoms;
        }
    }

    public static int[] getUniqueRandoms(int min, int max, int count) {
        int[] randoms = new int[count];
        List<Integer> randomList = new ArrayList<>();
        if (count > (max - min + 1)) {
            return null;
        }
        for (int i = min; i <= max; i++) {
            randomList.add(i);
        }
        for (int i = 0; i < count; i++) {
            int index = getRandom(0, randomList.size() - 1);
            randoms[i] = randomList.get(index);
            randomList.remove(index);
        }
        return randoms;
    }

    public static String getUUID() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("-", "");
    }

    // </editor-folder>

    public static float clamp(float value, float max, float min) {
        return java.lang.Math.max(java.lang.Math.min(value, max), min);
    }
}
