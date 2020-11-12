package dora.autosize.attr;

import android.view.View;
import android.view.ViewGroup;

public class MarginRightAttr extends AutoAttr {
    public MarginRightAttr(int pxVal, int baseWidth, int baseHeight) {
        super(pxVal, baseWidth, baseHeight);
    }

    public static MarginRightAttr generate(int val, int baseFlag) {
        MarginRightAttr attr = null;
        switch (baseFlag) {
            case AutoAttr.BASE_WIDTH:
                attr = new MarginRightAttr(val, Attrs.MARGIN_RIGHT, 0);
                break;
            case AutoAttr.BASE_HEIGHT:
                attr = new MarginRightAttr(val, 0, Attrs.MARGIN_RIGHT);
                break;
            case AutoAttr.BASE_DEFAULT:
                attr = new MarginRightAttr(val, 0, 0);
                break;
        }
        return attr;
    }

    @Override
    protected int attrVal() {
        return Attrs.MARGIN_RIGHT;
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
        lp.rightMargin = val;
    }
}
