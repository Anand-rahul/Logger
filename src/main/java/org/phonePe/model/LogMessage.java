package org.phonePe.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogMessage {
    private final String content;
    private final LogLevel level;
    private final String namespace;
    private final Date timestamp;
    private String formattedTimestamp;
    private final String trackingId;
    private final String hostName;

    public LogMessage(String content, LogLevel level, String namespace) {
        this(content, level, namespace, null, null);
    }

    public LogMessage(String content, LogLevel level, String namespace, String trackingId, String hostName) {
        this.content = content;
        this.level = level;
        this.namespace = namespace;
        this.timestamp = new Date();
        this.trackingId = trackingId;
        this.hostName = hostName;
    }

    public String getContent() {
        return content;
    }

    public LogLevel getLevel() {
        return level;
    }

    public String getNamespace() {
        return namespace;
    }

    public void formatTimestamp(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        this.formattedTimestamp = sdf.format(timestamp);
    }

    public String getFormattedTimestamp() {
        return formattedTimestamp;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public String getHostName() {
        return hostName;
    }
}
