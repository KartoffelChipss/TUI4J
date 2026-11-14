package org.strassburger.tui4j.console;

import java.nio.charset.Charset;

final class StdoutCharset {
    private StdoutCharset() {
    }

    public static Charset get() {
        for (String key : new String[] {"stdout.encoding", "sun.stdout.encoding", "native.encoding"}) {
            String name = System.getProperty(key);
            if (name != null) {
                try {
                    return Charset.forName(name);
                } catch (IllegalArgumentException ignored) {
                    // unknown or illegal charset name, try the next key
                }
            }
        }
        return Charset.defaultCharset();
    }
}
