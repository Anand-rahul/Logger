package org.phonePe.logSink;

import org.phonePe.model.LogMessage;

import java.util.Map;

public class ConsoleSink implements LogSink{
    private final String timestampFormat;

    public ConsoleSink(Map<String, String> config) {
        this.timestampFormat = config.getOrDefault("ts_format", "yyyy-MM-dd HH:mm:ss,SSS");
    }
    @Override
    public void append(LogMessage message) {
        message.formatTimestamp(timestampFormat);

        StringBuilder sb = new StringBuilder();
        sb.append(message.getLevel()).append(" [").append(message.getFormattedTimestamp()).append("] ");

        if (message.getHostName() != null) {
            sb.append("[").append(message.getHostName()).append("] ");
        }

        if (message.getTrackingId() != null) {
            sb.append("[").append(message.getTrackingId()).append("] ");
        }

        sb.append("[").append(message.getNamespace()).append("] ");
        sb.append(message.getContent());

        System.out.println(sb.toString());
    }

    @Override
    public void close() {

    }
}
