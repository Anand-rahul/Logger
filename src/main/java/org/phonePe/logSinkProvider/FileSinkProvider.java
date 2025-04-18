package org.phonePe.logSinkProvider;

import org.phonePe.logSink.FileSink;
import org.phonePe.logSink.LogSink;

import java.io.IOException;
import java.util.*;
public class FileSinkProvider implements LogSinkProvider {
    @Override
    public String getSinkType() {
        return "FILE";
    }
    @Override
    public LogSink createSink(Map<String, String> config) throws IOException {
        return new FileSink(config);
    }
}
