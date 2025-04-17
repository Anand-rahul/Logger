package org.phonePe.logSink;

import org.phonePe.model.LogMessage;

public interface LogSink {
    void append(LogMessage logMessage);

    void close();
}
