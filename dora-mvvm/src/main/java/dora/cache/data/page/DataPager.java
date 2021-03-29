package dora.cache.data.page;

import dora.cache.data.visitor.IPageDataVisitor;

import java.util.List;

public final class DataPager<T> implements IDataPager<T> {

    /**
     * 建议从0开始累加。
     */
    private int mCurrentPage;

    /**
     * 每页的数据数量不能为0，0不能做除数。
     */
    private int mPageSize = 1;
    private List<T> mData;
    private PageCallback<T> mCallback;

    public DataPager(List<T> data) {
        this.mData = data;
    }

    public int getNextPage() {
        return mCurrentPage + 1;
    }

    @Override
    public void setCurrentPage(int currentPage) {
        this.mCurrentPage = currentPage;
    }

    @Override
    public void setPageSize(int pageSize) {
        this.mPageSize = pageSize;
    }

    @Override
    public void setPageCallback(PageCallback<T> callback) {
        this.mCallback = callback;
    }

    @Override
    public void accept(IPageDataVisitor visitor) {
        visitor.visitDataPager(this);
    }

    @Override
    public void onResult(List<T> data) {
        mCallback.onResult(data);
    }

    public int getCurrentPage() {
        return mCurrentPage;
    }

    @Override
    public int getPageSize() {
        return mPageSize;
    }

    @Override
    public List<T> getData() {
        return mData;
    }
}
