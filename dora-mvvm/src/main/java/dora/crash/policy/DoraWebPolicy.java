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

import android.util.Log;
import dora.crash.CrashInfo;
import dora.crash.group.Group;
import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;

/**
 * Save the crash information to the crash information management system of DORA, and the server
 * is not online yet.
 * 保存崩溃信息到dora的崩溃信息管理系统，暂未上线服务器。
 */
public class DoraWebPolicy extends WebPolicyBase {

    public DoraWebPolicy(String url) {
        super(url);
    }

    public DoraWebPolicy(String url, CrashReportPolicy policy) {
        super(url, policy);
    }

    public DoraWebPolicy(String url, Group group) {
        super(url, group);
    }

    public DoraWebPolicy(String url, Group group, CrashReportPolicy policy) {
        super(url, group, policy);
    }

    @Override
    public void sendCrashInfoToWeb(String url, CrashInfo info, Group group) {
        if (group.counts()) {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("versionName", info.getVersionName());
            params.put("versionCode", String.valueOf(info.getVersionCode()));
            params.put("sdkVersion", String.valueOf(info.getSdkVersion()));
            params.put("androidVersion", info.getRelease());
            params.put("model", info.getModel());
            params.put("brand", info.getBrand());
            params.put("androidException", info.getThrowable().getMessage() + info.getException());
            OkHttpClient client = new OkHttpClient();
            FormBody.Builder builder = new FormBody.Builder();
            for (String key : params.keySet()) {
                builder.add(key, params.get(key));
            }
            RequestBody body = builder.build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("dora", e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                }
            });
        }
    }
}
