/*
 * Copyright (C) 2020 The Dora Open Source Project
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

package dora.crash.policy;

import android.os.Environment;
import dora.crash.CrashInfo;
import dora.crash.group.DefaultGroup;
import dora.crash.group.Group;
import dora.util.LogUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Save crash log information to SD card, please apply for storage permission by yourself.
 * 简体中文：把崩溃日志信息保存到SD卡，请自行申请存储权限。
 */
public class StoragePolicy extends CrashReportPolicy {

    private String mFolderName = "android-dora"; //手机系统根目录保存日志文件夹的名称

    public StoragePolicy() {
        this(new DefaultGroup());
    }

    public StoragePolicy(String folderName) {
        this(folderName, new DefaultGroup());
    }

    public StoragePolicy(Group group) {
        super(group, null);
    }

    public StoragePolicy(CrashReportPolicy policy) {
        this(new DefaultGroup(), policy);
    }

    public StoragePolicy(String folderName, Group group) {
        super(group, null);
        this.mFolderName = folderName;
    }

    public StoragePolicy(String folderName, CrashReportPolicy policy) {
        this(folderName, new DefaultGroup(), policy);
    }

    public StoragePolicy(Group group, CrashReportPolicy policy) {
        super(group, policy);
    }

    public StoragePolicy(String folderName, Group group, CrashReportPolicy policy) {
        super(group, policy);
        this.mFolderName = folderName;
    }

    @Override
    public void report(CrashInfo info, Group group) {
        super.report(info, group);
        try {
            if (group.counts()) {
                String path = Environment.getExternalStorageDirectory().getAbsolutePath();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
                String time = simpleDateFormat.format(new Date());
                File folder = new File(path, mFolderName);
                folder.mkdirs();
                File file = new File(folder.getAbsolutePath(), "crash" + time + ".txt");
                if (!file.exists()) {
                    file.createNewFile();
                }
                byte[] buffer = info.toString().trim().getBytes();
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(buffer, 0, buffer.length);
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        } catch (IOException e) {
            LogUtils.e(e.toString());
        }
    }
}