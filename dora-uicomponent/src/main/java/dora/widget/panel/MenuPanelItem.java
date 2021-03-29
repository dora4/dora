package dora.widget.panel;

import android.content.Context;
import android.view.View;

/**
 * 实现它来自定义面板的菜单，你也可以使用{@link dora.widget.panel.menu.AbsMenuPanelItem}。
 */
public interface MenuPanelItem extends MenuPanelItemRoot {

    int getLayoutId();

    View inflateView(Context context);

    String getMenu();

    void initData(View menuView);
}
