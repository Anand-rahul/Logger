package org.phonePe.logSink;

import org.phonePe.model.LogMessage;

import java.util.Map;

public class DatabaseSink implements LogSink{
    private final String timestampFormat;
    private final String dbHost;
    private final String dbPort;

    public DatabaseSink(Map<String, String> config) {
        this.timestampFormat = config.getOrDefault("ts_format", "yyyy-MM-dd HH:mm:ss,SSS");
        this.dbHost = config.getOrDefault("dbhost", "localhost");
        this.dbPort = config.getOrDefault("dbport", "3306");

        System.out.println("Connecting to database at " + dbHost + ":" + dbPort);
    }

    @Override
    public void append(LogMessage message) {
        message.formatTimestamp(timestampFormat);

        System.out.println("Writing to database: " + message.getLevel() + ": " + message.getContent());
    }

    @Override
    public void close() {
        System.out.println("Closing database connection");
    }
}
