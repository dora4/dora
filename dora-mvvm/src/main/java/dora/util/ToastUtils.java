package dora.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public final class ToastUtils {

    private static Toast mToast;

    private ToastUtils() {
    }

    public static void showShort(String msg) {
        showShort(GlobalContext.get(), msg);
    }

    public static void showShort(final Context context, final String msg) {
        if (!ThreadUtils.isMainThread()) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    showShortInternal(context, msg);
                }
            });
        } else {
            showShortInternal(context, msg);
        }
    }

    public static void showLong(String msg) {
        showLong(GlobalContext.get(), msg);
    }

    public static void showLong(final Context context, final String msg) {
        if (!ThreadUtils.isMainThread()) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    showLongInternal(context, msg);
                }
            });
        } else {
            showLongInternal(context, msg);
        }
    }

    private static void showShortInternal(Context context, String msg) {
        if (mToast == null) {
            synchronized (ToastUtils.class) {
                if (mToast == null)
                    mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
            }
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }

    private static void showLongInternal(Context context, String msg) {
        if (mToast == null) {
            synchronized (ToastUtils.class) {
                if (mToast == null)
                    mToast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
            }
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }

    public static void cancel() {
        if (mToast != null) {
            mToast.cancel();
        }
    }
}
