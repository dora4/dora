package dora.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class ViewHolder<T, B> extends RecyclerView.ViewHolder implements
        View.OnClickListener, View.OnLongClickListener {

    protected final String TAG = this.getClass().getSimpleName();
    protected OnViewClickListener mOnViewClickListener = null;
    protected OnViewLongClickListener mOnViewLongClickListener = null;
    protected B mBinding;

    public ViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    public void setBinding(B binding) {
        this.mBinding = binding;
    }

    /**
     * 设置数据
     *
     * @param data     数据
     * @param position 在 RecyclerView 中的位置
     */
    public abstract void setData(@NonNull B binding, @NonNull T data, int position);

    /**
     * 在 Activity 的 onDestroy 中使用 {@link BaseAdapter#releaseAllHolder(RecyclerView)} 方法 (super.onDestroy() 之前)
     * {@link ViewHolder#onRelease()} 才会被调用, 可以在此方法中释放一些资源
     */
    protected void onRelease() {

    }

    @Override
    public void onClick(View view) {
        if (mOnViewClickListener != null) {
            mOnViewClickListener.onViewClick(view, this.getPosition());
        }
    }

    @Override
    public boolean onLongClick(View view) {
        if (mOnViewLongClickListener != null) {
            return mOnViewLongClickListener.onViewLongClick(view, this.getPosition());
        }
        return false;
    }

    public void setOnItemClickListener(OnViewClickListener listener) {
        this.mOnViewClickListener = listener;
    }

    public void setOnItemLongClickListener(OnViewLongClickListener listener) {
        this.mOnViewLongClickListener = listener;
    }

    /**
     * item 点击事件
     */
    public interface OnViewClickListener {

        /**
         * item 被点击
         *
         * @param view     被点击的 {@link View}
         * @param position 在 RecyclerView 中的位置
         */
        void onViewClick(View view, int position);
    }

    /**
     * item 长按事件
     */
    public interface OnViewLongClickListener {

        /**
         * item 被长按
         *
         * @param view     被长按的 {@link View}
         * @param position 在 RecyclerView 中的位置
         */
        boolean onViewLongClick(View view, int position);
    }
}