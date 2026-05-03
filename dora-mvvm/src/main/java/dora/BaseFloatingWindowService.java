package dora;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;

import dora.widget.DraggableLayout;

/**
 * Used as the root container for floating window dragging.
 * The root view must be {@link dora.widget.DraggableLayout} to support drag gestures.
 *
 * public void requestFloatingPermission(Context context) {
 *     if (!Settings.canDrawOverlays(context)) {
 *         Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
 *         Uri.parse("package:"+context.getPackageName()));
 *         startActivityForResult(intent, REQUEST_OVERLAY_PERMISSION);
 *     }
 * }
 *
 * public void start(Context context) {
 *     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
 *         if (Settings.canDrawOverlays(context)) {
 *             startService(new Intent(context, FloatingWindowService.java));
 *         }
 *     }
 * }
 *
 * public void stop(Context context) {
 *     context.stopService(new Intent(context, FloatingWindowService.java));
 * }
 */
public abstract class BaseFloatingWindowService extends Service {

    protected WindowManager mWindowManager;
    protected View mFloatView;
    private static final int INITIAL_PARAM_X = 0;
    private static final int INITIAL_PARAM_Y = 0;

    protected int[] getInitialPosition() {
        return new int[] { INITIAL_PARAM_X, INITIAL_PARAM_Y };
    }

    protected abstract @LayoutRes int getLayoutId();

    protected void initViews() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mFloatView = LayoutInflater.from(this).inflate(getLayoutId(), null);
        initViews();
        WindowManager.LayoutParams params = getLayoutParams();
        params.gravity = Gravity.TOP | Gravity.START;
        params.x = getInitialPosition()[0];
        params.y = getInitialPosition()[1];
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mFloatView, params);
        // Use an array because it bypasses Java’s restriction that local variables used in inner
        // classes/lambda expressions must be final or effectively final.
        // 简体中文：使用数组的原因是绕过java局部变量的final限制
        final int[] initialX = new int[1];
        final int[] initialY = new int[1];
        DraggableLayout root = (DraggableLayout) mFloatView;
        root.setOnDragListener(new DraggableLayout.OnDragListener() {

            @Override
            public void onDragStart() {
                initialX[0] = params.x;
                initialY[0] = params.y;
            }

            @Override
            public void onDrag(float dx, float dy) {
                params.x = initialX[0] + (int) dx;
                params.y = initialY[0] + (int) dy;
                mWindowManager.updateViewLayout(root, params);
            }

            @Override
            public void onDragEnd() {
            }
        });
    }

    protected  <T extends View> T findViewById(@IdRes int id) {
        return mFloatView.findViewById(id);
    }

    private static WindowManager.LayoutParams getLayoutParams() {
        int layoutFlag;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutFlag = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutFlag = WindowManager.LayoutParams.TYPE_PHONE;
        }
        return new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                layoutFlag,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatView != null) {
            mWindowManager.removeView(mFloatView);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
