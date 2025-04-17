package org.phonePe.factoryImpl;

import org.phonePe.model.LogLevel;
import org.phonePe.model.LogMessage;

public class LogMessageFactory {
    private final String trackingId;
    private final String hostName;

    public LogMessageFactory(String trackingId, String hostName) {
        this.trackingId = trackingId;
        this.hostName = hostName != null ? hostName : "localhost";
    }

    public LogMessage createMessage(String content, LogLevel level, String namespace) {
        return new LogMessage(content, level, namespace, trackingId, hostName);
    }
}
