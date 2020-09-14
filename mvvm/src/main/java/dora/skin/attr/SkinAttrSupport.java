package dora.skin.attr;

import android.content.Context;
import android.util.AttributeSet;

import dora.skin.constant.SkinConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * 皮肤属性工具类。
 */
public class SkinAttrSupport {

    /**
     * 从xml的属性集合中获取皮肤相关的属性。
     */
    public static List<SkinAttr> getSkinAttrs(AttributeSet attrs, Context context) {
        List<SkinAttr> skinAttrs = new ArrayList<>();
        SkinAttr skinAttr;
        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            String attrName = attrs.getAttributeName(i);
            String attrValue = attrs.getAttributeValue(i);
            SkinAttrType attrType = getSupprotAttrType(attrName);
            if (attrType == null) {
                continue;
            }
            if (attrValue.startsWith("@")) {
                int id = Integer.parseInt(attrValue.substring(1));
                // 获取资源id的实体名称
                String entryName = context.getResources().getResourceEntryName(id);
                if (entryName.startsWith(SkinConfig.ATTR_PREFIX)) {
                    skinAttr = new SkinAttr(attrType, entryName);
                    skinAttrs.add(skinAttr);
                }
            }
        }
        return skinAttrs;

    }

    private static SkinAttrType getSupprotAttrType(String attrName) {
        for (SkinAttrType attrType : SkinAttrType.values()) {
            if (attrType.getAttrType().equals(attrName)) {
                return attrType;
            }
        }
        return null;
    }
}
