package dora.util;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public final class ToastUtils {

    private static Toast mToast;

    private ToastUtils() {
    }

    public static void showShort(final String msg) {
        if (!ThreadUtils.isMainThread()) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    showShortInternal(msg);
                }
            });
        } else {
            showShortInternal(msg);
        }
    }

    public static void showLong(final String msg) {
        if (!ThreadUtils.isMainThread()) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    showLongInternal(msg);
                }
            });
        } else {
            showLongInternal(msg);
        }
    }

    private static void showShortInternal(String msg) {
        if (mToast == null) {
            synchronized (ToastUtils.class) {
                if (mToast == null)
                    mToast = Toast.makeText(GlobalContext.get(), msg, Toast.LENGTH_SHORT);
            }
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }

    private static void showLongInternal(String msg) {
        if (mToast == null) {
            synchronized (ToastUtils.class) {
                if (mToast == null)
                    mToast = Toast.makeText(GlobalContext.get(), msg, Toast.LENGTH_LONG);
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
