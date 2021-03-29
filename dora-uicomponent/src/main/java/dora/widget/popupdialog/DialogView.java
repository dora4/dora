package dora.widget.popupdialog;

import android.view.*;
import android.widget.LinearLayout;

public class DialogView extends AbstractDialogView {

    protected View mContentView;
    private int mViewResId = View.NO_ID;
    private boolean mCanTouchOutside;
    private OnInflateListener mOnInflateListener;

    protected DialogView() {
    }

    public DialogView(int layoutResId) {
        this.mViewResId = layoutResId;
    }

    public DialogView(View view) {
        this.mContentView = view;
    }

    public DialogView(int layoutResId, int shadowColor) {
        this(layoutResId);
        setShadowColor(shadowColor);
    }

    public DialogView(View view, int shadowColor) {
        this(view);
        setShadowColor(shadowColor);
    }

    public void setOnInflateListener(OnInflateListener listener) {
        this.mOnInflateListener = listener;
    }

    /**
     * Only valid if the shadow background is set.
     */
    public void setCanTouchOutside(boolean canTouchOutside) {
        this.mCanTouchOutside = canTouchOutside;
    }

    public boolean isNeedShadowView() {
        return mNeedShadowView;
    }

    public int getShadowColor() {
        return mShadowColor;
    }

    /**
     * Must invoke after {@link OnInflateListener#onInflateFinish(View)}.
     */
    @Override
    public View findViewById(int resId) {
        return mContentView.findViewById(resId);
    }

    @Override
    protected View getContentView() {
        return mContentView;
    }

    public void setContentView(View contentView) {
        this.mContentView = contentView;
    }

    @Override
    protected void addContent(LayoutInflater inflater, ViewGroup parent, LinearLayout viewRoot) {
        if (mNeedShadowView) {
            viewRoot.setBackgroundColor(mShadowColor);
            setShadowViewOutsideCanDismiss(viewRoot, true);
        }
        if (mViewResId != View.NO_ID) {
            mContentView = inflater.inflate(mViewResId, viewRoot, false); //inflate layout
        }
        if (mOnInflateListener != null) {
            mOnInflateListener.onInflateFinish(mContentView);
        }
        mContentView.setFocusable(true);
        mContentView.setFocusableInTouchMode(true);
        mContentView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (mOnBackListener != null) {
                    mOnBackListener.onKey(v, keyCode, event);
                }
                return true;
            }
        });
        mContentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        viewRoot.addView(mContentView);
    }

    @Override
    protected void setShadowViewOutsideCanDismiss(View shadeView, final boolean canDismiss) {
        shadeView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (canDismiss) {
                    mOnCancelListener.onCancel();
                }
                return !mCanTouchOutside;
            }
        });
    }

    @Override
    public void setShadowColor(int shadowColor) {
        this.mNeedShadowView = true;
        this.mShadowColor = shadowColor;
    }

    public interface OnInflateListener {
        void onInflateFinish(View contentView);
    }
}