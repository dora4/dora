package dora;

import dora.util.IntentUtils;

/**
 * 在Activity中切换多个Fragment时使用。
 */
public interface PageSwitcher {

    /**
     * Fragment非流式切换之显示页面。
     */
    void showPage(String key);

    /**
     * Fragment非流式切换之显示页面。
     */
    void showPage(String key, IntentUtils.Extras extras);

    /**
     * Fragment流式切换之显示上一页。
     */
    void lastPage();

    /**
     * Fragment流式切换之显示上一页。
     */
    void lastPage(IntentUtils.Extras extras);

    /**
     * Fragment流式切换之显示下一页。
     */
    void nextPage();

    /**
     * Fragment流式切换之显示下一页。
     */
    void nextPage(IntentUtils.Extras extras);

    /**
     * 到达第一页再往上一页是否显示最后一页，到达最后一页再往下一页是否显示第一页。
     */
    boolean isLoop();
}
