package dora.skin;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.text.TextUtils;

import dora.skin.attr.SkinView;
import dora.skin.callback.ISkinChangedListener;
import dora.skin.callback.ISkinChangingCallback;
import dora.skin.util.PrefsUtils;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 皮肤管理器。
 */
public class SkinManager {

    private Context mContext;
    private Resources mResources;
    private ResourceManager mResourceManager;
    private PrefsUtils mPrefsUtils;
    private boolean mUsePlugin;

    /**
     * 换肤资源后缀。
     */
    private String mSuffix = "";
    private String mCurPluginPath;
    private String mCurPluginPkg;


    private Map<ISkinChangedListener, List<SkinView>> mSkinViewMaps = new HashMap<>();
    private List<ISkinChangedListener> mSkinChangedListeners = new ArrayList<>();

    private SkinManager() {
    }

    private static class SingletonHolder {
        static SkinManager sInstance = new SkinManager();
    }

    public static SkinManager getInstance() {
        return SingletonHolder.sInstance;
    }

    public void init(Context context) {
        mContext = context.getApplicationContext();
        mPrefsUtils = new PrefsUtils(mContext);
        String skinPluginPath = mPrefsUtils.getPluginPath();
        String skinPluginPkgName = mPrefsUtils.getPluginPkgName();
        mSuffix = mPrefsUtils.getSuffix();
        if (TextUtils.isEmpty(skinPluginPath))  //皮肤路径不存在
        {
            return;
        }
        File file = new File(skinPluginPath);
        if (!file.exists()) {
            return;
        }
        try {
            loadPlugin(skinPluginPath, skinPluginPkgName, mSuffix);
            mCurPluginPath = skinPluginPath;
            mCurPluginPkg = skinPluginPkgName;
        } catch (Exception e) {
            mPrefsUtils.clear();
            e.printStackTrace();
        }
    }

    private void loadPlugin(String skinPath, String skinPkgName, String suffix) throws Exception {
        //checkPluginParams(skinPath, skinPkgName);
        AssetManager assetManager = AssetManager.class.newInstance();
        Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
        addAssetPath.invoke(assetManager, skinPath);
        Resources superRes = mContext.getResources();
        mResources = new Resources(assetManager, superRes.getDisplayMetrics(), superRes.getConfiguration());
        mResourceManager = new ResourceManager(mResources, skinPkgName, suffix);
        mUsePlugin = true;
    }

    private boolean checkPluginParams(String skinPath, String skinPkgName) {
        if (TextUtils.isEmpty(skinPath) || TextUtils.isEmpty(skinPkgName)) {
            return false;
        }
        return true;
    }

    private void checkPluginParamsThrow(String skinPath, String skinPkgName) {
        if (!checkPluginParams(skinPath, skinPkgName)) {
            throw new IllegalArgumentException("skinPluginPath or skinPkgName can not be empty ! ");
        }
    }

    public void removeAnySkin() {
        clearPluginInfo();
        notifyChangedListeners();
    }

    public String getSuffix() {
        return mSuffix;
    }

    public boolean needChangeSkin() {
        return mUsePlugin || !TextUtils.isEmpty(mSuffix);
    }

    public ResourceManager getResourceManager() {
        if (!mUsePlugin) {
            mResourceManager = new ResourceManager(mContext.getResources(), mContext.getPackageName(), mSuffix);
        }
        return mResourceManager;
    }

    /**
     * 应用内换肤，传入资源区别的后缀。
     */
    public void changeSkin(String suffix) {
        clearPluginInfo();//clear before
        mSuffix = suffix;
        mPrefsUtils.putPluginSuffix(suffix);
        notifyChangedListeners();
    }

    private void clearPluginInfo() {
        mCurPluginPath = null;
        mCurPluginPkg = null;
        mUsePlugin = false;
        mSuffix = null;
        mPrefsUtils.clear();
    }

    private void updatePluginInfo(String skinPluginPath, String pkgName, String suffix) {
        mPrefsUtils.putPluginPath(skinPluginPath);
        mPrefsUtils.putPluginPkg(pkgName);
        mPrefsUtils.putPluginSuffix(suffix);
        mCurPluginPkg = pkgName;
        mCurPluginPath = skinPluginPath;
        mSuffix = suffix;
    }

    public void changeSkin(final String skinPluginPath, final String pkgName, ISkinChangingCallback callback) {
        changeSkin(skinPluginPath, pkgName, "", callback);
    }

    /**
     * 根据suffix选择插件内某套皮肤，默认为""。
     */
    public void changeSkin(final String skinPluginPath, final String pkgName, final String suffix, ISkinChangingCallback callback) {
        if (callback == null) {
            callback = ISkinChangingCallback.DEFAULT_SKIN_CHANGING_CALLBACK;
        }
        final ISkinChangingCallback skinChangingCallback = callback;
        skinChangingCallback.onStart();
        checkPluginParamsThrow(skinPluginPath, pkgName);
        if (skinPluginPath.equals(mCurPluginPath) && pkgName.equals(mCurPluginPkg)) {
            return;
        }
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    loadPlugin(skinPluginPath, pkgName, suffix);
                } catch (Exception e) {
                    e.printStackTrace();
                    skinChangingCallback.onError(e);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                try {
                    updatePluginInfo(skinPluginPath, pkgName, suffix);
                    notifyChangedListeners();
                    skinChangingCallback.onComplete();
                } catch (Exception e) {
                    e.printStackTrace();
                    skinChangingCallback.onError(e);
                }
            }
        }.execute();
    }

    public void addSkinView(ISkinChangedListener listener, List<SkinView> skinViews) {
        mSkinViewMaps.put(listener, skinViews);
    }

    public List<SkinView> getSkinViews(ISkinChangedListener listener) {
        return mSkinViewMaps.get(listener);
    }

    public void apply(ISkinChangedListener listener) {
        List<SkinView> skinViews = getSkinViews(listener);
        if (skinViews == null) {
            return;
        }
        for (SkinView skinView : skinViews) {
            skinView.apply();
        }
    }

    public void addChangedListener(ISkinChangedListener listener) {
        mSkinChangedListeners.add(listener);
    }

    public void removeChangedListener(ISkinChangedListener listener) {
        mSkinChangedListeners.remove(listener);
        mSkinViewMaps.remove(listener);
    }

    public void notifyChangedListeners() {
        for (ISkinChangedListener listener : mSkinChangedListeners) {
            listener.onSkinChanged();
        }
    }
}
