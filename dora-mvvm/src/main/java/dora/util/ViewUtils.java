package dora.util;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.AnimRes;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 视图相关工具。
 */
public final class ViewUtils implements Number {

    private ViewUtils() {
    }

    /**
     * RecyclerView默认配置。
     */
    public static RecyclerView configRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL));
        return recyclerView;
    }

    /**
     * RecyclerView自定义配置。
     */
    public static RecyclerView configRecyclerView(RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager) {
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        return recyclerView;
    }

    /**
     * 将View从父控件中移除。
     *
     * @param view 要移除的view
     */
    public static void removeViewFormParent(View view) {
        if (view == null) return;
        ViewParent parent = view.getParent();
        if (parent != null) {
            ((ViewGroup) parent).removeView(view);
        }
    }

    /**
     * 获取文本控件或输入框控件的文本。
     */
    public static String getText(TextView textView) {
        return textView.getText().toString().trim();
    }

    /**
     * 设置wrap_content的情况下，给定默认宽高。
     *
     * @param expected 期望的值
     */
    public static int applyWrapContentSize(int measureSpec, int expected) {
        int mode = View.MeasureSpec.getMode(measureSpec);
        if (mode == View.MeasureSpec.UNSPECIFIED
                || mode == View.MeasureSpec.AT_MOST) {
            measureSpec = View.MeasureSpec.makeMeasureSpec(expected, View.MeasureSpec.EXACTLY);
        }
        return measureSpec;
    }

    /**
     * 边缘检测。
     */
    public static boolean isTouchEdge(int edgeSize, Context context, MotionEvent e) {
        return e.getRawX() < edgeSize
                || e.getRawX() > ScreenUtils.getScreenWidth(context) - edgeSize
                || e.getRawY() < edgeSize
                || e.getRawY() > ScreenUtils.getScreenHeight(context) - edgeSize;
    }

    /**
     * 获取普通的画笔，不带描边。
     *
     * @param color 画笔的颜色
     */
    public static Paint getPaint(int color) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setDither(true);
        paint.setColor(color);
        return paint;
    }

    /**
     * 获取像铅笔一样的画笔，用于绘制线条。
     *
     * @param color 画笔的颜色
     */
    public static Paint getPencil(int color) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setDither(true);
        paint.setColor(color);
        return paint;
    }

    /**
     * 获取像刷子一样的画笔，带描边。
     *
     * @param color 画笔的颜色
     */
    public static Paint getBrush(int color) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setDither(true);
        paint.setColor(color);
        return paint;
    }

    public static ValueAnimator createValueAnimator(int duration, @Nullable ValueAnimator.AnimatorUpdateListener updateListener,
                                                    @Nullable ValueAnimator.AnimatorListener stateListener,
                                                    @Nullable ValueAnimator.AnimatorPauseListener pauseListener,
                                                    float... values) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(values);
        valueAnimator.setDuration(duration);
        valueAnimator.addUpdateListener(updateListener);
        valueAnimator.addListener(stateListener);
        valueAnimator.addPauseListener(pauseListener);
        return valueAnimator;
    }

    public static void drawArc(Canvas canvas,
                               float left, float top,
                               float right, float bottom,
                               float startAngle, float sweepAngle,
                               Paint paint) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawArc(left, top, right, bottom,
                    startAngle, sweepAngle, true, paint);
        } else {
            canvas.drawArc(new RectF(left, top, right, bottom),
                    startAngle, sweepAngle, true, paint);
        }
    }

    public static void drawRoundRect(Canvas canvas,
                                     float left, float top,
                                     float right, float bottom,
                                     float backgroundRadius,
                                     Paint paint) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawRoundRect(left, top, right, bottom,
                    backgroundRadius, backgroundRadius, paint);
        } else {
            canvas.drawRoundRect(new RectF(left, top, right, bottom),
                    backgroundRadius, backgroundRadius, paint);
        }
    }

    public static float clamp(float value, float max, float min) {
        return Math.max(Math.min(value, max), min);
    }

    public static void runAnimation(Context context, View view, @AnimRes int animResId) {
        Animation animation = AnimationUtils.loadAnimation(context, animResId);
        if (animation != null) {
            runAnimation(view, animation);
        }
    }

    public static void runAnimation(View view, Animation animation) {
        view.startAnimation(animation);
    }
}
