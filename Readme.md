# Logger Library

A simple, pluggable logging library for Java applications supporting multiple log levels, configurable sinks, and file rotation.

## Features

- Log levels: DEBUG, INFO, WARN, ERROR, FATAL
- Configurable timestamp format
- Pluggable sinks: FILE, CONSOLE, DB,HTTP
- File sink with size-based rotation and compressed backups
- Synchronous and asynchronous write modes
- Single- or multi-threaded models
- Easy configuration via properties file or Map

## Getting Started

### Installation

Include the source files under your project (e.g., in `src/main/java`). No external dependencies are required beyond Java SE.

## Configuration

You can configure the logger via a properties file or directly with a `Map<String, String>`.

**Example `loggerConfig.txt`:**
```properties
# timestamp format
ts_format:yyyy-MM-dd HH:mm:ss,SSS
# minimum level to log (Debug, Info, Warn, Error, Fatal)
log_level:INFO
# choose sink: FILE, CONSOLE, DB , HTTP  // Kafka to be added
sink_type:FILE
# file sink settings
file_location:var/log/app/application.log
max_file_size:10485760    # 10 MB
max_backup_files:5
# threading and write mode
thread_model:SINGLE        # SINGLE or MULTI
write_mode:SYNC            # SYNC or ASYNC
# optional metadata
tracking_id:app-123
host_name:server-01
```

**Or with code:**
```java
Map<String, String> config = new HashMap<>();
config.put("ts_format",        "yyyy-MM-dd HH:mm:ss,SSS");
config.put("log_level",        "INFO"); 
config.put("sink_type",        "FILE"); //FILE, CONSOLE, DB, HTTP
config.put("file_location",    "logs/app.log");
config.put("max_file_size",    "10485760");  // 10 MB considered
config.put("max_backup_files", "3"); //after 3 backups, the oldest will be deleted
config.put("thread_model",     "SINGLE");
config.put("write_mode",       "SYNC");
config.put("tracking_id",      "demo-app-123"); // optional
config.put("host_name",        "localhost"); // optional
Logger.init(config);
```

## Usage

```java
// Initialize with file or map
//Logger.getInstance().init(config);
Logger.getInstance().init("config-file");

// Obtain namespace-specific loggers
Logger authLogger = Logger.getInstance().getLogger("auth");//auth is namespace 
Logger dataLogger = Logger.getInstance().getLogger("data");//data is namespace

// Log messages
authLog.info("User authenticated successfully");
dataLog.warn("Missing optional field");
authLog.error("Authentication failed");

// Clean up
Logger.close();
```

## Extending with Custom Sinks

To add your own sink, implement `LogSink`:
```java
public class MySink implements LogSink {
    @Override
    public void append(LogMessage msg) {}
    @Override
    public void close() {  }
}
```

```java 
public class MySinkProvider implements LogSinkProvider {
    @Override
    public String getSinkType() {
        return "Custom";
    }

    @Override
    public LogSink createSink(Map<String, String> config) throws IOException {
        return new CustomSink(config);
    }
}
```

```java
SinkFactory.registerProvider(new MySinkProvider());
```

## Class Descriptions

### Core Components
- **Logger (Singleton)**
    - Implemented as a Singleton to ensure a single entry point for clients
    - getInstance() returns the singleton Logger instance
    - `init(...)` bootstraps the logging system
    - `getLogger(namespace)` returns a namespace‑specific logger
    - `close()` shuts down threads and closes all sinks
    - Manages namespace-specific loggers through an internal registry

- **LoggerContext**
    - Holds global settings (levels, format, thread/executor)
    - Chooses sync vs. async write mode
    - Routes each `LogMessage` to the configured sinks

- **LogLevel**
    - Enum of `DEBUG, INFO, WARN, ERROR, FATAL`
    - Encapsulates priority and `isLoggable(threshold)` logic

### Message Model
- **LogMessage**
    - Immutable data object for a single log entry
    - Fields:
        - `timestamp` (formatted)
        - `level`
        - `namespace`
        - `content` text
        - Optional `trackingId` & `hostName`

### Sinks & Extensions
- **LogSink**
    - Interface with two methods:
        - `append(LogMessage)`
        - `close()`

- **LogSinkProvider**
  - Interface with two methods:
    - `getSinkType()`
    - `createSink(Map<String, String> config)`

#### Built‑in Sink Implementations
- **FileSink**
    - Writes to a local file
    - Performs size‑based rotation and compresses old backups
- **ConsoleSink**
    - Writes to standard output (`System.out`)
- **DatabaseSink**
    - Stub that formats logs as SQL inserts
- **HttpSink**
    - Sends each log entry as a JSON HTTP POST

### Factory & Configuration
- **SinkFactory**
    - Instantiates the appropriate `LogSink` based on `sink_type`
- **ConfigProvider**
    - Interface for reading configuration key→value pairs
- **FileConfigProvider**
    - Reads `key:value` lines from a file, skipping blank/comment lines
- **MapConfigProvider**
    - Wraps a `Map<String,String>` to supply configuration in code  
