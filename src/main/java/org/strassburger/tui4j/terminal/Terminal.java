package org.strassburger.tui4j.terminal;

import java.nio.charset.Charset;

/**
 * Represents a terminal and its properties.
 */
public class Terminal {
    private static final long CACHE_DURATION_MS = 250;

    private TerminalSize.Size cachedSize;
    private long lastFetchTime;

    private final boolean ansiSupported;
    private final String osName;

    public Terminal() {
        this.cachedSize = TerminalSize.get();
        this.lastFetchTime = System.currentTimeMillis();
        this.osName = System.getProperty("os.name");
        this.ansiSupported = detectAnsiSupport();
    }

    /**
     * Returns the terminal size in columns and rows.
     */
    public TerminalSize.Size getSize() {
        long now = System.currentTimeMillis();
        if (cachedSize == null || (now - lastFetchTime) > CACHE_DURATION_MS) {
            cachedSize = TerminalSize.get();
            lastFetchTime = now;
        }
        return cachedSize;
    }

    /**
     * Returns true if ANSI escape codes are likely supported.
     */
    public boolean isAnsiSupported() {
        return ansiSupported;
    }

    /**
     * Returns the OS name.
     */
    public String getOsName() {
        return osName;
    }

    /**
     * Attempts to detect ANSI escape code support.
     */
    private boolean detectAnsiSupport() {
        // Windows 10 and later support ANSI codes natively
        if (osName.toLowerCase().contains("win")) {
            String ver = System.getProperty("os.version");
            try {
                String[] parts = ver.split("\\.");
                int major = Integer.parseInt(parts[0]);
                return major >= 10;
            } catch (Exception e) {
                return false;
            }
        }
        // Unix-like terminals should support ANSI codes
        return true;
    }

    @Override
    public String toString() {
        return "Terminal{" +
                "size=" + getSize() +
                ", ansiSupported=" + ansiSupported +
                ", osName='" + osName + '\'' +
                '}';
    }
}