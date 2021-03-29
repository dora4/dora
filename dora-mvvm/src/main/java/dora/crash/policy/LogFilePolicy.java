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
import android.util.Log;
import dora.crash.LogInfo;
import dora.crash.group.DefaultGroup;
import dora.crash.group.Group;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Write log information to the SD card of the mobile.
 * 向手机SD卡写入日志信息。
 */
public class LogFilePolicy extends LogReportPolicy {

    private String mFolderName = "android-dora"; //手机系统根目录保存日志文件夹的名称

    public LogFilePolicy() {
        this(new DefaultGroup());
    }

    public LogFilePolicy(String folderName) {
        this(folderName, new DefaultGroup());
    }

    public LogFilePolicy(Group group) {
        super(group, null);
    }

    public LogFilePolicy(LogReportPolicy policy) {
        this(new DefaultGroup(), policy);
    }

    public LogFilePolicy(String folderName, Group group) {
        super(group, null);
        this.mFolderName = folderName;
    }

    public LogFilePolicy(String folderName, LogReportPolicy policy) {
        this(folderName, new DefaultGroup(), policy);
    }

    public LogFilePolicy(Group group, LogReportPolicy policy) {
        super(group, policy);
    }

    public LogFilePolicy(String folderName, Group group, LogReportPolicy policy) {
        super(group, policy);
        this.mFolderName = folderName;
    }

    @Override
    public void report(LogInfo info, Group group) {
        super.report(info, group);
        try {
            if (group.counts()) {
                if (info.getContent() == null || info.getContent().equals("")) {
                    return;
                }
                String path = Environment.getExternalStorageDirectory().getAbsolutePath();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
                String time = simpleDateFormat.format(new Date());
                File folder = new File(path, mFolderName);
                folder.mkdirs();
                File file = new File(folder.getAbsolutePath(), "log" + time + ".txt");
                if (!file.exists()) {
                    file.createNewFile();
                }
                byte[] buffer = info.getContent().trim().getBytes();
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(buffer, 0, buffer.length);
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        } catch (IOException e) {
            Log.e("dora", "日志信息存储失败");
        }
    }
}