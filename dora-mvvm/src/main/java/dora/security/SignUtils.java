package dora.security;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Process;

import java.security.MessageDigest;

public class SignUtils {

    /**
     * Prevent repackaging.Verify the application's signing certificate by comparing the current APK's SHA-256
     * signature with the expected correct SHA-256 fingerprint.
     * If the signatures do not match, the application will be treated as tampered or re-signed.
     * How to get the SHA-256? Use the command:
     * keytool -list -v -keystore your_keystore.jks
     * 简体中文：防二次打包。验证应用的签名证书，通过比较当前 APK 的 SHA-256 签名与预期的正确签名指纹。
     * 如果签名不一致，则视为应用被篡改或重新签名。
     * 怎么样获取 SHA-256？使用命令：
     * keytool -list -v -keystore your_keystore.jks
     *
     * @param context Application context used to retrieve package information.
     *                简体中文：用于获取包信息的应用上下文。
     *
     * @param correctSHA256 The expected SHA-256 fingerprint of the signing certificate.
     *                      Format requirements:
     *                      1. All letters must be uppercase
     *                      2. Remove colons ':'
     *                      Example:
     *                      SHA-256: 12:34:56:78:9A:BC:DE:F0:12:34:56:78:9A:BC:DE:F0:12:34:56:78
     *                              :9A:BC:DE:F0:12:34:56:78:9A:BC:DE:F0
     *                      Code: "123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0"
     *                      简体中文：预期的签名证书 SHA-256 指纹。
     *                      格式要求：
     *                      1. 全部字母大写
     *                      2. 去掉冒号(:)
     *                      SHA-256: 12:34:56:78:9A:BC:DE:F0:12:34:56:78:9A:BC:DE:F0:12:34:56:78
     *                              :9A:BC:DE:F0:12:34:56:78:9A:BC:DE:F0
     *                      代码: "123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0"
     */
    public static void verifySignature(Context context, String correctSHA256) {
        try {
            PackageInfo packageInfo = getPackageInfo(context);
            Signature[] signatures;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                signatures = packageInfo.signingInfo.getApkContentsSigners();
            } else {
                signatures = packageInfo.signatures;
            }
            String sha256 = getSha256(signatures);
            if (!correctSHA256.equalsIgnoreCase(sha256)) {
                killProcess();
            }
        } catch (Exception e) {
            killProcess();
        }
    }

    private static PackageInfo getPackageInfo(Context context) throws PackageManager.NameNotFoundException {
        PackageManager pm = context.getPackageManager();
        PackageInfo packageInfo;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageInfo = pm.getPackageInfo(
                    context.getPackageName(),
                    PackageManager.GET_SIGNING_CERTIFICATES
            );
        } else {
            packageInfo = pm.getPackageInfo(
                    context.getPackageName(),
                    PackageManager.GET_SIGNATURES
            );
        }
        return packageInfo;
    }

    private static String getSha256(Signature[] signatures) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        StringBuilder sb = new StringBuilder();
        for (Signature signature : signatures) {
            byte[] cert = signature.toByteArray();
            byte[] digest = md.digest(cert);

            for (byte b : digest) {
                sb.append(String.format("%02X", b));
            }
        }
        return sb.toString();
    }

    private static void killProcess() {
        android.os.Process.killProcess(Process.myPid());
        System.exit(1);
    }
}
