package org.phonePe.logSinkProvider;

import org.phonePe.logSink.LogSink;
import java.io.IOException;
import java.util.*;
public interface LogSinkProvider {
    String getSinkType();
    LogSink createSink(Map<String, String> config) throws IOException;
}
