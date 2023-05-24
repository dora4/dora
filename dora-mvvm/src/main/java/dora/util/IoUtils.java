package dora.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;

import androidx.annotation.WorkerThread;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

/**
 * 文件操作相关工具。
 */
public final class IoUtils {

    private IoUtils() {
    }

    /**
     * 检测SD卡（内置）是否准备就绪。
     */
    public static boolean checkMediaMounted() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 读取手机文件系统根目录。
     */
    public static String getSdRoot() {
        if (checkMediaMounted()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        return "";
    }

    // <editor-folder desc="文件（夹）创建、复制、删除、移动、重命名">

    public static void createFolder(String[] dirs) {
        if (dirs != null) {
            for (String dir : dirs) {
                createFolder(dir);
            }
        }
    }

    public static void recreateFolder(String[] dirs) {
        for (String dir : dirs) {
            delete(new File(dir));
        }
        createFolder(dirs);
    }

    public static void createFolder(String dir) {
        if (TextUtils.isNotEmpty(dir)) {
            File folder = new File(dir);
            if (!folder.exists()) {
                folder.mkdirs();
            }
        }
    }

    public static void recreateFolder(String dir) {
        delete(new File(dir));
        createFolder(dir);
    }

    private static boolean copyFile(File file, String target) {
        File targetFile = new File(target);
        if (!targetFile.exists() || !file.isFile() || !targetFile.isDirectory()) {
            return false;
        }
        try {
            InputStream inStream = new FileInputStream(file);
            OutputStream outStream = new BufferedOutputStream(
                    new FileOutputStream(new File(targetFile, file.getName())));
            int len;
            byte[] buf = new byte[1024];
            while ((len = inStream.read(buf)) != -1) {
                outStream.write(buf, 0, len);
            }
            outStream.flush();
            close(outStream);
            close(inStream);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static boolean copyFolder(File file, String target) {
        File targetFile = new File(target);
        if (!targetFile.exists() || !file.isDirectory() || !targetFile.isDirectory()) {
            return false;
        }
        if (file.list() != null) {
            String[] children = file.list();
            for (int i = 0; i < children.length; i++) {
                File newDirFile = new File(target + File.separator + file.getName());
                if (!newDirFile.exists()) {
                    newDirFile.mkdir();
                }
                copy(new File(file, children[i]), target + File.separator + file.getName());
            }
        }
        return true;
    }

    public static boolean copy(File file, String target) {
        File targetFile = new File(target);
        if (!targetFile.exists() || !targetFile.isDirectory()) {
            return false;
        }
        if (file.isFile()) {
            return copyFile(file, target);
        } else {
            return copyFolder(file, target);
        }
    }

    private static boolean deleteFile(File file) {
        return file.isFile() && file.delete();
    }

    private static boolean deleteFolder(File file) {
        if (!file.isDirectory()) {
            return false;
        } else {
            if (file.list() != null) {
                String[] children = file.list();
                for (int i = 0; i < children.length; i++) {
                    delete(new File(file, children[i]));
                }
            }
            return file.delete();
        }
    }

    public static boolean delete(File file) {
        if (!file.exists()) {
            return false;
        }
        if (file.isFile()) {
            return deleteFile(file);
        } else {
            return deleteFolder(file);
        }
    }

    private static boolean moveFile(File file, String target) {
        File targetFile = new File(target);
        if (!file.isFile() || !targetFile.exists() || !targetFile.isDirectory()) {
            return false;
        } else {
            copyFile(file, target);
            deleteFile(file);
            return true;
        }
    }

    private static boolean moveFolder(File file, String target) {
        File targetFile = new File(target);
        return !(!file.isDirectory()) || !targetFile.exists() || !targetFile.isDirectory()
                && copyFolder(file, target) && deleteFolder(file);
    }

    public static boolean move(File file, String target) {
        File targetFile = new File(target);
        if (!targetFile.exists() || !targetFile.isDirectory()) {
            return false;
        }
        if (file.isFile()) {
            return moveFile(file, target);
        } else {
            return copyFolder(file, target) && delete(file);
        }
    }

    private static boolean renameFile(File file, String name) {
        if (!file.isFile()) {
            return false;
        } else {
            String parent = file.getParent();
            return file.renameTo(new File(parent, name));
        }
    }

    private static boolean renameFolder(File file, String name) {
        if (!file.isDirectory()) {
            return false;
        } else {
            String parent = file.getParent();
            return file.renameTo(new File(parent, name));
        }
    }

    public static boolean rename(File file, String name) {
        if (file.isFile()) {
            return renameFile(file, name);
        } else {
            return renameFolder(file, name);
        }
    }

    // </editor-folder>

    // <editor-folder desc="文件读写">

    public static byte[] read(File file) throws IOException {
        byte[] buffers = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FileInputStream is = new FileInputStream(file);
        int len;
        while ((len = is.read(buffers)) > 0) {
            baos.write(buffers, 0, len);
        }
        byte[] fileBytes = baos.toByteArray();
        baos.close();
        is.close();
        return fileBytes;
    }

    public static byte[] readAssets(Context context, String fileName) throws IOException {
        byte[] bytes = new byte[1024];
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = assetManager.open(fileName);
        inputStream.read(bytes);
        close(inputStream);
        return bytes;
    }

    public static String readAssetsText(Context context, String fileName) throws IOException {
        StringBuilder sb = new StringBuilder();
        AssetManager assetManager = context.getAssets();
        BufferedReader bf = new BufferedReader(new InputStreamReader(assetManager.open(fileName)));
        String line;
        while ((line = bf.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

    public static List<String> readTextLines(File file) {
        return readTextLines(file, false, "");
    }

    public static List<String> readM3U8Urls(File m3u8File) {
        return readTextLines(m3u8File, true, "#");
    }

    /**
     * 读取本地文本文件的数据。
     *
     * @param file 本地的文本文件
     * @param hasIgnoreLines 如果有要忽略的行，则设置为true，如果为true，请设置ignoreLineChars
     * @param ignoreLineChars 如果文件的一行以该字符串开始，则跳过该行的读取，如m3u文件#为Metadata信息，不是有效
     *                        的url地址，你应该忽略以"#"开头的行
     * @return
     */
    public static List<String> readTextLines(File file, boolean hasIgnoreLines, String ignoreLineChars) {
        List<String> lines = new ArrayList<>();
        try {
            FileInputStream inputStream = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while((line = reader.readLine()) != null){
                if (hasIgnoreLines && line.startsWith(ignoreLineChars)){
                } else if(line.length() > 0) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    public static List<String> readTextLines(String filePath) {
        return readTextLines(filePath, false, "");
    }

    public static List<String> readM3U8Urls(String m3u8FilePath) {
        return readTextLines(m3u8FilePath, true, "#");
    }

    public static List<String> readTextLines(String filePath, boolean hasIgnoreLines, String ignoreLineChars)  {
        return readTextLines(new File(filePath), hasIgnoreLines, ignoreLineChars);
    }

    public static String readText(File file) {
        try {
            FileInputStream is = new FileInputStream(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024 * 4];
            int len;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            byte[] data = baos.toByteArray();
            return new String(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String readText(String filePath)  {
        return readText(new File(filePath));
    }

    public static void write(Context context, String fileName, byte[] bytes, int modeType) throws FileNotFoundException {
        FileOutputStream outStream = context.openFileOutput(fileName, modeType);
        close(outStream);
    }

    public static void write(byte[] bytes, String filePath) throws IOException {
        FileOutputStream fos = null;
        try {
            File file = new File(filePath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            fos = new FileOutputStream(file);
            fos.write(bytes);
            fos.flush();
        } finally {
            close(fos);
        }
    }

    public static File write(InputStream inputStream, String filePath) {
        OutputStream outputStream = null;
        File mFile = new File(filePath);
        if (!mFile.getParentFile().exists()) {
            mFile.getParentFile().mkdirs();
        }
        try {
            outputStream = new FileOutputStream(mFile);
            byte buffer[] = new byte[4 * 1024];
            int lenght = 0;
            while ((lenght = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, lenght);
            }
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(inputStream);
            close(outputStream);
        }
        return mFile;
    }

    // </editor-folder>

    // <editor-folder desc="文件（夹）大小">

    public static String getRomTotalSize(Context context) {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return Formatter.formatFileSize(context, blockSize * totalBlocks);
    }

    public static String getRomAvailableSize(Context context) {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return Formatter.formatFileSize(context, blockSize * availableBlocks);
    }

    public static String formatFileSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1 && size > 0) {
            return size + "B";
        } else if (size <= 0) {
            return "0B";
        }
        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }
        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }
        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }

    public static String getFileSize(Context context, File file) {
        FileChannel fc = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            fc = fis.getChannel();
            long size = fc.size();
            return Formatter.formatFileSize(context, size);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(fc);
            close(fis);
        }
        return "";
    }

    public static long getFolderTotalSize(File file) {
        long size = 0;
        try {
            File[] files = file.listFiles();
            int subCount;
            if (files != null) {
                subCount = files.length;
                for (int i = 0; i < subCount; i++) {
                    if (files[i].isDirectory()) {
                        size = size + getFolderTotalSize(files[i]);
                    } else {
                        size = size + files[i].length();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    // </editor-folder>

    // <editor-folder desc="文件下载">

    @WorkerThread
    public static InputStream getNetworkStream(String url) {
        try {
            URL wurl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) wurl.openConnection();
            conn.connect();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return conn.getInputStream();
            } else {
                LogUtils.e(conn.getResponseMessage());
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 读取网络文本文件的数据，需在子线程执行。
     *
     * @param url 请求的文本文件url地址
     * @return 文本文件的每一行的字符串
     */
    @WorkerThread
    public static List<String> getTextFileLines(String url) {
        return getTextFileLines(url, false, "");
    }

    /**
     * 读取网络文本文件的数据，需要自行在子线程执行。
     *
     * @param url 请求的文本文件url地址
     * @param hasIgnoreLines 如果有要忽略的行，则设置为true，如果为true，请设置ignoreLineChars
     * @param ignoreLineChars 如果文件的一行以该字符串开始，则跳过该行的读取，如m3u文件#为Metadata信息，不是有效
     *                        的url地址，你应该忽略以"#"开头的行
     * @return 文本文件的每一行的字符串
     */
    @WorkerThread
    public static List<String> getTextFileLines(String url, boolean hasIgnoreLines, String ignoreLineChars) {
        List<String> lines = new ArrayList<>();
        InputStream inputStream = getNetworkStream(url);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while((line = reader.readLine()) != null){
                if (hasIgnoreLines && line.startsWith(ignoreLineChars)){
                } else if(line.length() > 0) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    /**
     * 文件下载。
     *
     * @param url 下载的文件地址
     * @param savePath 保存的文件路径
     * @return 返回保存后的文件路径
     */
    public static File download(String url, String savePath) {
        try {
            return Executors.newSingleThreadExecutor().submit(() -> downloadInBackground(url, savePath)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 文件下载，需要自行在子线程执行。
     *
     * @param url 下载的文件地址
     * @param savePath 保存的文件路径
     * @return 返回保存后的文件路径
     */
    @WorkerThread
    public static File downloadInBackground(String url, String savePath) {
        InputStream inputStream = getNetworkStream(url);
        File file = write(inputStream, savePath);
        close(inputStream);
        return file;
    }

    /**
     * 将文件下载到指定文件夹，需要自行在子线程执行。
     *
     * @param url 下载的文件地址
     * @param folder 保存到的文件夹
     * @return 返回保存后的文件路径
     */
    @WorkerThread
    public static File downloadFileToFolder(String url, String folder) {
        try {
            return Executors.newSingleThreadExecutor().submit(() -> downloadFileToFolderInBackground(url, folder)).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将文件下载到指定文件夹，需要自行在子线程执行。
     *
     * @param url 下载的文件地址
     * @param folder 保存到的文件夹
     * @return 返回保存后的文件路径
     */
    @WorkerThread
    public static File downloadFileToFolderInBackground(String url, String folder) {
        InputStream inputStream = getNetworkStream(url);
        File file = write(inputStream, folder + "/" + getFileNameFromPath(url));
        close(inputStream);
        return file;
    }

    /**
     * 批量下载文件。
     *
     * @param urls 要下载的所有文件的url
     * @param folder 保存到的文件夹
     */
    public static void batchDownloadFileToFolder(String[] urls, String folder) {
        Executors.newCachedThreadPool().submit(() -> {
            for (String url : urls) {
                downloadFileToFolder(url, folder);
            }
        });
    }

    // </editor-folder>

    // <editor-folder desc="路径处理">

    public static String getParentPath(String path) {
        if (path.endsWith(File.separator)) {
            path = path.substring(0, path.length() - 1);
        }
        int start = path.lastIndexOf(File.separator);
        return path.substring(0, start);
    }

    /**
     * 获取路径中的文件名，带文件名后缀。
     *
     * @param path 文件路径
     * @return 文件名，带后缀
     */
    public static String getFileNameFromPath(String path) {
        return getFileNameFromPath(path, true);
    }

    /**
     * 从文件路径中提取文件名，不带文件名后缀。
     *
     * @param path 文件路径
     * @return 文件名，不带后缀
     */
    public static String getNameFromPath(String path) {
        return getFileNameFromPath(path, false);
    }

    /**
     * 从文件路径中提取文件名，可指定是否需要文件名后缀。
     *
     * @param path 文件路径
     * @param withSuffix 是否需要文件名后缀
     * @return 文件名
     */
    private static String getFileNameFromPath(String path, boolean withSuffix) {
        int start = path.lastIndexOf(File.separator) + 1;
        int end = path.lastIndexOf(".");
        if (withSuffix) {
            return path.substring(start);
        } else {
            return path.substring(start, end);
        }
    }

    // </editor-folder>

    /**
     * 将对象转换为字节数组。
     *
     * @param obj 要转化的对象
     * @return 字节数组
     */
    public static byte[] bytes(Object obj) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

    /**
     * 获取文件夹的子目录的数量，包括文件和文件夹，如果本身为文件，则返回-1。
     *
     * @param file 文件夹
     * @return 文件夹的子目录数
     */
    public static int getSubCount(File file) {
        if (file != null) {
            if (!file.isDirectory()) {
                return -1;
            } else {
                return file.list().length;
            }
        }
        throw new NullPointerException("File can\'t be null.");
    }

    /**
     * 读取文件的MD5值，用于比对两个文件是否是同一个文件。
     *
     * @param file 要读取的文件对象
     * @return 文件的MD5值
     */
    public static String getMD5(File file) {
        FileInputStream fis = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            fis = new FileInputStream(file);
            byte[] buffer = new byte[2048];
            int length;
            while ((length = fis.read(buffer)) != -1) {
                md.update(buffer, 0, length);
            }
            byte[] bytes = md.digest();
            return MathUtils.bs2H(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            close(fis);
        }
    }

    /**
     * 关闭流，自动忽略异常处理。
     *
     * @param closeableList 可关闭的流的列表
     */
    public static void close(Closeable... closeableList) {
        try {
            for (Closeable closeable : closeableList) {
                if (closeable != null) {
                    closeable.close();
                }
            }
        } catch (IOException ignore) {
        }
    }
}
