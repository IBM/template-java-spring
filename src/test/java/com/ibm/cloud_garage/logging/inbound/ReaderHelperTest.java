package com.ibm.cloud_garage.logging.inbound;

import static java.lang.System.lineSeparator;
import static org.assertj.core.util.Strings.join;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("ReaderHelper")
public class ReaderHelperTest {

    @Nested
    @DisplayName("Given private constructor")
    class GivenPrivateConstructor {
        Constructor constructor;

        @BeforeEach
        void setup() {
            constructor = Stream.of(ReaderHelper.class.getDeclaredConstructors())
                    .filter(c -> !c.isAccessible())
                    .findFirst()
                    .orElse(null);
        }

        @Nested
        @DisplayName("When called with setAccessible(true)")
        class WhenCalledWithSetAccessibleTrue {
            @Test
            @DisplayName("Then it should create new instance")
            void thenItShouldCreateNewInstance()
                    throws IllegalAccessException, InvocationTargetException, InstantiationException {

                assertNotNull(constructor);
                constructor.setAccessible(true);

                assertNotNull(constructor.newInstance());
            }
        }
    }

    @Nested
    @DisplayName("Given readerToString()")
    class GivenReaderToString {
        BufferedReader reader;

        @BeforeEach
        void setup() {
            reader = mock(BufferedReader.class);
        }

        @Nested
        @DisplayName("When reader is null")
        class WhenReaderIsNull {
            @Test
            @DisplayName("Then return empty string")
            void thenReturnEmptyString() {
                assertEquals("", ReaderHelper.readerToString(null));
            }
        }

        @Nested
        @DisplayName("When reader contains 'test'")
        class WhenReaderContainsTest {
            @Test
            @DisplayName("Then return 'test'")
            void thenReturnTest() throws IOException {
                final String value = "test";
                when(reader.readLine()).thenReturn(value, (String) null);

                assertEquals(value, ReaderHelper.readerToString(reader));
            }
        }

        @Nested
        @DisplayName("When reader contains 'test\\nvalue'")
        class WhenReaderContainsTestValue {
            @Test
            @DisplayName("Then return 'test\\nvalue'")
            void thenReturnTest() throws IOException {
                final String value1 = "test";
                final String value2 = "value";
                when(reader.readLine()).thenReturn(value1, value2, (String) null);

                assertEquals(
                        join(value1, value2).with(lineSeparator()),
                        ReaderHelper.readerToString(reader));
            }
        }

        @Nested
        @DisplayName("When reader throws an exception")
        class WhenReaderThrowsAnException {
            @Test
            @DisplayName("Then throw exception")
            void thenThrowException() throws IOException {
                when(reader.readLine()).thenThrow(IOException.class);

                assertThrows(ReaderHelper.LineReadError.class, () -> {
                    ReaderHelper.readerToString(reader);
                });
            }
        }
    }

    @Nested
    @DisplayName("Given readerToByteArray()")
    class GivenReaderToByteArray {
        @Nested
        @DisplayName("When reader contains 'test'")
        class WhenReaderContainsTest {
            @Test
            @DisplayName("Then it should return byte[] for 'test'")
            void thenItShouldReturnByteForTest() throws IOException {
                final String value = "test";
                BufferedReader reader = mock(BufferedReader.class);
                when(reader.readLine()).thenReturn(value, (String) null);

                byte[] actual = ReaderHelper.readerToByteArray(reader);

                assertEquals(value, new String(actual));
            }
        }
    }
}
