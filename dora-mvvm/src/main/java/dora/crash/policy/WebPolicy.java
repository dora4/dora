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

import java.io.IOException;
import java.util.HashMap;

import dora.crash.CrashInfo;
import dora.crash.group.Group;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * I make it out of okhttp3 network framework.
 * 我以网络框架okhttp3为原材料制作了它。
 */
public class WebPolicy extends WebPolicyBase {

    private HashMap<String, String> mRequestParams;

    public WebPolicy(String url, HashMap<String, String> params) {
        super(url);
        this.mRequestParams = params;
    }

    public WebPolicy(String url, HashMap<String, String> params, CrashReportPolicy policy) {
        super(url, policy);
        this.mRequestParams = params;
    }

    public WebPolicy(String url, HashMap<String, String> params, Group group) {
        super(url, group);
        this.mRequestParams = params;
    }

    public WebPolicy(String url, HashMap<String, String> params, Group group, CrashReportPolicy policy) {
        super(url, group, policy);
        this.mRequestParams = params;
    }

    @Override
    public void sendCrashInfoToWeb(String url, CrashInfo info, Group group) {
        if (group.counts()) {
            OkHttpClient client = new OkHttpClient();
            FormBody.Builder builder = new FormBody.Builder();
            for (String key : mRequestParams.keySet()) {
                builder.add(key, mRequestParams.get(key));
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
