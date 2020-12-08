package dora.widget.panel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

public abstract class AbsMenuPanelItem<T extends IMenu> implements MenuPanelItem {

    protected String mTitle;
    protected Span mTitleSpan;
    protected T mMenu;
    protected int mMarginTop;

    public AbsMenuPanelItem(int marginTop, @NonNull T menu) {
        this(marginTop, "", new Span(), menu);
    }

    public AbsMenuPanelItem(int marginTop, String title, Span titleSpan, @NonNull T menu) {
        this.mMarginTop = marginTop;
        this.mTitle = title;
        this.mTitleSpan = titleSpan;
        this.mMenu = menu;
    }

    @Override
    public View inflateView(Context context) {
        View view = LayoutInflater.from(context).inflate(getLayoutId(), null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.topMargin = mMarginTop;
        view.setLayoutParams(lp);
        return view;
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
        return mMenu.getMenuName();
    }

    @Override
    public int getMarginTop() {
        return mMarginTop;
    }

    @Override
    public void setMarginTop(int marginTop) {
        this.mMarginTop = marginTop;
    }
}