package dora.widget.panel.drawable;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import androidx.annotation.ColorInt;

public class TagDrawable extends Drawable {

    private static final int DEFAULT_ROUND_CORNER = 10;
    private Paint mPaint;
    private RectF mRectF;
    private int mCorner;

    public TagDrawable(@ColorInt int color, int corner) {
        //创建一个没有大小的drawable，需要setBounds后才可以使用
        this(color, corner, new Rect());
    }

    public TagDrawable(@ColorInt int color, int corner, Rect rect) {
        this(color, corner, rect.left, rect.top, rect.right, rect.bottom);
    }

    public TagDrawable(String tagText, @ColorInt int color) {
        this(tagText, color, DEFAULT_ROUND_CORNER);
    }

    public TagDrawable(String tagText, @ColorInt int color, int corner) {
        mCorner = corner;
        mPaint = new Paint();
        mPaint.setColor(color);
        mPaint.setAntiAlias(true);
        Rect rect = new Rect();
        mPaint.getTextBounds(tagText, 0, tagText.length(), rect);
        setBounds(rect.left, rect.top, rect.right, rect.bottom);
    }

    public TagDrawable(@ColorInt int color, int left, int top, int right, int bottom) {
        this(color, DEFAULT_ROUND_CORNER, left, top, right, bottom);
    }

    public TagDrawable(@ColorInt int color, int corner, int left, int top, int right, int bottom) {
        mCorner = corner;
        mPaint = new Paint();
        mPaint.setColor(color);
        mPaint.setAntiAlias(true);
        setBounds(left, top, right, bottom);
    }

    public int getCorner() {
        return mCorner;
    }

    public void setCorner(int corner) {
        this.mCorner = corner;
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
        mRectF = new RectF(left, top, right, bottom);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRoundRect(mRectF, mCorner, mCorner, mPaint);
    }

    @Override
    public int getIntrinsicWidth() {
        return (int) mRectF.width();
    }

    @Override
    public int getIntrinsicHeight() {
        return (int) mRectF.height();
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
