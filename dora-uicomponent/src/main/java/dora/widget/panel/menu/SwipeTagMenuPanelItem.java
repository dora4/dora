package dora.widget.panel.menu;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import dora.widget.R;
import dora.widget.panel.MenuPanelItem;
import dora.widget.panel.drawable.TagDrawable;

/**
 * 提供一种复杂一点的可侧滑菜单条目的示例。
 */
public class SwipeTagMenuPanelItem implements MenuPanelItem {

    private int mMarginTop;
    private String mTitle;
    private Span mTitleSpan;
    private String mMenu;
    private String mTag;
    private String mTagColor;
    private OnDeleteListener mOnDeleteListener;

    public SwipeTagMenuPanelItem(int marginTop, String title, Span titleSpan, String menu, String tag,
                                 String tagColor, OnDeleteListener listener) {
        this.mMarginTop = marginTop;
        this.mTitle = title;
        this.mTitleSpan = titleSpan;
        this.mMenu = menu;
        this.mTag = tag;
        this.mTagColor = tagColor;
        this.mOnDeleteListener = listener;
    }

    public void setOnDeleteListener(OnDeleteListener l) {
        this.mOnDeleteListener = l;
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
        return R.layout.menu_panel_swipe_tag;
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
        TextView tv_menu_panel_swipe_tag_menu = menuView.findViewById(R.id.tv_menu_panel_swipe_tag_menu);
        Button btn_menu_panel_swipe_tag_delete = menuView.findViewById(R.id.btn_menu_panel_swipe_tag_delete);
        TextView tv_menu_panel_swipe_tag_tag = menuView.findViewById(R.id.tv_menu_panel_swipe_tag_tag);
        tv_menu_panel_swipe_tag_menu.setText(mMenu);
        btn_menu_panel_swipe_tag_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnDeleteListener != null) {
                    mOnDeleteListener.onDelete(SwipeTagMenuPanelItem.this, mMenu, mTag, mTagColor);
                }
            }
        });
        if (mTag != null && mTag.length() > 0) {
            tv_menu_panel_swipe_tag_tag.setText(mTag);
            int color = Color.parseColor(mTagColor);
            TagDrawable drawable = new TagDrawable(color, 0, 0,
                    tv_menu_panel_swipe_tag_tag.getMeasuredWidth(),
                    tv_menu_panel_swipe_tag_tag.getMeasuredHeight());
            tv_menu_panel_swipe_tag_tag.setBackground(drawable);
            tv_menu_panel_swipe_tag_tag.setTextColor(Color.WHITE);
        }
    }

    public interface OnDeleteListener {
        void onDelete(SwipeTagMenuPanelItem item, String menu, String tag, String tagColor);
    }
}
