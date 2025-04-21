package dora;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.LayoutRes;

/**
 * public static void requestFloatingPermission(Context context) {
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

    @SuppressLint("StaticFieldLeak")
    protected static BaseFloatingWindowService sInstance;
    protected WindowManager mWindowManager;
    private View mFloatView;
    private static final int INITIAL_PARAM_X = 0;
    private static final int INITIAL_PARAM_Y = 0;

    protected int[] getInitialPosition() {
        return new int[] { INITIAL_PARAM_X, INITIAL_PARAM_Y };
    }

    protected abstract @LayoutRes int getLayoutId();

    protected abstract void initViews(View floatView);

    public static BaseFloatingWindowService getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        mFloatView = LayoutInflater.from(this).inflate(getLayoutId(), null);
        initViews(mFloatView);
        WindowManager.LayoutParams params = getLayoutParams();

        params.gravity = Gravity.TOP | Gravity.START;
        params.x = getInitialPosition()[0];
        params.y = getInitialPosition()[1];
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManager.addView(mFloatView, params);
        enableDrag(mFloatView, params);
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

    private void enableDrag(final View view, final WindowManager.LayoutParams params) {
        view.setOnTouchListener(new View.OnTouchListener() {
            int initialX;
            int initialY;
            float initialTouchX;
            float initialTouchY;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        mWindowManager.updateViewLayout(view, params);
                        return true;
                }
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
