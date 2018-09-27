package com.ibm.hello.service;

import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class ServiceNameTest {

    @Nested
    @DisplayName("Given safeValueOf()")
    class GivenSafeValueOfStringServiceName {
        final ServiceName defaultValue = ServiceName.HELLO;

        @Nested
        @DisplayName("When null name provided")
        class WhenNullNameProvided {
            @Test
            @DisplayName("Then return default")
            void thenReturnDefault() {

                assertSame(defaultValue, ServiceName.safeValueOf(null, defaultValue));
            }
        }

        @Nested
        @DisplayName("When invalid name provided")
        class WhenInvalidNameProvided {
            @Test
            @DisplayName("Then return default")
            void thenReturnDefault() {

                assertSame(defaultValue, ServiceName.safeValueOf("bogus", defaultValue));
            }
        }

        @Nested
        @DisplayName("When valid name provided")
        class WhenValidNameProvided {
            @Test
            @DisplayName("Then return value")
            void thenReturnValue() {

                final ServiceName expectedValue = ServiceName.HOLA;
                assertSame(expectedValue, ServiceName.safeValueOf(expectedValue.simpleName(), defaultValue));
            }
        }

        @Nested
        @DisplayName("When valid name having wrong case provided")
        class WhenValidNameHavingWrongCaseProvided {
            @Test
            @DisplayName("Then return value")
            void thenReturnValue() {

                final ServiceName expectedValue = ServiceName.HOLA;
                final String serviceNameString = "HoLa";

                assertSame(expectedValue, ServiceName.safeValueOf(serviceNameString, defaultValue));
            }
        }
    }
}
