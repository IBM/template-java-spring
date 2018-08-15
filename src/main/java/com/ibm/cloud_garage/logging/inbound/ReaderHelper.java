package com.ibm.cloud_garage.logging.inbound;

import java.io.BufferedReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// this functionality is available in apache.io package but
// didn't seem like it was worth bringing in that entire
// package just for this functionality
public class ReaderHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReaderHelper.class);

    public static String readerToString(BufferedReader reader) {
        StringBuffer buffer = new StringBuffer();

        try {
            for (String line = reader.readLine(); line != null; ) {
                buffer.append(line);

                line = reader.readLine();
                if (line != null) {
                    buffer.append(System.lineSeparator());
                }
            }
        } catch (IOException ex) {
            LOGGER.error("Error reading from stream: ", ex);
        }

        return buffer.toString();
    }

    public static byte[] readerToByteArray(BufferedReader reader) {
        return readerToString(reader).getBytes();
    }
}
