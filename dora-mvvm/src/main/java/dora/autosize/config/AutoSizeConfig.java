package dora.autosize.config;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import dora.log.Logger;
import dora.util.ScreenUtils;

public class AutoSizeConfig {

    private static final String KEY_DESIGN_WIDTH = "design_width";
    private static final String KEY_DESIGN_HEIGHT = "design_height";
    private static AutoSizeConfig mInstance = new AutoSizeConfig();
    private int mScreenWidth;
    private int mScreenHeight;

    private int mDesignWidth;
    private int mDesignHeight;

    private boolean useDeviceSize;


    private AutoSizeConfig() {
    }

    public static AutoSizeConfig getInstance() {
        return mInstance;
    }

    public void checkParams() {
        if (mDesignHeight <= 0 || mDesignWidth <= 0) {
            throw new RuntimeException(
                    "you must set " + KEY_DESIGN_WIDTH + " and " + KEY_DESIGN_HEIGHT + "  in your manifest file.");
        }
    }

    public AutoSizeConfig useDeviceSize() {
        useDeviceSize = true;
        return this;
    }

    public int getScreenWidth() {
        return mScreenWidth;
    }

    public int getScreenHeight() {
        return mScreenHeight;
    }

    public int getDesignWidth() {
        return mDesignWidth;
    }

    public int getDesignHeight() {
        return mDesignHeight;
    }

    public void init(Context context) {
        getMetaData(context);
        if (useDeviceSize) {
            mScreenWidth = ScreenUtils.getContentWidth(context);
            mScreenHeight = ScreenUtils.getContentHeight(context);
        } else {
            mScreenWidth = ScreenUtils.getScreenWidth(context);
            mScreenHeight = ScreenUtils.getScreenHeight(context);
        }
        Logger.e(" screenWidth =" + mScreenWidth + " ,screenHeight = " + mScreenHeight);
    }

    private void getMetaData(Context context) {
        PackageManager packageManager = context.getPackageManager();
        ApplicationInfo applicationInfo;
        try {
            applicationInfo = packageManager.getApplicationInfo(context
                    .getPackageName(), PackageManager.GET_META_DATA);
            if (applicationInfo != null && applicationInfo.metaData != null) {
                mDesignWidth = (int) applicationInfo.metaData.get(KEY_DESIGN_WIDTH);
                mDesignHeight = (int) applicationInfo.metaData.get(KEY_DESIGN_HEIGHT);
            }
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(
                    "you must set " + KEY_DESIGN_WIDTH + " and " + KEY_DESIGN_HEIGHT + "  in your manifest file.", e);
        }

        Logger.e(" designWidth =" + mDesignWidth + " , designHeight = " + mDesignHeight);
    }
}
