package dora.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public abstract class BaseAdapter<T, B extends ViewDataBinding> extends RecyclerView.Adapter<ViewHolder<T, B>> {

    protected List<T> mItems;
    protected OnRecyclerViewItemClickListener mOnItemClickListener = null;
    protected OnRecyclerViewItemLongClickListener mOnItemLongClickListener = null;

    public BaseAdapter(List<T> items) {
        super();
        this.mItems = items;
    }

    /**
     * 遍历所有 {@link ViewHolder}, 释放他们需要释放的资源
     *
     * @param recyclerView {@link RecyclerView}
     */
    public static void releaseAllHolder(RecyclerView recyclerView) {
        if (recyclerView == null) {
            return;
        }
        for (int i = recyclerView.getChildCount() - 1; i >= 0; i--) {
            final View view = recyclerView.getChildAt(i);
            RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(view);
            if (viewHolder instanceof ViewHolder) {
                ((ViewHolder) viewHolder).onRelease();
            }
        }
    }

    /**
     * 创建 {@link ViewHolder}
     *
     * @param parent   父容器
     * @param viewType 布局类型
     * @return {@link ViewHolder}
     */
    @Override
    public ViewHolder<T, B> onCreateViewHolder(ViewGroup parent, final int viewType) {
        B binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                getLayoutId(viewType), parent, false);
        View view = binding.getRoot();
        ViewHolder<T, B> mHolder = getHolder(view, viewType);
        mHolder.setBinding(binding);
        mHolder.setOnItemClickListener(new ViewHolder.OnViewClickListener() {
            @Override
            public void onViewClick(View view, int position) {
                if (mOnItemClickListener != null && mItems.size() > 0) {
                    mOnItemClickListener.onItemClick(view, viewType, mItems.get(position), position);
                }
            }
        });
        mHolder.setOnItemLongClickListener(new ViewHolder.OnViewLongClickListener() {
            @Override
            public boolean onViewLongClick(View view, int position) {
                if (mOnItemLongClickListener != null && mItems.size() > 0) {
                    mOnItemLongClickListener.onItemLongClick(view, viewType, mItems.get(position), position);
                }
                return false;
            }
        });
        return mHolder;
    }

    /**
     * 绑定数据
     *
     * @param holder   {@link ViewHolder}
     * @param position 在 RecyclerView 中的位置
     */
    @Override
    public void onBindViewHolder(ViewHolder<T, B> holder, int position) {
        holder.setData(holder.mBinding, mItems.get(position), position);
    }

    /**
     * 返回数据总个数
     *
     * @return 数据总个数
     */
    @Override
    public int getItemCount() {
        return mItems.size();
    }

    /**
     * 返回数据集合
     *
     * @return 数据集合
     */
    public List<T> getItems() {
        return mItems;
    }

    /**
     * 获得 RecyclerView 中某个 position 上的 item 数据
     *
     * @param position 在 RecyclerView 中的位置
     * @return 数据
     */
    public T getItem(int position) {
        return mItems == null ? null : mItems.get(position);
    }

    /**
     * 让子类实现用以提供 {@link ViewHolder}
     *
     * @param v        用于展示的 {@link View}
     * @param viewType 布局类型
     * @return {@link ViewHolder}
     */
    @NonNull
    public abstract ViewHolder<T, B> getHolder(@NonNull View v, int viewType);

    /**
     * 提供用于 item 布局的 {@code layoutId}
     *
     * @param viewType 布局类型
     * @return 布局 id
     */
    public abstract int getLayoutId(int viewType);

    /**
     * 设置 item 点击事件
     *
     * @param listener
     */
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    /**
     * 设置 item 点击事件
     *
     * @param listener
     */
    public void setOnItemLongClickListener(OnRecyclerViewItemLongClickListener listener) {
        this.mOnItemLongClickListener = listener;
    }

    /**
     * item 点击事件
     *
     * @param <T>
     */
    public interface OnRecyclerViewItemClickListener<T> {

        /**
         * item 被点击
         *
         * @param view     被点击的 {@link View}
         * @param viewType 布局类型
         * @param data     数据
         * @param position 在 RecyclerView 中的位置
         */
        void onItemClick(@NonNull View view, int viewType, @NonNull T data, int position);
    }

    /**
     * item 长按事件
     *
     * @param <T>
     */
    public interface OnRecyclerViewItemLongClickListener<T> {

        /**
         * item 被点击
         *
         * @param view     被点击的 {@link View}
         * @param viewType 布局类型
         * @param data     数据
         * @param position 在 RecyclerView 中的位置
         */
        boolean onItemLongClick(@NonNull View view, int viewType, @NonNull T data, int position);
    }
}