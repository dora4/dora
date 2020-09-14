package dora.autosize.attr;

import android.view.View;
import android.view.ViewGroup;

public class MarginLeftAttr extends AutoAttr {
    public MarginLeftAttr(int pxVal, int baseWidth, int baseHeight) {
        super(pxVal, baseWidth, baseHeight);
    }

    public static MarginLeftAttr generate(int val, int baseFlag) {
        MarginLeftAttr attr = null;
        switch (baseFlag) {
            case AutoAttr.BASE_WIDTH:
                attr = new MarginLeftAttr(val, Attrs.MARGIN_LEFT, 0);
                break;
            case AutoAttr.BASE_HEIGHT:
                attr = new MarginLeftAttr(val, 0, Attrs.MARGIN_LEFT);
                break;
            case AutoAttr.BASE_DEFAULT:
                attr = new MarginLeftAttr(val, 0, 0);
                break;
        }
        return attr;
    }

    @Override
    protected int attrVal() {
        return Attrs.MARGIN_LEFT;
    }

    @Override
    protected boolean defaultBaseWidth() {
        return true;
    }

    @Override
    protected void execute(View view, int val) {
        if (!(view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams)) {
            return;
        }
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        lp.leftMargin = val;
    }
}
