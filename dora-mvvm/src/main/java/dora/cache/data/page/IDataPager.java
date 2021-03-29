package dora.cache.data.page;

import androidx.annotation.NonNull;
import dora.cache.data.visitor.IPageDataVisitor;

import java.util.List;

public interface IDataPager<T> extends PageCallback<T> {

    /**
     * 设置当前是第几页。
     *
     * @param currentPage 建议从0开始
     */
    void setCurrentPage(int currentPage);

    void setPageSize(int pageSize);

    void setPageCallback(PageCallback<T> callback);

    void accept(IPageDataVisitor visitor);

    int getCurrentPage();

    /**
     * 每页有几条数据？
     *
     * @return 不要返回0，0不能做除数
     */
    int getPageSize();

    @NonNull
    List<T> getData();
}
