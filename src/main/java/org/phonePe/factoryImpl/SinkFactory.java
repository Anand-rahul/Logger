package org.phonePe.factoryImpl;

import org.phonePe.logSink.*;
import org.phonePe.logSinkProvider.*;

import java.io.IOException;
import java.util.*;

//public class SinkFactory {
//    public static LogSink createSink(String sinkType, Map<String, String> config) throws IOException {
//        return switch (sinkType.toUpperCase()) {
//            case "FILE" -> new FileSink(config);
//            case "CONSOLE" -> new ConsoleSink(config);
//            case "DB" -> new DatabaseSink(config);
//            case "HTTP" -> new HttpSink(config);
//            default -> throw new IllegalArgumentException("Unsupported sink type: " + sinkType);
//        };
//    }
//}
public class SinkFactory {
    private static final Map<String, LogSinkProvider> providers = new HashMap<>();
    private static boolean initialized = false;

    private static synchronized void init() {
        if (!initialized) {
            // Load built-in providers
            registerBuiltInProviders();

            ServiceLoader<LogSinkProvider> serviceLoader = ServiceLoader.load(LogSinkProvider.class);
            for (LogSinkProvider provider : serviceLoader) {
                registerProvider(provider);
            }

            initialized = true;
        }
    }
    private static void registerBuiltInProviders() {
        registerProvider(new FileSinkProvider());
        registerProvider(new ConsoleSinkProvider());
        registerProvider(new DatabaseSinkProvider());
        registerProvider(new HttpSinkProvider());
    }

    public static void registerProvider(LogSinkProvider provider) {
        providers.put(provider.getSinkType().toUpperCase(), provider);
    }

    public static LogSink createSink(String sinkType, Map<String, String> config) throws IOException {
        if (!initialized) {
            init();
        }

        String type = sinkType.toUpperCase();
        LogSinkProvider provider = providers.get(type);

        if (provider == null) {
            throw new IllegalArgumentException("Unsupported sink type: " + sinkType);
        }

        return provider.createSink(config);
    }
}
