package dora;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

/**
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
    private int mTouchSlop = 10;
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            mTouchSlop = ViewConfiguration.get(mFloatView.getContext()).getScaledTouchSlop();
        }
        enableDrag(mFloatView, params);
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

    private void enableDrag(@NonNull final View view, final WindowManager.LayoutParams params) {
        view.setOnTouchListener(new View.OnTouchListener() {

            int initialX;
            int initialY;
            float touchX;
            float touchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        touchX = event.getRawX();
                        touchY = event.getRawY();
                    case MotionEvent.ACTION_MOVE:
                        float dx = event.getRawX() - touchX;
                        float dy = event.getRawY() - touchY;
                        // Consider it a drag if the movement distance is large enough
                        // 简体中文：如果移动距离足够大，则认为是拖动
                        if (Math.hypot(dx, dy) > mTouchSlop) {
                            params.x = initialX + (int) dx;
                            params.y = initialY + (int) dy;
                            mWindowManager.updateViewLayout(view, params);
                        }
                }
                // Allow event to pass through to child views
                // 简体中文：允许事件传递给子视图
                return false;
            }
        });
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
