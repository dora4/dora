package dora.widget.panel.menu;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import dora.widget.panel.MenuPanelItem;
import dora.widget.R;
import dora.widget.panel.drawable.TagDrawable;

public class TagMenuPanelItem implements MenuPanelItem {

    private int mMarginTop;
    private String mTitle;
    private Span mTitleSpan;
    private String mMenu;
    private String mTag;
    private String mTagColor;

    public TagMenuPanelItem(String menu, String tag,
                            String tagColor) {
        this(1, menu, tag, tagColor);
    }

    public TagMenuPanelItem(int marginTop, String menu, String tag,
                            String tagColor) {
        this(marginTop, "", new Span(10, 10), menu, tag, tagColor);
    }

    public TagMenuPanelItem(int marginTop, String title, Span titleSpan, String menu, String tag,
                            String tagColor) {
        this.mMarginTop = marginTop;
        this.mTitle = title;
        this.mTitleSpan = titleSpan;
        this.mMenu = menu;
        this.mTag = tag;
        this.mTagColor = tagColor;
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

    public String getTag() {
        return mTag;
    }

    public String getTagColor() {
        return mTagColor;
    }

    @Override
    public int getLayoutId() {
        return R.layout.menu_panel_tag;
    }

    @Override
    public View inflateView(Context context) {
        return LayoutInflater.from(context).inflate(getLayoutId(), null);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void initData(View menuView) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.topMargin = getMarginTop();
        menuView.setLayoutParams(lp);
        TextView tv_menu_panel_tag_menu = menuView.findViewById(R.id.tv_menu_panel_tag_menu);
        TextView tv_menu_panel_tag_tag = menuView.findViewById(R.id.tv_menu_panel_tag_tag);
        tv_menu_panel_tag_menu.setText(mMenu);
        if (mTag != null && mTag.length() > 0) {
            tv_menu_panel_tag_tag.setText(mTag);
            int color = Color.parseColor(mTagColor);
            TagDrawable drawable = new TagDrawable(color, 0, 0,
                    tv_menu_panel_tag_tag.getWidth(),
                    tv_menu_panel_tag_tag.getHeight());
            tv_menu_panel_tag_tag.setBackground(drawable);
            tv_menu_panel_tag_tag.setTextColor(Color.WHITE);
        }
    }
}
