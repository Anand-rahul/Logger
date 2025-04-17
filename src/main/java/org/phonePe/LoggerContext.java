package org.phonePe;

import org.phonePe.configProvider.ConfigProvider;
import org.phonePe.factoryImpl.LogMessageFactory;
import org.phonePe.factoryImpl.SinkFactory;
import org.phonePe.logSink.LogSink;
import org.phonePe.model.LogLevel;
import org.phonePe.model.LogMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoggerContext {
    private final Map<LogLevel, List<LogSink>> sinks = new EnumMap<>(LogLevel.class);
    private final LogLevel thresholdLevel;
    private final ExecutorService executorService;
    private final boolean isAsync;
    private final LogMessageFactory messageFactory;

    public LoggerContext(ConfigProvider configProvider) throws IOException {
        Map<String, String> config = configProvider.getConfig();
        String timestampFormat = config.getOrDefault("ts_format", "yyyy-MM-dd HH:mm:ss,SSS");
        this.thresholdLevel = LogLevel.fromString(config.getOrDefault("log_level", "INFO"));
        String sinkType = config.getOrDefault("sink_type", "CONSOLE");
        String threadModel = config.getOrDefault("thread_model", "SINGLE");
        String writeMode = config.getOrDefault("write_mode", "SYNC");

        if ("MULTI".equalsIgnoreCase(threadModel)) {
            this.executorService = Executors.newFixedThreadPool(2);
        } else {
            this.executorService = Executors.newSingleThreadExecutor();
        }

        this.isAsync = "ASYNC".equalsIgnoreCase(writeMode);

        String trackingId = config.get("tracking_id");
        String hostName = config.get("host_name");
        this.messageFactory = new LogMessageFactory(trackingId, hostName);

        for (LogLevel level : LogLevel.values()) {
            if (level.isLoggable(thresholdLevel)) {
                String levelSpecificSinkType = config.getOrDefault("sink_type_" + level.name(), sinkType);
                LogSink sink = SinkFactory.createSink(levelSpecificSinkType, config);

                if (!sinks.containsKey(level)) {
                    sinks.put(level, new ArrayList<>());
                }
                sinks.get(level).add(sink);
            }
        }
    }
    public void log(String content, LogLevel level, String namespace) {
        if (!level.isLoggable(thresholdLevel) || !sinks.containsKey(level)) {
            return;
        }
        LogMessage message = messageFactory.createMessage(content, level, namespace);

        List<LogSink> sinksForLevel = sinks.get(level);

        if (isAsync) {
            executorService.submit(() -> writeToSinks(message, sinksForLevel));
        } else {
            writeToSinks(message, sinksForLevel);
        }
    }
    private void writeToSinks(LogMessage message, List<LogSink> sinksForLevel) {
        for (LogSink sink : sinksForLevel) {
            sink.append(message);
        }
    }

    public void close() {
        executorService.shutdown(); // can await the shutdown

        for (List<LogSink> sinksForLevel : sinks.values()) {
            for (LogSink sink : sinksForLevel) {
                sink.close();
            }
        }
    }
}
