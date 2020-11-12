package dora.widget.panel.sliding;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import dora.widget.R;

public class SlidingItem extends ViewGroup {

    private static final String TAG = SlidingItem.class.getSimpleName();

    private int mScaleTouchSlop;

    private int mMaxVelocity;

    private int mPointerId;

    private int mHeight;

    private int mRightMenuWidths;

    private int mLimit;

    private View mContentView;

    private PointF mLastP = new PointF();

    private boolean mUnMoved = true;

    private PointF mFirstP = new PointF();

    private ValueAnimator mExpandAnim;

    private ValueAnimator mCloseAnim;

    private boolean mExpand;

    private boolean mUserSwiped;

    private static SlidingItem sViewCache;

    private static boolean sTouching;

    private VelocityTracker mVelocityTracker;

    private boolean mSwipeEnable;

    private boolean mMutex;

    private boolean mMutexInterceptFlag;

    private boolean mLeftSwipe;

    public SlidingItem(Context context) {
        this(context, null);
    }

    public SlidingItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs, defStyleAttr);
    }

    public boolean isExpand() {
        return mExpand;
    }

    public boolean isSwipeEnable() {
        return mSwipeEnable;
    }

    public void setSwipeEnable(boolean swipeEnable) {
        mSwipeEnable = swipeEnable;
    }

    public boolean isMutex() {
        return mMutex;
    }

    public SlidingItem setMutex(boolean mutex) {
        mMutex = mutex;
        return this;
    }

    public boolean isLeftSwipe() {
        return mLeftSwipe;
    }

    public SlidingItem setLeftSwipe(boolean leftSwipe) {
        mLeftSwipe = leftSwipe;
        return this;
    }

    public static SlidingItem getViewCache() {
        return sViewCache;
    }

    private void initAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        mScaleTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mMaxVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SlidingItem,
                defStyleAttr, 0);
        int count = a.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.SlidingItem_swipeAllowed) {
                mSwipeEnable = a.getBoolean(attr, true);
            } else if (attr == R.styleable.SlidingItem_mutex) {
                mMutex = a.getBoolean(attr, true);
            } else if (attr == R.styleable.SlidingItem_isLeftSwipe) {
                mLeftSwipe = a.getBoolean(attr, true);
            }
        }
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setClickable(true);
        mRightMenuWidths = 0;
        mHeight = 0;
        int contentWidth = 0;
        int childCount = getChildCount();
        final boolean measureMatchParentChildren = MeasureSpec.getMode(heightMeasureSpec) !=
                MeasureSpec.EXACTLY;
        boolean isNeedMeasureChildHeight = false;
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            childView.setClickable(true);
            if (childView.getVisibility() != GONE) {
                measureChild(childView, widthMeasureSpec, heightMeasureSpec);
                final MarginLayoutParams lp = (MarginLayoutParams) childView.getLayoutParams();
                mHeight = Math.max(mHeight, childView.getMeasuredHeight()/* + lp.topMargin + lp.bottomMargin*/);
                if (measureMatchParentChildren && lp.height == LayoutParams.MATCH_PARENT) {
                    isNeedMeasureChildHeight = true;
                }
                if (i > 0) {
                    mRightMenuWidths += childView.getMeasuredWidth();
                } else {
                    mContentView = childView;
                    contentWidth = childView.getMeasuredWidth();
                }
            }
        }
        setMeasuredDimension(getPaddingLeft() + getPaddingRight() + contentWidth,
                mHeight + getPaddingTop() + getPaddingBottom());
        mLimit = mRightMenuWidths * 4 / 10;
        if (isNeedMeasureChildHeight) {
            forceUniformHeight(childCount, widthMeasureSpec);
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    private void forceUniformHeight(int count, int widthMeasureSpec) {
        int uniformMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredHeight(),
                MeasureSpec.EXACTLY);
        for (int i = 0; i < count; ++i) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                if (lp.height == LayoutParams.MATCH_PARENT) {
                    int oldWidth = lp.width;
                    lp.width = child.getMeasuredWidth();
                    // Remeasure with new dimensions.
                    measureChildWithMargins(child, widthMeasureSpec, 0, uniformMeasureSpec, 0);
                    lp.width = oldWidth;
                }
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int left = 0 + getPaddingLeft();
        int right = 0 + getPaddingLeft();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (childView.getVisibility() != GONE) {
                if (i == 0) {
                    childView.layout(left, getPaddingTop(), left + childView.getMeasuredWidth(),
                            getPaddingTop() + childView.getMeasuredHeight());
                    left = left + childView.getMeasuredWidth();
                } else {
                    if (mLeftSwipe) {
                        childView.layout(left, getPaddingTop(), left + childView.getMeasuredWidth(),
                                getPaddingTop() + childView.getMeasuredHeight());
                        left = left + childView.getMeasuredWidth();
                    } else {
                        childView.layout(right - childView.getMeasuredWidth(), getPaddingTop(),
                                right, getPaddingTop() + childView.getMeasuredHeight());
                        right = right - childView.getMeasuredWidth();
                    }

                }
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mSwipeEnable) {
            acquireVelocityTracker(ev);
            final VelocityTracker verTracker = mVelocityTracker;
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mUserSwiped = false;
                    mUnMoved = true;
                    mMutexInterceptFlag = false;
                    if (sTouching) {
                        return false;
                    } else {
                        sTouching = true;
                    }
                    mLastP.set(ev.getRawX(), ev.getRawY());
                    mFirstP.set(ev.getRawX(), ev.getRawY());
                    if (sViewCache != null) {
                        if (sViewCache != this) {
                            sViewCache.smoothClose();
                            mMutexInterceptFlag = mMutex;
                        }
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                    mPointerId = ev.getPointerId(0);
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (mMutexInterceptFlag) {
                        break;
                    }
                    float gap = mLastP.x - ev.getRawX();
                    if (Math.abs(gap) > 10 || Math.abs(getScrollX()) > 10) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                    if (Math.abs(gap) > mScaleTouchSlop) {
                        mUnMoved = false;
                    }
                    scrollBy((int) (gap), 0);
                    if (mLeftSwipe) {
                        if (getScrollX() < 0) {
                            scrollTo(0, 0);
                        }
                        if (getScrollX() > mRightMenuWidths) {
                            scrollTo(mRightMenuWidths, 0);
                        }
                    } else {
                        if (getScrollX() < -mRightMenuWidths) {
                            scrollTo(-mRightMenuWidths, 0);
                        }
                        if (getScrollX() > 0) {
                            scrollTo(0, 0);
                        }
                    }
                    mLastP.set(ev.getRawX(), ev.getRawY());
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    if (Math.abs(ev.getRawX() - mFirstP.x) > mScaleTouchSlop) {
                        mUserSwiped = true;
                    }
                    if (!mMutexInterceptFlag) {
                        verTracker.computeCurrentVelocity(1000, mMaxVelocity);
                        final float velocityX = verTracker.getXVelocity(mPointerId);
                        if (Math.abs(velocityX) > 1000) {
                            if (velocityX < -1000) {
                                if (mLeftSwipe) {
                                    smoothExpand();
                                } else {
                                    smoothClose();
                                }
                            } else {
                                if (mLeftSwipe) {
                                    smoothClose();
                                } else {
                                    smoothExpand();
                                }
                            }
                        } else {
                            if (Math.abs(getScrollX()) > mLimit) {
                                smoothExpand();
                            } else {
                                smoothClose();
                            }
                        }
                    }
                    releaseVelocityTracker();
                    sTouching = false;
                    break;
                default:
                    break;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mSwipeEnable) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    if (Math.abs(ev.getRawX() - mFirstP.x) > mScaleTouchSlop) {
                        return true;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (mLeftSwipe) {
                        if (getScrollX() > mScaleTouchSlop) {
                            if (ev.getX() < getWidth() - getScrollX()) {
                                if (mUnMoved) {
                                    smoothClose();
                                }
                                return true;
                            }
                        }
                    } else {
                        if (-getScrollX() > mScaleTouchSlop) {
                            if (ev.getX() > -getScrollX()) {
                                if (mUnMoved) {
                                    smoothClose();
                                }
                                return true;
                            }
                        }
                    }
                    if (mUserSwiped) {
                        return true;
                    }
                    break;
            }
            if (mMutexInterceptFlag) {
                return true;
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    public void smoothExpand() {
        sViewCache = SlidingItem.this;
        if (null != mContentView) {
            mContentView.setLongClickable(false);
        }
        cancelAnim();
        mExpandAnim = ValueAnimator.ofInt(getScrollX(), mLeftSwipe ? mRightMenuWidths : -mRightMenuWidths);
        mExpandAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                scrollTo((Integer) animation.getAnimatedValue(), 0);
            }
        });
        mExpandAnim.setInterpolator(new OvershootInterpolator());
        mExpandAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mExpand = true;
            }
        });
        mExpandAnim.setDuration(300).start();
    }

    private void cancelAnim() {
        if (mCloseAnim != null && mCloseAnim.isRunning()) {
            mCloseAnim.cancel();
        }
        if (mExpandAnim != null && mExpandAnim.isRunning()) {
            mExpandAnim.cancel();
        }
    }

    public void smoothClose() {
        sViewCache = null;
        if (null != mContentView) {
            mContentView.setLongClickable(true);
        }
        cancelAnim();
        mCloseAnim = ValueAnimator.ofInt(getScrollX(), 0);
        mCloseAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                scrollTo((Integer) animation.getAnimatedValue(), 0);
            }
        });
        mCloseAnim.setInterpolator(new AccelerateInterpolator());
        mCloseAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mExpand = false;
            }
        });
        mCloseAnim.setDuration(300).start();
    }

    private void acquireVelocityTracker(final MotionEvent event) {
        if (null == mVelocityTracker) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    private void releaseVelocityTracker() {
        if (null != mVelocityTracker) {
            mVelocityTracker.clear();
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        if (this == sViewCache) {
            sViewCache.smoothClose();
            sViewCache = null;
        }
        super.onDetachedFromWindow();
    }

    @Override
    public boolean performLongClick() {
        if (Math.abs(getScrollX()) > mScaleTouchSlop) {
            return false;
        }
        return super.performLongClick();
    }

    public void quickClose() {
        if (this == sViewCache) {
            //先取消展开动画
            cancelAnim();
            sViewCache.scrollTo(0, 0);
            sViewCache = null;
        }
    }
}