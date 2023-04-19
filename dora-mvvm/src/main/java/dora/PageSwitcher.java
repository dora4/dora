package dora;

import dora.util.IntentUtils;

/**
 * 在Activity中切换多个Fragment时使用。
 */
public interface PageSwitcher {

    /**
     * Fragment流式切换之显示页面。
     */
    void showPage(String key);

    /**
     * Fragment流式切换之显示页面。
     */
    void showPage(String key, IntentUtils.Extras extras);

    /**
     * Fragment流式切换之显示下一页。
     */
    void nextPage();

    /**
     * Fragment流式切换之显示下一页。
     */
    void nextPage(IntentUtils.Extras extras);
}
