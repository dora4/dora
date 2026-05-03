package dora.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

public class DraggableLayout extends FrameLayout {

    private float downX;
    private float downY;
    private boolean isDragging = false;
    private final int touchSlop;

    private OnDragListener onDragListener;

    public DraggableLayout(Context context) {
        this(context, null);
    }

    public DraggableLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DraggableLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        setClickable(true);
    }

    public interface OnDragListener {
        void onDragStart();
        void onDrag(float dx, float dy);
        void onDragEnd();
    }

    public void setOnDragListener(OnDragListener listener) {
        this.onDragListener = listener;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getRawX();
                downY = ev.getRawY();
                isDragging = false;
                return false;
            case MotionEvent.ACTION_MOVE:
                float dx = ev.getRawX() - downX;
                float dy = ev.getRawY() - downY;
                if (!isDragging && Math.hypot(dx, dy) > touchSlop) {
                    isDragging = true;
                    if (onDragListener != null) {
                        onDragListener.onDragStart();
                    }
                    MotionEvent cancel = MotionEvent.obtain(ev);
                    cancel.setAction(MotionEvent.ACTION_CANCEL);
                    super.dispatchTouchEvent(cancel);
                    cancel.recycle();
                    return true;
                }
                break;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_MOVE:
                if (isDragging) {
                    float dx = event.getRawX() - downX;
                    float dy = event.getRawY() - downY;
                    if (onDragListener != null) {
                        onDragListener.onDrag(dx, dy);
                    }
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isDragging) {
                    isDragging = false;
                    if (onDragListener != null) {
                        onDragListener.onDragEnd();
                    }
                    return true;
                }
                return false;
            case MotionEvent.ACTION_CANCEL:
                isDragging = false;
                if (onDragListener != null) {
                    onDragListener.onDragEnd();
                }
                return true;
        }
        return super.onTouchEvent(event);
    }
}