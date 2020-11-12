package dora.widget.panel.menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import dora.widget.panel.MenuPanelItem;
import dora.widget.R;

public class NormalMenuPanelItem implements MenuPanelItem {

    private String mTitle;
    private Span mTitleSpan;
    private String mMenu;
    private int mMarginTop;

    public NormalMenuPanelItem(String menu) {
        this(1, menu);
    }

    public NormalMenuPanelItem(int marginTop, String menu) {
        this(marginTop, "", new Span(10, 10), menu);
    }

    public NormalMenuPanelItem(int marginTop, String title, Span titleSpan, String menu) {
        this.mMarginTop = marginTop;
        this.mTitle = title;
        this.mTitleSpan = titleSpan;
        this.mMenu = menu;
    }

    public NormalMenuPanelItem(String title, String menu) {
        this(1, title, new Span(10, 10), menu);
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
        TextView textView = menuView.findViewById(R.id.tv_menu_panel_normal_menu);
        textView.setText(mMenu);
    }
}
