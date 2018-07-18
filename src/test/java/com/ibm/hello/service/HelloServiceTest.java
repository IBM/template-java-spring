package com.ibm.hello.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.springframework.test.util.ReflectionTestUtils;

import com.ibm.hello.config.HelloConfig;

@DisplayName("HelloService")
public class HelloServiceTest {
    @Nested
    @DisplayName("Given createReply()")
    public class GivenCreateReply {
        String pattern;
        HelloService service;

        @BeforeEach
        public void setup() {
            service = new HelloService();

            pattern = "Hello there, %s!";

            ReflectionTestUtils.setField(service, "config", new HelloConfig().withPattern(pattern));
        }

        @Test
        @DisplayName("When name is `test` then return `Hello, test!`")
        public void when_name_is_test_return_hellotest() {
            final String name = "test";

            assertEquals(String.format(pattern, name), service.createReply(name));
        }

        @Test
        @DisplayName("When name is null then return `Hello, world!`")
        public void when_name_is_null_return_helloworld() {

            assertEquals(String.format(pattern, "world"), service.createReply(null));
        }
    }
}
