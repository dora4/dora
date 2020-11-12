package dora.skin;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

public class ResourceManager {

    private static final String DEF_TYPE_DRAWABLE = "drawable";
    private static final String DEF_TYPE_COLOR = "color";
    private Resources mResources;
    private String mPluginPackageName;
    private String mSuffix;

    public ResourceManager(Resources res, String pluginPackageName, String suffix) {
        mResources = res;
        mPluginPackageName = pluginPackageName;
        if (suffix == null) {
            suffix = "";
        }
        mSuffix = suffix;
    }

    public String getPluginPackageName() {
        return mPluginPackageName;
    }

    public String getSuffix() {
        return mSuffix;
    }

    public Resources getResources() {
        return mResources;
    }

    public Drawable getDrawableByName(String name) {
        try {
            name = appendSuffix(name);
            return mResources.getDrawable(mResources.getIdentifier(name, DEF_TYPE_DRAWABLE, mPluginPackageName));
        } catch (Resources.NotFoundException e) {
            try {
                return mResources.getDrawable(mResources.getIdentifier(name, DEF_TYPE_COLOR, mPluginPackageName));
            } catch (Resources.NotFoundException e2) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public int getColor(String name) {
        try {
            name = appendSuffix(name);
            return mResources.getColor(mResources.getIdentifier(name, DEF_TYPE_COLOR, mPluginPackageName));
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public ColorStateList getColorStateList(String name) {
        try {
            name = appendSuffix(name);
            return mResources.getColorStateList(mResources.getIdentifier(name, DEF_TYPE_COLOR, mPluginPackageName));
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 追加皮肤后缀。
     */
    private String appendSuffix(String name) {
        if (!TextUtils.isEmpty(mSuffix)) {
            name += "_" + mSuffix;
        }
        return name;
    }
}
