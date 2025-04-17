package org.phonePe.configProvider;

import java.util.HashMap;
import java.util.Map;

public class MapConfigProvider implements ConfigProvider{
    private final Map<String, String> config;

    public MapConfigProvider(Map<String, String> config) {
        this.config = new HashMap<>(config);
    }
    @Override
    public Map<String, String> getConfig() {
        return config;
    }
}
