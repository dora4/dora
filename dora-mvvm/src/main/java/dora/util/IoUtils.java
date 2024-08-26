/*
 * Copyright (C) 2023 The Dora Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dora.util;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.format.Formatter;

import androidx.annotation.NonNull;
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
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;

/**
 * Tools for file operations.
 * 简体中文：文件操作相关工具。
 */
public final class IoUtils {

    private IoUtils() {
    }

    /**
     * Detect if the SD card (internal) is ready.
     * 简体中文：检测SD卡（内置）是否准备就绪。
     */
    public static boolean checkMediaMounted() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * Read the root directory of the phone's file system.
     * 简体中文：读取手机文件系统根目录。
     */
    public static String getSdRoot() {
        if (checkMediaMounted()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        return "";
    }

    // <editor-folder desc="File (Folder) Creation, Copying, Deletion, Moving, Renaming">

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

    // <editor-folder desc="File reading and writing">

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

    public static List<String> readAssetsTextLines(Context context, String fileName) throws IOException {
        List<String> lines = new ArrayList<>();
        AssetManager assetManager = context.getAssets();
        BufferedReader bf = new BufferedReader(new InputStreamReader(assetManager.open(fileName)));
        String line;
        while ((line = bf.readLine()) != null) {
            lines.add(line);
        }
        return lines;
    }

    public static List<String> readTextLines(File file) {
        return readTextLines(file, false, "");
    }

    public static List<String> readM3U8Urls(File m3u8File) {
        return readTextLines(m3u8File, true, "#");
    }

    /**
     * Read data from a local text file.
     * 简体中文：读取本地文本文件的数据。
     *
     * @param file            Local text file
     * @param hasIgnoreLines  If there are lines to be ignored, set to true. If it's true, set
     *                        ignoreLineChars.
     *                        简体中文：如果有要忽略的行，则设置为true，如果为true，请设置ignoreLineChars
     * @param ignoreLineChars If a line of the file starts with this string, skip reading that line.
     *                        For example, in an m3u file, '#' is used for metadata information and
     *                        not a valid URL address. You should ignore lines that start with "#".
     *                        简体中文：如果文件的一行以该字符串开始，则跳过该行的读取，如m3u文件#为Metadata
     *                        信息，不是有效的url地址，你应该忽略以"#"开头的行。
     */
    public static List<String> readTextLines(File file, boolean hasIgnoreLines, String ignoreLineChars) {
        List<String> lines = new ArrayList<>();
        try {
            FileInputStream inputStream = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while ((line = reader.readLine()) != null) {
                if (hasIgnoreLines && line.startsWith(ignoreLineChars)) {
                } else if (line.length() > 0) {
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

    public static List<String> readTextLines(String filePath, boolean hasIgnoreLines, String ignoreLineChars) {
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

    public static String readText(String filePath) {
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

    // <editor-folder desc="File (Folder) Size">

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

    public static long getFileSize(File file) {
        FileChannel fc = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            fc = fis.getChannel();
            return fc.size();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(fc);
            close(fis);
        }
        return 0L;
    }

    public static String getFormattedFileSize(Context context, File file) {
        long size = getFileSize(file);
        return Formatter.formatFileSize(context, size);
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

    // <editor-folder desc="File Download">

    @WorkerThread
    public static InputStream getNetworkStream(String url) {
        try {
            URL wurl = new URL(url);
            if (wurl.getProtocol().equals("https")) {
                HttpsURLConnection conn = (HttpsURLConnection) wurl.openConnection();
                conn.connect();
                if (conn.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                    return conn.getInputStream();
                } else {
                    LogUtils.e(conn.getResponseMessage());
                    return null;
                }
            } else {
                HttpURLConnection conn = (HttpURLConnection) wurl.openConnection();
                conn.connect();
                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    return conn.getInputStream();
                } else {
                    LogUtils.e(conn.getResponseMessage());
                    return null;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Read data from a network text file, needs to be executed in a sub-thread.
     * 简体中文：读取网络文本文件的数据，需在子线程执行。
     *
     * @param url URL address of the requested text file.简体中文：请求的文本文件url地址
     * @return String of each line in the text file.简体中文：文本文件的每一行的字符串
     */
    @WorkerThread
    public static List<String> getTextFileLines(String url) {
        return getTextFileLines(url, false, "");
    }

    /**
     * Read data from a network text file, you need to execute it in a separate thread on your own.
     * 简体中文：读取网络文本文件的数据，需要自行在子线程执行。
     *
     * @param url             URL address of the requested text file.简体中文：请求的文本文件url地址
     * @param hasIgnoreLines  If there are lines to be ignored, set to true. If it's true, set
     *                        ignoreLineChars.
     *                        简体中文：如果有要忽略的行，则设置为true，如果为true，请设置ignoreLineChars
     * @param ignoreLineChars If a line of the file starts with this string, skip reading that line.
     *                        For example, in an m3u file, '#' is used for metadata information and
     *                        not a valid URL address. You should ignore lines that start with "#".
     *                        简体中文：如果文件的一行以该字符串开始，则跳过该行的读取，如m3u文件#为Metadata
     *                        信息，不是有效的url地址，你应该忽略以"#"开头的行。
     * @return String of each line in the text file。简体中文：文本文件的每一行的字符串
     */
    @WorkerThread
    public static List<String> getTextFileLines(String url, boolean hasIgnoreLines, String ignoreLineChars) {
        List<String> lines = new ArrayList<>();
        InputStream inputStream = getNetworkStream(url);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while ((line = reader.readLine()) != null) {
                if (hasIgnoreLines && line.startsWith(ignoreLineChars)) {
                } else if (line.length() > 0) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    /**
     * File download.
     * 简体中文：文件下载。
     *
     * @param url      Downloaded file address.简体中文：下载的文件地址
     * @param savePath Saved file path.简体中文：保存的文件路径
     * @return Return the path of the saved file.简体中文：返回保存后的文件路径
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
     * File download, needs to be executed in a separate thread on your own.
     * 简体中文：文件下载，需要自行在子线程执行。
     *
     * @param url      Downloaded file address.简体中文：下载的文件地址
     * @param savePath Saved file path.简体中文：保存的文件路径
     * @return Return the path of the saved file.简体中文：返回保存后的文件路径
     */
    @WorkerThread
    public static File downloadInBackground(String url, String savePath) {
        InputStream inputStream = getNetworkStream(url);
        File file = write(inputStream, savePath);
        close(inputStream);
        return file;
    }

    /**
     * Downloading a file to a specified folder needs to be executed in a separate thread on your own.
     * 简体中文：将文件下载到指定文件夹，需要自行在子线程执行。
     *
     * @param url    Downloaded file address.简体中文：下载的文件地址
     * @param folder Destination folder to save.简体中文：保存到的文件夹
     * @return Return the path of the saved file.简体中文：返回保存后的文件路径
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
     * Downloading a file to a specified folder needs to be executed in a separate thread on your own.
     * 简体中文：将文件下载到指定文件夹，需要自行在子线程执行。
     *
     * @param url    Downloaded file address.简体中文：下载的文件地址
     * @param folder Destination folder to save.简体中文：保存到的文件夹
     * @return Return the path of the saved file.简体中文：返回保存后的文件路径
     */
    @WorkerThread
    public static File downloadFileToFolderInBackground(String url, String folder) {
        InputStream inputStream = getNetworkStream(url);
        File file = write(inputStream, folder + "/" + getFileNameFromPath(url));
        close(inputStream);
        return file;
    }

    /**
     * Batch downloading files.
     * 简体中文：批量下载文件。
     *
     * @param urls   URLs of all the files to be downloaded.简体中文：要下载的所有文件的url
     * @param folder Destination folder to save.简体中文：保存到的文件夹
     */
    public static void batchDownloadFileToFolder(String[] urls, String folder) {
        batchDownloadFileToFolder(Arrays.asList(urls), folder);
    }

    /**
     * Batch downloading files.
     * 简体中文：批量下载文件。
     *
     * @param urls   URLs of all the files to be downloaded.简体中文：要下载的所有文件的url
     * @param folder Destination folder to save.简体中文：保存到的文件夹
     */
    public static void batchDownloadFileToFolder(List<String> urls, String folder) {
        Executors.newCachedThreadPool().submit(() -> {
            for (String url : urls) {
                downloadFileToFolder(url, folder);
            }
        });
    }

    // </editor-folder>

    // <editor-folder desc="Path Handling">

    public static String getParentPath(String path) {
        if (path.endsWith(File.separator)) {
            path = path.substring(0, path.length() - 1);
        }
        int start = path.lastIndexOf(File.separator);
        return path.substring(0, start);
    }

    /**
     * Get the file name from the path, including the file extension.
     * 简体中文：获取路径中的文件名，带文件名后缀。
     *
     * @param path File path.简体中文：文件路径
     * @return File name with extension.简体中文：文件名，带后缀
     */
    public static String getFileNameFromPath(String path) {
        return getFileNameFromPath(path, true);
    }

    /**
     * Extract the file name from the file path, without the file extension.
     * 简体中文：从文件路径中提取文件名，不带文件名后缀。
     *
     * @param path File path.简体中文：文件路径
     * @return File name without extension.简体中文：文件名，不带后缀
     */
    public static String getNameFromPath(String path) {
        return getFileNameFromPath(path, false);
    }

    /**
     * Extract the file name from the file path, with the option to specify whether the file name
     * extension is needed.
     * 简体中文：从文件路径中提取文件名，可指定是否需要文件名后缀。
     *
     * @param path       File path.简体中文：文件路径
     * @param withSuffix Whether the file name extension is needed.简体中文：是否需要文件名后缀
     * @return File name.简体中文：文件名
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
     * Convert an object to a byte array.
     * 简体中文：将对象转换为字节数组。
     *
     * @param obj The object to be converted.简体中文：要转化的对象
     * @return Byte array.简体中文：字节数组
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
     * Check if a file or folder exists.
     * 简体中文：检测一个文件或文件夹是否存在。
     *
     * @param path File or folder directory.简体中文：文件或文件夹目录
     * @return Whether it exists.简体中文：是否存在
     */
    public static boolean checkExists(String path) {
        File file = new File(path);
        return file.exists();
    }

    /**
     * Get the count of subdirectories in a folder, including files and folders. If the item itself
     * is a file, return -1.
     * 简体中文：获取文件夹的子目录的数量，包括文件和文件夹，如果本身为文件，则返回-1。
     *
     * @param file Folder.简体中文：文件夹
     * @return Number of subdirectories in a folder.简体中文：文件夹的子目录数
     */
    public static int getSubCount(File file) {
        if (file != null) {
            if (!file.isDirectory()) {
                return -1;
            } else {
                return file.list().length;
            }
        }
        throw new NullPointerException("File can't be null.");
    }

    /**
     * Read the MD5 value of a file, used to compare whether two files are the same.
     * 简体中文：读取文件的MD5值，用于比对两个文件是否是同一个文件。
     *
     * @param file The file object to be read.简体中文：要读取的文件对象
     * @return MD5 value of the file.简体中文：文件的MD5值
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
     * Close the stream, automatically ignoring exception handling.
     * 简体中文：关闭流，自动忽略异常处理。
     *
     * @param closeableList List of closable streams.简体中文：可关闭的流的列表
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

    public static String getPathFromUri(@NonNull Context context, @NonNull Uri uri) {
        String path = null;
        if (DocumentsContract.isDocumentUri(context, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            String[] splits = docId.split(":");
            String type = null;
            String id = null;
            if (splits.length == 2) {
                type = splits[0];
                id = splits[1];
            }
            if (uri.getAuthority().equals("com.android.externalstorage.documents")) {
                if ("primary".equals(type)) {
                    path = context.getExternalFilesDir(null) + File.separator + id;
                }
            } if (uri.getAuthority().equals("com.android.providers.downloads.documents")) {
                if ("raw".equals(type)) {
                    path = id;
                } else {
                    Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"),
                            Long.parseLong(docId)
                    );
                    path = contentUri.getPath();
                }
            } else if (uri.getAuthority().equals("com.android.providers.media.documents")) {
                Uri externalUri = null;
                switch (type) {
                    case "image":
                        externalUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    case "video":
                        externalUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    case "audio":
                        externalUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    case "document":
                        externalUri = MediaStore.Files.getContentUri("external");
                }
                if (externalUri != null) {
                    String selection = "_id=?";
                    String[] selectionArgs = new String[]{id};
                    path = getMediaPathFromUri(context, externalUri, selection, selectionArgs);
                }
            }
        } else if (ContentResolver.SCHEME_CONTENT.equalsIgnoreCase(uri.getScheme())) {
            path = getMediaPathFromUri(context, uri, null, null);
        } else if (ContentResolver.SCHEME_FILE.equalsIgnoreCase(uri.getScheme())) {
            path = uri.getPath();
        }
        if (path != null && new File(path).exists()) {
            return path;
        } else {
            return null;
        }
    }

    private static String getMediaPathFromUri(@NonNull Context context, @NonNull Uri uri, String selection, String[]selectionArgs){
        String path = uri.getPath();
        String sdPath = context.getExternalFilesDir(null).getAbsolutePath();
        if (path != null && !path.startsWith(sdPath)) {
            int sepIndex = path.indexOf(File.separator, 1);
            if (sepIndex == -1) path = null;
            else {
                path = sdPath + path.substring(sepIndex);
            }
        }
        if (path == null || !new File(path).exists()) {
            ContentResolver resolver = context.getContentResolver();
            String[] projection = new String[]{MediaStore.MediaColumns.DATA};
            Cursor cursor = resolver.query(uri, projection, selection, selectionArgs, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    try {
                        int index = cursor.getColumnIndexOrThrow(projection[0]);
                        if (index != -1) path = cursor.getString(index);
                    } catch (IllegalArgumentException e) {
                        path = null;
                    } finally {
                        cursor.close();
                    }
                }
            }
        }
        return path;
    }
}
