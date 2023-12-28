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

package dora.lifecycle.application;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import dora.lifecycle.config.GlobalConfig;
import dora.util.ManifestUtils;
import dora.util.ReflectionUtils;

public final class ManifestParser {

    private static final String METADATA_VALUE = "GlobalConfig";

    private static GlobalConfig parseModule(String className) {
        Object config = ReflectionUtils.newInstance(className);
        if (!(config instanceof GlobalConfig)) {
            throw new RuntimeException("Expected instanceof GlobalConfig, but found: " + config);
        }
        return (GlobalConfig) config;
    }

    public static List<GlobalConfig> parse(Context context) {
        List<GlobalConfig> modules = new ArrayList<>();
        Set<String> keySet = ManifestUtils.getApplicationMetadataKeyWhileValueEquals(context, METADATA_VALUE);
        for (String key : keySet) {
            modules.add(parseModule(key));
        }
        return modules;
    }
}