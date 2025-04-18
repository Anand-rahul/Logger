package org.phonePe;

import org.phonePe.factoryImpl.SinkFactory;
import org.phonePe.logSinkProvider.ConsoleSinkProvider;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoggerDemo {
    public static void main(String[] args) throws IOException {
        Map<String, String> config = new HashMap<>();
        config.put("ts_format", "yyyy-MM-dd HH:mm");
        config.put("log_level", "DEBUG");
        config.put("sink_type", "FILE");
        config.put("file_location", "var/log/app/application.log");
        config.put("max_file_size", "5242880"); // 5MB
        config.put("max_backup_files", "3");
        config.put("thread_model", "SINGLE");
        config.put("write_mode", "SYNC");
        config.put("tracking_id", "demo-app-123");

        //Logger.getInstance().init(config);
        Logger.getInstance().init("D:\\codeDump\\jvm\\Logger\\Logger\\src\\main\\resources\\flieconfig.txt");

        Logger authLogger = Logger.getInstance().getLogger("auth");
        Logger dataLogger = Logger.getInstance().getLogger("data");

        dataLogger.debug("Just tested and it working fine");
        authLogger.info("User authenticated successfully");
        dataLogger.warn("Data validation warning: missing optional field");
        authLogger.error("Authentication failed: invalid credentials");

        //SinkFactory.registerProvider(new ConsoleSinkProvider());

        Logger.getInstance().close();
    }
}