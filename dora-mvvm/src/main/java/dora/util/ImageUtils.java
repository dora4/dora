package dora.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Image processing related tools.
 * 简体中文： 图像处理相关工具。
 */
public final class ImageUtils {

    private ImageUtils() {
    }

    // <editor-folder desc="Pixel unit conversion">

    public static float dp2px(float dpVal) {
        return dp2px(GlobalContext.get(), dpVal);
    }

    public static float dp2px(Context context, float dpVal) {
        return DensityUtils.dp2px(context, dpVal);
    }

    public static float sp2px(float spVal) {
        return sp2px(GlobalContext.get(), spVal);
    }

    public static float sp2px(Context context, float spVal) {
        return DensityUtils.sp2px(context, spVal);
    }

    public static float px2dp(float pxVal) {
        return px2dp(GlobalContext.get(), pxVal);
    }

    public static float px2dp(Context context, float pxVal) {
        return DensityUtils.px2dp(context, pxVal);
    }

    public static float px2sp(float pxVal) {
        return px2sp(GlobalContext.get(), pxVal);
    }

    public static float px2sp(Context context, float pxVal) {
        return DensityUtils.px2sp(context, pxVal);
    }

    // </editor-folder>

    // <editor-folder desc="Bitmap creation and recycling">

    public static Bitmap createBitmap(int resId) {
        return createBitmap(GlobalContext.get(), resId);
    }

    public static Bitmap createBitmap(Context context, int resId) {
        return BitmapFactory.decodeResource(context.getResources(), resId);
    }

    public static Bitmap createBitmap(byte[] bytes) {
        if (bytes != null && bytes.length != 0) {
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } else {
            return null;
        }
    }

    public static Bitmap createBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof NinePatchDrawable) {
            Bitmap bitmap = Bitmap.createBitmap(
                    drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(),
                    drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                            : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            return bitmap;
        } else {
            return null;
        }
    }

    /**
     * Create bitmap.
     * 简体中文：创建位图。
     *
     * @param base64Img Base64-encoded image string
     * @return bitmap
     */
    public static Bitmap createBitmap(String base64Img) {
        if (TextUtils.isEmpty(base64Img)) {
            return null;
        }
        if (base64Img.contains("data:image/jpeg;base64,")) {
            base64Img = base64Img.replace("data:image/jpeg;base64,", "");
        } else if (base64Img.contains("data:image/jpg;base64,")) {
            base64Img = base64Img.replace("data:image/jpg;base64,", "");
        } else if (base64Img.contains("data:image/png;base64,")) {
            base64Img = base64Img.replace("data:image/png;base64,", "");
        }
        byte[] imgBytes = CryptoUtils.base64Decode(base64Img);
        return BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.length);
    }

    public static void recycle(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        System.gc();
    }

    // </editor-folder>

    // <editor-folder desc="Save and load images">

    public static void saveImgToAlbum(File file, String fileName) {
        saveImgToAlbum(GlobalContext.get(), file, fileName);
    }

    public static void saveImgToAlbum(Context context, File file, String fileName) {
        // Inserting the file into the system gallery
        // 简体中文：把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
            // After saving the image, send a broadcast to notify and update the database.
            // 简体中文：保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void saveAsJpeg(Bitmap bitmap, String path, int quality) {
        FileOutputStream fos = null;
        try {
            File file = new File(path);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fos);
            fos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void saveAsJpeg(Bitmap bitmap, String path) {
        saveAsJpeg(bitmap, path, 100);
    }

    public static void saveAsPng(Bitmap bitmap, String path, int quality) {
        FileOutputStream fos = null;
        try {
            File file = new File(path);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, quality, fos);
            fos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void saveAsPng(Bitmap bitmap, String path) {
        saveAsPng(bitmap, path, 100);
    }

    /**
     * Load thumbnail.
     * 简体中文：加载缩略图。
     *
     * @param imagePath Image file path
     * @param width Width to load
     * @param height Height to load
     */
    public static Bitmap loadImageThumbnail(String imagePath, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inJustDecodeBounds = false;
        int outWidth = options.outWidth;
        int outHeight = options.outHeight;
        int w = outWidth / width;
        int h = outHeight / height;
        int inSampleSize = java.lang.Math.min(w, h);
        if (inSampleSize <= 0) {
            inSampleSize = 1;
        }
        options.inSampleSize = inSampleSize;
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    public static Bitmap loadAssetBitmap(String assetPath) {
        return loadAssetBitmap(GlobalContext.get(), assetPath);
    }

    public static Bitmap loadAssetBitmap(Context context, String assetPath) {
        InputStream is = null;
        try {
            Resources resources = context.getResources();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inDensity = DisplayMetrics.DENSITY_HIGH;
            options.inScreenDensity = resources.getDisplayMetrics().densityDpi;
            options.inTargetDensity = resources.getDisplayMetrics().densityDpi;
            is = context.getAssets().open(assetPath);
            return BitmapFactory.decodeStream(is, new Rect(), options);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    // </editor-folder>

    // <editor-folder desc="Image processing or transformation">

    public static Bitmap scaleBitmap(Bitmap bitmap, int requiredWidth, int requiredHeight) {
        return Bitmap.createScaledBitmap(bitmap, requiredWidth, requiredHeight, true);
    }

    public static Bitmap zoomBitmap(Bitmap bitmap, int requiredWidth, int requiredHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaleWidth = ((float) requiredWidth) / width;
        float scaleHeight = ((float) requiredHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    public static Bitmap rotateBitmap(int angle, Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        Bitmap outputBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
        return outputBitmap;
    }

    /**
     * Create reflection bitmap.
     * 简体中文：制作倒影位图。
     */
    public static Bitmap makeReflectionBitmap(Bitmap bitmap) {
        int reflectionGap = 4;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);
        Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, height / 2, width, height / 2, matrix,
                false);
        Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (height + height / 2),
                Bitmap.Config.ARGB_8888);
        Paint defaultPaint = new Paint();
        Canvas canvas = new Canvas(bitmapWithReflection);
        canvas.drawBitmap(bitmap, 0, 0, null);
        canvas.drawRect(0, height, width, height + reflectionGap, defaultPaint);
        canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);
        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,
                bitmapWithReflection.getHeight() + reflectionGap, 0x70FFFFFF, 0x00FFFFFF,
                Shader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawRect(0, height, width, bitmapWithReflection.getHeight() + reflectionGap, paint);
        return bitmapWithReflection;
    }

    /**
     * Create black and white bitmap.
     * 简体中文：制作黑白位图。
     */
    public static Bitmap makeBlackBitmap(Bitmap bitmap) {
        Bitmap outputBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
                Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(outputBitmap);
        Paint paint = new Paint();
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(matrix);
        paint.setColorFilter(f);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return outputBitmap;
    }

    /**
     * Crop bitmap into a circle.
     * 简体中文：将位图裁剪成圆形。
     */
    public static Bitmap makeRoundBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left = 0, top = 0, right, bottom, dstLeft = 0, dstTop = 0, dstRight, dstBottom;
        if (width <= height) {
            roundPx = width / 2;
            right = width;
            bottom = width;
            height = width;
            dstRight = width;
            dstBottom = width;
        } else {
            roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            bottom = height;
            width = height;
            dstRight = height;
            dstBottom = height;
        }
        Bitmap outputBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(outputBitmap);
        Paint paint = new Paint();
        Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
        Rect dst = new Rect((int) dstLeft, (int) dstTop, (int) dstRight, (int) dstBottom);
        RectF rectF = new RectF(dst);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);
        return outputBitmap;
    }

    /**
     * Crop bitmap into a rounded rectangle.
     * 简体中文：将位图裁剪成圆角矩形。
     */
    public static Bitmap makeRoundCornerBitmap(Bitmap bitmap, int pixels, int bgColor) {
        Bitmap outputBitmap = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(outputBitmap);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);
        float roundPx = pixels;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(bgColor);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return outputBitmap;
    }

    // </editor-folder>

    private static int calculateInSampleSize(BitmapFactory.Options options, int requiredWidth,
                                             int requiredHeight) {
        int width = options.outWidth;
        int height = options.outHeight;
        int inSampleSize = 1;
        if (height > requiredHeight || width > requiredWidth) {
            int widthRatio = java.lang.Math.round((float) width / (float) requiredWidth);
            int heightRatio = java.lang.Math.round((float) height / (float) requiredHeight);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmap(Resources res, int resId, int requiredWidth,
                                             int requiredHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        options.inSampleSize = calculateInSampleSize(options, requiredWidth,
                requiredHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    /**
     * Screen capture.
     * 简体中文：屏幕截图。
     */
    public static Bitmap takeScreenShot(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        Rect rect = new Rect();
        view.getWindowVisibleDisplayFrame(rect);
        int statusBarHeight = rect.top;
        Bitmap outputBitmap = Bitmap.createBitmap(bitmap, 0, statusBarHeight,
                ScreenUtils.getScreenWidth(activity), ScreenUtils.getScreenHeight(activity) - statusBarHeight);
        view.destroyDrawingCache();
        return outputBitmap;
    }

    public static int getPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static void saveToPhotoAlbum(Context activity, File file) {
        String imageName = IoUtils.getFileNameFromPath(file.getAbsolutePath());
        try {
            MediaStore.Images.Media.insertImage(activity.getContentResolver(),
                    file.getAbsolutePath(), imageName, null);
            ToastUtils.showShort("Save into photo album successful");
        } catch (FileNotFoundException e) {
            ToastUtils.showShort("Failed to save into photo album");
        }
        activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.fromFile(file)));
    }
}
