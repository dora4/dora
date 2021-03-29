package dora.widget.panel;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import dora.util.DensityUtils;

/**
 * 自动给最后加一行提示信息，如共有几条记录的菜单面板。
 */
public class TipsMenuPanel extends MenuPanel {

    private String mTips = "";
    private TextView mTipsView;

    public TipsMenuPanel(Context context) {
        super(context);
    }

    public TipsMenuPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TipsMenuPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TipsMenuPanel setEmptyTips() {
        setTips("");
        return this;
    }

    public TipsMenuPanel setTips(String tips) {
        this.mTips = tips;
        return this;
    }

    @Override
    public void updatePanel() {
        if (mTipsView != null && mContainer != null) {
            mContainer.removeView(mTipsView);
        }
        if (mTips != null && mTips.length() > 0) {
            mTipsView = new TextView(getContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.topMargin = (int) DensityUtils.dp2px(5);
            lp.bottomMargin = (int) DensityUtils.dp2px(5);
            mTipsView.setGravity(Gravity.CENTER_HORIZONTAL);
            mTipsView.setTextColor(0xFF999999);
            mTipsView.setLayoutParams(lp);
            mTipsView.setText(mTips);
            //增加了底部的tips
            mContainer.addView(mTipsView);
        }
        super.updatePanel();
    }
}
