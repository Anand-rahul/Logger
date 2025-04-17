package org.phonePe.configProvider;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileConfigProvider implements ConfigProvider{
    private final Map<String, String> config = new HashMap<>();

    public FileConfigProvider(String filePath) throws IOException {
        try (var reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() ) continue;
                int idx = line.indexOf(':');
                if (idx < 0) continue;
                String key = line.substring(0, idx).trim();
                String val = line.substring(idx+1).trim();
                config.put(key, val);
            }
        }
    }

    @Override
    public Map<String, String> getConfig() {
        return config;
    }
}
