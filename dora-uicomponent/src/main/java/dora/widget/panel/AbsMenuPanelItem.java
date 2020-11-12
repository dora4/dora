package dora.widget.panel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;

public abstract class AbsMenuPanelItem<T extends IMenu> implements MenuPanelItem {

    protected String mTitle;
    protected Span mTitleSpan;
    protected T mMenu;
    protected int mMarginTop;

    public AbsMenuPanelItem(@NonNull T menu) {
        this(1, menu);
    }

    public AbsMenuPanelItem(int marginTop, @NonNull T menu) {
        this(marginTop, "", new Span(), menu);
    }

    public AbsMenuPanelItem(String title, @NonNull T menu) {
        //span建议适配屏幕分辨率，只在测试时使用
        this(1, title, new Span(10, 10), menu);
    }

    public AbsMenuPanelItem(String title, Span titleSpan, @NonNull T menu) {
        this(1, title, titleSpan, menu);
    }

    public AbsMenuPanelItem(int marginTop, String title, Span titleSpan, @NonNull T menu) {
        this.mMarginTop = marginTop;
        this.mTitle = title;
        this.mTitleSpan = titleSpan;
        this.mMenu = menu;
    }

    @Override
    public View inflateView(Context context) {
        return LayoutInflater.from(context).inflate(getLayoutId(), null);
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