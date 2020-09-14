package dora.skin.attr;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import dora.skin.ResourceManager;
import dora.skin.SkinManager;

public enum SkinAttrType {

    /**
     * 背景属性。
     */
    BACKGROUND("background") {
        @Override
        public void apply(View view, String resName) {
            Drawable drawable = getResourceManager().getDrawableByName(resName);
            if (drawable == null) {
                return;
            }
            view.setBackgroundDrawable(drawable);
        }
    },
    /**
     * 字体颜色。
     */
    COLOR("textColor") {
        @Override
        public void apply(View view, String resName) {
            ColorStateList colorlist = getResourceManager().getColorStateList(resName);
            if (colorlist == null) {
                return;
            }
            ((TextView) view).setTextColor(colorlist);
        }
    },
    /**
     * 图片资源。
     */
    SRC("src") {
        @Override
        public void apply(View view, String resName) {
            if (view instanceof ImageView) {
                Drawable drawable = getResourceManager().getDrawableByName(resName);
                if (drawable == null) {
                    return;
                }
                ((ImageView) view).setImageDrawable(drawable);
            }

        }
    };

    String attrType;

    SkinAttrType(String attrType) {
        this.attrType = attrType;
    }

    public String getAttrType() {
        return attrType;
    }

    public abstract void apply(View view, String resName);

    /**
     * 获取资源管理器。
     */
    public ResourceManager getResourceManager() {
        return SkinManager.getInstance().getResourceManager();
    }
}
