package org.phonePe.logSinkProvider;

import org.phonePe.logSink.HttpSink;
import org.phonePe.logSink.LogSink;

import java.io.IOException;
import java.util.*;
public class HttpSinkProvider implements LogSinkProvider {
    @Override
    public String getSinkType() {
        return "HTTP";
    }

    @Override
    public LogSink createSink(Map<String, String> config) throws IOException {
        return new HttpSink(config);
    }
}
