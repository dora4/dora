package dora.util;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public final class ViewUtils implements Number {

    private ViewUtils() {
    }

    /**
     * 默认配置。
     *
     * @param recyclerView
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
     * 自定义配置。
     *
     * @param recyclerView
     * @param layoutManager
     */
    public static RecyclerView configRecyclerView(RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager) {
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        return recyclerView;
    }

    public static String getText(TextView textView) {
        return textView.getText().toString().trim();
    }


    /**
     * 设置wrap_content的情况下，给定默认宽高。
     *
     * @param measureSpec
     * @param expected 期望的值
     * @return
     */
    public static int applyWrapContentSize(int measureSpec, int expected) {
        int mode = View.MeasureSpec.getMode(measureSpec);
        if (mode == View.MeasureSpec.UNSPECIFIED
                || mode == View.MeasureSpec.AT_MOST) {
            measureSpec = View.MeasureSpec.makeMeasureSpec(expected, View.MeasureSpec.EXACTLY);
        }
        return measureSpec;
    }

    public static Paint getPaint(int color) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setDither(true);
        paint.setColor(color);
        return paint;
    }

    public static Paint getPencil(int color) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setDither(true);
        paint.setColor(color);
        return paint;
    }

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
}
