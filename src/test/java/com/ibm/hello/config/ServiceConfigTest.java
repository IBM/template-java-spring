package com.ibm.hello.config;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.ibm.hello.service.ServiceName;

public class ServiceConfigTest {

    @Nested
    @DisplayName("Given getServiceNameFromString()")
    class GivenGetServiceNameFromString {
        @Nested
        @DisplayName("When invalid service name provided")
        class WhenInvalidServiceNameProvided {
            @Test
            @DisplayName("Then throw exception")
            void thenThrowException() {

                assertThrows(IllegalStateException.class, () -> {
                    ServiceConfig.getServiceNameFromString(null);
                });
            }
        }

        @Nested
        @DisplayName("When valid service name provided")
        class WhenValidServiceNameProvided {
            @Test
            @DisplayName("Then return ServiceName instance")
            void thenReturnServiceNameInstance() {

                ServiceName expectedValue = ServiceName.HELLO;
                assertSame(expectedValue, ServiceConfig.getServiceNameFromString(expectedValue.simpleName()));
            }
        }
    }
}
