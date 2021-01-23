package dora;

import dora.util.IntentUtils;

public interface PageSwitcher {

    void showPage(String name);
    void showPage(String name, IntentUtils.Extras extras);
    void nextPage();
    void nextPage(IntentUtils.Extras extras);
}
