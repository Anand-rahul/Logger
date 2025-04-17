package org.phonePe;

import org.phonePe.configProvider.ConfigProvider;
import org.phonePe.configProvider.FileConfigProvider;
import org.phonePe.configProvider.MapConfigProvider;
import org.phonePe.model.LogLevel;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

public class Logger {
    private static LoggerContext context;
    private final String namespace;

    private Logger(String namespace) {
        this.namespace = namespace;
    }


    public static void init(ConfigProvider configProvider) throws IOException {
        if (context != null) {
            context.close();
        }
        context = new LoggerContext(configProvider);
    }

    public static void init(String configFilePath) throws IOException {
        init(new FileConfigProvider(configFilePath));
    }

    public static void init(Map<String, String> config) throws IOException {
        init(new MapConfigProvider(config));
    }

    public static Logger getLogger(String namespace) {
        if (context == null) {
            try {
                init(new MapConfigProvider(Collections.singletonMap("sink_type", "CONSOLE")));
            } catch (IOException e) {
                throw new RuntimeException("Failed to initialize logger with default configuration", e);
            }
        }
        return new Logger(namespace);
    }

    // Log methods for different levels
    public void debug(String message) {
        log(message, LogLevel.DEBUG);
    }

    public void info(String message) {
        log(message, LogLevel.INFO);
    }

    public void warn(String message) {
        log(message, LogLevel.WARN);
    }

    public void error(String message) {
        log(message, LogLevel.ERROR);
    }

    public void fatal(String message) {
        log(message, LogLevel.FATAL);
    }

    private void log(String message, LogLevel level) {
        if (context != null) {
            context.log(message, level, namespace);
        }
    }

    public static void close() {
        if (context != null) {
            context.close();
            System.out.println("Closed Logger");
            context = null;
        }
    }
}
