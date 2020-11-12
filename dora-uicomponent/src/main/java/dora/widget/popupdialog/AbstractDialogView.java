package dora.widget.popupdialog;

import android.app.Activity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import dora.util.NavigationBarUtils;
import dora.widget.R;

public abstract class AbstractDialogView {

    public static final int DEFAULT_SHADOW_COLOR = 0x60000000;
    protected static final int INVALID = -1;
    protected static final int INVALID_COLOR = 0;
    protected final FrameLayout.LayoutParams mShadowLayoutParams = new FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
    );
    protected OnCancelListener mOnCancelListener;
    protected View.OnKeyListener mOnBackListener;
    protected FrameLayout.LayoutParams mGravityLayoutParams = new FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT,
            Gravity.BOTTOM
    );
    protected int mGravity = Gravity.NO_GRAVITY;
    protected boolean mNeedShadowView = false;
    protected int mShadowColor = INVALID_COLOR;

    public FrameLayout.LayoutParams getGravityLayoutParams() {
        return mGravityLayoutParams;
    }

    public void setGravityLayoutParams(FrameLayout.LayoutParams flp) {
        this.mGravityLayoutParams = flp;
    }

    public FrameLayout.LayoutParams getShadowLayoutParams() {
        return mShadowLayoutParams;
    }

    public int getGravity() {
        return mGravity;
    }

    public void setGravity(int gravity) {
        this.mGravity = gravity;
    }

    /**
     * DialogView的销毁将会引发PopupDialog的dismiss。
     */
    public void destroy() {
        if (mOnCancelListener != null) {
            mOnCancelListener.onCancel();
        }
    }

    /**
     * Add content view to decor view.
     *
     * @param inflater
     * @param parent   decor view
     * @return content view
     */
    protected View performInflateView(Activity activity, LayoutInflater inflater, FrameLayout parent) {
        View dialogView = inflater.inflate(R.layout.base_dialog_view, parent, false);
        LinearLayout dialogViewRoot = (LinearLayout) dialogView.findViewById(R.id
                .dialog_view_content);
        //有底部导航栏的情况下要上移，不能遮住NavigationBar
        if (NavigationBarUtils.isShowNavigationBar(activity)) {
            dialogViewRoot.setPadding(0,0,0, NavigationBarUtils.getNavigationBarHeight(activity));
        }
        if (mGravity != Gravity.NO_GRAVITY) {
            dialogViewRoot.setGravity(mGravity);
        }
        dialogViewRoot.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (mOnBackListener != null) {
                    mOnBackListener.onKey(v, keyCode, event);
                }
                return false;
            }
        });
        addContent(inflater, parent, dialogViewRoot);
        return dialogView;
    }

    public abstract View findViewById(int resId);

    protected abstract View getContentView();

    protected abstract void addContent(LayoutInflater inflater, ViewGroup parent, LinearLayout viewRoot);

    public abstract void setShadowColor(int shadowColor);

    protected abstract void setShadowViewOutsideCanDismiss(View shadeView, boolean canDismiss);

    public void setOnCancelListener(OnCancelListener listener) {
        this.mOnCancelListener = listener;
    }

    protected void setOnBackListener(View.OnKeyListener listener) {
        this.mOnBackListener = listener;
    }

    public interface OnCancelListener {
        void onCancel();
    }
}