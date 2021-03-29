package dora.widget.panel;

import java.util.Arrays;
import java.util.List;

public class MenuPanelItemGroup implements MenuPanelItemRoot {

    private int mMarginTop;
    private String mTitle;
    private Span mTitleSpan;
    private List<MenuPanelItem> mItems;

    public MenuPanelItemGroup(int marginTop, String title, MenuPanelItem.Span titleSpan, List<MenuPanelItem> items) {
        this.mMarginTop = marginTop;
        this.mTitle = title;
        this.mTitleSpan = titleSpan;
        this.mItems = items;
    }

    public MenuPanelItemGroup(String title, List<MenuPanelItem> items) {
        this(1, title, new MenuPanelItemRoot.Span(10, 10), items);
    }

    public MenuPanelItemGroup(String title, MenuPanelItem... items) {
        this(title, Arrays.asList(items));
    }

    public MenuPanelItemGroup(int marginTop, String title, MenuPanelItem.Span titleSpan, MenuPanelItem... items) {
        this(marginTop, title, titleSpan, Arrays.asList(items));
    }

    /**
     * 没有菜单组的标题。
     *
     * @param marginTop
     * @param items
     */
    public MenuPanelItemGroup(int marginTop, List<MenuPanelItem> items) {
        this(marginTop, "", new MenuPanelItem.Span(10, 10), items);
    }

    public MenuPanelItemGroup(int marginTop, MenuPanelItem... items) {
        this(marginTop, Arrays.asList(items));
    }

    /**
     * 没有菜单组的上边距。
     *
     * @param title
     * @param titleSpan
     */
    public MenuPanelItemGroup(String title, MenuPanelItem.Span titleSpan, List<MenuPanelItem> items) {
        this(0, title, titleSpan, items);
    }

    public MenuPanelItemGroup(String title, MenuPanelItem.Span titleSpan, MenuPanelItem... items) {
        this(title, titleSpan, Arrays.asList(items));
    }

    public boolean hasTitle() {
        return mTitle != null && !mTitle.equals("");
    }

    public int getMarginTop() {
        return mMarginTop;
    }

    @Override
    public void setMarginTop(int marginTop) {
        this.mMarginTop = marginTop;
    }

    public String getTitle() {
        return mTitle;
    }

    @Override
    public void setTitle(String title) {
        this.mTitle = title;
    }

    public MenuPanelItem.Span getTitleSpan() {
        return mTitleSpan;
    }

    @Override
    public void setTitleSpan(Span titleSpan) {
        this.mTitleSpan = titleSpan;
    }

    public List<MenuPanelItem> getItems() {
        return mItems;
    }
}
