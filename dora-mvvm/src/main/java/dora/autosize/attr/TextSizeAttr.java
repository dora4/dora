package dora.autosize.attr;

import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

public class TextSizeAttr extends AutoAttr {

    public TextSizeAttr(int pxVal, int baseWidth, int baseHeight) {
        super(pxVal, baseWidth, baseHeight);
    }

    public static TextSizeAttr generate(int val, int baseFlag) {
        TextSizeAttr attr = null;
        switch (baseFlag) {
            case AutoAttr.BASE_WIDTH:
                attr = new TextSizeAttr(val, Attrs.TEXT_SIZE, 0);
                break;
            case AutoAttr.BASE_HEIGHT:
                attr = new TextSizeAttr(val, 0, Attrs.TEXT_SIZE);
                break;
            case AutoAttr.BASE_DEFAULT:
                attr = new TextSizeAttr(val, 0, 0);
                break;
        }
        return attr;
    }

    @Override
    protected int attrVal() {
        return Attrs.TEXT_SIZE;
    }

    @Override
    protected boolean defaultBaseWidth() {
        return false;
    }

    @Override
    protected void execute(View view, int val) {
        if (!(view instanceof TextView))
            return;
        ((TextView) view).setIncludeFontPadding(false);
        ((TextView) view).setTextSize(TypedValue.COMPLEX_UNIT_PX, val);
    }


}
