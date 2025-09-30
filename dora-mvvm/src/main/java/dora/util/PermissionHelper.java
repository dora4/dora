package dora.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;

import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.HashMap;
import java.util.Map;

/**
 * PermissionHelper
 * <p>
 * English: A utility class to simplify Android runtime permission requests.
 * Supports both single and multiple permission requests, handling permanent denials,
 * and compatibility with different Android versions (including MANAGE_EXTERNAL_STORAGE).
 * <p>
 * 简体中文：一个简化 Android 运行时权限请求的工具类。
 * 支持单个和多个权限请求，处理永久拒绝，并兼容不同 Android 版本（包括 MANAGE_EXTERNAL_STORAGE）。
 */
public class PermissionHelper {

    private final ComponentActivity activity;
    private final Fragment fragment;
    private final Map<String, ActivityResultLauncher<String>> singleLaunchers = new HashMap<>();
    private ActivityResultLauncher<String[]> multiLauncher;
    private final Map<String, PermissionCallback> callbackMap = new HashMap<>();
    private String[] pendingPermissions;
    public static final int REQUEST_CODE_REQUEST_PERMISSION = 10001;

    /**
     * Private constructor with Activity.
     * 简体中文：带 Activity 的私有构造方法。
     */
    private PermissionHelper(ComponentActivity activity) {
        this.activity = activity;
        this.fragment = null;
    }

    /**
     * Private constructor with Fragment.
     * 简体中文：带 Fragment 的私有构造方法。
     */
    private PermissionHelper(Fragment fragment) {
        this.fragment = fragment;
        this.activity = fragment.requireActivity();
    }

    /**
     * Create PermissionHelper with a Context.
     * The context must be a ComponentActivity (directly or wrapped).
     * 简体中文：使用 Context 创建 PermissionHelper。
     * Context 必须是 ComponentActivity（直接或间接包装）。
     */
    public static PermissionHelper with(@NonNull Context context) {
        ComponentActivity act = findActivity(context);
        if (act == null) throw new IllegalArgumentException("Context must be a ComponentActivity");
        return new PermissionHelper(act);
    }

    /**
     * Create PermissionHelper with a Fragment.
     * 简体中文：使用 Fragment 创建 PermissionHelper。
     */
    public static PermissionHelper with(@NonNull Fragment fragment) {
        return new PermissionHelper(fragment);
    }

    private static ComponentActivity findActivity(Context context) {
        if (context instanceof ComponentActivity) return (ComponentActivity) context;
        if (context instanceof ContextWrapper) return findActivity(((ContextWrapper) context).getBaseContext());
        return null;
    }

    /**
     * Prepare launchers for given permissions.
     * 简体中文：为给定权限准备 launcher。
     */
    public PermissionHelper prepare(String... allPermissions) {
        if (activity == null && fragment == null) {
            throw new IllegalStateException("PermissionHelper not initialized. Call with(Context) or with(Fragment) first.");
        }
        if (multiLauncher == null) {
            multiLauncher = fragment != null
                    ? fragment.registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), this::onMultiResult)
                    : activity.registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), this::onMultiResult);
        }
        for (String perm : allPermissions) {
            if (!singleLaunchers.containsKey(perm)) {
                ActivityResultLauncher<String> launcher = fragment != null
                        ? fragment.registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> onSingleResult(perm, granted))
                        : activity.registerForActivityResult(new ActivityResultContracts.RequestPermission(), granted -> onSingleResult(perm, granted));
                singleLaunchers.put(perm, launcher);
            }
        }
        return this;
    }

    /**
     * Set the permissions to request.
     * 简体中文：设置需要请求的权限。
     */
    public PermissionHelper permissions(String... permissions) {
        this.pendingPermissions = permissions;
        return this;
    }

    /**
     * Check whether the specified permission has been granted.
     * 简体中文：检查指定的权限是否已被授予。
     */
    public boolean hasPermission(Activity activity, String permission) {
        return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Handle the case where a permission has been permanently denied by the user.
     * This will redirect the user to the app details settings page,
     * where they can manually enable the permission.
     * 简体中文：处理用户永久拒绝权限的情况。
     * 该方法会跳转到应用详情设置页面，让用户手动开启权限。
     */
    private void handlePermanentlyDenied() {
        Context ctx = null;
        if (fragment != null && fragment.isAdded()) {
            ctx = fragment.getContext();
        } else if (activity != null) {
            ctx = activity;
        }
        if (ctx != null) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + ctx.getPackageName()));
            ctx.startActivity(intent);
        }
    }

    /**
     * Request permissions and handle result via callback.
     * 简体中文：请求权限，并通过回调处理结果。
     */
    public void request() {
        request(null);
    }

    /**
     * Request permissions and handle result via callback.
     * 简体中文：请求权限，并通过回调处理结果。
     */
    public void request(PermissionCallback callback) {
        if (pendingPermissions == null || pendingPermissions.length == 0) return;
        boolean allGranted = true;
        for (String perm : pendingPermissions) {
            if (!hasPermission(activity, perm)) {
                allGranted = false;
                break;
            }
        }
        if (allGranted) {
            if (callback != null) {
                callback.onResult(true);
            }
            return;
        }
        if (pendingPermissions.length == 1) {
            ActivityResultLauncher<String> launcher = singleLaunchers.get(pendingPermissions[0]);
            if (launcher == null) throw new IllegalStateException("Launcher not registered for " + pendingPermissions[0]);
            callbackMap.put(pendingPermissions[0], callback);
            launcher.launch(pendingPermissions[0]);
        } else {
            if (multiLauncher == null) throw new IllegalStateException("Multi-permission launcher not registered");
            callbackMap.put("MULTI", callback);
            multiLauncher.launch(pendingPermissions);
        }
    }

    /**
     * Check whether storage permission is granted.
     * On Android 11+ it checks MANAGE_EXTERNAL_STORAGE,
     * on lower versions it checks READ/WRITE_EXTERNAL_STORAGE.
     * 简体中文：检查存储权限是否被授予。
     * Android 11+ 检查 MANAGE_EXTERNAL_STORAGE，
     * 更低版本检查 READ/WRITE_EXTERNAL_STORAGE。
     *
     * @see IntentUtils#getRequestStoragePermissionIntent(String)
     */
    public static boolean hasStoragePermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            int read = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
            int write = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            return read == PackageManager.PERMISSION_GRANTED &&
                    write == PackageManager.PERMISSION_GRANTED;
        }
    }

    /**
     * Check whether a permission has been permanently denied.
     * 简体中文：检查权限是否被永久拒绝。
     */
    public boolean isPermissionPermanentlyDenied(String permission) {
        if (fragment != null) {
            return !hasPermission(fragment.getActivity(), permission)
                    && !fragment.shouldShowRequestPermissionRationale(permission);
        } else if (activity != null) {
            return !hasPermission(activity, permission)
                    && !ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
        }
        return false;
    }

    private void onSingleResult(String permission, boolean granted) {
        PermissionCallback cb = callbackMap.remove(permission);
        if (cb != null) {
            if (!granted) {
                if (isPermissionPermanentlyDenied(permission)) {
                    handlePermanentlyDenied();
                } else {
                    if (fragment != null) {
                        fragment.requestPermissions(pendingPermissions, REQUEST_CODE_REQUEST_PERMISSION);
                    } else if (activity != null) {
                        ActivityCompat.requestPermissions(activity, pendingPermissions, REQUEST_CODE_REQUEST_PERMISSION);
                    }
                }
            }
            cb.onResult(granted);
        }
    }

    private void onMultiResult(Map<String, Boolean> results) {
        PermissionCallback cb = callbackMap.remove("MULTI");
        if (cb != null) {
            boolean allGranted = true;
            boolean permanentlyDenied = false;
            for (Map.Entry<String, Boolean> entry : results.entrySet()) {
                String permission = entry.getKey();
                boolean granted = entry.getValue();
                if (!granted) {
                    allGranted = false;
                    if (isPermissionPermanentlyDenied(permission)) {
                        permanentlyDenied = true;
                    } else {
                        if (fragment != null) {
                            fragment.requestPermissions(pendingPermissions, REQUEST_CODE_REQUEST_PERMISSION);
                        } else if (activity != null) {
                            ActivityCompat.requestPermissions(activity, pendingPermissions, REQUEST_CODE_REQUEST_PERMISSION);
                        }
                    }
                }
            }
            if (permanentlyDenied) {
                handlePermanentlyDenied();
            }
            cb.onResult(allGranted);
        }
    }

    /**
     * Callback for permission request result.
     * 简体中文：权限请求结果的回调接口。
     */
    public interface PermissionCallback {

        /**
         * Called when permissions result is available.
         * 简体中文：当权限结果可用时调用。
         */
        void onResult(boolean granted);
    }

    /**
     * Permission constants for common Android permissions.
     * 简体中文：常用 Android 权限的常量。
     */
    public static class Permission {
        public static final String CAMERA = Manifest.permission.CAMERA;
        public static final String RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;
        public static final String READ_CONTACTS = Manifest.permission.READ_CONTACTS;
        public static final String WRITE_CONTACTS = Manifest.permission.WRITE_CONTACTS;
        public static final String GET_ACCOUNTS = Manifest.permission.GET_ACCOUNTS;
        public static final String READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE; // Deprecated Android 13+
        public static final String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE; // Deprecated Android 11+
        public static final String MANAGE_EXTERNAL_STORAGE = Manifest.permission.MANAGE_EXTERNAL_STORAGE; // Android 11
        public static final String READ_MEDIA_IMAGES = "android.permission.READ_MEDIA_IMAGES";   // Android 13+
        public static final String READ_MEDIA_VIDEO = "android.permission.READ_MEDIA_VIDEO";     // Android 13+
        public static final String READ_MEDIA_AUDIO = "android.permission.READ_MEDIA_AUDIO";     // Android 13+
        public static final String READ_MEDIA_VISUAL_USER_SELECTED = "android.permission.READ_MEDIA_VISUAL_USER_SELECTED"; // Android 14+
        public static final String ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
        public static final String ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
        public static final String ACCESS_BACKGROUND_LOCATION = Manifest.permission.ACCESS_BACKGROUND_LOCATION; // Android 10+
        public static final String READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
        public static final String CALL_PHONE = Manifest.permission.CALL_PHONE;
        public static final String READ_CALL_LOG = Manifest.permission.READ_CALL_LOG;
        public static final String WRITE_CALL_LOG = Manifest.permission.WRITE_CALL_LOG;
        public static final String ADD_VOICEMAIL = Manifest.permission.ADD_VOICEMAIL;
        public static final String USE_SIP = Manifest.permission.USE_SIP;
        public static final String PROCESS_OUTGOING_CALLS = Manifest.permission.PROCESS_OUTGOING_CALLS; // Deprecated Android 9+
        public static final String SEND_SMS = Manifest.permission.SEND_SMS;
        public static final String RECEIVE_SMS = Manifest.permission.RECEIVE_SMS;
        public static final String READ_SMS = Manifest.permission.READ_SMS;
        public static final String RECEIVE_WAP_PUSH = Manifest.permission.RECEIVE_WAP_PUSH;
        public static final String RECEIVE_MMS = Manifest.permission.RECEIVE_MMS;
        public static final String BODY_SENSORS = Manifest.permission.BODY_SENSORS;
        public static final String BODY_SENSORS_BACKGROUND = "android.permission.BODY_SENSORS_BACKGROUND"; // Android 14+
        public static final String BLUETOOTH = Manifest.permission.BLUETOOTH;               // Before Android 12
        public static final String BLUETOOTH_ADMIN = Manifest.permission.BLUETOOTH_ADMIN;   // Android 12+
        public static final String BLUETOOTH_SCAN = "android.permission.BLUETOOTH_SCAN";     // Android 12+
        public static final String BLUETOOTH_CONNECT = "android.permission.BLUETOOTH_CONNECT"; // Android 12+
        public static final String BLUETOOTH_ADVERTISE = "android.permission.BLUETOOTH_ADVERTISE"; // Android 12+
        public static final String POST_NOTIFICATIONS = "android.permission.POST_NOTIFICATIONS"; // Android 13+
        public static final String FOREGROUND_SERVICE = Manifest.permission.FOREGROUND_SERVICE;
        public static final String FOREGROUND_SERVICE_MEDIA_PLAYBACK = "android.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK"; // Android 14+
        public static final String FOREGROUND_SERVICE_CAMERA = "android.permission.FOREGROUND_SERVICE_CAMERA";                 // Android 14+
        public static final String FOREGROUND_SERVICE_CONNECTED_DEVICE = "android.permission.FOREGROUND_SERVICE_CONNECTED_DEVICE"; // Android 14+
        public static final String INTERNET = Manifest.permission.INTERNET;
        public static final String NFC = Manifest.permission.NFC;
        public static final String NEARBY_WIFI_DEVICES = "android.permission.NEARBY_WIFI_DEVICES"; // Android 13+
        public static final String SCHEDULE_EXACT_ALARM = "android.permission.SCHEDULE_EXACT_ALARM"; // Android 12+
    }
}
