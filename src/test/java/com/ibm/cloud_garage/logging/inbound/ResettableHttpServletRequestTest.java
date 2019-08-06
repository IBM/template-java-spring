package com.ibm.cloud_garage.logging.inbound;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

@DisplayName("ResettableHttpServletRequest")
public class ResettableHttpServletRequestTest {
    HttpServletRequest requestMock;
    ResettableHttpServletRequest.SimpleServletInputStream streamMock;
    ResettableHttpServletRequest classUnderTest;

    @BeforeEach
    public void setup() {
        requestMock = mock(HttpServletRequest.class);
        classUnderTest = spy(new ResettableHttpServletRequest(requestMock));

        streamMock = mock(ResettableHttpServletRequest.SimpleServletInputStream.class);
        ReflectionTestUtils.setField(classUnderTest, "servletStream", streamMock);
    }

    @DisplayName("Given getInputStream()")
    @Nested class GivenGetInputStream {
        @DisplayName("When input stream has not been read")
        @Nested class WhenInputStreamHasNotBeenRead {
            @DisplayName("Then initialize and return servlet stream")
            @Test void thenInitializeAndReturnServletStream() throws IOException {
                doReturn(false).when(classUnderTest).inputStreamHasBeenRead();

                final String testString = "test";
                BufferedReader reader = new BufferedReader(new StringReader(testString));
                when(requestMock.getReader()).thenReturn(reader);

                ServletInputStream actualStream = classUnderTest.getInputStream();

                assertEquals(streamMock, actualStream);
                verify(requestMock).getReader();
                verify(streamMock).setInputStreamData(testString.getBytes());
            }
        }

        @DisplayName("When input stream has been read")
        @Nested class WhenInputStreamHasBeenRead {
            @DisplayName("Then just return servlet stream")
            @Test void thenJustReturnServletStream() throws IOException {
                doReturn(true).when(classUnderTest).inputStreamHasBeenRead();

                ServletInputStream actualStream = classUnderTest.getInputStream();

                assertEquals(streamMock, actualStream);
                verify(requestMock, times(0)).getReader();
                verify(streamMock, times(0)).setInputStreamData(any());
            }
        }
    }

    @DisplayName("Given resetInputStream()")
    @Nested class GivenResetInputStream {
        @DisplayName("When input stream has been read")
        @Nested class WhenInputStreamHasBeenRead {
            @DisplayName("Then create a new input stream")
            @Test void thenCreateANewInputStream() {
                final byte[] rawData = "test".getBytes();
                ReflectionTestUtils.setField(classUnderTest, "rawData", rawData);

                doReturn(true).when(classUnderTest).inputStreamHasBeenRead();

                classUnderTest.resetInputStream();

                verify(streamMock).setInputStreamData(rawData);
            }
        }

        @Nested
        @DisplayName("When input stream has not been read")
        class WhenInputStreamHasNotBeenRead {
            @Test
            @DisplayName("Then do nothing")
            void thenDoNothing() {
                doReturn(false).when(classUnderTest).inputStreamHasBeenRead();

                classUnderTest.resetInputStream();

                verify(streamMock, times(0)).setInputStreamData(any());
            }
        }
    }

    @DisplayName("Given inputStreamHasBeenRead()")
    @Nested class GivenInputStreamHasBeenRead {
        @DisplayName("When rawData is null")
        @Nested class WhenRawDataIsNull {
            @DisplayName("Then return false")
            @Test void thenReturnFalse() {
                assertFalse(classUnderTest.inputStreamHasBeenRead());
            }
        }

        @DisplayName("When rawData is not null")
        @Nested class WhenRawDataIsNotNull {
            @DisplayName("Then return true")
            @Test void thenReturnTrue() {
                ReflectionTestUtils.setField(classUnderTest, "rawData", new byte[] {});

                assertTrue(classUnderTest.inputStreamHasBeenRead());
            }
        }
    }

    @DisplayName("Given getReader()")
    @Nested class GivenGetReader {
        @DisplayName("When called")
        @Nested class WhenCalled {

            @DisplayName("Then return a new reader from the inputStream and character encoding")
            @Test void thenReturnANewReaderFromTheInputStreamAndCharacterEncoding() throws IOException {
                final ServletInputStream inputStream = mock(ServletInputStream.class);
                doReturn(inputStream).when(classUnderTest).getInputStream();

                final String characterEncoding = "UTF-8";
                when(requestMock.getCharacterEncoding()).thenReturn(characterEncoding);

                classUnderTest.getReader();

                verify(classUnderTest).getInputStream();
                verify(requestMock).getCharacterEncoding();
            }
        }
    }

    @DisplayName("Given setReadListener()")
    @Nested class GivenSetReadListener {
        @DisplayName("When called with readListener")
        @Nested class WhenCalledWithReadListener {
            @DisplayName("Then set readListener on underlying stream")
            @Test void thenSetReadListenerOnUnderlyingStream() throws IOException {
                final ServletInputStream inputStream = mock(ServletInputStream.class);
                when(requestMock.getInputStream()).thenReturn(inputStream);

                final ReadListener readListener = mock(ReadListener.class);

                classUnderTest.setReadListener(readListener);

                verify(inputStream).setReadListener(readListener);
            }
        }

        @DisplayName("When getInputStream throws an exception")
        @Nested class WhenGetInputStreamThrowsAnException {
            @DisplayName("Then do nothing")
            @Test void thenDoNothing() throws IOException {
                when(requestMock.getInputStream()).thenThrow(IOException.class);

                classUnderTest.setReadListener(null);
            }
        }
    }

    @Nested
    @DisplayName("Given SimpleServletInputStream")
    class GivenSimpleServletInputStream {
        InputStream streamMock;
        ResettableHttpServletRequest.SimpleServletInputStream simpleStream;

        @BeforeEach
        void setup() {
            streamMock = mock(InputStream.class);

            simpleStream = classUnderTest.new SimpleServletInputStream();
            simpleStream.setInputStream(streamMock);
        }

        @Nested
        @DisplayName("Given isFinished()")
        class GivenIsFinished {
            @Nested
            @DisplayName("When underlying stream has available bytes")
            class WhenUnderlyingStreamHasAvailableBytes {
                @Test
                @DisplayName("Then return false")
                void thenReturnTrue() throws IOException {
                    when(streamMock.available()).thenReturn(1);

                    assertFalse(simpleStream.isFinished());
                }
            }

            @Nested
            @DisplayName("When underlying stream does not have available bytes")
            class WhenUnderlyingStreamDoesNotHaveAvailableBytes {
                @Test
                @DisplayName("Then return true")
                void thenReturnFalse() throws IOException {
                    when(streamMock.available()).thenReturn(-1);

                    assertTrue(simpleStream.isFinished());
                }
            }

            @Nested
            @DisplayName("When underlying stream throws an exception")
            class WhenUnderlyingStreamThrowsAnException {
                @Test
                @DisplayName("Then return true")
                void thenReturnTrue() throws IOException {
                    when(streamMock.available()).thenThrow(IOException.class);

                    assertTrue(simpleStream.isFinished());
                }
            }
        }

        @Nested
        @DisplayName("Given isReady()")
        class GivenIsReady {
            @Nested
            @DisplayName("When underlying stream has available bytes")
            class WhenUnderlyingStreamHasAvailableBytes {
                @Test
                @DisplayName("Then return true")
                void thenReturnTrue() throws IOException {
                    when(streamMock.available()).thenReturn(1);

                    assertTrue(simpleStream.isReady());
                }
            }

            @Nested
            @DisplayName("When underlying stream does not have available bytes")
            class WhenUnderlyingStreamDoesNotHaveAvailableBytes {
                @Test
                @DisplayName("Then return false")
                void thenReturnFalse() throws IOException {
                    when(streamMock.available()).thenReturn(-1);

                    assertFalse(simpleStream.isReady());
                }
            }

            @Nested
            @DisplayName("When underlying stream throws an exception")
            class WhenUnderlyingStreamThrowsAnException {
                @Test
                @DisplayName("Then return false")
                void thenReturnTrue() throws IOException {
                    when(streamMock.available()).thenThrow(IOException.class);

                    assertFalse(simpleStream.isReady());
                }
            }
        }

        @Nested
        @DisplayName("Given setReadListener()")
        class GivenSetReadListener {
            @Nested
            @DisplayName("When called")
            class WhenCalled {
                @Test
                @DisplayName("Then call outer setReadListener()")
                void thenCallOuterSetReadListener() {
                    final ReadListener readListener = mock(ReadListener.class);

                    doNothing().when(classUnderTest).setReadListener(any());

                    simpleStream.setReadListener(readListener);

                    verify(classUnderTest).setReadListener(readListener);
                }
            }
        }

        @Nested
        @DisplayName("Given read()")
        class GivenRead {
            @Nested
            @DisplayName("When called")
            class WhenCalled {
                @Test
                @DisplayName("Then call underlying stream")
                void thenCallUnderlyingStream() throws IOException {
                    int expected = 100;
                    when(streamMock.read()).thenReturn(expected);

                    assertEquals(expected, simpleStream.read());
                }
            }
        }

        @Nested
        @DisplayName("Given read(byte[])")
        class GivenReadByteArray {
            @Nested
            @DisplayName("When called")
            class WhenCalled {
                @Test
                @DisplayName("Then call underlying stream")
                void thenCallUnderlyingStream() throws IOException {
                    int expected = 99;
                    when(streamMock.read(any())).thenReturn(expected);

                    assertEquals(expected, simpleStream.read(new byte[] {}));
                }
            }
        }

        @Nested
        @DisplayName("Given read(byte[], int, int)")
        class GivenReadByteArrayIntInt {
            @Nested
            @DisplayName("When called")
            class WhenCalled {
                @Test
                @DisplayName("Then call underlying stream")
                void thenCallUnderlyingStream() throws IOException {
                    int expected = 98;
                    int start = 5;
                    int count = 10;
                    when(streamMock.read(any(), eq(start), eq(count))).thenReturn(expected);

                    assertEquals(expected, simpleStream.read(new byte[] {}, start, count));
                }
            }
        }

        @Nested
        @DisplayName("Given skip()")
        class GivenSkip {
            @Nested
            @DisplayName("When called")
            class WhenCalled {
                @Test
                @DisplayName("Then call underlying stream")
                void thenCallUnderlyingStream() throws IOException {
                    long expected = 97;
                    long count = 10;
                    when(streamMock.skip(count)).thenReturn(expected);

                    assertEquals(expected, simpleStream.skip(count));
                }
            }
        }

        @Nested
        @DisplayName("Given available()")
        class GivenAvailable {
            @Nested
            @DisplayName("When called")
            class WhenCalled {
                @Test
                @DisplayName("Then call underlying stream")
                void thenCallUnderlyingStream() throws IOException {
                    int expected = 96;
                    when(streamMock.available()).thenReturn(expected);

                    assertEquals(expected, simpleStream.available());
                }
            }
        }

        @Nested
        @DisplayName("Given close()")
        class GivenClose {
            @Nested
            @DisplayName("When called")
            class WhenCalled {
                @Test
                @DisplayName("Then call underlying stream")
                void thenCallUnderlyingStream() throws IOException {
                    simpleStream.close();

                    verify(streamMock).close();
                }
            }
        }

        @Nested
        @DisplayName("Given mark()")
        class GivenMark {
            @Nested
            @DisplayName("When called")
            class WhenCalled {
                @Test
                @DisplayName("Then call underlying stream")
                void thenCallUnderlyingStream() throws IOException {
                    int position = 20;
                    simpleStream.mark(position);

                    verify(streamMock).mark(position);
                }
            }
        }

        @Nested
        @DisplayName("Given reset()")
        class GivenReset {
            @Nested
            @DisplayName("When called")
            class WhenCalled {
                @Test
                @DisplayName("Then call underlying stream")
                void thenCallUnderlyingStream() throws IOException {
                    simpleStream.reset();

                    verify(streamMock).reset();
                }
            }
        }

        @Nested
        @DisplayName("Given markSupported()")
        class GivenMarkSupported {
            @Nested
            @DisplayName("When called")
            class WhenCalled {
                @Test
                @DisplayName("Then call underlying stream")
                void thenCallUnderlyingStream() {
                    when(streamMock.markSupported()).thenReturn(true);

                    assertTrue(simpleStream.markSupported());

                    verify(streamMock).markSupported();
                }
            }
        }
    }
}
