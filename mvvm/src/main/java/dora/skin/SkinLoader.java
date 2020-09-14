package dora.skin;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

public class SkinLoader implements ISkinLoader {

    private static final String DEF_TYPE_DRAWABLE = "drawable";
    private static final String DEF_TYPE_COLOR = "color";
    private static SkinLoader sLoader;
    private ResourceManager mResourceManager;
    private SkinManager mSkinManager;
    private Resources mSkinResources;
    private String mCurPluginPkgName;

    private SkinLoader() {
        mSkinManager = SkinManager.getInstance();
        mResourceManager = mSkinManager.getResourceManager();
        mSkinResources = mResourceManager.getResources();
        mCurPluginPkgName = mResourceManager.getPluginPackageName();
    }

    public static SkinLoader getInstance() {
        if (sLoader == null) {
            synchronized (SkinLoader.class) {
                if (sLoader == null) {
                    sLoader = new SkinLoader();
                }
            }
        }
        return sLoader;
    }

    public Resources getSkinResources() {
        return mSkinResources;
    }

    public String combineResName(String resName) {
        String suffix = mSkinManager.getSuffix();
        if (suffix != null && !suffix.equals("")) {
            return resName + "_" + suffix;
        }
        return resName;
    }

    @Override
    public int getDrawableRes(String resName) {
        return mSkinResources.getIdentifier(combineResName(resName), DEF_TYPE_DRAWABLE, mCurPluginPkgName);
    }

    @Override
    public int getColorRes(String resName) {
        return mSkinResources.getIdentifier(combineResName(resName), DEF_TYPE_COLOR, mCurPluginPkgName);
    }

    @Override
    public Drawable getDrawable(String resName) {
        return mResourceManager.getDrawableByName(combineResName(resName));
    }

    @Override
    public Drawable getDrawableWithRect(String resName, Rect rect) {
        Drawable drawable = getDrawable(resName);
        if (drawable != null) {
            drawable.setBounds(rect);
        }
        return drawable;
    }

    @Override
    public Drawable getDrawableWithPixels(String resName, int width, int height) {
        Rect rect = new Rect(0, 0, width, height);
        return getDrawableWithRect(resName, rect);
    }

    @Override
    public Drawable getDrawableWithSize(String resName, int size) {
        return getDrawableWithPixels(resName, size, size);
    }

    @Override
    public Bitmap getBitmap(String resName) {
        return BitmapFactory.decodeResource(mSkinResources, getDrawableRes(combineResName(resName)));
    }

    @Override
    public void setImageDrawable(ImageView imageView, String resName) {
        Drawable drawable = getDrawable(resName);
        if (drawable == null) {
            return;
        }
        imageView.setImageDrawable(drawable);
    }

    @Override
    public void setBackgroundDrawable(View view, String resName) {
        Drawable drawable = getDrawable(resName);
        if (drawable == null) {
            return;
        }
        view.setBackgroundDrawable(drawable);
    }
}

