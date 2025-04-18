package org.phonePe;

import org.phonePe.configProvider.ConfigProvider;
import org.phonePe.configProvider.FileConfigProvider;
import org.phonePe.configProvider.MapConfigProvider;
import org.phonePe.model.LogLevel;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Logger {
    private static Logger instance;
    private static LoggerContext context;
    private String namespace;
    private static final Map<String, Logger> namespaceLoggers = new HashMap<>();

    private Logger() {
    }

    // Private constructor for namespace-specific loggers
    private Logger(String namespace) {
        this.namespace = namespace;
    }

    /**
     * Gets the singleton instance of Logger
     */
    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    public void init(ConfigProvider configProvider) throws IOException {
        if (context != null) {
            context.close();
        }
        context = new LoggerContext(configProvider);
    }

    public void init(String configFilePath) throws IOException {
        init(new FileConfigProvider(configFilePath));
    }

    public void init(Map<String, String> config) throws IOException {
        init(new MapConfigProvider(config));
    }

    public Logger getLogger(String namespace) {
        if (context == null) {
            try {
                init(new MapConfigProvider(Collections.singletonMap("sink_type", "CONSOLE")));
            } catch (IOException e) {
                throw new RuntimeException("Failed to initialize logger with default configuration", e);
            }
        }

        // Return existing namespace logger or create a new one
        if (!namespaceLoggers.containsKey(namespace)) {
            namespaceLoggers.put(namespace, new Logger(namespace));
        }
        return namespaceLoggers.get(namespace);
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
    public void close() {
        if (context != null) {
            context.close();
            System.out.println("Closed Logger");
            context = null;
            namespaceLoggers.clear();
        }
    }
}
