package org.phonePe;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoggerDemo {
    public static void main(String[] args) throws IOException {

        Map<String, String> config = new HashMap<>();
        config.put("ts_format", "yyyy-MM-dd HH:mm");
        config.put("log_level", "DEBUG");
        config.put("sink_type", "CONSOLE");
        config.put("file_location", "logs/application.log");
        config.put("max_file_size", "5242880"); // 5MB
        config.put("max_backup_files", "3");
        config.put("thread_model", "SINGLE");
        config.put("write_mode", "SYNC");
        config.put("tracking_id", "demo-app-123");

        //Logger.init("F:\\CodeDump\\java\\Logger\\src\\main\\resources\\flieconfig.txt");

        Logger.init(config);

        // Get a logger for a specific namespace
        Logger authLogger = Logger.getLogger("auth");
        Logger dataLogger = Logger.getLogger("data");

        dataLogger.debug("Just tested and it working fine");
        authLogger.info("User authenticated successfully");
        dataLogger.warn("Data validation warning: missing optional field");
        authLogger.error("Authentication failed: invalid credentials");

        Logger.close();
    }
}