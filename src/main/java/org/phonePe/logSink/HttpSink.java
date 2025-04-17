package org.phonePe.logSink;


import org.phonePe.model.LogMessage;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class HttpSink implements LogSink {
    private final URL endpoint;
    private final DateTimeFormatter tsFormatter;
    private final String trackingIdKey = "trackingId";
    private final String hostNameKey  = "hostName";

    public HttpSink(Map<String,String> config) throws IOException {
        String urlStr = config.getOrDefault("http_endpoint", "http://localhost:8080/logs");
        this.endpoint = new URL(urlStr);

        String fmt = config.getOrDefault("ts_format", "yyyy-MM-dd HH:mm:ss,SSS");
        this.tsFormatter = DateTimeFormatter.ofPattern(fmt);
    }

    @Override
    public void append(LogMessage msg) {
        // ensure timestamp is formatted
        msg.formatTimestamp(tsFormatter.toString());

        // build a minimal JSON payload
        StringBuilder sb = new StringBuilder();
        sb.append("{")
                .append("\"level\":\"").append(msg.getLevel()).append("\",")
                .append("\"timestamp\":\"").append(msg.getFormattedTimestamp()).append("\",")
                .append("\"namespace\":\"").append(msg.getNamespace()).append("\",")
                .append("\"message\":\"").append(msg.getContent().replace("\"","\\\"")).append("\"");
        if (msg.getTrackingId() != null) {
            sb.append(",\"").append(trackingIdKey).append("\":\"")
                    .append(msg.getTrackingId()).append("\"");
        }
        if (msg.getHostName() != null) {
            sb.append(",\"").append(hostNameKey).append("\":\"")
                    .append(msg.getHostName()).append("\"");
        }
        sb.append("}");

        byte[] payload = sb.toString().getBytes(StandardCharsets.UTF_8);

        try {
            HttpURLConnection conn = (HttpURLConnection) endpoint.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Content-Length", String.valueOf(payload.length));

            try (OutputStream os = conn.getOutputStream()) {
                os.write(payload);
            }

            int code = conn.getResponseCode();
            conn.disconnect();
        } catch (IOException e) {
            System.err.println("HttpSink error: " + e.getMessage());
        }
    }

    @Override
    public void close() {
        // nothing to do
    }
}
