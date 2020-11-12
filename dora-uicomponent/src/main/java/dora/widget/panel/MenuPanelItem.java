package dora.widget.panel;

import android.content.Context;
import android.view.View;

/**
 * 叶子节点。
 */
public interface MenuPanelItem extends MenuPanelItemRoot {

    int getLayoutId();

    View inflateView(Context context);

    String getMenu();

    void initData(View menuView);
}
