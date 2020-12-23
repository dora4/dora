package dora.widget.panel.menu;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import dora.util.TextUtils;
import dora.widget.panel.MenuPanelItem;
import dora.widget.R;

public class InputMenuPanelItem implements MenuPanelItem {

    private String mTitle;
    private Span mTitleSpan;
    private String mMenu;
    private String mContent;
    private int mMarginTop;
    private ContentWatcher mWatcher;

    public InputMenuPanelItem(String menu) {
        this(1, "", new Span(), menu, "", null);
    }

    public InputMenuPanelItem(String menu, String content) {
        this(1, "", new Span(), menu, content, null);
    }

    public InputMenuPanelItem(String menu, String content, ContentWatcher watcher) {
        this(1, "", new Span(), menu, content, watcher);
    }

    /**
     *
     * @param marginTop
     * @param title
     * @param titleSpan
     * @param menu 提示信息
     * @param content 文本框输入的内容
     * @param watcher
     */
    public InputMenuPanelItem(int marginTop, String title, Span titleSpan, String menu, String content, ContentWatcher watcher) {
        this.mMarginTop = marginTop;
        this.mTitle = title;
        this.mTitleSpan = titleSpan;
        this.mMenu = menu;
        this.mContent = content;
        this.mWatcher = watcher;
    }

    @Override
    public void initData(View menuView) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.topMargin = getMarginTop();
        menuView.setLayoutParams(lp);
        EditText editText = menuView.findViewById(R.id.et_menu_panel_input);
        editText.setHint(mMenu);
        if (TextUtils.isNotEmpty(mContent)) {
            editText.setText(mContent);
            editText.setSelection(mContent.length());
        }
        if (mWatcher != null) {
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mWatcher.onContentChanged(InputMenuPanelItem.this, s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }
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
        return R.layout.menu_panel_input;
    }

    @Override
    public View inflateView(Context context) {
        return LayoutInflater.from(context).inflate(getLayoutId(), null);
    }

    public interface ContentWatcher {
        void onContentChanged(InputMenuPanelItem item, String content);
    }
}
