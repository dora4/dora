package dora.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecyclerAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter {

    public static final int ITEM_TYPE = 0;
    public static final int EMPTY_TYPE = 99;
    public static final int BASE_HEADER_TYPE = 100;
    public static final int BASE_FOOTER_TYPE = 200;

    protected Context context;
    protected LayoutInflater inflater;

    protected List<T> dataSource;

    protected SparseArray<View> headers;
    protected int resId;
    protected View emptyView;
    protected SparseArray<View> footers;

    protected OnItemClickListener<T> onItemClickListener;
    protected View.OnClickListener onClickListener;

    protected boolean showHeaderFooterWhenEmpty;

    public BaseRecyclerAdapter(@NonNull Context context, @LayoutRes int resId) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.resId = resId;

        this.dataSource = new ArrayList<>();
        this.headers = new SparseArray<>();
        this.footers = new SparseArray<>();
    }

    public List<T> getDataSource() {
        return dataSource;
    }

    public void clearDataSource() {
        dataSource.clear();
    }

    public void addAll(List<T> list) {
        dataSource.addAll(list);
    }

    public void addHeaderView(View header) {
        if (headers.indexOfValue(header) < 0) {
            headers.put(BASE_HEADER_TYPE + headers.size(), header);
        }
    }

    public void removeHeaderView() {
        if (headers.size() == 1) {
            headers.clear();
        }
    }

    public void addFooterView(View footer) {
        if (footers.indexOfValue(footer) < 0) {
            footers.put(BASE_FOOTER_TYPE + footers.size(), footer);
        }
    }

    public void removeFooterView() {
        if (footers.size() == 1) {
            footers.clear();
        }
    }

    public int getHeadersCount() {
        return headers.size();
    }

    public int getFootersCount() {
        return footers.size();
    }

    public void setEmptyView(View view) {
        this.emptyView = view;
    }

    public void setShowHeaderFooterWhenEmpty(boolean showHeaderFooterWhenEmpty) {
        this.showHeaderFooterWhenEmpty = showHeaderFooterWhenEmpty;
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.onClickListener = listener;
    }

    public void setOnItemClickListener(OnItemClickListener<T> listener) {
        this.onItemClickListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        if (emptyView != null && !showHeaderFooterWhenEmpty && dataSource.isEmpty()) {
            return EMPTY_TYPE;
        }

        if (position < headers.size()) {
            return headers.keyAt(position);
        }

        int index = position - headers.size();
        int viewType;
        if (dataSource.isEmpty()) {
            if (emptyView == null) {
                viewType = footers.keyAt(index);
            } else if (index == 0) {
                viewType = EMPTY_TYPE;
            } else {
                index = index - 1;
                viewType = footers.keyAt(index);
            }
        } else {
            if (index < dataSource.size()) {
                viewType = getType(position);
            } else {
                index = index - dataSource.size();
                viewType = footers.keyAt(index);
            }
        }
        return viewType;
    }

    protected int getType(int position) {
        return ITEM_TYPE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // empty
        if (viewType == EMPTY_TYPE) {
            return new RecyclerView.ViewHolder(emptyView) {
            };
        }

        // header
        View view = headers.get(viewType);
        if (view != null) {
            return new RecyclerView.ViewHolder(view) {
            };
        }

        // footer
        view = footers.get(viewType);
        if (view != null) {
            return new RecyclerView.ViewHolder(view) {
            };
        }

        // item
        return createItemViewHolder(inflater.inflate(resId, parent, false), viewType);
    }

    protected abstract VH createItemViewHolder(View view, int viewType);

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (getItemViewType(position) == ITEM_TYPE) {
            int index = position - headers.size();
            final T obj = dataSource.get(index);
            if (onItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClicked(obj, position);
                    }
                });
            }
            bindData((VH) holder, dataSource.get(index), index);
        }
    }

    protected abstract void bindData(VH h, T obj, int position);

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        RecyclerView.LayoutManager lm = recyclerView.getLayoutManager();
        if (lm instanceof GridLayoutManager) {
            final GridLayoutManager glm = (GridLayoutManager) lm;
            glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    // fix span size when LayoutManager is GridLayoutManager
                    int viewType = getItemViewType(position);
                    int spanSize = glm.getSpanCount();
                    if (ITEM_TYPE == viewType) {
                        spanSize = 1;
                    }
                    return spanSize;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (emptyView != null && dataSource.isEmpty()) {
            if (showHeaderFooterWhenEmpty) {
                return headers.size() + 1 + footers.size();
            } else {
                return 1;
            }
        }
        return headers.size() + dataSource.size() + footers.size();
    }

    public interface OnItemClickListener<T> {
        void onItemClicked(T obj, int position);
    }
}