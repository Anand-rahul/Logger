package org.phonePe.factoryImpl;

import org.phonePe.logSink.*;

import java.io.IOException;
import java.util.Map;

public class SinkFactory {
    public static LogSink createSink(String sinkType, Map<String, String> config) throws IOException {
        return switch (sinkType.toUpperCase()) {
            case "FILE" -> new FileSink(config);
            case "CONSOLE" -> new ConsoleSink(config);
            case "DB" -> new DatabaseSink(config);
            case "HTTP" -> new HttpSink(config);
            default -> throw new IllegalArgumentException("Unsupported sink type: " + sinkType);
        };
    }
}
