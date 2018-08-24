package com.ibm.cloud_garage.logging.inbound;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResettableHttpServletRequest extends HttpServletRequestWrapper {
    private static Logger logger = LoggerFactory.getLogger(ResettableHttpServletRequest.class);

    private final SimpleServletInputStream servletStream;
    private byte[] rawData;

    public ResettableHttpServletRequest(ServletRequest request) {
        this((HttpServletRequest) request);
    }

    public ResettableHttpServletRequest(HttpServletRequest request) {
        super(request);

        this.servletStream = new SimpleServletInputStream();
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (!inputStreamHasBeenRead()) {
            rawData = ReaderHelper.readerToByteArray(super.getReader());
            servletStream.setInputStreamData(rawData);
        }

        return servletStream;
    }

    public void resetInputStream() {
        if (inputStreamHasBeenRead()) {
            servletStream.setInputStreamData(rawData);
        }
    }

    protected boolean inputStreamHasBeenRead() {
        return rawData != null;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream(), getCharacterEncoding()));
    }

    protected void setReadListener(ReadListener listener) {
        try {
            super.getInputStream().setReadListener(listener);
        } catch (IOException e) {
            logger.error("Unable to get input stream to set read listener", e);
        }
    }

    protected class SimpleServletInputStream extends ServletInputStream {
        private InputStream inputStream;

        public void setInputStreamData(byte[] rawData) {
            setInputStream(new ByteArrayInputStream(rawData));
        }

        public void setInputStream(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        @Override
        public boolean isFinished() {
            try {
                return inputStream.available() == -1;
            } catch (IOException ex) {
                return true;
            }
        }

        @Override
        public boolean isReady() {
            try {
                return inputStream.available() != -1;
            } catch (IOException ex) {
                return false;
            }
        }

        @Override
        public void setReadListener(ReadListener listener) {
            ResettableHttpServletRequest.this.setReadListener(listener);
        }

        @Override
        public int read() throws IOException {
            return inputStream.read();
        }

        @Override
        public int read(byte[] bytes) throws IOException {
            return inputStream.read(bytes);
        }

        @Override
        public int read(byte[] bytes, int i, int i1) throws IOException {
            return inputStream.read(bytes, i, i1);
        }

        @Override
        public long skip(long l) throws IOException {
            return inputStream.skip(l);
        }

        @Override
        public int available() throws IOException {
            return inputStream.available();
        }

        @Override
        public void close() throws IOException {
            inputStream.close();
        }

        @Override
        public void mark(int i) {
            inputStream.mark(i);
        }

        @Override
        public void reset() throws IOException {
            inputStream.reset();
        }

        @Override
        public boolean markSupported() {
            return inputStream.markSupported();
        }
    }
}
