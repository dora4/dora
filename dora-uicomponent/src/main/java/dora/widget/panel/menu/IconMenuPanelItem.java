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

public class IconMenuPanelItem implements MenuPanelItem {

    private String mTitle;
    private Span mTitleSpan;
    private @DrawableRes
    int mIconRes;
    private String mMenu;
    private int mMarginTop;

    public IconMenuPanelItem(@DrawableRes int iconRes, String menu) {
        this(1, iconRes, menu);
    }

    public IconMenuPanelItem(int marginTop, @DrawableRes int iconRes, String menu) {
        this(marginTop, "", new Span(), iconRes, menu);
    }
    public IconMenuPanelItem(String title, @DrawableRes int iconRes, String menu) {
        this(1, title, new Span(10, 10), iconRes, menu);
    }
    public IconMenuPanelItem(int marginTop, String title, Span titleSpan, @DrawableRes int iconRes, String menu) {
        this.mMarginTop = marginTop;
        this.mTitle = title;
        this.mTitleSpan = titleSpan;
        this.mIconRes = iconRes;
        this.mMenu = menu;
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
    public int getMarginTop() {
        return mMarginTop;
    }

    @Override
    public void setMarginTop(int marginTop) {
        this.mMarginTop = marginTop;
    }

    @Override
    public int getLayoutId() {
        return R.layout.menu_panel_icon;
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
        ImageView imageView = menuView.findViewById(R.id.iv_menu_panel_icon_menu);
        TextView textView = menuView.findViewById(R.id.tv_menu_panel_icon_menu);
        imageView.setImageResource(mIconRes);
        textView.setText(mMenu);
    }
}
