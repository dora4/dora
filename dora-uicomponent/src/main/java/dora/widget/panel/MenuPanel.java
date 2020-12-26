package dora.widget.panel;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * 通用功能菜单，类似于RecyclerView。
 */
public class MenuPanel extends ScrollView implements View.OnClickListener {

    private static final String TAG = "MenuPanel";
    private static final int SEEK_FOR_ITEM_ERROR_NOT_FOUND = -1;
    private static final int SEEK_FOR_ITEM_ERROR_MISS_MENU_NAME = -2;
    /**
     * 面板的背景颜色，一般为浅灰色。
     */
    private int mPanelBgColor = 0xFFF5F5F9;
    protected List<MenuPanelItem> mItems = new ArrayList<>();
    protected List<View> mItemViewsCache = new ArrayList<>();
    private OnPanelMenuClickListener mOnPanelMenuClickListener;
    private OnPanelScrollListener mOnPanelScrollListener;
    private List<GroupInfo> mGroupInfo = new ArrayList<>();
    private LinkedList<ListenerDelegate> mListenerInfo = new LinkedList<>();
    protected FrameLayout mPanelRoot;
    /**
     * 存放Menu和Custom View。
     */
    protected LinearLayout mContainer;

    public MenuPanel(Context context) {
        super(context);
        init(context);
    }

    public MenuPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MenuPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public MenuPanel removeItem(MenuPanelItem item) {
        int position = seekForItemPosition(item);
        if (position != SEEK_FOR_ITEM_ERROR_NOT_FOUND &&
                position != SEEK_FOR_ITEM_ERROR_MISS_MENU_NAME) {
            removeItem(position);
        } else {
            Log.e(TAG, "failed to seekForItemPosition，" + position);
        }
        return this;
    }

    private void init(Context context) {
        setFillViewport(true);
        addContainer(context);
    }

    public void setOnPanelMenuClickListener(OnPanelMenuClickListener l) {
        this.mOnPanelMenuClickListener = l;
    }

    public void setOnPanelScrollListener(OnPanelScrollListener l) {
        this.mOnPanelScrollListener = l;
    }

    public View parseItemView(MenuPanelItem item) {
        return parseItemView(item, false);
    }

    public View parseItemView(MenuPanelItem item, boolean isLoadData) {
        View menuView = item.inflateView(getContext());
        if (isLoadData) {
            item.initData(menuView);
        }
        return menuView;
    }

    public List<MenuPanelItem> getItems() {
        return mItems;
    }

    public MenuPanelItem getItem(int position) {
        return mItems.get(position);
    }

    public List<View> getItemViewsCache() {
        return mItemViewsCache;
    }

    public GroupInfo getGroupInfo(MenuPanelItem item) {
        for (GroupInfo groupInfo : mGroupInfo) {
            if (groupInfo.hasItem(item)) {
                return groupInfo;
            }
        }
        return null;
    }

    /**
     * 根据item的position移除一个item，此方法被多处引用，修改前需要理清布局层级结构。
     *
     * @param position
     * @return
     */
    public MenuPanel removeItem(int position) {
        MenuPanelItem item = this.mItems.get(position);
        GroupInfo groupInfo = getGroupInfo(item);
        boolean belongToGroup = groupInfo != null;
        View view = getCacheViewFromPosition(position);
        if (!belongToGroup) {
            if (mContainer != null) {
                this.mContainer.removeView(view);
            }
        } else {
            //属于一个组
            LinearLayout menuGroupCard = groupInfo.groupMenuCard;
            menuGroupCard.removeView(view);
            groupInfo.removeItem(item);
            //一个组内的item全部被移除后，也移除掉这个组
            if (groupInfo.isEmpty()) {
                if (mContainer != null) {
                    //连同title一起移除
                    this.mContainer.removeView(menuGroupCard);
                }
                mGroupInfo.remove(groupInfo);
            }
        }
        this.mItems.remove(position);
        this.mItemViewsCache.remove(position);
        this.mListenerInfo.remove(position);
        return this;
    }

    /**
     * 清空所有item和相关view。
     */
    public MenuPanel clearAll() {
        if (mItems.size() > 0) {
            mItems.clear();
        }
        if (mContainer != null) {
            mContainer.removeAllViews();
        }
        mItemViewsCache.clear();
        mGroupInfo.clear();
        mListenerInfo.clear();
        return this;
    }

    /**
     * 移除连续的item。
     *
     * @param start 第一个item的下标，包括
     * @param end   最后一个item的下标，包括
     * @return
     */
    public MenuPanel removeItemRange(int start, int end) {
        for (int i = start; i < end + 1; i++) {
            removeItem(start);
        }
        return this;
    }

    /**
     * 从某个位置移除到最后一个item。
     *
     * @param start 第一个item的下标，包括
     * @return
     */
    public MenuPanel removeItemFrom(int start) {
        int end = mItems.size() - 1;
        if (start <= end) {
            //有就移除
            removeItemRange(start, end);
        }
        return this;
    }

    /**
     * 从第一个item移除到某个位置。
     *
     * @param end 最后一个item的下标，包括
     * @return
     */
    public MenuPanel removeItemTo(int end) {
        int start = 0;
        removeItemRange(start, end);
        return this;
    }

    public int getItemCount() {
        return mItems.size();
    }

    public final MenuPanel addMenuGroup(MenuPanelItemGroup itemGroup) {
        boolean hasTitle = itemGroup.hasTitle();
        List<MenuPanelItem> items = itemGroup.getItems();
        TextView titleView = new TextView(getContext());
        titleView.setPadding(itemGroup.getTitleSpan().left, itemGroup.getTitleSpan().top,
                itemGroup.getTitleSpan().right, itemGroup.getTitleSpan().bottom);
        titleView.setText(itemGroup.getTitle());
        titleView.setTextSize(15);
        titleView.setTextColor(0xFF999999);
        LinearLayout menuGroupCard = new LinearLayout(getContext());
        menuGroupCard.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.topMargin = itemGroup.getMarginTop();
        menuGroupCard.setLayoutParams(lp);
        if (hasTitle) {
            menuGroupCard.addView(titleView);
        }
        for (MenuPanelItem item : items) {
            //清除组内item的边距等
            applyDefault(item);
            addMenuToCard(item, menuGroupCard);
        }
        if (mContainer != null) {
            mContainer.addView(menuGroupCard);
            //保存菜单组信息
            mGroupInfo.add(new GroupInfo(hasTitle, items, menuGroupCard));
        }
        return this;
    }

    @Override
    public void addView(View child) {
        if (!(child instanceof FrameLayout)) {
            return;
        }
        if (getChildCount() > 1) {
            return;
        }
        super.addView(child);
    }

    private void addContainer(Context context) {
        mPanelRoot = new FrameLayout(context);
        mContainer = new LinearLayout(context);
        mContainer.setOrientation(LinearLayout.VERTICAL);
        mContainer.setBackgroundColor(mPanelBgColor);
        mPanelRoot.addView(mContainer);
        addView(mPanelRoot);
    }

    public FrameLayout getPanelRoot() {
        return mPanelRoot;
    }

    public LinearLayout getContainer() {
        return mContainer;
    }

    public final MenuPanel addMenu(MenuPanelItem item) {
        View menuView = bindItemListener(item);
        if (!item.hasTitle()) {
            mContainer.addView(menuView);
        } else {
            TextView titleView = new TextView(getContext());
            titleView.setPadding(item.getTitleSpan().left, item.getTitleSpan().top,
                    item.getTitleSpan().right, item.getTitleSpan().bottom);
            titleView.setText(item.getTitle());
            titleView.setTextSize(15);
            titleView.setTextColor(0xFF999999);
            LinearLayout menuCard = new LinearLayout(getContext());
            menuCard.setOrientation(LinearLayout.VERTICAL);
            menuCard.addView(titleView);
            menuCard.addView(menuView);
            mContainer.addView(menuCard);
        }
        return this;
    }

    private void addMenuToCard(MenuPanelItem item, LinearLayout container) {
        View menuView = bindItemListener(item);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.topMargin = item.getMarginTop();
        menuView.setLayoutParams(lp);
        container.addView(menuView);
    }

    public int seekForItemPosition(MenuPanelItem item) {
        for (int i = 0; i < mItems.size(); i++) {
            MenuPanelItem mpi = mItems.get(i);
            String menu = mpi.getMenu();
            if (menu.equals("") || item.getMenu().equals("")) {
                return SEEK_FOR_ITEM_ERROR_MISS_MENU_NAME;  //失去菜单名称
            }
            if (menu.equals(item.getMenu())) {
                return i;
            }
        }
        return SEEK_FOR_ITEM_ERROR_NOT_FOUND;
    }

    /**
     * 获取MenuPanel中条目布局中的子控件，推荐使用。
     *
     * @param position
     * @param viewId
     * @return
     */
    public View getCacheChildView(int position, int viewId) {
        View menuView = getCacheViewFromPosition(position);
        if (menuView != null) {
            return menuView.findViewById(viewId);
        }
        return null;
    }

    /**
     * 获取item的view，用于修改item的数据。
     *
     * @param item
     * @return
     */
    public View getCacheViewFromItem(MenuPanelItem item) {
        int position = seekForItemPosition(item);
        if (position != SEEK_FOR_ITEM_ERROR_NOT_FOUND &&
                position != SEEK_FOR_ITEM_ERROR_MISS_MENU_NAME) {
            return getCacheViewFromPosition(position);
        }
        return null;
    }

    /**
     * 获取item的view，用于修改item的数据。
     *
     * @param position item的位置，从0开始
     * @return
     */
    public View getCacheViewFromPosition(int position) {
        if (position < mItemViewsCache.size()) {
            return mItemViewsCache.get(position);
        }
        return null;
    }

    protected View getCacheViewFromTag(String tag) {
        for (ListenerDelegate delegate : mListenerInfo) {
            String dtag = delegate.getTag();
            if (dtag.equals(tag)) {
                int position = delegate.getPosition();
                return getCacheViewFromPosition(position);
            }
        }
        return null;
    }

    /**
     * 绑定item的点击事件。
     *
     * @param item
     * @return 绑定成功后返回item的view
     */
    private View bindItemListener(MenuPanelItem item) {
        mItems.add(item);
        //解析Item所对应的布局，并调用item的initData
        View menuView = parseItemView(item, true);
        mItemViewsCache.add(menuView);
        String tag = UUID.randomUUID().toString().substring(0, 16);
        menuView.setTag(tag);
        ListenerDelegate delegate = getListenerInfo(tag);
        menuView.setOnClickListener(delegate);
        mListenerInfo.add(delegate);
        return menuView;
    }

    private void applyDefault(MenuPanelItem item) {
        item.setMarginTop((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1,
                getResources().getDisplayMetrics()));   //item的上边距修改为1dp
        item.setTitle(""); //item去掉标题
        item.setTitleSpan(new MenuPanelItem.Span());    //item去掉标题边距
    }

    /**
     * 不是菜单，所以不会影响菜单的点击事件位置，但需要自己处理控件内部的点击事件。
     *
     * @param view
     * @param <T>
     */
    public <T extends View> MenuPanel addCustomView(T view) {
        mContainer.addView(view);
        return this;
    }

    public <T extends View> MenuPanel addCustomView(T view, int index) {
        mContainer.addView(view, index);
        return this;
    }

    public MenuPanel removeCustomViewAt(int position) {
        if (mContainer.getChildCount() > position) {
            //有就移除
            mContainer.removeViewAt(position);
        }
        return this;
    }

    /**
     * 样式等参数改变才需要更新，只有类似于addItem、removeItem这样的，不需要调用此方法。
     */
    public void updatePanel() {
        requestLayout();
    }

    public ListenerDelegate getListenerInfo(String tag) {
        return new ListenerDelegate(tag, mItems.size() - 1, this);
    }

    static class GroupInfo {

        boolean hasTitle;
        List<MenuPanelItem> items;
        LinearLayout groupMenuCard;

        public GroupInfo(boolean hasTitle, @NonNull List<MenuPanelItem> items, LinearLayout groupMenuCard) {
            this.hasTitle = hasTitle;
            this.items = items;
            this.groupMenuCard = groupMenuCard;
        }

        public boolean hasItem(MenuPanelItem item) {
            return items.contains(item);
        }

        public int getItemCount() {
            return this.items.size();
        }

        public void addItem(MenuPanelItem item) {
            this.items.add(item);
        }

        public void removeItem(MenuPanelItem item) {
            this.items.remove(item);
        }

        public boolean isEmpty() {
            return items.size() == 0;
        }

        public List<MenuPanelItem> getItems() {
            return items;
        }
    }

    @Override
    public void onClick(View v) {
        if (mOnPanelMenuClickListener != null) {
            String tag = (String) v.getTag();
            for (ListenerDelegate delegate : mListenerInfo) {
                if (delegate.getTag().equals(tag)) {
                    int clickPos = delegate.getPosition();
                    mOnPanelMenuClickListener.onMenuClick(clickPos, v, mItems.get(clickPos).getMenu());
                    break;
                }
            }
        }
    }

    public MenuPanel setPanelBgColor(int color) {
        this.mPanelBgColor = color;
        this.mContainer.setBackgroundColor(mPanelBgColor);
        return this;
    }

    public interface OnPanelMenuClickListener {
        void onMenuClick(int position, View view, String menuName);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mOnPanelScrollListener != null) {
            if (getScrollY() == 0) {
                mOnPanelScrollListener.onScrollToTop();
            } else if (mPanelRoot.getMeasuredHeight() == getScrollY() + getHeight()) {
                mOnPanelScrollListener.onScrollToBottom();
            }
        }
    }

    public interface OnPanelScrollListener {
        void onScrollToTop();

        void onScrollToBottom();
    }

    static class ListenerDelegate implements OnClickListener {

        private String mTag;
        private int mPosition;
        private OnClickListener mListener;

        public ListenerDelegate(String tag, int position, OnClickListener listener) {
            this.mTag = tag;
            this.mPosition = position;
            this.mListener = listener;
        }

        public int getPosition() {
            return mPosition;
        }

        public String getTag() {
            return mTag;
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v);
        }
    }
}