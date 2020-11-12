package dora.permission.overlay;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.WindowManager;

import dora.R;
import dora.permission.Action;
import dora.permission.Rationale;
import dora.permission.RequestExecutor;
import dora.permission.source.Source;

abstract class BaseRequest implements OverlayRequest {

    private Source mSource;

    private Rationale<Void> mRationale = new Rationale<Void>() {
        @Override
        public void showRationale(Context context, Void data, RequestExecutor executor) {
            executor.execute();
        }
    };
    private Action<Void> mGranted;
    private Action<Void> mDenied;

    BaseRequest(Source source) {
        this.mSource = source;
    }

    @Override
    public final OverlayRequest rationale(Rationale<Void> rationale) {
        this.mRationale = rationale;
        return this;
    }

    @Override
    public final OverlayRequest onGranted(Action<Void> granted) {
        this.mGranted = granted;
        return this;
    }

    @Override
    public final OverlayRequest onDenied(Action<Void> denied) {
        this.mDenied = denied;
        return this;
    }

    /**
     * Why permissions are required.
     */
    final void showRationale(RequestExecutor executor) {
        mRationale.showRationale(mSource.getContext(), null, executor);
    }

    /**
     * Callback acceptance status.
     */
    final void callbackSucceed() {
        if (mGranted != null) {
            mGranted.onAction(null);
        }
    }

    /**
     * Callback rejected state.
     */
    final void callbackFailed() {
        if (mDenied != null) {
            mDenied.onAction(null);
        }
    }

    static boolean tryDisplayDialog(Context context) {
        Dialog dialog = new Dialog(context, R.style.Theme_Dora_Permission);
        int overlay = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        int alertWindow = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        int windowType = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? overlay : alertWindow;
        dialog.getWindow().setType(windowType);
        try {
            dialog.show();
        } catch (Exception e) {
            return false;
        } finally {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
        return true;
    }
}