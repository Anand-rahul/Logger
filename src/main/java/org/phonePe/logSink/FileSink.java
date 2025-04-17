package org.phonePe.logSink;

import org.phonePe.model.LogMessage;

import java.io.*;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

public class FileSink implements LogSink{
    private final String filePath;
    private final String timestampFormat;
    private Writer writer;
    private final long maxFileSize;
    private final int maxBackupFiles;
    private long currentFileSize;
    private final File logFile;

    public FileSink(Map<String, String> config) throws IOException {
        this.filePath = config.getOrDefault("file_location", "application.log");
        this.timestampFormat = config.getOrDefault("ts_format", "yyyy-MM-dd HH:mm:ss,SSS");
        this.maxFileSize = Long.parseLong(config.getOrDefault("max_file_size", "10485760")); // Default: 10MB
        this.maxBackupFiles = Integer.parseInt(config.getOrDefault("max_backup_files", "5"));

        this.logFile = new File(filePath);
        if (!logFile.exists()) {
            File parent = logFile.getParentFile();
            if (parent != null) parent.mkdirs();
            logFile.createNewFile();
        }

        this.currentFileSize = logFile.length();
        this.writer = new BufferedWriter(new FileWriter(logFile, true));
    }
    @Override
    public void append(LogMessage logMessage) {
        try {
            logMessage.formatTimestamp(timestampFormat);

            StringBuilder sb = new StringBuilder();
            sb.append(logMessage.getLevel()).append(" [").append(logMessage.getFormattedTimestamp()).append("] ");

            if (logMessage.getHostName() != null) {
                sb.append("[").append(logMessage.getHostName()).append("] ");
            }

            if (logMessage.getTrackingId() != null) {
                sb.append("[").append(logMessage.getTrackingId()).append("] ");
            }

            sb.append("[").append(logMessage.getNamespace()).append("] ");
            sb.append(logMessage.getContent()).append(System.lineSeparator());

            String logEntry = sb.toString();

            // Check if rotation is needed
            if (currentFileSize + logEntry.length() > maxFileSize) {
                rotateLogFile();
            }

            writer.write(logEntry);
            writer.flush();
            currentFileSize = logFile.length();
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
        }
    }

    @Override
    public void close() {
        try {
            writer.close();
        } catch (IOException e) {
            System.err.println("Error closing file sink: " + e.getMessage());
        }
    }
    private void rotateLogFile() throws IOException {
        writer.close();

        File oldestBackup = new File(filePath + "." + maxBackupFiles + ".gz");
        if (oldestBackup.exists()) {
            oldestBackup.delete();
        }

        for (int i = maxBackupFiles - 1; i > 0; i--) {
            File backupFile = new File(filePath + "." + i + ".gz");
            if (backupFile.exists()) {
                File newBackupFile = new File(filePath + "." + (i + 1) + ".gz");
                backupFile.renameTo(newBackupFile);
            }
        }

        File compressedFile = new File(filePath + ".1.gz");
        try (GZIPOutputStream gzipOut = new GZIPOutputStream(new FileOutputStream(compressedFile));
             FileInputStream fis = new FileInputStream(logFile)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                gzipOut.write(buffer, 0, bytesRead);
            }
        }

        new FileWriter(logFile).close();
        this.currentFileSize = 0;

        this.writer = new BufferedWriter(new FileWriter(logFile, true));
    }
}
