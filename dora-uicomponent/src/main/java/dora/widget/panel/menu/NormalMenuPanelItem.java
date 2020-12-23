package dora.widget.panel.menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;

import dora.widget.panel.MenuPanelItem;
import dora.widget.R;

public class NormalMenuPanelItem implements MenuPanelItem {

    private String mTitle;
    private Span mTitleSpan;
    private String mMenu;
    private int mMarginTop;
    private boolean mShowArrowIcon;
    private String mArrowText;

    /**
     * 不显示角落图标，也不显示角落文本。
     *
     * @param menu
     */
    public NormalMenuPanelItem(String menu) {
        this(1, "", new Span(), menu, true, "");
    }

    public NormalMenuPanelItem(String menu, boolean showArrowIcon) {
        this(1, "", new Span(), menu, showArrowIcon, "");
    }

    public NormalMenuPanelItem(int marginTop, String menu) {
        this(marginTop, "", new Span(), menu, true, "");
    }

    public NormalMenuPanelItem(int marginTop, String menu, boolean showArrowIcon) {
        this(marginTop, "", new Span(), menu, showArrowIcon, "");
    }

    /**
     * 显示角落图标，也显示角落文本。
     *
     * @param menu
     * @param arrowText
     */
    public NormalMenuPanelItem(String menu, String arrowText) {
        this(1, "", new Span(), menu, true, arrowText);
    }

    public NormalMenuPanelItem(String menu, boolean showArrowIcon, String arrowText) {
        this(1, "", new Span(), menu, showArrowIcon, arrowText);
    }

    public NormalMenuPanelItem(int marginTop, String title, Span titleSpan, String menu, boolean showArrowIcon, String arrowText) {
        this.mMarginTop = marginTop;
        this.mTitle = title;
        this.mTitleSpan = titleSpan;
        this.mMenu = menu;
        this.mShowArrowIcon = showArrowIcon;
        this.mArrowText = arrowText;
    }

    @Override
    public boolean hasTitle() {
        return mTitle != null && !mTitle.equals("");
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public void setTitle(String title) {
        this.mTitle = title;
    }

    @Override
    public Span getTitleSpan() {
        return mTitleSpan;
    }

    @Override
    public void setTitleSpan(Span titleSpan) {
        this.mTitleSpan = titleSpan;
    }

    @Override
    public String getMenu() {
        return mMenu;
    }

    @Override
    public void setMarginTop(int marginTop) {
        this.mMarginTop = marginTop;
    }

    @Override
    public int getMarginTop() {
        return mMarginTop;
    }

    @Override
    public int getLayoutId() {
        return R.layout.menu_panel_normal;
    }

    @Override
    public View inflateView(Context context) {
        return LayoutInflater.from(context).inflate(getLayoutId(), null);
    }

    @Override
    public void initData(View menuView) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.topMargin = getMarginTop();
        menuView.setLayoutParams(lp);
        TextView menuTextView = menuView.findViewById(R.id.tv_menu_panel_normal_menu);
        ImageView arrowIconView = menuView.findViewById(R.id.iv_menu_panel_normal_arrow);
        TextView arrowTextView = menuView.findViewById(R.id.tv_menu_panel_normal_arrow);
        menuTextView.setText(mMenu);
        if (mShowArrowIcon) {
            arrowIconView.setVisibility(View.VISIBLE);
        } else {
            arrowIconView.setVisibility(View.INVISIBLE);
        }
        arrowTextView.setText(mArrowText);
    }
}
