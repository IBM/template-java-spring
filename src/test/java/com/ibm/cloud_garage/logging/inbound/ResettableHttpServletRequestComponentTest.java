package com.ibm.cloud_garage.logging.inbound;

import static com.ibm.cloud_garage.logging.inbound.ReaderHelper.readerToString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;


@Tag("component")
@DisplayName("ResettableHttpServletRequest")
public class ResettableHttpServletRequestComponentTest {
    HttpServletRequest requestMock;
    ResettableHttpServletRequest classUnderTest;

    @BeforeEach
    void setup() {
        requestMock = mock(HttpServletRequest.class);
        classUnderTest = spy(new ResettableHttpServletRequest(requestMock));
    }

    @DisplayName("Given ResettableHttpServletRequest component")
    @Nested
    class GivenResettableHttpServletRequestComponent {
        final String inputString = "input text";

        @BeforeEach
        void setup() throws IOException {
            when(requestMock.getReader()).thenReturn(new BufferedReader(new StringReader(inputString)));
            when(requestMock.getCharacterEncoding()).thenReturn("UTF-8");
        }

        @DisplayName("When Raeder read once")
        @Nested class WhenReaderReadOnce {
            @DisplayName("Then it will return stream data")
            @Test
            void thenItWillReturnStreamData() throws IOException {
                BufferedReader reader = classUnderTest.getReader();

                assertEquals(inputString, readerToString(reader));
            }
        }

        @DisplayName("When Reader read twice")
        @Nested
        class WhenReaderReadTwice {
            @BeforeEach
            public void setup() throws IOException {
            }

            @DisplayName("Then it will return empty string")
            @Test void thenItWillReturnEmptyString() throws IOException {
                final BufferedReader firstReader = classUnderTest.getReader();

                readerToString(firstReader);

                BufferedReader secondReader = classUnderTest.getReader();

                assertEquals("", readerToString(secondReader));
            }
        }

        @DisplayName("When resetInputStream called between reading the stream")
        @Nested
        class WhenResetInputStreamCalledBetweenReadingTheStream {
            @DisplayName("Then it will return stream data")
            @Test void thenItWillReturnStreamData() throws IOException {
                final BufferedReader firstReader = classUnderTest.getReader();

                readerToString(firstReader);

                classUnderTest.resetInputStream();

                BufferedReader secondReader = classUnderTest.getReader();

                assertEquals(inputString, readerToString(secondReader));
            }
        }
    }
}
