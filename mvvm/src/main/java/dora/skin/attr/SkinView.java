package dora.skin.attr;

import android.view.View;

import java.util.List;

/**
 * 要换肤的控件。
 */
public class SkinView {

    //    SoftReference<View> viewRef;
    View view;
    List<SkinAttr> attrs;

    public SkinView(View view, List<SkinAttr> skinAttrs) {
        this.view = view;
        this.attrs = skinAttrs;
    }

    public void apply() {
        // View view = viewRef.get();
        if (view == null) {
            return;
        }
        for (SkinAttr attr : attrs) {
            attr.apply(view);
        }
    }
}
