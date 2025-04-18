package org.phonePe.logSinkProvider;

import org.phonePe.logSink.DatabaseSink;
import org.phonePe.logSink.LogSink;

import java.io.IOException;
import java.util.*;
public class DatabaseSinkProvider implements LogSinkProvider {
    @Override
    public String getSinkType() {
        return "DB";
    }

    @Override
    public LogSink createSink(Map<String, String> config) throws IOException {
        return new DatabaseSink(config);
    }
}
