package org.phonePe.logSinkProvider;

import org.phonePe.logSink.ConsoleSink;
import org.phonePe.logSink.LogSink;

import java.io.IOException;
import java.util.*;
public class ConsoleSinkProvider implements LogSinkProvider {
    @Override
    public String getSinkType() {
        return "CONSOLE";
    }

    @Override
    public LogSink createSink(Map<String, String> config) throws IOException {
        return new ConsoleSink(config);
    }
}
