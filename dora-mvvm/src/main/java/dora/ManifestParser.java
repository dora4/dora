package dora;

import android.content.Context;

import dora.util.ManifestUtils;
import dora.util.ReflectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class ManifestParser {

    private static final String METADATA_VALUE = "GlobalConfig";
    private final Context context;

    public ManifestParser(Context context) {
        this.context = context;
    }

    private static GlobalConfig parseModule(String className) {
        Object config = ReflectionUtils.newInstance(className);
        if (!(config instanceof GlobalConfig)) {
            throw new RuntimeException("Expected instanceof GlobalConfig, but found: " + config);
        }
        return (GlobalConfig) config;
    }

    public List<GlobalConfig> parse() {
        List<GlobalConfig> modules = new ArrayList<>();
        Set<String> keySet = ManifestUtils.getApplicationMetadataKeyWhileValueEquals(context, METADATA_VALUE);
        for (String key : keySet) {
            modules.add(parseModule(key));
        }
        return modules;
    }
}