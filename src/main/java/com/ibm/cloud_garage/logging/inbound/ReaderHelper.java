package com.ibm.cloud_garage.logging.inbound;

import static java.lang.System.lineSeparator;
import static java.util.stream.Collectors.joining;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// this functionality is available in apache.io package but
// didn't seem like it was worth bringing in that entire
// package just for this functionality
public final class ReaderHelper {

    private ReaderHelper() {
        super();
    }

    public static String readerToString(BufferedReader reader) {
        if (reader == null) {
            return "";
        }

        List<String> lines = new ArrayList<>();

        try {
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                lines.add(line);
            }
        } catch (IOException e) {
            throw new LineReadError(e);
        }

        return lines.stream().collect(joining(lineSeparator()));
    }

    public static byte[] readerToByteArray(BufferedReader reader) {
        return readerToString(reader).getBytes();
    }

    public static class LineReadError extends RuntimeException {
        public LineReadError(Throwable cause) {
            super(cause);
        }
    }
}
