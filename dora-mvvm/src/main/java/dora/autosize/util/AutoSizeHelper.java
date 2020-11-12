package dora.autosize.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import dora.R;
import dora.autosize.AutoLayoutInfo;
import dora.autosize.attr.Attrs;
import dora.autosize.attr.AutoAttr;
import dora.autosize.attr.HeightAttr;
import dora.autosize.attr.MarginAttr;
import dora.autosize.attr.MarginBottomAttr;
import dora.autosize.attr.MarginLeftAttr;
import dora.autosize.attr.MarginRightAttr;
import dora.autosize.attr.MarginTopAttr;
import dora.autosize.attr.MaxHeightAttr;
import dora.autosize.attr.MaxWidthAttr;
import dora.autosize.attr.MinHeightAttr;
import dora.autosize.attr.MinWidthAttr;
import dora.autosize.attr.PaddingAttr;
import dora.autosize.attr.PaddingBottomAttr;
import dora.autosize.attr.PaddingLeftAttr;
import dora.autosize.attr.PaddingRightAttr;
import dora.autosize.attr.PaddingTopAttr;
import dora.autosize.attr.TextSizeAttr;
import dora.autosize.attr.WidthAttr;
import dora.autosize.config.AutoSizeConfig;
import dora.log.Logger;

public class AutoSizeHelper {

    private static final int[] LL = new int[]
            { //
                    android.R.attr.textSize,
                    android.R.attr.padding,//
                    android.R.attr.paddingLeft,//
                    android.R.attr.paddingTop,//
                    android.R.attr.paddingRight,//
                    android.R.attr.paddingBottom,//
                    android.R.attr.layout_width,//
                    android.R.attr.layout_height,//
                    android.R.attr.layout_margin,//
                    android.R.attr.layout_marginLeft,//
                    android.R.attr.layout_marginTop,//
                    android.R.attr.layout_marginRight,//
                    android.R.attr.layout_marginBottom,//
                    android.R.attr.maxWidth,//
                    android.R.attr.maxHeight,//
                    android.R.attr.minWidth,//
                    android.R.attr.minHeight,//16843072


            };
    private static final int INDEX_TEXT_SIZE = 0;
    private static final int INDEX_PADDING = 1;
    private static final int INDEX_PADDING_LEFT = 2;
    private static final int INDEX_PADDING_TOP = 3;
    private static final int INDEX_PADDING_RIGHT = 4;
    private static final int INDEX_PADDING_BOTTOM = 5;
    private static final int INDEX_WIDTH = 6;
    private static final int INDEX_HEIGHT = 7;
    private static final int INDEX_MARGIN = 8;
    private static final int INDEX_MARGIN_LEFT = 9;
    private static final int INDEX_MARGIN_TOP = 10;
    private static final int INDEX_MARGIN_RIGHT = 11;
    private static final int INDEX_MARGIN_BOTTOM = 12;
    private static final int INDEX_MAX_WIDTH = 13;
    private static final int INDEX_MAX_HEIGHT = 14;
    private static final int INDEX_MIN_WIDTH = 15;
    private static final int INDEX_MIN_HEIGHT = 16;
    /**
     * move to other place?
     */
    private static AutoSizeConfig mAutoSizeConfig;
    private final ViewGroup mHost;

    public AutoSizeHelper(ViewGroup host) {
        mHost = host;

        if (mAutoSizeConfig == null) {
            initAutoLayoutConfig(host);
        }

    }

    public static AutoLayoutInfo getAutoLayoutInfo(Context context,
                                                   AttributeSet attrs) {

        AutoLayoutInfo info = new AutoLayoutInfo();

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AutoLayout_Layout);
        int baseWidth = a.getInt(R.styleable.AutoLayout_Layout_layout_auto_base_width, 0);
        int baseHeight = a.getInt(R.styleable.AutoLayout_Layout_layout_auto_base_height, 0);
        a.recycle();

        TypedArray array = context.obtainStyledAttributes(attrs, LL);

        int n = array.getIndexCount();


        for (int i = 0; i < n; i++) {
            int index = array.getIndex(i);
//            String val = array.getString(index);
//            if (!isPxVal(val)) continue;

            if (!DimenUtils.isPxVal(array.peekValue(index))) continue;

            int pxVal = 0;
            try {
                pxVal = array.getDimensionPixelOffset(index, 0);
            } catch (Exception ignore)//not dimension
            {
                continue;
            }
            switch (index) {
                case INDEX_TEXT_SIZE:
                    info.addAttr(new TextSizeAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_PADDING:
                    info.addAttr(new PaddingAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_PADDING_LEFT:
                    info.addAttr(new PaddingLeftAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_PADDING_TOP:
                    info.addAttr(new PaddingTopAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_PADDING_RIGHT:
                    info.addAttr(new PaddingRightAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_PADDING_BOTTOM:
                    info.addAttr(new PaddingBottomAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_WIDTH:
                    info.addAttr(new WidthAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_HEIGHT:
                    info.addAttr(new HeightAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_MARGIN:
                    info.addAttr(new MarginAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_MARGIN_LEFT:
                    info.addAttr(new MarginLeftAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_MARGIN_TOP:
                    info.addAttr(new MarginTopAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_MARGIN_RIGHT:
                    info.addAttr(new MarginRightAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_MARGIN_BOTTOM:
                    info.addAttr(new MarginBottomAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_MAX_WIDTH:
                    info.addAttr(new MaxWidthAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_MAX_HEIGHT:
                    info.addAttr(new MaxHeightAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_MIN_WIDTH:
                    info.addAttr(new MinWidthAttr(pxVal, baseWidth, baseHeight));
                    break;
                case INDEX_MIN_HEIGHT:
                    info.addAttr(new MinHeightAttr(pxVal, baseWidth, baseHeight));
                    break;
            }
        }
        array.recycle();
        Logger.e(" getAutoLayoutInfo " + info.toString());
        return info;
    }

    /**
     * 会直接将view的LayoutParams上设置的width，height直接进行百分比处理。
     *
     * @param view
     */
    public static void auto(View view) {
        autoSize(view);
        autoPadding(view);
        autoMargin(view);
        autoTextSize(view, AutoAttr.BASE_DEFAULT);
    }

    /**
     * @param view
     * @param attrs #Attrs.WIDTH|Attrs.HEIGHT
     * @param base  AutoAttr.BASE_WIDTH|AutoAttr.BASE_HEIGHT|AutoAttr.BASE_DEFAULT
     */
    public static void auto(View view, int attrs, int base) {
        AutoLayoutInfo autoLayoutInfo = AutoLayoutInfo.getAttrFromView(view, attrs, base);
        if (autoLayoutInfo != null)
            autoLayoutInfo.fillAttrs(view);
    }

    public static void autoTextSize(View view) {
        auto(view, Attrs.TEXT_SIZE, AutoAttr.BASE_DEFAULT);
    }

    public static void autoTextSize(View view, int base) {
        auto(view, Attrs.TEXT_SIZE, base);
    }

    public static void autoMargin(View view) {
        auto(view, Attrs.MARGIN, AutoAttr.BASE_DEFAULT);
    }

    public static void autoMargin(View view, int base) {
        auto(view, Attrs.MARGIN, base);
    }

    public static void autoPadding(View view) {
        auto(view, Attrs.PADDING, AutoAttr.BASE_DEFAULT);
    }

    public static void autoPadding(View view, int base) {
        auto(view, Attrs.PADDING, base);
    }

    public static void autoSize(View view) {
        auto(view, Attrs.WIDTH | Attrs.HEIGHT, AutoAttr.BASE_DEFAULT);
    }

    public static void autoSize(View view, int base) {
        auto(view, Attrs.WIDTH | Attrs.HEIGHT, base);
    }

    public static boolean autoed(View view) {
        Object tag = view.getTag(R.id.id_tag_autosize_size);
        if (tag != null) return true;
        view.setTag(R.id.id_tag_autosize_size, "Just Identify");
        return false;
    }

    public static float getPercentWidth1px() {
        int screenWidth = AutoSizeConfig.getInstance().getScreenWidth();
        int designWidth = AutoSizeConfig.getInstance().getDesignWidth();
        return 1.0f * screenWidth / designWidth;
    }

    public static float getPercentHeight1px() {
        int screenHeight = AutoSizeConfig.getInstance().getScreenHeight();
        int designHeight = AutoSizeConfig.getInstance().getDesignHeight();
        return 1.0f * screenHeight / designHeight;
    }

    public static int getPercentWidthSize(int val) {
        int screenWidth = AutoSizeConfig.getInstance().getScreenWidth();
        int designWidth = AutoSizeConfig.getInstance().getDesignWidth();
        return (int) (val * 1.0f / designWidth * screenWidth);
    }

    public static int getPercentWidthSizeBigger(int val) {
        int screenWidth = AutoSizeConfig.getInstance().getScreenWidth();
        int designWidth = AutoSizeConfig.getInstance().getDesignWidth();

        int res = val * screenWidth;
        if (res % designWidth == 0) {
            return res / designWidth;
        } else {
            return res / designWidth + 1;
        }

    }

    public static int getPercentHeightSizeBigger(int val) {
        int screenHeight = AutoSizeConfig.getInstance().getScreenHeight();
        int designHeight = AutoSizeConfig.getInstance().getDesignHeight();

        int res = val * screenHeight;
        if (res % designHeight == 0) {
            return res / designHeight;
        } else {
            return res / designHeight + 1;
        }
    }

    public static int getPercentHeightSize(int val) {
        int screenHeight = AutoSizeConfig.getInstance().getScreenHeight();
        int designHeight = AutoSizeConfig.getInstance().getDesignHeight();

        return (int) (val * 1.0f / designHeight * screenHeight);
    }

    private void initAutoLayoutConfig(ViewGroup host) {
        mAutoSizeConfig = AutoSizeConfig.getInstance();
        mAutoSizeConfig.init(host.getContext());
    }

    public void adjustChildren() {
        AutoSizeConfig.getInstance().checkParams();

        for (int i = 0, n = mHost.getChildCount(); i < n; i++) {
            View view = mHost.getChildAt(i);
            ViewGroup.LayoutParams params = view.getLayoutParams();

            if (params instanceof AutoLayoutParams) {
                AutoLayoutInfo info =
                        ((AutoLayoutParams) params).getAutoLayoutInfo();
                if (info != null) {
                    info.fillAttrs(view);
                }
            }
        }

    }

    public interface AutoLayoutParams {
        AutoLayoutInfo getAutoLayoutInfo();
    }
}
