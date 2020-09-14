package dora.skin.attr;

import android.view.View;

/**
 * 皮肤属性。
 */
public class SkinAttr {

    /**
     * 资源名。
     */
    String resName;

    /**
     * 属性类型。
     */
    SkinAttrType attrType;

    public SkinAttr(SkinAttrType attrType, String resName) {
        this.resName = resName;
        this.attrType = attrType;
    }

    /**
     * 把皮肤的属性应用到View上。
     */
    public void apply(View view) {
        attrType.apply(view, resName);
    }
}
