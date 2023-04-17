package dora.util;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import dora.lifecycle.config.GlobalConfig;

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