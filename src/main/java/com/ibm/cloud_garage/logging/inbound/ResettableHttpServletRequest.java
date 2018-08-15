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

    private final HttpServletRequest request;
    private SimpleServletInputStream servletStream;

    private byte[] rawData;

    public ResettableHttpServletRequest(ServletRequest request) {
        this((HttpServletRequest) request);
    }

    public ResettableHttpServletRequest(HttpServletRequest request) {
        super(request);
        this.request = request;
        this.servletStream = new SimpleServletInputStream(request);
    }

    public void resetInputStream() {
        if (rawData != null) {
            servletStream.setInputStream(new ByteArrayInputStream(rawData));
        }
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (rawData == null) {
            rawData = ReaderHelper.readerToByteArray(this.request.getReader());
            servletStream.setInputStream(new ByteArrayInputStream(rawData));
        }

        return servletStream;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream(), getCharacterEncoding()));
    }

    private class SimpleServletInputStream extends ServletInputStream {
        private final HttpServletRequest originalRequest;
        private InputStream inputStream;

        public SimpleServletInputStream(HttpServletRequest originalRequest) {
            this.originalRequest = originalRequest;
        }

        public InputStream getInputStream() {
            return inputStream;
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
            try {
                originalRequest.getInputStream().setReadListener(listener);
            } catch (IOException e) {
                logger.error("Unable to getInputStream()");
            }
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
