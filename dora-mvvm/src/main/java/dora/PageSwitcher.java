package dora;

import dora.util.IntentUtils;

/**
 * 在Activity中切换多个Fragment时使用。
 */
public interface PageSwitcher {

    void showPage(String name);
    void showPage(String name, IntentUtils.Extras extras);
    void nextPage();
    void nextPage(IntentUtils.Extras extras);
}
