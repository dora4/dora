package dora.cache.data.visitor;

import dora.cache.data.page.IDataPager;

public abstract class BasePageDataVisitor<T> implements IPageDataVisitor<T> {

    @Override
    public void visitDataPager(IDataPager pager) {
        pager.onResult(getResult(pager.getData(), pager.getData().size(), pager.getCurrentPage(), pager.getPageSize()));
    }
}
